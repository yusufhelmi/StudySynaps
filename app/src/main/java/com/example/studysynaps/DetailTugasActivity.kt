package com.example.studysynaps

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.studysynaps.models.SessionManager
import com.example.studysynaps.network.RetrofitClient
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream

class DetailTugasActivity : AppCompatActivity() {

    private lateinit var tvTitle: TextView
    private lateinit var tvDeadline: TextView
    private lateinit var tvDescription: TextView
    private lateinit var tvFileName: TextView
    private lateinit var btnSubmit: MaterialButton
    private lateinit var btnSelectFile: MaterialCardView
    
    private var selectedFileUri: Uri? = null
    private var assignmentId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_tugas)

        initViews()
        setupData()
        setupListeners()
    }

    private fun initViews() {
        tvTitle = findViewById(R.id.tv_title)
        tvDeadline = findViewById(R.id.tv_deadline)
        tvDescription = findViewById(R.id.tv_description)
        tvFileName = findViewById(R.id.tv_file_name)
        btnSubmit = findViewById(R.id.btn_submit)
        btnSelectFile = findViewById(R.id.btn_select_file)
        
        findViewById<ImageButton>(R.id.btn_back).setOnClickListener { finish() }
    }

    private fun setupData() {
        assignmentId = intent.getStringExtra("EXTRA_ID")
        val title = intent.getStringExtra("EXTRA_TITLE")
        val desc = intent.getStringExtra("EXTRA_DESC")
        val deadline = intent.getStringExtra("EXTRA_DEADLINE")

        tvTitle.text = title ?: "Detail Tugas"
        tvDescription.text = desc ?: "Tidak ada deskripsi"
        tvDeadline.text = "Deadline: ${deadline ?: "-"}"
    }

    private fun setupListeners() {
        btnSelectFile.setOnClickListener {
            openFilePicker()
        }

        btnSubmit.setOnClickListener {
            uploadFile()
        }
    }

    private val filePickerLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.data?.let { uri ->
                selectedFileUri = uri
                tvFileName.text = "File Terpilih" // Simple feedback, ideally get filename
                btnSubmit.isEnabled = true
            }
        }
    }

    private fun openFilePicker() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "*/*" // Allow all file types for now, filtering can be added
        filePickerLauncher.launch(intent)
    }

    private fun uploadFile() {
        if (selectedFileUri == null || assignmentId == null) return

        val sessionManager = SessionManager(this)
        val userId = sessionManager.getUserId() ?: return

        // Create temp file from URI
        val file = getFileFromUri(selectedFileUri!!) ?: return
        
        val requestFile = file.asRequestBody("application/octet-stream".toMediaTypeOrNull())
        val body = MultipartBody.Part.createFormData("file", file.name, requestFile)
        
        val userIdPart = userId.toRequestBody("text/plain".toMediaTypeOrNull())
        val assignmentIdPart = assignmentId!!.toRequestBody("text/plain".toMediaTypeOrNull())

        Toast.makeText(this, "Mengupload...", Toast.LENGTH_LONG).show()
        btnSubmit.isEnabled = false

        RetrofitClient.instance.submitAssignment(userIdPart, assignmentIdPart, body).enqueue(object : Callback<com.example.studysynaps.models.ApiResponse<Any>> {
            override fun onResponse(
                call: Call<com.example.studysynaps.models.ApiResponse<Any>>,
                response: Response<com.example.studysynaps.models.ApiResponse<Any>>
            ) {
                btnSubmit.isEnabled = true
                if (response.isSuccessful) {
                    if (response.body()?.status == true) {
                        Toast.makeText(this@DetailTugasActivity, "Berhasil dikirim!", Toast.LENGTH_LONG).show()
                        finish()
                    } else {
                        Toast.makeText(this@DetailTugasActivity, "Gagal: ${response.body()?.message}", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    val errorMsg = response.errorBody()?.string() ?: "Unknown error"
                    Toast.makeText(this@DetailTugasActivity, "Server Error: $errorMsg", Toast.LENGTH_LONG).show()
                    android.util.Log.e("UploadError", errorMsg)
                }
            }

            override fun onFailure(call: Call<com.example.studysynaps.models.ApiResponse<Any>>, t: Throwable) {
                btnSubmit.isEnabled = true
                Toast.makeText(this@DetailTugasActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun getFileFromUri(uri: Uri): File? {
        // Helper to convert URI to temporary File
        try {
            val inputStream = contentResolver.openInputStream(uri) ?: return null
            val file = File(cacheDir, "upload_temp_${System.currentTimeMillis()}")
            val outputStream = FileOutputStream(file)
            inputStream.copyTo(outputStream)
            inputStream.close()
            outputStream.close()
            return file
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }
}
