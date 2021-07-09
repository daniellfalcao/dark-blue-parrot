plugins {
    id("library")
}

dependencies {
    // modules dependencies
    implementation(project(Modules.Infrastructure.commonProject))
    api(project(Modules.Infrastructure.protoStubProject))
}