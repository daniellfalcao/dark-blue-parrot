plugins {
    id("library")
}

dependencies {
    // modules dependencies
    implementation(project(Modules.Infrastructure.commonProject))
    implementation(project(Modules.Infrastructure.domainProject))
    implementation(project(Modules.Infrastructure.dataProject))
    // test libraries dependencies
    testImplementation(Libraries.AndroidX.Test.runner)
    testImplementation(Libraries.AndroidX.Test.core)
    testImplementation(Libraries.AndroidX.Test.junit)
    testImplementation(Libraries.AndroidX.Test.coreTesting)
    testImplementation(Libraries.DI.Test.koinKotlin)
    testImplementation(Libraries.DI.Test.koinJUnit)
    testImplementation(Libraries.Test.JUnit.self)
    testImplementation(Libraries.Test.Robolectric.self)
}