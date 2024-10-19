package dev.uncandango.alltheleaks.mixin.core.main;

import com.klikli_dev.occultism.Occultism;
import com.klikli_dev.occultism.common.item.spirit.MinerSpiritItem;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;

@Debug(print = true, export = true)
@Mixin(value = MinerSpiritItem.class, remap = false)
public class MinerSpiritItemMixin extends Item {
	public MinerSpiritItemMixin(Properties properties) {
		super(properties);
	}

	@WrapMethod(method = "initCapabilities")
	private ICapabilityProvider atl$saferInitCapabilites(ItemStack stack, CompoundTag nbt, Operation<ICapabilityProvider> original) {
		if (!Occultism.SERVER_CONFIG.spec.isLoaded()) {
			return super.initCapabilities(stack, nbt);
		} else {
			return original.call(stack, nbt);
		}
	}
}
