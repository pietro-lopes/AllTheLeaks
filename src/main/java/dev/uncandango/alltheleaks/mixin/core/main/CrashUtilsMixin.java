package dev.uncandango.alltheleaks.mixin.core.main;

import com.darkere.crashutils.CrashUtils;
import com.darkere.crashutils.DataStructures.PlayerActivityHistory;
import dev.uncandango.alltheleaks.annotation.CompatibleHashes;
import net.minecraft.world.level.Level;
import net.minecraftforge.server.ServerLifecycleHooks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Timer;
import java.util.TimerTask;

@Mixin(value = CrashUtils.class, remap = false)
public class CrashUtilsMixin {

	@CompatibleHashes(values = -346956539)
	@Redirect(method = "setupFtbChunksUnloading", at = @At(value = "INVOKE", target = "Ljava/util/Timer;scheduleAtFixedRate(Ljava/util/TimerTask;JJ)V"))
	private void cancelTimerIfExists(Timer instance, TimerTask task, long delay, long period){
		instance.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				var server = ServerLifecycleHooks.getCurrentServer();
				if (server == null) return;
				var world = server.getLevel(Level.OVERWORLD);
				if (world == null) return;
				PlayerActivityHistory history = new PlayerActivityHistory(world);
				CrashUtils.LOGGER.info("Unloading chunks for players that have not been online in: " + CrashUtils.SERVER_CONFIG.getExpireTimeInDays() + " Days");
				CrashUtils.LOGGER.info(history.getPlayersInChunkClearTime().size() + " Player(s) affected ");

				for (String player : history.getPlayersInChunkClearTime()) {
					CrashUtils.LOGGER.info("Unloading " + player + "'s Chunks");
					world.getServer().getCommands().performPrefixedCommand(world.getServer().createCommandSourceStack(), "ftbchunks unload_all " + player);
				}
			}
		}, delay, period);
	}
}
