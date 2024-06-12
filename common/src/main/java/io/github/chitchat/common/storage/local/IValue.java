package io.github.chitchat.common.storage.local;

import java.io.Serializable;

public interface IValue<T extends Serializable> {
    T computeDefaultValue();
}
