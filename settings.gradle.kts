pluginManagement {
    repositories {
        maven("https://maven.minecraftforge.net/")
        maven("https://prmaven.neoforged.net/ModDevGradle/pr118") {
            content {
                includeModule("net.neoforged.moddev.legacy", "net.neoforged.moddev.legacy.gradle.plugin")
                includeModule("net.neoforged.moddev", "net.neoforged.moddev.gradle.plugin")
                includeModule("net.neoforged.moddev.repositories", "net.neoforged.moddev.repositories.gradle.plugin")
                includeModule("net.neoforged", "moddev-gradle")
            }
        }
        gradlePluginPortal()
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}

rootProject.name = "AllTheLeaks-1.20.1"
