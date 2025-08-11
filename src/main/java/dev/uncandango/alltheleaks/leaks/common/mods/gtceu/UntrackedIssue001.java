package dev.uncandango.alltheleaks.leaks.common.mods.gtceu;

import com.google.common.cache.Cache;
import com.gregtechceu.gtceu.api.data.worldgen.ores.OreGenCache;
import com.gregtechceu.gtceu.api.data.worldgen.ores.OrePlacer;
import dev.uncandango.alltheleaks.annotation.Issue;
import dev.uncandango.alltheleaks.utils.ReflectionHelper;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.level.LevelEvent;

import java.lang.invoke.VarHandle;
import java.util.Arrays;

@Issue(modId = "gtceu", versionRange = "*")
public class UntrackedIssue001 {
	private static final VarHandle GTCEU$ORE_PLACER;
	private static final VarHandle ORE_GEN_CACHE;

	static {
		GTCEU$ORE_PLACER = ReflectionHelper.getFieldFromClass(ChunkGenerator.class, "gtceu$orePlacer", OrePlacer.class, false);
		ORE_GEN_CACHE = ReflectionHelper.getFieldFromClass(OrePlacer.class, "oreGenCache", OreGenCache.class, false);
	}

	public UntrackedIssue001() {
		var gameBus = MinecraftForge.EVENT_BUS;
		gameBus.addListener(this::clearOreCache);
	}

	private void clearOreCache(LevelEvent.Unload event) {
		if (!event.getLevel().isClientSide()) {
			if (event.getLevel() instanceof ServerLevel serverLevel) {
				var generator = serverLevel.getChunkSource().getGenerator();
				var orePlacer = (OrePlacer)GTCEU$ORE_PLACER.get(generator);
				var oreGenCache = (OreGenCache)ORE_GEN_CACHE.get(orePlacer);
				Arrays.stream(OreGenCache.class.getDeclaredFields()).forEach(field -> {
					if (field.getType() == Cache.class) {
						try {
							 field.setAccessible(true);
							 ((Cache)field.get(oreGenCache)).invalidateAll();
						} catch (Throwable ignore){
						}
					}
				});
			}
		}
	}
}
