package model;

public class Buyer {
    private int id;
    private String lastName;
    private String firstName;
    private String phone;
    private String cardNumber;

    public Buyer(int id, String lastName, String firstName, String phone, String cardNumber) {
        this.id = id;
        this.lastName = lastName;
        this.firstName = firstName;
        this.phone = phone;
        this.cardNumber = cardNumber;
    }

    public int getId() { return id; }
    public String getLastName() { return lastName; }
    public String getFirstName() { return firstName; }
    public String getPhone() { return phone; }
    public String getCardNumber() { return cardNumber; }

    public void setLastName(String v) { this.lastName = v; }
    public void setFirstName(String v) { this.firstName = v; }
    public void setPhone(String v) { this.phone = v; }
    public void setCardNumber(String v) { this.cardNumber = v; }

    @Override
    public String toString() {
        return lastName + " " + firstName;
    }
}