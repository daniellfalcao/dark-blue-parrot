package com.github.daniellfalcao.data.user.dao

import androidx.room.Dao
import androidx.room.Query
import com.github.daniellfalcao.data._module.database.dao.BaseDao
import com.github.daniellfalcao.data.user.model.entity.UserEntity
import com.github.daniellfalcao.data.user.model.view.ProfileView
import kotlinx.coroutines.flow.Flow

@Dao
abstract class UserDao : BaseDao<UserEntity>() {

    @Query("SELECT * FROM user")
    abstract suspend fun user(): UserEntity?

    @Query("SELECT * FROM user")
    abstract fun flowProfile(): Flow<ProfileView?>

}