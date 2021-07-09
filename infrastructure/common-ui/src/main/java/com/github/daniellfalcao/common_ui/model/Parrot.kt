package com.github.daniellfalcao.common_ui.model

import androidx.annotation.DrawableRes
import com.github.daniellfalcao.common_ui.R

enum class Parrot(@DrawableRes val drawableRes: Int) {
    BLUE(R.drawable.img_parrot_blue),
    GREEN(R.drawable.img_parrot_green),
    PINK(R.drawable.img_parrot_pink),
    YELLOW(R.drawable.img_parrot_yellow)
}