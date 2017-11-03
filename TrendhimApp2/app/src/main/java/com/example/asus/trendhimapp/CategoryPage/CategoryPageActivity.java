package com.example.asus.trendhimapp.CategoryPage;

import android.content.Context;
import android.content.Intent;
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
    CategoryAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_category_page, null, false);
        BaseActivity.drawer.addView(contentView, 0);
        getSupportActionBar().setDisplayShowTitleEnabled(true);

        setTitle(getCategory());
        // Lookup the recycler view in activity layout
        RecyclerView recyclerView = findViewById(R.id.recyclerViewCategory_page);

        // Initialize categories
        categories = new ArrayList<>();
        // Create adapter passing in the sample user data
        adapter = new CategoryAdapter(this, categories);

        // Attach the adapter to the recycler view to populate items
        recyclerView.setAdapter(adapter);
        adapter.addData(getCategory());
        // Set layout manager to position the items
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2,
                GridLayoutManager.VERTICAL, false));

    }

    String getCategory(){
        Intent intent = getIntent();
        if(intent != null){
            String category = intent.getStringExtra("category");
            if(category != null)
                return category;
        }
        return null;
    }


}
