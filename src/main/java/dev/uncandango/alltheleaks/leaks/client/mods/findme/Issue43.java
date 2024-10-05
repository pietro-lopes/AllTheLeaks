package dev.uncandango.alltheleaks.leaks.client.mods.findme;

import com.buuz135.findme.FindMeModClient;
import dev.uncandango.alltheleaks.annotation.Issue;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.level.LevelEvent;

@Issue(modId = "findme", versionRange = "(,3.3.1]")
public class Issue43 {
	public Issue43() {
		var gameBus = NeoForge.EVENT_BUS;
		gameBus.addListener(this::clearLastStack);
	}

	private void clearLastStack(LevelEvent.Unload event){
		if (event.getLevel().isClientSide()) {
			FindMeModClient.lastRenderedStack = ItemStack.EMPTY;
		}
	}
}
