package com.blueo.common;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class ReflectionUtil {

	private static final String TYPE_CLASS_NAME_PREFIX = "class ";
	private static final String TYPE_INTERFACE_NAME_PREFIX = "interface ";

	private ReflectionUtil() {
	}

	public static String getClassName(Type type) {
		if (type == null) {
			return "";
		}
		String className = type.toString();
		if (className.startsWith(TYPE_CLASS_NAME_PREFIX)) {
			className = className.substring(TYPE_CLASS_NAME_PREFIX.length());
		} else if (className.startsWith(TYPE_INTERFACE_NAME_PREFIX)) {
			className = className.substring(TYPE_INTERFACE_NAME_PREFIX.length());
		}
		return className;
	}

	public static Class<?> getClass(Type type) throws ClassNotFoundException {
		String className = getClassName(type);
		if (className == null || className.isEmpty()) {
			return null;
		}
		return Class.forName(className);
	}

	public static Object newInstance(Type type) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		Class<?> clazz = getClass(type);
		if (clazz == null) {
			return null;
		}
		return clazz.newInstance();
	}

	public static Type[] getParameterizedTypes(Object object) {
		Type superclassType = object.getClass().getGenericSuperclass();
		if (!ParameterizedType.class.isAssignableFrom(superclassType.getClass())) {
			return null;
		}

		return ((ParameterizedType) superclassType).getActualTypeArguments();
	}

	public static boolean hasDefaultConstructor(Class<?> clazz) throws SecurityException {
		Class<?>[] empty = {};
		try {
			clazz.getConstructor(empty);
		} catch (NoSuchMethodException e) {
			return false;
		}
		return true;
	}

	public static Class<?> getFieldClass(Class<?> clazz, String name) {
		if (clazz == null || name == null || name.isEmpty()) {
			return null;
		}

		Class<?> propertyClass = null;

		for (Field field : clazz.getDeclaredFields()) {
			field.setAccessible(true);
			if (field.getName().equalsIgnoreCase(name)) {
				propertyClass = field.getType();
				break;
			}
		}

		return propertyClass;
	}

	public static Class<?> getMethodReturnType(Class<?> clazz, String name) {
		if (clazz == null || name == null || name.isEmpty()) {
			return null;
		}

		name = name.toLowerCase();
		Class<?> returnType = null;

		for (Method method : clazz.getDeclaredMethods()) {
			if (method.getName().equals(name)) {
				returnType = method.getReturnType();
				break;
			}
		}

		return returnType;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static Object getEnumConstant(Class<?> clazz, String name) {
		if (clazz == null || name == null || name.isEmpty()) {
			return null;
		}
		return Enum.valueOf((Class<Enum>) clazz, name);
	}
}