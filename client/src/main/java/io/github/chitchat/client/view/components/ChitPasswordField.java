package io.github.chitchat.client.view.components;

import io.github.chitchat.client.view.components.common.IFieldErrorProperty;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.BooleanPropertyBase;
import javafx.css.PseudoClass;
import lombok.Getter;
import org.controlsfx.control.textfield.CustomPasswordField;

public class ChitPasswordField extends CustomPasswordField implements IFieldErrorProperty {
    private static final PseudoClass PASSWORD_VISIBLE =
            PseudoClass.getPseudoClass("password-visible");

    @Getter private final BooleanProperty passwordErrorProperty;
    @Getter private final BooleanProperty passwordVisibleProperty;

    private String initialPromptText;
    private String ephemeralPassword;

    public ChitPasswordField() {
        super();
        getStyleClass().addAll("chit-password-field", "chit-text-field");

        Platform.runLater(
                () -> {
                    this.initialPromptText = this.getPromptText();
                    this.getRight().setOnMousePressed(_ -> revealPassword());
                    this.getRight().setOnMouseReleased(_ -> concealPassword());
                });

        passwordVisibleProperty =
                new BooleanPropertyBase(false) {
                    @Override
                    protected void invalidated() {
                        pseudoClassStateChanged(PASSWORD_VISIBLE, get());
                    }

                    @Override
                    public Object getBean() {
                        return ChitPasswordField.this;
                    }

                    @Override
                    public String getName() {
                        return "passwordVisible";
                    }
                };

        passwordErrorProperty =
                new BooleanPropertyBase() {
                    @Override
                    protected void invalidated() {
                        pseudoClassStateChanged(ERROR_PSEUDO_CLASS, get());
                    }

                    @Override
                    public Object getBean() {
                        return ChitPasswordField.this;
                    }

                    @Override
                    public String getName() {
                        return "passwordError";
                    }
                };
    }

    public void setError(boolean error) {
        passwordErrorProperty.set(error);
    }

    private void revealPassword() {
        passwordVisibleProperty.set(true);

        ephemeralPassword = this.getText();
        this.setPromptText(ephemeralPassword);
        this.clear();
    }

    private void concealPassword() {
        passwordVisibleProperty.set(false);

        this.setText(ephemeralPassword);
        this.setPromptText(initialPromptText);
    }
}
