package dev.uncandango.alltheleaks.api.windows;

import org.lwjgl.system.APIUtil;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.NativeType;
import org.lwjgl.system.SharedLibrary;
import org.lwjgl.system.windows.Kernel32;

import static org.lwjgl.system.APIUtil.apiGetFunctionAddress;
import static org.lwjgl.system.Checks.CHECKS;
import static org.lwjgl.system.Checks.check;
import static org.lwjgl.system.JNI.*;

public class PsApi {
	private static final SharedLibrary PSAPI = APIUtil.apiCreateLibrary("psapi");
	public static final class Functions {

		private Functions() {
		}

		public static final long
			EmptyWorkingSet = apiGetFunctionAddress(PSAPI, "EmptyWorkingSet"),
			GetProcessMemoryInfo = apiGetFunctionAddress(PSAPI, "GetProcessMemoryInfo");
	}
	public static SharedLibrary getLibrary() {
		return PSAPI;
	}

	@NativeType("BOOL")
	private static boolean nEmptyWorkingSet(@NativeType("HANDLE") long hProcess) {
		long __functionAddress = Functions.EmptyWorkingSet;
		if (CHECKS) {
			check(__functionAddress);
			check(hProcess);
		}
		return callPI(hProcess, __functionAddress) != 0;
	}

	public static boolean EmptyWorkingSetOfCurrentProcess() {
		return nEmptyWorkingSet(Kernel32.GetCurrentProcess());
	}

	@NativeType("BOOL")
	public static boolean GetProcessMemoryInfo(@NativeType("HANDLE") long Process,
											@NativeType("PROCESS_MEMORY_COUNTERS *") long ppsmemCounters,
											@NativeType("DWORD") int cb) {
		long __functionAddress = Functions.GetProcessMemoryInfo;
		if (CHECKS) {
			check(__functionAddress);
			check(Process);
			check(ppsmemCounters);
		}
		return callPPPI(Process, ppsmemCounters, cb, __functionAddress) != 0;
	}
}
