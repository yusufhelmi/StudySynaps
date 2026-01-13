package com.example.studysynaps

import android.os.Bundle
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import com.example.studysynaps.network.RetrofitClient
import com.example.studysynaps.models.SessionManager
import com.example.studysynaps.models.Billing
import com.google.android.material.button.MaterialButton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PaymentActivity : AppCompatActivity() {

    private lateinit var tvStatus: TextView
    private lateinit var tvStatusDesc: TextView
    private lateinit var tvTotalBill: TextView
    private lateinit var tvPaidAmount: TextView
    private lateinit var tvArrears: TextView
    private lateinit var tvBankInfo: TextView
    private lateinit var rgPaymentType: RadioGroup
    private lateinit var btnPay: MaterialButton
    private lateinit var progressBar: ProgressBar
    private lateinit var layoutPaymentOptions: LinearLayout
    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_payment) // Ensure this file exists

        sessionManager = SessionManager(this)

        setupViews()
        fetchBillingInfo()
    }

    private fun setupViews() {
        val root = findViewById<android.view.View>(android.R.id.content)
        ViewCompat.setOnApplyWindowInsetsListener(root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.updatePadding(top = systemBars.top, bottom = systemBars.bottom)
            insets
        }

        findViewById<ImageButton>(R.id.btn_back).setOnClickListener { finish() }

        tvStatus = findViewById(R.id.tv_status_mahasiswa)
        tvStatusDesc = findViewById(R.id.tv_status_desc)
        tvTotalBill = findViewById(R.id.tv_total_bill)
        tvPaidAmount = findViewById(R.id.tv_paid_amount)
        tvArrears = findViewById(R.id.tv_arrears)
        tvBankInfo = findViewById(R.id.tv_bank_info)
        rgPaymentType = findViewById(R.id.rg_payment_type)
        btnPay = findViewById(R.id.btn_pay_now)
        progressBar = findViewById(R.id.progress_bar)
        layoutPaymentOptions = findViewById(R.id.layout_payment_options)

        btnPay.setOnClickListener {
            handlePayment()
        }
    }

    private fun fetchBillingInfo() {
        val userId = sessionManager.getUserId() ?: return
        progressBar.visibility = android.view.View.VISIBLE

        RetrofitClient.instance.getBillingInfo(userId).enqueue(object : Callback<com.example.studysynaps.models.ApiResponse<Billing>> {
            override fun onResponse(
                call: Call<com.example.studysynaps.models.ApiResponse<Billing>>,
                response: Response<com.example.studysynaps.models.ApiResponse<Billing>>
            ) {
                progressBar.visibility = android.view.View.GONE
                if (response.isSuccessful && response.body()?.status == true) {
                    val billing = response.body()?.data
                    if (billing != null) {
                        updateUI(billing)
                    }
                } else {
                    Toast.makeText(this@PaymentActivity, "Gagal memuat tagihan", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<com.example.studysynaps.models.ApiResponse<Billing>>, t: Throwable) {
                progressBar.visibility = android.view.View.GONE
                Toast.makeText(this@PaymentActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun updateUI(billing: Billing) {
        // Format Currency
        val total = formatRupiah(billing.amountDue.toDoubleOrNull() ?: 0.0)
        val paid = formatRupiah(billing.amountPaid.toDoubleOrNull() ?: 0.0)
        val arrears = formatRupiah(billing.arrears.toDoubleOrNull() ?: 0.0)

        tvTotalBill.text = total
        tvPaidAmount.text = paid
        tvArrears.text = arrears // Arrears now correctly reflects total debt from DB
        
        tvBankInfo.text = "${billing.bankName ?: "Bank"} - ${billing.bankAccount ?: "-"}"

        // Update Status User UI based on API Billing Status
        // Note: Real user status might be in Session, but we trust Billing status here for context
        val isPaidOff = (billing.arrears.toDoubleOrNull() ?: 0.0) <= 0
        
        if (isPaidOff) {
            tvStatus.text = "Aktif"
            tvStatus.setTextColor(getColor(R.color.green_success))
            tvStatusDesc.text = "Pembayaran lunas. Status mahasiswa aktif."
            layoutPaymentOptions.visibility = android.view.View.GONE // Hide payment options if paid
        } else {
            // Check if Partial (Dispensasi)
            if (billing.status == "partial") {
                tvStatus.text = "Aktif (Dispensasi)"
                 tvStatus.setTextColor(getColor(R.color.green_success)) // Still active
                 tvStatusDesc.text = "Status aktif dengan dispensasi. Segera lunasi tunggakan."
            } else {
                tvStatus.text = "Tidak Aktif"
                tvStatus.setTextColor(getColor(R.color.red_error))
                tvStatusDesc.text = "Lakukan pembayaran untuk mengaktifkan status."
            }
            layoutPaymentOptions.visibility = android.view.View.VISIBLE
        }
    }

    private var selectedPaymentType: String = "full"
    private var paymentBottomSheet: com.google.android.material.bottomsheet.BottomSheetDialog? = null
    private var tvFileName: TextView? = null
    private var btnConfirm: MaterialButton? = null
    private var imgPreview: android.widget.ImageView? = null

    private val filePickerLauncher = registerForActivityResult(androidx.activity.result.contract.ActivityResultContracts.GetContent()) { uri ->
        if (uri != null) {
            tvFileName?.text = "Bukti_Bayar.jpg" // Mock name
            tvFileName?.setTextColor(getColor(android.R.color.black))
            btnConfirm?.isEnabled = true
            
            // Show Preview
            imgPreview?.visibility = android.view.View.VISIBLE
            imgPreview?.setImageURI(uri)
        }
    }

    private fun handlePayment() {
        val selectedId = rgPaymentType.checkedRadioButtonId
        if (selectedId == -1) {
            Toast.makeText(this, "Pilih metode pembayaran", Toast.LENGTH_SHORT).show()
            return
        }

        selectedPaymentType = if (selectedId == R.id.rb_full) "full" else "dispensasi"
        
        showUploadBottomSheet()
    }

    private fun showUploadBottomSheet() {
        val view = layoutInflater.inflate(R.layout.bottom_sheet_upload_payment, null)
        paymentBottomSheet = com.google.android.material.bottomsheet.BottomSheetDialog(this)
        paymentBottomSheet?.setContentView(view)

        // Init Views in Sheet
        val btnSelectFile = view.findViewById<LinearLayout>(R.id.btn_select_file)
        tvFileName = view.findViewById(R.id.tv_file_name)
        btnConfirm = view.findViewById(R.id.btn_confirm_pay)
        imgPreview = view.findViewById(R.id.img_preview)

        btnSelectFile.setOnClickListener {
            filePickerLauncher.launch("image/*")
        }

        btnConfirm?.setOnClickListener {
            simulateAdminChecking()
        }

        paymentBottomSheet?.show()
    }

    private fun simulateAdminChecking() {
        paymentBottomSheet?.dismiss()
        
        // Custom Progress Dialog
        val progressDialog = android.app.AlertDialog.Builder(this)
            .setTitle("Memverifikasi Pembayaran")
            .setMessage("Sistem sedang mengecek bukti pembayaran Anda. Mohon tunggu...")
            .setCancelable(false)
            .create()

        progressDialog.show()

        // Delay 3 seconds
        android.os.Handler(android.os.Looper.getMainLooper()).postDelayed({
            progressDialog.dismiss()
            processRealPayment()
        }, 3000)
    }

    private fun processRealPayment() {
         val userId = sessionManager.getUserId() ?: return
         // Show simple loading or reuse main progress bar
         progressBar.visibility = android.view.View.VISIBLE

         RetrofitClient.instance.payBill(userId, selectedPaymentType).enqueue(object : Callback<com.example.studysynaps.models.ApiResponse<Any>> {
            override fun onResponse(
                call: Call<com.example.studysynaps.models.ApiResponse<Any>>,
                response: Response<com.example.studysynaps.models.ApiResponse<Any>>
            ) {
                progressBar.visibility = android.view.View.GONE
                
                if (response.isSuccessful && response.body()?.status == true) {
                    showSuccessDialog()
                    
                    // Update Local Session Status
                    sessionManager.updateStatus("active")
                    // Refresh Data
                    fetchBillingInfo()
                } else {
                    Toast.makeText(this@PaymentActivity, "Gagal: ${response.body()?.message}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<com.example.studysynaps.models.ApiResponse<Any>>, t: Throwable) {
                progressBar.visibility = android.view.View.GONE
                Toast.makeText(this@PaymentActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun showSuccessDialog() {
        android.app.AlertDialog.Builder(this)
            .setTitle("Pembayaran Berhasil")
            .setMessage("Bukti pembayaran telah diverifikasi. Status kemahasiswaan Anda kini AKTIF.")
            .setIcon(R.drawable.ic_check_circle) // Ensure you have check icon or use generic
            .setPositiveButton("OK", null)
            .show()
    }

    private fun formatRupiah(number: Double): String {
        val format = java.text.NumberFormat.getCurrencyInstance(java.util.Locale("in", "ID"))
        return format.format(number)
    }
}
