package dev.uncandango.alltheleaks.leaks.client.mods.mowziesmobs;

import com.bobmowzie.mowziesmobs.client.ClientProxy;
import com.bobmowzie.mowziesmobs.client.sound.BossMusicPlayer;
import com.bobmowzie.mowziesmobs.server.entity.MowzieEntity;
import dev.uncandango.alltheleaks.AllTheLeaks;
import dev.uncandango.alltheleaks.annotation.Issue;
import dev.uncandango.alltheleaks.mixin.UpdateableLevel;
import dev.uncandango.alltheleaks.utils.ReflectionHelper;
import net.minecraftforge.common.MinecraftForge;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodType;
import java.lang.invoke.VarHandle;
import java.util.List;

@Issue(modId = "mowziesmobs", versionRange = "[1.7.0,)", mixins = {"main.MMModelAnimatorMixin","main.RenderUmvuthiMixin","main.RenderGeomancyBaseMixin"}, description = "Stops boss music on level change and updates entity at `MMModelAnimator`/`RenderGeomancyBase`")
public class UntrackedIssue002 {
	public static final VarHandle SUNBLOCK_SOUNDS;
	public static final VarHandle CURRENT_MUSIC;
	public static final MethodHandle GET_BOSS;
	public static final MethodHandle SET_BOSS;

	public UntrackedIssue002() {
		var gameBus = MinecraftForge.EVENT_BUS;
		gameBus.addListener(this::clearSoundInstances);
	}

	static {
		SUNBLOCK_SOUNDS = ReflectionHelper.getFieldFromClass(ClientProxy.class, "sunblockSounds", List.class, true);
		var bossMusicClass = ReflectionHelper.getClass("com.bobmowzie.mowziesmobs.client.sound.BossMusic");
		GET_BOSS = ReflectionHelper.getMethodFromClass(bossMusicClass, "getBoss", MethodType.methodType(MowzieEntity.class), false);
		SET_BOSS = ReflectionHelper.getMethodFromClass(bossMusicClass, "setBoss", MethodType.methodType(void.class, MowzieEntity.class), false);
		CURRENT_MUSIC = ReflectionHelper.getFieldFromClass(BossMusicPlayer.class, "currentMusic", bossMusicClass, true);
	}

	private void clearSoundInstances(UpdateableLevel.RenderEnginesUpdated event) {
		((List)SUNBLOCK_SOUNDS.get()).clear();
		if (CURRENT_MUSIC.get() != null) {
			try {
				SET_BOSS.invoke(CURRENT_MUSIC.get(), (Object) null);
			} catch (Throwable e) {
				AllTheLeaks.LOGGER.error("Failed to set boss to null.", e);
			}
		}
	}
}
