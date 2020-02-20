package com.test.revolut.data.source.remote

import com.test.revolut.data.ExchangeRates
import com.test.revolut.data.source.ExchangeRatesDataSource
import com.test.revolut.data.source.ExchangeRatesResponse
import com.test.revolut.data.source.remote.api.RevolutService
import java.math.BigDecimal
import java.util.*
import kotlin.collections.LinkedHashMap

class RemoteExchangeRatesDataSource(private val api: RevolutService) :
    ExchangeRatesDataSource {
    override fun getExchangeRates(baseCurrency: String): ExchangeRates {
        var response: ExchangeRatesResponse? = null
        try {
            response = api.getBaseResponse(baseCurrency).blockingGet()
        } catch (ex: Exception) {
        }
        return ExchangeRates(
            baseCurrency, BigDecimal.ONE, (response ?: ExchangeRatesResponse(
                baseCurrency,
                Date(),
                LinkedHashMap()
            )
                    ).rates
        )
    }
}