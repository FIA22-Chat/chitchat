package io.github.chitchat.common.storage.database.types;

import java.util.EnumSet;
import org.jetbrains.annotations.NotNull;

public class BitFlag {
    private static final String FIELD_NAME = "elements";

    /**
     * Retrieves the inner bitmask value of an {@link EnumSet} by bending Java to our will
     * (Reflection).
     *
     * @apiNote Currently only supports RegularEnumSet, meaning enums that have over 64 enum
     *     constants will not work.
     * @param set The EnumSet to retrieve the bitmask from
     * @return The bitmask of the EnumSet
     * @throws RuntimeException If any of the reflection calls fail
     */
    public static long toBitMask(@NotNull EnumSet<?> set) {
        var setClazz = set.getClass();

        try {
            var elementsField = setClazz.getDeclaredField(FIELD_NAME);
            elementsField.setAccessible(true);

            return (long) elementsField.get(set);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Creates and sets the inner bitmask value of {@link EnumSet} by bending Java to our will
     * (Reflection).
     *
     * @apiNote Currently only supports RegularEnumSet, meaning enums that have over 64 enum
     *     constants will not work.
     * @param enumClazz The class of the EnumSet to create
     * @param mask The bitmask to create the EnumSet from
     * @return A new EnumSet created from the bitmask
     * @throws RuntimeException If any of the reflection calls fail
     */
    public static <T extends Enum<T>> @NotNull EnumSet<T> fromBitMask(
            Class<T> enumClazz, long mask) {
        var set = EnumSet.noneOf(enumClazz);
        setBitMask(set, mask);

        return set;
    }

    /**
     * Sets the inner bitmask value of an {@link EnumSet} by bending Java to our will (Reflection).
     *
     * @apiNote Currently only supports RegularEnumSet, meaning enums that have over 64 enum
     *     constants will not work.
     * @param set The EnumSet to set the inner bitmask of
     * @param mask The bitmask to set the EnumSet to
     * @throws RuntimeException If any of the reflection calls fail
     */
    public static void setBitMask(@NotNull EnumSet<?> set, long mask) {
        var setClazz = set.getClass();

        try {
            var elementsField = setClazz.getDeclaredField(FIELD_NAME);
            elementsField.setAccessible(true);

            elementsField.set(set, mask);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
