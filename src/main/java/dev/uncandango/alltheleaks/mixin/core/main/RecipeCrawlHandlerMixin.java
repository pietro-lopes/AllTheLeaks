package dev.uncandango.alltheleaks.mixin.core.main;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import dev.uncandango.alltheleaks.annotation.CompatibleHashes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.violetmoon.zeta.event.play.ZServerTick;
import org.violetmoon.zeta.util.handler.RecipeCrawlHandler;

@Mixin(value = RecipeCrawlHandler.class, remap = false)
public class RecipeCrawlHandlerMixin {
	@Shadow
	@Final
	private static Multimap<Item, ItemStack> backwardsVanillaDigestion;

	@Shadow
	@Final
	private static Multimap<Item, ItemStack> vanillaRecipeDigestion;

	@CompatibleHashes(values = 2140087603)
	@ModifyArg(method = "onTick", at = @At(value = "INVOKE", target = "Lorg/violetmoon/zeta/event/play/ZRecipeCrawl$Digest;<init>(Lcom/google/common/collect/Multimap;Lcom/google/common/collect/Multimap;)V"), index = 0)
	private static Multimap<Item, ItemStack> wrapFirstMap(Multimap<Item, ItemStack> digestion){
		return ArrayListMultimap.create(digestion);
	}

	@CompatibleHashes(values = 2140087603)
	@ModifyArg(method = "onTick", at = @At(value = "INVOKE", target = "Lorg/violetmoon/zeta/event/play/ZRecipeCrawl$Digest;<init>(Lcom/google/common/collect/Multimap;Lcom/google/common/collect/Multimap;)V"), index = 1)
	private static Multimap<Item, ItemStack> wrapSecondMap(Multimap<Item, ItemStack> digestion){
		return ArrayListMultimap.create(digestion);
	}

	@CompatibleHashes(values = 2140087603)
	@Inject(method = "onTick", at = @At(value = "INVOKE", target = "Lorg/violetmoon/zeta/util/handler/RecipeCrawlHandler;fire(Lorg/violetmoon/zeta/event/bus/IZetaPlayEvent;)V", shift = At.Shift.AFTER))
	private static void clearMaps(ZServerTick.Start tick, CallbackInfo ci){
		backwardsVanillaDigestion.clear();
		vanillaRecipeDigestion.clear();
	}
}
