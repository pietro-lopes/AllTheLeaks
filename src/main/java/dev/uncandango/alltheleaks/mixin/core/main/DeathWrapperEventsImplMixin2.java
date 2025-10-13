package dev.uncandango.alltheleaks.mixin.core.main;

import com.google.common.collect.ImmutableList;
import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.ModifyReceiver;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import io.wispforest.accessories.api.events.OnDeathCallback;
import io.wispforest.accessories.api.events.OnDropCallback;
import net.minecraft.util.Tuple;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Coerce;
import org.spongepowered.asm.mixin.injection.Redirect;
import top.theillusivec4.curios.api.event.DropRulesEvent;
import top.theillusivec4.curios.api.type.capability.ICurio;

import java.util.function.Predicate;

@Mixin(targets = "io.wispforest.accessories_compat.curios.AccessoriesEventHooks$DeathWrapperEventsImpl")
public abstract class DeathWrapperEventsImplMixin2 implements OnDeathCallback, OnDropCallback {
	@Unique
	private static ImmutableList<Tuple<Predicate<ItemStack>, ICurio.DropRule>> atl$overrides;

	@WrapOperation(method = "shouldDrop", at = @At(value = "FIELD", target = "Lio/wispforest/accessories_compat/curios/AccessoriesEventHooks$DeathWrapperEventsImpl;latestDropRules:Ltop/theillusivec4/curios/api/event/DropRulesEvent;"))
	private void getOverrides(@Coerce Object instance, DropRulesEvent value, Operation<Void> original){
		atl$overrides = value.getOverrides();
	}

	@Definition(id = "latestDropRules", field = "Lio/wispforest/accessories_compat/curios/AccessoriesEventHooks$DeathWrapperEventsImpl;latestDropRules:Ltop/theillusivec4/curios/api/event/DropRulesEvent;")
	@Expression("this.latestDropRules != null")
	@ModifyExpressionValue(method = "onDrop", at = @At("MIXINEXTRAS:EXPRESSION"))
	private boolean checkIfOverridesNull(boolean original){
		return atl$overrides != null || original;
	}

	@Redirect(method = "onDrop", at = @At(value = "INVOKE", target = "Ltop/theillusivec4/curios/api/event/DropRulesEvent;getOverrides()Lcom/google/common/collect/ImmutableList;"))
	private ImmutableList<?> preventNullCall(DropRulesEvent instance){
		if (instance == null) return null;
		return instance.getOverrides();
	}

	@ModifyReceiver(method = "onDrop", at = @At(value = "INVOKE", target = "Lcom/google/common/collect/ImmutableList;iterator()Lcom/google/common/collect/UnmodifiableIterator;"))
	private ImmutableList<?> replaceIfNeeded(ImmutableList instance){
		if (instance == null) return atl$overrides;
		return instance;
	}

//	/**
//	 * @author Uncandango
//	 * @reason Should use overrides directly
//	 */
//	@Overwrite
//	public @Nullable DropRule onDrop(DropRule dropRule, ItemStack stack, SlotReference reference, DamageSource damageSource){
//		if(atl$overrides != null) {
//			for (var override : atl$overrides) {
//				if (override.getA().test(stack)) return CuriosWrappingUtils.convert(override.getB());
//			}
//		}
//
//		return null;
//	}


}
