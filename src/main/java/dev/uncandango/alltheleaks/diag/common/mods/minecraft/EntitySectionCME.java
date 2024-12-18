package dev.uncandango.alltheleaks.diag.common.mods.minecraft;

import dev.uncandango.alltheleaks.annotation.Issue;

import java.util.concurrent.atomic.AtomicBoolean;

@Issue(modId = "minecraft", issueId = "Entity Section CME Debug", versionRange = "1.21.1", mixins = {"main.EntitySectionMixin","main.PersistentEntitySectionManagerMixin"}, config = "entitySectionCME")
public class EntitySectionCME {
	public static AtomicBoolean SHOULD_LOG = new AtomicBoolean(false);

}
