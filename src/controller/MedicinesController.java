package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Medicine;
import util.DataStore;

public class MedicinesController {

    @FXML private TableView<Medicine> medicinesTable;
    @FXML private TableColumn<Medicine, String> colName;
    @FXML private TableColumn<Medicine, String> colCategory;
    @FXML private TableColumn<Medicine, Double> colPrice;
    @FXML private TableColumn<Medicine, Boolean> colNeedRecipe;

    private final DataStore store = DataStore.getInstance();

    @FXML
    private void initialize() {
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colCategory.setCellValueFactory(new PropertyValueFactory<>("category"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        colNeedRecipe.setCellValueFactory(new PropertyValueFactory<>("needRecipe"));

        medicinesTable.setItems(store.getMedicines());

        ContextMenu contextMenu = new ContextMenu();
        MenuItem editItem = new MenuItem("Редактировать");
        MenuItem deleteItem = new MenuItem("Удалить");
        MenuItem duplicateItem = new MenuItem("Дублировать");

        editItem.setOnAction(e -> editMedicine(medicinesTable.getSelectionModel().getSelectedItem()));
        deleteItem.setOnAction(e -> deleteMedicine(medicinesTable.getSelectionModel().getSelectedItem()));
        duplicateItem.setOnAction(e -> duplicateMedicine(medicinesTable.getSelectionModel().getSelectedItem()));

        contextMenu.getItems().addAll(editItem, deleteItem, duplicateItem);

        medicinesTable.setRowFactory(tv -> {
            TableRow<Medicine> row = new TableRow<>();
            row.setOnContextMenuRequested(event -> {
                if (!row.isEmpty()) {
                    medicinesTable.getSelectionModel().select(row.getItem());
                    contextMenu.show(row, event.getScreenX(), event.getScreenY());
                }
            });
            return row;
        });
    }

    @FXML
    private void onAdd() {
        Medicine created = showForm(null);
        if (created != null) {
            Medicine withId = new Medicine(store.nextMedicineId(), created.getName(),
                    created.getCategory(), created.getPrice(), created.isNeedRecipe());
            store.getMedicines().add(withId);
        }
    }

    private void editMedicine(Medicine medicine) {
        if (medicine == null) return;
        Medicine updated = showForm(medicine);
        if (updated != null) {
            medicine.setName(updated.getName());
            medicine.setCategory(updated.getCategory());
            medicine.setPrice(updated.getPrice());
            medicine.setNeedRecipe(updated.isNeedRecipe());
            medicinesTable.refresh();
        }
    }

    private void deleteMedicine(Medicine medicine) {
        if (medicine == null) return;
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                "Удалить препарат " + medicine.getName() + "?",
                ButtonType.YES, ButtonType.NO);
        alert.showAndWait().ifPresent(bt -> {
            if (bt == ButtonType.YES) store.getMedicines().remove(medicine);
        });
    }

    private void duplicateMedicine(Medicine medicine) {
        if (medicine == null) return;
        Medicine copy = new Medicine(store.nextMedicineId(), medicine.getName(),
                medicine.getCategory(), medicine.getPrice(), medicine.isNeedRecipe());
        store.getMedicines().add(copy);
    }

    private Medicine showForm(Medicine existing) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/medicine_form.fxml"));
            Stage dialog = new Stage();
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.setTitle(existing == null ? "Новый препарат" : "Редактирование препарата");
            dialog.setScene(new Scene(loader.load()));

            MedicineFormController ctrl = loader.getController();
            if (existing != null) ctrl.setMedicine(existing);
            dialog.showAndWait();

            return ctrl.getResult();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}