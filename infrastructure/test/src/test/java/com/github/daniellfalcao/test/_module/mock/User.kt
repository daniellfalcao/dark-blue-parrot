package com.github.daniellfalcao.test._module.mock

import com.github.daniellfalcao.domain.user.model.UserDTO
import com.proto.parrot.service.user.User

val user1: User.Builder = User.newBuilder()
    .setId("id-teste-1")
    .setUsername("daniel")
    .setBirthday("15/12/1995")
    .setParrot(UserDTO.Parrot.BLUE.name)
    .setLikes(0)
    .setBookmarks(0)

val user2: User.Builder = User.newBuilder()
    .setId("id-teste-2")
    .setUsername("leinad")
    .setBirthday("20/02/1950")
    .setParrot(UserDTO.Parrot.YELLOW.name)
    .setLikes(6)
    .setBookmarks(10)