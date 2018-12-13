package com.example.austinkincade.roomieslist1.models;

import com.google.firebase.firestore.ServerTimestamp;

import java.io.Serializable;
import java.util.Date;

/**
 * Holds the definition of a shopping list.
 *
 * @version     1.0     (current version number of program)
 * @since       1.0     (the version of the package this class was first added to)
 */
public class ShoppingListModel implements Serializable {
    private String shoppingListId, shoppingListName, createdBy;

    // this makes sure that the time is consistent with the database
    @ServerTimestamp
    private Date date;
    public ShoppingListModel() {}

    public ShoppingListModel(String shoppingListId, String shoppingListName, String createdBy) {
        this.shoppingListId = shoppingListId;
        this.shoppingListName = shoppingListName;
        this.createdBy = createdBy;
    }

    public String getShoppingListId() {
        return shoppingListId;
    }

    public String getShoppingListName() {
        return shoppingListName;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public Date getDate() {
        return date;
    }
}
