package com.test.revolut

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.test.revolut.data.ExchangeRates
import com.test.revolut.data.source.CombinedExchangeRatesDataSource
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.SingleSource
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Function
import io.reactivex.schedulers.Schedulers
import java.math.BigDecimal
import java.math.MathContext
import java.util.concurrent.TimeUnit

class MainViewModel(private val dataSource: CombinedExchangeRatesDataSource) : ViewModel() {
    private val exchangeRatesLiveData = MutableLiveData<ExchangeRates>()
    private val compositeDisposable = CompositeDisposable()
    private val mathContext = MathContext(10)
    private var baseCurrency = "EUR"

    init {
        compositeDisposable.add(Single.fromCallable {
            dataSource.toLocal().getExchangeRates(baseCurrency)
        }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { exchangeRates, error ->
                if (error == null && exchangeRates != null) {
                    exchangeRatesLiveData.value = exchangeRates
                    baseCurrency = exchangeRates.baseCurrency
                    subscribeForUpdates()
                }
            }
        )
    }

    private fun getExchangeRatesObservable(): Observable<ExchangeRates> {
        return Observable.interval(1, TimeUnit.SECONDS)
            .flatMapSingle(object : Function<Any, SingleSource<ExchangeRates>> {
                override fun apply(t: Any): SingleSource<ExchangeRates> {
                    return Single.fromCallable {
                        dataSource.toLocal().getExchangeRates(baseCurrency)
                    }
                }
            })
    }

    private fun subscribeForUpdates() {
        compositeDisposable.clear()
        compositeDisposable.add(
            getExchangeRatesObservable()
                .delay(1, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { t -> exchangeRatesLiveData.value = t },
                    { subscribeForUpdates() }
                )
        )
    }

    fun getExchangeRates(): MutableLiveData<ExchangeRates> {
        return exchangeRatesLiveData
    }

    private fun postExchangeRate(exchangeRates: ExchangeRates) {
        compositeDisposable.clear()
        compositeDisposable.add(
            Completable.fromCallable { dataSource.setExchangeRates(exchangeRates) }.observeOn(
                Schedulers.io()
            ).subscribe(
                { subscribeForUpdates() },
                { subscribeForUpdates() }
            )
        )
    }

    fun select(currency: String) {
        if (baseCurrency != currency) {
            baseCurrency = currency
            val oldExchangeRates = exchangeRatesLiveData.value!!
            val currencyRates = LinkedHashMap<String, BigDecimal>()
            val newBaseValue =
                oldExchangeRates.baseValue.multiply(
                    oldExchangeRates.currencyRates[currency] ?: BigDecimal.ONE
                )

            currencyRates.put(oldExchangeRates.baseCurrency, BigDecimal.ONE)
            currencyRates.putAll(oldExchangeRates.currencyRates)
            val newBaseExchangeRate = currencyRates.remove(currency) ?: BigDecimal.ONE
            currencyRates.forEach {
                currencyRates.set(
                    it.key,
                    it.value.divide(newBaseExchangeRate, mathContext)
                )
            }
            val newExchangeRates = ExchangeRates(currency, newBaseValue, currencyRates)
            exchangeRatesLiveData.value = newExchangeRates
            postExchangeRate(newExchangeRates)
        }
    }

    fun change(currency: String, value: BigDecimal) {
        if (currency == baseCurrency && value != exchangeRatesLiveData.value!!.baseValue) {
            val newExchangeRates = ExchangeRates(
                baseCurrency, value, exchangeRatesLiveData.value!!.currencyRates
            )
            exchangeRatesLiveData.value = newExchangeRates
            postExchangeRate(newExchangeRates)
        }
    }


    override fun onCleared() {
        compositeDisposable.dispose()
        super.onCleared()
    }
}