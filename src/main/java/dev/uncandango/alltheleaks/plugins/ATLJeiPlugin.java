package dev.uncandango.alltheleaks.plugins;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.Event;
import net.neoforged.neoforge.common.NeoForge;

@JeiPlugin
public class ATLJeiPlugin implements IModPlugin {
	@Override
	public ResourceLocation getPluginUid() {
		return ResourceLocation.parse("jei:alltheleaks");
	}

	@Override
	public void onRuntimeUnavailable() {
		NeoForge.EVENT_BUS.post(new RuntimeUnavailableEvent());
	}

	public static class RuntimeUnavailableEvent extends Event {

	}
}
