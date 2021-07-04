object Modules {

    object Application {
        const val appProject = ":app"
        const val appProjectName = "app"
    }

    object Infrastructure {
        const val commonProject = ":infrastructure:common"
        const val commonProjectName = "common"
        const val dataProject = ":infrastructure:data"
        const val dataProjectName = "data"
        const val domainProject = ":infrastructure:domain"
        const val domainProjectName = "domain"
        const val testProject = ":infrastructure:test"
        const val testProjectName = "test"
        const val commonUIProject = ":infrastructure:common-ui"
        const val commonUIProjectName = "common-ui"
    }

    object Feature {
        const val authenticationProject = ":features:authentication"
        const val authenticationProjectName = "authentication"
        const val bookmarkProject = ":features:bookmark"
        const val bookmarkProjectName = "bookmark"
        const val homeProject = ":features:home"
        const val homeProjectName = "home"
        const val profileProject = ":features:profile"
        const val profileProjectName = "profile"
    }

}