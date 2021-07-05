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

    implementation("androidx.core:core-ktx:1.6.0")
    implementation("androidx.appcompat:appcompat:1.3.0")
    implementation("com.google.android.material:material:1.3.0")
    implementation("androidx.constraintlayout:constraintlayout:2.0.4")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.3")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.4.0")
}