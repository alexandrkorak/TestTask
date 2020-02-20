package com.test.revolut.data.currency.provider

import androidx.annotation.DrawableRes

interface IconProvider {

    @DrawableRes
    fun getIconRes(currencyCode: String): Int
}