package io.github.chitchat.common.storage.database;

import com.fasterxml.uuid.Generators;
import com.fasterxml.uuid.impl.TimeBasedEpochRandomGenerator;
import java.util.Random;
import java.util.UUID;

public class DbUtil {
    private static final TimeBasedEpochRandomGenerator generator =
            Generators.timeBasedEpochRandomGenerator(new Random());

    /**
     * Generates a new UUID v7.
     *
     * @return a new UUID
     */
    public static UUID newId() {
        return generator.generate();
    }
}
