import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

plugins {
    id("library")
}

android {
    buildTypes {
        val localProperties = gradleLocalProperties(rootDir)
        val serverName = "GRPC_SERVER_ADDRESS" to localProperties.getProperty("grpc.address")
        val serverPort = "GRPC_SERVER_PORT" to localProperties.getProperty("grpc.port")
        getByName(Settings.BuildType.debug) {
            buildConfigField("String", serverName.first, "\"${serverName.second}\"")
            buildConfigField("int", serverPort.first, serverPort.second)
        }
        getByName(Settings.BuildType.release) {
            buildConfigField("String", serverName.first, "\"${serverName.second}\"")
            buildConfigField("int", serverPort.first, serverPort.second)
        }
    }
}

dependencies {
    // modules dependencies
    implementation(project(Modules.Infrastructure.commonProject))
    implementation(project(Modules.Infrastructure.domainProject))
}