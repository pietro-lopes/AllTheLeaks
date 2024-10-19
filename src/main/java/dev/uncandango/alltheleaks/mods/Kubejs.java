package dev.uncandango.alltheleaks.mods;

import com.google.common.collect.Interners;
import dev.latvian.mods.kubejs.BuiltinKubeJSPlugin;
import dev.latvian.mods.kubejs.server.ServerScriptManager;
import dev.latvian.mods.rhino.Context;
import dev.uncandango.alltheleaks.mixin.core.accessor.RegexIDFilterAccessor;
import dev.uncandango.alltheleaks.utils.ReflectionHelper;
import net.minecraftforge.fml.ModList;

import java.util.Iterator;
import java.util.Map;

public interface Kubejs {
    static void run() {
        RegexIDFilterAccessor.atl$setInterner(Interners.newStrongInterner());
        if (ModList.get().isLoaded("jei")) {
            // JEI
            BuiltinKubeJSPlugin.GLOBAL.put("jeiRuntime", null);
        }
        ServerScriptManager.instance.unload();
        ServerScriptManager.instance = new ServerScriptManager(null);
        for (Iterator<Map.Entry<String, Object>> iter = BuiltinKubeJSPlugin.GLOBAL.entrySet().iterator(); iter.hasNext(); ) {
            var it = iter.next();
            if (it.getValue() == null) continue;
            var contextValues = ReflectionHelper.getValueFromFieldWithClass(it.getValue(), it.getValue().getClass(), Context.class);
            for (var context : contextValues) {
                if (context.getClassShutter() instanceof ServerScriptManager) iter.remove();
            }
        }
    }
}
