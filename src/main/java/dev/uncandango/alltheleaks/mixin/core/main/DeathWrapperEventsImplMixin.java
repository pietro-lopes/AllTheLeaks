package dev.uncandango.alltheleaks.mixin.core.main;

import com.google.common.collect.ImmutableList;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import io.wispforest.accessories.api.DropRule;
import io.wispforest.accessories.api.events.OnDeathCallback;
import io.wispforest.accessories.api.events.OnDropCallback;
import io.wispforest.accessories.api.slot.SlotReference;
import io.wispforest.cclayer.DeathWrapperEventsImpl;
import net.minecraft.util.Tuple;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import top.theillusivec4.curios.api.event.DropRulesEvent;
import top.theillusivec4.curios.api.type.capability.ICurio;
import top.theillusivec4.curios.compat.CuriosWrappingUtils;

import java.util.function.Predicate;

@Mixin(DeathWrapperEventsImpl.class)
public abstract class DeathWrapperEventsImplMixin implements OnDeathCallback, OnDropCallback {
	@Unique
	private static ImmutableList<Tuple<Predicate<ItemStack>, ICurio.DropRule>> atl$overrides;

	@WrapOperation(method = "shouldDrop", at = @At(value = "FIELD", target = "Lio/wispforest/cclayer/DeathWrapperEventsImpl;latestDropRules:Ltop/theillusivec4/curios/api/event/DropRulesEvent;"))
	private void getOverrides(DeathWrapperEventsImpl instance, DropRulesEvent value, Operation<Void> original){
		atl$overrides = value.getOverrides();
	}

	/**
	 * @author Uncandango
	 * @reason Should use overrides directly
	 */
	@Overwrite
	public @Nullable DropRule onDrop(DropRule dropRule, ItemStack stack, SlotReference reference, DamageSource damageSource){
		if(atl$overrides != null) {
			for (var override : atl$overrides) {
				if (override.getA().test(stack)) return CuriosWrappingUtils.convert(override.getB());
			}
		}

		return null;
	}


}
