package com.example.android.inventoryapp.data;

/**
 * InventoryApp
 * Created by Thomas Schmidt on 09.04.2018.
 */
public class Product {

    private int _id;
    private String productname;
    private int price;
    private int quantity;
    private int variant;
    private String suppliername;
    private String supplierphonenr;


    /**
     * Full Constructor
     *
     * @param _id             of the product
     * @param productname     name of the product
     * @param price           of the product
     * @param quantity        of the product
     * @param variant         product variant NO - XL
     * @param suppliername    name of the supplier
     * @param supplierphonenr phone number of the supplier
     */
    public Product(int _id, String productname, int price, int quantity, int variant, String suppliername, String supplierphonenr) {
        this._id = _id;
        this.productname = productname;
        this.price = price;
        this.quantity = quantity;
        this.variant = variant;
        this.suppliername = suppliername;
        this.supplierphonenr = supplierphonenr;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getProductname() {
        return productname;
    }

    public void setProductname(String productname) {
        this.productname = productname;
    }

    public int getPrice() {
        return price;
    }

    public double getFormattedPrice() {

        return price / 100;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getVariant() {
        return variant;
    }

    public void setVariant(int variant) {
        this.variant = variant;
    }

    public String getSuppliername() {
        return suppliername;
    }

    public void setSuppliername(String suppliername) {
        this.suppliername = suppliername;
    }

    public String getSupplierphonenr() {
        return supplierphonenr;
    }

    public void setSupplierphonenr(String supplierphonenr) {
        this.supplierphonenr = supplierphonenr;
    }
}
