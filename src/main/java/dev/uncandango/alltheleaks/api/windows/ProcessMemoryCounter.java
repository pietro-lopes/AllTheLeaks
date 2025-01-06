package dev.uncandango.alltheleaks.api.windows;

import org.jetbrains.annotations.Nullable;
import org.lwjgl.BufferUtils;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.NativeResource;
import org.lwjgl.system.NativeType;
import org.lwjgl.system.Struct;

import java.nio.ByteBuffer;

import static org.lwjgl.system.MemoryUtil.memAddress;
import static org.lwjgl.system.MemoryUtil.memGetAddress;

/**
 * <h3>Layout</h3>
 *
 * <pre><code>
 * struct PROCESS_MEMORY_COUNTERS {
 *     dword {@link #cb};
 *     dword {@link #pageFaultCount};
 *     size_t {@link #peakWorkingSetSize};
 *     size_t {@link #workingSetSize};
 *     size_t {@link #quotaPeakPagedPoolUsage};
 *     size_t {@link #quotaPagedPoolUsage};
 *     size_t {@link #quotaPeakNonPagedPoolUsage};
 *     size_t {@link #quotaNonPagedPoolUsage};
 *     size_t {@link #pagefileUsage};
 *     size_t {@link #peakPagefileUsage};
 * }</code></pre>
 */
@NativeType("struct PROCESS_MEMORY_COUNTERS")
public class ProcessMemoryCounter extends Struct<ProcessMemoryCounter> implements NativeResource {
	/**
	 * The struct size in bytes.
	 */
	public static final int SIZEOF;

	/**
	 * The struct alignment in bytes.
	 */
	public static final int ALIGNOF;

	public static final int
		CB,
		PAGE_FAULT_COUNT,
		PEAK_WORKING_SET_SIZE,
		WORKING_SET_SIZE,
		QUOTA_PEAK_PAGED_POOL_USAGE,
		QUOTA_PAGED_POOL_USAGE,
		QUOTA_PEAK_NON_PAGED_POOL_USAGE,
		QUOTA_NON_PAGED_POOL_USAGE,
		PAGEFILE_USAGE,
		PEAK_PAGEFILE_USAGE;

	static {
		Layout layout = __struct(
			__member(4),
			__member(4),
			__member(POINTER_SIZE),
			__member(POINTER_SIZE),
			__member(POINTER_SIZE),
			__member(POINTER_SIZE),
			__member(POINTER_SIZE),
			__member(POINTER_SIZE),
			__member(POINTER_SIZE),
			__member(POINTER_SIZE)
		);

		SIZEOF = layout.getSize();
		ALIGNOF = layout.getAlignment();

		CB = layout.offsetof(0);
		PAGE_FAULT_COUNT = layout.offsetof(1);
		PEAK_WORKING_SET_SIZE = layout.offsetof(2);
		WORKING_SET_SIZE = layout.offsetof(3);
		QUOTA_PEAK_PAGED_POOL_USAGE = layout.offsetof(4);
		QUOTA_PAGED_POOL_USAGE = layout.offsetof(5);
		QUOTA_PEAK_NON_PAGED_POOL_USAGE = layout.offsetof(6);
		QUOTA_NON_PAGED_POOL_USAGE = layout.offsetof(7);
		PAGEFILE_USAGE = layout.offsetof(8);
		PEAK_PAGEFILE_USAGE = layout.offsetof(9);
	}

	protected ProcessMemoryCounter(long address, @Nullable ByteBuffer container) {
		super(address, container);
	}

	/**
	 * Creates a {@code ProcessMemoryCounter} instance at the current position of the specified {@link ByteBuffer} container. Changes to the buffer's content will be
	 * visible to the struct instance and vice versa.
	 *
	 * <p>The created instance holds a strong reference to the container object.</p>
	 */
	public ProcessMemoryCounter(ByteBuffer container) {
		super(memAddress(container), __checkContainer(container, SIZEOF));
	}

	@Override
	protected ProcessMemoryCounter create(long address, @Nullable ByteBuffer container) {
		return new ProcessMemoryCounter(address, container);
	}

	public static ProcessMemoryCounter create() {
		ByteBuffer container = BufferUtils.createByteBuffer(SIZEOF);
		return new ProcessMemoryCounter(memAddress(container), container);
	}

	public static ProcessMemoryCounter malloc(MemoryStack stack) {
		return new ProcessMemoryCounter(stack.nmalloc(ALIGNOF, SIZEOF), null);
	}

	@Override
	public int sizeof() {
		return SIZEOF;
	}

	public int cb() {
		return ncb(address());
	}

	public static int ncb(long struct) {
		return UNSAFE.getInt(null, struct + ProcessMemoryCounter.CB);
	}

	public int pageFaultCount() {
		return npageFaultCount(address());
	}

	public static int npageFaultCount(long struct) {
		return UNSAFE.getInt(null, struct + ProcessMemoryCounter.PAGE_FAULT_COUNT);
	}

	public long peakWorkingSetSize() {
		return npeakWorkingSetSize(address());
	}

	public static long npeakWorkingSetSize(long struct) {
		return memGetAddress(struct + ProcessMemoryCounter.PEAK_WORKING_SET_SIZE);
	}

	public long workingSetSize() {
		return nworkingSetSize(address());
	}

	public static long nworkingSetSize(long struct) {
		return memGetAddress(struct + ProcessMemoryCounter.WORKING_SET_SIZE);
	}

	public long quotaPeakPagedPoolUsage() {
		return nquotaPeakPagedPoolUsage(address());
	}

	public static long nquotaPeakPagedPoolUsage(long struct) {
		return memGetAddress(struct + ProcessMemoryCounter.QUOTA_PEAK_PAGED_POOL_USAGE);
	}

	public long quotaPagedPoolUsage() {
		return nquotaPagedPoolUsage(address());
	}

	public static long nquotaPagedPoolUsage(long struct) {
		return memGetAddress(struct + ProcessMemoryCounter.QUOTA_PAGED_POOL_USAGE);
	}

	public long quotaPeakNonPagedPoolUsage() {
		return nquotaPeakNonPagedPoolUsage(address());
	}

	public static long nquotaPeakNonPagedPoolUsage(long struct) {
		return memGetAddress(struct + ProcessMemoryCounter.QUOTA_PEAK_NON_PAGED_POOL_USAGE);
	}

	public long quotaNonPagedPoolUsage() {
		return nquotaNonPagedPoolUsage(address());
	}

	public static long nquotaNonPagedPoolUsage(long struct) {
		return memGetAddress(struct + ProcessMemoryCounter.QUOTA_NON_PAGED_POOL_USAGE);
	}

	public long pagefileUsage() {
		return npagefileUsage(address());
	}

	public static long npagefileUsage(long struct) {
		return memGetAddress(struct + ProcessMemoryCounter.PAGEFILE_USAGE);
	}

	public long peakPagefileUsage() {
		return npeakPagefileUsage(address());
	}

	public static long npeakPagefileUsage(long struct) {
		return memGetAddress(struct + ProcessMemoryCounter.PEAK_PAGEFILE_USAGE);
	}
}
