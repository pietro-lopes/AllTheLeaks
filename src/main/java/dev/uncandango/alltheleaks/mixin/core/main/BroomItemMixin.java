package dev.uncandango.alltheleaks.mixin.core.main;

import dev.uncandango.alltheleaks.mixin.UpdateableLevel;
import net.joefoxe.hexerei.client.renderer.entity.custom.BroomEntity;
import net.joefoxe.hexerei.item.custom.BroomItem;
import net.joefoxe.hexerei.util.CachedMap;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BroomItem.class)
public class BroomItemMixin implements UpdateableLevel<BroomItem> {
	@Shadow
	@Final
	private CachedMap<ItemStack, BroomEntity> cachedBroom;

	@Inject(method = "<init>", at = @At("RETURN"))
	private void grabObject(String broomType, Item.Properties properties, CallbackInfo ci) {
		UpdateableLevel.register(this);
	}

	@Override
	public void atl$onClientLevelUpdated(@Nullable ClientLevel level) {
		this.cachedBroom.clear();
	}
}
