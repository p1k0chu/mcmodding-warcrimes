package io.github.p1k0chu.mcmodcrimes.common;

import org.jspecify.annotations.Nullable;

import java.lang.reflect.Field;
import java.util.Objects;

@SuppressWarnings("unchecked")
public final class FieldStealer {
    private FieldStealer() {
    }

    public static <T> T getField(Object obj, String fieldName) {
        return getField(obj, fieldName, null);
    }

    public static <T> T getField(Object obj, String fieldName, @Nullable Class<?> clazz) {
        try {
            return (T) findField(Objects.requireNonNullElseGet(clazz, obj::getClass), fieldName).get(obj);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static Field findField(Class<?> clazz, String fieldName) {
        try {
            Field f = clazz.getDeclaredField(fieldName);
            f.setAccessible(true);
            return f;
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }
}
