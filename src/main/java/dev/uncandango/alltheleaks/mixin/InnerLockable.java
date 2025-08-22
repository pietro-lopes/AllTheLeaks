package dev.uncandango.alltheleaks.mixin;

public interface InnerLockable extends Lockable {
	boolean atl$isInnerLocked();
	void atl$setInnerLocked(boolean locked);
	boolean atl$isLocked();
	void atl$setLocked(boolean locked);
}
