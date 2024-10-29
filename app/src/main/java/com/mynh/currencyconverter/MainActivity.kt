package com.mynh.currencyconverter

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.mynh.currencyconverter.helper.CurrencyRates

class MainActivity : AppCompatActivity() {
    private lateinit var editTextTop: EditText
    private lateinit var editTextBottom: EditText
    private lateinit var spinnerTop: Spinner
    private lateinit var spinnerBottom: Spinner
    private lateinit var tvRate: TextView

    private var isTopSource = true
    private var isUpdating = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initializeViews()
        setupSpinners()
        setupListeners()
    }

    private fun initializeViews() {
        editTextTop = findViewById(R.id.editTextTop)
        editTextBottom = findViewById(R.id.editTextBottom)
        spinnerTop = findViewById(R.id.spinnerTop)
        spinnerBottom = findViewById(R.id.spinnerBottom)
        tvRate = findViewById(R.id.tvRate)
    }

    private fun setupSpinners() {
        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            CurrencyRates.currencies.map { "${it.code} - ${it.name}" }
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        spinnerTop.adapter = adapter
        spinnerBottom.adapter = adapter

        // Set default selections (USD and EUR)
        spinnerTop.setSelection(adapter.getPosition("USD - US Dollar"))
        spinnerBottom.setSelection(adapter.getPosition("EUR - Euro"))
    }

    private fun setupListeners() {
        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                if (!isUpdating) {
                    updateConversion()
                }
            }
        }

        editTextTop.addTextChangedListener(textWatcher)
        editTextBottom.addTextChangedListener(textWatcher)

        editTextTop.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) isTopSource = true
        }

        editTextBottom.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) isTopSource = false
        }

        val spinnerListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                updateConversion()
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        spinnerTop.onItemSelectedListener = spinnerListener
        spinnerBottom.onItemSelectedListener = spinnerListener
    }

    private fun updateConversion() {
        if (isUpdating) return

        isUpdating = true
        try {
            val fromCurrencyCode = spinnerTop.selectedItem.toString().split(" - ")[0]
            val toCurrencyCode = spinnerBottom.selectedItem.toString().split(" - ")[0]

            val sourceEdit = if (isTopSource) editTextTop else editTextBottom
            val targetEdit = if (isTopSource) editTextBottom else editTextTop

            val fromCurrency = if (isTopSource) fromCurrencyCode else toCurrencyCode
            val toCurrency = if (isTopSource) toCurrencyCode else fromCurrencyCode

            val input = sourceEdit.text.toString().toDoubleOrNull() ?: 0.0
            val rate = CurrencyRates.getExchangeRate(fromCurrency, toCurrency)
            val result = input * rate

            targetEdit.setText(String.format("%.2f", result))
            updateRateDisplay(fromCurrency, toCurrency, rate)
        } catch (e: Exception) {
            // Handle error
            Toast.makeText(this, "Error converting currency", Toast.LENGTH_SHORT).show()
        } finally {
            isUpdating = false
        }
    }

    private fun updateRateDisplay(from: String, to: String, rate: Double) {
        val formattedRate = CurrencyRates.formatAmount(rate, to)
        tvRate.text = "1 $from = $formattedRate"
    }
}