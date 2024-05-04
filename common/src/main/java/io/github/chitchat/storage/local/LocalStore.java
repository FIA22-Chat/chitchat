package io.github.chitchat.storage.local;

import io.github.chitchat.storage.local.config.Evaluation;
import java.io.*;
import java.nio.file.Path;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

/**
 * A storage that persists the value to disk, reevaluating as specified by {@link Evaluation}.
 *
 * @param <T> the type of the value to store which must implement {@link Serializable}
 */
public class LocalStore<T extends Serializable> implements Flushable {
    public static final String STORE_HOME = "STORE_HOME";
    private static final Logger log = LogManager.getLogger(LocalStore.class);

    @NotNull private final File fileStore;
    private final Evaluation evaluationStrategy;
    private T value;
    private boolean isLoaded;

    /**
     * Creates a local store that persists the value to disk, reevaluating as specified by {@link
     * Evaluation}. Caution should be taken when the type or the name of the value changes, as the
     * reference to the file stored on the disk will be lost.
     *
     * @param defaultValue the default value to store, must implement {@link Serializable}. This
     *     will be used to generate the file name.
     * @param name the name of the value to store. This will be used to generate the file name.
     * @param evaluation the evaluation strategy to use when saving the value to disk. If {@link
     *     Evaluation#LAZY} is used, the value will be saved to disk only when {@link #flush()} is
     *     called.
     */
    public LocalStore(T defaultValue, String name, Evaluation evaluation)
            throws IOException, ClassNotFoundException {
        this(defaultValue, name, null, evaluation);
    }

    /**
     * Creates a local store that persists the value to disk, reevaluating as specified by {@link
     * Evaluation}. Caution should be taken when the type or the name of the value changes, as the
     * reference to the file stored on the disk will be lost.
     *
     * @param defaultValue the default value to store, must implement {@link Serializable}. This
     *     will be used to generate the file name.
     * @param name the name of the value to store. This will be used to generate the file name.
     * @param storagePath the path to store the value. If null, the environment variable {@value
     *     STORE_HOME} will be used.
     * @param evaluation the evaluation strategy to use when saving the value to disk. If {@link
     *     Evaluation#LAZY} is used, the value will be saved to disk only when {@link #flush()} is
     *     called.
     */
    public LocalStore(T defaultValue, String name, Path storagePath, Evaluation evaluation)
            throws IOException, ClassNotFoundException {
        this.value = defaultValue;
        this.evaluationStrategy = evaluation;

        // Use the provided storage path or the environment variable
        var pathEnv = System.getenv(STORE_HOME);
        if (storagePath == null) storagePath = Path.of(pathEnv == null ? "" : pathEnv);
        fileStore =
                storagePath.resolve(value.getClass().getSimpleName() + name + ".store").toFile();
        log.trace("File store path: {}", fileStore.getAbsolutePath());

        if (evaluation == Evaluation.EAGER)
            if (!load()) save(); // Try to load, saving the default value if loading fails
    }

    /**
     * Set the value and save it to the disk depending on the evaluation strategy.
     *
     * @param value the updated value to store
     */
    public void set(T value) {
        this.value = value;
        isLoaded = true; // No need to load the value from the disk again

        if (evaluationStrategy != Evaluation.EAGER) return;
        try {
            save();
        } catch (IOException e) {
            log.error("Failed to save the value to the disk.", e);
        }
    }

    /**
     * Get the value, loading it from the disk if not already loaded.
     *
     * @return the value
     */
    public T get() {
        if (!isLoaded) {
            try {
                load();
            } catch (IOException | ClassNotFoundException e) {
                log.error("Failed to load the value from the disk.", e);
            }
        }
        return value;
    }

    /**
     * Flush the value to the disk. This happens automatically when {@link Evaluation#EAGER} is
     * used.
     *
     * @throws IOException if an I/O error occurs
     */
    @Override
    public void flush() throws IOException {
        log.trace("Flushing the value to the disk.");
        save();
    }

    /** Drop the value from the disk. */
    public void drop() {
        var _ = fileStore.delete();
    }

    /** Get the path to the file store. */
    public Path getFilePath() {
        return fileStore.toPath();
    }

    /** Check if the value is loaded from the disk. */
    public boolean isLoaded() {
        return isLoaded;
    }

    /**
     * Save the value to the file store, overwriting the existing file.
     *
     * @throws IOException if an I/O error occurs
     */
    private void save() throws IOException {
        var _ = fileStore.getParentFile().mkdirs();
        var _ = fileStore.createNewFile();

        var outputStream = new FileOutputStream(fileStore);
        var objectOutputStream = new ObjectOutputStream(outputStream);
        objectOutputStream.writeObject(value);

        objectOutputStream.close();
        outputStream.close();
    }

    /**
     * Load the value from the file store if it exists.
     *
     * @return true if the value was loaded, false otherwise.
     * @throws IOException if an I/O error occurs
     * @throws ClassNotFoundException if the class of the object read from the stream cannot be
     *     found
     */
    @SuppressWarnings("unchecked")
    private boolean load() throws IOException, ClassNotFoundException {
        if (!fileStore.exists()) return false;

        var inputStream = new FileInputStream(fileStore);
        var objectInputStream = new ObjectInputStream(inputStream);
        var obj = objectInputStream.readObject();
        objectInputStream.close();
        inputStream.close();

        if (!value.getClass().isAssignableFrom(obj.getClass()))
            throw new ClassCastException("The object is not of the same type as the value.");

        value = (T) obj;
        isLoaded = true;
        return true;
    }
}
