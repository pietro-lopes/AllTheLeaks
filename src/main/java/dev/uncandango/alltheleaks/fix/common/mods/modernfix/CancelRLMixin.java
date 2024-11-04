package dev.uncandango.alltheleaks.fix.common.mods.modernfix;

import dev.uncandango.alltheleaks.annotation.Issue;

// We cancel mfix RL dedupe because it does not includes all RL cases
@Issue(modId = "modernfix", versionRange = "[5.0.0,)", mixinsToCancel = {"org.embeddedt.modernfix.common.mixin.perf.deduplicate_location.MixinResourceLocation"}, description = "Replace modernfix RL deduplicate feature if needed")
public class CancelRLMixin {
}
