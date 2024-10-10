package dev.uncandango.alltheleaks.mixin.core.accessor;

import com.mojang.serialization.MapCodec;
import mezz.jei.gui.bookmarks.IBookmark;
import mezz.jei.gui.config.BookmarkJsonConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(BookmarkJsonConfig.class)
public interface BookmarkJsonConfigAccessor {
	@Accessor("BOOKMARK_CODEC")
	static void atl$setBookmarkCodec(MapCodec<IBookmark> bookmarkCodec){};
}
