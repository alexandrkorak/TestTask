package com.test.revolut.data.source

import com.test.revolut.data.ExchangeRates

class ExchangeRatesRepository(
    private val localDataSource: MutableExchangeRatesDataSource,
    private val remoteDataSource: ExchangeRatesDataSource
) : CombinedExchangeRatesDataSource {
    override fun toLocal(): MutableExchangeRatesDataSource {
        return localDataSource
    }

    override fun setExchangeRates(exchangeRates: ExchangeRates) {
        localDataSource.setExchangeRates(exchangeRates)
    }

    override fun getExchangeRates(baseCurrency: String): ExchangeRates {
        val local = localDataSource.getExchangeRates(baseCurrency)
        val remote = remoteDataSource.getExchangeRates(baseCurrency)
        val currencyRates =
            if (remote.currencyRates.isEmpty()) local.currencyRates else remote.currencyRates
        return ExchangeRates(remote.baseCurrency, local.baseValue, currencyRates)
    }
}
