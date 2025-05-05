package dev.uncandango.alltheleaks.mixinsq;

import com.bawnorton.mixinsquared.api.MixinCanceller;
import net.neoforged.fml.loading.LoadingModList;
import net.neoforged.neoforge.common.util.Lazy;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ATLMixinCanceller implements MixinCanceller {

	public static final Lazy<Set<String>> cancelList = Lazy.of(() -> {
		Set<String> list = new HashSet<>();
		if (isLoaded("additionalentityattributes")) {
			list.add("de.dafuqs.additionalentityattributes.mixin.common.BlockMixin");
		}
		return list;
	});

	private static boolean isLoaded(String modId){
		var modlist = LoadingModList.get();
		if (modlist == null) return false;
		return modlist.getModFileById(modId) != null;
	}

	@Override
	public boolean shouldCancel(List<String> targetClassNames, String mixinClassName) {
		return cancelList.get().contains(mixinClassName);
	}
}
