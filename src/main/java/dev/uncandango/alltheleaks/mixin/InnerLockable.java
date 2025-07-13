package dev.uncandango.alltheleaks.mixin;

public interface InnerLockable extends Lockable {
	boolean isInnerLocked();
	void setInnerLocked(boolean locked);
	boolean isLocked();
	void setLocked(boolean locked);
}
