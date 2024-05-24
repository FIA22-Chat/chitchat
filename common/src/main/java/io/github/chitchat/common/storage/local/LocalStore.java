package io.github.chitchat.common.storage.local;

import io.github.chitchat.common.storage.local.config.Evaluation;
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
    private static final Logger log = LogManager.getLogger(LocalStore.class);
    private static final String STORE_HOME = "STORE_HOME";

    private final Evaluation evaluationStrategy;
    private final File fileStore;
    private final IValue<T> defaultValue;
    private final Class<T> type;
    private boolean isLoaded;
    private T value;

    /**
     * Creates a local store that persists the value to disk, reevaluating as specified by {@link
     * Evaluation}. Caution should be taken when the type or the name of the value changes, as the
     * reference to the file stored on the disk will be lost.
     *
     * @param evaluation the evaluation strategy to use when saving the value to disk. If {@link
     *     Evaluation#LAZY} is used, the value will be saved to disk only when {@link #flush()} is
     *     called.
     * @param type the type of the value to store which must implement {@link Serializable}.
     * @param defaultValue the default value to store, must implement {@link Serializable}. This
     *     will be used to generate the file name.
     */
    public LocalStore(Evaluation evaluation, Class<T> type, IValue<T> defaultValue) {
        this(type.getSimpleName(), evaluation, null, type, defaultValue);
    }

    /**
     * Creates a local store that persists the value to disk, reevaluating as specified by {@link
     * Evaluation}. Caution should be taken when the type or the name of the value changes, as the
     * reference to the file stored on the disk will be lost.
     *
     * @param name the name of the value to store. This will be used to generate the file name.
     * @param evaluation the evaluation strategy to use when saving the value to disk. If {@link
     *     Evaluation#LAZY} is used, the value will be saved to disk only when {@link #flush()} is
     *     called.
     * @param type the type of the value to store which must implement {@link Serializable}.
     * @param defaultValue the default value to store, must implement {@link Serializable}.
     */
    public LocalStore(String name, Evaluation evaluation, Class<T> type, IValue<T> defaultValue) {
        this(name, evaluation, null, type, defaultValue);
    }

    /**
     * Creates a local store that persists the value to disk, reevaluating as specified by {@link
     * Evaluation}. Caution should be taken when the type or the name of the value changes, as the
     * reference to the file stored on the disk will be lost.
     *
     * @param type the type of the value to store which must implement {@link Serializable}.
     * @param storagePath the path to store the value. If null, the system property or finally
     *     environment variable {@link #STORE_HOME} will be used.
     * @param name the name of the value to store. This will be used to generate the file name.
     * @param evaluation the evaluation strategy to use when saving the value to disk. If {@link
     *     Evaluation#LAZY} is used, the value will be saved to disk only when {@link #flush()} is
     *     called.
     * @param defaultValue the default value to store, must implement {@link Serializable}.
     */
    public LocalStore(
            String name,
            Evaluation evaluation,
            Path storagePath,
            Class<T> type,
            IValue<T> defaultValue) {
        this.evaluationStrategy = evaluation;
        this.defaultValue = defaultValue;
        this.type = type;

        // Use the provided storage path or the environment variable
        var pathEnv = System.getenv(STORE_HOME);
        var pathProp = System.getProperty(STORE_HOME);
        // Check items in the following order, using the first one that is not a null
        // storage path, system property, environment variable
        if (storagePath == null)
            storagePath = Path.of(pathEnv == null ? (pathProp == null ? "" : pathProp) : pathEnv);
        fileStore = storagePath.resolve(name + ".store").toFile();
        log.trace("Assigned {} store path {}", name, fileStore.getAbsolutePath());

        if (evaluationStrategy == Evaluation.EAGER) get(); // Ensure the value is loaded
    }

    /**
     * Set the value and save it to the disk depending on the evaluation strategy.
     *
     * @param value the updated value to store
     */
    public synchronized void set(T value) {
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
    public synchronized @NotNull T get() {
        if (!isLoaded) {
            try {
                load();
            } catch (ClassNotFoundException | IOException e) {
                log.error("Failed to load the value from the disk, using default value", e);
            } finally {
                if (value == null) value = defaultValue.computeDefaultValue();
                isLoaded = true;
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
    public synchronized void flush() throws IOException {
        log.trace("Flushing the value to the disk.");
        save();
    }

    /** Drop the value from the disk. */
    public synchronized void drop() {
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
    private synchronized void save() throws IOException {
        var parent = fileStore.getParentFile();
        if (parent != null) {
            var _ = parent.mkdirs();
        }
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
    private synchronized boolean load() throws IOException, ClassNotFoundException {
        if (!fileStore.exists()) return false;

        var inputStream = new FileInputStream(fileStore);
        var objectInputStream = new ObjectInputStream(inputStream);
        var obj = objectInputStream.readObject();
        objectInputStream.close();
        inputStream.close();

        if (!type.isInstance(obj) && obj != null)
            throw new ClassCastException(
                    "The object is not of the correct type, expected "
                            + type.getName()
                            + " but got "
                            + obj.getClass().getName());

        value = (T) obj;
        return true;
    }
}
