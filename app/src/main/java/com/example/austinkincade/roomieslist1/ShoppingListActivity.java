package com.example.austinkincade.roomieslist1;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.Toast;

import com.example.austinkincade.roomieslist1.fragments.HistoryFragment;
import com.example.austinkincade.roomieslist1.fragments.ShoppingListFragment;
import com.example.austinkincade.roomieslist1.models.ShoppingListModel;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShoppingListActivity extends AppCompatActivity {
    private String userEmail;
    private GoogleApiClient googleApiClient;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore rootRef;
    private FirebaseAuth.AuthStateListener authStateListener;
    private ShoppingListModel shoppingListModel;
    private String shoppingListId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_list);

        GoogleSignInAccount googleSignInAccount = GoogleSignIn.getLastSignedInAccount(this);
        if (googleSignInAccount != null) {
            userEmail = googleSignInAccount.getEmail();
        }

        googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Auth.GOOGLE_SIGN_IN_API)
                .build();

        firebaseAuth = FirebaseAuth.getInstance();
        rootRef = FirebaseFirestore.getInstance();

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                if (firebaseUser == null) {
                    Intent intent = new Intent(ShoppingListActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
            }
        };

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        shoppingListModel = (ShoppingListModel) getIntent().getSerializableExtra("shoppingListModel");
        String shoppingListName = shoppingListModel.getShoppingListName();
        // Set activity title to the name of the user defined list
        setTitle(shoppingListName);
        // go grab the shopping list ID from the model
        shoppingListId = shoppingListModel.getShoppingListId();

        ViewPager viewPager = findViewById(R.id.view_pager);
        setupViewPager(viewPager);
        viewPager.setOffscreenPageLimit(2);

        TabLayout tabLayout = findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void signOut() {
        Map<String, Object> map = new HashMap<>();
        map.put("tokenId", FieldValue.delete());

        rootRef.collection("users").document(userEmail).update(map).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                firebaseAuth.signOut();

                if (googleApiClient.isConnected()) {
                    Auth.GoogleSignInApi.signOut(googleApiClient);
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        googleApiClient.connect();
        firebaseAuth.addAuthStateListener(authStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (googleApiClient.isConnected()) {
            googleApiClient.disconnect();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.shopping_list_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;

            case R.id.add_friend:
                shareShoppingList();
                return true;


            case R.id.sign_out:
                signOut();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void shareShoppingList() {
        AlertDialog.Builder builder = new AlertDialog.Builder(ShoppingListActivity.this);
        builder.setTitle("Share your List!");
        builder.setMessage("Enter your friend's email");

        final EditText editText = new EditText(ShoppingListActivity.this);
        editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_WORDS);
        editText.setHint("Your friend's email");
        editText.setHintTextColor(Color.GRAY);

        builder.setView(editText);
        builder.setPositiveButton("Share", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String friendEmail = editText.getText().toString().trim();
                // copy over the list to the friend's collection
                rootRef.collection("shoppingLists").document(friendEmail)
                        .collection("userShoppingLists").document(shoppingListId)
                        .set(shoppingListModel);
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

    private class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> fragmentList = new ArrayList<>();
        private final List<String> titleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            return fragmentList.get(i);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return titleList.get(position);
        }

        void addFragment (Fragment fragment, String title) {
            fragmentList.add(fragment);
            titleList.add(title);
        }
    }

    public ShoppingListModel getShoppingListModel() { return shoppingListModel; }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());

        // This is for the Shopping list fragment
        ShoppingListFragment trueFragment = new ShoppingListFragment();
        Bundle trueBundle = new Bundle();
        trueBundle.putBoolean("izInShoppingList", true);
        trueFragment.setArguments(trueBundle);

        viewPagerAdapter.addFragment(trueFragment, "SHOPPING LIST");

        // this is for the product fragment
        ShoppingListFragment falseFragment = new ShoppingListFragment();
        Bundle falseBundle = new Bundle();
        falseBundle.putBoolean("izInShoppingList", false);
        falseFragment.setArguments(falseBundle);

        viewPagerAdapter.addFragment(falseFragment, "PRODUCT LIST");

        // the is for the history fragment
        viewPagerAdapter.addFragment(new HistoryFragment(), "HISTORY");

        viewPager.setAdapter(viewPagerAdapter);
    }
}
