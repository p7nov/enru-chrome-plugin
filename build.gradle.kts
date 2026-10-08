plugins {
    kotlin("multiplatform") version "2.1.0"
}

group = "org.psemyonov"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

kotlin {
    js {
        browser {
            webpackTask {
                mainOutputFileName = "background.js"
            }
            // Tests run on Node instead, so no browser is required
            testTask { enabled = false }
        }
        nodejs()
        binaries.executable()
    }

    sourceSets {
        jsTest.dependencies {
            implementation(kotlin("test"))
        }
    }
}

// Unpacked extension (background.js + manifest.json) ends up in build/dist/js/productionExecutable
tasks.register("buildExtension") {
    group = "build"
    description = "Builds the unpacked Chrome extension into build/dist/js/productionExecutable"
    dependsOn("jsBrowserDistribution")
}
