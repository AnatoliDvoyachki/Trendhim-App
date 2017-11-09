package com.example.asus.trendhimapp.productPage;

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

import com.example.asus.trendhimapp.mainActivities.BaseActivity;
import com.example.asus.trendhimapp.shoppingCartActivity.ShoppingCartProduct;
import com.example.asus.trendhimapp.wishlistPage.WishlistProduct;
import com.example.asus.trendhimapp.R;
import com.example.asus.trendhimapp.util.BitmapFlyweight;
import com.example.asus.trendhimapp.util.Constants;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class ProductActivity extends BaseActivity implements View.OnClickListener {
    private ImageView bannerImageView, leftImageView, rightImageView;
    private TextView brandTextView, priceTextView, productNameTextView;
    private int instanceCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_product, null, false);
        BaseActivity.drawer.addView(contentView, 0);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        initializeComponents();
        loadProduct();
    }

    private void initializeComponents() {
        this.instanceCount = 0;
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
        priceTextView.setTypeface(null, Typeface.BOLD);
        priceTextView.setTextSize(getResources().getDimension(R.dimen.product_activity_text_size));

        productNameTextView = findViewById(R.id.productNameTextView);
    }

    @SuppressLint("SetTextI18n")
    public void loadProduct() {
        Intent intent = getIntent();
        if (intent != null) {
            String productName = intent.getStringExtra(Constants.KEY_PRODUCT_NAME);
            String price = intent.getStringExtra(Constants.KEY_PRICE);
            String bannerPictureUrl = intent.getStringExtra(Constants.KEY_BANNER_PIC_URL);
            String leftPictureUrl = intent.getStringExtra(Constants.KEY_LEFT_PIC_URL);
            String rightPictureUrl = intent.getStringExtra(Constants.KEY_RIGHT_PIC_URL);
            String brand = intent.getStringExtra(Constants.KEY_BRAND_NAME);
            if (productName != null && price != null && bannerPictureUrl != null &&
                    leftPictureUrl != null && rightPictureUrl != null && brand != null) {
                // set all the values & pictures
                brandTextView.setText(brand);
                priceTextView.setText(price + " €");
                productNameTextView.setText(productName);
                BitmapFlyweight.getPicture(
                        bannerPictureUrl, bannerImageView);
                BitmapFlyweight.getPicture(
                        leftPictureUrl, leftImageView);
                BitmapFlyweight.getPicture(
                        rightPictureUrl, rightImageView);
            }
        }
    }

    @Override
    public void onClick(View v) {
        final int id = v.getId();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            if (id == R.id.addToWishlistButton) {
                addToWishlist();
            } else {
                addToShoppingCart();
            }
        } else {
            Toast.makeText(this, R.string.not_logged_in_unsuccess_message, Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Adds an item to the shopping list
     **/
    private void addToShoppingCart() {
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference(Constants.TABLE_NAME_SHOPPING_CART);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        Intent intent = getIntent();
        if (intent != null) {
            String userEmail = user.getEmail();
            String productKey = intent.getStringExtra(Constants.KEY_PRODUCT_KEY);
            String productName = intent.getStringExtra(Constants.KEY_PRODUCT_NAME);
            String bannerPictureUrl = intent.getStringExtra(Constants.KEY_BANNER_PIC_URL);
            String price = intent.getStringExtra(Constants.KEY_PRICE);
            if (!shoppingCartProductExists(userEmail, productKey)) {
                String entityName;
                if (productKey.startsWith(Constants.WATCH_REGEX)) {
                    entityName = productKey.replaceAll(Constants.ALL_DIGITS_REGEX, "es");
                } else {
                    entityName = productKey.replaceAll(Constants.ALL_DIGITS_REGEX, "s");
                }
                ShoppingCartProduct product =
                        new ShoppingCartProduct(productKey,
                                userEmail, 1);

                myRef.push().setValue(product);
                Toast.makeText(this, "Item added to the cart", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Item is already in the cart", Toast.LENGTH_SHORT).show();
            }
            this.instanceCount = 0;
        }
    }

    /**
     * Adds an item to the user's wishlist
     **/
    private void addToWishlist() {
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference(Constants.TABLE_NAME_WISHLIST);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        Intent intent = getIntent();
        if (intent != null) {
            String userEmail = user.getEmail();
            String productKey = intent.getStringExtra(Constants.KEY_PRODUCT_KEY);
            if (!wishlistProductExists(userEmail, productKey)) {
                String entityName;
                if (productKey.startsWith(Constants.WATCH_REGEX)) {
                    entityName = productKey.replaceAll(Constants.ALL_DIGITS_REGEX, "es");
                } else {
                    entityName = productKey.replaceAll(Constants.ALL_DIGITS_REGEX, "s");
                }
                myRef.push().setValue(new WishlistProduct(productKey, entityName, userEmail));
                Toast.makeText(this, R.string.added_to_wishlist_success_message, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, R.string.item_already_in_the_wishlist_message, Toast.LENGTH_SHORT).show();
            }
            this.instanceCount = 0;
        }
    }

    /**
     * Returns true, if the shopping cart contains an item
     * with the email and key passed as parameters. Otherwise, fa;se.
     **/
    private boolean shoppingCartProductExists(final String userEmail, final String productKey) {
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference(Constants.TABLE_NAME_SHOPPING_CART);
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        ShoppingCartProduct product = ds.getValue(ShoppingCartProduct.class);
                        if (userEmail.equals(product.getUserEmail()) &&
                                productKey.equals(product.getProductKey())) {
                            ++instanceCount;
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
        return instanceCount != 0;
    }

    /**
     * Returns true, if the wishlist contains an item with the
     * email and key passed as parameters. Otherwise, fase.
     **/
    private boolean wishlistProductExists(final String userEmail, final String productKey) {
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference(Constants.TABLE_NAME_WISHLIST);
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        WishlistProduct wishProd = ds.getValue(WishlistProduct.class);
                        if (userEmail.equals(wishProd.getUserEmail()) &&
                                productKey.equals(wishProd.getProductKey())) {
                            ++instanceCount;
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
        return instanceCount != 0;
    }
}
