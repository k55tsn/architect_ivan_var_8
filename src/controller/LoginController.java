package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.User;
import util.UserLoader;

import java.util.List;
import java.util.function.Consumer;

public class LoginController {

    @FXML private TextField loginField;
    @FXML private PasswordField passwordField;
    @FXML private Label errorLabel;

    private Consumer<User> onSuccess;

    public void setOnSuccess(Consumer<User> onSuccess) {
        this.onSuccess = onSuccess;
    }

    @FXML
    private void onSubmit() {
        try {
            List<User> users = UserLoader.load("users.txt");
            String login = loginField.getText().trim();
            String password = passwordField.getText();

            for (User u : users) {
                if (u.getLogin().equals(login) && u.getPassword().equals(password)) {
                    if (onSuccess != null) onSuccess.accept(u);
                    close();
                    return;
                }
            }
            errorLabel.setText("Неправильный логин/пароль");
        } catch (Exception e) {
            errorLabel.setText("Ошибка: " + e.getMessage());
        }
    }

    @FXML
    private void onCancel() {
        close();
    }

    private void close() {
        ((Stage) loginField.getScene().getWindow()).close();
    }
}