package dev.uncandango.alltheleaks.mixin.core.main;

import dev.uncandango.alltheleaks.feature.common.mods.minecraft.ResourceLocationDedupe;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Coerce;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ResourceLocation.class)
public class ResourceLocationMixin {
	@Shadow
	@Mutable
	@Final
	private String namespace;

	@Shadow
	@Mutable
	@Final
	private String path;

	@Inject(method = "<init>(Ljava/lang/String;Ljava/lang/String;Lnet/minecraft/resources/ResourceLocation$Dummy;)V", at = @At("RETURN"))
	private void atl$internStrings(String namespace, String path, @Coerce Object dummy, CallbackInfo ci) {
		var start = System.nanoTime();

		this.path = ResourceLocationDedupe.internPath(path);
		this.namespace = ResourceLocationDedupe.internNamespace(namespace);

//		this.path = path.intern();
//		this.namespace = namespace.intern();

		var end = System.nanoTime();
		ResourceLocationDedupe.TIMER.addAndGet(end - start);
	}
}
