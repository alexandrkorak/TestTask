package com.test.revolut.util

import android.text.Editable
import android.text.TextWatcher

abstract class TextChangeListener: TextWatcher {
    override fun afterTextChanged(s: Editable?) {

    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
    }
}