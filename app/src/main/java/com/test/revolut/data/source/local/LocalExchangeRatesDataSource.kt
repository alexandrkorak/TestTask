package com.test.revolut.data.source.local

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.res.Resources
import com.google.common.base.Optional
import com.google.gson.Gson
import com.test.revolut.R
import com.test.revolut.data.ExchangeRates
import com.test.revolut.data.source.ExchangeRatesResponse
import com.test.revolut.data.source.MutableExchangeRatesDataSource
import java.io.*
import java.math.BigDecimal


const val DIR_NAME = "data"
const val EXCHANGE_RATES_PARAM_KEY = "exchange_rates"

class LocalExchangeRatesDataSource(context: Context) :
    MutableExchangeRatesDataSource {
    private val context = context.applicationContext
    private var exchangeRatesOptional: Optional<ExchangeRates> = Optional.absent()

    private fun getExchangeRatesObjectFile(): File {
        return File(
            context.getDir(DIR_NAME, MODE_PRIVATE),
            EXCHANGE_RATES_PARAM_KEY
        )
    }

    private fun getLocal(): ExchangeRates {
        if (exchangeRatesOptional.isPresent) {
            return exchangeRatesOptional.get()
        } else {
            val objectInputStream = ObjectInputStream(FileInputStream(getExchangeRatesObjectFile()))
            objectInputStream.use {
                return objectInputStream.readObject() as ExchangeRates
            }
        }
    }

    private fun putLocal(exchangeRates: ExchangeRates) {
        exchangeRatesOptional = Optional.of(exchangeRates)
        val objectOutputStream = ObjectOutputStream(FileOutputStream(getExchangeRatesObjectFile()))
        objectOutputStream.use {
            objectOutputStream.writeObject(exchangeRates)
        }
    }

    private fun readBaseExchangeRates(res: Resources): ExchangeRates {
        val br = BufferedReader(
            InputStreamReader(res.openRawResource(R.raw.base_currency_exchange_rate))
        )
        br.use {
            val sb = StringBuilder()
            var temp: String?
            while (br.readLine().also { temp = it } != null) sb.append(temp)
            val baseResponse =
                Gson().fromJson(sb.toString(), ExchangeRatesResponse::class.java)
            return ExchangeRates(baseResponse.base, BigDecimal.ONE, baseResponse.rates)
        }
    }

    override fun getExchangeRates(baseCurrency: String): ExchangeRates {
        try {
            return getLocal()
        } catch (ex: Exception) {
            val baseExchangeRates = readBaseExchangeRates(context.resources)
            putLocal(baseExchangeRates)
            return baseExchangeRates
        }
    }

    override fun setExchangeRates(exchangeRates: ExchangeRates) {
        putLocal(exchangeRates)
    }
}