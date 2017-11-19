package com.example.asus.trendhimapp.shoppingCartPage;

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
import com.example.asus.trendhimapp.util.BitmapFlyweight;
import com.example.asus.trendhimapp.util.Constants;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;
import java.util.Objects;

public class ShoppingCartAdapter extends RecyclerView.Adapter<ShoppingCartAdapter.ViewHolder> {

    private List<CategoryProduct> shoppingCartProducts;
    private Context context;

    ShoppingCartAdapter(Context context, List<CategoryProduct> shoppingCartProducts) {
        this.context = context;
        this.shoppingCartProducts = shoppingCartProducts;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View shoppingCartProds = inflater.inflate(R.layout.item_shopping_cart, parent, false);

        return new ViewHolder(shoppingCartProds);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final CategoryProduct currentProduct = shoppingCartProducts.get(position);

        // Set banner image
        ImageView bannerImageView = holder.bannerImageView;
        BitmapFlyweight.getPicture(currentProduct.getBannerPictureURL(), bannerImageView);

        // Set product name text field
        TextView productNameTextView = holder.productNameTextView;
        productNameTextView.setText(currentProduct.getName());

        final TextView quantityTextView = holder.quantityTextView;
        final TextView currentProductPriceTextView = holder.totalPriceTextView;

        // Set initial quantity and total price
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference(Constants.TABLE_NAME_SHOPPING_CART);
        final String productKey = currentProduct.getKey(), userEmail = user.getEmail();

        if(user != null){
            myRef.orderByChild(Constants.KEY_USER_EMAIL)
                    .equalTo(userEmail).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        for (final DataSnapshot ds : dataSnapshot.getChildren()) {
                            final ShoppingCartProduct scp = ds.getValue(ShoppingCartProduct.class);

                            if (productKey.equals(scp.getProductKey())) {
                                quantityTextView.setText(scp.getQuantity());

                                // Set initial price
                                int price = currentProduct.getPrice() * Integer.parseInt(quantityTextView.getText().toString());
                                currentProductPriceTextView.setText(String.valueOf(price) + "€");

                                // Update the subtotal
                                int currentSubtotal = ShoppingCartActivity.getSubtotalCost() + price;
                                ShoppingCartActivity.setSubtotal(currentSubtotal);

                                // Update the shipping cost
                                if(ShoppingCartActivity.getSubtotalCost() <= 75)
                                    ShoppingCartActivity.setShippingCost(Constants.SINGLE_ITEM_SHIPPING_COST);
                                else
                                    ShoppingCartActivity.setShippingCost(0);

                                // Update the total cost
                                int currentShippingCost = ShoppingCartActivity.getShippingCost();
                                int grandTotal = currentShippingCost + currentSubtotal;
                                ShoppingCartActivity.setGrandTotalCost(grandTotal);

                                // Initialize the remove button
                                ImageButton removeItemButton = holder.removeItemButton;
                                removeItemButton.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        initializeRemoveButton(currentProduct);
                                    }
                                });

                                // Setup the plus button and handle on click listener events
                                Button incrementButton = holder.incrementButton;
                                incrementButton.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        int currentQuantity = Integer.parseInt(quantityTextView.getText().toString());

                                        // Update current item total price and the quantity
                                        quantityTextView.setText(String.valueOf(++currentQuantity));
                                        int currentItemTotalPrice = currentQuantity * currentProduct.getPrice();
                                        currentProductPriceTextView.setText(String.valueOf(currentItemTotalPrice) + "€");
                                        ds.getRef().child(Constants.KEY_QUANTITY).setValue(String.valueOf(currentQuantity)); //update the product quantity in the database

                                        // Update subtotal price
                                        int currentSubtotal = ShoppingCartActivity.getSubtotalCost() + (currentProduct.getPrice());
                                        ShoppingCartActivity.setSubtotal(currentSubtotal);

                                        // Update shipping cost
                                        // Free shipping if the items price is over 75 euros
                                        if(ShoppingCartActivity.getSubtotalCost() <= 75)
                                            ShoppingCartActivity.setShippingCost(Constants.SINGLE_ITEM_SHIPPING_COST);
                                        else
                                            ShoppingCartActivity.setShippingCost(0);

                                        // Update grand total price
                                        int grandTotal = ShoppingCartActivity.getSubtotalCost() + ShoppingCartActivity.getShippingCost();
                                        ShoppingCartActivity.setGrandTotalCost(grandTotal);

                                    }
                                });

                                // Setup the minus button and handle on click listener events
                                Button decrementButton = holder.decrementButton;
                                decrementButton.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        int currentQuantity = Integer.parseInt(quantityTextView.getText().toString());
                                        if (currentQuantity - 1 >= 1) {

                                            // Update current item total price and the quantity
                                            quantityTextView.setText(String.valueOf(--currentQuantity));
                                            int currentItemTotalPrice = currentQuantity * currentProduct.getPrice();
                                            currentProductPriceTextView.setText(String.valueOf(currentItemTotalPrice) + "€");
                                            ds.getRef().child(Constants.KEY_QUANTITY).setValue(String.valueOf(currentQuantity)); //update the product quantity in the database

                                            // Update subtotal price
                                            int currentSubtotal = ShoppingCartActivity.getSubtotalCost() - currentProduct.getPrice();

                                            ShoppingCartActivity.setSubtotal(currentSubtotal);

                                            // Free shipping if the items price is over 75 euros
                                            if(ShoppingCartActivity.getSubtotalCost() <= 75)
                                                ShoppingCartActivity.setShippingCost(Constants.SINGLE_ITEM_SHIPPING_COST);
                                            else
                                                ShoppingCartActivity.setShippingCost(0);

                                            // Update grand total price
                                            int grandTotal = ShoppingCartActivity.getSubtotalCost() + ShoppingCartActivity.getShippingCost();
                                            ShoppingCartActivity.setGrandTotalCost(grandTotal);

                                        }
                                    }
                                });
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {}
            });

        }

        holder.bannerImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getProduct(currentProduct, view);
            }
        });

    }

    /**
     * Handle on click listener events for the remove button
     * Remove an item from the shopping cart - recycler view and firebase
     * @param currentProduct
     */
    private void initializeRemoveButton(final CategoryProduct currentProduct) {
        // Create the delete confirmation dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(true);
        builder.setMessage("Remove " + currentProduct.getName() + " from the shopping cart?");

        // Yes option
        builder.setPositiveButton(R.string.positive_option, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                removeItem(currentProduct); // Remove the product from Firebase
                shoppingCartProducts.remove(currentProduct); // Remove the item from the recycler view

                // Update the shipping cost
                if(ShoppingCartActivity.getSubtotalCost() <= 75) {
                    ShoppingCartActivity.setShippingCost(Constants.SINGLE_ITEM_SHIPPING_COST);
                } else {
                    ShoppingCartActivity.setShippingCost(0);
                }

                // Reset subtotal and grand total so they can be recalculated by notifyDataSetChanged() call
                ShoppingCartActivity.setSubtotal(0);
                ShoppingCartActivity.setGrandTotalCost(0);

                notifyDataSetChanged(); // Notify the adapter about the change

                Toast.makeText(context, R.string.remove_success_message, Toast.LENGTH_SHORT).show(); // Notify the user
            }
        });

        // Cancel option
        builder.setNegativeButton(R.string.negative_option, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        // Show the dialog window to the user
        AlertDialog dialog = builder.create();
        dialog.show();

        // Customize button text
        Button btnPositive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
        btnPositive.setTextColor(ContextCompat.getColor(context, R.color.black));
        Button btnNegative = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
        btnNegative.setTextColor(ContextCompat.getColor(context, R.color.black));

        // Align the buttons in the center of the dialog window
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) btnPositive.getLayoutParams();
        layoutParams.weight = 10;
        btnPositive.setLayoutParams(layoutParams);
        btnNegative.setLayoutParams(layoutParams);
    }

    /**
     * Redirect the user to the right product when the banner picture is clicked
     * @param currentProduct
     */
    private void getProduct(final CategoryProduct currentProduct, final View view) {
        //Query the shopping cart database to get the product category
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(Constants.TABLE_NAME_SHOPPING_CART);
        //get product which key is equal to the one clicked
        databaseReference.orderByChild(Constants.KEY_PRODUCT_KEY).equalTo(currentProduct.getKey())
                .addListenerForSingleValueEvent(new ValueEventListener() {

                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){

                            for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                                //Found product
                                final ShoppingCartProduct shoppingCartProduct = dataSnapshot1.getValue(ShoppingCartProduct.class);
                                //Query to the product category to get the product information
                                DatabaseReference category_products_database =
                                        FirebaseDatabase.getInstance().getReference(getCategory(shoppingCartProduct.getProductKey()));

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
     *  @return the given product entity name
     *  Used to query the database
     *  @param productKey
     **/
    private String getCategory(String productKey) {
        String entityName;
        if (productKey.startsWith(Constants.WATCH_PREFIX))
            entityName = productKey.replaceAll(Constants.ALL_DIGITS_REGEX, "es");
        else if (productKey.startsWith(Constants.BEARD_CARE_PREFIX))
            entityName = productKey.replaceAll(Constants.ALL_DIGITS_REGEX, "");
        else
            entityName = productKey.replaceAll(Constants.ALL_DIGITS_REGEX, "s");

        return entityName;
    }

    /**
     * Removes an item from Firebase that matches the given product key
     * @param product
     **/
    private void removeItem(CategoryProduct product) {
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference(Constants.TABLE_NAME_SHOPPING_CART);

        final String productKey = product.getKey(), userEmail = user.getEmail();

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        ShoppingCartProduct scp = ds.getValue(ShoppingCartProduct.class);
                        if (productKey.equals(scp.getProductKey()) &&
                                userEmail.equals(scp.getUserEmail())) {
                            ds.getRef().removeValue(); // If found, remove it
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });

    }

    /**
     * Retrieves all shopping cart items for the current user and fills the RecyclerView
     **/
    void populateRecyclerView() {

        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference(Constants.TABLE_NAME_SHOPPING_CART);

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final String email = user.getEmail();

        myRef.orderByChild(Constants.KEY_USER_EMAIL).equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {

                        final ShoppingCartProduct currentProduct = ds.getValue(ShoppingCartProduct.class);

                        DatabaseReference myRef1 =
                                FirebaseDatabase.getInstance().getReference(getCategory(currentProduct.getProductKey()));

                        myRef1.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    for (DataSnapshot ds1 : dataSnapshot.getChildren()) {

                                        if (currentProduct.getProductKey().equals(ds1.getKey())) {

                                            Product product = ds1.getValue(Product.class);
                                                CategoryProduct categoryProduct = new CategoryProduct(product.getProductName(), product.getPrice(),
                                                        product.getBrand(), product.getBannerPictureUrl(), ds1.getKey());

                                            shoppingCartProducts.add(categoryProduct); //add the product to the shopping cart

                                            notifyItemInserted(getItemCount()); //notify the adapter than an item has been inserted in the last position

                                            // Update the shipping cost
                                            if(ShoppingCartActivity.getSubtotalCost() <= 75)
                                                ShoppingCartActivity.setShippingCost(Constants.SINGLE_ITEM_SHIPPING_COST);
                                            else
                                                ShoppingCartActivity.setShippingCost(0);
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

    @Override
    public int getItemCount() {
        return shoppingCartProducts.size();
    }

    /**
     *  Provide a direct reference to each of the views
     * used to cache the views within the layout for fast access
     */
    static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView bannerImageView;
        TextView productNameTextView;
        TextView quantityTextView;
        Button incrementButton;
        Button decrementButton;
        TextView totalPriceTextView;
        ImageButton removeItemButton;
        /**
         *  We also create a constructor that does the view lookups to find each subview
         */
        ViewHolder(View itemView) {

            super(itemView);

            bannerImageView = itemView.findViewById(R.id.banner_image_view_shopping_cart);
            productNameTextView = itemView.findViewById(R.id.product_name_text_view);
            quantityTextView = itemView.findViewById(R.id.quantity_text_view);
            incrementButton = itemView.findViewById(R.id.increment_button);
            decrementButton = itemView.findViewById(R.id.decrement_button);
            totalPriceTextView = itemView.findViewById(R.id.total_products_price_text_view);
            removeItemButton = itemView.findViewById(R.id.remove_item_button);
        }
    }
}
