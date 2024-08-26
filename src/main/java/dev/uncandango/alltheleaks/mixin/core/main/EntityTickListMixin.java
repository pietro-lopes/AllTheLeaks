package dev.uncandango.alltheleaks.mixin.core.main;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Consumer;

@Mixin(net.minecraft.world.level.entity.EntityTickList.class)
public class EntityTickListMixin {
	@Shadow
	private Int2ObjectMap<Entity> passive;

//	@Unique
//	private Int2ObjectMap<Entity> atl$debugEntityList = new Int2ObjectLinkedOpenHashMap<>();

	@Inject(method = "forEach", at = @At(value = "FIELD", target = "Lnet/minecraft/world/level/entity/EntityTickList;iterated:Lit/unimi/dsi/fastutil/ints/Int2ObjectMap;", shift = At.Shift.AFTER, ordinal = 2))
	private void atl$clearPassive(Consumer<Entity> p_entity, CallbackInfo ci) {
		this.passive.clear();
	}

//	@Inject(method = "add", at = @At("TAIL"))
//	private void atl$addToList(Entity entity, CallbackInfo ci){
//		atl$debugEntityList.put(entity.getId(), entity);
//	}
//
//	@Inject(method = "remove", at = @At("TAIL"))
//	private void atl$removeFromList(Entity entity, CallbackInfo ci){
//		atl$debugEntityList.remove(entity.getId());
//	}
}
