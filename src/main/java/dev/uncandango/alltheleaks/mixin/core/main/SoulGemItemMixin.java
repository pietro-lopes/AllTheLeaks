package dev.uncandango.alltheleaks.mixin.core.main;

import com.klikli_dev.occultism.common.item.tool.SoulGemItem;
import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import dev.uncandango.alltheleaks.annotation.CompatibleHashes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(SoulGemItem.class)
public class SoulGemItemMixin {
	@Unique
	private static final CompoundTag ATL$DUMMY_TAG = new CompoundTag();

	@CompatibleHashes(values = {1629780409,-1460291271})
	@WrapOperation(method = {"getDescriptionId", "appendHoverText"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;getOrCreateTag()Lnet/minecraft/nbt/CompoundTag;"))
	private CompoundTag atl$preventCreatingTag(ItemStack instance, Operation<CompoundTag> original){
		var tag = instance.getTagElement("entityData");
		if (tag == null) {
			return ATL$DUMMY_TAG;
		} else {
			return instance.getTag();
		}
	}

}
