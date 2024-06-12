package io.github.chitchat.client.view.components;

import io.github.chitchat.client.view.components.common.IFieldErrorProperty;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.BooleanPropertyBase;
import lombok.Getter;
import org.controlsfx.control.textfield.CustomTextField;

@Getter
public class ChitTextField extends CustomTextField implements IFieldErrorProperty {
    private final BooleanProperty errorProperty;

    public ChitTextField() {
        super();
        getStyleClass().add("chit-text-field");

        errorProperty =
                new BooleanPropertyBase() {
                    @Override
                    protected void invalidated() {
                        pseudoClassStateChanged(ERROR_PSEUDO_CLASS, get());
                    }

                    @Override
                    public Object getBean() {
                        return ChitTextField.this;
                    }

                    @Override
                    public String getName() {
                        return "error";
                    }
                };
    }

    public void setError(boolean error) {
        errorProperty.set(error);
    }
}
