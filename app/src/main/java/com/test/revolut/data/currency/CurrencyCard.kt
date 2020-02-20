package com.test.revolut.data.currency

import androidx.annotation.DrawableRes
import java.math.BigDecimal

class CurrencyCard(
    val currencyCode: String, val currencyTitle: String,
    @DrawableRes val imageRes: Int, val value: BigDecimal
)