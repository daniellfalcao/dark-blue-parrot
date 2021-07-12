package com.github.daniellfalcao.data.user.model.view

import androidx.room.Embedded
import com.github.daniellfalcao.data.user.model.entity.UserEntity
import com.github.daniellfalcao.data.user.model.entity.toDTO
import com.github.daniellfalcao.domain.user.model.ProfileDTO
import com.github.daniellfalcao.domain.user.model.UserDTO

class ProfileView {

    @Embedded
    var user: UserEntity? = null

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ProfileView

        if (user != other.user) return false

        return true
    }

    override fun hashCode(): Int {
        return user?.hashCode() ?: 0
    }
}

fun ProfileView.toDTO() = ProfileDTO(
    user = user?.toDTO() ?: UserDTO()
)