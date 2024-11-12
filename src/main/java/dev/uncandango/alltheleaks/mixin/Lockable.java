package dev.uncandango.alltheleaks.mixin;

public interface Lockable {
	boolean isLocked();
	void setLocked(boolean locked);
}
