package dev.uncandango.alltheleaks.mixin.core.main;

import me.lucko.spark.common.SparkPlugin;
import me.lucko.spark.common.sampler.source.ClassSourceLookup;
import me.lucko.spark.neoforge.NeoForgeClassSourceLookup;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(targets = "org.embeddedt.modernfix.spark.SparkLaunchProfiler$ModernFixSparkPlugin")
public abstract class ModernFixSparkPluginMixin implements SparkPlugin {
	@Override
	public ClassSourceLookup createClassSourceLookup() {
		return new NeoForgeClassSourceLookup();
	}
}
