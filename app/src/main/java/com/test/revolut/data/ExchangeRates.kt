package com.test.revolut.data

import java.io.Serializable
import java.math.BigDecimal

data class ExchangeRates(
    val baseCurrency: String,
    val baseValue: BigDecimal,
    val currencyRates: LinkedHashMap<String, BigDecimal>
) : Serializable {
    fun getCurrencies(): List<String> {
        val list = currencyRates.keys.toMutableList()
        list.add(0, baseCurrency)
        return list
    }
}