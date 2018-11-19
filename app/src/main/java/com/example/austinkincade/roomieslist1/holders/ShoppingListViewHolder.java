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
import com.example.austinkincade.roomieslist1.models.ShoppingListModel;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class ShoppingListViewHolder extends RecyclerView.ViewHolder {
    private TextView shoppingListNameTextView, createdByTextView, dateTextView;

    public ShoppingListViewHolder(@NonNull View itemView) {
        super(itemView);
        // connect the text view objects to the layout
        shoppingListNameTextView = itemView.findViewById(R.id.shopping_list_name_text_view);
        createdByTextView = itemView.findViewById(R.id.created_by_text_view);
        dateTextView = itemView.findViewById(R.id.date_text_view);
    }

    public void setShoppingList(final Context context, final String userEmail, final ShoppingListModel shoppingListModel) {
        final String shoppingListName = shoppingListModel.getShoppingListName();
        final String shoppingListId   = shoppingListModel.getShoppingListId();
        shoppingListNameTextView.setText(shoppingListName);

        String createdBy = shoppingListModel.getCreatedBy();
        createdByTextView.setText(createdBy);

        Date date = shoppingListModel.getDate();
        if (date != null) {
            DateFormat dateFormat = SimpleDateFormat.getDateInstance(DateFormat.MEDIUM, Locale.US);
            String shoppingListCreationDate = dateFormat.format(date);
            dateTextView.setText(shoppingListCreationDate);
        }

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ShoppingListActivity.class);
                intent.putExtra("shoppingListModel", shoppingListModel);
                v.getContext().startActivity(intent);
            }
        });

        itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Edit Shopping List Name");

                final EditText editText = new EditText(context);
                editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_WORDS);
                editText.setText(shoppingListName);
                editText.setSelection(editText.getText().length());
                editText.setHintTextColor(Color.GRAY);

                builder.setView(editText);

                // grab firestore object to modify the name
                final FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
                final Map<String, Object> map = new HashMap<>();

                builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String newShoppingListName = editText.getText().toString().trim();
                        map.put("shoppingListName", newShoppingListName);
                        rootRef.collection("shoppingLists").document(userEmail).collection("userShoppingLists").document(shoppingListId).update(map);
                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                AlertDialog alertDialog = builder.create();
                alertDialog.show();
                return true;
            }
        });
    }
}
