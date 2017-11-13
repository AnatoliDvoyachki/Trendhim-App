package com.example.asus.trendhimapp.shoppingCart;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import com.example.asus.trendhimapp.productPage.Product;
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

        // Set initial quantity
        final TextView quantityTextView = holder.quantityTextView;
        final TextView currentProductPriceTextView = holder.totalPriceTextView;

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

                                // Update the total cost
                                if(ShoppingCartActivity.getSubtotalCost() <= 75) {
                                    ShoppingCartActivity.setShippingCost(5);
                                } else {
                                    ShoppingCartActivity.setShippingCost(0);
                                }

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

                                // Setup the plus button
                                Button incrementButton = holder.incrementButton;
                                incrementButton.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        int currentQuantity = Integer.parseInt(quantityTextView.getText().toString());
                                        Log.i("shit", "QUANTITY + " + currentQuantity);

                                        // Update current item total price
                                        quantityTextView.setText(String.valueOf(++currentQuantity));
                                        int currentItemTotalPrice = currentQuantity * currentProduct.getPrice();
                                        currentProductPriceTextView.setText(String.valueOf(currentItemTotalPrice) + "€");
                                        Log.i("shit", "item total Price + " + currentItemTotalPrice);
                                        ds.getRef().child("quantity").setValue(String.valueOf(currentQuantity));


                                        Log.i("shit", "shipping Price + " + ShoppingCartActivity.getShippingCost());

                                        // Update subtotal price
                                        Log.i("shit", "current subtotal Price + " + ShoppingCartActivity.getSubtotalCost());
                                        int currentSubtotal = ShoppingCartActivity.getSubtotalCost() + (currentProduct.getPrice());
                                        ShoppingCartActivity.setSubtotal(currentSubtotal);
                                        Log.i("shit", "current subtotal Price + " + ShoppingCartActivity.getSubtotalCost());

                                        if(ShoppingCartActivity.getSubtotalCost() <= 75) {
                                            ShoppingCartActivity.setShippingCost(5);
                                        } else {
                                            ShoppingCartActivity.setShippingCost(0);
                                        }
                                        Log.i("shit", "shipping Price + " + ShoppingCartActivity.getShippingCost());

                                        // Update grand total price
                                        int grandTotal = ShoppingCartActivity.getSubtotalCost() + ShoppingCartActivity.getShippingCost();
                                        ShoppingCartActivity.setGrandTotalCost(grandTotal);
                                        Log.i("shit", "current grand total Price1 + " + ShoppingCartActivity.getGrandTotalCost());


                                    }
                                });

                                // Setup the minus button
                                Button decrementButton = holder.decrementButton;
                                decrementButton.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        int currentQuantity = Integer.parseInt(quantityTextView.getText().toString());
                                        if (currentQuantity - 1 >= 1) {

                                            // Update current item total price
                                            quantityTextView.setText(String.valueOf(--currentQuantity));
                                            int currentItemTotalPrice = currentQuantity * currentProduct.getPrice();
                                            currentProductPriceTextView.setText(String.valueOf(currentItemTotalPrice) + "€");
                                            ds.getRef().child("quantity").setValue(String.valueOf(currentQuantity));

                                            // Update subtotal price
                                            int currentSubtotal = ShoppingCartActivity.getSubtotalCost() - currentProduct.getPrice();

                                            ShoppingCartActivity.setSubtotal(currentSubtotal);

                                            if(ShoppingCartActivity.getSubtotalCost() <= 75) {
                                                ShoppingCartActivity.setShippingCost(5);
                                            } else {
                                                ShoppingCartActivity.setShippingCost(0);
                                            }

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
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }

    }

    private void initializeRemoveButton(final CategoryProduct currentProduct) {
        // Create the confirmation dialog
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
                    ShoppingCartActivity.setShippingCost(5);
                } else {
                    ShoppingCartActivity.setShippingCost(0);
                }

                // Reset subtotal and grand total so
                // they can be recalculated by notifyDataSetChanged()
                ShoppingCartActivity.setSubtotal(0);
                ShoppingCartActivity.setGrandTotalCost(0);

                notifyDataSetChanged(); // Notify for the change

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

        // Allign the buttons in the center of the dialog window
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) btnPositive.getLayoutParams();
        layoutParams.weight = 10;
        btnPositive.setLayoutParams(layoutParams);
        btnNegative.setLayoutParams(layoutParams);
    }

    /**
     *  @return the entity name of the product with @param productKey
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
     * Removes an item from Firebase that matches the product key of @param product
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

                                            shoppingCartProducts.add(categoryProduct);

                                            notifyItemInserted(getItemCount());

                                            // Update the shipping cost
                                            if(ShoppingCartActivity.getSubtotalCost() <= 75) {
                                                ShoppingCartActivity.setShippingCost(5);
                                            } else {
                                                ShoppingCartActivity.setShippingCost(0);
                                            }


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
     * View holder pattern implementation
     **/
    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView bannerImageView;
        TextView productNameTextView;
        TextView quantityTextView;
        Button incrementButton;
        Button decrementButton;
        TextView totalPriceTextView;
        ImageButton removeItemButton;

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