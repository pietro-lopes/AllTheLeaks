package dev.uncandango.alltheleaks.mixin.core.main;

import com.minecolonies.api.crafting.GenericRecipe;
import com.minecolonies.api.equipment.registry.EquipmentTypeEntry;
import dev.uncandango.alltheleaks.leaks.client.mods.minecolonies.Issue10302;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(GenericRecipe.class)
public class GenericRecipeMixin {
	@Inject(method = "<init>(Lnet/minecraft/resources/ResourceLocation;Lnet/minecraft/world/item/ItemStack;Ljava/util/List;Ljava/util/List;Ljava/util/List;ILnet/minecraft/world/level/block/Block;Lnet/minecraft/resources/ResourceKey;Lcom/minecolonies/api/equipment/registry/EquipmentTypeEntry;Lnet/minecraft/world/entity/LivingEntity;Ljava/util/List;I)V", at = @At("RETURN"))
	private void grabInstance(ResourceLocation id, ItemStack output, List altOutputs, List additionalOutputs, List inputs, int gridSize, Block intermediate, ResourceKey lootTable, EquipmentTypeEntry requiredTool, LivingEntity requiredEntity, List restrictions, int levelSort, CallbackInfo ci){
		if (requiredEntity != null) {
			Issue10302.recipesWithEntities.add((GenericRecipe) (Object) this);
		}
	}
}
