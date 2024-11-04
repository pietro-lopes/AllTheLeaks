package dev.uncandango.alltheleaks.mixin.core.main;

import com.llamalad7.mixinextras.sugar.Local;
import dev.uncandango.alltheleaks.mixin.UpdateableLevel;
import jeresources.util.FakeClientLevel;
import jeresources.util.MobTableBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Supplier;

@Mixin(value = MobTableBuilder.class, remap = false)
public class MobTableBuilderMixin implements UpdateableLevel<MobTableBuilder> {

	@Shadow
	@Mutable
	@Final
	private Level level;

	@ModifyArg(method = "add", at = @At(value = "INVOKE", target = "Ljava/util/Map;put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;"), index = 1)
	private Object atl$wrapLevel(Object supplier, @Local(argsOnly = true) EntityType<?> entityType) {
		return (Supplier<LivingEntity>) () -> {
			if(level instanceof FakeClientLevel fakeLevel) {
				return (LivingEntity) entityType.create(fakeLevel);
			} else return (LivingEntity) entityType.create(Minecraft.getInstance().level);
		};
	}

	@ModifyArg(method = "addSheep", at = @At(value = "INVOKE", target = "Ljava/util/Map;put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;"), index = 1)
	private Object atl$wrapLevel2(Object supplier, @Local(argsOnly = true) EntityType<?> entityType, @Local(argsOnly = true) DyeColor dye) {
		return (Supplier<LivingEntity>) () -> {
			Level localLevel;
			if (level instanceof FakeClientLevel fakeLevel) {
				localLevel = fakeLevel;
			} else {
				localLevel = Minecraft.getInstance().level;
			}

			Sheep sheep = (Sheep) entityType.create(localLevel);

			assert sheep != null;

			sheep.setColor(dye);
			return sheep;
		};
	}

	@Inject(method = "<init>", at = @At("TAIL"))
	private void registerInstance(CallbackInfo ci){
		UpdateableLevel.register(this);
	}

	@Override
	public void atl$onClientLevelUpdated(@Nullable ClientLevel level) {
		if (level != null) this.level = level;
	}
}
