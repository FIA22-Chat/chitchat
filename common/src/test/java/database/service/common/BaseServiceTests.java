package database.service.common;

import io.github.chitchat.common.storage.database.models.common.BaseModel;
import io.github.chitchat.common.storage.database.service.common.IBaseService;
import io.github.chitchat.common.storage.database.service.exceptions.DuplicateItemException;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;

public abstract class BaseServiceTests<
        Service extends IBaseService<Model>, Model extends BaseModel> {
    protected final Service service = initService();

    @Contract(" -> new")
    protected abstract @NotNull Service initService();

    @Contract(" -> new")
    protected abstract @NotNull Model generateModel();

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
