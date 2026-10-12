package model;

public class Medicine {
    private int id;
    private String name;
    private String category;
    private double price;
    private boolean needRecipe;

    public Medicine(int id, String name, String category, double price, boolean needRecipe) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.price = price;
        this.needRecipe = needRecipe;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getCategory() { return category; }
    public double getPrice() { return price; }
    public boolean isNeedRecipe() { return needRecipe; }

    public void setName(String v) { this.name = v; }
    public void setCategory(String v) { this.category = v; }
    public void setPrice(double v) { this.price = v; }
    public void setNeedRecipe(boolean v) { this.needRecipe = v; }
}