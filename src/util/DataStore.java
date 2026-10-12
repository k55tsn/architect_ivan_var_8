package util;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Buyer;
import model.Medicine;
import model.Sale;

public class DataStore {

    private static final DataStore INSTANCE = new DataStore();

    public static DataStore getInstance() {
        return INSTANCE;
    }

    private final ObservableList<Buyer> buyers = FXCollections.observableArrayList();
    private final ObservableList<Medicine> medicines = FXCollections.observableArrayList();
    private final ObservableList<Sale> sales = FXCollections.observableArrayList();

    private int nextBuyerId = 1;
    private int nextMedicineId = 1;
    private int nextSaleId = 1;

    private DataStore() {}

    public ObservableList<Buyer> getBuyers() { return buyers; }
    public ObservableList<Medicine> getMedicines() { return medicines; }
    public ObservableList<Sale> getSales() { return sales; }

    public int nextBuyerId() { return nextBuyerId++; }
    public int nextMedicineId() { return nextMedicineId++; }
    public int nextSaleId() { return nextSaleId++; }
}