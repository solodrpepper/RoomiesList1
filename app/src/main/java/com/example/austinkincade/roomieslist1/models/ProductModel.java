package com.example.austinkincade.roomieslist1.models;

/**
 * Holds the definition of a product.
 *
 * @version     1.0     (current version number of program)
 * @since       1.0     (the version of the package this class was first added to)
 */
public class ProductModel {
    private String productId, productName;
    private Boolean izInShoppingList;

    public ProductModel() {}

    public ProductModel(String productId, String productName, Boolean izInShoppingList) {
        this.productId = productId;
        this.productName = productName;
        this.izInShoppingList = izInShoppingList;
    }

    public String getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public Boolean getIzInShoppingList() {
        return izInShoppingList;
    }
}
