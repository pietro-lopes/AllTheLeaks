package dev.uncandango.alltheleaks.leaks.client.mods.journeymap;

import dev.uncandango.alltheleaks.annotation.Issue;
import dev.uncandango.alltheleaks.mixin.core.accessor.EntityComparatorAccessor;
import dev.uncandango.alltheleaks.utils.ReflectionHelper;
import journeymap.client.model.EntityHelper;

import java.lang.invoke.VarHandle;

@Issue(modId = "journeymap", versionRange = "[1.21-6.0.0-beta.,)", mixins = {"accessor.EntityComparatorAccessor", "main.EntityHelperMixin"})
public class UntrackedIssue001 {
	private static final VarHandle entityDistanceComparator;
	private static final VarHandle entityDTODistanceComparator;
	private static final Class<?> edcClass;
	private static final Class<?> edtoClass;

	static {
		edcClass = ReflectionHelper.getPrivateClass(EntityHelper.class, "journeymap.client.model.EntityHelper$EntityDistanceComparator");
		edtoClass = ReflectionHelper.getPrivateClass(EntityHelper.class, "journeymap.client.model.EntityHelper$EntityDTODistanceComparator");
		entityDistanceComparator = ReflectionHelper.getFieldFromClass(EntityHelper.class, "entityDistanceComparator", edcClass, true);
		entityDTODistanceComparator = ReflectionHelper.getFieldFromClass(EntityHelper.class, "entityDTODistanceComparator", edtoClass, true);
	}

	public UntrackedIssue001() {
//		var gameBus = NeoForge.EVENT_BUS;
//		gameBus.addListener(this::clearPlayerFromComparators);
	}

	public static void clearPlayerFromComparator() {
		var edc = ReflectionHelper.getFieldValue(entityDistanceComparator);
		if (edc instanceof EntityComparatorAccessor accessor) {
			accessor.atl$setPlayer(null);
		}
	}

	public static void clearPlayerFromDTOComparator() {
		var edtoc = ReflectionHelper.getFieldValue(entityDTODistanceComparator);
		if (edtoc instanceof EntityComparatorAccessor accessor) {
			accessor.atl$setPlayer(null);
		}
	}
}
