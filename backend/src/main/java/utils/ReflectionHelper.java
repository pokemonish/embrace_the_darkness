package utils;

import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

public class ReflectionHelper {
    @Nullable
    public static Object createInstance(String className) {
        try {
            return Class.forName(className).newInstance();
        } catch (IllegalArgumentException | SecurityException |
                    InstantiationException | IllegalAccessException |
                    ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Nullable
    public static Object createInstanceWithParameters(String className, Object parameter) {
        try {
            Class<?> clazz = Class.forName(className);

            Constructor constructor;

            try {
                constructor = clazz.getConstructor(parameter.getClass());
            } catch (NoSuchMethodException e) {
                constructor = clazz.getConstructor(parameter.getClass().getInterfaces());
            }
            return constructor.newInstance(parameter);
        } catch (IllegalArgumentException | SecurityException |
                InstantiationException | IllegalAccessException |
                ClassNotFoundException | NoSuchMethodException |
                InvocationTargetException e) {
            e.printStackTrace();
        }

        return null;
    }


    public static void setFieldValue(Object object,
                                 String fieldName,
                                 String value) {
    try {
        Field field = object.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);

        if (field.getType().equals(String.class)) {
            field.set(object, value);
        } else if (field.getType().equals(int.class)) {
            field.set(object, Integer.decode(value));
        }

        field.setAccessible(false);
    } catch (NumberFormatException | NoSuchFieldException |
                IllegalAccessException e) {
        e.printStackTrace();
    }
    }
}
