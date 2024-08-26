package dev.uncandango.alltheleaks.utils;

import com.google.common.collect.Maps;
import dev.uncandango.alltheleaks.AllTheLeaks;
import org.jetbrains.annotations.Nullable;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.invoke.VarHandle;
import java.lang.reflect.Constructor;
import java.lang.reflect.RecordComponent;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Stream;

public class ReflectionHelper {
	public static final MethodHandles.Lookup LOOKUP = MethodHandles.lookup();
	public static final Map<Class<?>, MethodHandles.Lookup> PRIVATE_LOOKUP = Maps.newHashMap();

	public static VarHandle getFieldFromClass(Class<?> clazz, String fieldName, Class<?> fieldClass, boolean isStatic) {
		VarHandle handler;
		var lookup = safeLookup(clazz);
		handler = safeVarHandler(lookup, clazz, fieldName, fieldClass, isStatic);
		if (handler == null) {
			throw new RuntimeException("VarHandler is null");
		}
		return handler;
	}

	public static MethodHandle getMethodFromClass(Class<?> clazz, String methodName, MethodType methodType, boolean isStatic) {
		MethodHandle handler;
		var lookup = safeLookup(clazz);
		handler = safeMethodHandler(lookup, clazz, methodName, methodType, isStatic);
		if (handler == null) {
			throw new RuntimeException("MethodHandler is null");
		}
		return handler;
	}

	public static Constructor<?> getRecordCtor(Class<?> clazz) {
		var componentTypes = Stream.of(clazz.getRecordComponents()).map(RecordComponent::getType).toList();
		for (var ctor : clazz.getDeclaredConstructors()) {
			if (Arrays.asList(ctor.getParameterTypes()).equals(componentTypes)) {
				return ctor;
			}
		}
		throw new RuntimeException("Record constructor for class " + clazz + " not found!");
	}

	@Nullable
	public static MethodHandles.Lookup safeLookup(Class<?> clazz) {
		MethodHandles.Lookup lookup = PRIVATE_LOOKUP.getOrDefault(clazz, null);
		if (lookup != null) {
			return lookup;
		}
		try {
			lookup = MethodHandles.privateLookupIn(clazz, LOOKUP);
		} catch (IllegalAccessException e) {
			AllTheLeaks.LOGGER.warn("Failed to get lookup for class {}", clazz);
		}
		PRIVATE_LOOKUP.put(clazz, lookup);
		return lookup;
	}

	@Nullable
	private static VarHandle safeVarHandler(MethodHandles.Lookup lookup, Class<?> clazz, String fieldName, Class<?> fieldClass, boolean isStatic) {
		VarHandle handler = null;
		if (lookup == null) {
			return null;
		}
		try {
			handler = isStatic ? lookup.findStaticVarHandle(clazz, fieldName, fieldClass) : lookup.findVarHandle(clazz, fieldName, fieldClass);
		} catch (Exception e) {
			AllTheLeaks.LOGGER.warn("Failed to get VarHandle for class {} with field {} and type {}", clazz, fieldName, fieldClass);
		}
		return handler;
	}

	@Nullable
	private static MethodHandle safeMethodHandler(MethodHandles.Lookup lookup, Class<?> clazz, String methodName, MethodType methodType, boolean isStatic) {
		MethodHandle handler = null;
		if (lookup == null) {
			return null;
		}
		try {
			handler = isStatic ? lookup.findStatic(clazz, methodName, methodType) : lookup.findVirtual(clazz, methodName, methodType);
		} catch (Exception e) {
			AllTheLeaks.LOGGER.warn("Failed to get MethodHandle for class {} with method {} and type {}", clazz, methodName, methodType);
		}
		return handler;
	}

	public static Class<?> getPrivateClass(Class<?> parent, String subclass) {
		for (var clazz : parent.getNestMembers()) {
			if (clazz.getName().equals(subclass)) {
				return clazz;
			}
		}
		throw new RuntimeException("Sub class not found!");
	}

	public static Object getFieldValue(VarHandle handle) {
		return handle.get();
	}

	public static Class<?> getClass(String className) {
		try {
			return Class.forName(className);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
	}
}
