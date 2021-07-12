package com.github.daniellfalcao.data.user.model.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.github.daniellfalcao.domain.user.model.UserDTO
import com.github.daniellfalcao.domain.user.model.UserDTO.Companion.birthdayDateFormat
import com.github.daniellfalcao.domain.user.model.UserDTO.Parrot
import com.proto.parrot.service.user.User
import java.text.ParseException
import java.util.Date

@Entity(tableName = "user")
data class UserEntity(
    @PrimaryKey @ColumnInfo(name = "id") val id: String,
    @ColumnInfo(name = "username") val username: String,
    @ColumnInfo(name = "birthday") val birthday: Date?,
    @ColumnInfo(name = "parrot") val parrot: Parrot,
    @ColumnInfo(name = "likes") var likes: Long,
    @ColumnInfo(name = "bookmarks") var bookmarks: Long,
)

fun User.toEntity() = UserEntity(
    id = id,
    username = username,
    birthday = try {
        birthdayDateFormat.parse(birthday)
    } catch (e: ParseException) {
        null
    },
    parrot = Parrot.from(parrot),
    likes = likes,
    bookmarks = bookmarks,
)

fun UserEntity.toDTO() = UserDTO(
    username = username,
    parrot = parrot,
    likes = likes,
    bookmarks = bookmarks,
)