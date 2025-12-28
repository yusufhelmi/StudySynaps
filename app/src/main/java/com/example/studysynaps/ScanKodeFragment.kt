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
                // (Untuk saat ini kita hanya tampilkan Toast)
                Toast.makeText(requireContext(), "Presensi dengan kode: $kode", Toast.LENGTH_LONG).show()

                // Nanti di sini Anda bisa menambahkan logika untuk mengirim kode ke server
            }
        }
    }
}
