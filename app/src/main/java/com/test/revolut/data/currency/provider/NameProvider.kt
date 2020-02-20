package com.test.revolut.data.currency.provider

interface NameProvider {
    fun getName(currencyCode: String): String
}