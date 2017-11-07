package com.example.asus.trendhimapp.weeklyLook.singleWeeklyLook;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.asus.trendhimapp.R;
import com.example.asus.trendhimapp.categoryPage.CategoryProduct;
import com.example.asus.trendhimapp.mainActivities.BaseActivity;
import com.example.asus.trendhimapp.util.BitmapFlyweight;
import com.example.asus.trendhimapp.util.Constants;

import java.util.ArrayList;

public class SingleWeeklyLookActivity extends BaseActivity {

    ImageView mainLookImage, secondLookImage, thirdLookImage, fourthLookImage, fifthLookImage;
    TextView lookPhrase;
    ArrayList<CategoryProduct> wishListProducts;
    SingleWeeklyLookAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_single_weekly_look, null, false);
        BaseActivity.drawer.addView(contentView, 0);
        getSupportActionBar().setDisplayShowTitleEnabled(true);

        setTitle("Weekly Looks");

        mainLookImage = findViewById(R.id.mainLookImage);
        secondLookImage = findViewById(R.id.secondLookImage);
        thirdLookImage = findViewById(R.id.thirdLookImage);
        fourthLookImage = findViewById(R.id.fourthLookImage);
        fifthLookImage = findViewById(R.id.fifthLookImage);
        lookPhrase = findViewById(R.id.lookPhrase);

        loadImages();
        initializeRv();

    }

    @SuppressLint("SetTextI18n")
    public void loadImages() {

        Intent intent = getIntent();

        if (intent != null) {

            String mainPictureUrl = intent.getStringExtra(Constants.KEY_LOOK_MAIN_PICTURE_URL);
            String secondPictureUrl = intent.getStringExtra(Constants.KEY_LOOK_SECOND_PICTURE_URL);
            String thirdPictureUrl = intent.getStringExtra(Constants.KEY_LOOK_THIRD_PICTURE_URL);
            String fourthPictureUrl = intent.getStringExtra(Constants.KEY_LOOK_FOURTH_PICTURE_URL);
            String fifthPictureUrl = intent.getStringExtra(Constants.KEY_LOOK_FIFTH_PICTURE_URL);
            String phrase = intent.getStringExtra(Constants.KEY_LOOK_PHRASE);

            if (mainPictureUrl != null && secondPictureUrl != null && thirdPictureUrl != null && fourthPictureUrl != null
                    && fifthPictureUrl != null && phrase != null) {
                // set all picture images
                Log.i("shit", "everything is ok");
                BitmapFlyweight.getPicture(
                        mainPictureUrl, mainLookImage);
                BitmapFlyweight.getPicture(
                        secondPictureUrl, secondLookImage);
                BitmapFlyweight.getPicture(
                        thirdPictureUrl, thirdLookImage);
                BitmapFlyweight.getPicture(
                        fourthPictureUrl, fourthLookImage);
                BitmapFlyweight.getPicture(
                        fifthPictureUrl, fifthLookImage);
                lookPhrase.setText(phrase);

            }
        }
    }

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
    public void initializeRv(){
        // Lookup the recycler view in the activity layout (new)
        RecyclerView recyclerViewNewProducts = findViewById(R.id.rvLookProducts);

        //Recommended products initialize
        wishListProducts = new ArrayList<>();

        // Create a new adapter for the category
        adapter = new SingleWeeklyLookAdapter(this, wishListProducts);

        // Attach the adapter to the recycler view
        recyclerViewNewProducts.setAdapter(adapter);

        //Populate the recycler view
        adapter.addData(getLookId());

        SnapHelper helperNew = new LinearSnapHelper();
        LinearLayoutManager layoutManagerNew = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerViewNewProducts.setLayoutManager(layoutManagerNew);

        helperNew.attachToRecyclerView(recyclerViewNewProducts);

    }
}
