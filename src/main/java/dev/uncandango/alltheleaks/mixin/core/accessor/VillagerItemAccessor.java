package dev.uncandango.alltheleaks.mixin.core.accessor;

import de.maxhenkel.easyvillagers.corelib.CachedMap;
import de.maxhenkel.easyvillagers.entity.EasyVillagerEntity;
import de.maxhenkel.easyvillagers.items.VillagerItem;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value = VillagerItem.class, remap = false)
public interface VillagerItemAccessor {
	@Accessor("cachedVillagers")
	CachedMap<ItemStack, EasyVillagerEntity> getCachedVillagers();
}
