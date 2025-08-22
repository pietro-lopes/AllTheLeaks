package dev.uncandango.alltheleaks.mixin.core.main;

import dev.uncandango.alltheleaks.mixin.Trackable;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Level.class)
public class LevelMixin implements Trackable {
	@Override
	public Class<?> atl$getBaseClass() {
		return Level.class;
	}
}
