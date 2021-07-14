plugins {
    id("library")
    kotlin("kapt")
}
android {
    testOptions {
        unitTests {
            isIncludeAndroidResources = true
        }
    }

}

dependencies {
    // modules dependencies
    implementation(project(Modules.Infrastructure.commonProject))
    implementation(project(Modules.Infrastructure.domainProject))
    implementation(project(Modules.Infrastructure.dataProject))
    // test libraries dependencies
    testImplementation(Libraries.Kotlin.Reflect.self)
    testImplementation(Libraries.AndroidX.Test.runner)
    testImplementation(Libraries.AndroidX.Test.core)
    testImplementation(Libraries.AndroidX.Test.junit)
    testImplementation(Libraries.AndroidX.Test.coreTesting)
    testImplementation(Libraries.AndroidX.Test.roomTesting)
    testImplementation(Libraries.DI.Test.koinKotlin)
    testImplementation(Libraries.DI.Test.koinJUnit)
    testImplementation(Libraries.Kotlin.Coroutines.Test.self)
    testImplementation(Libraries.Test.JUnit.self)
    testImplementation(Libraries.Test.Robolectric.self)
    testImplementation(Libraries.Test.Mockito.core)
    testImplementation(Libraries.Test.Mockito.inline)
    testImplementation(Libraries.Test.Mockito.kotlin)
}