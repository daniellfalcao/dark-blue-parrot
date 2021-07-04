allprojects {
    repositories {
        google()
        mavenCentral()
    }
}

buildscript {

    repositories {
        google()
        mavenCentral()
    }
    dependencies {
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}