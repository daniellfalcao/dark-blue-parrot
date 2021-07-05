plugins {
    id("library")
}

dependencies {
    // modules dependencies
    implementation(project(Modules.Infrastructure.commonProject))
    implementation(project(Modules.Infrastructure.domainProject))
}