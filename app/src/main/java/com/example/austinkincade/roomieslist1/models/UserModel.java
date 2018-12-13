package com.example.austinkincade.roomieslist1.models;

/**
 * Holds the definition of a user.
 *
 * @version     1.0     (current version number of program)
 * @since       1.0     (the version of the package this class was first added to)
 */
public class UserModel {
    private String userEmail, tokenId, userName;
    public UserModel() {}

    public UserModel(String userEmail, String tokenId, String userName) {
        this.userEmail = userEmail;
        this.tokenId = tokenId;
        this.userName = userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public String getTokenID() {
        return tokenId;
    }

    public String getUserName() {
        return userName;
    }
}
