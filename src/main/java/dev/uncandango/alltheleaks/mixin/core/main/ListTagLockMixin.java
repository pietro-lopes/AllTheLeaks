package dev.uncandango.alltheleaks.mixin.core.main;

import dev.uncandango.alltheleaks.implementations.ATLUnmodifiable;
import dev.uncandango.alltheleaks.mixin.Lockable;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import java.util.List;
@Mixin(ListTag.class)
public class ListTagLockMixin implements Lockable {
	@Unique
	private boolean atl$locked = false;

	@Override
	public boolean atl$isLocked() {
		return atl$locked;
	}

	@Override
	public void atl$setLocked(boolean locked) {
		if (!atl$locked && locked) {
			atl$lockListTag((ListTag) (Object) this);
		}
		atl$locked = locked;
	}

	@Unique
	private static void atl$lockListTag(ListTag listTag) {
		List<Tag> oldList = ObfuscationReflectionHelper.getPrivateValue(ListTag.class, listTag, FMLEnvironment.production ? "f_128716_" : "list");
		if (!oldList.getClass().getSimpleName().equals("ATLUnmodifiableList") && !oldList.getClass().getSimpleName().equals("ATLUnmodifiableRandomAccessList") ) {
			ObfuscationReflectionHelper.setPrivateValue(ListTag.class, listTag, ATLUnmodifiable.unmodifiableList(oldList), FMLEnvironment.production ? "f_128716_" : "list");
			for (var tag : oldList) {
				if (tag instanceof Lockable lockable){
					lockable.atl$setLocked(true);
				}
			}
		}
	}
}
