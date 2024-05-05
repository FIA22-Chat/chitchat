package storage.local;

import static org.junit.jupiter.api.Assertions.*;

import io.github.chitchat.common.storage.local.LocalStore;
import io.github.chitchat.common.storage.local.config.Evaluation;
import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import org.junit.jupiter.api.Test;

public class LocalStoreTests {
    Path path = Path.of("src/test/resources").toAbsolutePath();
    String defaultValue = "defaultValueTest";

    @Test
    void createLazyLocalStore() throws IOException, ClassNotFoundException {
        var store = new LocalStore<>(defaultValue, "testLazyStore", path, Evaluation.LAZY);
        assertNotNull(store);
    }

    @Test
    void createEagerLocalStore() throws IOException, ClassNotFoundException {
        var store = new LocalStore<>(defaultValue, "testEagerStore", path, Evaluation.EAGER);
        assertNotNull(store);

        assertTrue(store.getFilePath().toAbsolutePath().toFile().exists());
        assertTrue(path.resolve(store.getFilePath().getFileName()).toFile().exists());
        store.drop();
    }

    @Test
    void getLocalStoreValue() throws IOException, ClassNotFoundException {
        var store = new LocalStore<>(defaultValue, "testGetStore", path, Evaluation.LAZY);
        assertNotNull(store);
        assertEquals(defaultValue, store.get());
    }

    @Test
    void setLocalStoreValue() throws IOException, ClassNotFoundException {
        var store = new LocalStore<>(defaultValue, "testSetStore", path, Evaluation.LAZY);
        assertNotNull(store);
        assertEquals(defaultValue, store.get());

        store.set("newValue");
        assertEquals("newValue", store.get());
    }

    @Test
    void dropLocalStore() throws IOException, ClassNotFoundException {
        var store = new LocalStore<>(defaultValue, "testDropStore", path, Evaluation.LAZY);
        assertNotNull(store);
        assertEquals(defaultValue, store.get());

        store.drop();
        assertFalse(store.getFilePath().toFile().exists());
    }

    @Test
    void flushLocalStore() throws IOException, ClassNotFoundException {
        var store = new LocalStore<>(defaultValue, "testFlushStore", path, Evaluation.LAZY);
        assertNotNull(store);
        assertEquals(defaultValue, store.get());

        store.flush();
        assertEquals(defaultValue, store.get());

        var store2 = new LocalStore<>(defaultValue, "testFlushStore", path, Evaluation.LAZY);
        assertNotNull(store);
        assertEquals(defaultValue, store2.get());

        store2.set("newValue");
        store2.flush();
        assertEquals("newValue", store2.get());

        store2.drop();
    }

    @Test
    void objectLocalStore() throws IOException, ClassNotFoundException {
        var map = new HashMap<String, Integer>();
        map.put("test1", 1);
        map.put("test2", 2);

        var store = new LocalStore<>(map, "testObjectStore", path, Evaluation.LAZY);
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
