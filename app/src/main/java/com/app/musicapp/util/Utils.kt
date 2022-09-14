package com.app.musicapp.util

import android.text.Editable

object Utils {

    fun String.toEditable() = Editable.Factory.getInstance().newEditable(this)
}