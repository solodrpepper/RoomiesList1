package com.example.austinkincade.roomieslist1;

import com.example.austinkincade.roomieslist1.models.ShoppingListModel;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class ShoppingListActivityTest {

//    @Test
//    public void onSignOutMakeSureItSignedOut(){
//        ShoppingListActivity s = new ShoppingListActivity();
//        s.getShoppingListModel();
//    }

    @Test
    public void ShoppingListModel_isReturned(){
        ShoppingListActivity s = new ShoppingListActivity();
        ShoppingListModel shopping = s.getShoppingListModel();
        assertNull(shopping);
    }


}