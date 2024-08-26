package dev.uncandango.alltheleaks.mixin_extensions;

import java.util.Map;
import java.util.function.Consumer;

public interface EventKey {
	Map<String, Consumer<?>> atl$getKeyMap();
}
