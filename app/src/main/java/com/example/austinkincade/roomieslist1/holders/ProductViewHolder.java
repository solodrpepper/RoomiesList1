package com.example.austinkincade.roomieslist1.holders;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.austinkincade.roomieslist1.R;
import com.example.austinkincade.roomieslist1.ShoppingListActivity;
import com.example.austinkincade.roomieslist1.models.ProductModel;
import com.example.austinkincade.roomieslist1.models.ShoppingListModel;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class ProductViewHolder extends RecyclerView.ViewHolder {
    private TextView productNameTextView;

    public ProductViewHolder(@NonNull View itemView) {
        super(itemView);
        // connect the text view objects to the layout
        productNameTextView = itemView.findViewById(R.id.product_name_text_view);
    }

    public void setProduct(final Context context, final String userEmail, final ShoppingListModel shoppingListModel, ProductModel productModel) {
        final String shoppingListName = shoppingListModel.getShoppingListName();
        final String shoppingListId   = shoppingListModel.getShoppingListId();
        final String productName      = productModel.getProductName();
        productNameTextView.setText(productName);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // move product
            }
        });

        itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
//                AlertDialog.Builder builder = new AlertDialog.Builder(context);
//                builder.setTitle("Edit Shopping List Name");
//
//                final EditText editText = new EditText(context);
//                editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_WORDS);
//                editText.setText(shoppingListName);
//                editText.setSelection(editText.getText().length());
//                editText.setHintTextColor(Color.GRAY);
//
//                builder.setView(editText);
//
//                // grab firestore object to modify the name
//                final FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
//                final Map<String, Object> map = new HashMap<>();
//
//                builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        String newShoppingListName = editText.getText().toString().trim();
//                        map.put("shoppingListName", newShoppingListName);
//                        rootRef.collection("shoppingLists").document(userEmail).collection("userShoppingLists").document(shoppingListId).update(map);
//                    }
//                });
//
//                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                    }
//                });
//
//                AlertDialog alertDialog = builder.create();
//                alertDialog.show();
                return true;
            }
        });
    }
}
