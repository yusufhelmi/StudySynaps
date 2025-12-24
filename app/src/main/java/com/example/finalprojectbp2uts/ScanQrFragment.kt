package com.example.finalprojectbp2uts

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.*
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

    // Kontrak untuk meminta izin kamera
    private val cameraPermissionRequest =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                Toast.makeText(requireContext(), "Izin kamera diberikan", Toast.LENGTH_SHORT).show()
                startCamera() // <-- PANGGIL startCamera() SETELAH IZIN DIBERIKAN
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

        // Setup Barcode Scanner
        val options = BarcodeScannerOptions.Builder()
            .setBarcodeFormats(Barcode.FORMAT_QR_CODE)
            .build()
        barcodeScanner = BarcodeScanning.getClient(options)

        // Cek dan minta izin saat fragment dibuat
        checkCameraPermission()
    }

    private fun checkCameraPermission() {
        when {
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED -> {
                // Izin sudah ada, langsung mulai kamera
                Log.d("ScanQrFragment", "Izin sudah ada, memulai kamera.")
                startCamera()
            }
            shouldShowRequestPermissionRationale(Manifest.permission.CAMERA) -> {
                // Beri penjelasan tambahan jika perlu (opsional)
                Toast.makeText(requireContext(), "Izin kamera dibutuhkan untuk fitur ini.", Toast.LENGTH_LONG).show()
                cameraPermissionRequest.launch(Manifest.permission.CAMERA)
            }
            else -> {
                // Langsung minta izin
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
                    it.setAnalyzer(cameraExecutor, ::processImageProxy)
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

    private fun processImageProxy(imageProxy: ImageProxy) {
        val mediaImage = imageProxy.image
        if (mediaImage != null) {
            val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)
            barcodeScanner.process(image)
                .addOnSuccessListener { barcodes ->
                    if (barcodes.isNotEmpty()) {
                        cameraExecutor.shutdown() // Hentikan proses scan
                        val barcode = barcodes.first()
                        val rawValue = barcode.rawValue
                        Log.d("ScanQrFragment", "QR Code terdeteksi: $rawValue")
                        activity?.runOnUiThread {
                            Toast.makeText(requireContext(), "QR Code: $rawValue", Toast.LENGTH_LONG).show()
                        }
                    }
                }
                .addOnFailureListener {
                    Log.e("ScanQrFragment", "Gagal memproses barcode", it)
                }
                .addOnCompleteListener {
                    imageProxy.close() // Selalu tutup imageProxy
                }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Pastikan executor dimatikan saat view dihancurkan
        if (!cameraExecutor.isShutdown) {
            cameraExecutor.shutdown()
        }
    }
}
