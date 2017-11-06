package com.example.asus.trendhimapp.wishlistPage;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.example.asus.trendhimapp.mainActivities.BaseActivity;
import com.example.asus.trendhimapp.R;

public class WishlistActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_wishlist, null, false);
        BaseActivity.drawer.addView(contentView, 0);

        getSupportActionBar().setDisplayShowTitleEnabled(true);

        setTitle(getString(R.string.wishlist_activity_title));

        RecyclerView recyclerView = findViewById(R.id.recycler_view);

        recyclerView.setNestedScrollingEnabled(true);

        WishlistAdapter wishlistAdapter = new WishlistAdapter(this);
        wishlistAdapter.populateRecyclerView();

        recyclerView.setAdapter(wishlistAdapter);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}