package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Buyer;

public class BuyersController {

    @FXML private TableView<Buyer> buyersTable;
    @FXML private TableColumn<Buyer, String> colLastName;
    @FXML private TableColumn<Buyer, String> colFirstName;
    @FXML private TableColumn<Buyer, String> colPhone;
    @FXML private TableColumn<Buyer, String> colCard;

    private final ObservableList<Buyer> buyers = FXCollections.observableArrayList();
    private int nextId = 1;

    @FXML
    private void initialize() {
        colLastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        colFirstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        colPhone.setCellValueFactory(new PropertyValueFactory<>("phone"));
        colCard.setCellValueFactory(new PropertyValueFactory<>("cardNumber"));

        buyersTable.setItems(buyers);

        ContextMenu contextMenu = new ContextMenu();
        MenuItem editItem = new MenuItem("Редактировать");
        MenuItem deleteItem = new MenuItem("Удалить");
        MenuItem duplicateItem = new MenuItem("Дублировать");

        editItem.setOnAction(e -> editBuyer(buyersTable.getSelectionModel().getSelectedItem()));
        deleteItem.setOnAction(e -> deleteBuyer(buyersTable.getSelectionModel().getSelectedItem()));
        duplicateItem.setOnAction(e -> duplicateBuyer(buyersTable.getSelectionModel().getSelectedItem()));

        contextMenu.getItems().addAll(editItem, deleteItem, duplicateItem);

        buyersTable.setRowFactory(tv -> {
            TableRow<Buyer> row = new TableRow<>();
            row.setOnContextMenuRequested(event -> {
                if (!row.isEmpty()) {
                    buyersTable.getSelectionModel().select(row.getItem());
                    contextMenu.show(row, event.getScreenX(), event.getScreenY());
                }
            });
            return row;
        });
    }

    @FXML
    private void onAdd() {
        Buyer created = showForm(null);
        if (created != null) {
            Buyer withId = new Buyer(nextId++, created.getLastName(), created.getFirstName(),
                    created.getPhone(), created.getCardNumber());
            buyers.add(withId);
        }
    }

    private void editBuyer(Buyer buyer) {
        if (buyer == null) return;
        Buyer updated = showForm(buyer);
        if (updated != null) {
            buyer.setLastName(updated.getLastName());
            buyer.setFirstName(updated.getFirstName());
            buyer.setPhone(updated.getPhone());
            buyer.setCardNumber(updated.getCardNumber());
            buyersTable.refresh();
        }
    }

    private void deleteBuyer(Buyer buyer) {
        if (buyer == null) return;
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                "Удалить покупателя " + buyer.getLastName() + " " + buyer.getFirstName() + "?",
                ButtonType.YES, ButtonType.NO);
        alert.showAndWait().ifPresent(bt -> {
            if (bt == ButtonType.YES) buyers.remove(buyer);
        });
    }

    private void duplicateBuyer(Buyer buyer) {
        if (buyer == null) return;
        Buyer copy = new Buyer(nextId++, buyer.getLastName(), buyer.getFirstName(),
                buyer.getPhone(), buyer.getCardNumber());
        buyers.add(copy);
    }

    private Buyer showForm(Buyer existing) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/buyer_form.fxml"));
            Stage dialog = new Stage();
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.setTitle(existing == null ? "Новый покупатель" : "Редактирование покупателя");
            dialog.setScene(new Scene(loader.load()));

            BuyerFormController ctrl = loader.getController();
            if (existing != null) ctrl.setBuyer(existing);
            dialog.showAndWait();

            return ctrl.getResult();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public ObservableList<Buyer> getBuyers() {
        return buyers;
    }
}