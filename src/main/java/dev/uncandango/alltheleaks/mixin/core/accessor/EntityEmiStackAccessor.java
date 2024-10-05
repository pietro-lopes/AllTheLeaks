package dev.uncandango.alltheleaks.mixin.core.accessor;

import fzzyhmstrs.emi_loot.util.EntityEmiStack;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(EntityEmiStack.class)
public interface EntityEmiStackAccessor {
	@Mutable
	@Accessor("entity")
	void setEntity(Entity entity);
}
