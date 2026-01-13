package com.example.studysynaps

import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.studysynaps.models.SessionManager
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody

class UpdatePhotoActivity : AppCompatActivity() {

    private lateinit var ivPreview: ImageView
    private lateinit var progressBar: ProgressBar
    private var selectedUri: Uri? = null

    private val pickImage = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            selectedUri = it
            ivPreview.setImageURI(it)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_photo)

        val btnBack = findViewById<ImageView>(R.id.btn_back)
        ivPreview = findViewById(R.id.iv_preview)
        val btnChoose = findViewById<Button>(R.id.btn_choose_image)
        val btnSave = findViewById<Button>(R.id.btn_save)
        progressBar = findViewById(R.id.progress_bar)

        btnBack.setOnClickListener { onBackPressedDispatcher.onBackPressed() }
        
        btnChoose.setOnClickListener {
            pickImage.launch("image/*")
        }

        btnSave.setOnClickListener {
            if (selectedUri != null) {
                uploadPhoto(selectedUri!!)
            } else {
                Toast.makeText(this, "Pilih gambar terlebih dahulu", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun uploadPhoto(uri: Uri) {
        progressBar.visibility = View.VISIBLE
        val sessionManager = SessionManager(this)
        val userId = sessionManager.getUserId() ?: return

        val file = getFileFromUri(uri)
        if (file == null) {
            progressBar.visibility = View.GONE
            Toast.makeText(this, "Gagal mengambil file", Toast.LENGTH_SHORT).show()
            return
        }

        // Must match Backend "photo" key
        val requestFile = RequestBody.create("image/*".toMediaTypeOrNull(), file)
        val body = MultipartBody.Part.createFormData("photo", file.name, requestFile)
        val userIdBody = RequestBody.create("text/plain".toMediaTypeOrNull(), userId)

        com.example.studysynaps.network.RetrofitClient.instance.uploadPhoto(userIdBody, body)
            .enqueue(object : retrofit2.Callback<com.example.studysynaps.models.ApiResponse<Any>> {
                override fun onResponse(
                    call: retrofit2.Call<com.example.studysynaps.models.ApiResponse<Any>>,
                    response: retrofit2.Response<com.example.studysynaps.models.ApiResponse<Any>>
                ) {
                    progressBar.visibility = View.GONE
                    if (response.isSuccessful && response.body()?.status == true) {
                        Toast.makeText(this@UpdatePhotoActivity, "Foto berhasil diupdate!", Toast.LENGTH_SHORT).show()
                        
                        // Save new URL if returned
                        val newUrl = response.body()?.data?.toString()
                        if (newUrl != null) {
                            sessionManager.saveUserPhoto(newUrl)
                            // Toast.makeText(this@UpdatePhotoActivity, "Saved Server Path", Toast.LENGTH_SHORT).show()
                        } else {
                            // Fallback: Save local URI as string temp
                            sessionManager.saveUserPhoto(uri.toString())
                            // Toast.makeText(this@UpdatePhotoActivity, "Saved Local URI", Toast.LENGTH_SHORT).show()
                        }
                        
                        finish() // Close Activity
                    } else {
                        // Parse error
                        val msg = try {
                            response.errorBody()?.string() ?: response.message()
                        } catch (e: Exception) {
                            response.message()
                        }
                        Toast.makeText(this@UpdatePhotoActivity, "Gagal: $msg", Toast.LENGTH_LONG).show()
                    }
                }

                override fun onFailure(
                    call: retrofit2.Call<com.example.studysynaps.models.ApiResponse<Any>>,
                    t: Throwable
                ) {
                    progressBar.visibility = View.GONE
                    Toast.makeText(this@UpdatePhotoActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
    }

    private fun getFileFromUri(uri: Uri): java.io.File? {
        try {
            val inputStream = contentResolver.openInputStream(uri)
            val file = java.io.File(cacheDir, "temp_profile_upload.jpg")
            val outputStream = java.io.FileOutputStream(file)
            inputStream?.copyTo(outputStream)
            inputStream?.close()
            outputStream.close()
            return file
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }
}
