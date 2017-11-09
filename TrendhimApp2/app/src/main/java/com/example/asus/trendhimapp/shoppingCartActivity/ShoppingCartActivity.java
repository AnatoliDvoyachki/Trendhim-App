package com.example.asus.trendhimapp.shoppingCartActivity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.example.asus.trendhimapp.R;
import com.example.asus.trendhimapp.categoryPage.CategoryProduct;
import com.example.asus.trendhimapp.mainActivities.BaseActivity;

import java.util.ArrayList;
import java.util.List;

public class ShoppingCartActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_shopping_cart, null, false);
        BaseActivity.drawer.addView(contentView, 0);
        getSupportActionBar().setDisplayShowTitleEnabled(true);

        setTitle("Shopping Cart");
        initializeComponents();

    }

    private void initializeComponents() {
        RecyclerView recyclerView = findViewById(R.id.the_recycler_view);
        List<CategoryProduct> shoppingCartProducts = new ArrayList<>();
        ShoppingCartAdapter sca = new ShoppingCartAdapter(this, shoppingCartProducts);
        recyclerView.setAdapter(sca);
        sca.populateRecyclerView();

        // Set layout manager to position the items
        recyclerView.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false));
    }

}
