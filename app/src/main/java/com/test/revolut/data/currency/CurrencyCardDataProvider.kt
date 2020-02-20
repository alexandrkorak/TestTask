package com.test.revolut.data.currency

import com.test.revolut.data.ExchangeRates
import com.test.revolut.data.currency.adapter.CurrencyCardAdapter
import com.test.revolut.data.currency.provider.IconProvider
import com.test.revolut.data.currency.provider.NameProvider
import com.test.revolut.data.currency.provider.RateProvider
import java.math.BigDecimal
import java.math.MathContext

class CurrencyCardDataProvider(
    private val nameProvider: NameProvider,
    private val iconProvider: IconProvider,
    private val rateProvider: RateProvider
) : CurrencyCardProvider {
    private val adapter: CurrencyCardAdapter =
        CurrencyCardAdapter(this)
    private var currencies = mutableListOf<String>()
    private var base: BigDecimal = BigDecimal.ONE

    override fun setExchangeRates(it: ExchangeRates) {
        rateProvider.updateExchangeRates(it.currencyRates)
        base = it.baseValue
        if (currencies.isEmpty()) {
            currencies.addAll(it.getCurrencies())
            adapter.notifyDataSetChanged()
        } else if (it.baseCurrency != currencies[0]) {
            val baseCurrencyPos = currencies.indexOf(it.baseCurrency)
            currencies.removeAt(baseCurrencyPos)
            currencies.add(0, it.baseCurrency)
            adapter.notifyItemMoved(baseCurrencyPos, 0)
            adapter.scrollToFirstItem()
        } else if (!adapter.isComputingLayout()) {
            adapter.notifyItemRangeChanged(0, currencies.size)
        }
    }

    override fun getAdapter(): CurrencyCardAdapter {
        return adapter
    }

    override fun size(): Int {
        return currencies.size
    }

    override fun get(position: Int): CurrencyCard {
        val currencyCode = currencies[position]
        return CurrencyCard(
            currencyCode,
            nameProvider.getName(currencyCode),
            iconProvider.getIconRes(currencyCode),
            rateProvider.getRate(currencyCode).multiply(base, MathContext(5))
        )
    }
}