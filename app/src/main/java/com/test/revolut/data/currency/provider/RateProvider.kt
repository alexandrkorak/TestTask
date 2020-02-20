package com.test.revolut.data.currency.provider

import java.math.BigDecimal

interface RateProvider {
    fun getRate(currencyCode: String): BigDecimal

    fun updateExchangeRates(exchangeRates: Map<String, BigDecimal>)
}