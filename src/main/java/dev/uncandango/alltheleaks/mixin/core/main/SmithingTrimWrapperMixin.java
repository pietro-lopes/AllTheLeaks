package dev.uncandango.alltheleaks.mixin.core.main;

import com.buuz135.smithingtemplateviewer.SmithingTrimWrapper;
import dev.uncandango.alltheleaks.mixin.UpdateableLevel;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.item.crafting.SmithingTrimRecipe;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@MethodsReturnNonnullByDefault
@Mixin(SmithingTrimWrapper.class)
public abstract class SmithingTrimWrapperMixin implements UpdateableLevel<SmithingTrimWrapper> {
	@Unique
	private static final EntityType.EntityFactory<ArmorStand> atl$factory = ((entityType, level) -> {
		var armorStand = entityType.create(level);
		armorStand.setNoBasePlate(true);
		armorStand.setShowArms(true);
		armorStand.yBodyRot = 210.0F;
		armorStand.setXRot(25.0F);
		armorStand.yHeadRot = armorStand.getYRot();
		armorStand.yHeadRotO = armorStand.getYRot();
		return armorStand;
	});
	@Shadow
	private ArmorStand armorStand;

	@Inject(method = "<init>", at = @At("TAIL"))
	private void registerInstance(SmithingTrimRecipe recipe, CallbackInfo ci) {
		UpdateableLevel.register(this);
	}

	@Override
	public void atl$onClientLevelUpdated(@Nullable ClientLevel level) {
		if (level == null && armorStand != null) {
			armorStand = null;
		} else {
			if (armorStand != null && armorStand.level() != level) {
				this.armorStand = atl$factory.create(EntityType.ARMOR_STAND, level);
				this.updateArmorStand();
			}
		}
	}

	@Shadow
	public abstract void updateArmorStand();
}
