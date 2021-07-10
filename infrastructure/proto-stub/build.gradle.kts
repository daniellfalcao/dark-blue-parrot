import com.google.protobuf.gradle.*

plugins {
    id("com.android.library")
    id("kotlin-android")
    id("com.google.protobuf")
}

val generatedFilesDir = "$projectDir/build/generated/src/main"

android {
    compileSdkVersion(Settings.Sdk.targetVersion)
    buildToolsVersion = Settings.Sdk.toolsVersion
    sourceSets.getByName("main") {
        proto {
            srcDir("src/main/proto")
        }
        java.srcDirs(generatedFilesDir)
    }
}

dependencies {
    // modules dependencies
    implementation(project(Modules.Infrastructure.commonProject))
    // libraries dependencies
    api(Libraries.Proto.GRPC.protobufLite)
    api(Libraries.Proto.GRPC.kotlinStub)
    api(Libraries.Proto.Google.protobufJavaLite)
    runtimeOnly(Libraries.Proto.GRPC.okHttp)
}

protobuf {
    generatedFilesBaseDir = generatedFilesDir
    protoc {
        artifact = Libraries.Proto.Google.protoc
    }
    plugins {
        id("java") {
            artifact = Libraries.Proto.GRPC.genJava
        }
        id("grpc") {
            artifact = Libraries.Proto.GRPC.genJava
        }
        id("grpckt") {
            artifact = Libraries.Proto.GRPC.genKotlin
        }
    }
    generateProtoTasks {
        all().forEach {
            it.plugins {
                id("java") {
                    option("lite")
                }
                id("grpc") {
                    option("lite")
                }
                id("grpckt") {
                    option("lite")
                }
            }
        }
    }
}