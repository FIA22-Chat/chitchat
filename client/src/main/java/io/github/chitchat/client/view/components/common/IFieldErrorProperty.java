package io.github.chitchat.client.view.components.common;

import javafx.css.PseudoClass;

public interface IFieldErrorProperty {
    PseudoClass ERROR_PSEUDO_CLASS = PseudoClass.getPseudoClass("error");

    void setError(boolean error);
}
