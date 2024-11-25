package dev.uncandango.alltheleaks.leaks.client.mods.badpackets;

import dev.uncandango.alltheleaks.annotation.Issue;
import dev.uncandango.alltheleaks.mixin.core.accessor.ConnectionAccessor;
import dev.uncandango.alltheleaks.mixin.core.accessor.FakePlayerNetHandlerAccessor;
import lol.bai.badpackets.impl.handler.ClientPacketHandler;
import lol.bai.badpackets.impl.handler.ServerPacketHandler;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.server.ServerStoppedEvent;
import net.minecraftforge.eventbus.api.EventPriority;

@Issue(modId = "badpackets", versionRange = "[0.4.1,)", description = "Clears `ChannelRegistry#handlers` map on packet close", mixins = "main.ClientPacketListenerMixin")
public class UntrackedIssue001 {
}
