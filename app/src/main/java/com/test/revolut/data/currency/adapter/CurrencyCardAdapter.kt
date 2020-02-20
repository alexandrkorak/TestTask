package com.test.revolut.data.currency.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import com.google.common.base.Optional
import com.test.revolut.data.currency.CurrencyCardProvider
import com.test.revolut.data.currency.CurrencyClickListener
import com.test.revolut.databinding.ItemCurrencyCardBinding

class CurrencyCardAdapter(
    private val currencyCardProvider: CurrencyCardProvider
) : RecyclerView.Adapter<CurrencyCardViewHolder>() {
    private var clickListenerOptional = Optional.absent<CurrencyClickListener>()
    private lateinit var recyclerView: RecyclerView

    fun setListener(currencyClickListener: CurrencyClickListener?) {
        clickListenerOptional = Optional.fromNullable(currencyClickListener)
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        this.recyclerView = recyclerView
        (recyclerView.itemAnimator as SimpleItemAnimator?)?.supportsChangeAnimations = false
    }

    fun scrollToFirstItem() {
        recyclerView.scrollToPosition(0)
    }

    fun isComputingLayout(): Boolean {
        return recyclerView.isComputingLayout
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrencyCardViewHolder {
        val itemCurrencyCardBinding = ItemCurrencyCardBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return CurrencyCardViewHolder(
            itemCurrencyCardBinding
        )
    }

    override fun getItemCount(): Int {
        return currencyCardProvider.size()
    }

    override fun onBindViewHolder(holderCard: CurrencyCardViewHolder, position: Int) {
        val currencyCard = currencyCardProvider.get(position)
        holderCard.bind(currencyCard, clickListenerOptional)
    }
}