package controller;

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Medicine;

public class MedicineFormController {

    @FXML private TextField nameField;
    @FXML private ComboBox<String> categoryCombo;
    @FXML private TextField priceField;
    @FXML private CheckBox needRecipeCheck;
    @FXML private Label errorLabel;

    private Medicine result = null;

    @FXML
    private void initialize() {
        categoryCombo.getItems().addAll(
                "Антибиотики",
                "Обезболивающие",
                "Витамины",
                "Сердечно-сосудистые",
                "Противовирусные",
                "Успокоительные"
        );
    }

    public void setMedicine(Medicine medicine) {
        nameField.setText(medicine.getName());
        categoryCombo.setValue(medicine.getCategory());
        priceField.setText(String.valueOf(medicine.getPrice()));
        needRecipeCheck.setSelected(medicine.isNeedRecipe());
    }

    public Medicine getResult() { return result; }

    @FXML
    private void onSave() {
        String name = nameField.getText().trim();
        String category = categoryCombo.getValue();
        String priceStr = priceField.getText().trim();

        if (name.isEmpty()) {
            errorLabel.setText("Введите наименование препарата");
            return;
        }
        if (category == null || category.isEmpty()) {
            errorLabel.setText("Выберите категорию");
            return;
        }

        double price;
        try {
            price = Double.parseDouble(priceStr.replace(",", "."));
        } catch (NumberFormatException e) {
            errorLabel.setText("Цена должна быть числом");
            return;
        }
        if (price <= 0) {
            errorLabel.setText("Цена должна быть положительной");
            return;
        }

        boolean needRecipe = needRecipeCheck.isSelected();

        result = new Medicine(0, name, category, price, needRecipe);
        close();
    }

    @FXML
    private void onCancel() {
        result = null;
        close();
    }

    private void close() {
        ((Stage) nameField.getScene().getWindow()).close();
    }
}