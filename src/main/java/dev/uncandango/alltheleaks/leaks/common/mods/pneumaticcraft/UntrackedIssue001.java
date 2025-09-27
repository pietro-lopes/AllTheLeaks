package dev.uncandango.alltheleaks.leaks.common.mods.pneumaticcraft;

import dev.uncandango.alltheleaks.annotation.Issue;

@Issue(modId = "pneumaticcraft", versionRange = "[6.0.15,)", mixins = {"main.DroneEntityMixin", "main.ProgrammableControllerBlockEntityMixin", "main.AerialInterfaceBlockEntityMixin"}, description = "Unregister drones from MinecraftForge event via `Entity#onRemovedFromWorld()`")
public class UntrackedIssue001 {
}
