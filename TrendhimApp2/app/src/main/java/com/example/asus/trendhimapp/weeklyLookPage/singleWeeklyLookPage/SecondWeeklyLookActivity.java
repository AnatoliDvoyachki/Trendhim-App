package com.example.asus.trendhimapp.weeklyLookPage.singleWeeklyLookPage;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.view.LayoutInflater;
import android.view.View;

import com.example.asus.trendhimapp.R;
import com.example.asus.trendhimapp.categoryPage.CategoryProduct;
import com.example.asus.trendhimapp.mainActivities.BaseActivity;
import com.example.asus.trendhimapp.util.Constants;
import com.example.asus.trendhimapp.weeklyLookPage.WeeklyLook;

import java.util.ArrayList;
import java.util.List;


public class SecondWeeklyLookActivity extends BaseActivity {

    List<WeeklyLook> weeklyLooks;
    SecondWeeklyLookAdapter adapter;
    ArrayList<CategoryProduct> wishListProducts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_second_rv, null, false);
        BaseActivity.drawer.addView(contentView, 0);
        initializeWeeklyLookRecyclerView();
    }

    /**
     * Initialize weekly list components
     */
    void initializeWeeklyLookRecyclerView() {

        // Lookup the recycler view in the activity layout (new)
        RecyclerView recyclerViewWeeklyLooks = findViewById(R.id.recyclerViewWeeklyLookSecond);

        //Weekly looks initialize
        weeklyLooks = new ArrayList<>();

        // Create a new adapter for the category
        adapter = new SecondWeeklyLookAdapter(this, weeklyLooks, new SingleWeeklyLookAdapter(this, wishListProducts), getLookId());

        // Attach the adapter to the recycler view
        recyclerViewWeeklyLooks.setAdapter(adapter);
        recyclerViewWeeklyLooks.setNestedScrollingEnabled(true);
        Intent intent = getIntent();
        if(intent != null) {
            //Populate the recycler view
            String lookKey = intent.getStringExtra(Constants.KEY_LOOK_KEY);
            if(lookKey != null)
                adapter.addData(lookKey);

        }

        SnapHelper helperNew = new LinearSnapHelper();
        LinearLayoutManager layoutManagerNew = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerViewWeeklyLooks.setLayoutManager(layoutManagerNew);

        helperNew.attachToRecyclerView(recyclerViewWeeklyLooks);

    }


    /**
     * @return look id from the intent
     */
    public String getLookId(){
        Intent intent = getIntent();
        String key;
        if (intent != null) {

            key = intent.getStringExtra(Constants.KEY_LOOK_KEY);

            if (key != null)
                return key;
        }
        return null;
    }
}
