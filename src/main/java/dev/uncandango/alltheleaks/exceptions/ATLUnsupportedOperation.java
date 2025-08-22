package dev.uncandango.alltheleaks.exceptions;

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
}
