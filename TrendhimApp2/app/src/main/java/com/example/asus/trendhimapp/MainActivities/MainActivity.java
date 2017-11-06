package com.example.asus.trendhimapp.MainActivities;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.asus.trendhimapp.categoryPage.CategoryProduct;
import com.example.asus.trendhimapp.MainActivities.RecentProducts.RecentProductsAdapter;
import com.example.asus.trendhimapp.R;

import java.util.ArrayList;

public class MainActivity extends BaseActivity {

    ArrayList<CategoryProduct> recentProducts;
    public static RecentProductsAdapter adapter;
    public static TextView noRecentProducts;
    ImageView recentProductImage, recommendedProductsImage;
    int i = 0, i1= 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.content_main, null, false);
        BaseActivity.drawer.addView(contentView, 0);

        final LinearLayout recentProductsLayout = findViewById(R.id.recentProductsLayout);

        recentProductImage = findViewById(R.id.recentproductsImage);

        recentProductImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(i % 2 == 0)
                    recentProductsLayout.setVisibility(View.VISIBLE);
                if(i % 2 != 0)
                    recentProductsLayout.setVisibility(View.GONE);
                i++;
            }
        });

        noRecentProducts = findViewById(R.id.noRecentProducts);

        final LinearLayout recommendedProductsLayout = findViewById(R.id.recommendedProductsLayout);

        recommendedProductsImage = findViewById(R.id.recommendedProductsImage);
        recommendedProductsImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(i1 % 2 == 0)
                    recommendedProductsLayout.setVisibility(View.VISIBLE);
                if(i1 % 2 != 0)
                    recommendedProductsLayout.setVisibility(View.GONE);
                i1++;
            }
        });

        // Lookup the recycler view in activity layout
        RecyclerView recyclerView = findViewById(R.id.recyclerViewmain);

        // Initialize categories
        recentProducts = new ArrayList<>();

        // Create adapter for the categories
        adapter = new RecentProductsAdapter(this, recentProducts);

        // Attach the adapter to the recycler view
        recyclerView.setAdapter(adapter);

        //Populate the recycler view
        adapter.addData();

        SnapHelper helper = new LinearSnapHelper();
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        helper.attachToRecyclerView(recyclerView);

    }

}

