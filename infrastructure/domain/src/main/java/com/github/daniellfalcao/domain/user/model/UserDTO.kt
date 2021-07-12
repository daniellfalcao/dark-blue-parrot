package com.github.daniellfalcao.domain.user.model

import java.text.SimpleDateFormat
import java.util.Locale


data class UserDTO(
    val username: String,
    val parrot: Parrot,
    val likes: Long,
    val bookmarks: Long
) {

    constructor() : this("", Parrot.default, 0, 0)

    enum class Parrot {
        BLUE,
        GREEN,
        PINK,
        YELLOW;

        companion object {

            val default = BLUE

            fun from(string: String) = when (string) {
                BLUE.name -> BLUE
                GREEN.name -> GREEN
                PINK.name -> PINK
                YELLOW.name -> YELLOW
                else -> default
            }
        }
    }

    companion object {
        val birthdayDateFormat: SimpleDateFormat
            get() = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    }
}