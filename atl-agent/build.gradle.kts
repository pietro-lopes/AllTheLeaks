import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

plugins {
    `java-library`
    `maven-publish`
    idea
}

repositories {
    maven { url = uri("https://maven.neoforged.net/releases") }
    maven {
        name = "Mojang"
        url = uri("https://libraries.minecraft.net")
    }
    mavenCentral()
}

base {
    archivesName = "atl_agent"
    group = "dev.uncandango.atl_agent"
    version = "1.0.0"
}

java.toolchain.languageVersion = JavaLanguageVersion.of(21)

//java.disableAutoTargetJvm()

dependencies {
    compileOnly("org.jetbrains:annotations:24.0.1")

    compileOnly("com.mojang:datafixerupper:8.0.16")
    compileOnly("cpw.mods:securejarhandler:3.0.8")

    // Logger
    compileOnly("org.slf4j:slf4j-api:2.0.9")

    // ASM
    implementation("org.ow2.asm:asm:9.7")
    compileOnly("org.ow2.asm:asm-analysis:9.7")
    compileOnly("org.ow2.asm:asm-commons:9.7")
    compileOnly("org.ow2.asm:asm-tree:9.7")
    compileOnly("org.ow2.asm:asm-util:9.7")

    // LWJGL
    compileOnly("org.lwjgl:lwjgl:3.3.3")

}

tasks.jar {
    manifest {
        attributes(
            mapOf(
                // "FMLModType" to "LIBRARY",
                "Can-Redefine-Classes" to "false",
                "Can-Retransform-Classes" to "false",
                "Can-Set-Native-Method-Prefix" to "false",
                "Premain-Class" to "dev.uncandango.atl_agent.ATLAgent",
                "Automatic-Module-Name" to "dev.uncandango.atl_agent",
//                "Specification-Title" to "atl_agent",
//                "Specification-Vendor" to "Uncandango",
//                "Specification-Version" to "1",
//                "Implementation-Title" to project.name,
//                "Implementation-Version" to "1.0.0",
//                "Implementation-Vendor" to "Uncandango",
//                "Implementation-Timestamp" to LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
            )
        )
    }
}

java {
    withSourcesJar()
}

idea {
    module {
        isDownloadJavadoc = true
        isDownloadSources = true
    }
}
