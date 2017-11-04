package com.example.asus.trendhimapp.ProductPage;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.asus.trendhimapp.MainActivities.BaseActivity;
import com.example.asus.trendhimapp.ProductPage.Products.BitmapFactory;
import com.example.asus.trendhimapp.R;


public class ProductActivity extends BaseActivity implements View.OnClickListener {

    private ImageView bannerImageView, leftImageView, rightImageView;
    private TextView brandTextView, priceTextView, productNameTextView;

    // some test values for a bag
    //public String bannerPic = "https://firebasestorage.googleapis.com/v0/b/trendhim-31939.appspot.com/o/bag_pictures%2Fbag1%2Fbag1_banner.png?alt=media&token=953c4b0e-6308-494d-b788-b069f478eb1c";
    // public String leftPic = "https://firebasestorage.googleapis.com/v0/b/trendhim-31939.appspot.com/o/bag_pictures%2Fbag1%2Fbag1_left_sided_picture.PNG?alt=media&token=53584d12-c70b-4a8e-82b2-e32ef8177e19";
    //public String rightPic = "https://firebasestorage.googleapis.com/v0/b/trendhim-31939.appspot.com/o/bag_pictures%2Fbag1%2Fbag1_right_sided_picture.PNG?alt=media&token=1849f345-bff7-4b91-bca8-163aeeef356f";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_product, null, false);
        BaseActivity.drawer.addView(contentView, 0);

        getSupportActionBar().setDisplayShowTitleEnabled(true);
        initializeComponents();
        getProductFromIntent();
    }

    private void initializeComponents() {
        bannerImageView = findViewById(R.id.bannerImageView);
        leftImageView = findViewById(R.id.leftImageView);
        rightImageView = findViewById(R.id.rightImageView);

        Button addToCartButton = findViewById(R.id.addToCartButton);
        Button addToWishlistButton = findViewById(R.id.addToWishlistButton);
        addToCartButton.setOnClickListener(this);
        addToWishlistButton.setOnClickListener(this);

        brandTextView = findViewById(R.id.brandTextView);
        brandTextView.setTypeface(null, Typeface.BOLD); // make the text bold
        brandTextView.setTextSize(getResources().getDimension(R.dimen.product_activity_text_size)); // font size: 7sp

        priceTextView = findViewById(R.id.priceTextView);
        priceTextView.setTypeface(null, Typeface.BOLD); // make the text bold
        priceTextView.setTextSize(getResources().getDimension(R.dimen.product_activity_text_size)); // font size: 7sp

        productNameTextView = findViewById(R.id.productNameTextView);

        // test value
        //  Product product = new Product(2, "Bag", bannerPic, leftPic, rightPic, "Delton", 150);
//        Intent fromCategory = getIntent();
//        Bundle bundle = fromCategory.getExtras();
//        if (bundle != null) {
//            product = (Product) bundle.get(Constants.PRODUCT_ITEM);
//        }
//        bundle.clear(); // cleanup the bundle

    }


    @Override
    public void onClick(View v) {
        final int id = v.getId();
        if (id == R.id.addToWishlistButton) {
            Toast.makeText(getApplicationContext(), "Test add to wishlist", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getApplicationContext(), "Test add to cart", Toast.LENGTH_LONG).show();
        }
    }

    @SuppressLint("SetTextI18n")
    public void getProductFromIntent() {

        Intent intent = getIntent();

        if (intent != null) {
            String productName = intent.getStringExtra("productName");
            String price = intent.getStringExtra("price");
            String bannerPictureUrl = intent.getStringExtra("bannerPictureUrl");
            String leftPictureUrl = intent.getStringExtra("leftPictureUrl");
            String rightPictureUrl = intent.getStringExtra("rightPictureUrl");
            String brand = intent.getStringExtra("brand");

            if (productName != null && price != null && bannerPictureUrl != null && leftPictureUrl != null
                    && rightPictureUrl != null && brand != null) {

                brandTextView.setText(brand); //set brand
                priceTextView.setText(price + " €"); // set price
                productNameTextView.setText(productName); //set product name
                BitmapFactory.getPicture(
                        bannerPictureUrl, bannerImageView); // get the banner
                BitmapFactory.getPicture(
                       leftPictureUrl, leftImageView); // get the left picture
                BitmapFactory.getPicture(
                        rightPictureUrl, rightImageView); // get the right picture
            }
        }
    }

}