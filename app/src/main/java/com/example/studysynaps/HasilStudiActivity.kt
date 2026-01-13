package com.example.studysynaps

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageButton
import android.widget.Spinner
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

class HasilStudiActivity : AppCompatActivity() {

    private lateinit var rvGrades: RecyclerView
    private lateinit var tvIps: TextView
    private lateinit var tvTotalSks: TextView
    private lateinit var spinnerSemester: Spinner
    private lateinit var adapter: GradeAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_hasil_studi)
        
        // Hide Action Bar if exists
        supportActionBar?.hide()

        // Set Status Bar Color
        window.statusBarColor = android.graphics.Color.parseColor("#1A2142")
        
        // Set Status Bar Icons to Light (White)
        val windowInsetsController = ViewCompat.getWindowInsetsController(window.decorView)
        windowInsetsController?.isAppearanceLightStatusBars = false

        setupEdgeToEdge()

        // Init Views
        rvGrades = findViewById(R.id.rv_grades)
        tvIps = findViewById(R.id.tv_ips_value)
        tvTotalSks = findViewById(R.id.tv_total_sks)
        spinnerSemester = findViewById(R.id.spinner_semester)
        val btnBack = findViewById<ImageButton>(R.id.btn_back)

        btnBack.setOnClickListener { finish() }

        rvGrades.layoutManager = LinearLayoutManager(this)

        setupSpinner()
    }

    private fun setupSpinner() {
        // Dropdown Semester 1 & 2
        val semesters = arrayOf("Semester 1", "Semester 2")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, semesters)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerSemester.adapter = adapter

        // Set default to Semester 2 (Latest)
        spinnerSemester.setSelection(1)

        spinnerSemester.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val selectedSem = position + 1 // Index 0 -> Sem 1
                fetchGrades(selectedSem)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }

    private fun fetchGrades(semester: Int) {
        val session = SessionManager(this)
        val userId = session.getUserId() ?: return

        // Show Loading/Reset?
        
        RetrofitClient.instance.getSemesterGrades(userId, semester).enqueue(object : Callback<com.example.studysynaps.models.ApiResponse<GradeResponse>> {
            override fun onResponse(
                call: Call<com.example.studysynaps.models.ApiResponse<GradeResponse>>,
                response: Response<com.example.studysynaps.models.ApiResponse<GradeResponse>>
            ) {
                if (response.isSuccessful && response.body()?.status == true) {
                    val data = response.body()?.data
                    if (data != null) {
                        tvIps.text = data.ips.toString()
                        tvTotalSks.text = data.totalSks.toString()
                        rvGrades.adapter = GradeAdapter(data.courses)
                    } else {
                        Toast.makeText(this@HasilStudiActivity, "Data Kosong", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@HasilStudiActivity, "Gagal memuat data", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<com.example.studysynaps.models.ApiResponse<GradeResponse>>, t: Throwable) {
                Toast.makeText(this@HasilStudiActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
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
