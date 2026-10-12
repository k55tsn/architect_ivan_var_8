package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Buyer;

public class BuyerFormController {

    @FXML private TextField lastNameField;
    @FXML private TextField firstNameField;
    @FXML private TextField phoneField;
    @FXML private TextField cardField;
    @FXML private Label errorLabel;

    private Buyer result = null;

    public void setBuyer(Buyer buyer) {
        lastNameField.setText(buyer.getLastName());
        firstNameField.setText(buyer.getFirstName());
        phoneField.setText(buyer.getPhone());
        cardField.setText(buyer.getCardNumber());
    }

    public Buyer getResult() { return result; }

    @FXML
    private void onSave() {
        String lastName = lastNameField.getText().trim();
        String firstName = firstNameField.getText().trim();
        String phone = phoneField.getText().trim();
        String card = cardField.getText().trim();

        if (lastName.isEmpty() || firstName.isEmpty()) {
            errorLabel.setText("Заполните фамилию и имя");
            return;
        }
        if (!card.matches("\\d{8}")) {
            errorLabel.setText("Номер карты должен содержать ровно 8 цифр");
            return;
        }

        result = new Buyer(0, lastName, firstName, phone, card);
        close();
    }

    @FXML
    private void onCancel() {
        result = null;
        close();
    }

    private void close() {
        ((Stage) lastNameField.getScene().getWindow()).close();
    }
}