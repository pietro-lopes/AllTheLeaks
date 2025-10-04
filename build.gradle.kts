import org.gradle.jvm.tasks.Jar
import org.gradle.plugins.ide.idea.model.IdeaLanguageLevel
import java.time.Instant
import java.time.format.DateTimeFormatter

plugins {
    java
    idea
    `maven-publish`
    id("net.neoforged.moddev.legacyforge") version "2.0.88"
}

val minecraftVersion: String by extra
val minecraftVersionRange: String by extra
val forgeVersion: String by extra
val forgeVersionRange: String by extra
val loaderVersionRange: String by extra
val parchmentMcVersion: String by extra
val parchmentVersion: String by extra
val modId: String by extra
val modName: String by extra
val modLicense: String by extra
val modVersion: String by extra
val modGroupId: String by extra
val modAuthors: String by extra
val modDescription: String by extra
val geckolibVersionRange: String by extra
val moonlightVersionRange: String by extra

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
        url = uri("https://maven.latvian.dev/releases")
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
            // includeGroup("com.jozufozu.flywheel")
            includeGroup("com.tterrag.registrate")
            includeGroup("com.simibubi.create")
        }
    }

    maven { url = uri("https://maven.terraformersmc.com/releases/") } // Mod Menu, EMI

    maven { url = uri("https://api.modrinth.com/maven") } // LazyDFU, Jade

    maven {
        url = uri("https://maven.blamejared.com/")
    }

    maven { url = uri("https://maven.bawnorton.com/releases") }

    maven {
        url = uri("https://maven.shedaniel.me/")
        content {
            includeGroup("me.shedaniel")
            includeGroup("me.shedaniel.cloth")
            includeGroup("dev.architectury")
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

java.toolchain.languageVersion = JavaLanguageVersion.of(17)

sourceSets {
    main {
        resources.srcDir("src/generated/resources")
    }
}

val mc = minecraftVersion
val fg = forgeVersion
legacyForge {
    version = "$mc-$fg"
    runs {
        register("client") {
            client()
        }
        // Run this to bypass all the Too Early Config Errors
        register("client-production") {
            client()
            jvmArgument("-Dproduction")
        }
        register("client2-production") {
            client()
            jvmArgument("-Dproduction")
            programArguments.addAll("--username", "Dev2")
        }
        register("server") {
            server()
            gameDirectory = file("runs/server")
            jvmArgument("-Dproduction")
        }

        configureEach {
            //logLevel = org.slf4j.event.Level.DEBUG
            systemProperty("forge.logging.console.level", "debug")
            systemProperty("forge.logging.markers", "LOADING")
            //loggingConfigFile
            jvmArgument("-XX:+DisableExplicitGC") // Test gc command
            jvmArgument("-Xmx5000m")
            //jvmArgument("-XX:+IgnoreUnrecognizedVMOptions")
            jvmArgument("-XX:+AllowEnhancedClassRedefinition")
            jvmArgument("-Dgeckolib.disable_examples=true")
            if (type.get().startsWith("client")) {
                programArguments.addAll("--width", "1920", "--height", "1080")
                gameDirectory = file("runs/client")
                systemProperty("mixin.debug.export", "true")
                systemProperty("alltheleaks.indev", "true")
                jvmArguments.addAll(
                    "-XX:+UnlockExperimentalVMOptions",
                    "-XX:+UseG1GC",
                    "-XX:G1NewSizePercent=20",
                    "-XX:G1ReservePercent=20",
                    "-XX:MaxGCPauseMillis=50",
                    "-XX:G1HeapRegionSize=32M"
                )
                //jvmArgument("-Xlog:safepoint:file=safepoint.log::filecount=0")
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

    //Mixins (IDE is crashing with !!, using ?.let instead...)
    annotationProcessor("io.github.llamalad7:mixinextras-common:0.5.0")?.let {
       compileOnly(it)
    }
    jarJar("io.github.llamalad7:mixinextras-forge:0.5.0")?.let {
        implementation(it)
    }
    annotationProcessor("org.spongepowered:mixin:0.8.5:processor")

    annotationProcessor("com.github.bawnorton.mixinsquared:mixinsquared-common:0.3.6-beta.1")?.let {
        compileOnly(it)
    }
    jarJar("com.github.bawnorton.mixinsquared:mixinsquared-forge:0.3.6-beta.1")?.let {
        implementation(it)
    }

    // Minimum version
    modCompileOnly("curse.maven:create-328085:4625535") // create-1.20.1-0.5.1.c.jar
    modCompileOnly("curse.maven:createaddition-439890:4685223") // "createaddition-1.20.1-1.0.0b.jar"
    modCompileOnly("curse.maven:citadel-331936:4613231") // citadel-2.4.2-1.20.1.jar
    modCompileOnly("curse.maven:applied-energistics-2-223794:4733834") // appliedenergistics2-forge-15.0.10.jar
    modCompileOnly("curse.maven:applied-energistics-2-wireless-terminals-459929:4748197") // ae2wtlib-15.0.11-forge.jar
//    modCompileOnly("curse.maven:jei-238222:5528637") // jei-1.20.1-forge-15.4.0.9.jar
//    modCompileOnly("curse.maven:jei-238222:5531897") // jei-1.20.1-forge-15.8.0.10.jar
    modCompileOnly("mezz.jei:jei-1.20.1-forge:15.8.2.24") // jei-1.20.1-forge-15.8.2.24.jar
//    modCompileOnly("curse.maven:jei-238222:5576338") // 15.8.2
//    modCompileOnly("curse.maven:jei-238222:5567914") // jei-1.20.1-forge-15.8.2.23.jar
    modCompileOnly("curse.maven:railcraft-reborn-901491:5491848") // railcraft-reborn-1.20.1-1.1.2.jar
    modCompileOnly("curse.maven:curios-309927:4731891") // curios-forge-5.2.0+1.20.1.jar
    modCompileOnly("curse.maven:tfc-volcanoes-962578:5055726") // TFCThermalDeposits-1.20.1-1.3.2.jar
    modCompileOnly("curse.maven:architectury-api-419699:4581905") // architectury-9.0.8-forge.jar
    modCompileOnly("curse.maven:irons-spells-n-spellbooks-855414:5616394") // irons_spellbooks-1.20.1-3.4.0.jar
    modCompileOnly("curse.maven:aether-255308:5302178") // aether-1.20.1-1.4.2-neoforge.jar
    modCompileOnly("software.bernie.geckolib:geckolib-forge-1.20.1:4.4.8") { isTransitive = true }
    modCompileOnly("com.eliotlash.mclib:mclib:20")
    modCompileOnly("curse.maven:occultism-361026:4586250") // occultism-1.20.1-1.80.7.jar
//    modCompileOnly("curse.maven:travelers-backpack-321117:4592150") // TravelersBackpack-1.20.1-9.1.0.jar
    modCompileOnly("curse.maven:travelers-backpack-321117:5764972") // travelersbackpack-forge-1.20.1-9.1.16.jar
//    modCompileOnly("curse.maven:ars-nouveau-401955:5190105") // 4.10.0
    modCompileOnly("curse.maven:ars-nouveau-401955:4631012") // ars_nouveau-1.20.1-4.0.0.jar
    modCompileOnly("curse.maven:forbidden-arcanus-309858:4692792") // forbidden_arcanus-1.20.1-2.2.0-beta1.jar
    modCompileOnly("curse.maven:tool-belt-260262:4581167") // ToolBelt-1.20-1.20.0.jar
    modCompileOnly("curse.maven:just-enough-archaeology-890755:4659878") // jearchaeology-1.20.1-1.0.0.jar
    modCompileOnly("curse.maven:betterf3-401648:4641169") // BetterF3-7.0.1-Forge-1.20.1.jar
//    modCompileOnly("com.lowdragmc.ldlib:ldlib-forge-1.20.1:1.0.26.b") { isTransitive = false }
    modCompileOnly("curse.maven:ldlib-626676:5618585") // ldlib-forge-1.20.1-1.0.26.b.jar
    // modCompileOnly("com.jozufozu.flywheel:flywheel-forge-1.20:0.6.9-4") // flywheel-forge-1.20.1-0.6.9-4.jar
    // get from create 0.5.1c
    modCompileOnly("blank:flywheel-forge-1.20:0.6.9-4") // flywheel-forge-1.20.1-0.6.9-4.jar
    modCompileOnly("curse.maven:gregtechceu-modern-890405:5253480") //
    modCompileOnly("curse.maven:ftb-library-forge-404465:5364190") // ftb-library-forge-2001.2.2.jar
    modCompileOnly("curse.maven:easy-villagers-400514:5153629") //
    modCompileOnly("curse.maven:journeymap-32274:5293067")
    modCompileOnly("curse.maven:corail-tombstone-243707:4575470") // tombstone-8.5.0-1.20.jar
    modCompileOnly("curse.maven:blue-skies-312918:5010316")
    modCompileOnly("curse.maven:just-enough-resources-jer-240630:5057220") // JustEnoughResources-1.20.1-1.4.0.247.jar
//    modCompileOnly("curse.maven:minecolonies-245506:5661958") // minecolonies-1.20.1-1.1.647-beta.jar
    modCompileOnly("curse.maven:minecolonies-245506:5725332") // minecolonies-1.20.1-1.1.665-snapshot-universal.jar
    modCompileOnly("curse.maven:pneumaticcraft-repressurized-281849:5279325") // pneumaticcraft-repressurized-6.0.15+mc1.20.1.jar
//    modCompileOnly("curse.maven:pneumaticcraft-repressurized-281849:5680613") // pneumaticcraft-repressurized-6.0.17+mc1.20.1.jar
    modCompileOnly("curse.maven:the-twilight-forest-227639:5468648")
    modCompileOnly("curse.maven:mouse-tweaks-60089:5338457") // MouseTweaks-forge-mc1.20.1-2.25.1.jar
    modCompileOnly("curse.maven:athena-841890:4621937") // athena-forge-1.20.1-3.0.0.jar
    modCompileOnly("curse.maven:evilcraft-74610:5776778") // EvilCraft-1.20.1-1.2.48.jar
    modCompileOnly("curse.maven:mythicbotany-400058:5101899") // MythicBotany-1.20.1-4.0.3.jar
    modCompileOnly("curse.maven:cyclops-core-232758:5262063") // CyclopsCore-1.20.1-1.19.1.jar
    modCompileOnly("curse.maven:structure-gel-api-378802:5278429") // structure_gel-1.20.1-2.16.2.jar
    modCompileOnly("curse.maven:iceberg-520110:4627133") // Iceberg-1.20.1-forge-1.1.25.jar
    modCompileOnly("curse.maven:emi-580555:4776467") // emi-1.0.21+1.20.1+forge.jar
    modCompileOnly("curse.maven:nuclearcraft-neoteric-840010:4947601") // NuclearCraft-1.20.1-1.0.0-beta.1.jar
    modCompileOnly("curse.maven:difficulty-lock-390886:5146560") // difficultylock-1.20.1-4.1.jar
    modCompileOnly("curse.maven:corpse-316582:4584202") // corpse-1.20.1-1.0.3.jar
    modCompileOnly("curse.maven:emi-loot-681783:4971064") // emi_loot-0.6.5+1.20.1+forge.jar
    modCompileOnly("curse.maven:findme-291936:4614446") // findme-3.1.0-forge.jar
    modCompileOnly("curse.maven:badpackets-615134:4438956") // badpackets-forge-0.4.1.jar
    modCompileOnly("curse.maven:alexs-mobs-426558:4618074") // alexsmobs-1.22.5.jar
    modCompileOnly("curse.maven:mowzies-mobs-250498:5399941")
    modCompileOnly("curse.maven:serene-seasons-291874:5790653") // SereneSeasons-forge-1.20.1-9.1.0.0.jar
    modCompileOnly("curse.maven:entity-texture-features-fabric-568563:5697083") // entity_texture_features_forge_1.20.1-6.2.3.jar
    modCompileOnly("curse.maven:entity-model-features-844662:5674620") // entity_model_features_forge_1.20.1-2.2.2.jar
    modCompileOnly("curse.maven:beans-backpacks-946775:5368249") // BeansBackpacks-forge-1.20.1-2.0.jar
    modCompileOnly("curse.maven:phosphophyllite-412551:4633720") // phosphophyllite-1.20.1-0.7.0-alpha.jar
    modCompileOnly("curse.maven:domestication-innovation-591149:4578807") // domesticationinnovation-1.7.0-1.20.jar
    modCompileOnly("curse.maven:selene-499980:5938277") // moonlight-1.20-2.13.32-forge.jar
    modCompileOnly("curse.maven:mana-and-artifice-406360:6056053") // mna-forge-1.20.1-3.1.0.4-all.jar
    modCompileOnly("curse.maven:zeta-968868:5418213") // Zeta-1.0-19.jar
    //compileOnly("curse.maven:modernfix-790626:6725223")
    //modCompileOnly("curse.maven:modernfix-790626:6725223")
    modCompileOnly("dev.latvian.mods:kubejs-forge:2001.6.5-build.16")
    modCompileOnly("curse.maven:immersive-engineering-231951:6206989")
    modCompileOnly("curse.maven:ender-io-64578:6761673")
    modCompileOnly("curse.maven:alchemylib-293426:5037781")
    modCompileOnly("curse.maven:sgjourney-689083:6390662")
    modCompileOnly("curse.maven:productivebees-377897:5566102")
    modCompileOnly("curse.maven:blood-magic-224791:5290993")
    modCompileOnly("curse.maven:cofh-core-69162:5374122")
    modCompileOnly("curse.maven:ars-elemental-561470:6390889")
    modCompileOnly("curse.maven:potion-blender-697859:6033408")

    //modCompileOnly("curse.maven:railcraft-reborn-901491:6149940")
    //modCompileOnly("curse.maven:corail-woodcutter-331983:5749500")
    //compileOnly("curse.maven:corail-woodcutter-331983:5749500")
    modCompileOnly("curse.maven:draconic-evolution-223565:6793843")
    modCompileOnly("curse.maven:theurgy-430636:6145150")
    modCompileOnly("curse.maven:mekanism-268560:6552911")
    modCompileOnly("curse.maven:enigmatic-legacy-336184:5600004")
    modCompileOnly("curse.maven:integrated-tunnels-251389:6829060")
    modCompileOnly("curse.maven:super-factory-manager-306935:6849465")
    modCompileOnly("curse.maven:i-wanna-skate-695018:4576733")
    modCompileOnly("curse.maven:what-are-they-up-to-945479:6315241")
    modCompileOnly("curse.maven:xaeros-minimap-263420:6778028")
    modCompileOnly("curse.maven:botania-225643:6870713") // Botania-1.20.1-450-FORGE.jar
    modCompileOnly("curse.maven:shulkerboxtooltip-315811:4611155")
    modCompileOnly("curse.maven:fire-spread-tweaks-446366:6186005")
    modCompileOnly("curse.maven:crash-utilities-371813:4716086")
    modCompileOnly("curse.maven:forgified-fabric-api-889079:6289136")
    modCompileOnly("blank:fabric-networking-api-v1:1.3.11+503a202477")
    modCompileOnly("curse.maven:create-mobile-packages-1232978:6763649")
    modCompileOnly("curse.maven:easy-piglins-419372:6213598")
    //modCompileOnly("curse.maven:advanced-loot-info-1205426:7018898")

    // Middle versions
//    modCompileOnly("curse.maven:createaddition-439890:5099752") // not fixed 1.20.1-1.2.3
//    modCompileOnly("curse.maven:railcraft-reborn-901491:5534181") // not fixed 1.1.6

    // Latest versions
    modRuntimeOnly("curse.maven:create-328085:6641603") // create-1.20.1-6.0.6.jar
    modRuntimeOnly("curse.maven:createaddition-439890:6306951") // createaddition-1.20.1-1.3.1.jar
    modRuntimeOnly("curse.maven:citadel-331936:5633260")// citadel-2.6.0-1.20.1.jar
    modRuntimeOnly("curse.maven:applied-energistics-2-223794:5641282") // appliedenergistics2-forge-15.2.13.jar
    modRuntimeOnly("curse.maven:applied-energistics-2-wireless-terminals-459929:5217955") // ae2wtlib-15.2.3-forge.jar
    modCompileOnly("curse.maven:jei-238222:5846810") // jei-1.20.1-forge-15.20.0.105.jar
    modRuntimeOnly("curse.maven:jei-238222:5846810") // jei-1.20.1-forge-15.20.0.105.jar
    // modRuntimeOnly("curse.maven:async-jei-just-enough-item-1180017:6139008") // jei-1.20.1-forge-15.20.0.106-5-async.jar
    modRuntimeOnly("curse.maven:railcraft-reborn-901491:5650737") // railcraft-reborn-1.20.1-1.1.7.jar
    modRuntimeOnly("curse.maven:curios-309927:5843594") // curios-forge-5.11.0+1.20.1.jar

    // @Overwrite mixin's are a no-go, my friend
    // you got cucked by a single access transformer
//    modRuntimeOnly("curse.maven:tfc-volcanoes-962578:5647602") // TFCVolcanoes-1.20.1-1.3.14.jar


    modRuntimeOnly("curse.maven:architectury-api-419699:5137938") // architectury-9.2.14-forge.jar
    modRuntimeOnly("curse.maven:irons-spells-n-spellbooks-855414:5838009") // irons_spellbooks-1.20.1-3.4.0.4.jar
    modRuntimeOnly("curse.maven:aether-255308:5786709") // aether-1.20.1-1.5.0-neoforge.jar
//    modRuntimeOnly("software.bernie.geckolib:geckolib-forge-1.20.1:4.4.8") // 1.20.1:4.4.9
    modRuntimeOnly("software.bernie.geckolib:geckolib-forge-1.20.1:4.8.2")
    modRuntimeOnly("curse.maven:occultism-361026:5844288") // occultism-1.20.1-1.140.1.jar
    modRuntimeOnly("curse.maven:travelers-backpack-321117:6370600") // travelersbackpack-forge-1.20.1-9.1.34.jar
    modRuntimeOnly("curse.maven:ars-nouveau-401955:5600384") // ars_nouveau-1.20.1-4.12.4-all.jar
    modRuntimeOnly("curse.maven:tool-belt-260262:5393183") // ToolBelt-1.20.1-1.20.01.jar
    modRuntimeOnly("curse.maven:just-enough-archaeology-890755:5324518") // jearchaeology-1.20.1-1.0.4.jar
    modRuntimeOnly("curse.maven:supplementaries-412082:6749363") // supplementaries-1.20-3.1.36.jar
    modRuntimeOnly("curse.maven:betterf3-401648:4863626") // BetterF3-7.0.2-Forge-1.20.1.jar
    modRuntimeOnly("curse.maven:spark-361579:4738952") // spark-1.10.53-forge.jar
    modRuntimeOnly("curse.maven:ftb-library-forge-404465:5567591") // ftb-library-forge-2001.2.4.jar
    modRuntimeOnly("curse.maven:easy-villagers-400514:5724570") // easy-villagers-forge-1.20.1-1.1.23.jar
    modRuntimeOnly("curse.maven:journeymap-32274:5789363") // journeymap-1.20.1-5.10.3-forge.jar
//    modRuntimeOnly("curse.maven:corail-tombstone-243707:5925362") // tombstone-1.20.1-8.8.6.jar
    modRuntimeOnly("curse.maven:blue-skies-312918:5010316") // blue_skies-1.20.1-1.3.31.jar
    modRuntimeOnly("curse.maven:just-enough-resources-jer-240630:5057220") // JustEnoughResources-1.20.1-1.4.0.247.jar
    modRuntimeOnly("curse.maven:minecolonies-245506:5874253") // minecolonies-1.20.1-1.1.716-snapshot.jar
    modRuntimeOnly("curse.maven:pneumaticcraft-repressurized-281849:6142963") // pneumaticcraft-repressurized-6.0.20+mc1.20.1.jar
    modRuntimeOnly("curse.maven:athena-841890:5176879") // athena-forge-1.20.1-3.1.2.jar
    modRuntimeOnly("curse.maven:evilcraft-74610:5776778") // EvilCraft-1.20.1-1.2.48.jar
    modRuntimeOnly("curse.maven:the-twilight-forest-227639:5468648") // twilightforest-1.20.1-4.3.2508-universal.jar
    modRuntimeOnly("curse.maven:mouse-tweaks-60089:5338457") // MouseTweaks-forge-mc1.20.1-2.25.1.jar
    modRuntimeOnly("curse.maven:mythicbotany-400058:5101899") // MythicBotany-1.20.1-4.0.3.jar
    modRuntimeOnly("curse.maven:refined-storage-243076:4844585") // refinedstorage-1.12.4.jar
    modRuntimeOnly("curse.maven:small-ships-450659:5566900") // smallships-forge-1.20.1-2.0.0-b1.4.jar
    //modRuntimeOnly("curse.maven:modernfix-790626:6766127") // modernfix-forge-5.24.3+mc1.20.1.jar
    modCompileOnly("curse.maven:modernfix-790626:6766127")
    compileOnly("curse.maven:modernfix-790626:6766127")
    //modRuntimeOnly("curse.maven:modernfix-790626:6766127")
    modRuntimeOnly("curse.maven:iceberg-520110:5838149") // Iceberg-1.20.1-forge-1.1.25.jar
    // modRuntimeOnly("curse.maven:emi-580555:5872526") // emi-1.1.18+1.20.1+forge.jar
    modRuntimeOnly("curse.maven:nuclearcraft-neoteric-840010:5916571") // NuclearCraft-1.20.1-1.1.0-rc.3.jar
    modRuntimeOnly("curse.maven:difficulty-lock-390886:5572725") // difficultylock-1.20.1-4.6.jar
    modRuntimeOnly("curse.maven:corpse-316582:5816907") // corpse-forge-1.20.1-1.0.17.jar
    // modRuntimeOnly("curse.maven:emi-loot-681783:5760210") // emi_loot-0.7.4+1.20.1+forge.jar
    modRuntimeOnly("curse.maven:findme-291936:5074609") // findme-3.2.1-forge.jar
    modRuntimeOnly("curse.maven:badpackets-615134:4784395") // badpackets-forge-0.4.3.jar
    modRuntimeOnly("curse.maven:alexs-mobs-426558:5698791") // alexsmobs-1.22.9.jar
    modRuntimeOnly("curse.maven:mowzies-mobs-250498:6224182") // mowziesmobs-1.7.1.jar
    modRuntimeOnly("curse.maven:serene-seasons-291874:5790653") // SereneSeasons-1.20.1-9.1.0.0.jar
//    modRuntimeOnly("curse.maven:entity-texture-features-fabric-568563:5921292") // entity_texture_features_forge_1.20.1-6.2.8.jar
//    modRuntimeOnly("curse.maven:entity-model-features-844662:5722712") // entity_model_features_forge_1.20.1-2.2.6.jar
    modRuntimeOnly("curse.maven:beans-backpacks-946775:5368249") // BeansBackpacks-forge-1.20.1-2.0.jar
    modRuntimeOnly("curse.maven:phosphophyllite-412551:6085275") // phosphophyllite-1.20.1-0.7.0-alpha.0.2.jar
    modRuntimeOnly("curse.maven:domestication-innovation-591149:4911955") // domesticationinnovation-1.7.1-1.20.1.jar

    modRuntimeOnly("curse.maven:entity-texture-features-fabric-568563:6954759")
    modRuntimeOnly("curse.maven:entity-model-features-844662:6922712")

    /* Greg headache before MDG legacy
    1. Download jar
    2. Remove jarjar and jars folder

    modRuntimeOnly("blank:gtceu-1.20.1:1.4.0-jarjarless")
    modRuntimeOnly("curse.maven:ldlib-626676:5618585") // ldlib-forge-1.20.1-1.0.26.b.jar
    modRuntimeOnly("curse.maven:configuration-444699:4608425") // configuration-forge-1.20.1-2.2.0.jar

    */
    modRuntimeOnly("curse.maven:gregtechceu-modern-890405:6792524") // gtceu-1.20.1-1.6.4.jar
    //runtimeOnly("curse.maven:gregtechceu-modern-890405:6792524")

    // Required dependencies runtimes
    modRuntimeOnly("curse.maven:cloth-config-348521:5729105")
//    modRuntimeOnly("curse.maven:terrafirmacraft-302973:5872631") // TerraFirmaCraft-Forge-1.20.1-3.2.10.jar
    modCompileOnly("curse.maven:terrafirmacraft-302973:6187491") // TerraFirmaCraft-Forge-1.20.1-3.2.14.jar
    modRuntimeOnly("curse.maven:patchouli-306770:4966125") // Patchouli-1.20.1-84-FORGE.jar
    modRuntimeOnly("curse.maven:ferritecore-429235:4810975") // ferritecore-6.0.1-forge.jar
    modRuntimeOnly("curse.maven:caelus-308989:5281700") // caelus-forge-3.2.0+1.20.1.jar
    modRuntimeOnly("curse.maven:playeranimator-658587:4587214") // player-animation-lib-forge-1.0.2-rc1+1.20.jar
    modRuntimeOnly("curse.maven:modonomicon-538392:5786081") // modonomicon-1.20.1-forge-1.77.3.jar
    modRuntimeOnly("curse.maven:smartbrainlib-661293:5654964") // SmartBrainLib-forge-1.20.1-1.15.jar
    modRuntimeOnly("curse.maven:forbidden-arcanus-309858:5198323") // forbidden_arcanus-1.20.1-2.2.6.jar
    modRuntimeOnly("curse.maven:valhelsia-core-416935:5189548") // valhelsia_core-forge-1.20.1-1.1.2.jar
    modRuntimeOnly("curse.maven:selene-499980:6740431") // moonlight-1.20-2.14.13-forge.jar
    modRuntimeOnly("curse.maven:structure-gel-api-378802:5278429") // structure_gel-1.20.1-2.16.2.jar
    modRuntimeOnly("curse.maven:structurize-298744:5782214") // structurize-1.20.1-1.0.760-snapshot.jar
    modRuntimeOnly("curse.maven:towntalk-900364:5355511") // towntalk-1.20.1-1.1.0.jar
    modRuntimeOnly("curse.maven:multi-piston-303278:5204918") // multipiston-1.20-1.2.43-RELEASE.jar
    modRuntimeOnly("curse.maven:domum-ornamentum-527361:5764067") // domum_ornamentum-1.20.1-1.0.282-snapshot-universal.jar
    modRuntimeOnly("curse.maven:blockui-522992:5658253") // blockui-1.20.1-1.0.186-beta.jar
    modRuntimeOnly("curse.maven:cyclops-core-232758:5583765") // CyclopsCore-1.20.1-1.19.5.jar
    modRuntimeOnly("curse.maven:libx-412525:5207625") // LibX-1.20.1-5.0.14.jar
    modRuntimeOnly("curse.maven:botania-225643:6870713") // Botania-1.20.1-450-FORGE.jar
    modRuntimeOnly("curse.maven:collective-342584:5840216") // collective-1.20.1-7.87.jar
    modRuntimeOnly("curse.maven:fzzy-config-1005914:5908784") // fzzy_config-0.5.7+1.20.1+forge.jar
    modRuntimeOnly("curse.maven:kotlin-for-forge-351264:5402061") // kotlinforforge-4.11.0-all.jar
    modRuntimeOnly("curse.maven:glitchcore-955399:5787839") // GlitchCore-forge-1.20.1-0.0.1.1.jar
    // ACTIVATING THIS WILL CAUSE LOGS TO BE WARN ONLY
    //modRuntimeOnly("curse.maven:mana-and-artifice-406360:6056053") // mna-forge-1.20.1-3.1.0.4-all.jar
    modRuntimeOnly("curse.maven:zeta-968868:6432578") // Zeta-1.0-30.jar
    modRuntimeOnly("curse.maven:ender-io-64578:6274905") // EnderIO-1.20.1-6.2.11-beta-all.jar
    modRuntimeOnly("curse.maven:i-wanna-skate-695018:4576733")
    modRuntimeOnly("curse.maven:coroutil-237749:5096038")
    modRuntimeOnly("curse.maven:what-are-they-up-to-945479:6315241")
    modRuntimeOnly("curse.maven:xaeros-minimap-263420:6778028")
    modRuntimeOnly("curse.maven:shulkerboxtooltip-315811:4611155")
    modRuntimeOnly("curse.maven:collective-342584:7062349")
    modRuntimeOnly("curse.maven:fire-spread-tweaks-446366:6186005")
    modRuntimeOnly("curse.maven:crash-utilities-371813:4716086")

    // LeakDiagTool
    //modRuntimeOnly("blank:leakdiagtool:1.0.0")
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
            "geckolib_version_range" to geckolibVersionRange,
            "moonlight_version_range" to moonlightVersionRange
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
    module {
        isDownloadJavadoc = true
        isDownloadSources = true
    }
}
