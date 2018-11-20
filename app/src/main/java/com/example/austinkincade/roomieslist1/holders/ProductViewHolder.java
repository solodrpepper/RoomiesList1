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
import com.example.austinkincade.roomieslist1.models.ProductModel;
import com.example.austinkincade.roomieslist1.models.ShoppingListModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class ProductViewHolder extends RecyclerView.ViewHolder {
    private TextView productNameTextView;

    public ProductViewHolder(@NonNull View itemView) {
        super(itemView);
        // connect the text view objects to the layout
        productNameTextView = itemView.findViewById(R.id.product_name_text_view);
    }

    public void setProduct(final Context context, final View shoppingListViewFragment, final String userEmail, final ShoppingListModel shoppingListModel, ProductModel productModel) {
        final String shoppingListName = shoppingListModel.getShoppingListName();
        final String shoppingListId   = shoppingListModel.getShoppingListId();
        final String productId = productModel.getProductId();
        final String productName      = productModel.getProductName();
        final Boolean izInShoppingList = productModel.getIzInShoppingList();
        productNameTextView.setText(productName);

        FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
        final DocumentReference productIdRef = rootRef.collection("products").document(shoppingListId)
                .collection("shoppingListProducts").document(productId);

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
                            // Send notification
                        }
                    }
                });
            }
        });

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
