package dev.uncandango.alltheleaks.mixin.core.main;

import com.legacy.structure_gel.core.SGConfig;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraftforge.common.ForgeConfigSpec;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value = SGConfig.Client.class, remap = false)
public abstract class SGConfigClientMixin {

	@WrapMethod(method = "showStructureBlockInfo")
	private boolean wrapShowStructureBlockInfo(Operation<Boolean> original) {
		if (SGConfigAccessor.getCLIENT_SPEC().isLoaded()) {
			return original.call();
		} else {
			return false;
		}
	}

	@Mixin(value = SGConfig.class, remap = false)
	public interface SGConfigAccessor {
		@Accessor("CLIENT_SPEC")
		static ForgeConfigSpec getCLIENT_SPEC() {
			throw new AssertionError();
		}
	}
}
