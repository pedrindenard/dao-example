package com.app.musicapp.util

import android.os.Handler
import android.os.Looper
import android.text.Editable

object Utils {

    fun String.toEditable(): Editable = Editable.Factory.getInstance().newEditable(this)

    fun delayMillis(delayMillis: Long = 1000, onBlock: () -> Unit) {
        Handler(Looper.getMainLooper()).postDelayed({ onBlock() }, delayMillis)
    }
}