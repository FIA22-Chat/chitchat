package io.github.chitchat.storage.local.config;

/** Defines how the value should be reevaluated. */
public enum Evaluation {
    /** The value should only be flushed to disk when manually requested. */
    LAZY,
    /** The value should be automatically flushed to disk as soon as possible. */
    EAGER
}
