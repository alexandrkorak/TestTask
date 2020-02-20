package com.test.revolut.data.source

import com.test.revolut.data.ExchangeRates

interface ExchangeRatesDataSource {
    fun getExchangeRates(baseCurrency: String): ExchangeRates
}