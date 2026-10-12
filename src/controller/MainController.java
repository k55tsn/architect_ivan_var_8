package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class MainController {

    @FXML private Label statusLabel;
    @FXML private TabPane tabPane;

    // Таблицы
    @FXML private TableView<?> salesTable;
    @FXML private TableView<?> buyersTable;
    @FXML private TableView<?> medicinesTable;

    // Колонки продаж
    @FXML private TableColumn<?, ?> colSalesId;
    @FXML private TableColumn<?, ?> colSalesBuyer;
    @FXML private TableColumn<?, ?> colSalesMedicine;
    @FXML private TableColumn<?, ?> colSalesQuantity;
    @FXML private TableColumn<?, ?> colSalesDate;
    @FXML private TableColumn<?, ?> colSalesRecipe;
    @FXML private TableColumn<?, ?> colSalesSum;
    @FXML private TableColumn<?, ?> colSalesDeviation;

    // Колонки покупателей
    @FXML private TableColumn<?, ?> colBuyerLastName;
    @FXML private TableColumn<?, ?> colBuyerFirstName;
    @FXML private TableColumn<?, ?> colBuyerPhone;
    @FXML private TableColumn<?, ?> colBuyerCard;

    // Колонки препаратов
    @FXML private TableColumn<?, ?> colMedName;
    @FXML private TableColumn<?, ?> colMedCategory;
    @FXML private TableColumn<?, ?> colMedPrice;
    @FXML private TableColumn<?, ?> colMedRecipe;

    @FXML
    private void initialize() {
        statusLabel.setText("Не авторизован");
        tabPane.getSelectionModel().select(0); // активна вкладка "Продажи"
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