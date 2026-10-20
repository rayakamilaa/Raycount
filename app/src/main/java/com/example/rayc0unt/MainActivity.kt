package com.example.rayc0unt

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton

class MainActivity : AppCompatActivity() {

    private lateinit var tvExpression: TextView
    private lateinit var tvResult: TextView
    private var currentInput = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvExpression = findViewById(R.id.tvExpression)
        tvResult = findViewById(R.id.tvResult)

        // Tombol Pintasan untuk Membuka Halaman Kalkulator Diskon
        findViewById<MaterialButton>(R.id.btnOpenDiscount).setOnClickListener {
            val intent = Intent(this, DiscountActivity::class.java)
            startActivity(intent)
        }

        // Tombol Pintasan untuk Membuka Halaman Konversi Mata Uang / Kurs IDR
        findViewById<MaterialButton>(R.id.btnOpenCurrency).setOnClickListener {
            val intent = Intent(this, CurrencyActivity::class.java)
            startActivity(intent)
        }

        val numberButtons = listOf(
            R.id.btn0, R.id.btn1, R.id.btn2, R.id.btn3,
            R.id.btn4, R.id.btn5, R.id.btn6, R.id.btn7,
            R.id.btn8, R.id.btn9, R.id.btnDot
        )

        for (id in numberButtons) {
            findViewById<Button>(id).setOnClickListener { v ->
                val btn = v as Button
                currentInput += btn.text.toString()
                tvResult.text = currentInput
            }
        }

        val operatorButtons = mapOf(
            R.id.btnPlus to "+",
            R.id.btnMinus to "–",
            R.id.btnMultiply to "×",
            R.id.btnDivide to "÷"
        )

        for ((id, op) in operatorButtons) {
            findViewById<Button>(id).setOnClickListener {
                if (currentInput.isNotEmpty() && !currentInput.endsWith(" ")) {
                    currentInput += " $op "
                    tvResult.text = currentInput
                }
            }
        }

        findViewById<Button>(R.id.btnC).setOnClickListener {
            currentInput = ""
            tvResult.text = getString(R.string.default_result)
            tvExpression.text = getString(R.string.default_expression)
        }

        findViewById<Button>(R.id.btnEqual).setOnClickListener {
            try {
                if (currentInput.isNotEmpty()) {
                    val result = evaluateExpression(currentInput)
                    tvExpression.text = currentInput
                    tvResult.text = if (result % 1.0 == 0.0) {
                        result.toLong().toString()
                    } else {
                        result.toString()
                    }
                    currentInput = tvResult.text.toString()
                }
            } catch (e: Exception) {
                tvResult.text = "Error"
            }
        }
    }

    private fun evaluateExpression(str: String): Double {
        val cleanStr = str.replace("×", "*")
            .replace("÷", "/")
            .replace("–", "-")
        val items = cleanStr.trim().split("\\s+".toRegex())
        if (items.size < 3) return items[0].toDouble()

        var result = items[0].toDouble()
        var i = 1
        while (i < items.size) {
            val op = items[i]
            val nextNum = items[i + 1].toDouble()
            when (op) {
                "+" -> result += nextNum
                "-" -> result -= nextNum
                "*" -> result *= nextNum
                "/" -> if (nextNum != 0.0) result /= nextNum else throw ArithmeticException("Divide by zero")
            }
            i += 2
        }
        return result
    }
}