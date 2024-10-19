package dev.uncandango.alltheleaks.plugins;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.Event;

@JeiPlugin
public class ATLJeiPlugin implements IModPlugin {
	@Override
	public ResourceLocation getPluginUid() {
		return new ResourceLocation("jei:alltheleaks");
	}

	@Override
	public void onRuntimeUnavailable() {
		MinecraftForge.EVENT_BUS.post(new RuntimeUnavailableEvent());
	}

	public static class RuntimeUnavailableEvent extends Event {

	}
}
