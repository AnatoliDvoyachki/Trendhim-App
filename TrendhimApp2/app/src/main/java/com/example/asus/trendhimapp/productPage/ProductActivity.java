package com.example.asus.trendhimapp.productPage;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.asus.trendhimapp.R;
import com.example.asus.trendhimapp.mainActivities.BaseActivity;
import com.example.asus.trendhimapp.shoppingCart.ShoppingCartProduct;
import com.example.asus.trendhimapp.util.BitmapFlyweight;
import com.example.asus.trendhimapp.util.Constants;
import com.example.asus.trendhimapp.wishlistPage.WishlistProduct;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ProductActivity extends BaseActivity implements View.OnClickListener {

    private ImageView bannerImageView, leftImageView, rightImageView;
    private TextView brandTextView, priceTextView, productNameTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initializeComponents();
        loadProduct();
    }

    /**
     * Initialize product activity components
     */
    private void initializeComponents() {
        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_product, null, false);
        BaseActivity.drawer.addView(contentView, 0);
        getSupportActionBar().setDisplayShowTitleEnabled(true);

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

    /**
     * Loads products and sets its components
     */
    public void loadProduct() {

        Intent fromCategoryPage = getIntent();

        if (fromCategoryPage != null) {

            String productName = fromCategoryPage.getStringExtra(Constants.KEY_PRODUCT_NAME);
            String price = fromCategoryPage.getStringExtra(Constants.KEY_PRICE);
            String bannerPictureUrl = fromCategoryPage.getStringExtra(Constants.KEY_BANNER_PIC_URL);
            String leftPictureUrl = fromCategoryPage.getStringExtra(Constants.KEY_LEFT_PIC_URL);
            String rightPictureUrl = fromCategoryPage.getStringExtra(Constants.KEY_RIGHT_PIC_URL);
            String brand = fromCategoryPage.getStringExtra(Constants.KEY_BRAND_NAME);

            if (productName != null && price != null && brand != null) {

                // set all the values & pictures
                brandTextView.setText(brand);
                priceTextView.setText(String.format("%s€", price));
                productNameTextView.setText(productName);

                if(bannerPictureUrl != null)
                    BitmapFlyweight.getPicture(
                            bannerPictureUrl, bannerImageView);

                if(leftPictureUrl != null)
                    BitmapFlyweight.getPicture(
                        leftPictureUrl, leftImageView);

                if(rightPictureUrl != null)
                    BitmapFlyweight.getPicture(
                            rightPictureUrl, rightImageView);
            }
        }
    }

    /**
     * Handle wishlist and shopping cart on click listeners
     * @param v
     */
    @Override
    public void onClick(View v) {
        final int id = v.getId();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if (currentUser != null) {
            if (id == R.id.addToWishlistButton) {
                Intent intent = getIntent();

                if (intent != null) {

                    String productName = intent.getStringExtra(Constants.KEY_PRODUCT_NAME);
                    final AlertDialog.Builder builder = new AlertDialog.Builder(ProductActivity.this);
                    builder.setCancelable(true);
                    builder.setMessage("Add " + productName + " to wishlist?");

                    // Yes option
                    builder.setPositiveButton(R.string.positive_option, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            addToWishlist();
                        }
                    });

                    // Cancel option
                    builder.setNegativeButton(R.string.negative_option, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

                    // Show the message
                    AlertDialog dialog = builder.create();
                    dialog.show();

                    // Customize button text
                    Button btnPositive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                    btnPositive.setTextColor(ContextCompat.getColor(ProductActivity.this, R.color.black));
                    Button btnNegative = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
                    btnNegative.setTextColor(ContextCompat.getColor(ProductActivity.this, R.color.black));

                    // Align the buttons in the center of the dialog window
                    LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) btnPositive.getLayoutParams();
                    layoutParams.weight = 10;
                    btnPositive.setLayoutParams(layoutParams);
                    btnNegative.setLayoutParams(layoutParams);
                }
            } else {
                Intent intent = getIntent();

                if (intent != null) {

                    String productName = intent.getStringExtra(Constants.KEY_PRODUCT_NAME);
                    final AlertDialog.Builder builder = new AlertDialog.Builder(ProductActivity.this);
                    builder.setCancelable(true);
                    builder.setMessage("Add " + productName + " to shopping cart?");

                    // Yes option
                    builder.setPositiveButton(R.string.positive_option, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            addToShoppingCart();
                        }
                    });

                    // Cancel option
                    builder.setNegativeButton(R.string.negative_option, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

                    // Show the message
                    AlertDialog dialog = builder.create();
                    dialog.show();

                    // Customize button text
                    Button btnPositive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                    btnPositive.setTextColor(ContextCompat.getColor(ProductActivity.this, R.color.black));
                    Button btnNegative = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
                    btnNegative.setTextColor(ContextCompat.getColor(ProductActivity.this, R.color.black));

                    // Align the buttons in the center of the dialog window
                    LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) btnPositive.getLayoutParams();
                    layoutParams.weight = 10;
                    btnPositive.setLayoutParams(layoutParams);
                    btnNegative.setLayoutParams(layoutParams);
                }
            }
        } else {
            Toast.makeText(this, R.string.not_logged_in_unsuccess_message, Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Adds an item to the shopping list
     **/
    private void addToShoppingCart() {

        Intent fromCategoryPage = getIntent();
        if (fromCategoryPage != null) {
            String productKey = fromCategoryPage.getStringExtra(Constants.KEY_PRODUCT_KEY);
            executeAddToCart(productKey);
        }
    }

    /**
     * Adds an item to the shopping cart
     **/
    private void executeAddToCart(final String categoryProductKey) {

        final DatabaseReference myRef = FirebaseDatabase.getInstance().getReference(Constants.TABLE_NAME_SHOPPING_CART);
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        final boolean[] exists = {false};

        if(user != null){ //if the user is logged in

            myRef.orderByChild(Constants.KEY_PRODUCT_KEY).equalTo(categoryProductKey)
                    .addListenerForSingleValueEvent(new ValueEventListener() { // get user recent products

                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                ShoppingCartProduct shoppingCartProduct = dataSnapshot1.getValue(ShoppingCartProduct.class);

                                if(Objects.equals(shoppingCartProduct.getUserEmail(), user.getEmail())) {
                                    int currentQuantity = Integer.parseInt(shoppingCartProduct.getQuantity()) + 1;
                                    //Increase the product quantity if the product is already in the shopping cart
                                    dataSnapshot1.getRef().child(Constants.KEY_QUANTITY).setValue(String.valueOf(currentQuantity));

                                    Toast.makeText(getApplicationContext(), R.string.item_added_to_cart_message,
                                            Toast.LENGTH_SHORT).show();
                                    exists[0] = true;
                                    break;
                                }
                            }

                            //Add the product if the product is not in the shopping cart
                            if(!exists[0]){
                                Map<String, Object> values = new HashMap<>();
                                values.put(Constants.KEY_PRODUCT_KEY, categoryProductKey);
                                values.put(Constants.KEY_USER_EMAIL, user.getEmail());
                                values.put(Constants.KEY_QUANTITY, "1");
                                myRef.push().setValue(values);
                                Toast.makeText(getApplicationContext(), R.string.item_added_to_cart_message,
                                        Toast.LENGTH_SHORT).show();
                            }

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {}
                    });
        }
    }

    /**
     * Adds an item to the user's wishlist
     **/
    private void addToWishlist() {

        final DatabaseReference myRef = FirebaseDatabase.getInstance().getReference(Constants.TABLE_NAME_WISHLIST);
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        final boolean[] exists = {false};

        Intent intent = getIntent();

        if (intent != null) {
            final String userEmail = user.getEmail();
            final String productKey = intent.getStringExtra(Constants.KEY_PRODUCT_KEY);

            //Checks if a product is already added to the wishlist
            // when the user clicks the add to wishlist button
            myRef.orderByChild(Constants.KEY_PRODUCT_KEY).equalTo(productKey)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                        WishlistProduct wishlistProduct = dataSnapshot1.getValue(WishlistProduct.class);

                        if(Objects.equals(wishlistProduct.getUserEmail(), userEmail)) {

                            Toast.makeText(getApplicationContext(),
                                    R.string.item_already_in_the_wishlist_message, Toast.LENGTH_SHORT).show();
                            exists[0] = true;

                            break;
                        }
                    }
                    //Add the product if the product is not in the wishlist
                    if(!exists[0]){
                        Map<String, Object> values = new HashMap<>();
                        values.put(Constants.KEY_PRODUCT_KEY, productKey);
                        values.put(Constants.KEY_USER_EMAIL,userEmail);
                        values.put(Constants.KEY_ENTITY_NAME, getCategory(productKey));
                        myRef.push().setValue(values);
                        Toast.makeText(getApplicationContext(), R.string.added_to_wishlist_success_message, Toast.LENGTH_SHORT).show();

                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }
    }

    /**
     * Get the product category - Used to query the wishlist database
     * @param productKey
     * @return
     */
    String getCategory(String productKey){
        String entityName;
        if (productKey.startsWith(Constants.WATCH_PREFIX))
            entityName = productKey.replaceAll(Constants.ALL_DIGITS_REGEX, "es");
        else if (productKey.startsWith(Constants.BEARD_CARE_PREFIX))
            entityName = productKey.replaceAll(Constants.ALL_DIGITS_REGEX, "");
        else
            entityName = productKey.replaceAll(Constants.ALL_DIGITS_REGEX, "s");

        return entityName;
    }

}
