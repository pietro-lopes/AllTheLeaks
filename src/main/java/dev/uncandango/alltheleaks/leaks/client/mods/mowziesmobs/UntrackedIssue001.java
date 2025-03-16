package dev.uncandango.alltheleaks.leaks.client.mods.mowziesmobs;

import com.bobmowzie.mowziesmobs.client.ClientProxy;
import com.bobmowzie.mowziesmobs.client.sound.BossMusicPlayer;
import dev.uncandango.alltheleaks.annotation.Issue;
import dev.uncandango.alltheleaks.mixin.UpdateableLevel;
import dev.uncandango.alltheleaks.utils.ReflectionHelper;
import net.minecraftforge.common.MinecraftForge;

import java.lang.invoke.VarHandle;
import java.util.List;

@Issue(modId = "mowziesmobs", versionRange = "[1.6.4,1.6.5]", mixins = {"main.MMModelAnimatorMixin","main.RenderUmvuthiMixin","main.RenderGeomancyBaseMixin"}, description = "Stops boss music on level change and updates entity at `MMModelAnimator`/`RenderGeomancyBase`")
public class UntrackedIssue001 {
	public static final VarHandle SUNBLOCK_SOUNDS;

	public UntrackedIssue001() {
		var gameBus = MinecraftForge.EVENT_BUS;
		gameBus.addListener(this::clearSoundInstances);
	}

	static {
		SUNBLOCK_SOUNDS = ReflectionHelper.getFieldFromClass(ClientProxy.class, "sunblockSounds", List.class, true);
		// Dummy to validate variable call
		var dummy = BossMusicPlayer.bossMusic;
	}

	private void clearSoundInstances(UpdateableLevel.RenderEnginesUpdated event) {
		((List)SUNBLOCK_SOUNDS.get()).clear();
		if (BossMusicPlayer.bossMusic != null) {
			BossMusicPlayer.bossMusic.setBoss(null);
		}
	}
}
