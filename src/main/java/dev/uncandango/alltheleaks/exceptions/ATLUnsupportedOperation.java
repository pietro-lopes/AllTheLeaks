package dev.uncandango.alltheleaks.exceptions;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

import java.util.concurrent.atomic.AtomicInteger;

public class ATLUnsupportedOperation extends UnsupportedOperationException {
	private static final AtomicInteger errorCount = new AtomicInteger();
	private static final AtomicInteger lastReported = new AtomicInteger();
	private static final AtomicInteger lastTickReported = new AtomicInteger();

	private ATLUnsupportedOperation(){
	}

	public ATLUnsupportedOperation(String message) {
		super(message);
		errorCount.incrementAndGet();
	}

	public static int getErrorCount(){
		return errorCount.get();
	}

	public static int getUnreportedErrorCount(){
		var count = getErrorCount();
		if (count > lastReported.get()) {
			lastReported.set(count);
			return count;
		}
		return 0;
	}
//
//	public static MutableComponent reportErrors(int tickCount) {
//		var current = errorCount.get();
//		if (current > lastReported.get()) {
//			if (tickCount - lastTickReported.get() > 300) { // 300 ticks, 15s
//				lastReported.set(current);
//				lastTickReported.set(tickCount);
//				return Component.translatable("[AllTheLeaks] There are %s errors related to Ingredient Dedupe, check logs for more info and report to developer.", current).withStyle(ChatFormatting.RED);
//			}
//		}
//		return Component.empty();
//	}
}
