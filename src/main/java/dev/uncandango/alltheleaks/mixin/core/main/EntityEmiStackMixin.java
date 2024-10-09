package dev.uncandango.alltheleaks.mixin.core.main;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import dev.emi.emi.api.stack.EmiStack;
import dev.uncandango.alltheleaks.mixin.EntityEmiStackFactory;
import dev.uncandango.alltheleaks.mixin.UpdateableLevel;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import stepsword.mahoutsukai.integration.emi.EntityEmiStack;

@Pseudo
@Mixin(value = EntityEmiStack.class, remap = false)
public class EntityEmiStackMixin implements UpdateableLevel<EntityEmiStack>, EntityEmiStackFactory {
	@Shadow
	public ResourceLocation loc;
	@Shadow
	Entity entity;
	@Unique
	private EntityType.EntityFactory<Entity> atl$entityFactory;

	@Unique
	private EntityType<Entity> atl$entityType;

	@Inject(method = "<init>", at = @At("RETURN"))
	private void grabEntity(ResourceLocation loc, Entity entity, CallbackInfo ci) {
		atl$entityFactory = EntityType::create;
		atl$entityType = (EntityType<Entity>) entity.getType();
		UpdateableLevel.register(this);
	}

	@WrapMethod(method = "copy()Ldev/emi/emi/api/stack/EmiStack;")
	private EmiStack atl$copy(Operation<EmiStack> original) {
		var factory = this.atl$entityFactory;
		return ((EntityEmiStackFactory) new EntityEmiStack(this.loc, this.entity)).atl$withFactory(factory);
	}

	@Override
	public void onClientLevelUpdated(ClientLevel level) {
		this.entity = atl$entityFactory.create(atl$entityType, level);
	}

	@Override
	public EntityEmiStack atl$withFactory(EntityType.EntityFactory<Entity> factory) {
		atl$entityFactory = factory;
		return (EntityEmiStack) (Object) this;
	}
}
