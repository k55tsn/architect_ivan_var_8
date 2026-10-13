package controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.Buyer;
import model.Medicine;
import model.Sale;
import util.DataStore;

public class SaleFormController {

    @FXML private ComboBox<Buyer> buyerCombo;
    @FXML private ComboBox<Medicine> medicineCombo;
    @FXML private Spinner<Integer> quantitySpinner;
    @FXML private DatePicker datePicker;
    @FXML private CheckBox recipeCheck;
    @FXML private TextField sumField;
    @FXML private Label errorLabel;

    private Sale result = null;
    private final DataStore store = DataStore.getInstance();

    @FXML
    private void initialize() {
        buyerCombo.setItems(store.getBuyers());
        medicineCombo.setItems(store.getMedicines());

        SpinnerValueFactory.IntegerSpinnerValueFactory qtyFactory =
                new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 50, 1);
        quantitySpinner.setValueFactory(qtyFactory);

        datePicker.setValue(java.time.LocalDate.now());

        buyerCombo.valueProperty().addListener((obs, oldV, newV) -> recalcSum());
        medicineCombo.valueProperty().addListener((obs, oldV, newV) -> recalcSum());
        quantitySpinner.valueProperty().addListener((obs, oldV, newV) -> recalcSum());
    }

    private void recalcSum() {
        Medicine med = medicineCombo.getValue();
        Integer qty = quantitySpinner.getValue();
        if (med != null && qty != null) {
            double sum = med.getPrice() * qty * 0.95;
            sumField.setText(String.format("%.2f", sum));
        } else {
            sumField.setText("");
        }
    }

    public void setSale(Sale sale) {
        buyerCombo.setValue(sale.getBuyer());
        medicineCombo.setValue(sale.getMedicine());
        quantitySpinner.getValueFactory().setValue(sale.getQuantity());
        datePicker.setValue(sale.getDate());
        recipeCheck.setSelected(sale.isRecipePresented());
        recalcSum();
    }

    public Sale getResult() { return result; }

    @FXML
    private void onSave() {
        Buyer buyer = buyerCombo.getValue();
        Medicine med = medicineCombo.getValue();
        Integer qty = quantitySpinner.getValue();
        java.time.LocalDate date = datePicker.getValue();

        if (buyer == null) { errorLabel.setText("Выберите покупателя"); return; }
        if (med == null) { errorLabel.setText("Выберите препарат"); return; }
        if (date == null) { errorLabel.setText("Выберите дату"); return; }
        if (qty == null || qty < 1 || qty > 50) {
            errorLabel.setText("Количество должно быть от 1 до 50");
            return;
        }
        if (med.isNeedRecipe() && !recipeCheck.isSelected()) {
            errorLabel.setText("Препарат требует рецепта — отметьте флажок");
            return;
        }

        result = new Sale(0, buyer, med, qty, date, recipeCheck.isSelected());
        close();
    }

    @FXML
    private void onCancel() {
        result = null;
        close();
    }

    private void close() {
        ((Stage) buyerCombo.getScene().getWindow()).close();
    }
}