package dev.uncandango.alltheleaks.mixin.core.accessor;

import com.minecolonies.core.compatibility.jei.JEIPlugin;
import com.minecolonies.core.compatibility.jei.JobBasedRecipeCategory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;

@Pseudo
@Mixin(JEIPlugin.class)
public interface MineColoniesJEIPluginAccessor {
	@Accessor("categories")
	List<JobBasedRecipeCategory<?>> getCategories();
}
