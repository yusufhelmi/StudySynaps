package com.example.studysynaps

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.textfield.TextInputEditText

class ScanKodeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_scan_kode, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Cari komponen dari layout
        val etKodePresensi: TextInputEditText = view.findViewById(R.id.et_kode_presensi)
        val btnBatal: Button = view.findViewById(R.id.btn_batal)
        val btnAbsen: Button = view.findViewById(R.id.btn_absen)

        // Tambahkan aksi untuk tombol Batal
        btnBatal.setOnClickListener {
            // Kosongkan input field
            etKodePresensi.text?.clear()
            Toast.makeText(requireContext(), "Dibatalkan", Toast.LENGTH_SHORT).show()
        }

        // Tambahkan aksi untuk tombol Absen
        btnAbsen.setOnClickListener {
            val kode = etKodePresensi.text.toString()

            if (kode.isBlank()) {
                // Jika kode kosong, beri peringatan
                etKodePresensi.error = "Kode tidak boleh kosong"
            } else {
                // Jika kode terisi, proses presensi
                // Format sesuai backend: "COURSE:[KODE]"
                val qrContent = if (kode.startsWith("COURSE:")) kode else "COURSE:$kode"
                
                val session = com.example.studysynaps.models.SessionManager(requireContext())
                val userId = session.getUserId()

                if (userId != null) {
                    com.example.studysynaps.network.RetrofitClient.instance.submitScan(userId, qrContent)
                        .enqueue(object : retrofit2.Callback<com.example.studysynaps.models.ApiResponse<Any>> {
                            override fun onResponse(
                                call: retrofit2.Call<com.example.studysynaps.models.ApiResponse<Any>>,
                                response: retrofit2.Response<com.example.studysynaps.models.ApiResponse<Any>>
                            ) {
                                if (response.isSuccessful && response.body()?.status == true) {
                                    Toast.makeText(requireContext(), response.body()?.message, Toast.LENGTH_LONG).show()
                                    etKodePresensi.text?.clear()
                                } else {
                                    // Parse Error Body untuk HTTP 4xx/5xx
                                    val errorJson = response.errorBody()?.string()
                                    val msg = try {
                                        val jsonObject = org.json.JSONObject(errorJson)
                                        jsonObject.getString("message")
                                    } catch (e: Exception) {
                                        "Gagal presensi (Code: ${response.code()})"
                                    }
                                    Toast.makeText(requireContext(), msg, Toast.LENGTH_LONG).show()
                                }
                            }

                            override fun onFailure(
                                call: retrofit2.Call<com.example.studysynaps.models.ApiResponse<Any>>,
                                t: Throwable
                            ) {
                                Toast.makeText(requireContext(), "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                            }
                        })
                } else {
                    Toast.makeText(requireContext(), "User tidak ditemukan", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
