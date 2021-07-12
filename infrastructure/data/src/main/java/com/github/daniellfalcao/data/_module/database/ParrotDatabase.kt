package com.github.daniellfalcao.data._module.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.github.daniellfalcao.data._module.database.converters.DateConverter
import com.github.daniellfalcao.data.user.dao.UserDao
import com.github.daniellfalcao.data.user.model.entity.UserEntity
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Database(
    entities = [UserEntity::class],
    version = 1
)
@TypeConverters(DateConverter::class)
abstract class ParrotDatabase : RoomDatabase() {

    abstract val userDao: UserDao

    companion object {

        private const val DATABASE_NAME = "parrot-DB"

        private var db: ParrotDatabase? = null

        fun instance(context: Context): ParrotDatabase {
            return db ?: synchronized(this) {
                db ?: synchronized(this) { buildDatabase(context).also { db = it } }
            }
        }

        @OptIn(DelicateCoroutinesApi::class)
        suspend fun dropDatabase() = withContext(Dispatchers.Default) {
            db?.runInTransaction { db?.clearAllTables() }
        }


        private fun buildDatabase(context: Context): ParrotDatabase {
            return Room.databaseBuilder(context, ParrotDatabase::class.java, DATABASE_NAME)
                .fallbackToDestructiveMigration()
                .build()
        }

    }
}