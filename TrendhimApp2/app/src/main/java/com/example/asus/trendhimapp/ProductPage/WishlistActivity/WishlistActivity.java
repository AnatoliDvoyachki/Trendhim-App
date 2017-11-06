package com.example.asus.trendhimapp.ProductPage.WishlistActivity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.example.asus.trendhimapp.MainActivities.BaseActivity;
import com.example.asus.trendhimapp.ProductPage.Product;
import com.example.asus.trendhimapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

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

    }

    private void setEmail() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            this.userEmail = user.getEmail();
        } else {
            Log.d("WishAct: setEmail", "null email");
        }
    }
}
