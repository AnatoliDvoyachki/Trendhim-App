package com.example.asus.trendhimapp.mainActivities;

import android.content.Context;
import android.content.Intent;
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

import com.example.asus.trendhimapp.R;
import com.example.asus.trendhimapp.categoryPage.CategoryProduct;
import com.example.asus.trendhimapp.mainActivities.newProducts.NewProductsAdapter;
import com.example.asus.trendhimapp.mainActivities.recentProducts.RecentProductsAdapter;
import com.example.asus.trendhimapp.weeklyLook.WeeklyLookActivity;

import java.util.ArrayList;

public class MainActivity extends BaseActivity {

    ArrayList<CategoryProduct> recentProducts, recommendedProducts;
    public RecentProductsAdapter adapter;
    public static TextView noRecentProducts;
    ImageView recentProductImage, recommendedProductsImage;
    int i = 0, i1= 0;
    public NewProductsAdapter newProductAdapter;
    public ImageView weeklyLookImage;

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

        initializeRecentProductRecyclerView();
        initializeRecommendedProductsRecyclerView();
    }

    void initializeRecentProductRecyclerView(){
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

    void initializeRecommendedProductsRecyclerView() {

        // Lookup the recycler view in the activity layout (new)
        RecyclerView recyclerViewNewProducts = findViewById(R.id.rvRecommendProducts);

        //Recommended products initialize
        recommendedProducts = new ArrayList<>();

        // Create a new adapter for the category
        newProductAdapter = new NewProductsAdapter(this, recommendedProducts);

        // Attach the adapter to the recycler view
        recyclerViewNewProducts.setAdapter(newProductAdapter);

        //Populate the recycler view
        newProductAdapter.addData();

        SnapHelper helperNew = new LinearSnapHelper();
        LinearLayoutManager layoutManagerNew = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerViewNewProducts.setLayoutManager(layoutManagerNew);

        helperNew.attachToRecyclerView(recyclerViewNewProducts);

        weeklyLookImage = findViewById(R.id.weeklyLookImage);

        weeklyLookImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), WeeklyLookActivity.class);
                startActivity(intent);
            }
        });

    }

}

