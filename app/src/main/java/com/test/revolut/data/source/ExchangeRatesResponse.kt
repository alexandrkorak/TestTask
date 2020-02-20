package com.test.revolut.data.source

import java.math.BigDecimal
import java.util.*

data class ExchangeRatesResponse(
    val base: String,
    val date: Date,
    val rates: LinkedHashMap<String, BigDecimal>
)