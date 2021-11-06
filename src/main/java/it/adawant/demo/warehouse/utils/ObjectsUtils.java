package it.adawant.demo.warehouse.utils;

import java.util.Objects;
import java.util.stream.Stream;

public class ObjectsUtils {
    private ObjectsUtils() {
    }

    public static boolean anyNotNull(Object... objs) {
        return Stream.of(objs).anyMatch(Objects::nonNull);
    }
}
