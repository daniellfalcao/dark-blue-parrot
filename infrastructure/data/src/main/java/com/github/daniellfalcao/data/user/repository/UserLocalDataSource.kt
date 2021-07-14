package com.github.daniellfalcao.data.user.repository

import com.github.daniellfalcao.data.user.dao.TokenDao
import com.github.daniellfalcao.data.user.dao.UserDao
import com.github.daniellfalcao.data.user.model.entity.toEntity
import com.proto.parrot.service.user.User
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull

class UserLocalDataSource(
    private val userDao: UserDao,
    private val tokenDao: TokenDao
) {

    suspend fun user() = userDao.user()

    suspend fun saveUser(user: User) {
        val entity = user.toEntity()
        userDao.runInTransaction {
            userDao.deleteUserDifferentThan(entity.id)
            userDao.insertOrUpdate(entity)
        }
    }

    suspend fun token() = tokenDao.token()

    suspend fun saveToken(token: String) {
        tokenDao.saveToken(token)
    }

    suspend fun deleteToken() {
        tokenDao.deleteToken()
    }

    fun flowUser() = userDao.flowUser().filterNotNull().distinctUntilChanged()

}