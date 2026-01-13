package com.example.studysynaps

import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.studysynaps.models.GradeResponse
import com.example.studysynaps.models.SessionManager
import com.example.studysynaps.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TranskripActivity : AppCompatActivity() {

    private lateinit var rvGrades: RecyclerView
    private lateinit var tvIpk: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_transkrip)

        // Status Bar Customization
        supportActionBar?.hide()
        window.statusBarColor = android.graphics.Color.parseColor("#1A2142")
        val windowInsetsController = ViewCompat.getWindowInsetsController(window.decorView)
        windowInsetsController?.isAppearanceLightStatusBars = false

        setupEdgeToEdge()

        // Init Views
        rvGrades = findViewById(R.id.rv_grades)
        tvIpk = findViewById(R.id.tv_ipk_value)
        val btnBack = findViewById<ImageButton>(R.id.btn_back)

        btnBack.setOnClickListener { finish() }

        rvGrades.layoutManager = LinearLayoutManager(this)

        fetchTranscript()
    }

    private fun fetchTranscript() {
        val session = SessionManager(this)
        val userId = session.getUserId() ?: return
        
        RetrofitClient.instance.getTranscript(userId).enqueue(object : Callback<com.example.studysynaps.models.ApiResponse<GradeResponse>> {
            override fun onResponse(
                call: Call<com.example.studysynaps.models.ApiResponse<GradeResponse>>,
                response: Response<com.example.studysynaps.models.ApiResponse<GradeResponse>>
            ) {
                if (response.isSuccessful && response.body()?.status == true) {
                    val data = response.body()?.data
                    if (data != null) {
                        tvIpk.text = data.ips.toString() // Uses 'ips' field which holds the calculated value (IPK in this context)
                        rvGrades.adapter = GradeAdapter(data.courses)
                    } else {
                        Toast.makeText(this@TranskripActivity, "Data Kosong", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@TranskripActivity, "Gagal memuat transkrip", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<com.example.studysynaps.models.ApiResponse<GradeResponse>>, t: Throwable) {
                Toast.makeText(this@TranskripActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
    
    private fun setupEdgeToEdge() {
         val root = findViewById<View>(R.id.root_layout)
         ViewCompat.setOnApplyWindowInsetsListener(root) { view, windowInsets ->
            val insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.updatePadding(top = insets.top, bottom = insets.bottom)
            windowInsets
        }
    }
}
