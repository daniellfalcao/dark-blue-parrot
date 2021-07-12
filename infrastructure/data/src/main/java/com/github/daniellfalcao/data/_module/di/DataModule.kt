package com.github.daniellfalcao.data._module.di

import android.app.Application
import android.content.Context
import com.github.daniellfalcao.common.di.DI
import com.github.daniellfalcao.data.BuildConfig
import com.github.daniellfalcao.data.R
import com.github.daniellfalcao.data._module.database.ParrotDatabase
import com.github.daniellfalcao.data.user.dao.TokenDao
import com.github.daniellfalcao.data.user.repository.UserLocalDataSource
import com.github.daniellfalcao.data.user.repository.UserRemoteDataSource
import com.github.daniellfalcao.data.user.repository.UserRepositoryImpl
import com.proto.parrot.service.authentication.AuthenticationServiceGrpcKt
import com.proto.parrot.service.authentication.RegisterServiceGrpcKt
import com.proto.parrot.service.post.PostServiceGrpcKt
import com.proto.parrot.service.user.UserServiceGrpcKt
import io.grpc.Channel
import io.grpc.ManagedChannelBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.asExecutor
import org.koin.dsl.module

object DataModule : DI {

    override fun koinModule() = module {

        // shared preferences instance
        single {
            val application = get<Application>()
            application.getSharedPreferences(
                application.getString(R.string.parrot_preferences_key),
                Context.MODE_PRIVATE
            )
        }

        // grpc channel
        single<Channel> {
            ManagedChannelBuilder.forAddress(
                BuildConfig.GRPC_SERVER_ADDRESS,
                BuildConfig.GRPC_SERVER_PORT
            ).usePlaintext()
                .executor(Dispatchers.IO.asExecutor())
                .build()
        }

        // grpc services
        single { UserServiceGrpcKt.UserServiceCoroutineStub(get()) }
        single { RegisterServiceGrpcKt.RegisterServiceCoroutineStub(get()) }
        single { AuthenticationServiceGrpcKt.AuthenticationServiceCoroutineStub(get()) }
        single { PostServiceGrpcKt.PostServiceCoroutineStub(get()) }

        // database
        single { ParrotDatabase.instance(get()) }

        // DAO's
        single { get<ParrotDatabase>().userDao }
        single { TokenDao(get()) }

        // repositories
        single { UserLocalDataSource(get(), get()) }
        single { UserRemoteDataSource(get(), get(), get()) }
        single { UserRepositoryImpl(get(), get()) }

    }
}