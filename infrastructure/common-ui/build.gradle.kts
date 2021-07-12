plugins {
    id("library")
}

dependencies {
    // modules dependencies
    implementation(project(Modules.Infrastructure.commonProject))
    implementation(project(Modules.Infrastructure.domainProject))
    // libraries dependencies
    api(Libraries.AndroidX.Lifecycle.self)
    api(Libraries.AndroidX.Lifecycle.runtime)
    api(Libraries.AndroidX.Lifecycle.common)
    api(Libraries.AndroidX.View.activity)
    api(Libraries.AndroidX.View.fragment)
    api(Libraries.AndroidX.View.appCompat)
    api(Libraries.AndroidX.View.constraintLayout)
    api(Libraries.AndroidX.View.recyclerView)
    api(Libraries.AndroidX.Navigation.fragmentKtx)
    api(Libraries.AndroidX.Navigation.uiKtx)
    api(Libraries.AndroidX.Navigation.dynamicFeatureFragment)
    api(Libraries.Google.MaterialComponents.self)
    api(Libraries.Google.Play.core)
    api(Libraries.Google.Play.coreKtx)
}