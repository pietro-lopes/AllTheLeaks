import org.gradle.plugins.ide.idea.model.IdeaLanguageLevel

plugins {
    java
    idea
    `maven-publish`
    id("net.neoforged.moddev") version "2.0.33-beta"
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
val geckolibVersionRange: String by project
val entityCullingVersionRange: String by project
val dummmmmmyVersionRange: String by project
val pneumaticcraftVersionRange: String by project
val ambientsoundsVersionRange: String by project
val occultismVersionRange: String by project

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
    maven {
        // saps.dev Maven (KubeJS and Rhino)
        url = uri("https://maven.saps.dev/releases")
        content {
            includeGroup("dev.latvian.mods")
            includeGroup("dev.latvian.apps")
        }
    }
    maven {
        url = uri("https://jitpack.io")
        content {
            includeGroup("com.github.rtyley")
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

sourceSets {
    main {
        resources.srcDir("src/generated/resources")
    }
    create("jei") {
        compileClasspath += sourceSets.main.get().compileClasspath
        runtimeClasspath += sourceSets.main.get().runtimeClasspath
    }
    create("emi") {
        compileClasspath += sourceSets.main.get().compileClasspath
        runtimeClasspath += sourceSets.main.get().runtimeClasspath
    }
    create("jeiServer") {
        compileClasspath += sourceSets.main.get().compileClasspath
        runtimeClasspath += sourceSets.main.get().runtimeClasspath
    }
    create("emiServer") {
        compileClasspath += sourceSets.main.get().compileClasspath
        runtimeClasspath += sourceSets.main.get().runtimeClasspath
    }
}

neoForge {
    version = neoVersion
    runs {
        register("client-jei") {
            client()
            sourceSet = sourceSets.getByName("jei")
        }
        register("client2-jei") {
            client()
            sourceSet = sourceSets.getByName("jei")
            programArguments.addAll("--username", "Dev2")
        }
        register("client-emi") {
            client()
            sourceSet = sourceSets.getByName("emi")
        }
        register("server-jei") {
            server()
//            programArgument("--nogui")
            gameDirectory = file("runs/server")
            sourceSet = sourceSets.getByName("jeiServer")
        }
        register("server-emi") {
            server()
//            programArgument("--nogui")
            gameDirectory = file("runs/server")
            sourceSet = sourceSets.getByName("emiServer")
        }
        configureEach {
            logLevel = org.slf4j.event.Level.DEBUG
            jvmArgument("-Xmx3000m")
            jvmArgument("-XX:+IgnoreUnrecognizedVMOptions")
            jvmArgument("-XX:+AllowEnhancedClassRedefinition")
            if (type.get().startsWith("client")) {
                programArguments.addAll("--width", "1920", "--height", "1080")
                gameDirectory = file("runs/client")
                systemProperty("mixin.debug.export", "true")
                jvmArguments.addAll(
                    "-XX:+UnlockExperimentalVMOptions",
                    "-XX:+UseG1GC",
                    "-XX:G1NewSizePercent=20",
                    "-XX:G1ReservePercent=20",
                    "-XX:MaxGCPauseMillis=50",
                    "-XX:G1HeapRegionSize=32M"
                )
                jvmArgument("-Xlog:safepoint:file=safepoint.log::filecount=0")
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

val jeiRuntimeOnly: Configuration by configurations.getting
val emiRuntimeOnly: Configuration by configurations.getting
val jeiServerRuntimeOnly: Configuration by configurations.getting
val emiServerRuntimeOnly: Configuration by configurations.getting
dependencies {
    // Here we go
    val journeymap = "curse.maven:journeymap-32274:5777475" // "1.21-6.0.0-beta.27"
    val railcraft = "curse.maven:railcraft-reborn-901491:5650759" // "1.2.2"
    val spark = "curse.maven:spark-361579:5759671" // "1.10.109-neoforge"
    val pnmc = "curse.maven:pneumaticcraft-repressurized-281849:5776294" // "8.1.4"
    val iss = "curse.maven:irons-spells-n-spellbooks-855414:5769235" // "1.21-3.7.1"
    val geckolib = "1.21.1:4.6.6"
    val mousetweaks = "curse.maven:mouse-tweaks-60089:5637846" // 2.26.1
    val ftblibrary = "curse.maven:ftb-library-forge-404465:5754910" // 2101.1.3
    val jade = "curse.maven:jade-324717:5639932" // 15.1.8+neoforge
    val entityCullingCompile = "blank:entityculling:neoforge-1.6.7-mc1.21" // 1.6.7
    val entityCullingRuntime = "blank:entityculling:neoforge-1.7.0-mc1.21" // 1.7.0
    val evilcraft = "curse.maven:evilcraft-74610:5776798" // 1.2.58
    val mekanism = "curse.maven:mekanism-268560:5680395" // 10.7.7.64
    val easyvillager = "curse.maven:easy-villagers-400514:5724571" // 1.1.23
    val iceberg = "curse.maven:iceberg-520110:5750025" // 1.2.8
    val findme = "curse.maven:findme-291936:5511906" // 3.3.1
    val ars = "curse.maven:ars-nouveau-401955:5721123" // 5.1.0
    val ftbquests = "curse.maven:ftb-quests-forge-289412:5635133" // 2101.1.0
    val securitycraft = "curse.maven:security-craft-64760:5655403" // 1.9.10-beta9
    val lootr = "curse.maven:lootr-361276:5709012" // 10.33.82
    val dummmmmmy = "curse.maven:mmmmmmmmmmmm-225738:5779508" // 1.21-2.0.6
    val jeiEmi = "curse.maven:jei-238222:5692315" // jei-1.21.1-neoforge-19.16.4.168.jar
    val jei = "curse.maven:jei-238222:5781938" // jei-1.21.1-neoforge-19.19.6.236.jar
    val etf = "curse.maven:entity-texture-features-fabric-568563:5734430" // entity_texture_features_neoforge_1.21-6.2.5.jar
    val emf = "curse.maven:entity-model-features-844662:5722727" // entity_model_features_neoforge_1.21-2.2.6.jar
    val sereneSeasons = "curse.maven:serene-seasons-291874:5753503" // SereneSeasons-neoforge-1.21.1-10.1.0.1.jar
    val connectivity = "curse.maven:connectivity-470193:5728632" // connectivity-1.21-5.8.jar
    val emi = "curse.maven:emi-580555:5769216" // emi-1.1.14+1.21.1+neoforge.jar
    val emiLoot = "curse.maven:emi-loot-681783:5760220" // emi_loot-0.7.4+1.21+neoforge.jar
    val minecoloniesCompile = "curse.maven:minecolonies-245506:5734626" // minecolonies-1.1.701-1.21.1-snapshot.jar
    val minecoloniesJei = "curse.maven:minecolonies-245506:5809182" // minecolonies-1.1.726-1.21.1-snapshot.jar
    val creativecore = "curse.maven:creativecore-257814:5773866" // CreativeCore_NEOFORGE_v2.12.17_mc1.21.1.jar
    val ambientsounds = "curse.maven:ambientsounds-254284:5744185" // AmbientSounds_NEOFORGE_v6.1.2_mc1.21.1.jar
    val occultism = "curse.maven:occultism-361026:5793616" // occultism-1.21.1-neoforge-1.161.3.jar
    val mahou = "curse.maven:mahou-tsukai-342543:5754205" // mahoutsukai-1.21.0-v1.35.18.jar
    val kubejs = "2101.7.1-build.181"
    val rhino = "2101.2.5-build.54"
    val sophCore = "curse.maven:sophisticated-core-618298:5810072" // sophisticatedcore-1.21-0.6.45.722.jar
    val athena = "curse.maven:athena-841890:5629395" // athena-neoforge-1.21-4.0.1.jar

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
    compileOnly(entityCullingCompile)
    compileOnly(evilcraft)
    compileOnly(mekanism)
    compileOnly(easyvillager)
    compileOnly(iceberg)
    compileOnly(findme)
    compileOnly(ars)
    compileOnly(ftbquests)
    compileOnly(securitycraft)
    compileOnly(lootr)
    compileOnly(dummmmmmy)
    compileOnly(jei)
    compileOnly(etf)
    compileOnly(emf)
    compileOnly(sereneSeasons)
    compileOnly(connectivity)
    compileOnly(emi)
    compileOnly(emiLoot)
    compileOnly(minecoloniesCompile)
    compileOnly(creativecore)
    compileOnly(ambientsounds)
    compileOnly(occultism)
    compileOnly(mahou)
    implementation(interfaceInjectionData("dev.latvian.mods:kubejs-neoforge:$kubejs")!!)
    implementation("dev.latvian.mods:rhino:$rhino")
    compileOnly(sophCore)
    compileOnly(athena)

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
    runtimeOnly(entityCullingRuntime)
    runtimeOnly(evilcraft)
    runtimeOnly(mekanism)
    runtimeOnly(easyvillager)
    runtimeOnly(iceberg)
    runtimeOnly(findme)
    runtimeOnly(ars)
    runtimeOnly(ftbquests)
    runtimeOnly(securitycraft)
    runtimeOnly(lootr)
    runtimeOnly(dummmmmmy)

    runtimeOnly(connectivity)
    runtimeOnly(creativecore)
    runtimeOnly(ambientsounds)
    runtimeOnly(occultism)
    runtimeOnly(mahou)
    runtimeOnly(sophCore)
    runtimeOnly(athena)


    jeiRuntimeOnly(etf)
    jeiRuntimeOnly(emf)
//    jeiRuntimeOnly(sereneSeasons)
    emiRuntimeOnly(etf)
    emiRuntimeOnly(emf)
//    emiRuntimeOnly(sereneSeasons)

    jeiRuntimeOnly(jei)
    jeiServerRuntimeOnly(jei)
    jeiRuntimeOnly(minecoloniesJei)
    jeiServerRuntimeOnly(minecoloniesJei)

    emiRuntimeOnly(jeiEmi)
    emiServerRuntimeOnly(jeiEmi)
    emiRuntimeOnly(emi)
    emiServerRuntimeOnly(emi)
    emiRuntimeOnly(emiLoot)
    emiServerRuntimeOnly(emiLoot)
    emiRuntimeOnly(minecoloniesCompile)
    emiServerRuntimeOnly(minecoloniesCompile)

    // Dependencies for runtime
    runtimeOnly("curse.maven:caelus-308989:5442975") // caelus-neoforge-7.0.0+1.21.jar
    runtimeOnly("curse.maven:adorned-1036809:5652826") // curios-neoforge-9.1.2+1.21.0.jar
    runtimeOnly("curse.maven:architectury-api-419699:5553800") // architectury-13.0.6-neoforge.jar
    runtimeOnly("curse.maven:resourceful-lib-570073:5483169") // resourcefullib-neoforge-1.21-3.0.9.jar
    runtimeOnly("curse.maven:resourceful-config-714059:5548748") // resourcefulconfig-neoforge-1.21-3.0.3.jar
    runtimeOnly("curse.maven:cyclops-core-232758:5666110") // cyclopscore-1.21.1-neoforge-1.20.5-581.jar
    runtimeOnly("curse.maven:ftb-teams-forge-404468:5631446") // ftb-teams-neoforge-2101.1.0.jar
    runtimeOnly("curse.maven:playeranimator-658587:5698755") // player-animation-lib-forge-2.0.0-alpha1+1.21.jar
    runtimeOnly("curse.maven:selene-499980:5764367")
//    jeiRuntimeOnly("curse.maven:glitchcore-955399:5660740") // GlitchCore-neoforge-1.21.1-2.1.0.0.jar
//    emiRuntimeOnly("curse.maven:glitchcore-955399:5660740") // GlitchCore-neoforge-1.21.1-2.1.0.0.jar
    runtimeOnly("curse.maven:cupboard-326652:5570763") // cupboard-1.21-2.8.jar
    runtimeOnly("curse.maven:fzzy-config-1005914:5778237") // fzzy_config-0.5.1+1.20.6+neoforge.jar
    runtimeOnly("curse.maven:kotlin-for-forge-351264:5611971") // kotlinforforge-5.5.0-all.jar
    runtimeOnly("curse.maven:blockui-522992:5782223") // blockui-1.0.192-1.21.1-snapshot.jar
    runtimeOnly("curse.maven:domum-ornamentum-527361:5764083") // domum_ornamentum-1.0.204-1.21.1-snapshot.jar
    runtimeOnly("curse.maven:multi-piston-303278:5783614") // multipiston-1.2.51-1.21.1-snapshot.jar
    runtimeOnly("curse.maven:structurize-298744:5761666") // structurize-1.0.752-1.21.1-snapshot.jar
    runtimeOnly("curse.maven:towntalk-900364:5653504") // towntalk-1.2.0.jar
    runtimeOnly("curse.maven:modonomicon-538392:5786073") // modonomicon-1.21.1-neoforge-1.108.1.jar
    runtimeOnly("curse.maven:smartbrainlib-661293:5723837") // SmartBrainLib-neoforge-1.21.1-1.16.1.jar
    runtimeOnly("curse.maven:sophisticated-backpacks-422301:5787622") // sophisticatedbackpacks-1.21-3.20.17.1113.jar

    // LeakDiagTool
    runtimeOnly("blank:leakdiagtool:0.0.1-beta")
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
            "railcraft_version_range" to railcraftVersionRange,
            "geckolib_version_range" to geckolibVersionRange,
            "entityculling_version_range" to entityCullingVersionRange,
            "dummmmmmy_version_range" to dummmmmmyVersionRange,
            "pneumaticcraft_version_range" to pneumaticcraftVersionRange,
            "ambientsounds_version_range" to ambientsoundsVersionRange,
            "occultism_version_range" to occultismVersionRange
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
