package dev.uncandango.alltheleaks.mixin.core.main;

import com.minecolonies.api.crafting.GenericRecipe;
import com.minecolonies.api.equipment.registry.EquipmentTypeEntry;
import dev.uncandango.alltheleaks.mixin.UpdateableLevel;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(value = GenericRecipe.class, remap = false)
public class GenericRecipeMixin implements UpdateableLevel<GenericRecipe> {

	@Shadow
	@Mutable
	@Final
	private LivingEntity requiredEntity;

	@Unique
	private EntityType<Animal> atl$entityType;

	@Inject(method = "<init>(Lnet/minecraft/resources/ResourceLocation;Lnet/minecraft/world/item/ItemStack;Ljava/util/List;Ljava/util/List;Ljava/util/List;ILnet/minecraft/world/level/block/Block;Lnet/minecraft/resources/ResourceLocation;Lcom/minecolonies/api/equipment/registry/EquipmentTypeEntry;Lnet/minecraft/world/entity/LivingEntity;Ljava/util/List;I)V", at = @At("TAIL"))
	private void registerInstance(ResourceLocation id, ItemStack output, List<?> altOutputs, List<?> additionalOutputs, List<?> inputs, int gridSize, Block intermediate, ResourceLocation lootTable, EquipmentTypeEntry requiredTool, LivingEntity requiredEntity, List<?> restrictions, int levelSort, CallbackInfo ci) {
		if (requiredEntity != null) {
			//noinspection unchecked
			atl$entityType = (EntityType<Animal>) requiredEntity.getType();
			UpdateableLevel.register(this);
		}
	}

	@Override
	public void atl$onClientLevelUpdated(@Nullable ClientLevel level) {
		if (level != null && this.requiredEntity.level() != level) {
			this.requiredEntity = atl$entityType.create(level);
		}
	}
}
