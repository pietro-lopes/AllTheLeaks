package dev.uncandango.alltheleaks.mixin.core.accessor;

import com.anthonyhilyard.iceberg.renderer.CustomItemRenderer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.entity.animal.horse.Horse;
import net.minecraft.world.entity.decoration.ArmorStand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value = CustomItemRenderer.class, remap = false)
public interface CustomItemRendererAccessor {
	@Mutable
	@Accessor("entity")
	static void setEntity(Entity entity) {}

	@Mutable
	@Accessor("horse")
	static void setHorse(Horse horse){};

	@Mutable
	@Accessor("armorStand")
	static void setArmorStand(ArmorStand armorStand){};

	@Mutable
	@Accessor("wolf")
	static void setWolf(Wolf wolf){};
}
