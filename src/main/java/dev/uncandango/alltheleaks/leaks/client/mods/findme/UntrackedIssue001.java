package dev.uncandango.alltheleaks.leaks.client.mods.findme;

import com.buuz135.findme.FindMeModClient;
import dev.uncandango.alltheleaks.annotation.Issue;
import dev.uncandango.alltheleaks.mixin.UpdateableLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;

@Issue(modId = "findme", versionRange = "[3.1.0,)", description = "Clears `FindMeModClient#lastRenderedStack` on client level update")
public class UntrackedIssue001 {
	public UntrackedIssue001() {
		var gameBus = MinecraftForge.EVENT_BUS;
		gameBus.addListener(this::clearLastRenderedStack);
	}

	static {
		// Dummy values to validate variable calls
		var dummy = FindMeModClient.lastRenderedStack;
		var dummy2 = FindMeModClient.lastTooltipTime;
	}

	private void clearLastRenderedStack(UpdateableLevel.RenderEnginesUpdated event) {
		FindMeModClient.lastRenderedStack = ItemStack.EMPTY;
		FindMeModClient.lastTooltipTime = 0L;
	}
}
