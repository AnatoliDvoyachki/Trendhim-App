package com.example.asus.trendhimapp.settings.order;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import com.example.asus.trendhimapp.R;
import com.example.asus.trendhimapp.mainActivities.BaseActivity;

import java.util.ArrayList;

import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;

public class OrderActivity extends BaseActivity {

    ArrayList<UserOrder> orders;
    OrderAdapter adapter;
    RecyclerView recyclerView;
    //access the noOrdersLayout from the order adapter
    public static RelativeLayout noOrdersLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.user_orders, null, false);

        BaseActivity.drawer.addView(contentView, 0);
        getSupportActionBar().setDisplayShowTitleEnabled(true);

        setTitle(getString(R.string.orders_placed));

        initializeComponents();
    }

    public void initializeComponents(){

        noOrdersLayout = findViewById(R.id.noOrdersLayout);

        // Lookup the recycler view in activity layout
        recyclerView = findViewById(R.id.ordersRecyclerView);

        //Define animators to change the behavior of the RecyclerView
        recyclerView.setItemAnimator(new SlideInUpAnimator());

        // Initialize categories
        orders = new ArrayList<>();

        // Create adapter for the categories
        adapter = new OrderAdapter(this, orders);

        // Attach the adapter to the recycler view
        recyclerView.setAdapter(adapter);

        //Populate the recycler view
        adapter.addData();

        // Set layout manager to position the items
        recyclerView.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false));

    }

}