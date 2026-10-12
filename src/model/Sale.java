package model;

import java.time.LocalDate;

public class Sale {
    private int id;
    private Buyer buyer;
    private Medicine medicine;
    private int quantity;
    private LocalDate date;
    private boolean recipePresented;

    public Sale(int id, Buyer buyer, Medicine medicine, int quantity,
                LocalDate date, boolean recipePresented) {
        this.id = id;
        this.buyer = buyer;
        this.medicine = medicine;
        this.quantity = quantity;
        this.date = date;
        this.recipePresented = recipePresented;
    }

    public int getId() { return id; }
    public Buyer getBuyer() { return buyer; }
    public Medicine getMedicine() { return medicine; }
    public int getQuantity() { return quantity; }
    public LocalDate getDate() { return date; }
    public boolean isRecipePresented() { return recipePresented; }

    public void setBuyer(Buyer v) { this.buyer = v; }
    public void setMedicine(Medicine v) { this.medicine = v; }
    public void setQuantity(int v) { this.quantity = v; }
    public void setDate(LocalDate v) { this.date = v; }
    public void setRecipePresented(boolean v) { this.recipePresented = v; }

    // Сумма = Цена × Количество × 0.95
    public double getSum() {
        return medicine.getPrice() * quantity * 0.95;
    }
}