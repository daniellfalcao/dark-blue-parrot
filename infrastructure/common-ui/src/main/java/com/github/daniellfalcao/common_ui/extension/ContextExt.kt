package com.github.daniellfalcao.common_ui.extension

import android.content.Context
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat

/**
 * Wrap ContextCompat.getDrawable call into a extension of Context.
 *
 * */
fun Context.compatDrawable(@DrawableRes id: Int) = ContextCompat.getDrawable(this, id)

/**
 * Wrap ContextCompat.getColor call into a extension of Context.
 *
 * */
fun Context.compatColor(@ColorRes id: Int) = ContextCompat.getColor(this, id)

/**
 * Wrap ContextCompat.getColorStateList call into a extension of Context.
 *
 * */
fun Context.compatColorStateList(@ColorRes id: Int) = ContextCompat.getColorStateList(this, id)