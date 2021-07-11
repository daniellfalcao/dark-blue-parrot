package com.github.daniellfalcao.domain.user.model.entity

import java.util.Date

data class UserEntity(
    var id: String,
    var username: String,
    var birthday: Date,
    var parrot: Parrot
) {

    enum class Parrot {
        BLUE,
        GREEN,
        PINK,
        YELLOW
    }
}