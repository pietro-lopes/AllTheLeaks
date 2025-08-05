package dev.uncandango.alltheleaks.mixin.core.main;

import blusunrize.immersiveengineering.api.IEApi;
import com.llamalad7.mixinextras.sugar.Local;
import dev.uncandango.alltheleaks.annotation.CompatibleHashes;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.server.ServerLifecycleHooks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import java.util.function.Function;

@Mixin(targets = {"blusunrize.immersiveengineering.common.world.Villages$TradeListing"})
public class VillagesTradelistMixin {

	@CompatibleHashes(values = {1806661297})
	@ModifyArg(method = "<init>(Lblusunrize/immersiveengineering/common/world/Villages$TradeOutline;Lnet/minecraft/tags/TagKey;Lblusunrize/immersiveengineering/common/world/Villages$PriceInterval;II)V", at = @At(value = "INVOKE", target = "Lblusunrize/immersiveengineering/common/world/Villages$TradeListing;<init>(Lblusunrize/immersiveengineering/common/world/Villages$TradeOutline;Ljava/util/function/Function;Lblusunrize/immersiveengineering/common/world/Villages$PriceInterval;II)V"), index = 1)
	private static Function<Level, ItemStack> atl$fixResolveTag(Function<Level, ItemStack> item, @Local(argsOnly = true) TagKey<Item> tag){
		return (level -> {
			if (level == null) {
				var server = ServerLifecycleHooks.getCurrentServer();
				if (server == null) {
					return ItemStack.EMPTY;
				} else {
					return IEApi.getPreferredTagStack(server.registryAccess(), tag);
				}
			} else return IEApi.getPreferredTagStack(level.registryAccess(), tag);
		});
	}
}
