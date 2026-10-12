package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class MainController {
    @FXML private Label statusLabel;

    @FXML
    private void initialize() {
        statusLabel.setText("Не авторизован");
    }
}