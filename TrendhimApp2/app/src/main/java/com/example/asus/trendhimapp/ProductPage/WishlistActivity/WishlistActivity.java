package com.example.asus.trendhimapp.ProductPage.WishlistActivity;

import android.content.Context;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;

import com.example.asus.trendhimapp.MainActivities.BaseActivity;
import com.example.asus.trendhimapp.ProductPage.Product;
import com.example.asus.trendhimapp.R;
import com.example.asus.trendhimapp.Util.Constants;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class WishlistActivity extends BaseActivity {
    private WishlistAdapter wishlistAdapter;
    private List<WishlistProduct> wishList;
    private List<Product> productList;
    private String userEmail;
    private RecyclerView recyclerView;

//    private String banner = "https://firebasestorage.googleapis.com/v0/b/trendhim-31939.appspot.com/o/bracelet_pictures%2Fbracelet2%2Fbracelet2_banner-min.PNG?alt=media&token=4f297965-63a9-4374-9c1e-bb74ef359456";
//    private String left = "https://firebasestorage.googleapis.com/v0/b/trendhim-31939.appspot.com/o/bracelet_pictures%2Fbracelet2%2Fbracelet2_left-min.PNG?alt=media&token=588a6805-e61a-42c1-a819-8d8cb69c18ed";
//    private String right = "https://firebasestorage.googleapis.com/v0/b/trendhim-31939.appspot.com/o/bracelet_pictures%2Fbracelet2%2Fbracelet2_right-min.PNG?alt=media&token=669a6e85-55fe-4690-bd4e-e3aee97e065a";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_wishlist, null, false);
        BaseActivity.drawer.addView(contentView, 0);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        setTitle(getString(R.string.wishlist_activity_title));
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setNestedScrollingEnabled(true);
        setEmail();
        wishList = new ArrayList<>();
        productList = new ArrayList<>();
//        Product product = new Product("Key", "Test product", banner, left, right, "Trendhim", 250);
//        Product product1 = new Product("Key", "Test product", banner, left, right, "Trendhim", 250);
//        Product product2 = new Product("Key", "Test product", banner, left, right, "Trendhim", 250);
//        productList.add(product);
//        productList.add(product1);
//        productList.add(product2);
//        productList.add(product);
//        productList.add(product1);
//        productList.add(product2);
//        productList.add(product);
//        productList.add(product1);
//        productList.add(product2);
        wishlistAdapter = new WishlistAdapter(this, productList, userEmail);
        recyclerView.setAdapter(wishlistAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        getWishList();
        getProductList();
    }

    private void setEmail() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            this.userEmail = user.getEmail();
        } else {
            Log.d("WishAct: setEmail", "null email");
        }
    }

    /**
     * Get all wishlist products of the user
     **/
    private void getWishList() {
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference(Constants.TABLE_NAME_WISHLIST);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String email = user.getEmail();
            Log.d(getLocalClassName(), email);
            Query query = myRef.orderByChild("userEmail").equalTo(email);
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            WishlistProduct wishProd = ds.getValue(WishlistProduct.class);
                            Log.d(getLocalClassName(), wishProd.toString());
                            wishList.add(wishProd);
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {}
            });
        }
    }

    /**
     * Get the real products
     **/
    private void getProductList() {
        for (int i = 0; i < wishList.size(); i++) {
            final WishlistProduct wishProd = wishList.get(i);
            final String entityName = wishProd.getEntityName(), productKey = wishProd.getProductKey();
            DatabaseReference myRef = FirebaseDatabase.getInstance().getReference(entityName);
            myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            String currentProductKey = ds.getKey();
                            if (productKey.equals(currentProductKey)) {
                                Product product = ds.getValue(Product.class);
                                Log.d(getLocalClassName(), product.toString());
                                productList.add(product);
                                wishlistAdapter.notifyDataSetChanged();
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {}
            });

        }
    }
}
