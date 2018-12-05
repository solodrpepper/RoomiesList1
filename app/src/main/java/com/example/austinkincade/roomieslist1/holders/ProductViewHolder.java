package com.example.austinkincade.roomieslist1.holders;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.austinkincade.roomieslist1.R;
import com.example.austinkincade.roomieslist1.models.NotificationModel;
import com.example.austinkincade.roomieslist1.models.ProductModel;
import com.example.austinkincade.roomieslist1.models.ShoppingListModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

/**
 * To display products in shopping lists.
 *
 * @version     1.0     (current version number of program)
 * @since       1.0     (the version of the package this class was first added to)
 */
public class ProductViewHolder extends RecyclerView.ViewHolder {
    private TextView productNameTextView;

    public ProductViewHolder(@NonNull View itemView) {
        super(itemView);
        // connect the text view objects to the layout
        productNameTextView = itemView.findViewById(R.id.product_name_text_view);
    }

    public void setProduct(final Context context, final View shoppingListViewFragment, final String userEmail, final String userName, final ShoppingListModel shoppingListModel, ProductModel productModel) {
        /**
         *  To make an actual update into the database.
         */
        final String shoppingListId   = shoppingListModel.getShoppingListId();

        final String shoppingListName = shoppingListModel.getShoppingListName();
        final String productId = productModel.getProductId();
        final String productName      = productModel.getProductName();
        final Boolean izInShoppingList = productModel.getIzInShoppingList();
        productNameTextView.setText(productName);

        final FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
        final DocumentReference productIdRef = rootRef.collection("products").document(shoppingListId)
                .collection("shoppingListProducts").document(productId);

        /**
         *  To open a new activity every time we use a click on a particular shopping list.
         */
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // move product between shopping and product list
                Map<String, Object> map = new HashMap<>();
                if (izInShoppingList) {
                    map.put("izInShoppingList", false);
                } else {
                    map.put("izInShoppingList", true);
                }
                // now we need to update the database
                productIdRef.update(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        if (izInShoppingList) {
                            rootRef.collection("shoppingLists").document(userEmail)
                                    .collection("userShoppingLists").document(shoppingListId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                                    Map<String, Object> map1 = (Map<String, Object>) task.getResult().get("users");
//                                    String notificationMessage = userName + " just bought " + productName + " from " +
//                                            shoppingListName + "'s list!";
//
//                                    NotificationModel notification = new NotificationModel(notificationMessage, userEmail);
//
//                                    // loop through all the people the list is shared to
//                                    for (Map.Entry<String, Object> entry : map1.entrySet()) {
//                                        String sharedUserEmail = entry.getKey();
//
//                                        // don't want to send a notification to ourselves!
//                                        if (!sharedUserEmail.equals(userEmail)) {
//                                            rootRef.collection("notifications").document(sharedUserEmail)
//                                                    .collection("userNotifications").document()
//                                                    .set(notification);
//                                        }
//                                    }
                                }
                            });
                        }
                    }
                });
            }
        });

        /**
         *  To update a shopping list.
         */
        itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Edit / Delete Item");

                final EditText editText = new EditText(context);
                editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_WORDS);
                editText.setText(productName);
                editText.setSelection(editText.getText().length());
                editText.setHintTextColor(Color.GRAY);

                builder.setView(editText);

                builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String newProductName = editText.getText().toString().trim();
                        final Map<String, Object> map = new HashMap<>();
                        map.put("productName", newProductName);
                        // update Firebase with new product name
                        productIdRef.update(map);
                    }
                });

                builder.setNegativeButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        productIdRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Snackbar.make(shoppingListViewFragment, "Product deleted!", Snackbar.LENGTH_LONG).show();
                            }
                        });
                    }
                });

                AlertDialog alertDialog = builder.create();
                alertDialog.show();
                return true;
            }
        });
    }
}
