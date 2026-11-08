package dev.uncandango.alltheleaks.feature.common.mods.minecraft;

import net.minecraftforge.common.util.Lazy;
import dev.uncandango.alltheleaks.AllTheLeaks;
import dev.uncandango.alltheleaks.annotation.Issue;
import dev.uncandango.alltheleaks.exceptions.ATLIllegalState;
import net.minecraftforge.fml.ModList;

import java.util.List;

@Issue(modId = "minecraft", issueId = "Player Revive Fix", versionRange = "1.20.1", mixins = {"main.EntityMixin3"})
public class PlayerReviveFix {
	private static List<String> BLACKLISTED_MODS = List.of("playerrevive");
	public static final Lazy<Boolean> ANY_BLACKLISTED_MOD_LOADED = Lazy.of(() -> {
		for (var mod : BLACKLISTED_MODS) {
			if (ModList.get().isLoaded(mod)){
				return true;
			}
		}
		return false;
	});

	private static boolean IS_BEING_CLONED = false;

	public static void setIsBeingCloned(boolean isBeingCloned) {
		IS_BEING_CLONED = isBeingCloned;
	}

	public static boolean shouldRedirectToReviveCaps(){
		if (Boolean.TRUE.equals(ANY_BLACKLISTED_MOD_LOADED.get())) {
			return false;
		}
		if (IS_BEING_CLONED) {
			AllTheLeaks.LOGGER.warn("A mod called revive instead of reviveCaps, redirecting...");
			AllTheLeaks.LOGGER.debug("Redirecting to ReviveCaps", new ATLIllegalState(ATLIllegalState.TYPE.NO_REMOVAL_REASON,"Cannot have null removal reason on Clone event!"));
		}
		return IS_BEING_CLONED;
	}
}
