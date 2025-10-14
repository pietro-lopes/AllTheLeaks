package dev.uncandango.alltheleaks.leaks.common.mods.forge;

import com.google.common.base.Stopwatch;
import cpw.mods.jarhandling.SecureJar;
import cpw.mods.jarhandling.impl.Jar;
import dev.uncandango.alltheleaks.AllTheLeaks;
import dev.uncandango.alltheleaks.annotation.Issue;
import dev.uncandango.alltheleaks.utils.ReflectionHelper;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.server.ServerStoppedEvent;
import net.minecraftforge.eventbus.ListenerList;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventListener;
import org.apache.maven.artifact.versioning.DefaultArtifactVersion;
import org.apache.maven.artifact.versioning.InvalidVersionSpecificationException;
import org.apache.maven.artifact.versioning.VersionRange;

import java.io.IOException;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodType;
import java.lang.invoke.VarHandle;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Issue(modId = "forge", issueId = "eventbus-#39", versionRange = "[47.2,)",
	description = "Regenerate listeners cache on server stopped")
public class Issue39 {
	public static final MethodHandle GET_LISTENERS;
	public static final VarHandle ALL_LISTS;
	public static final VarHandle LISTS;
	public static final Class<?> LISTENER_LIST_INST_CLASS;

	static {
		ALL_LISTS = ReflectionHelper.getFieldFromClass(ListenerList.class, "allLists", List.class, true);
		LISTENER_LIST_INST_CLASS = ReflectionHelper.getPrivateClass(ListenerList.class, "net.minecraftforge.eventbus.ListenerList$ListenerListInst");
		GET_LISTENERS = ReflectionHelper.getMethodFromClass(LISTENER_LIST_INST_CLASS, "getListeners", MethodType.methodType(IEventListener.class.arrayType()), false);
		LISTS = ReflectionHelper.getFieldFromClass(ListenerList.class, "lists", LISTENER_LIST_INST_CLASS.arrayType(), false);
	}

	public Issue39() {
		var gameBus = MinecraftForge.EVENT_BUS;
		try {
			var eventBusJar = ((Jar) SecureJar.from(Path.of(gameBus.getClass().getProtectionDomain().getCodeSource().getLocation().toURI())));
			var busVersion = eventBusJar.getManifest().getAttributes("net/minecraftforge/eventbus/service/").getValue("Implementation-Version");
			eventBusJar.getRootPath().getFileSystem().close();
			var version = new DefaultArtifactVersion(busVersion);
			var range = VersionRange.createFromVersionSpec("[6.2.26,)");
			if (range.containsVersion(version)) {
				AllTheLeaks.LOGGER.debug("Skipping memory leak fix on Event Bus: version range {} contains version {}", range, version);
				return;
			}
		} catch (URISyntaxException | IOException | InvalidVersionSpecificationException e) {
			AllTheLeaks.LOGGER.error("Error while querying version of event bus", e);
			return;
		}
		gameBus.addListener(EventPriority.LOWEST, this::rebuildListenersCache);
	}

	private void rebuildListenersCache(ServerStoppedEvent event) {
		try {
			var watch = Stopwatch.createStarted();
			@SuppressWarnings("unchecked") var listeners = (List<ListenerList>) ALL_LISTS.get();
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
