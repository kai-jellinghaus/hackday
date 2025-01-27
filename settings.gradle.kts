pluginManagement {
    repositories {
        maven {
            val MAVEN_DOWNLOAD_URL = "MAVEN_DOWNLOAD_URL"
            val MAVEN_USER = "MAVEN_USER"
            val MAVEN_PASSWORD = "MAVEN_PASSWORD"

            val mavenDownloadURL = System.getenv(MAVEN_DOWNLOAD_URL)
            if (mavenDownloadURL == null) {
                throw GradleException("$MAVEN_DOWNLOAD_URL has to be set in environment")
            }
            var mavenUser = System.getenv(MAVEN_USER)
            if (mavenUser.isNullOrBlank()) {
                logger.info("$MAVEN_USER is not set in environment. Setting to \"x\"")
                mavenUser = "x"
            }
            var mavenPassword = System.getenv(MAVEN_PASSWORD)
            if (mavenPassword.isNullOrBlank()) {
                logger.info("$MAVEN_PASSWORD is not set in environment. Setting to \"x\"")
                mavenPassword = "x"
            }

            url = uri(mavenDownloadURL)
            credentials {
                username = mavenUser
                password = mavenPassword
            }
        }
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.PREFER_SETTINGS)
    repositories {
        maven {
            val MAVEN_DOWNLOAD_URL = "MAVEN_DOWNLOAD_URL"
            val MAVEN_USER = "MAVEN_USER"
            val MAVEN_PASSWORD = "MAVEN_PASSWORD"

            val mavenDownloadURL = System.getenv(MAVEN_DOWNLOAD_URL)
            if (mavenDownloadURL == null) {
                throw GradleException("$MAVEN_DOWNLOAD_URL has to be set in environment")
            }
            var mavenUser = System.getenv(MAVEN_USER)
            if (mavenUser.isNullOrBlank()) {
                logger.info("$MAVEN_USER is not set in environment. Setting to \"x\"")
                mavenUser = "x"
            }
            var mavenPassword = System.getenv(MAVEN_PASSWORD)
            if (mavenPassword.isNullOrBlank()) {
                logger.info("$MAVEN_PASSWORD is not set in environment. Setting to \"x\"")
                mavenPassword = "x"
            }

            url = uri(mavenDownloadURL)
            credentials {
                username = mavenUser
                password = mavenPassword
            }
        }
    }
}

plugins {
    id("com.supcis.java-toolchain-resolver") version "0.0.1"
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}

toolchainManagement {
    jvm {
        javaRepositories {
            repository("maven") {
                resolverClass.set(SupJavaToolchainResolver::class.java)

                val MAVEN_USER = "MAVEN_USER"
                val MAVEN_PASSWORD = "MAVEN_PASSWORD"

                var mavenUser = System.getenv(MAVEN_USER)
                if (mavenUser.isNullOrBlank()) {
                    logger.info("$MAVEN_USER is not set in environment")
                    mavenUser = "x"
                }
                var mavenPassword = System.getenv(MAVEN_PASSWORD)
                if (mavenPassword.isNullOrBlank()) {
                    logger.info("$MAVEN_PASSWORD is not set in environment")
                    mavenPassword = "x"
                }

                credentials {
                    username = mavenUser
                    password = mavenPassword
                }
            }
        }
    }
}

// @formatter:off
dependencyResolutionManagement {
    versionCatalogs {
        val log4jVersion = "2.23.0"
        val kotlinVersion= "2.1.0"
        create("thirdParty") {
            bundle("all", listOf())
        }
        create("infrastruktur") {
            plugin("version", "com.supcis.version").version("0.0.11")
            plugin("publish", "com.supcis.publish").version("0.0.6")
        }
        create("thirdPartyTest") {
            bundle("all", listOf())
        }
    }
}
// @formatter:on
