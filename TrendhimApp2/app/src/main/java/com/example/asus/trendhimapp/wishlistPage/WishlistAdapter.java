package com.example.asus.trendhimapp.wishlistPage;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.asus.trendhimapp.R;
import com.example.asus.trendhimapp.categoryPage.CategoryProduct;
import com.example.asus.trendhimapp.mainActivities.MainActivity;
import com.example.asus.trendhimapp.mainActivities.recentProducts.RecentProductsAdapter;
import com.example.asus.trendhimapp.productPage.Product;
import com.example.asus.trendhimapp.productPage.ProductActivity;
import com.example.asus.trendhimapp.shoppingCartPage.ShoppingCartProduct;
import com.example.asus.trendhimapp.util.BitmapFlyweight;
import com.example.asus.trendhimapp.util.Constants;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class WishlistAdapter extends RecyclerView.Adapter<WishlistAdapter.ViewHolder> {

    private String userEmail;
    private Context context;
    private List<CategoryProduct> productList;

    public WishlistAdapter(Context context) {
        this.productList = new ArrayList<>();
        this.context = context;
        setEmail();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View wishlistProducts = inflater.inflate(R.layout.item_wishlist, parent, false);

        return new ViewHolder(wishlistProducts);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, @SuppressLint("RecyclerView") final int position) {

        final CategoryProduct currentProduct = productList.get(position);
        viewHolder.productNameTextView.setText(currentProduct.getName());
        viewHolder.priceTextView.setText(currentProduct.getPrice() + "€");
        viewHolder.emailTextView.setText(userEmail);

        BitmapFlyweight.getPicture(currentProduct.getBannerPictureURL(), viewHolder.bannerImageView);
        viewHolder.removeProductButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                executeRemoveItem(currentProduct);
            }
        });

        viewHolder.addToCartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                executeAddToCart(currentProduct, position);
            }
        });

        viewHolder.bannerImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              getProduct(currentProduct, view);
            }
        });

    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    /**
     * Redirect the user to the right product when the banner picture is clicked
     * @param currentProduct
     */
    private void getProduct(final CategoryProduct currentProduct, final View view) {
        //Query the wishlist database to get the product category
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(Constants.TABLE_NAME_WISHLIST);
        //get product which key is equal to the one clicked
        databaseReference.orderByChild(Constants.KEY_PRODUCT_KEY).equalTo(currentProduct.getKey())
                .addListenerForSingleValueEvent(new ValueEventListener() {

                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){

                            for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                                //Found product
                                final WishlistProduct wishlistProduct = dataSnapshot1.getValue(WishlistProduct.class);
                                //Query to the product category to get the product information
                                DatabaseReference category_products_database =
                                        FirebaseDatabase.getInstance().getReference(wishlistProduct.getEntityName());

                                category_products_database.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        if (dataSnapshot.exists()) {
                                            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                                //If the object key is correct
                                                if(Objects.equals(currentProduct.getKey(), dataSnapshot1.getKey())) {

                                                    Product foundProduct = dataSnapshot1.getValue(Product.class);
                                                    Intent toProductPage = new Intent(context, ProductActivity.class);

                                                    ActivityOptionsCompat options = ActivityOptionsCompat.
                                                            makeSceneTransitionAnimation((Activity) context, view, "profile");

                                                    toProductPage.putExtra(Constants.KEY_PRODUCT_KEY, currentProduct.getKey());
                                                    toProductPage.putExtra(Constants.KEY_PRODUCT_NAME, foundProduct.getProductName());
                                                    toProductPage.putExtra(Constants.KEY_BRAND_NAME, foundProduct.getBrand());
                                                    toProductPage.putExtra(Constants.KEY_BANNER_PIC_URL, foundProduct.getBannerPictureUrl());
                                                    toProductPage.putExtra(Constants.KEY_PRICE, String.valueOf(foundProduct.getPrice()));
                                                    toProductPage.putExtra(Constants.KEY_LEFT_PIC_URL, foundProduct.getLeftPictureUrl());
                                                    toProductPage.putExtra(Constants.KEY_RIGHT_PIC_URL, foundProduct.getRightPictureUrl());

                                                    context.startActivity(toProductPage, options.toBundle());

                                                }
                                            }
                                            //Add the product to recent products
                                            RecentProductsAdapter.addToRecent(currentProduct);
                                            MainActivity.adapter.notifyDataSetChanged();

                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {}
                                });
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {}
                });
    }

    /**
     * Loads all wishlist items in the Recycler view
     **/
    void populateRecyclerView() {
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference(Constants.TABLE_NAME_WISHLIST);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            String email = user.getEmail();
            Query query = myRef.orderByChild(Constants.KEY_USER_EMAIL).equalTo(email);
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {

                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            WishlistProduct wishProd = ds.getValue(WishlistProduct.class);
                            final String entityName = wishProd.getEntityName(), productKey = wishProd.getProductKey();
                            DatabaseReference myRef1 = FirebaseDatabase.getInstance().getReference(entityName);
                            myRef1.addListenerForSingleValueEvent(new ValueEventListener() {

                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {

                                    if (dataSnapshot.exists()) {

                                        for (DataSnapshot ds1 : dataSnapshot.getChildren()) {

                                            if (productKey.equals(ds1.getKey())) {

                                                Product currentProduct = ds1.getValue(Product.class); //get the right

                                                // Create the category product
                                                CategoryProduct categoryProduct = new CategoryProduct(currentProduct.getProductName(),
                                                        currentProduct.getPrice(), currentProduct.getBrand(),
                                                        currentProduct.getBannerPictureUrl(), ds1.getKey());
                                                productList.add(categoryProduct); // Add it to the list

                                                notifyItemInserted(getItemCount()); // Update UI - notify the adapter than an item has been inserted
                                            }
                                        }
                                    }
                                }
                                @Override
                                public void onCancelled(DatabaseError databaseError) {}
                            });
                        }
                    }
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {}
            });
        }
    }

    /**
     * Adds an item to the shopping cart
     **/
    private void executeAddToCart(final CategoryProduct categoryProduct, final int position) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(true);
        builder.setMessage("Add " + categoryProduct.getName() + " to shopping cart?");

        // Yes option
        builder.setPositiveButton(R.string.positive_option, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                final DatabaseReference myRef = FirebaseDatabase.getInstance()
                        .getReference(Constants.TABLE_NAME_SHOPPING_CART);
                final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                final boolean[] exists = {false};

                if(user != null){ //if the user is logged in
                    myRef.orderByChild(Constants.KEY_PRODUCT_KEY).equalTo(categoryProduct.getKey())
                            .addListenerForSingleValueEvent(new ValueEventListener() { // get user recent products
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                                        ShoppingCartProduct shoppingCartProduct = dataSnapshot1.getValue(ShoppingCartProduct.class);

                                        //if the product is already in the shopping cart
                                        if(Objects.equals(shoppingCartProduct.getUserEmail(), user.getEmail())) {
                                            int currentQuantity = Integer.parseInt(shoppingCartProduct.getQuantity()) + 1;
                                            //Increase the product quantity
                                            dataSnapshot1.getRef().child(Constants.KEY_QUANTITY).setValue(String.valueOf(currentQuantity));
                                            Toast.makeText(context, R.string.item_added_to_cart_message, Toast.LENGTH_SHORT).show();
                                            exists[0] = true;
                                            break;
                                        }
                                    }
                                    if(!exists[0]){
                                        Map<String, Object> values = new HashMap<>();
                                        values.put(Constants.KEY_PRODUCT_KEY, categoryProduct.getKey());
                                        values.put(Constants.KEY_USER_EMAIL, user.getEmail());
                                        values.put(Constants.KEY_QUANTITY, "1");
                                        myRef.push().setValue(values);
                                        Toast.makeText(context, R.string.item_added_to_cart_message, Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {}
                            });
                }
                removeItem(categoryProduct); //Remove item from the wishlist firebase
                productList.remove(position); //Remove item from the wishlist recycler view
                notifyItemRemoved(position); //Notify the adapter that an item has been removed
            }

        });

        // Cancel option
        builder.setNegativeButton(R.string.negative_option, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        // Show the dialog window
        AlertDialog dialog = builder.create();
        dialog.show();

        // Customize button text
        Button btnPositive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
        btnPositive.setTextColor(ContextCompat.getColor(context, R.color.black));
        Button btnNegative = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
        btnNegative.setTextColor(ContextCompat.getColor(context, R.color.black));

        // Align the buttons in the center of the dialog window
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) btnPositive.getLayoutParams();
        layoutParams.weight = R.dimen.center_position;
        btnPositive.setLayoutParams(layoutParams);
        btnNegative.setLayoutParams(layoutParams);

    }

    /**
     * Starts a sequence for the removal of an item from the Wishlist
     **/
    private void executeRemoveItem(final CategoryProduct categoryProduct) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(true);
        builder.setMessage("Remove " + categoryProduct.getName() + " from wishlist?");

        // Yes option
        builder.setPositiveButton(R.string.positive_option,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        removeItem(categoryProduct); // Item is removed from Firebase
                        productList.remove(categoryProduct); // Item is also removed from the RecyclerView
                        notifyDataSetChanged(); // Refresh the view
                        Toast.makeText(context, R.string.remove_success_message, Toast.LENGTH_SHORT).show();
                    }
                });

        // Cancel option
        builder.setNegativeButton(R.string.negative_option,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss(); // If cancel is pressed, dialog is closed
                    }
                });

        // Show the dialog window
        AlertDialog dialog = builder.create();
        dialog.show();

        // Customize button text
        Button btnPositive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
        btnPositive.setTextColor(ContextCompat.getColor(context, R.color.black));
        Button btnNegative = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
        btnNegative.setTextColor(ContextCompat.getColor(context, R.color.black));

        // Align the buttons in the center of the dialog window
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) btnPositive.getLayoutParams();
        layoutParams.weight = R.dimen.center_position;
        btnPositive.setLayoutParams(layoutParams);
        btnNegative.setLayoutParams(layoutParams);
    }

    /**
     * Removes a product from the user's wishlist datbase - firebase
     **/
    private void removeItem(final CategoryProduct categoryProduct) {

        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference(Constants.TABLE_NAME_WISHLIST);
        final String productKey = categoryProduct.getKey();

        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        WishlistProduct currentProd = ds.getValue(WishlistProduct.class);

                        if (productKey.equals(currentProd.getProductKey()) &&
                                userEmail.equals(currentProd.getUserEmail())) {
                            ds.getRef().removeValue(); // If found, remove the item from teh wishlist
                            notifyDataSetChanged(); // Update the UI
                        }
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }

    /**
     * Sets the email of the current user
     **/
    private void setEmail() {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null) {
            this.userEmail = firebaseUser.getEmail();
        }
    }

    /**
     * Provide a direct reference to each of the views
     * used to cache the views within the layout for fast access
     */
    static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView bannerImageView;
        TextView productNameTextView;
        TextView priceTextView;
        TextView emailTextView;
        ImageButton removeProductButton;
        ImageButton addToCartButton;

        ViewHolder(View itemView) {

            super(itemView);

            bannerImageView = itemView.findViewById(R.id.banner_image_view);
            productNameTextView = itemView.findViewById(R.id.product_name_text_view);
            priceTextView = itemView.findViewById(R.id.price_text_view);
            emailTextView = itemView.findViewById(R.id.email_text_field);
            removeProductButton = itemView.findViewById(R.id.remove_button);
            addToCartButton = itemView.findViewById(R.id.add_to_cart_button);
        }
    }
}
