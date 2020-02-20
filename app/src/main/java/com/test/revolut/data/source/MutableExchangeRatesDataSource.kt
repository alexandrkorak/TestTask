package com.test.revolut.data.source

import com.test.revolut.data.ExchangeRates

interface MutableExchangeRatesDataSource: ExchangeRatesDataSource {
    fun setExchangeRates(exchangeRates: ExchangeRates)
}