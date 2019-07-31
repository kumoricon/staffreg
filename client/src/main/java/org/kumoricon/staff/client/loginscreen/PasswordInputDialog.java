package org.kumoricon.staff.client.loginscreen;

import javafx.application.Platform;
import javafx.beans.Observable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;


public class PasswordInputDialog extends Dialog<String> {
    private final GridPane grid;
    private final Label label1;
    private final Label label2;
    private final PasswordField password1Field;
    private final PasswordField password2Field;


    public PasswordInputDialog() {
        DialogPane dialogPane = this.getDialogPane();
        this.password1Field = new PasswordField();
        this.password1Field.setMaxWidth(20);
        this.password2Field = new PasswordField();
        this.password2Field.setMaxWidth(20);

        GridPane.setHgrow(this.password1Field, Priority.ALWAYS);
        GridPane.setFillWidth(this.password1Field, true);
        this.label1 = new Label("Password:");
        this.label1.setPrefWidth(-1.0D);
        this.label1.textProperty().bind(dialogPane.contentTextProperty());
        this.label2 = new Label("Repeat:");
        this.label2.setPrefWidth(-1.0D);

        this.grid = new GridPane();
        this.grid.setHgap(10.0D);
        this.grid.setMaxWidth(2);
        this.grid.setAlignment(Pos.CENTER_LEFT);
        dialogPane.contentTextProperty().addListener((o) -> this.updateGrid());
        dialogPane.getStyleClass().add("text-input-dialog");
        dialogPane.getButtonTypes().addAll(ButtonType.OK);
        dialogPane.lookupButton(ButtonType.OK).setDisable(true);
        this.updateGrid();
        this.setResultConverter((dialogButton) -> {
            ButtonBar.ButtonData data = dialogButton == null ? null : dialogButton.getButtonData();
            return data == ButtonBar.ButtonData.OK_DONE ? this.password1Field.getText() : null;
        });

        this.password1Field.textProperty().addListener(this::checkPasswordIsValid);
        this.password2Field.textProperty().addListener(this::checkPasswordIsValid);

    }

    private void checkPasswordIsValid(Observable observable) {

        if (password1Field.getText().trim().isEmpty()) {
            this.getDialogPane().lookupButton(ButtonType.OK).setDisable(true);
        }

        if (password1Field.getText().length() < 4) {
            this.getDialogPane().lookupButton(ButtonType.OK).setDisable(true);
        }

        if (password1Field.getText().equals(password2Field.getText())) {
            this.getDialogPane().lookupButton(ButtonType.OK).setDisable(false);
        } else {
            this.getDialogPane().lookupButton(ButtonType.OK).setDisable(true);
        }
    }

    public final TextField getEditor1() {
        return this.password1Field;
    }
    public final TextField getEditor2() { return this.password2Field; }

    private void updateGrid() {
        this.grid.getChildren().clear();
        this.grid.add(this.label1, 0, 0);
        this.grid.add(this.password1Field, 1, 0);
        this.grid.add(this.label2, 0, 1);
        this.grid.add(this.password2Field, 1, 1);

        this.getDialogPane().setContent(this.grid);
        Platform.runLater(this.password1Field::requestFocus);
    }
}
