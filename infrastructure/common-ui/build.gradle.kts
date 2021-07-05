plugins {
    id("library")
}

dependencies {
    // modules dependencies
    implementation(project(Modules.Infrastructure.commonProject))
    // libraries dependencies
    api(Libraries.AndroidX.View.core)
    api(Libraries.AndroidX.View.activity)
    api(Libraries.AndroidX.View.fragment)
    api(Libraries.AndroidX.View.appCompat)
    api(Libraries.AndroidX.View.constraintLayout)
    api(Libraries.AndroidX.View.recyclerView)
    api(Libraries.Google.MaterialComponents.self)
}