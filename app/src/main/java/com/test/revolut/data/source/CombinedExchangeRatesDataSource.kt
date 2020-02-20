package com.test.revolut.data.source

interface CombinedExchangeRatesDataSource : MutableExchangeRatesDataSource {
    fun toLocal(): MutableExchangeRatesDataSource
}