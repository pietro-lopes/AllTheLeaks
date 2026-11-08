package dev.uncandango.alltheleaks.mixin.core.main;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import dev.uncandango.alltheleaks.feature.common.mods.minecraft.PlayerReviveFix;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.common.capabilities.CapabilityProvider;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Entity.class)
public abstract class EntityMixin3 extends CapabilityProvider<Entity> {
	protected EntityMixin3(Class<Entity> baseClass) {
		super(baseClass);
	}

	@WrapMethod(method = "revive", remap = false)
	private void shouldRevive(Operation<Void> original) {
		if (!PlayerReviveFix.shouldRedirectToReviveCaps()) {
			original.call();
		} else {
			this.reviveCaps();
		}
	}
}
