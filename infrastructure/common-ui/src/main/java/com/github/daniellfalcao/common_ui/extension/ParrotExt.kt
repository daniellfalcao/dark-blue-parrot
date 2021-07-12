package com.github.daniellfalcao.common_ui.extension

import android.content.Context
import com.github.daniellfalcao.common_ui.R
import com.github.daniellfalcao.domain.user.model.UserDTO.Parrot

fun Parrot.toDrawable(context: Context) = when (this) {
    Parrot.BLUE -> context.compatDrawable(R.drawable.img_parrot_blue)
    Parrot.GREEN -> context.compatDrawable(R.drawable.img_parrot_green)
    Parrot.PINK -> context.compatDrawable(R.drawable.img_parrot_pink)
    Parrot.YELLOW -> context.compatDrawable(R.drawable.img_parrot_yellow)
}

fun Parrot.toProfileDrawable(context: Context) = when (this) {
    Parrot.BLUE -> context.compatDrawable(R.drawable.img_parrot_blue_profile)
    Parrot.GREEN -> context.compatDrawable(R.drawable.img_parrot_green_profile)
    Parrot.PINK -> context.compatDrawable(R.drawable.img_parrot_pink_profile)
    Parrot.YELLOW -> context.compatDrawable(R.drawable.img_parrot_yellow_profile)
}