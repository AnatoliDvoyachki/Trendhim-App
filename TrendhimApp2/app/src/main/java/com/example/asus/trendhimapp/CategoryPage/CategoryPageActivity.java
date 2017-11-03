package com.example.asus.trendhimapp.CategoryPage;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.example.asus.trendhimapp.MainActivities.BaseActivity;
import com.example.asus.trendhimapp.R;

import java.util.ArrayList;

public class CategoryPageActivity extends BaseActivity {

    ArrayList<CategoryPage> categories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_category_page, null, false);
        BaseActivity.drawer.addView(contentView, 0);
        getSupportActionBar().setDisplayShowTitleEnabled(true);

        setTitle("Category Page");
        // Lookup the recycler view in activity layout
        RecyclerView recyclerView = findViewById(R.id.recyclerViewCategory_page);

        // Initialize categories
        categories = CategoryPage.createCategoryList(50);

        // Create adapter passing in the sample user data
        CategoryAdapter adapter = new CategoryAdapter(this, categories);

        // Attach the adapter to the recycler view to populate items
        recyclerView.setAdapter(adapter);

        // Set layout manager to position the items
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2,
                GridLayoutManager.VERTICAL, false));

    }
}
