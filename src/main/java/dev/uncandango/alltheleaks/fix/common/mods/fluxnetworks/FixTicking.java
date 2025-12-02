package dev.uncandango.alltheleaks.fix.common.mods.fluxnetworks;

import dev.uncandango.alltheleaks.annotation.Issue;

@Issue(modId = "fluxnetworks", versionRange = "8.0.0", mixins = "main.ServerFluxNetworkMixin", config = "skipTickingUnloadedFluxNetworks")
public class FixTicking {
}
