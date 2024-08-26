import org.gradle.plugins.ide.idea.model.IdeaLanguageLevel

plugins {
    java
    idea
    `maven-publish`
    id("net.neoforged.moddev") version "2.0.16-beta"
}

val minecraftVersion: String by project
val minecraftVersionRange: String by project
val neoVersion: String by project
val neoVersionRange: String by project
val loaderVersionRange: String by project
val parchmentMcVersion: String by project
val parchmentVersion: String by project
val modId: String by project
val modName: String by project
val modLicense: String by project
val modVersion: String by project
val modGroupId: String by project
val modAuthors: String by project
val modDescription: String by project
val issVersionRange: String by project
val railcraftVersionRange: String by project

repositories {
    mavenLocal()
    exclusiveContent {
        forRepository {
            maven("https://cursemaven.com")
        }
        filter {
            includeGroup("curse.maven")
        }
    }
    maven("https://dl.cloudsmith.io/public/geckolib3/geckolib/maven/") {
        name = "GeckoLib"
        content {
            includeGroup("software.bernie.geckolib")
        }
    }

    flatDir {
        dir("libs")
    }
}

base {
    archivesName = modId
    group = modGroupId
    version = modVersion
}

java.toolchain.languageVersion = JavaLanguageVersion.of(21)

neoForge {
    version = neoVersion
    runs {
        register("client") {
            client()
//            jvmArguments.addAll("-XX:+IgnoreUnrecognizedVMOptions", "-XX:+AllowEnhancedClassRedefinition")
            systemProperty("mixin.debug.export", "true")
//            jvmArgument("-Dmixin.debug.export=true")
            gameDirectory = file("runs/client")
        }
        register("server") {
            server()
            programArgument("--nogui")
            gameDirectory = file("runs/server")
        }
        configureEach {
            logLevel = org.slf4j.event.Level.DEBUG
            jvmArgument("-Xmx1500m")
            jvmArgument("-XX:+AlwaysPreTouch")
            jvmArgument("-XX:+IgnoreUnrecognizedVMOptions")
            jvmArgument("-XX:+AllowEnhancedClassRedefinition")
            if (type.get() == "client") {
                programArguments.addAll("--width", "1920", "--height", "1080")
            }
        }
    }
    mods {
        register(modId) {
            sourceSet(sourceSets.main.get())
        }
    }
    parchment {
        minecraftVersion = parchmentMcVersion
        mappingsVersion = parchmentVersion
    }
}

sourceSets {
    main {
        resources.srcDir("src/generated/resources")
    }
}

dependencies {
    // Shame! 🔔
    val journeymap = "curse.maven:journeymap-32274:5616349" // "1.21-6.0.0-beta.20"
    val railcraft = "curse.maven:railcraft-reborn-901491:5650759" // "1.2.2"
    val spark = "curse.maven:spark-361579:5622205" // "1.10.97-neoforge"
    val pnmc = "curse.maven:pneumaticcraft-repressurized-281849:5619556" // "8.0.3"
    val iss = "curse.maven:irons-spells-n-spellbooks-855414:5659880" // "1.21-3.4.5"
    val geckolib = "1.21:4.5.8"
    val mousetweaks = "curse.maven:mouse-tweaks-60089:5637846" // 2.26.1
    val ftblibrary = "curse.maven:ftb-library-forge-404465:5634888" // 2101.1.1
    val jade = "curse.maven:jade-324717:5639932" // 15.1.8+neoforge
    val enderman = "curse.maven:enderman-overhaul-574409:5518192" //


    // Required
    compileOnly(journeymap)
    compileOnly(railcraft)
    compileOnly(spark)
    compileOnly(pnmc)
    compileOnly(iss)
    compileOnly(mousetweaks)
    compileOnly("software.bernie.geckolib:geckolib-neoforge-${geckolib}")
    compileOnly(ftblibrary)
    compileOnly(jade)
    compileOnly(enderman)

    // Testing at runtime
    runtimeOnly(journeymap)
    runtimeOnly(railcraft)
    runtimeOnly(spark)
    runtimeOnly(pnmc)
    runtimeOnly(iss)
    runtimeOnly(mousetweaks)
    runtimeOnly("software.bernie.geckolib:geckolib-neoforge-${geckolib}")
    runtimeOnly(ftblibrary)
    runtimeOnly(jade)
    runtimeOnly(enderman)

    // Dependencies
    runtimeOnly("curse.maven:caelus-308989:5442975") // caelus-neoforge-7.0.0+1.21.jar
    runtimeOnly("curse.maven:adorned-1036809:5652826") // curios-neoforge-9.1.2+1.21.0.jar
    runtimeOnly("curse.maven:architectury-api-419699:5553800") // architectury-13.0.6-neoforge.jar
    runtimeOnly("curse.maven:resourceful-lib-570073:5483169") // resourcefullib-neoforge-1.21-3.0.9.jar
    runtimeOnly("curse.maven:resourceful-config-714059:5548748") // resourcefulconfig-neoforge-1.21-3.0.3.jar

    runtimeOnly("blank:leakdiagtool:1.0.0")
}

tasks {
    processResources {
        val replaceProperties = mapOf(
            "minecraft_version" to minecraftVersion,
            "minecraft_version_range" to minecraftVersionRange,
            "neo_version" to neoVersion,
            "neo_version_range" to neoVersionRange,
            "loader_version_range" to loaderVersionRange,
            "mod_id" to modId,
            "mod_name" to modName,
            "mod_license" to modLicense,
            "mod_version" to modVersion,
            "mod_authors" to modAuthors,
            "mod_description" to modDescription,
            "iss_version_range" to issVersionRange,
            "railcraft_version_range" to railcraftVersionRange
        )

        inputs.properties(replaceProperties)
        filesMatching(listOf("META-INF/neoforge.mods.toml")) {
            expand(replaceProperties)
        }
    }
    compileJava {
        options.encoding = "UTF-8"
    }
}

publishing {
    publications {
        register<MavenPublication>("mavenJava") {
            from(components.getByName("java"))
        }
    }
    repositories {
        maven("file://$projectDir/repo")
    }
}

idea {
    project {
        jdkName = java.sourceCompatibility.toString()
        languageLevel = IdeaLanguageLevel(java.sourceCompatibility.toString())
    }
}
