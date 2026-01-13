package com.example.studysynaps

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.mlkit.vision.barcode.BarcodeScanner
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class ScanQrFragment : Fragment() {

    private lateinit var cameraExecutor: ExecutorService
    private lateinit var previewView: PreviewView
    private lateinit var barcodeScanner: BarcodeScanner

    private val cameraPermissionRequest =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                Toast.makeText(requireContext(), "Izin kamera diberikan", Toast.LENGTH_SHORT).show()
                startCamera()
            } else {
                Toast.makeText(requireContext(), "Izin kamera diperlukan untuk scan QR", Toast.LENGTH_LONG).show()
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_scan_qr, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        previewView = view.findViewById(R.id.camera_preview)
        cameraExecutor = Executors.newSingleThreadExecutor()

        val options = BarcodeScannerOptions.Builder()
            .setBarcodeFormats(Barcode.FORMAT_QR_CODE)
            .build()
        barcodeScanner = BarcodeScanning.getClient(options)

        checkCameraPermission()
    }

    private fun checkCameraPermission() {
        when {
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED -> {
                Log.d("ScanQrFragment", "Izin sudah ada, memulai kamera.")
                startCamera()
            }
            shouldShowRequestPermissionRationale(Manifest.permission.CAMERA) -> {
                Toast.makeText(requireContext(), "Izin kamera dibutuhkan untuk fitur ini.", Toast.LENGTH_LONG).show()
                cameraPermissionRequest.launch(Manifest.permission.CAMERA)
            }
            else -> {
                Log.d("ScanQrFragment", "Meminta izin kamera.")
                cameraPermissionRequest.launch(Manifest.permission.CAMERA)
            }
        }
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())

        cameraProviderFuture.addListener({
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
            val preview = Preview.Builder().build().also {
                it.setSurfaceProvider(previewView.surfaceProvider)
            }
            val imageAnalyzer = ImageAnalysis.Builder()
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build()
                .also {
                    it.setAnalyzer(cameraExecutor, object : ImageAnalysis.Analyzer {
                        @androidx.camera.core.ExperimentalGetImage
                        override fun analyze(imageProxy: ImageProxy) {
                            val mediaImage = imageProxy.image
                            if (mediaImage == null) {
                                imageProxy.close()
                                return
                            }

                            try {
                                val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)
                                barcodeScanner.process(image)
                                    .addOnSuccessListener { barcodes ->
                                        if (barcodes.isNotEmpty()) {
                                            // Stop scanner temporarily
                                            cameraExecutor.shutdown()
                                            
                                            val barcode = barcodes.first()
                                            val rawValue = barcode.rawValue ?: ""
                                            Log.d("ScanQrFragment", "QR Code: $rawValue")
                                            
                                            activity?.runOnUiThread {
                                                processQrCode(rawValue)
                                            }
                                        }
                                    }
                                    .addOnFailureListener { e ->
                                        Log.e("ScanQrFragment", "Gagal memproses barcode", e)
                                    }
                                    .addOnCompleteListener { 
                                        imageProxy.close()
                                    }
                            } catch (e: Exception) {
                                Log.e("ScanQrFragment", "Error", e)
                                imageProxy.close()
                            }
                        }
                    })
                }
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    this.viewLifecycleOwner, cameraSelector, preview, imageAnalyzer
                )
            } catch (exc: Exception) {
                Log.e("ScanQrFragment", "Gagal mengikat use case kamera", exc)
            }
        }, ContextCompat.getMainExecutor(requireContext()))
    }

    private fun processQrCode(qrContent: String) {
        val session = com.example.studysynaps.models.SessionManager(requireContext())
        val userId = session.getUserId() ?: return

        com.example.studysynaps.network.RetrofitClient.instance.submitScan(userId, qrContent)
            .enqueue(object : retrofit2.Callback<com.example.studysynaps.models.ApiResponse<Any>> {
                override fun onResponse(
                    call: retrofit2.Call<com.example.studysynaps.models.ApiResponse<Any>>,
                    response: retrofit2.Response<com.example.studysynaps.models.ApiResponse<Any>>
                ) {
                    if (response.isSuccessful && response.body()?.status == true) {
                        Toast.makeText(requireContext(), response.body()?.message, Toast.LENGTH_LONG).show()
                        // Optional: Navigate away or restart camera
                    } else {
                        val msg = response.body()?.message ?: "Gagal presensi"
                        Toast.makeText(requireContext(), msg, Toast.LENGTH_LONG).show()
                        // Restart Camera Logic if needed (requires re-init executor)
                    }
                }

                override fun onFailure(
                    call: retrofit2.Call<com.example.studysynaps.models.ApiResponse<Any>>,
                    t: Throwable
                ) {
                    Toast.makeText(requireContext(), "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        if (!cameraExecutor.isShutdown) {
            cameraExecutor.shutdown()
        }
    }
}
