package com.example.rayc0unt

import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import java.text.NumberFormat
import java.util.Locale

class CurrencyActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_currency)

        val etIdrAmount = findViewById<EditText>(R.id.etIdrAmount)
        val spinnerCurrency = findViewById<Spinner>(R.id.spinnerCurrency)
        val btnConvert = findViewById<MaterialButton>(R.id.btnConvert)
        val tvResult = findViewById<TextView>(R.id.tvCurrencyResult)

        // Auto-Format Titik Ribuan pada Input Rupiah
        etIdrAmount.addTextChangedListener(object : TextWatcher {
            private var current = ""
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                if (s.toString() != current) {
                    etIdrAmount.removeTextChangedListener(this)
                    val cleanString = s.toString().replace(Regex("[Rp,.\\s]"), "")
                    if (cleanString.isNotEmpty()) {
                        val parsed = cleanString.toDouble()
                        val localeID = Locale.Builder().setLanguage("in").setRegion("ID").build()
                        val formatted = NumberFormat.getNumberInstance(localeID).format(parsed)
                        current = formatted
                        etIdrAmount.setText(formatted)
                        etIdrAmount.setSelection(formatted.length)
                    } else {
                        current = ""
                        etIdrAmount.setText("")
                    }
                    etIdrAmount.addTextChangedListener(this)
                }
            }
        })

        // Daftar Mata Uang
        val currencies = arrayOf(
            "USD (Dolar Amerika - Est. 15.600)",
            "SGD (Dolar Singapura - Est. 11.800)",
            "KRW (Won Korea - Est. 11.5)"
        )

        val adapter = object : ArrayAdapter<String>(
            this,
            R.layout.item_spinner_dropdown,
            currencies
        ) {
            override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                val view = super.getView(position, convertView, parent) as TextView
                view.setTextColor(Color.parseColor("#2D1F23"))
                return view
            }

            override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
                val view = super.getDropDownView(position, convertView, parent) as TextView
                view.setTextColor(Color.parseColor("#2D1F23"))
                return view
            }
        }

        spinnerCurrency.adapter = adapter
        spinnerCurrency.background = getDrawable(R.drawable.bg_spinner_dropdown)

        // Logika Hitung Konversi
        btnConvert.setOnClickListener {
            val inputStr = etIdrAmount.text.toString().replace(Regex("[.,]"), "")
            if (inputStr.isNotEmpty()) {
                val idr = inputStr.toDouble()
                val selectedPosition = spinnerCurrency.selectedItemPosition

                val (convertedValue, symbol) = when (selectedPosition) {
                    0 -> Pair(idr / 15600.0, "USD")
                    1 -> Pair(idr / 11800.0, "SGD")
                    2 -> Pair(idr / 11.5, "KRW")
                    else -> Pair(0.0, "")
                }

                val formatted = String.format(Locale.US, "%,.2f", convertedValue)
                tvResult.text = "Hasil: $symbol $formatted"
            } else {
                tvResult.text = "Mohon masukkan nominal Rupiah!"
            }
        }
    }
}