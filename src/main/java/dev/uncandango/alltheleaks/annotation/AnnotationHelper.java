package dev.uncandango.alltheleaks.annotation;

import com.google.common.collect.Maps;
import dev.uncandango.alltheleaks.AllTheLeaks;
import dev.uncandango.alltheleaks.utils.ReflectionHelper;
import net.neoforged.neoforgespi.language.ModFileScanData;
import org.apache.logging.log4j.util.Cast;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class AnnotationHelper {
	private static final Map<Class<?>, Map<String, Object>> DEFAULT_VALUES = Maps.newHashMap();

	public static <T> T getValue(ModFileScanData.AnnotationData annotation, String fieldName) {
		Class<?> annotationClass = ReflectionHelper.getClass(annotation.annotationType().getClassName());
		Object defaultValue = getDefaultValue(annotationClass, fieldName);
		return Cast.cast(annotation.annotationData().getOrDefault(fieldName, defaultValue));
	}

	private static Object getDefaultValue(Class<?> annotation, String fieldName) {
		DEFAULT_VALUES.computeIfAbsent(annotation, annot ->
			Arrays.stream(annot.getDeclaredMethods())
				.filter(method -> !Objects.isNull(method.getDefaultValue()))
				.collect(Collectors.toMap(Method::getName, AnnotationHelper::returnListIfArray))
		);

		Map<String, Object> defaultMap = DEFAULT_VALUES.get(annotation);
		if (defaultMap == null) {
			AllTheLeaks.LOGGER.error("Field {} not found in annotation class {}", fieldName, annotation);
			return null;
		}
		return defaultMap.getOrDefault(fieldName, null);
	}

	private static Object returnListIfArray(Method method) {
		var value = method.getDefaultValue();
		return value.getClass().isArray() ? List.of((Object[]) value) : value;
	}
}
