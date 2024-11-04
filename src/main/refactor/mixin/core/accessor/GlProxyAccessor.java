package dev.uncandango.alltheleaks.mixin.core.accessor;

import com.seibel.distanthorizons.core.render.glObject.GLProxy;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.concurrent.ConcurrentLinkedQueue;

@Pseudo
@Mixin(value = GLProxy.class, remap = false)
public interface GlProxyAccessor {
    @Accessor("renderThreadRunnableQueue")
    ConcurrentLinkedQueue<Runnable> atl$getRenderThreadRunnableQueue();
}
