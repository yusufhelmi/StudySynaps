package com.example.studysynaps

import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.studysynaps.network.RetrofitClient
import com.example.studysynaps.models.SessionManager
import com.example.studysynaps.models.ApiResponse
import com.example.studysynaps.models.AttendanceLog
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailPresensiActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_presensi)

        val btnBack = findViewById<ImageButton>(R.id.btn_back)
        val tvCourseInfo = findViewById<TextView>(R.id.tv_course_info)
        val rvDetail = findViewById<RecyclerView>(R.id.rv_detail_presensi)
        
        btnBack.setOnClickListener { finish() }

        rvDetail.layoutManager = LinearLayoutManager(this)

        val courseId = intent.getStringExtra("EXTRA_COURSE_ID")
        val courseName = intent.getStringExtra("EXTRA_COURSE_NAME") ?: "Detail Mata Kuliah"
        
        tvCourseInfo.text = courseName

        if (courseId != null) {
            loadHistory(courseId, rvDetail)
        } else {
            Toast.makeText(this, "Error: Course ID tidak diterima", Toast.LENGTH_LONG).show()
        }
    }

    private fun loadHistory(courseId: String, recyclerView: RecyclerView) {
        val session = SessionManager(this)
        val userId = session.getUserId() ?: return

        RetrofitClient.instance.getAttendanceHistory(userId, courseId).enqueue(object : Callback<ApiResponse<List<AttendanceLog>>> {
            override fun onResponse(
                call: Call<ApiResponse<List<AttendanceLog>>>,
                response: Response<ApiResponse<List<AttendanceLog>>>
            ) {
                if (response.isSuccessful && response.body()?.status == true) {
                    val logs = response.body()?.data ?: emptyList()
                    if (logs.isEmpty()) {
                        Toast.makeText(this@DetailPresensiActivity, "Belum ada riwayat presensi", Toast.LENGTH_SHORT).show()
                    }
                    recyclerView.adapter = DetailPresensiAdapter(logs)
                } else {
                    Toast.makeText(this@DetailPresensiActivity, "Gagal memuat: ${response.message()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ApiResponse<List<AttendanceLog>>>, t: Throwable) {
                Toast.makeText(this@DetailPresensiActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
