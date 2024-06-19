package hieu.tlus.appfruitnguyenlehoaihieu.model;

import java.io.Serializable;

public class Fruit implements Serializable {
    private String fruitid;
    private String name;
    private int quantity;
    private float price;
    private String supplier;
    private String purchaseDate;
    private String anhfruit;

    public Fruit(String fruitid, String name, int quantity, float price, String supplier, String purchaseDate, String anhfruit) {
        this.fruitid = fruitid;
        this.name = name;
        this.quantity = quantity;
        this.price = price;
        this.supplier = supplier;
        this.purchaseDate = purchaseDate;
        this.anhfruit = anhfruit;
    }

    public String getFruitid() {
        return fruitid;
    }

    public void setFruitid(String fruitid) {
        this.fruitid = fruitid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getSupplier() {
        return supplier;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }

    public String getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(String purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public String getAnhfruit() {
        return anhfruit;
    }

    public void setAnhfruit(String anhfruit) {
        this.anhfruit = anhfruit;
    }
}
