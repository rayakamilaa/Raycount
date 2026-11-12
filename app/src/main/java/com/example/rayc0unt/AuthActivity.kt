package com.example.rayc0unt

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class AuthActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)

        val etName = findViewById<EditText>(R.id.etName)
        val tvNameError = findViewById<TextView>(R.id.tvNameError)
        val etEmail = findViewById<EditText>(R.id.etEmail)
        val tvEmailError = findViewById<TextView>(R.id.tvEmailError)
        val btnSubmit = findViewById<Button>(R.id.btnSubmit)

        btnSubmit.setOnClickListener {
            val nameStr = etName.text.toString().trim()
            val emailStr = etEmail.text.toString().trim()

            var isValid = true

            // --- VALIDASI NAMA ---
            if (nameStr.isEmpty()) {
                tvNameError.text = "Nama tidak boleh kosong"
                tvNameError.visibility = View.VISIBLE
                isValid = false
            } else if (nameStr.length > 15) {
                tvNameError.text = "nama maksimal 15 huruf"
                tvNameError.visibility = View.VISIBLE
                isValid = false
            } else if (!nameStr.matches("^[a-zA-Z\\s]+$".toRegex())) {
                // Mengecek apakah mengandung simbol khusus seperti @#$ dll
                tvNameError.text = "nama hanya boleh menggunakan huruf"
                tvNameError.visibility = View.VISIBLE
                isValid = false
            } else {
                tvNameError.visibility = View.GONE
            }

            // --- VALIDASI EMAIL ---
            // Syarat: wajib mengandung '@' dan berakhiran '.com' (atau format standar gmail)
            val emailPattern = "[a-zA-Z0-9._-]+@gmail\\.com"
            if (emailStr.isEmpty()) {
                tvEmailError.text = "Email tidak boleh kosong"
                tvEmailError.visibility = View.VISIBLE
                isValid = false
            } else if (!emailStr.matches(emailPattern.toRegex())) {
                tvEmailError.text = "format harus berupa nama@gmail.com"
                tvEmailError.visibility = View.VISIBLE
                isValid = false
            } else {
                tvEmailError.visibility = View.GONE
            }

            // --- JIKA SEMUA VALIDASI LOLOS ---
            if (isValid) {
                Toast.makeText(this, "Login Berhasil, Selamat Datang $nameStr!", Toast.LENGTH_SHORT).show()

                // Berpindah ke kalkulator utama (MainActivity)
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish() // Menutup AuthActivity agar tidak bisa di-back ke halaman login
            }
        }
    }
}