package dev.uncandango.alltheleaks.leaks.client.mods.minecolonies;

import com.minecolonies.api.MinecoloniesAPIProxy;
import dev.uncandango.alltheleaks.AllTheLeaks;
import dev.uncandango.alltheleaks.annotation.Issue;
import dev.uncandango.alltheleaks.utils.ReflectionHelper;
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent;
import net.neoforged.neoforge.common.NeoForge;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodType;
import java.lang.invoke.VarHandle;
import java.util.Map;

@Issue(modId = "minecolonies", versionRange = "[1.1.1149-1.21.1-snapshot,)", extraModDep = "jei", extraModDepVersions = "*", mixins = {"main.MineColoniesJEIPluginMixin"})
public class UntrackedIssue002 {
	public static final VarHandle eventHandlersPerType;
	public static final MethodHandle getEventBus;
	public static final Class<?> customRecipesReloadEventClass;

	public UntrackedIssue002() {
		var gameBus = NeoForge.EVENT_BUS;
		gameBus.addListener(this::clearCustomRecipeEvent);
	}

	static {
		var defaultEventBusClass = ReflectionHelper.getClass("com.minecolonies.api.eventbus.DefaultEventBus");
		eventHandlersPerType = ReflectionHelper.getFieldFromClass(defaultEventBusClass, "eventHandlersPerType", Map.class, false);
		var eventBusClass = ReflectionHelper.getClass("com.minecolonies.api.eventbus.EventBus");
		getEventBus = ReflectionHelper.getMethodFromClass(MinecoloniesAPIProxy.class, "getEventBus", MethodType.methodType(eventBusClass), false);
		customRecipesReloadEventClass = ReflectionHelper.getClass("com.minecolonies.api.eventbus.events.CustomRecipesReloadedEvent");
	}

	private void clearCustomRecipeEvent(ClientPlayerNetworkEvent.LoggingOut event) {
		if (event.getPlayer() != null) {
			try {
				var bus = getEventBus.invoke(MinecoloniesAPIProxy.getInstance());
				((Map<Class,Object>)eventHandlersPerType.get(bus)).remove(customRecipesReloadEventClass);
			} catch (Throwable t) {
				AllTheLeaks.LOGGER.error("Error while trying to get Minecolonies event bus", t);
			}

		}
	}
}
