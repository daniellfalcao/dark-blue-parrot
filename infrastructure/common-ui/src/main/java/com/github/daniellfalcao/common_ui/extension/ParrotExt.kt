package com.github.daniellfalcao.common_ui.extension

import android.content.Context
import com.github.daniellfalcao.common_ui.R
import com.github.daniellfalcao.domain.user.model.entity.UserEntity

fun UserEntity.Parrot.toDrawable(context: Context) = when(this) {
    UserEntity.Parrot.BLUE -> context.compatDrawable(R.drawable.img_parrot_blue)
    UserEntity.Parrot.GREEN -> context.compatDrawable(R.drawable.img_parrot_green)
    UserEntity.Parrot.PINK -> context.compatDrawable(R.drawable.img_parrot_pink)
    UserEntity.Parrot.YELLOW -> context.compatDrawable(R.drawable.img_parrot_yellow)
}

fun UserEntity.Parrot.toProfileDrawable(context: Context) = when(this) {
    UserEntity.Parrot.BLUE -> context.compatDrawable(R.drawable.img_parrot_blue_profile)
    UserEntity.Parrot.GREEN -> context.compatDrawable(R.drawable.img_parrot_green_profile)
    UserEntity.Parrot.PINK -> context.compatDrawable(R.drawable.img_parrot_pink_profile)
    UserEntity.Parrot.YELLOW -> context.compatDrawable(R.drawable.img_parrot_yellow_profile)
}