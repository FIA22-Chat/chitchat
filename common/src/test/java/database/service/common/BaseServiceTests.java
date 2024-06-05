package database.service.common;

import io.github.chitchat.common.storage.database.models.common.BaseModel;
import io.github.chitchat.common.storage.database.service.common.IBaseService;
import io.github.chitchat.common.storage.database.service.exceptions.DuplicateItemException;
import java.util.Random;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;

public abstract class BaseServiceTests<
        Service extends IBaseService<Model>, Model extends BaseModel> {
    private final Random random = new Random();
    protected final Service service = initService("baseServiceTests" + random.nextInt() + ".db");

    protected abstract @NotNull Service initService(String name);

    @Contract(" -> new")
    protected abstract @NotNull Model generateModel();

    @Test
    void count() throws DuplicateItemException {
        var service = initService("baseServiceCountTests" + random.nextInt() + ".db");
        var model = generateModel();
        service.create(model);

        service.count();
    }

    @Test
    void create() throws DuplicateItemException {
        var model = generateModel();
        service.create(model);
    }

    @Test
    void update() throws DuplicateItemException {
        var model = generateModel();
        service.create(model);
        service.update(model);
    }

    @Test
    void delete() throws DuplicateItemException {
        var model = generateModel();
        service.create(model);
        service.delete(model);
    }
}
