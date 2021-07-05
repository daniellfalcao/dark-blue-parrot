plugins {
    id("com.android.application")
    id("kotlin-android")
    id("org.jetbrains.kotlin.plugin.parcelize")
}

android {

    compileSdkVersion(Settings.Sdk.targetVersion)
    buildToolsVersion(Settings.Sdk.toolsVersion)

    defaultConfig {
        applicationId = Settings.applicationId
        minSdkVersion(Settings.Sdk.minVersion)
        targetSdkVersion(Settings.Sdk.targetVersion)
        versionCode = Settings.Version.code
        versionName = Settings.Version.name
    }

    buildTypes {
        getByName(Settings.BuildType.release) {
            minifyEnabled(true)
            proguardFiles(
                getDefaultProguardFile(Settings.Proguard.android),
                Settings.Proguard.rules
            )
        }
    }

    kotlinOptions {
        freeCompilerArgs = Settings.CompileOptions.freeCompilerArgs
    }

    dynamicFeatures = mutableSetOf(
        Modules.Feature.authenticationProject,
        Modules.Feature.homeProject,
        Modules.Feature.bookmarkProject,
        Modules.Feature.profileProject
    )

    buildFeatures.viewBinding = true

}

dependencies {
    // modules dependencies
    implementation(project(Modules.Infrastructure.commonProject))
    implementation(project(Modules.Infrastructure.commonUIProject))
    implementation(project(Modules.Infrastructure.domainProject))
    implementation(project(Modules.Infrastructure.dataProject))
    // libraries dependencies
    implementation(Libraries.AndroidX.Lifecycle.self)
    implementation(Libraries.AndroidX.Lifecycle.runtime)
    implementation(Libraries.AndroidX.Lifecycle.common)
}