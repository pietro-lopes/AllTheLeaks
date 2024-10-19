package dev.uncandango.alltheleaks.mods;

import com.google.common.collect.Streams;
import dev.uncandango.alltheleaks.AllTheLeaks;
import org.violetmoon.zeta.advancement.ManualTrigger;
import org.violetmoon.zeta.mod.ZetaMod;

import java.util.Arrays;
import java.util.function.Function;

public interface Quark {
    static void run() {
        org.violetmoon.quark.base.Quark.ZETA.configManager.setJeiReloadListener(Function.identity()::apply);
        Streams.concat(ZetaMod.ZETA.modules.getModules().stream(), org.violetmoon.quark.base.Quark.ZETA.modules.getModules().stream())
                .forEach(module -> Streams.concat(Arrays.stream(module.getClass().getDeclaredFields()), Arrays.stream(module.getClass().getSuperclass().getDeclaredFields()))
                        .filter(f -> ManualTrigger.class.isAssignableFrom(f.getType()))
                        .forEach(field -> {
                            try {
                                field.set(null, null);
                            } catch (IllegalAccessException e) {
                                throw new RuntimeException(e);
                            }
                            AllTheLeaks.LOGGER.info("Cleaning Zeta module: {}", module.displayName);
                        }));
    }
}
