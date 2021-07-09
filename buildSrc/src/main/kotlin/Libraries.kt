object Libraries {

    object Kotlin {

        object Coroutines {
            private const val version = "1.5.0"
            // releases (https://mvnrepository.com/artifact/org.jetbrains.kotlinx/kotlinx-coroutines-core)
            const val core = "org.jetbrains.kotlinx:kotlinx-coroutines-core:$version"
            // releases (https://mvnrepository.com/artifact/org.jetbrains.kotlinx/kotlinx-coroutines-android)
            const val android = "org.jetbrains.kotlinx:kotlinx-coroutines-android:$version"

            object Test {
                // releases (https://mvnrepository.com/artifact/org.jetbrains.kotlinx/kotlinx-coroutines-test)
                const val android = "org.jetbrains.kotlinx:kotlinx-coroutines-test:$version"
            }

        }
    }

    object AndroidX {

        object View {
            // releases (https://maven.google.com/web/index.html#androidx.core:core-ktx)
            const val core = "androidx.core:core-ktx:1.6.0"
            // releases (https://maven.google.com/web/index.html#androidx.activity:activity-ktx)
            const val activity = "androidx.activity:activity-ktx:1.2.3"
            // releases (https://maven.google.com/web/index.html#androidx.fragment:fragment-ktx)
            const val fragment = "androidx.fragment:fragment-ktx:1.3.5"
            // releases (https://maven.google.com/web/index.html#androidx.recyclerview:recyclerview)
            const val recyclerView = "androidx.recyclerview:recyclerview:1.2.1"
            // releases (https://maven.google.com/web/index.html#androidx.appcompat:appcompat)
            const val appCompat = "androidx.appcompat:appcompat:1.3.0"
            // releases (https://maven.google.com/web/index.html#androidx.constraintlayout:constraintlayout)
            const val constraintLayout = "androidx.constraintlayout:constraintlayout:2.0.4"
        }

        object Lifecycle {
            // releases (https://maven.google.com/web/index.html#androidx.lifecycle:lifecycle-extensions)
            const val self = "androidx.lifecycle:lifecycle-extensions:2.2.0"
            // releases (https://maven.google.com/web/index.html#androidx.lifecycle:lifecycle-runtime-ktx)
            const val runtime =  "androidx.lifecycle:lifecycle-runtime-ktx:2.3.1"
            // releases (https://maven.google.com/web/index.html#androidx.lifecycle:lifecycle-common)
            const val common =  "androidx.lifecycle:lifecycle-common:2.3.1"
        }

        object Navigation {
            // releases (https://maven.google.com/web/index.html#androidx.navigation)
            private const val version = "2.3.5"
            const val fragmentKtx = "androidx.navigation:navigation-fragment-ktx:$version"
            const val uiKtx = "androidx.navigation:navigation-ui-ktx:$version"
            const val dynamicFeatureFragment = "androidx.navigation:navigation-dynamic-features-fragment:$version"
        }

        object Test {
            // releases (https://maven.google.com/web/index.html#androidx.test:runner)
            const val runner = "androidx.test:runner:1.4.0"
            // releases (https://maven.google.com/web/index.html#androidx.test.ext:junit)
            const val junit =  "androidx.test.ext:junit:1.1.3"
            // releases (https://maven.google.com/web/index.html#androidx.test:core-ktx)
            const val core = "androidx.test:core-ktx:1.4.0"
            // releases (https://maven.google.com/web/index.html#androidx.arch.core:core-testing)
            const val coreTesting = "androidx.arch.core:core-testing:2.1.0"
        }
    }

    object Google {
        object MaterialComponents {
            // releases (https://maven.google.com/web/index.html#com.google.android.material:material)
            const val self = "com.google.android.material:material:1.4.0"
        }
        object Play {
            // releases (https://maven.google.com/web/index.html#com.google.android.play:core)
            const val core = "com.google.android.play:core:1.10.0"
            // releases (https://maven.google.com/web/index.html#com.google.android.play:core-ktx)
            const val coreKtx = "com.google.android.play:core-ktx:1.8.1"
        }
    }

    // TODO: releases
    object Proto {

        object GRPC {
            const val protobufLite = "io.grpc:grpc-protobuf-lite:1.37.0"
            const val kotlinStub = "io.grpc:grpc-kotlin-stub:1.1.0"
            const val genJava = "io.grpc:protoc-gen-grpc-java:1.37.0"
            const val genKotlin = "io.grpc:protoc-gen-grpc-kotlin:1.1.0:jdk7@jar"
        }

        object Google {
            private const val version = "3.15.8"
            const val protobufJavaLite = "com.google.protobuf:protobuf-javalite:$version"
            const val protoc = "com.google.protobuf:protoc:$version"
        }
    }

    object DI {
        // releases (https://github.com/InsertKoinIO/koin/releases)
        private const val version = "3.1.2"
        const val koin = "io.insert-koin:koin-core:$version"
        const val koinAndroid = "io.insert-koin:koin-android:$version"

        object Test {
            const val koinKotlin = "io.insert-koin:koin-test:$version"
            const val koinJUnit = "io.insert-koin:koin-test-junit4:$version"
        }

    }

    object Timber {
        // releases (https://github.com/JakeWharton/timber/releases)
        const val self = "com.jakewharton.timber:timber:4.7.1"
    }

    object Test {

        object Mockito {
            // releases (https://mvnrepository.com/artifact/org.mockito/mockito-core)
            const val core = "org.mockito:mockito-core:3.1.0"
            const val kotlin = "com.nhaarman.mockitokotlin2:mockito-kotlin:2.2.0"
        }

        object JUnit {
            // releases (https://mvnrepository.com/artifact/junit/junit)
            const val self = "junit:junit:4.13.2"
        }

        object Robolectric {
            // releases (https://github.com/robolectric/robolectric/releases)
            const val self = "org.robolectric:robolectric:4.6.1"
        }

    }

}