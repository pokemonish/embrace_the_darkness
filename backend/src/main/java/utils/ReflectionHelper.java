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
    public static Object createInstanceWithParameters(String className, Object[] parameters) {
        try {
            Class<?> clazz = Class.forName(className);

            Class[] types = new Class[parameters.length];

            for (int i = 0; i < parameters.length; ++i) {
                types[i] = parameters[i].getClass();
            }

            Constructor constructor = null;
            try {
                constructor = clazz.getConstructor(types);
            } catch (NoSuchMethodException e) {
                Class[][] interfaces = new Class[parameters.length][];

                for (int i = 0; i < parameters.length; ++i) {
                    interfaces[i] = parameters[i].getClass().getInterfaces();
                }
                for (Class[] interface1 : interfaces) {
                    try {
                        constructor = clazz.getConstructor(interface1);
                    } catch (NoSuchMethodException e1) {
                        continue;
                    }
                    break;
                }
            }
            assert (constructor != null);
            return constructor.newInstance(parameters);
        } catch (IllegalArgumentException | SecurityException |
                InstantiationException | IllegalAccessException |
                ClassNotFoundException |
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
