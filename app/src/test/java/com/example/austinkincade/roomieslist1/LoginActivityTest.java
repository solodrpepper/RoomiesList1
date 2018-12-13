package com.example.austinkincade.roomieslist1;

import com.example.austinkincade.roomieslist1.models.ProductModel;
import com.example.austinkincade.roomieslist1.models.ShoppingListModel;
import com.example.austinkincade.roomieslist1.models.UserModel;

import org.junit.Test;

import static org.junit.Assert.*;

public class LoginActivityTest {

    // Creates 2 shoppers
    @Test
    public void Shopper_isCreated() {
        UserModel user1   = new UserModel("jak19001@byui.edu", "987654321", "user1");
        String user1Email = user1.getUserEmail();
        String tokenId1   = user1.getTokenID();
        String user1Name  = user1.getUserName();
        assertNotNull(user1Email);
        assertNotNull(tokenId1);
        assertNotNull(user1Name);
        assertNotNull(user1);
        UserModel user2   = new UserModel("ang20005@byui.edu", "123456789", "user2");
        String user2Email = user1.getUserEmail();
        String tokenId2   = user1.getTokenID();
        String user2Name  = user1.getUserName();
        assertNotNull(user2Email);
        assertNotNull(tokenId2);
        assertNotNull(user2Name);
        assertNotNull(user2);
    }

    // One shopper creates a shopping list
    @Test
    public void ShoppingList_isCreated() {
        ShoppingListModel s = new ShoppingListModel("001", "walmart", "user1");
        String shoppingListId   = s.getShoppingListId();
        String shoppingListName = s.getShoppingListName();
        String createdBy        = s.getCreatedBy();
        assertNotNull(shoppingListId);
        assertNotNull(shoppingListName);
        assertNotNull(createdBy);
        assertNotNull(s);
    }

    // The same shopper creates a product
    @Test
    public void Product_isCreated() {
        ProductModel p = new ProductModel("001", "apple", true);
        String productId         = p.getProductId();
        String productName       = p.getProductName();
        Boolean isInShoppingList = p.getIzInShoppingList();
        assertNotNull(productId);
        assertNotNull(productName);
        assertNotNull(isInShoppingList);
        assertNotNull(p);
    }

    // Shares the list with the 2nd shopper
//    @Test
//    public void ShoppingList_isShared() {
//
//    }
}