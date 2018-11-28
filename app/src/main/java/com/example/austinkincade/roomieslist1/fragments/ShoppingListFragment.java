package com.example.austinkincade.roomieslist1.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.austinkincade.roomieslist1.MainActivity;
import com.example.austinkincade.roomieslist1.R;
import com.example.austinkincade.roomieslist1.ShoppingListActivity;
import com.example.austinkincade.roomieslist1.holders.ProductViewHolder;
import com.example.austinkincade.roomieslist1.holders.ShoppingListViewHolder;
import com.example.austinkincade.roomieslist1.models.ProductModel;
import com.example.austinkincade.roomieslist1.models.ShoppingListModel;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class ShoppingListFragment extends Fragment {
    private String shoppingListId;
    private FirebaseFirestore rootRef;
    private CollectionReference shoppingListProductsRef;
    private Boolean izInShoppingList;
    private String userEmail, userName;
    private GoogleApiClient googleApiClient;
    FirestoreRecyclerAdapter<ProductModel, ProductViewHolder> firestoreRecyclerAdapter;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View shoppingListViewFragment = inflater.inflate(R.layout.fragment_shopping_list, container, false);
        Bundle bundle = getArguments();
        izInShoppingList = bundle.getBoolean("izInShoppingList");

        GoogleSignInAccount googleSignInAccount = GoogleSignIn.getLastSignedInAccount(getContext());
        if (googleSignInAccount != null) {
            userEmail = googleSignInAccount.getEmail();
            userName  = googleSignInAccount.getDisplayName();
        }

        googleApiClient = new GoogleApiClient.Builder(getContext())
                .addApi(Auth.GOOGLE_SIGN_IN_API)
                .build();


        // get the shopping list model
        final ShoppingListModel shoppingListModel = ((ShoppingListActivity) getActivity()).getShoppingListModel();
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

        // creating the recycler view, text view for empty page, and the progress bar
        final RecyclerView recyclerView = shoppingListViewFragment.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        final TextView emptyView = shoppingListViewFragment.findViewById(R.id.empty_view);
        final ProgressBar progressBar = shoppingListViewFragment.findViewById(R.id.progress_bar);

        // create a query to populate the recycler view with the user's lists
        Query query = shoppingListProductsRef.whereEqualTo("izInShoppingList", izInShoppingList)
                .orderBy("productName", Query.Direction.ASCENDING);

        FirestoreRecyclerOptions<ProductModel> firestoreRecyclerOptions = new FirestoreRecyclerOptions.Builder<ProductModel>()
                .setQuery(query, ProductModel.class)
                .build();

        firestoreRecyclerAdapter =
                new FirestoreRecyclerAdapter<ProductModel, ProductViewHolder>(firestoreRecyclerOptions) {
                    @Override
                    protected void onBindViewHolder(@NonNull ProductViewHolder holder, int position, @NonNull ProductModel model) {
                        holder.setProduct(getContext(), shoppingListViewFragment, userEmail, userName, shoppingListModel,model);
                    }

                    @NonNull
                    @Override
                    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_product, viewGroup, false);
                        return new ProductViewHolder(view);
                    }

                    @Override
                    public void onDataChanged() {
                        if (progressBar != null) {
                            progressBar.setVisibility(View.GONE);
                        }

                        if (getItemCount() == 0) {
                            recyclerView.setVisibility(View.GONE);
                            emptyView.setVisibility(View.VISIBLE);
                        } else {
                            recyclerView.setVisibility(View.VISIBLE);
                            emptyView.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public int getItemCount() {
                        return super.getItemCount();
                    }
                };
        recyclerView.setAdapter(firestoreRecyclerAdapter);

        return shoppingListViewFragment;
    }

    private void addProduct(String productName) {
        String productId = shoppingListProductsRef.document().getId();
        ProductModel productModel = new ProductModel(productId, productName, izInShoppingList);
        shoppingListProductsRef.document(productId).set(productModel);
    }

    @Override
    public void onStart() {
        super.onStart();
        googleApiClient.connect();
        firestoreRecyclerAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (googleApiClient.isConnected()) {
            googleApiClient.disconnect();
        }

        if (firestoreRecyclerAdapter != null) {
            firestoreRecyclerAdapter.stopListening();
        }
    }
}
