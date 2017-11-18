package com.example.asus.trendhimapp.settings.order.detailedOrder;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.example.asus.trendhimapp.R;
import com.example.asus.trendhimapp.categoryPage.CategoryProduct;
import com.example.asus.trendhimapp.mainActivities.BaseActivity;

import java.util.ArrayList;

import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;

public class DetailedOrderActivity extends BaseActivity {

    ArrayList<CategoryProduct> orders;
    DetailedOrderAdapter adapter;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.detailed_order_activity, null, false);

        BaseActivity.drawer.addView(contentView, 0);
        getSupportActionBar().setDisplayShowTitleEnabled(true);

        setTitle(getString(R.string.orders_placed));

        // Lookup the recycler view in activity layout
        recyclerView = findViewById(R.id.detailedOrderRecyclerView);

        //Define animators to change the behavior of the RecyclerView
        recyclerView.setItemAnimator(new SlideInUpAnimator());

        // Initialize categories
        orders = new ArrayList<>();

        // Create adapter for the categories
        adapter = new DetailedOrderAdapter(this, orders);

        // Attach the adapter to the recycler view
        recyclerView.setAdapter(adapter);

        Intent intent = getIntent();
        if(intent != null) {
            String key = intent.getStringExtra("key");
            if(key != null) {
                //Populate the recycler view
                adapter.addData(key);
            }
        }

        // Set layout manager to position the items
        recyclerView.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false));

    }
}

