package com.lightningkite.kotlin.android.example

import android.graphics.Color
import android.graphics.Typeface
import android.widget.TextView
import org.jetbrains.anko.textColor

/**
 * We usually use a file like this to define styling.
 * Alternatively, you could define all of your styles strictly in the style xml as usual.
 *
 * Created by jivie on 7/13/16.
 */

fun TextView.styleDefault() {
    textSize = 14f
    textColor = Color.BLACK
}


fun TextView.styleTitle() {
    textSize = 18f
    textColor = Color.BLACK
    setTypeface(null, Typeface.BOLD)
}