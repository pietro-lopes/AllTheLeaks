package dev.uncandango.alltheleaks.report;

import dev.uncandango.alltheleaks.AllTheLeaks;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class ReportManager {
	static int currentTick;
	static final Map<String, ReportTask> tasks = new HashMap<>();

	public static void tick(){
		currentTick++;
		tickAllTasks();
	}

	public static void registerTask(String name, int tickInterval, Runnable task){
		registerTask(name, tickInterval, task, () -> true);
	}

	public static void registerTask(String name, int tickInterval, Runnable task, Supplier<Boolean> condition){
		if (tickInterval <= 0) return;
		tasks.put(name, new ReportTask(name, tickInterval, task, condition));
	}

	public static boolean isRegistered(String name){
		return tasks.containsKey(name);
	}

	public static int getCurrentTick() {
		return currentTick;
	}

	private static void tickAllTasks() {
		for (var task : tasks.values()) {
			task.tick();
		}
	}

	public static void stop(String name) {
		tasks.get(name).stop();
	}

	static class ReportTask {
		private final int tickInterval;
		private final Runnable task;
		private final String name;
		private int lastTickReported;
		private boolean stopped;
		private final Supplier<Boolean> condition;

		public ReportTask(String name, int tickInterval, Runnable task, Supplier<Boolean> condition) {
			this.tickInterval = tickInterval;
			this.task = task;
			this.name = name;
			this.stopped = false;
			this.condition = condition;
		}

		public void stop(){
			this.stopped = true;
		}

		void tick(){
			if (this.shouldRun() && condition.get()) {
				try {
					this.task.run();
				} catch (Throwable e) {
					AllTheLeaks.LOGGER.error("Error while running task \"" + name + "\"", e);
				} finally {
					lastTickReported = ReportManager.getCurrentTick();
				}
			}
		}

		boolean shouldRun(){
			return !stopped && ReportManager.getCurrentTick() - lastTickReported > tickInterval;
		}
	}
}
