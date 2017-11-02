package com.example.asus.trendhimapp.ProductPage;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.asus.trendhimapp.BaseActivity;
import com.example.asus.trendhimapp.R;

public class ProductActivity extends BaseActivity implements View.OnClickListener,
        CompoundButton.OnCheckedChangeListener {

    private ImageView bannerImageView, leftImageView, rightImageView, brandImageView;
    private Button addToCartButton, addToWishlistButton;
    private CheckBox discountCheckBox;
    private TextView brandTextView, priceTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_product, null, false);
        BaseActivity.drawer.addView(contentView, 0);
        getSupportActionBar().setDisplayShowTitleEnabled(true);

        setTitle("Product Page");
        // test
        int a = 1;
        initializeComponents();
    }

    private void initializeComponents() {
        bannerImageView = (ImageView) findViewById(R.id.bannerImageView);
        leftImageView = (ImageView) findViewById(R.id.leftImageView);
        rightImageView = (ImageView) findViewById(R.id.rightImageView);
        brandImageView = (ImageView) findViewById(R.id.brandPictureImageView);
        addToCartButton = (Button) findViewById(R.id.addToCartButton);
        addToWishlistButton = (Button) findViewById(R.id.addToWishlistButton);
        discountCheckBox = (CheckBox) findViewById(R.id.discountCheckBox);
        brandTextView = (TextView) findViewById(R.id.brandTextView);
        priceTextView = (TextView) findViewById(R.id.priceTextView);
        addToCartButton.setOnClickListener(this);
        addToWishlistButton.setOnClickListener(this);
        discountCheckBox.setOnCheckedChangeListener(this);

//        Bundle fromProductList = getIntent().getExtras();
//        Product selectedItem = (Product) fromProductList.get(Constants.PRODUCT_OBJECT_KEY); // retrieve the object from the list view
//
//        brandTextView.setText(selectedItem.getBrand()); // set the brand
//        priceTextView.setText(String.format("$%.2f", selectedItem.getPrice())); // format and set the price
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

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        Toast.makeText(getApplicationContext(), Boolean.toString(isChecked), Toast.LENGTH_LONG).show();
    }
}
