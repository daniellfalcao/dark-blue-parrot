package com.github.daniellfalcao.data.user.dao

import androidx.room.Dao
import androidx.room.Query
import com.github.daniellfalcao.data._module.database.dao.BaseDao
import com.github.daniellfalcao.data.user.model.entity.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao
abstract class UserDao : BaseDao<UserEntity>() {

    @Query("SELECT * FROM user")
    abstract suspend fun user(): UserEntity?

    @Query("DELETE FROM user WHERE id != :id")
    abstract suspend fun deleteUserDifferentThan(id: String)

    @Query("SELECT * FROM user")
    abstract fun flowUser(): Flow<UserEntity?>

}