plugins {
    id("library")
}

dependencies {
    api(Libraries.Kotlin.Coroutines.core)
    api(Libraries.Kotlin.Coroutines.android)
    api(Libraries.AndroidX.Lifecycle.self)
    api(Libraries.DI.koin)
    api(Libraries.DI.koinAndroid)
    api(Libraries.Timber.self)
}