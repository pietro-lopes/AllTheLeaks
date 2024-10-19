package dev.uncandango.alltheleaks.mixin.core.main;

import dev.ftb.mods.ftbquests.FTBQuestsEventHandler;
import dev.ftb.mods.ftbquests.quest.task.KillTask;
import dev.ftb.mods.ftbquests.quest.task.Task;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Pseudo
@Mixin(value = FTBQuestsEventHandler.class, remap = false)
public class FTBQuestsEventHandlerMixin {
    @Shadow
    private List<KillTask> killTasks;

    @Shadow
    private List<Task> autoSubmitTasks;

    @Inject(method = "serverStopped", at = @At("TAIL"))
    void atl$clearTasks(MinecraftServer server, CallbackInfo ci) {
        if (killTasks != null) killTasks.clear();
        if (autoSubmitTasks != null) autoSubmitTasks.clear();
    }
}
