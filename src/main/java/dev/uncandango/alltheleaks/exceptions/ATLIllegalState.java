package dev.uncandango.alltheleaks.exceptions;

import java.util.concurrent.atomic.AtomicInteger;

public class ATLIllegalState extends IllegalStateException {
	private ATLIllegalState(){
	}

	public ATLIllegalState(ATLIllegalState.TYPE type, String message) {
		super(message);
		type.increment();
	}

	public enum TYPE {
		NO_REMOVAL_REASON(new AtomicInteger());

		private final AtomicInteger errorCount;

		TYPE(AtomicInteger errorCount) {
			this.errorCount = errorCount;
		}

		public int getErrorCount(){
			return this.errorCount.get();
		}

		int increment(){
			return this.errorCount.incrementAndGet();
		}
	}
}
