package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TabPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Sale;
import util.DataStore;

public class MainController {

    @FXML private Label statusLabel;
    @FXML private TabPane tabPane;
    @FXML private Button loginBtn;

    private final DataStore store = DataStore.getInstance();

    @FXML
    private void initialize() {
        // Первичное обновление строки состояния
        updateStatus();

        // Пересчёт при изменении количества продаж
        store.getSales().addListener((javafx.collections.ListChangeListener<Sale>) c -> updateStatus());

        // Обновление при входе/выходе и отключение кнопки "Войти"
        store.authorizedProperty().addListener((obs, oldV, newV) -> {
            updateStatus();
            loginBtn.setDisable(newV); // Если авторизован (true) — кнопка неактивна
        });

        // Начальное состояние кнопки (на случай, если уже авторизован)
        loginBtn.setDisable(store.isAuthorized());
    }

    /**
     * Обновляет строку состояния внизу главного окна:
     * статус авторизации, количество записей в продажах, текущая дата.
     */
    private void updateStatus() {
        String user = store.isAuthorized()
                ? "Авторизован"
                : "Не авторизован";
        int salesCount = store.getSales().size();
        String date = java.time.LocalDate.now().toString();
        statusLabel.setText(user + " | Записей: " + salesCount + " | Дата: " + date);
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
            store.setAuthorized(true);
            updateStatus();
        });

        dialog.showAndWait();
    }
}