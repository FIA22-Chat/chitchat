package database.service.common;

import static org.junit.jupiter.api.Assertions.*;

import io.github.chitchat.common.storage.database.models.common.IndexableModel;
import io.github.chitchat.common.storage.database.service.common.IIndexableService;
import io.github.chitchat.common.storage.database.service.exceptions.DuplicateItemException;
import java.util.List;
import org.junit.jupiter.api.Test;

public abstract class IndexableServiceTests<
                Service extends IIndexableService<Model>, Model extends IndexableModel>
        extends BaseServiceTests<Service, Model> {

    @Test
    void existsById() throws DuplicateItemException {
        var model = generateModel();
        service.create(model);
        assertTrue(service.exists(model.getId()));
    }

    @Test
    void existsByObject() throws DuplicateItemException {
        var model = generateModel();
        service.create(model);
        assertTrue(service.exists(model));
    }

    @Test
    void get() throws DuplicateItemException {
        var model = generateModel();
        service.create(model);
        service.get(model.getId())
                .ifPresentOrElse(m -> assertEquals(model, m), () -> fail("Model not found"));
    }

    @Test
    void getMultiple() throws DuplicateItemException {
        var model1 = generateModel();
        var model2 = generateModel();
        service.create(model1);
        service.create(model2);
        var models = service.get(List.of(model1.getId(), model2.getId()));

        assertEquals(2, models.size());
        assertEquals(model1, models.get(model1.getId()));
        assertEquals(model2, models.get(model2.getId()));
    }
}
