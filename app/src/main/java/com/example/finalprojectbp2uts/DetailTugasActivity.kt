package com.example.finalprojectbp2uts

import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.button.MaterialButton
import android.widget.ImageView

class DetailTugasActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_detail_tugas)

        setupEdgeToEdge()
        setupViews()
    }

    private fun setupEdgeToEdge() {
        val headerContainer = findViewById<android.view.View>(R.id.header_container)
        ViewCompat.setOnApplyWindowInsetsListener(headerContainer) { view, windowInsets ->
            val insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.updatePadding(top = insets.top)
            windowInsets
        }
    }

    private fun setupViews() {
        findViewById<ImageButton>(R.id.btn_back).setOnClickListener {
            finish()
        }

        // Get data from intent
        val title = intent.getStringExtra("EXTRA_TITLE")
        // val subtitle = intent.getStringExtra("EXTRA_SUBTITLE") // Use if passed

        if (title != null) {
            findViewById<TextView>(R.id.tv_tugas_title).text = title
        }

        findViewById<MaterialButton>(R.id.btn_add_assignment).setOnClickListener {
            showUploadBottomSheet()
        }
    }

    private fun showUploadBottomSheet() {
        val dialog = BottomSheetDialog(this)
        val view = layoutInflater.inflate(R.layout.layout_upload_bottom_sheet, null)
        
        // Handle sheet close
        view.findViewById<ImageView>(R.id.btn_close_sheet).setOnClickListener {
            dialog.dismiss()
        }
        
        // Handle browse button placeholder
        view.findViewById<MaterialButton>(R.id.btn_browse).setOnClickListener {
           // Placeholder for file picker
        }

        dialog.setContentView(view)
        dialog.show()
    }
}
