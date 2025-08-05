package dev.uncandango.alltheleaks.report;

import dev.uncandango.alltheleaks.AllTheLeaks;

import java.util.ArrayList;
import java.util.List;

public class ReportManager {
	static int currentTick;
	static final List<ReportTask> tasks = new ArrayList<>();

	public static void tick(){
		currentTick++;
		tickAllTasks();
	}

	public static boolean registerTask(String name, int tickInternal, Runnable task){
		return tasks.add(new ReportTask(name, tickInternal, task));
	}

	static int getCurrentTick() {
		return currentTick;
	}

	private static void tickAllTasks() {
		for (var task : tasks) {
			task.tick();
		}
	}

	static class ReportTask {
		private final int tickInterval;
		private final Runnable task;
		private final String name;
		private int lastTickReported;

		public ReportTask(String name, int tickInterval, Runnable task) {
			this.tickInterval = tickInterval;
			this.task = task;
			this.name = name;
		}

		void tick(){
			if (this.shouldRun()) {
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
			return ReportManager.getCurrentTick() - lastTickReported > tickInterval;
		}
	}
}
