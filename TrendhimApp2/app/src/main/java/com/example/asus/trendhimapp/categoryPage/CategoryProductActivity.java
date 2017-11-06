package com.example.asus.trendhimapp.categoryPage;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.example.asus.trendhimapp.mainActivities.BaseActivity;
import com.example.asus.trendhimapp.R;

import java.util.ArrayList;

import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;

public class CategoryProductActivity extends BaseActivity {

    ArrayList<CategoryProduct> categoryProducts;
    CategoryAdapter adapter;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_category_page, null, false);
        BaseActivity.drawer.addView(contentView, 0);
        getSupportActionBar().setDisplayShowTitleEnabled(true);

        setTitle(setTitle());

        // Lookup the recycler view in activity layout
        recyclerView = findViewById(R.id.recyclerViewCategory_page);

        //Define animators to change the behavior of the RecyclerView
        recyclerView.setItemAnimator(new SlideInUpAnimator());

        // Initialize categories
        categoryProducts = new ArrayList<>();

        // Create adapter for the categories
        adapter = new CategoryAdapter(this, categoryProducts, getCategory());

        // Attach the adapter to the recycler view
        recyclerView.setAdapter(adapter);

        //Populate the recycler view
        adapter.addData(getCategory());

        // Set layout manager to position the items
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2,
                GridLayoutManager.VERTICAL, false));

    }

    /**
     * Get category from the Navigation Bar - Used for the database queries
     * @return category title
     */
    public String getCategory(){
        Intent intent = getIntent();
        if(intent != null){
            String category = intent.getStringExtra("category");
            if(category != null)
                return category;
        }
        return null;
    }

    /**
     * Set Activity tile depending on the Category
     * @return category title
     */
    public String setTitle(){
        String category = null;
        switch (getCategory()){
            case "bags":
                category = "Bags";
                break;
            case "bracelets":
                category = "Bracelets";
                break;
            case  "ties":
                category = "Ties";
                break;
            case "bow_ties":
                category = "Bow Ties";
                break;
            case "beard_care":
                category = "Beard Care";
                break;
            case "necklaces":
                category = "Necklaces";
                break;
            case "watches":
                category = "Watches";
        }
        return category;
    }
}
