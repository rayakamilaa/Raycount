package com.example.rayc0unt

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import java.text.NumberFormat
import java.util.Locale

class DiscountActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_discount)

        val etPrice = findViewById<EditText>(R.id.etPrice)
        val etDiscount = findViewById<EditText>(R.id.etDiscount)
        val btnCalculate = findViewById<MaterialButton>(R.id.btnCalculateDiscount)
        val tvResult = findViewById<TextView>(R.id.tvDiscountResult)

        // Fitur Auto-Formatting Titik Ribuan pada Input Harga Asli
        etPrice.addTextChangedListener(object : TextWatcher {
            private var current = ""
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                if (s.toString() != current) {
                    etPrice.removeTextChangedListener(this)
                    val cleanString = s.toString().replace(Regex("[Rp,.\\s]"), "")
                    if (cleanString.isNotEmpty()) {
                        val parsed = cleanString.toDouble()
                        val formatted = NumberFormat.getNumberInstance(Locale("in", "ID")).format(parsed)
                        current = formatted
                        etPrice.setText(formatted)
                        etPrice.setSelection(formatted.length)
                    } else {
                        current = ""
                        etPrice.setText("")
                    }
                    etPrice.addTextChangedListener(this)
                }
            }
        })

        // Logika Hitung Diskon
        btnCalculate.setOnClickListener {
            val priceStr = etPrice.text.toString().replace(Regex("[.,]"), "")
            val discStr = etDiscount.text.toString()

            if (priceStr.isNotEmpty() && discStr.isNotEmpty()) {
                val price = priceStr.toDouble()
                val discount = discStr.toDouble()
                val finalPrice = price - (price * (discount / 100))

                // Format hasil akhir dengan titik ribuan agar rapi (misal: Rp 90.000)
                val formattedResult = NumberFormat.getNumberInstance(Locale("in", "ID")).format(finalPrice)
                tvResult.text = "Total Akhir: Rp $formattedResult"
            } else {
                tvResult.text = "Mohon isi harga dan diskon!"
            }
        }
    }
}