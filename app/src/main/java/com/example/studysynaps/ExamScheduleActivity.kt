package com.example.studysynaps

import android.os.Bundle
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.studysynaps.network.RetrofitClient
import com.example.studysynaps.models.ExamItem
import com.example.studysynaps.models.SessionManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ExamScheduleActivity : AppCompatActivity() {

    private lateinit var examAdapter: ExamAdapter
    private lateinit var rvExams: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_exam_schedule)

        setupEdgeToEdge()
        
        // Setup Header Back Button
        findViewById<ImageButton>(R.id.btn_back).setOnClickListener {
            finish()
        }

        setupRecyclerView()
        fetchExamSchedule()
    }

    private fun setupRecyclerView() {
        rvExams = findViewById(R.id.rv_exams)
        rvExams.layoutManager = LinearLayoutManager(this)
        examAdapter = ExamAdapter(emptyList()) // Init empty
        rvExams.adapter = examAdapter
    }

    private fun fetchExamSchedule() {
        val session = SessionManager(this)
        val userId = session.getUserId()
        
        // DEBUG TOAST
        // Toast.makeText(this, "Debug: User ID = $userId", Toast.LENGTH_LONG).show()

        if (userId == null) return

        RetrofitClient.instance.getExamSchedule(userId).enqueue(object : Callback<com.example.studysynaps.models.ApiResponse<List<ExamItem>>> {
            override fun onResponse(
                call: Call<com.example.studysynaps.models.ApiResponse<List<ExamItem>>>,
                response: Response<com.example.studysynaps.models.ApiResponse<List<ExamItem>>>
            ) {
                if (response.isSuccessful && response.body()?.status == true) {
                    val exams = response.body()?.data ?: emptyList()
                    if (exams.isEmpty()) {
                        Toast.makeText(this@ExamScheduleActivity, "Belum ada jadwal ujian", Toast.LENGTH_SHORT).show()
                    }
                    rvExams.adapter = ExamAdapter(exams)
                } else {
                    Toast.makeText(this@ExamScheduleActivity, "Gagal memuat jadwal", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<com.example.studysynaps.models.ApiResponse<List<ExamItem>>>, t: Throwable) {
                Toast.makeText(this@ExamScheduleActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun setupEdgeToEdge() {
        val root = findViewById<LinearLayout>(R.id.root_container)
        ViewCompat.setOnApplyWindowInsetsListener(root) { view, windowInsets ->
            val insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.updatePadding(top = insets.top, bottom = insets.bottom)
            windowInsets
        }
    }
}
