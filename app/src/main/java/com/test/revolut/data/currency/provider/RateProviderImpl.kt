package com.test.revolut.data.currency.provider

import java.math.BigDecimal

class RateProviderImpl : RateProvider {
    private val rates = mutableMapOf<String, BigDecimal>()

    override fun getRate(currencyCode: String): BigDecimal {
        return rates[currencyCode] ?: BigDecimal.ONE
    }

    override fun updateExchangeRates(exchangeRates: Map<String, BigDecimal>) {
        rates.clear()
        rates.putAll(exchangeRates)
    }
}