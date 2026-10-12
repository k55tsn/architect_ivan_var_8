package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class MainController {

    @FXML private Label statusLabel;

    @FXML
    private void initialize() {
        statusLabel.setText("Не авторизован");
    }

    @FXML
    private void onLogin() throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/login.fxml"));
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle("Авторизация");
        dialog.setScene(new Scene(loader.load()));

        LoginController controller = loader.getController();
        controller.setOnSuccess(user ->
                statusLabel.setText("Авторизован: " + user.getFullName())
        );

        dialog.showAndWait();
    }
}