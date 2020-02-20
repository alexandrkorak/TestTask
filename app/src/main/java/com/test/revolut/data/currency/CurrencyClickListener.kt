package com.test.revolut.data.currency

import java.math.BigDecimal

interface CurrencyClickListener {
    fun onClick(currency: String)

    fun onChange(currency: String, value: BigDecimal)
}