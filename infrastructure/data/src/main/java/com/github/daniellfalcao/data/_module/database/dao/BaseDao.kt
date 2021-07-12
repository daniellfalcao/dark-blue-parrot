package com.github.daniellfalcao.data._module.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Transaction
import androidx.room.Update


@Dao
abstract class BaseDao<T> {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    protected abstract suspend fun insert(data: @JvmSuppressWildcards T): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    protected abstract suspend fun insert(vararg data: @JvmSuppressWildcards T)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    protected abstract suspend fun update(data: @JvmSuppressWildcards T): Int

    @Update(onConflict = OnConflictStrategy.REPLACE)
    protected abstract suspend fun update(vararg data: @JvmSuppressWildcards T)

    open suspend fun insertOrUpdate(vararg data: @JvmSuppressWildcards T) {
        data.forEach { if (insert(it) == DATA_NOT_INSERTED) update(it) }
    }

    open suspend fun insertOrUpdate(data: List<@JvmSuppressWildcards T>) {
        data.forEach { if (insert(it) == DATA_NOT_INSERTED) update(it) }
    }

    open suspend fun insertOrUpdate(data: @JvmSuppressWildcards T): Long {
        var dataID = insert(data)
        if (dataID == DATA_NOT_INSERTED) dataID = update(data).toLong()
        return dataID
    }

    @Transaction
    open suspend fun runInTransaction(operations: suspend () -> Unit) {
        operations()
    }

    companion object {
        const val DATA_NOT_INSERTED = -1L
    }

}