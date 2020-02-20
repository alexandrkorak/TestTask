package com.test.revolut

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.test.revolut.data.ExchangeRates
import com.test.revolut.data.currency.CurrencyCardDataProvider
import com.test.revolut.data.currency.CurrencyCardProvider
import com.test.revolut.data.currency.CurrencyClickListener
import com.test.revolut.data.currency.adapter.CurrencyCardAdapter
import com.test.revolut.data.currency.provider.IconProviderImpl
import com.test.revolut.data.currency.provider.NameProviderImpl
import com.test.revolut.data.currency.provider.RateProviderImpl
import com.test.revolut.data.source.ExchangeRatesRepository
import com.test.revolut.data.source.local.LocalExchangeRatesDataSource
import com.test.revolut.data.source.remote.RemoteExchangeRatesDataSource
import com.test.revolut.data.source.remote.api.getRevolutService
import com.test.revolut.databinding.ActivityMainBinding
import java.math.BigDecimal

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var dataInterface: CurrencyCardProvider
    lateinit var adapter: CurrencyCardAdapter
    lateinit var viewModel: MainViewModel

    val listener: CurrencyClickListener = object : CurrencyClickListener {
        override fun onClick(currency: String) {
            viewModel.select(currency)
        }

        override fun onChange(currency: String, value: BigDecimal) {
            viewModel.change(currency, value)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        setSupportActionBar(binding.mainToolbar)

        dataInterface = CurrencyCardDataProvider(
            NameProviderImpl(), IconProviderImpl(this), RateProviderImpl()
        )
        adapter = dataInterface.getAdapter()
        binding.mainRecyclerView.adapter = adapter
        binding.mainRecyclerView.layoutManager = LinearLayoutManager(this)


        // View model
        val dataSource = ExchangeRatesRepository(
            LocalExchangeRatesDataSource(this),
            RemoteExchangeRatesDataSource(getRevolutService())
        )

        val factory = MainViewModelFactory(dataSource)
        viewModel = ViewModelProvider(this, factory).get(MainViewModel::class.java)
        viewModel.getExchangeRates().observe(this, Observer<ExchangeRates> {
            dataInterface.setExchangeRates(it)
        })
    }

    override fun onResume() {
        super.onResume()
        adapter.setListener(listener)
    }

    override fun onPause() {
        adapter.setListener(null)
        super.onPause()
    }
}
