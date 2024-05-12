package io.github.chitchat.common.storage.database.models.common;

import java.io.Serializable;
import lombok.AllArgsConstructor;

/**
 * The {@link BaseModel} class is the base class for all models. Any class that extends this will be
 * considered a model and will also be serializable.
 *
 * @see IndexableModel
 */
@AllArgsConstructor
public class BaseModel implements Serializable {}
