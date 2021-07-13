package com.github.daniellfalcao.test._module

import android.app.Application
import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import com.github.daniellfalcao.data._module.database.ParrotDatabase
import com.github.daniellfalcao.data.user.dao.TokenDao
import com.github.daniellfalcao.data.user.repository.UserLocalDataSource
import com.proto.parrot.service.authentication.AuthenticationServiceGrpcKt
import com.proto.parrot.service.authentication.RegisterServiceGrpcKt
import com.proto.parrot.service.post.PostServiceGrpcKt
import com.proto.parrot.service.user.UserServiceGrpcKt
import org.junit.After
import org.junit.Rule
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import org.koin.test.KoinTestRule
import org.koin.test.get
import java.util.concurrent.Executors
import kotlin.reflect.KMutableProperty
import kotlin.reflect.jvm.isAccessible

abstract class RepositoryTest : BaseKoinTest() {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @get:Rule
    open val koinTestRule = KoinTestRule.create {
        allowOverride(true)
        androidContext(application)
        modules(
            module {
                // shared preferences instance
                single {
                    val application = get<Application>()
                    application.getSharedPreferences("test-preferences", Context.MODE_PRIVATE)
                }

                // grpc services
                single { UserServiceGrpcKt.UserServiceCoroutineStub(get()) }
                single { RegisterServiceGrpcKt.RegisterServiceCoroutineStub(get()) }
                single { AuthenticationServiceGrpcKt.AuthenticationServiceCoroutineStub(get()) }
                single { PostServiceGrpcKt.PostServiceCoroutineStub(get()) }

                // database
                single<ParrotDatabase> {
                    Room.inMemoryDatabaseBuilder(get(), ParrotDatabase::class.java)
                        .setTransactionExecutor(Executors.newSingleThreadExecutor())
                        .allowMainThreadQueries()
                        .fallbackToDestructiveMigration()
                        .build().also { setDatabaseInstance(it) }
                }

                // DAO's
                single { get<ParrotDatabase>().userDao }
                single { TokenDao(get()) }

                // repositories
                single { UserLocalDataSource(get(), get()) }
            }
        )
    }

    @After
    fun closeDatabase() {
        get<ParrotDatabase>().close()
        setDatabaseInstance(null)
    }

    private fun setDatabaseInstance(instance: ParrotDatabase?) {
        (ParrotDatabase::class.nestedClasses.toMutableList()[0].members.toMutableList()[1] as KMutableProperty)
            .apply {
                isAccessible = true
                setter.call(ParrotDatabase::class, instance)
            }
    }

}