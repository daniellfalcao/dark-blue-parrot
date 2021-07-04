object Settings {

    const val applicationId = "com.github.daniellfalcao.darkblueparrot"

    object Sdk {
        const val minVersion = 21
        const val targetVersion = 30
        const val toolsVersion = "30.0.3"
    }

    object Version {

        private const val versionMajor = "01"
        private const val versionMinor = "00"
        private const val versionPatch = "00"
        private const val versionBuild = "0"

        const val name = "$versionMajor.$versionMinor.$versionPatch"

        val code = versionMajor.toInt().times(1_000_000)
            .plus(versionMinor.toInt().times(1_000))
            .plus(versionPatch.toInt().times(100))
            .plus(versionBuild.toInt())
    }

    object BuildType {
        const val debug = "debug"
        const val release = "release"
    }

    object CompileOptions {
        val freeCompilerArgs = listOf("-Xopt-in=kotlin.RequiresOptIn")
    }

    object Proguard {
        const val android = "proguard-android-optimize.txt"
        const val consumer = "consumer-rules.pro"
        const val rules = "proguard-rules.pro"
    }

}