package com.example.austinkincade.roomieslist1.holders;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.austinkincade.roomieslist1.R;
import com.example.austinkincade.roomieslist1.models.ShoppingListModel;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ShoppingListViewHolder extends RecyclerView.ViewHolder {
    private TextView shoppingListNameTextView, createdByTextView, dateTextView;
    public ShoppingListViewHolder(@NonNull View itemView) {
        super(itemView);
        // connect the text view objects to the layout
        shoppingListNameTextView = itemView.findViewById(R.id.shopping_list_name_text_view);
        createdByTextView = itemView.findViewById(R.id.created_by_text_view);
        dateTextView = itemView.findViewById(R.id.date_text_view);
    }

    public void setShoppingList(Context context, ShoppingListModel shoppingListModel) {
        String shoppingListName = shoppingListModel.getShoppingListName();
        shoppingListNameTextView.setText(shoppingListName);

        String createdBy = shoppingListModel.getCreatedBy();
        createdByTextView.setText(createdBy);

        Date date = shoppingListModel.getDate();
        if (date != null) {
            DateFormat dateFormat = SimpleDateFormat.getDateInstance(DateFormat.MEDIUM, Locale.US);
            String shoppingListCreationDate = dateFormat.format(date);
            dateTextView.setText(shoppingListCreationDate);
        }
    }
}
