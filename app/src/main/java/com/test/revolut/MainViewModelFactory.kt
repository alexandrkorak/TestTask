package com.test.revolut

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.test.revolut.data.source.CombinedExchangeRatesDataSource

class MainViewModelFactory(private val dataSource: CombinedExchangeRatesDataSource) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MainViewModel(dataSource) as T
    }
}