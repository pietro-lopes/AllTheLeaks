import org.gradle.jvm.tasks.Jar
import org.gradle.plugins.ide.idea.model.IdeaLanguageLevel
import java.time.Instant
import java.time.format.DateTimeFormatter

plugins {
    java
    idea
    `maven-publish`
    id("net.neoforged.moddev.legacy") version "2.0.61-beta-pr-118-legacy"
}

val minecraftVersion: String by project
val minecraftVersionRange: String by project
val forgeVersion: String by project
val forgeVersionRange: String by project
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
val geckolibVersionRange: String by project

repositories {
    mavenCentral()
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
        name = "GTCEu Maven"
        url = uri("https://maven.gtceu.com")
        content {
            includeGroup("com.gregtechceu.gtceu")
        }
    }

    maven { url = uri("https://maven.firstdarkdev.xyz/snapshots") } // LDLib

    maven { // Flywheel
        url = uri("https://maven.tterrag.com/")
        content {
            // need to be specific here due to version overlaps
            includeGroup("com.jozufozu.flywheel")
            includeGroup("com.tterrag.registrate")
            includeGroup("com.simibubi.create")
        }
    }

    maven { url = uri("https://maven.terraformersmc.com/releases/") } // Mod Menu, EMI

    maven { url = uri("https://api.modrinth.com/maven") } // LazyDFU, Jade

    maven {
        url = uri("https://maven.blamejared.com/")
    }

    maven {
        url = uri("https://maven.shedaniel.me/")
        content {
            includeGroup("me.shedaniel")
            includeGroup("me.shedaniel.cloth")
            includeGroup("dev.architectury")
        }
    }

    maven {
        url = uri("https://modmaven.dev/")
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

java.toolchain.languageVersion = JavaLanguageVersion.of(17)

sourceSets {
    main {
        resources.srcDir("src/generated/resources")
    }
}

neoForge {
    version = "$minecraftVersion-$forgeVersion"
    runs {
        register("client") {
            client()
        }
        register("server") {
            server()
//            programArgument("--nogui")
            gameDirectory = file("runs/server")
        }

        configureEach {
            logLevel = org.slf4j.event.Level.DEBUG
            jvmArgument("-Xmx3000m")
            jvmArgument("-XX:+IgnoreUnrecognizedVMOptions")
            jvmArgument("-XX:+AllowEnhancedClassRedefinition")
            jvmArgument("-Dgeckolib.disable_examples=true")
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

mixin {
    add(sourceSets.main.get(),"alltheleaks.refmap.json")
    config("alltheleaks.mixins.json")
}

afterEvaluate {
    tasks.withType(Jar::class).configureEach {
        manifest.attributes(
            mapOf(
            "MixinConfigs" to "alltheleaks.mixins.json",
            "Specification-Title" to project.name,
            "Specification-Vendor" to modAuthors,
            "Specification-Version" to modVersion,
            "Implementation-Title" to project.name,
            "Implementation-Version" to project.version,
            "Implementation-Vendor" to modAuthors,
            "Implementation-Timestamp" to DateTimeFormatter.ISO_INSTANT.format(Instant.now())
        ))
    }
}

dependencies {
    // the f?
    compileOnly("org.jetbrains:annotations:24.1.0")
    // Lombok for Greg
    compileOnly("org.projectlombok:lombok:1.18.32")
    annotationProcessor("org.projectlombok:lombok:1.18.32")


    //Mixins
    compileOnly(annotationProcessor("io.github.llamalad7:mixinextras-common:0.4.1")!!)
    implementation(jarJar("io.github.llamalad7:mixinextras-forge:0.4.1")!!)
    annotationProcessor("org.spongepowered:mixin:0.8.5:processor")

    // Minimum version
    modCompileOnly("curse.maven:create-328085:4625535") // create-1.20.1-0.5.1.c.jar
    modCompileOnly("curse.maven:createaddition-439890:4685223") // "createaddition-1.20.1-1.0.0b.jar"
    modCompileOnly("curse.maven:citadel-331936:4613231") // citadel-2.4.2-1.20.1.jar
    modCompileOnly("curse.maven:applied-energistics-2-223794:4733834") // appliedenergistics2-forge-15.0.10.jar
    modCompileOnly("curse.maven:applied-energistics-2-wireless-terminals-459929:4748197") // ae2wtlib-15.0.11-forge.jar
    modCompileOnly("curse.maven:jei-238222:5528637") // jei-1.20.1-forge-15.4.0.9.jar
    modCompileOnly("curse.maven:railcraft-reborn-901491:5491848") // railcraft-reborn-1.20.1-1.1.2.jar
    modCompileOnly("curse.maven:curios-309927:4731891") // curios-forge-5.2.0+1.20.1.jar
    modCompileOnly("curse.maven:tfc-volcanoes-962578:5055726") // TFCThermalDeposits-1.20.1-1.3.2.jar
    modCompileOnly("curse.maven:architectury-api-419699:4581905") // architectury-9.0.8-forge.jar
    modCompileOnly("curse.maven:irons-spells-n-spellbooks-855414:5616394") // irons_spellbooks-1.20.1-3.4.0.jar
    modCompileOnly("curse.maven:aether-255308:5302178") // aether-1.20.1-1.4.2-neoforge.jar
    modCompileOnly("software.bernie.geckolib:geckolib-forge-1.20.1:4.4.8") { isTransitive = false }
    modCompileOnly("curse.maven:occultism-361026:4586250") // occultism-1.20.1-1.80.7.jar
    modCompileOnly("curse.maven:travelers-backpack-321117:4592150") // TravelersBackpack-1.20.1-9.1.0.jar
//    modCompileOnly("curse.maven:ars-nouveau-401955:5190105") // 4.10.0
    modCompileOnly("curse.maven:ars-nouveau-401955:4631012") // ars_nouveau-1.20.1-4.0.0.jar
    modCompileOnly("curse.maven:forbidden-arcanus-309858:4692792") // forbidden_arcanus-1.20.1-2.2.0-beta1.jar
    modCompileOnly("curse.maven:tool-belt-260262:4581167") // ToolBelt-1.20-1.20.0.jar
    modCompileOnly("curse.maven:just-enough-archaeology-890755:4659878") // jearchaeology-1.20.1-1.0.0.jar
    modCompileOnly("curse.maven:betterf3-401648:4641169") // BetterF3-7.0.1-Forge-1.20.1.jar
    modCompileOnly("com.lowdragmc.ldlib:ldlib-forge-1.20.1:1.0.26.b") { isTransitive = false }
//    modCompileOnly("curse.maven:ldlib-626676:5618585") // ldlib-forge-1.20.1-1.0.26.b.jar
    modCompileOnly("com.jozufozu.flywheel:flywheel-forge-1.20:0.6.9-4") // flywheel-forge-1.20.1-0.6.9-4.jar
    modCompileOnly("curse.maven:gregtechceu-modern-890405:5253480") //

    // Middle versions
//    modCompileOnly("curse.maven:createaddition-439890:5099752") // not fixed 1.20.1-1.2.3
//    modCompileOnly("curse.maven:railcraft-reborn-901491:5534181") // not fixed 1.1.6

    // Latest versions
    modRuntimeOnly("curse.maven:create-328085:5797605") // create-1.20.1-0.5.1.i.jar
//    modRuntimeOnly("curse.maven:createaddition-439890:5658602") // createaddition-1.20.1-1.2.4e.jar
//    modRuntimeOnly("curse.maven:citadel-331936:5633260")// citadel-2.6.0-1.20.1.jar
//    modRuntimeOnly("curse.maven:applied-energistics-2-223794:5641282") // appliedenergistics2-forge-15.2.13.jar
//    modRuntimeOnly("curse.maven:applied-energistics-2-wireless-terminals-459929:5217955") // ae2wtlib-15.2.3-forge.jar
    modRuntimeOnly("curse.maven:jei-238222:5793297") // jei-1.20.1-forge-15.20.0.104.jar
//    modRuntimeOnly("curse.maven:railcraft-reborn-901491:5650737") // railcraft-reborn-1.20.1-1.1.7.jar
//    modRuntimeOnly("curse.maven:curios-309927:5680164") // curios-forge-5.10.0+1.20.1.jar
//    modRuntimeOnly("curse.maven:tfc-volcanoes-962578:5647602") // TFCVolcanoes-1.20.1-1.3.14.jar
//    modRuntimeOnly("curse.maven:architectury-api-419699:5137938") // architectury-9.2.14-forge.jar
//    modRuntimeOnly("curse.maven:irons-spells-n-spellbooks-855414:5765121") // irons_spellbooks-1.20.1-3.4.0.2.jar
//    modRuntimeOnly("curse.maven:aether-255308:5786709") // aether-1.20.1-1.5.0-neoforge.jar
//    modRuntimeOnly("software.bernie.geckolib:geckolib-forge-1.20.1:4.4.9") // 1.20.1:4.4.9
//    modRuntimeOnly("curse.maven:occultism-361026:5793620") // occultism-1.20.1-1.139.1.jar
//    modRuntimeOnly("curse.maven:travelers-backpack-321117:5764972") // travelersbackpack-forge-1.20.1-9.1.16.jar
//    modRuntimeOnly("curse.maven:ars-nouveau-401955:5600384") // ars_nouveau-1.20.1-4.12.4-all.jar
//    modRuntimeOnly("curse.maven:tool-belt-260262:5393183") // ToolBelt-1.20.1-1.20.01.jar
//    modRuntimeOnly("curse.maven:just-enough-archaeology-890755:5324518") // jearchaeology-1.20.1-1.0.4.jar
//    modRuntimeOnly("curse.maven:supplementaries-412082:5676069") // supplementaries-1.20-2.8.17.jar
//    modRuntimeOnly("curse.maven:betterf3-401648:4863626") // BetterF3-7.0.2-Forge-1.20.1.jar
//    modRuntimeOnly("curse.maven:spark-361579:4738952") // spark-1.10.53-forge.jar

    /* Greg headache
    1. Download jar
    2. Remove jarjar and jars folder
     */
    modRuntimeOnly("blank:gtceu-1.20.1:1.4.0-jarjarless")
    modRuntimeOnly("curse.maven:ldlib-626676:5618585") // ldlib-forge-1.20.1-1.0.26.b.jar
    modRuntimeOnly("curse.maven:configuration-444699:4608425") // configuration-forge-1.20.1-2.2.0.jar

    // Required dependencies runtimes
//    modRuntimeOnly("curse.maven:cloth-config-348521:5729105")
//    modRuntimeOnly("curse.maven:terrafirmacraft-302973:5571484") // TerraFirmaCraft-Forge-1.20.1-3.2.7.jar
//    modRuntimeOnly("curse.maven:patchouli-306770:4966125") // Patchouli-1.20.1-84-FORGE.jar
//    modRuntimeOnly("curse.maven:ferritecore-429235:4810975") // ferritecore-6.0.1-forge.jar
//    modRuntimeOnly("curse.maven:caelus-308989:5281700") // caelus-forge-3.2.0+1.20.1.jar
//    modRuntimeOnly("curse.maven:playeranimator-658587:4587214") // player-animation-lib-forge-1.0.2-rc1+1.20.jar
//    modRuntimeOnly("curse.maven:modonomicon-538392:5786081") // modonomicon-1.20.1-forge-1.77.3.jar
//    modRuntimeOnly("curse.maven:smartbrainlib-661293:5654964") // SmartBrainLib-forge-1.20.1-1.15.jar
//    modRuntimeOnly("curse.maven:forbidden-arcanus-309858:5198323") // forbidden_arcanus-1.20.1-2.2.6.jar
//    modRuntimeOnly("curse.maven:valhelsia-core-416935:5189548") // valhelsia_core-forge-1.20.1-1.1.2.jar
//    modRuntimeOnly("curse.maven:selene-499980:5822487") // moonlight-1.20-2.13.12-forge.jar










    // Here we go
//    modCompileOnly("curse.maven:citadel-331936:5143956")
//    modCompileOnly("curse.maven:railcraft-reborn-901491:5242853")
    modCompileOnly("curse.maven:kubejs-238086:5268032")
    modCompileOnly("curse.maven:tfc-thermal-deposits-962578:5317918")
    modCompileOnly("curse.maven:ftb-xmod-compat-889915:5257897")
//    modCompileOnly("curse.maven:jei-238222:5101366")

    modCompileOnly("curse.maven:patchouli-306770:4966125")
//    modCompileOnly("curse.maven:applied-energistics-2-223794:5330978")
    modCompileOnly("curse.maven:kubejs-enderio-910379:4872849") { isTransitive = false }
    modCompileOnly("curse.maven:ftb-quests-forge-289412:5315015")
    modCompileOnly("curse.maven:blue-skies-312918:5010316")

    // modCompileOnly("blank:flywheel-forge:1.20.1-0.6.10-7")
//    modCompileOnly("com.jozufozu.flywheel:flywheel-forge-1.20.1:0.6.10-7")

    modCompileOnly("curse.maven:oculus-581495:5299671")

    modCompileOnly("curse.maven:zeta-968868:5254672")
    modCompileOnly("curse.maven:quark-243121:5346894")



//    modCompileOnly("curse.maven:applied-energistics-2-wireless-terminals-459929:5217955")

    modCompileOnly("curse.maven:productivebees-377897:5381132")
    modCompileOnly("curse.maven:creeper-overhaul-561625:5125710")


    modCompileOnly("blank:mclib:20")


    modCompileOnly("curse.maven:enderman-overhaul-574409:5019620")

//    modCompileOnly("curse.maven:irons-spells-n-spellbooks-855414:5341949")

    modCompileOnly("curse.maven:minecolonies-245506:5346155")

//    modCompileOnly("curse.maven:create-328085:4835191")

//    modCompileOnly("curse.maven:jei-238222:5101366")
    modCompileOnly("curse.maven:create-steam-n-rails-688231:5331300")

    modCompileOnly("curse.maven:pneumaticcraft-repressurized-281849:5279325")

    modCompileOnly("curse.maven:ftb-chunks-forge-314906:5267364")

    modCompileOnly("curse.maven:just-enough-resources-jer-240630:5057220")

    modCompileOnly("curse.maven:just-enough-archaeology-890755:5324518")

//    modCompileOnly("curse.maven:corail-tombstone-243707:5395076")
    modCompileOnly("curse.maven:corail-tombstone-243707:5201363")

    modCompileOnly("curse.maven:journeymap-32274:5293067")

    modCompileOnly("curse.maven:mekanism-268560:5125665")

//    modCompileOnly("curse.maven:ldlib-626676:5252086")
//    modCompileOnly("curse.maven:ldlib-626676:5343863")

    modCompileOnly("curse.maven:jade-324717:5339264")

    modCompileOnly("curse.maven:easy-villagers-400514:5153629")

    modCompileOnly("curse.maven:security-craft-64760:5207771")

    modCompileOnly("curse.maven:distant-horizons-508933:5390046")

    modCompileOnly("curse.maven:ice-and-fire-dragons-264231:5122408")

    modCompileOnly("curse.maven:lootjs-570630:5404565")

    modCompileOnly("curse.maven:rhino-416294:4944325")

    modCompileOnly("curse.maven:ambientsounds-254284:5345142")
    modCompileOnly("curse.maven:xaeros-minimap-263420:5262365")
//    modCompileOnly("curse.maven:xaeros-minimap-263420:5394772")
//    modCompileOnly("curse.maven:xaeros-world-map-317780:5394829")
    modCompileOnly("blank:ImmersiveEngineering:1.20.1-10.0.0-169")

    modCompileOnly("curse.maven:xaeros-world-map-317780:5262424")
    modCompileOnly("curse.maven:ftb-quests-forge-289412:5418359")
    modCompileOnly("curse.maven:spark-361579:4738952")

    modCompileOnly("curse.maven:cyclops-core-232758:5262063")
    modCompileOnly("curse.maven:integrated-dynamics-236307:5297722")

    modCompileOnly("curse.maven:ftb-library-forge-404465:5364190")

    modCompileOnly("curse.maven:terrafirmacraft-302973:5356073")

//    modCompileOnly("curse.maven:architectury-api-419699:5137938")

    modCompileOnly("curse.maven:curios-309927:5367944")

    modCompileOnly("curse.maven:dynamiclights-reforged-551736:4731947")

    modRuntimeOnly("blank:leakdiagtool:1.0.0")
}

tasks {
    processResources {
        val replaceProperties = mapOf(
            "minecraft_version" to minecraftVersion,
            "minecraft_version_range" to minecraftVersionRange,
            "forge_version" to forgeVersion,
            "forge_version_range" to forgeVersionRange,
            "loader_version_range" to loaderVersionRange,
            "mod_id" to modId,
            "mod_name" to modName,
            "mod_license" to modLicense,
            "mod_version" to modVersion,
            "mod_authors" to modAuthors,
            "mod_description" to modDescription,
            "geckolib_version_range" to geckolibVersionRange
        )

        inputs.properties(replaceProperties)
        filesMatching(listOf("META-INF/mods.toml")) {
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
