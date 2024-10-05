package dev.uncandango.alltheleaks.mixin.core.main;

import dev.ftb.mods.ftbquests.FTBQuestsEventHandler;
import dev.ftb.mods.ftbquests.quest.BaseQuestFile;
import dev.ftb.mods.ftbquests.quest.ServerQuestFile;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = FTBQuestsEventHandler.class, remap = false)
public abstract class FTBQuestsEventHandlerMixin {

	@Shadow
	protected abstract void fileCacheClear(BaseQuestFile file);

	@Inject(method = "serverStopped", at = @At(value = "FIELD", target = "Ldev/ftb/mods/ftbquests/quest/ServerQuestFile;INSTANCE:Ldev/ftb/mods/ftbquests/quest/ServerQuestFile;", ordinal = 2))
	void atl$clearTasks(MinecraftServer server, CallbackInfo ci) {
		fileCacheClear(ServerQuestFile.INSTANCE);
	}
}
