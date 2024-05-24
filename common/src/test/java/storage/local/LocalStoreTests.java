package storage.local;

import static org.junit.jupiter.api.Assertions.*;

import io.github.chitchat.common.storage.local.IValue;
import io.github.chitchat.common.storage.local.LocalStore;
import io.github.chitchat.common.storage.local.config.Evaluation;
import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import org.junit.jupiter.api.Test;

public class LocalStoreTests {
    static String defaultValueComputed = "default";
    static IValue<String> defaultValue = () -> defaultValueComputed;
    Path path = Path.of("src/test/resources").toAbsolutePath();

    @Test
    void createLazyLocalStore() throws IOException, ClassNotFoundException {
        var store =
                new LocalStore<>(
                        "testLazyStore", Evaluation.LAZY, path, String.class, defaultValue);
        assertNotNull(store);
    }

    @Test
    void createEagerLocalStore() throws IOException, ClassNotFoundException {
        var store =
                new LocalStore<>(
                        "testEagerStore", Evaluation.EAGER, path, String.class, defaultValue);
        assertNotNull(store);

        assertTrue(store.getFilePath().toAbsolutePath().toFile().exists());
        assertTrue(path.resolve(store.getFilePath().getFileName()).toFile().exists());
        store.drop();
    }

    @Test
    void getLocalStoreValue() throws IOException, ClassNotFoundException {
        var store = new LocalStore<>("testGetStore", Evaluation.LAZY, String.class, defaultValue);
        assertNotNull(store);
        assertEquals(defaultValueComputed, store.get());
    }

    @Test
    void setLocalStoreValue() throws IOException, ClassNotFoundException {
        var store = new LocalStore<>("testSetStore", Evaluation.LAZY, String.class, defaultValue);
        assertNotNull(store);
        assertEquals(defaultValueComputed, store.get());

        store.set("newValue");
        assertEquals("newValue", store.get());
    }

    @Test
    void dropLocalStore() throws IOException, ClassNotFoundException {
        var store = new LocalStore<>("testDropStore", Evaluation.LAZY, String.class, defaultValue);
        assertNotNull(store);
        assertEquals(defaultValueComputed, store.get());

        store.drop();
        assertFalse(store.getFilePath().toFile().exists());
    }

    @Test
    void flushLocalStore() throws IOException, ClassNotFoundException {
        var store = new LocalStore<>("testFlushStore", Evaluation.LAZY, String.class, defaultValue);
        assertNotNull(store);
        assertEquals(defaultValueComputed, store.get());

        store.flush();
        assertEquals(defaultValueComputed, store.get());

        var store2 =
                new LocalStore<>("testFlushStore", Evaluation.LAZY, String.class, defaultValue);
        assertNotNull(store);
        assertEquals(defaultValueComputed, store2.get());

        store2.set("newValue");
        store2.flush();
        assertEquals("newValue", store2.get());

        store2.drop();
    }

    @SuppressWarnings("unchecked")
    @Test
    void objectLocalStore() throws IOException, ClassNotFoundException {
        var map = new HashMap<String, Integer>();
        IValue<HashMap<String, Integer>> defaultValue = () -> map;
        map.put("test1", 1);
        map.put("test2", 2);

        var store =
                new LocalStore<>(
                        "testObjectStore",
                        Evaluation.LAZY,
                        (Class<HashMap<String, Integer>>) (Class<?>) HashMap.class,
                        defaultValue);
        assertNotNull(store);
        store.flush();

        var value = store.get();
        assertEquals(map, value);
        assertEquals(1, value.get("test1"));
        assertEquals(2, value.get("test2"));

        map.put("test3", 3);
        store.set(map);
        store.flush();

        value = store.get();
        assertEquals(map, value);
        assertEquals(1, value.get("test1"));
        assertEquals(2, value.get("test2"));
        assertEquals(3, value.get("test3"));

        store.drop();
    }
}
