package com.example.austinkincade.roomieslist1;

import java.util.Iterator;
import java.util.List;

public class ShoppingList {

    private List<ShoppingItem> shoppingList;
    private String originalCreatorID;
    private String shoppingListName;

    public ShoppingList(String shoppingListName) {
        this.shoppingListName = shoppingListName;
    }

    public List<ShoppingItem> getShoppingList() {
        return shoppingList;
    }

    public void setShoppingList(List<ShoppingItem> shoppingList) {
        shoppingList = shoppingList;
    }

    public String getOriginalCreatorID() {
        return originalCreatorID;
    }

    public void setOriginalCreatorID(String originalCreatorID) {
        this.originalCreatorID = originalCreatorID;
    }

    public String getShoppingListName() {
        return shoppingListName;
    }

    public void setShoppingListName(String shoppingListName) {
        this.shoppingListName = shoppingListName;
    }

    public void addShoppingItem(ShoppingItem item){
        shoppingList.add(item);
    }

    public void removeShoppingItem(ShoppingItem item){
        for (Iterator<ShoppingItem> iter = shoppingList.listIterator(); iter.hasNext(); ) {
           ShoppingItem a = iter.next();
            if (item.getItemName() == a.getItemName()) {
                iter.remove();
            }
        }
    }

    @Override
    public String toString() {
        return shoppingListName;
    }

}
