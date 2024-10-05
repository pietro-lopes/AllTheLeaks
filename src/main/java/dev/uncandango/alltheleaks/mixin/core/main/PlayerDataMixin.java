package dev.uncandango.alltheleaks.mixin.core.main;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.uncandango.alltheleaks.leaks.common.mods.journeymap.UntrackedIssue001;
import dev.uncandango.alltheleaks.mixin.OnCloneEvent;
import journeymap.common.nbt.PlayerData;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Map;
import java.util.UUID;

@Mixin(value = PlayerData.class, remap = false)
public abstract class PlayerDataMixin implements OnCloneEvent {
	@Shadow
	private CompoundTag data;

	@Shadow
	Map<String, PlayerData.Player> playerMap;

	@Shadow
	public abstract PlayerData.Player getPlayer(ServerPlayer serverPlayer);

	@Override
	public void atl$onCloneEvent(PlayerEvent.Clone event) {
		var playerUUID = event.getEntity().getStringUUID();
		this.data.remove(playerUUID);
		this.playerMap.remove(playerUUID);
		this.getPlayer((ServerPlayer)event.getEntity());
	}

//	@Mixin(value = PlayerData.Player.class, remap = false)
//	public static class PlayerMixin {
//		@WrapOperation(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/players/PlayerList;getPlayer(Ljava/util/UUID;)Lnet/minecraft/server/level/ServerPlayer;"))
//		private ServerPlayer atl$getPlayerFromEvent(PlayerList instance, UUID playerUUID, Operation<ServerPlayer> original){
//			var origPlayer = original.call(instance, playerUUID);
//			if (origPlayer.isRemoved()) {
//				return UntrackedIssue001.clonedPlayer;
//			}
//			return origPlayer;
//		}
//	}
}
