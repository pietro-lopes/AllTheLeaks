package dev.uncandango.alltheleaks.mixinsq;

import com.bawnorton.mixinsquared.api.MixinCanceller;
import dev.uncandango.alltheleaks.leaks.IssueManager;

import java.util.List;

public class ATLMixinCanceller implements MixinCanceller {
	@Override
	public boolean shouldCancel(List<String> targetClassNames, String mixinClassName) {
		return IssueManager.getMixinToCancel().contains(mixinClassName);
	}
}
