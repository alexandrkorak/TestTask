package com.test.revolut.data.currency

import com.test.revolut.data.ExchangeRates
import com.test.revolut.data.currency.provider.RateProvider
import com.test.revolut.data.currency.adapter.CurrencyCardAdapter
import java.math.BigDecimal

interface CurrencyCardProvider {
    fun getAdapter(): CurrencyCardAdapter

    fun size(): Int
    fun get(position: Int): CurrencyCard
    fun setExchangeRates(it: ExchangeRates)
}