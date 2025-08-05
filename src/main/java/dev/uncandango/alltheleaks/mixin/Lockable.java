package dev.uncandango.alltheleaks.mixin;

public interface Lockable {
	boolean atl$isLocked();
	void atl$setLocked(boolean locked);
}