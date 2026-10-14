package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TabPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import util.DataStore;

public class MainController {

    @FXML private Label statusLabel;
    @FXML private TabPane tabPane;

    @FXML
private void initialize() {
    updateStatus();
    DataStore store = DataStore.getInstance();
    store.getSales().addListener((javafx.collections.ListChangeListener<model.Sale>) c -> updateStatus());
    store.authorizedProperty().addListener((obs, o, n) -> updateStatus());
}

private void updateStatus() {
    DataStore store = DataStore.getInstance();
    String user = store.isAuthorized() ? "Авторизован" : "Не авторизован";
    int count = store.getSales().size();
    String date = java.time.LocalDate.now().toString();
    statusLabel.setText(user + " | Записей: " + count + " | Дата: " + date);
}

   @FXML
private void onLogin() throws Exception {
    FXMLLoader loader = new FXMLLoader(getClass().getResource("/login.fxml"));
    Stage dialog = new Stage();
    dialog.initModality(Modality.APPLICATION_MODAL);
    dialog.setTitle("Авторизация");
    dialog.setScene(new Scene(loader.load()));

    LoginController controller = loader.getController();
    controller.setOnSuccess(user -> {
        statusLabel.setText("Авторизован: " + user.getFullName());
        DataStore.getInstance().setAuthorized(true);   // ← добавьте эту строку
    });

    dialog.showAndWait();
}
}