package kutschke.utility;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ArrayUtils {

    private ArrayUtils() {

    }

    public static <T extends Number> Object toPrimitiveArray(final T[] array) {
        final Class<?> primitiveClass = getPrimitiveClass(array.getClass().getComponentType());
        final Object arr = Array.newInstance(primitiveClass, array.length);
        for (int i = 0; i < array.length; i++) {
            Array.set(arr, i, array[i]);
        }
        return arr;
    }

    private static Class<?> getPrimitiveClass(final Class<?> componentType) {
        // review: always use brackets!
        if (Integer.class.isAssignableFrom(componentType)) {
            return int.class;
        }
        if (Double.class.isAssignableFrom(componentType)) {
            return double.class;
        }
        // review: throw instead of null
        return null;
    }

    @SuppressWarnings("unchecked")
    public static <T extends Number> T[] toWrapperArray(final Object array) {
        if (!array.getClass().isArray()) {
            throw new IllegalArgumentException("array is not an Array");
        }
        final Class<? extends T> primitiveClass = (Class<? extends T>) getWrapperClass(array.getClass()
                .getComponentType());
        final Object arr = Array.newInstance(primitiveClass, Array.getLength(array));
        for (int i = 0; i < Array.getLength(array); i++) {
            Array.set(arr, i, Array.get(array, i));
        }
        return (T[]) arr;
    }

    private static Class<?> getWrapperClass(final Class<?> componentType) {
        if (int.class.isAssignableFrom(componentType)) {
            return Integer.class;
        }
        if (double.class.isAssignableFrom(componentType)) {
            return Double.class;
        }
        return null;
    }

    public static <T> T[] shift(final T[] arr, final int amount) {
        final T[] result = Arrays.copyOf(arr, arr.length);
        for (int i = 0; i < arr.length; i++) {
            result[(i + amount) % arr.length] = arr[i];
        }
        return result;
    }

    public static double[] flatten(final Object array) {
        final List<Double> doubles = new ArrayList<Double>();
        flatten(array, doubles);
        final double[] result = new double[doubles.size()];
        int index = 0;
        for (final Double d : doubles) {
            result[index] = d;
            index++;
        }
        return result;
    }

    private static void flatten(final Object array, final List<Double> acc) {
        if (array.getClass().isArray()) {
            for (int i = 0; i < Array.getLength(array); i++) {
                flatten(Array.get(array, i), acc);
            }
        } else if (array.getClass().isAssignableFrom(Double.class)) {
            acc.add((Double) array);
        } else {
            throw new IllegalArgumentException("was not a double Arrays!");
        }

    }

}
