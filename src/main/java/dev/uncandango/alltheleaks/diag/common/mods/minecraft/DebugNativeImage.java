package dev.uncandango.alltheleaks.diag.common.mods.minecraft;

import com.mojang.blaze3d.platform.NativeImage;
import dev.uncandango.alltheleaks.annotation.Issue;

import java.lang.ref.WeakReference;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Issue(modId = "minecraft", issueId = "Debug Native Images", versionRange = "1.21.1", mixins = {"main.NativeImageMixin"}, config = "debugNativeImage")
public class DebugNativeImage {
	public static final Map<Key, Set<Value>> NATIVE_IMAGES_TRACKER = new ConcurrentHashMap<>();

	public record Key(long pixels, boolean useStbFree) {
	}

	public record Value(WeakReference<NativeImage> imageRef, StackTraceElement[] stackTraceElements, String description) {
	}
}
