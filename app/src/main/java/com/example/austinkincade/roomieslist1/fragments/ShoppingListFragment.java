package com.example.austinkincade.roomieslist1.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.austinkincade.roomieslist1.MainActivity;
import com.example.austinkincade.roomieslist1.R;
import com.example.austinkincade.roomieslist1.ShoppingListActivity;
import com.example.austinkincade.roomieslist1.models.ProductModel;
import com.example.austinkincade.roomieslist1.models.ShoppingListModel;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class ShoppingListFragment extends Fragment {
    private String shoppingListId;
    private FirebaseFirestore rootRef;
    private CollectionReference shoppingListProductsRef;
    private Boolean izInShoppingList;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View shoppingListViewFragment = inflater.inflate(R.layout.fragment_shopping_list, container, false);
        Bundle bundle = getArguments();
        izInShoppingList = bundle.getBoolean("izInShoppingList");

        // get the shopping list model
        ShoppingListModel shoppingListModel = ((ShoppingListActivity) getActivity()).getShoppingListModel();
        shoppingListId = shoppingListModel.getShoppingListId();

        // Defining the FAB
        FloatingActionButton fab = shoppingListViewFragment.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Add Product");

                final EditText editText = new EditText(getContext());
                editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_WORDS);
                editText.setHint("What do you need?");
                editText.setHintTextColor(Color.GRAY);

                builder.setView(editText);
                builder.setPositiveButton("Add Item", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String productName = editText.getText().toString().trim();
                        addProduct(productName);
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
            }
        });

        rootRef = FirebaseFirestore.getInstance();
        shoppingListProductsRef = rootRef.collection("products").document(shoppingListId).collection("shoppingListProducts");

        return shoppingListViewFragment;
    }

    private void addProduct(String productName) {
        String productId = shoppingListProductsRef.document().getId();
        ProductModel productModel = new ProductModel(productId, productName, izInShoppingList);
        shoppingListProductsRef.document(productId).set(productModel);
    }
}
