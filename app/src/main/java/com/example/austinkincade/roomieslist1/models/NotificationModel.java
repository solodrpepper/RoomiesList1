package com.example.austinkincade.roomieslist1.models;

/**
 * Holds the definition of a notification.
 *
 * @version     1.0     (current version number of program)
 * @since       1.0     (the version of the package this class was first added to)
 */
public class NotificationModel {
    private String notificationMessage, senderUserEmail;

    public NotificationModel() {}

    public NotificationModel(String notificationMessage, String senderUserEmail) {
        this.notificationMessage = notificationMessage;
        this.senderUserEmail = senderUserEmail;
    }

    public String getNotificationMessage() {
        return notificationMessage;
    }

    public String getSenderUserEmail() {
        return senderUserEmail;
    }
}
