package com.example.asus.trendhimapp.categoryPage;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.example.asus.trendhimapp.R;
import com.example.asus.trendhimapp.mainActivities.BaseActivity;
import com.example.asus.trendhimapp.util.Constants;

import java.util.ArrayList;

import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;

public class CategoryProductActivity extends BaseActivity {

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
        RecyclerView recyclerView = findViewById(R.id.recyclerViewCategory_page);

        //Define animators to change the behavior of the RecyclerView
        recyclerView.setItemAnimator(new SlideInUpAnimator());

        // Initialize categories
        ArrayList<CategoryProduct> categoryProducts = new ArrayList<>();

        // Create adapter for the categories
        CategoryAdapter adapter = new CategoryAdapter(this, categoryProducts, getCategory());

        // Attach the adapter to the recycler view
        recyclerView.setAdapter(adapter);

        //Populate the recycler view
        adapter.addData(getCategory());

        // Set layout manager to position the items
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2,
                GridLayoutManager.VERTICAL, false));

    }

    /**
     * Get product category from the intent - Used for the database queries
     *
     * @return category title
     */
    public String getCategory() {
        Intent intent = getIntent();
        if (intent != null) {
            String category = intent.getStringExtra(Constants.KEY_CATEGORY);
            if (category != null)
                return category;
        }
        return null;
    }

    /**
     * Set Activity tile depending on the product category
     *
     * @return category title
     */
    public String setTitle() {
        String category = null;
        switch (getCategory()) {
            case Constants.TABLE_NAME_BAGS:
                category = "Bags";
                break;
            case Constants.TABLE_NAME_BRACELETS:
                category = "Bracelets";
                break;
            case Constants.TABLE_NAME_TIES:
                category = "Ties";
                break;
            case Constants.TABLE_NAME_BOW_TIES:
                category = "Bow Ties";
                break;
            case Constants.TABLE_NAME_BEARD_CARE:
                category = "Beard Care";
                break;
            case Constants.TABLE_NAME_NECKLACES:
                category = "Necklaces";
                break;
            case Constants.TABLE_NAME_WATCHES:
                category = "Watches";
        }
        return category;
    }

}
