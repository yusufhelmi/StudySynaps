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
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.studysynaps.network.RetrofitClient

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

        // Load current profile photo
        loadCurrentProfilePhoto()

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
                        // Show Dialog forcing re-login
                        androidx.appcompat.app.AlertDialog.Builder(this@UpdatePhotoActivity)
                            .setTitle("Berhasil Update")
                            .setMessage("Foto profil berhasil diperbarui. Silakan login ulang untuk melihat perubahan.")
                            .setPositiveButton("OK") { _, _ ->
                                // Clear Session & Logout
                                sessionManager.clearSession()
                                val intent = android.content.Intent(this@UpdatePhotoActivity, LoginOrRegist::class.java)
                                intent.flags = android.content.Intent.FLAG_ACTIVITY_NEW_TASK or android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK
                                startActivity(intent)
                                finishAffinity()
                            }
                            .setCancelable(false)
                            .show()
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
            val bitmap = android.graphics.BitmapFactory.decodeStream(inputStream)
            inputStream?.close()

            val file = java.io.File(cacheDir, "temp_profile_upload.jpg")
            val outputStream = java.io.FileOutputStream(file)
            
            // Compress to JPEG, Quality 80
            bitmap.compress(android.graphics.Bitmap.CompressFormat.JPEG, 80, outputStream)
            
            outputStream.flush()
            outputStream.close()
            return file
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }


    private fun loadCurrentProfilePhoto() {
        val sessionManager = SessionManager(this)
        var photoUrl = sessionManager.getUserPhoto()

        if (!photoUrl.isNullOrEmpty()) {
            if (!photoUrl.startsWith("http") && !photoUrl.startsWith("content") && !photoUrl.startsWith("file")) {
                val baseUrl = RetrofitClient.BASE_URL.replace("index.php/", "")
                photoUrl = "$baseUrl$photoUrl"
            }

            Glide.with(this)
                .load(photoUrl)
                .placeholder(R.drawable.cristiano_ronaldo)
                .error(R.drawable.cristiano_ronaldo)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(ivPreview)
        }
    }
}
