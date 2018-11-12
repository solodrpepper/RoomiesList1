package com.example.austinkincade.roomieslist1;

import java.util.Iterator;
import java.util.List;

public class User {

    private String userID;
    private String name;
    private List<ShoppingList> shoppingListList;

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ShoppingList> getShoppingListList() {
        return shoppingListList;
    }

    public void setShoppingListList(List<ShoppingList> shoppingListList) {
        this.shoppingListList = shoppingListList;
    }

    public void addList(ShoppingList list){
        shoppingListList.add(list);
    }

    public void removeList(ShoppingList list){
        for (Iterator<ShoppingList> iter = shoppingListList.listIterator(); iter.hasNext(); ) {
            ShoppingList a = iter.next();
            if (list.getShoppingListName() == a.getShoppingListName()) {
                iter.remove();
            }
        }

    }
}
