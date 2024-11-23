import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import com.google.gson.stream.JsonReader
import org.gradle.plugins.ide.idea.model.IdeaLanguageLevel
import java.nio.file.Files

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

    maven {
        name = "Iron's Maven - Release"
        url = uri("https://code.redspace.io/releases")
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
            gameDirectory = file("runs/server")
            sourceSet = sourceSets.getByName("jeiServer")
        }
        register("server-emi") {
            server()
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
                systemProperty("alltheleaks.indev", "true")
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
    val iss = "1.21-3.7.1" //  "curse.maven:irons-spells-n-spellbooks-855414:5769235" // "1.21-3.7.1"
    val geckolib = "1.21.1:4.6.6"
    val mousetweaks = "curse.maven:mouse-tweaks-60089:5637846" // 2.26.1
    val ftblibrary = "curse.maven:ftb-library-forge-404465:5754910" // 2101.1.3
    val jade = "curse.maven:jade-324717:5639932" // 15.1.8+neoforge
    val evilcraft = "curse.maven:evilcraft-74610:5602422" // 1.2.50
    val mekanism = "curse.maven:mekanism-268560:5680395" // 10.7.7.64
    val easyvillager = "curse.maven:easy-villagers-400514:5724571" // 1.1.23
    val iceberg = "curse.maven:iceberg-520110:5750025" // 1.2.8
    val findme = "curse.maven:findme-291936:5511906" // 3.3.1
    val ars = "curse.maven:ars-nouveau-401955:5721123" // 5.1.0
    val ftbquests = "curse.maven:ftb-quests-forge-289412:5635133" // 2101.1.0
    val securitycraft = "curse.maven:security-craft-64760:5655403" // 1.9.10-beta9
    val lootr = "curse.maven:lootr-361276:5709012" // 10.33.82
    val dummmmmmy = "curse.maven:mmmmmmmmmmmm-225738:5779508" // 1.21-2.0.6
    val jeiEmi = "curse.maven:jei-238222:5781415" // jei-1.21.1-neoforge-19.19.6.235.jar
    val jei = "curse.maven:jei-238222:5781938" // jei-1.21.1-neoforge-19.19.6.236.jar
    val jeiRuntime = "curse.maven:jei-238222:5846880" // jei-1.21.1-neoforge-19.21.0.247.jar
    val etf = "curse.maven:entity-texture-features-fabric-568563:5734430" // entity_texture_features_neoforge_1.21-6.2.5.jar
    val etfRuntime = "curse.maven:entity-texture-features-fabric-568563:5874160" // entity_texture_features_neoforge_1.21.1-6.2.7.jar
    val emf = "curse.maven:entity-model-features-844662:5722727" // entity_model_features_neoforge_1.21-2.2.6.jar
    val emfRuntime = "curse.maven:entity-model-features-844662:5722727" // entity_model_features_neoforge_1.21-2.2.6.jar
    val sereneSeasons = "curse.maven:serene-seasons-291874:5753503" // SereneSeasons-neoforge-1.21.1-10.1.0.1.jar
    val connectivity = "curse.maven:connectivity-470193:5728632" // connectivity-1.21-5.8.jar
    val emi = "curse.maven:emi-580555:5769216" // emi-1.1.14+1.21.1+neoforge.jar
    val emiRuntime = "curse.maven:emi-580555:5872513" // emi-1.1.18+1.21.1+neoforge.jar
    val emiLoot = "curse.maven:emi-loot-681783:5760220" // emi_loot-0.7.4+1.21+neoforge.jar
    val minecoloniesCompile = "curse.maven:minecolonies-245506:5734626" // minecolonies-1.1.701-1.21.1-snapshot.jar
    val minecoloniesRuntime = "curse.maven:minecolonies-245506:5879446" // minecolonies-1.1.774-1.21.1-snapshot.jar
    val creativecore = "curse.maven:creativecore-257814:5773866" // CreativeCore_NEOFORGE_v2.12.17_mc1.21.1.jar
    val ambientsounds = "curse.maven:ambientsounds-254284:5744185" // AmbientSounds_NEOFORGE_v6.1.2_mc1.21.1.jar
    val occultism = "curse.maven:occultism-361026:5793616" // occultism-1.21.1-neoforge-1.161.3.jar
    val mahou = "curse.maven:mahou-tsukai-342543:5754205" // mahoutsukai-1.21.0-v1.35.18.jar
    val kubejs = "2101.7.1-build.181"
    val rhino = "2101.2.5-build.54"
    val sophCore = "curse.maven:sophisticated-core-618298:5810072" // sophisticatedcore-1.21-0.6.45.722.jar
    val athena = "curse.maven:athena-841890:5629395" // athena-neoforge-1.21-4.0.1.jar
    val jep = "curse.maven:just-enough-professions-jep-417645:5805652" // JustEnoughProfessions-neoforge-1.21.1-4.0.3.jar
    val jepRuntime = "curse.maven:just-enough-professions-jep-417645:5805652" // JustEnoughProfessions-neoforge-1.21.1-4.0.3.jar
    val jdt = "curse.maven:just-dire-things-1002348:5615453" // justdirethings-1.0.0.jar
    val curios = "curse.maven:curios-continuation-1037991:5638950" // curios-neoforge-9.0.6+1.21.jar
    val reliquary = "curse.maven:reliquary-reincarnations-241319:5860336" // reliquary-1.21.1-2.0.44.1245.jar
    val stp = "curse.maven:smithing-template-viewer-1133580:5871981" // smithingtemplateviewer-1.0.0.jar
    val travelersBackpack = "curse.maven:travelers-backpack-321117:5602831" // travelersbackpack-neoforge-1.21-10.0.5.jar
    val accessories = "curse.maven:accessories-938917:5848473" // accessories
    val accessoriesCclayer = "curse.maven:accessories-cc-layer-1005683:5610040" // accessories_cclayer-9.0.5-beta.6+1.21.jar
    val genetics = "curse.maven:genetics-resequenced-1040563:5613298" // geneticsresequenced-1.21-1.1.10.jar
    val buildingGadgets = "curse.maven:building-gadgets-298187:5615703" // buildinggadgets2-1.3.7.jar

    // You need to download yourself and put on "libs" folder
    // https://www.curseforge.com/minecraft/mc-mods/entityculling/files/all?page=1&pageSize=20&version=1.21.1&gameVersionTypeId=6
    val entityCullingCompile = "blank:entityculling:neoforge-1.6.7-mc1.21" // 1.6.7
    val entityCullingRuntime = "blank:entityculling:neoforge-1.7.0-mc1.21" // 1.7.0

    // Required
    compileOnly(journeymap)
    compileOnly(railcraft)
    compileOnly(spark)
    compileOnly(pnmc)
    compileOnly("io.redspace:irons_spellbooks:${iss}")
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
    implementation("dev.latvian.mods:kubejs-neoforge:$kubejs")?.let {
        interfaceInjectionData(it)
    }
    implementation("dev.latvian.mods:rhino:$rhino")
    compileOnly(sophCore)
    compileOnly(athena)
    compileOnly(jep)
    compileOnly(jdt)
    compileOnly(curios)
    compileOnly(reliquary)
    compileOnly(stp)
    compileOnly(travelersBackpack)
    compileOnly(accessories)
    compileOnly(accessoriesCclayer)
    compileOnly(genetics)
    compileOnly(buildingGadgets)
    compileOnly("curse.maven:ftb-jei-extras-1103259:5725276") // ftbjeiextras-21.1.0.jar
    compileOnly("curse.maven:productivebees-377897:5611632") // productivebees-1.21.0-13.4.0.jar
    compileOnly("curse.maven:modernfix-790626:5876357") // modernfix-neoforge-5.19.5+mc1.21.1.jar

    // Testing at runtime from latest version reading a CF file
    val gson = GsonBuilder().create()
    val minecraftInstanceJson = Files.newInputStream(File("minecraftinstance.json").toPath()).use { inputStream ->
        val jsonReader = JsonReader(inputStream.reader())
        gson.fromJson<JsonObject>(jsonReader, JsonObject::class.java)
    }
    minecraftInstanceJson.get("installedAddons").asJsonArray.forEach {
        val addonJson = it.asJsonObject
        val cfId = addonJson.get("addonID").asNumber
        val nameId = addonJson.get("webSiteURL").asString.split("/").last()
        val fileId = addonJson.getAsJsonObject("installedFile").asJsonObject.get("id").asNumber
//        println("runtimeOnly(\"curse.maven:$nameId-$cfId:$fileId\")")
        if (nameId.contains("curios")) return@forEach
        runtimeOnly("curse.maven:$nameId-$cfId:$fileId")
    }

    runtimeOnly(entityCullingRuntime)

    jeiRuntimeOnly(etfRuntime)
    jeiRuntimeOnly(emfRuntime)
    jeiRuntimeOnly(jepRuntime)
//    jeiRuntimeOnly(sereneSeasons)
    emiRuntimeOnly(etfRuntime)
    emiRuntimeOnly(emfRuntime)
//    emiRuntimeOnly(sereneSeasons)

    jeiRuntimeOnly(jeiRuntime)
    jeiServerRuntimeOnly(jeiRuntime)
    jeiRuntimeOnly(minecoloniesRuntime)
    jeiServerRuntimeOnly(minecoloniesRuntime)
    jeiRuntimeOnly(jepRuntime)

    emiRuntimeOnly(jeiEmi)
    emiServerRuntimeOnly(jeiEmi)
    emiRuntimeOnly(emiRuntime)
    emiServerRuntimeOnly(emiRuntime)
    emiRuntimeOnly(emiLoot)
    emiServerRuntimeOnly(emiLoot)
    emiRuntimeOnly(minecoloniesRuntime)
    emiServerRuntimeOnly(minecoloniesRuntime)

    // Dependencies for runtime
//    jeiRuntimeOnly("curse.maven:glitchcore-955399:5660740") // GlitchCore-neoforge-1.21.1-2.1.0.0.jar
//    emiRuntimeOnly("curse.maven:glitchcore-955399:5660740") // GlitchCore-neoforge-1.21.1-2.1.0.0.jar
    runtimeOnly("curse.maven:playeranimator-658587:5698755") // player-animation-lib-forge-2.0.0-alpha1+1.21.jar

    // LeakDiagTool
    runtimeOnly("blank:leakdiagtool:0.0.1-beta")

    // for Ingredient Dedupe runtime test

    runtimeOnly("curse.maven:ftb-jei-extras-1103259:5907752") // ftbjeiextras-21.1.3.jar
    runtimeOnly("curse.maven:glodium-957920:5821676") // Glodium-1.21-2.2-neoforge.jar
    runtimeOnly("curse.maven:advancedae-1084104:5881249") // AdvancedAE-1.1.1-1.21.1.jar
    runtimeOnly("curse.maven:ex-pattern-provider-892005:5887634") // ExtendedAE-1.21-2.1.4-neoforge.jar
    runtimeOnly("curse.maven:productivebees-377897:5903573") // productivebees-1.21.1-13.6.5.jar
    runtimeOnly("curse.maven:applied-energistics-2-223794:5729094") // appliedenergistics2-19.0.23-beta.jar
    runtimeOnly("curse.maven:industrial-foregoing-266515:5880501")
    runtimeOnly("curse.maven:titanium-287342:5881103")
    runtimeOnly("curse.maven:modernfix-790626:5876357") // modernfix-neoforge-5.19.5+mc1.21.1.jar

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
