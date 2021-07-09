plugins {
    `java-gradle-plugin`
    `kotlin-dsl`
    `kotlin-dsl-precompiled-script-plugins`
}

repositories {
    jcenter()
    google()
}

dependencies {
    implementation(kotlin("gradle-plugin", version = "1.5.20"))
    implementation("com.android.tools.build:gradle:4.2.2" )
    implementation("androidx.navigation:navigation-safe-args-gradle-plugin:2.3.5")
    implementation("com.google.protobuf:protobuf-gradle-plugin:0.8.16")
}