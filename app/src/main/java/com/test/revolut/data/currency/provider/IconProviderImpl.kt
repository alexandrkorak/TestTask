package com.test.revolut.data.currency.provider

import android.content.Context
import androidx.annotation.DrawableRes
import com.test.revolut.R

const val ICON_PREFIX = "ic_"
const val DRAWABLE_RESOURCE_TYPE = "drawable"

class IconProviderImpl(context: Context) :
    IconProvider {
    private val res = context.applicationContext.resources
    private val packageName = context.packageName
    private val map = mutableMapOf<String, Int>()

    @DrawableRes
    override fun getIconRes(currencyCode: String): Int {
        return map.get(currencyCode) ?: populate(currencyCode)
    }

    @DrawableRes
    private fun populate(currencyCode: String): Int {
        val currencyShortCode = currencyCode.substring(0, 2).toLowerCase()
        var resId = res.getIdentifier(
            ICON_PREFIX + currencyShortCode,
            DRAWABLE_RESOURCE_TYPE,
            packageName
        )
        if (resId == 0) {
            resId = R.drawable.ic_launcher_background
        }
        map.put(currencyCode, resId)
        return resId
    }
}