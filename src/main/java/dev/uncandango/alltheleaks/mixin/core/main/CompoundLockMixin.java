package dev.uncandango.alltheleaks.mixin.core.main;

import dev.uncandango.alltheleaks.implementations.ATLUnmodifiable;
import dev.uncandango.alltheleaks.mixin.Lockable;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import java.util.Map;

@Mixin(CompoundTag.class)
public class CompoundLockMixin implements Lockable {
	@Unique
	private boolean atl$locked = false;

	@Override
	public boolean atl$isLocked() {
		return atl$locked;
	}

	@Override
	public void atl$setLocked(boolean locked) {
		if (!atl$locked && locked) {
			atl$lockCompoundTag((CompoundTag)(Object) this);
		}
		atl$locked = locked;
	}

	@Unique
	private static void atl$lockCompoundTag(CompoundTag tag) {
		Map<String, Tag> oldMap = ObfuscationReflectionHelper.getPrivateValue(CompoundTag.class, tag, FMLEnvironment.production ? "f_128329_" : "tags");
		if (!oldMap.getClass().getSimpleName().equals("ATLUnmodifiable")) {
			ObfuscationReflectionHelper.setPrivateValue(CompoundTag.class, tag, ATLUnmodifiable.unmodifiableMap(oldMap), FMLEnvironment.production ? "f_128329_" : "tags");
			for (var key : tag.getAllKeys()) {
				if (tag.get(key) instanceof Lockable lockable){
					lockable.atl$setLocked(true);
				}
			}
		}
	}
}
