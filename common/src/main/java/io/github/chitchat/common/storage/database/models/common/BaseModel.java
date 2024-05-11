package io.github.chitchat.common.storage.database.models.common;

import java.io.Serializable;
import lombok.experimental.SuperBuilder;

/**
 * The {@link BaseModel} class is the base class for all models. Any class that extends this will be
 * considered a model and will also be serializable.
 *
 * @see IndexableModel
 */
@SuperBuilder
public class BaseModel implements Serializable {}
