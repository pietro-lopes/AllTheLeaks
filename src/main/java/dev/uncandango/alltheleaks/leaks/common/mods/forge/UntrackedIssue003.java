package dev.uncandango.alltheleaks.leaks.common.mods.forge;

import com.google.common.base.Stopwatch;
import dev.uncandango.alltheleaks.AllTheLeaks;
import dev.uncandango.alltheleaks.annotation.Issue;
import dev.uncandango.alltheleaks.utils.ReflectionHelper;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.server.ServerStoppedEvent;
import net.minecraftforge.eventbus.ListenerList;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventListener;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodType;
import java.lang.invoke.VarHandle;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Issue(modId = "forge", versionRange = "[47.2,)")
public class UntrackedIssue003 {
	public static final MethodHandle GET_LISTENERS;
	public static final VarHandle ALL_LISTS;
	public static final VarHandle LISTS;
	public static final Class LISTENER_LIST_INST_CLASS;

	static {
		ALL_LISTS = ReflectionHelper.getFieldFromClass(ListenerList.class, "allLists", List.class, true);
		LISTENER_LIST_INST_CLASS = ReflectionHelper.getPrivateClass(ListenerList.class, "net.minecraftforge.eventbus.ListenerList$ListenerListInst");
		GET_LISTENERS = ReflectionHelper.getMethodFromClass(LISTENER_LIST_INST_CLASS, "getListeners", MethodType.methodType(IEventListener.class.arrayType()), false);
		LISTS = ReflectionHelper.getFieldFromClass(ListenerList.class, "lists", LISTENER_LIST_INST_CLASS.arrayType(), false);
	}

	public UntrackedIssue003() {
		var gameBus = MinecraftForge.EVENT_BUS;
		gameBus.addListener(EventPriority.LOWEST, this::rebuildListenersCache);
	}

	private void rebuildListenersCache(ServerStoppedEvent event) {
		try {
			var watch = Stopwatch.createStarted();
			var listeners = (List<ListenerList>) ALL_LISTS.get();
			for (var listener : listeners) {
				var list = (Object[]) LISTS.get(listener);
				for (var ev : list) {
					GET_LISTENERS.invoke(ev);
				}
			}
			AllTheLeaks.LOGGER.debug("Rebuild listeners cache took {}ms", watch.stop().elapsed(TimeUnit.MILLISECONDS));
		} catch (Throwable e) {
			AllTheLeaks.LOGGER.error("Error while trying to rebuild listeners cache", e);
		}
	}
}
