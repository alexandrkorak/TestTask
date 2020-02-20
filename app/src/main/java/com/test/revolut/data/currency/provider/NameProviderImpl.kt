package com.test.revolut.data.currency.provider

import android.os.Build
import com.test.revolut.data.currency.provider.NameProvider
import java.util.*

class NameProviderImpl : NameProvider {
    private val map = mutableMapOf<String, String>()

    override fun getName(currencyCode: String): String {
        return map.get(currencyCode) ?: populate(currencyCode)
    }

    private fun populate(currencyCode: String): String {
        val baseCurrency: Currency = Currency.getInstance(currencyCode)
        val baseCurrencyName =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) baseCurrency.getDisplayName(
                Locale.getDefault()
            ) else baseCurrency.currencyCode
        map.put(currencyCode, baseCurrencyName)
        return baseCurrencyName
    }
}