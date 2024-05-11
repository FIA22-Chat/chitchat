package io.github.chitchat.common.storage.database;

import com.fasterxml.uuid.Generators;
import com.fasterxml.uuid.impl.TimeBasedEpochGenerator;
import java.util.UUID;

public class Generator {
    private static final TimeBasedEpochGenerator generator = Generators.timeBasedEpochGenerator();

    /**
     * Generates a new UUID v7.
     *
     * @return a new UUID
     */
    public static UUID newId() {
        return generator.generate();
    }
}
