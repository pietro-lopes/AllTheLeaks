package dev.uncandango.alltheleaks.leaks.common.mods.pneumaticcraft;

import dev.uncandango.alltheleaks.annotation.Issue;
import dev.uncandango.alltheleaks.mixin_extensions.EventKey;
import me.desht.pneumaticcraft.common.drone.IDroneBase;
import me.desht.pneumaticcraft.common.entity.drone.AbstractDroneEntity;
import me.desht.pneumaticcraft.common.entity.drone.DroneEntity;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.EntityLeaveLevelEvent;

@Issue(modId = "pneumaticcraft", versionRange = "(,8.0.3]", mixins = "DroneEntityMixin")
public class UntrackedIssue001 {

	public UntrackedIssue001() {
		var gameBus = NeoForge.EVENT_BUS;
		gameBus.addListener(this::unregisterDrones);
	}

	private void unregisterDrones(EntityLeaveLevelEvent event){
		if (event.getEntity().level().isClientSide()) return;
		if (event.getEntity() instanceof EventKey obj) {
			NeoForge.EVENT_BUS.unregister(obj.atl$getKeyMap().get("onSemiblockEvent"));
		}
	}
}
