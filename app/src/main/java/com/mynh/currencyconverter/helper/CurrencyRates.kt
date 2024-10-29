package com.mynh.currencyconverter.helper

import com.mynh.currencyconverter.model.Currency

object CurrencyRates {
    // List of available currencies
    val currencies = listOf(
        Currency("USD", "US Dollar", "$"),
        Currency("EUR", "Euro", "€"),
        Currency("GBP", "British Pound", "£"),
        Currency("JPY", "Japanese Yen", "¥"),
        Currency("AUD", "Australian Dollar", "A$"),
        Currency("CAD", "Canadian Dollar", "C$"),
        Currency("CHF", "Swiss Franc", "Fr"),
        Currency("CNY", "Chinese Yuan", "¥"),
        Currency("NZD", "New Zealand Dollar", "NZ$"),
        Currency("SGD", "Singapore Dollar", "S$")
    )

    // Exchange rates with USD as base currency
    private val usdRates = mapOf(
        "USD" to 1.0,
        "EUR" to 0.92,
        "GBP" to 0.82,
        "JPY" to 150.0,
        "AUD" to 1.53,
        "CAD" to 1.36,
        "CHF" to 0.89,
        "CNY" to 7.25,
        "NZD" to 1.67,
        "SGD" to 1.35
    )

    fun getExchangeRate(from: String, to: String): Double {
        val fromRate = usdRates[from] ?: 1.0
        val toRate = usdRates[to] ?: 1.0
        return toRate / fromRate
    }

    fun getCurrencyByCode(code: String): Currency? {
        return currencies.find { it.code == code }
    }

    fun formatAmount(amount: Double, currencyCode: String): String {
        val currency = getCurrencyByCode(currencyCode)
        return when (currencyCode) {
            "JPY", "CNY" -> String.format("%s%.0f", currency?.symbol ?: "", amount)
            else -> String.format("%s%.2f", currency?.symbol ?: "", amount)
        }
    }
}