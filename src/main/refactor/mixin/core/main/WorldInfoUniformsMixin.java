package dev.uncandango.alltheleaks.mixin.core.main;

import net.irisshaders.iris.gl.uniform.UniformHolder;
import net.irisshaders.iris.gl.uniform.UniformUpdateFrequency;
import net.irisshaders.iris.uniforms.IrisExclusiveUniforms;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Pseudo;

import java.lang.ref.WeakReference;

@Pseudo
@Mixin(value = IrisExclusiveUniforms.WorldInfoUniforms.class, remap = false)
public class WorldInfoUniformsMixin {
    /**
     * @author Uncandango
     * @reason =)
     */
    @SuppressWarnings("DataFlowIssue")
    @Overwrite
    public static void addWorldInfoUniforms(UniformHolder uniforms) {
        WeakReference<ClientLevel> level = new WeakReference<>(Minecraft.getInstance().level);
        // TODO: Use level.dimensionType() coordinates for 1.18!
        uniforms.uniform1i(UniformUpdateFrequency.PER_FRAME, "bedrockLevel", () -> {
            if (level.get() != null) {
                return level.get().dimensionType().minY();
            } else {
                return 0;
            }
        });
        uniforms.uniform1f(UniformUpdateFrequency.PER_FRAME, "cloudHeight", () -> {
            if (level.get() != null) {
                return level.get().effects().getCloudHeight();
            } else {
                return 192.0;
            }
        });

        uniforms.uniform1i(UniformUpdateFrequency.PER_FRAME, "heightLimit", () -> {
            if (level.get() != null) {
                return level.get().dimensionType().height();
            } else {
                return 256;
            }
        });
        uniforms.uniform1i(UniformUpdateFrequency.PER_FRAME, "logicalHeightLimit", () -> {
            if (level.get() != null) {
                return level.get().dimensionType().logicalHeight();
            } else {
                return 256;
            }
        });
        uniforms.uniform1b(UniformUpdateFrequency.PER_FRAME, "hasCeiling", () -> {
            if (level.get() != null) {
                return level.get().dimensionType().hasCeiling();
            } else {
                return false;
            }
        });
        uniforms.uniform1b(UniformUpdateFrequency.PER_FRAME, "hasSkylight", () -> {
            if (level.get() != null) {
                return level.get().dimensionType().hasSkyLight();
            } else {
                return true;
            }
        });
        uniforms.uniform1f(UniformUpdateFrequency.PER_FRAME, "ambientLight", () -> {
            if (level.get() != null) {
                return level.get().dimensionType().ambientLight();
            } else {
                return 0f;
            }
        });
    }
}
