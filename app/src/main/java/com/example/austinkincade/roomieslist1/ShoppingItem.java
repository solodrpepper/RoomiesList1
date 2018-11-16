package com.example.austinkincade.roomieslist1;

import java.util.List;

public class ShoppingItem {

    private String itemName;
    private float price;
    private Boolean isChecked;

    public ShoppingItem(String itemName) {
        this.itemName = itemName;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public Boolean getChecked() {
        return isChecked;
    }

    public void setChecked(Boolean checked) {
        isChecked = checked;
    }
    @Override
    public String toString() {
        return itemName;
    }
}

