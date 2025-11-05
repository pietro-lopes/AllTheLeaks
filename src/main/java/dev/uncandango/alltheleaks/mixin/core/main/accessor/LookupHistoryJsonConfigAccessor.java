package dev.uncandango.alltheleaks.mixin.core.main.accessor;

import com.mojang.serialization.MapCodec;
import mezz.jei.gui.bookmarks.IBookmark;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(targets = "mezz.jei.gui.config.LookupHistoryJsonConfig")
public interface LookupHistoryJsonConfigAccessor {
	@Accessor("BOOKMARK_CODEC")
	static void atl$setBookmarkCodec(MapCodec<IBookmark> bookmarkCodec){};
}
