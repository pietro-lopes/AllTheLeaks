package dev.uncandango.alltheleaks.leaks.client.mods.mna;

import com.mna.items.armor.FeyArmorItem;
import dev.uncandango.alltheleaks.annotation.Issue;

@Issue(modId = "mna", versionRange = "[3.1.0,)", mixins = {"main.FeyArmorRendererMixin", "main.MAGeckoRendererMixin"}, description = "Adds post render cleanup to `FeyArmorRenderer` and `MAGeckoRenderer`")
public class UntrackedIssue001 {

	static {
		var dummy = FeyArmorItem.renderEntity;
	}
}
