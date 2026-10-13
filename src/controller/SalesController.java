package controller;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Sale;
import util.CsvUtil;
import util.DataStore;

import java.io.File;
import java.util.List;

public class SalesController {

    @FXML private TableView<Sale> salesTable;
    @FXML private TableColumn<Sale, Integer> colId;
    @FXML private TableColumn<Sale, String> colBuyer;
    @FXML private TableColumn<Sale, String> colMedicine;
    @FXML private TableColumn<Sale, Integer> colQuantity;
    @FXML private TableColumn<Sale, String> colDate;
    @FXML private TableColumn<Sale, String> colRecipe;
    @FXML private TableColumn<Sale, Double> colSum;
    @FXML private TableColumn<Sale, Double> colDeviation;

    private final DataStore store = DataStore.getInstance();

    @FXML
    private void initialize() {
        System.out.println(">>> SalesController.initialize() вызван");
        colId.setCellValueFactory(c -> new SimpleIntegerProperty(c.getValue().getId()).asObject());
        colBuyer.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getBuyer().toString()));
        colMedicine.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getMedicine().getName()));
        colQuantity.setCellValueFactory(c -> new SimpleIntegerProperty(c.getValue().getQuantity()).asObject());
        colDate.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getDate().toString()));
        colRecipe.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().isRecipePresented() ? "Да" : "Нет"));
        colSum.setCellValueFactory(c -> new SimpleDoubleProperty(c.getValue().getSum()).asObject());
        colDeviation.setCellValueFactory(c -> new SimpleDoubleProperty(calculateDeviation(c.getValue())).asObject());

        salesTable.setItems(store.getSales());

        ContextMenu contextMenu = new ContextMenu();
        MenuItem editItem = new MenuItem("Редактировать");
        MenuItem deleteItem = new MenuItem("Удалить");
        MenuItem duplicateItem = new MenuItem("Дублировать");

        editItem.setOnAction(e -> editSale(salesTable.getSelectionModel().getSelectedItem()));
        deleteItem.setOnAction(e -> deleteSale(salesTable.getSelectionModel().getSelectedItem()));
        duplicateItem.setOnAction(e -> duplicateSale(salesTable.getSelectionModel().getSelectedItem()));

        contextMenu.getItems().addAll(editItem, deleteItem, duplicateItem);

        salesTable.setRowFactory(tv -> {
            TableRow<Sale> row = new TableRow<>();
            row.setOnContextMenuRequested(event -> {
                if (!row.isEmpty()) {
                    salesTable.getSelectionModel().select(row.getItem());
                    contextMenu.show(row, event.getScreenX(), event.getScreenY());
                }
            });
            return row;
        });
    }

    private double calculateDeviation(Sale sale) {
        if (store.getSales().isEmpty()) return 0;
        double avg = store.getSales().stream().mapToDouble(Sale::getSum).average().orElse(0);
        return sale.getSum() - avg;
    }

    @FXML
    private void onAdd() {
        System.out.println(">>> SalesController.onAdd() вызван");
        Sale created = showForm(null);
        if (created != null) {
            Sale withId = new Sale(store.nextSaleId(), created.getBuyer(), created.getMedicine(),
                    created.getQuantity(), created.getDate(), created.isRecipePresented());
            store.getSales().add(withId);
            salesTable.refresh();
        }
    }

    private void editSale(Sale sale) {
        if (sale == null) return;
        Sale updated = showForm(sale);
        if (updated != null) {
            sale.setBuyer(updated.getBuyer());
            sale.setMedicine(updated.getMedicine());
            sale.setQuantity(updated.getQuantity());
            sale.setDate(updated.getDate());
            sale.setRecipePresented(updated.isRecipePresented());
            salesTable.refresh();
        }
    }

    private void deleteSale(Sale sale) {
        if (sale == null) return;
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                "Удалить продажу ID " + sale.getId() + "?",
                ButtonType.YES, ButtonType.NO);
        alert.showAndWait().ifPresent(bt -> {
            if (bt == ButtonType.YES) {
                store.getSales().remove(sale);
                salesTable.refresh();
            }
        });
    }

    private void duplicateSale(Sale sale) {
        if (sale == null) return;
        Sale copy = new Sale(store.nextSaleId(), sale.getBuyer(), sale.getMedicine(),
                sale.getQuantity(), sale.getDate(), sale.isRecipePresented());
        store.getSales().add(copy);
        salesTable.refresh();
    }

    private Sale showForm(Sale existing) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/sale_form.fxml"));
            Stage dialog = new Stage();
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.setTitle(existing == null ? "Новая продажа" : "Редактирование продажи");
            dialog.setScene(new Scene(loader.load()));

            SaleFormController ctrl = loader.getController();
            if (existing != null) ctrl.setSale(existing);
            dialog.showAndWait();

            return ctrl.getResult();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @FXML
    private void onSave() {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Сохранить продажи в CSV");
        chooser.setInitialFileName("sales.csv");
        chooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("CSV files", "*.csv"));

        File file = chooser.showSaveDialog(salesTable.getScene().getWindow());
        if (file == null) return;

        try {
            CsvUtil.saveSales(file, store.getSales());
            Alert alert = new Alert(Alert.AlertType.INFORMATION,
                    "Сохранено " + store.getSales().size() + " записей в " + file.getName());
            alert.showAndWait();
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Ошибка сохранения: " + e.getMessage()).showAndWait();
        }
    }

    @FXML
    private void onLoad() {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Загрузить продажи из CSV");
        chooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("CSV files", "*.csv"));

        File file = chooser.showOpenDialog(salesTable.getScene().getWindow());
        if (file == null) return;

        try {
            List<Sale> loaded = CsvUtil.loadSales(file);
            store.getSales().setAll(loaded);
            salesTable.refresh();
            Alert alert = new Alert(Alert.AlertType.INFORMATION,
                    "Загружено " + loaded.size() + " записей из " + file.getName());
            alert.showAndWait();
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Ошибка загрузки: " + e.getMessage()).showAndWait();
        }
    }
}