package dev.uncandango.alltheleaks.utils;

import com.google.common.collect.Maps;
import dev.uncandango.alltheleaks.AllTheLeaks;
import org.jetbrains.annotations.Nullable;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.FrameNode;
import org.objectweb.asm.tree.IntInsnNode;
import org.objectweb.asm.tree.InvokeDynamicInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.LookupSwitchInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.TableSwitchInsnNode;
import org.objectweb.asm.tree.TypeInsnNode;

import java.io.IOException;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.invoke.VarHandle;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ReflectionHelper {
	public static final MethodHandles.Lookup LOOKUP = MethodHandles.lookup();
	public static final Map<Class<?>, MethodHandles.Lookup> PRIVATE_LOOKUP = Maps.newHashMap();

	static boolean setField(Object obj, Class<?> clazz, String fieldName, Object arg) {
        try {
            var field = clazz.getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(obj, arg);
            return true;
        } catch (Exception e) {
            AllTheLeaks.LOGGER.warn("Failed to set value {} to object {} from method from class {}.", arg, obj, clazz);
        }
        return false;
    }

    public static <T> List<T> getValueFromFieldWithClass(Object obj, Class<?> clazz, Class<T> fieldClazz) {
        List<T> result = new ArrayList<>();
        Arrays.stream(clazz.getDeclaredFields()).filter(f -> fieldClazz.isAssignableFrom(f.getType())).forEach(targetField -> {
            try {
                targetField.setAccessible(true);
                result.add(fieldClazz.cast(targetField.get(obj)));
            } catch (Exception e) {
                AllTheLeaks.LOGGER.warn("Failed to get value from field {} with class {} from object {} with class {}.", targetField, fieldClazz, obj, clazz);
            }
        });
        return result;
    }

    static List<Field> getFieldsFromFieldWithClass(Class<?> clazz, Class<?> ...fieldClazz) {
        List<Field> result = new ArrayList<>();
        Arrays.stream(clazz.getDeclaredFields()).filter(f -> Arrays.stream(fieldClazz).anyMatch(f1 -> f1.isAssignableFrom(f.getType()))).forEach(result::add);
        return result;
    }

    @Nullable
    static VarHandle getVarHandler(MethodHandles.Lookup lookup, Class<?> objClass, String name, Class<?> fieldClass){
        VarHandle handler = null;
        try {
           handler = lookup.findVarHandle(objClass, name, fieldClass);
        } catch (Exception e){
            AllTheLeaks.LOGGER.info("Failed to get varhandler.", e);
        }
        return handler;
    }

    static boolean setFieldWithCheck(Object obj, Class<?> clazz, String fieldName, Object arg) {
        if (clazz.isAssignableFrom(obj.getClass())) {
            return setField(obj, clazz, fieldName, arg);
        } else AllTheLeaks.LOGGER.debug("Object: {} is not of class {}", obj, clazz);
        return false;
    }

    static void setFields(Object obj, Class<?> clazz, Collection<String> fieldNames, Object[] args) {
        if (fieldNames.size() == args.length) {
            var it = fieldNames.iterator();
            for (int i = 0; it.hasNext(); i++) {
                setField(obj, clazz, it.next(), args[i]);
            }
        } else {
            AllTheLeaks.LOGGER.warn("Failed to set many fields on reflection, number of arrays does not match.");
        }
    }

    static void setFieldsToNull(Object obj, Class<?> clazz, Collection<String> fieldNames) {
        setFields(obj, clazz, fieldNames, new Object[fieldNames.size()]);
    }

    static void nukeObject(Object obj, Class<?> clazz) {
        var fields = Arrays.stream(clazz.getDeclaredFields()).map(Field::getName).toList();
        setFieldsToNull(obj, clazz, fields);
    }

	public static <T> T cast(final Object o) {
		if (o == null) {
			return null;
		}
		@SuppressWarnings("unchecked")
		final T t = (T) o;
		return t;
	}

	public static Class<?> getClass(String className) {
		try {
			return Class.forName(className);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
	}

	public static VarHandle getFieldFromClass(Class<?> clazz, String fieldName, Class<?> fieldClass, boolean isStatic) {
		VarHandle handler;
		var lookup = safeLookup(clazz);
		handler = safeVarHandler(lookup, clazz, fieldName, fieldClass, isStatic);
		if (handler == null) {
			throw new RuntimeException("VarHandler is null");
		}
		return handler;
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

	public static Class<?> getPrivateClass(Class<?> parent, String subclass) {
		for (var clazz : parent.getNestMembers()) {
			if (clazz.getName().equals(subclass)) {
				return clazz;
			}
		}
		throw new RuntimeException("Sub class not found!");
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

	public static MethodHandle getMethodFromClass(Class<?> clazz, String methodName, MethodType methodType, boolean isStatic) {
		MethodHandle handler;
		var lookup = safeLookup(clazz);
		handler = safeMethodHandler(lookup, clazz, methodName, methodType, isStatic);
		if (handler == null) {
			throw new RuntimeException("MethodHandler is null");
		}
		return handler;
	}

	public static void dumpClass(ClassNode classNode, String className){
		ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS);

		try {
			classNode.accept(cw);
		} catch (Throwable e) {
			AllTheLeaks.LOGGER.error(e.getMessage(), e);
		}
		byte[] clazz = cw.toByteArray();

		try {
			final Path tempFile = Files.createTempDirectory("classDump").resolve(className.replaceAll("/", ".") + ".class");
			Files.write(tempFile, clazz);
			AllTheLeaks.LOGGER.debug("Wrote {} byte class file {} to {}", clazz.length, className, tempFile);
		} catch (IOException e) {
			AllTheLeaks.LOGGER.error("Failed to write class file {}", className, e);
		}
	}

}
