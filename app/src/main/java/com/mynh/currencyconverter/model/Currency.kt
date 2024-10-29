package com.mynh.currencyconverter.model

data class Currency(
    val code: String,    // Currency code (e.g., "USD")
    val name: String,    // Currency name (e.g., "US Dollar")
    val symbol: String   // Currency symbol (e.g., "$")
)