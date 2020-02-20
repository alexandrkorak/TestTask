package com.test.revolut.data.currency.adapter


import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.google.common.base.Optional
import com.test.revolut.data.currency.CurrencyCard
import com.test.revolut.data.currency.CurrencyClickListener
import com.test.revolut.databinding.ItemCurrencyCardBinding
import com.test.revolut.util.TextChangeListener
import java.math.BigDecimal
import java.util.*

class CurrencyCardViewHolder(val itemCurrencyCardBinding: ItemCurrencyCardBinding) :
    RecyclerView.ViewHolder(itemCurrencyCardBinding.root) {

    fun bind(
        currencyCard: CurrencyCard,
        currencyClickListenerOptional: Optional<CurrencyClickListener>
    ) {
        itemCurrencyCardBinding.root.setOnClickListener {
            if (currencyClickListenerOptional.isPresent) {
                currencyClickListenerOptional.get().onClick(
                    currencyCard.currencyCode
                )
            }
        }
        val textWatcher = object : TextChangeListener() {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (currencyClickListenerOptional.isPresent) {
                    var value = BigDecimal.ONE
                    try {
                        value = BigDecimal(s.toString())
                    } catch (ignore: Exception) {
                    }
                    currencyClickListenerOptional.get().onChange(currencyCard.currencyCode, value)
                }
            }
        }
        itemCurrencyCardBinding.currencyInput.onFocusChangeListener =
            View.OnFocusChangeListener { v, hasFocus ->
                if (hasFocus) {
                    if (currencyClickListenerOptional.isPresent) {
                        currencyClickListenerOptional.get().onClick(
                            currencyCard.currencyCode
                        )
                    }
                    itemCurrencyCardBinding.currencyInput.addTextChangedListener(textWatcher)
                } else {
                    itemCurrencyCardBinding.currencyInput.removeTextChangedListener(textWatcher)
                }
            }
        itemCurrencyCardBinding.icon.setImageResource(currencyCard.imageRes)
        itemCurrencyCardBinding.title.text = currencyCard.currencyCode
        itemCurrencyCardBinding.subTitle.text = currencyCard.currencyTitle


        // We update text only if user is not interacting with the input
        if (!itemCurrencyCardBinding.currencyInput.hasFocus()) {
            itemCurrencyCardBinding.currencyInput.setText(
                String.format(
                    Locale.getDefault(),
                    currencyCard.value.stripTrailingZeros().toString()
                )
            )
        }
    }
}