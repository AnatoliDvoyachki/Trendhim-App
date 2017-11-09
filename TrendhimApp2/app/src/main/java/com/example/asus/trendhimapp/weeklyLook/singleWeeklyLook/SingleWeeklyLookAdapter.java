package com.example.asus.trendhimapp.weeklyLook.singleWeeklyLook;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.asus.trendhimapp.R;
import com.example.asus.trendhimapp.categoryPage.CategoryProduct;
import com.example.asus.trendhimapp.mainActivities.recentProducts.RecentProductsAdapter;
import com.example.asus.trendhimapp.productPage.Product;
import com.example.asus.trendhimapp.productPage.ProductActivity;
import com.example.asus.trendhimapp.util.BitmapFlyweight;
import com.example.asus.trendhimapp.util.Constants;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.List;
import java.util.Objects;

public class SingleWeeklyLookAdapter extends RecyclerView.Adapter<SingleWeeklyLookAdapter.ViewHolder> {

    private Context context;
    private List<CategoryProduct> productsWishList;
    private final String TAG = this.getClass().getSimpleName();

    SingleWeeklyLookAdapter(Context context, List<CategoryProduct> categories) {
        this.productsWishList = categories;
        this.context = context;
    }

    @Override
    public SingleWeeklyLookAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View categoryProductsView = inflater.inflate(R.layout.item_look, parent, false);

        // Return a new holder instance
        return new SingleWeeklyLookAdapter.ViewHolder(categoryProductsView);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onBindViewHolder(SingleWeeklyLookAdapter.ViewHolder viewHolder, final int position) {

        // Get the data model based on position
        final CategoryProduct product = productsWishList.get(position);

        // Set item views based on the views and data model

        TextView productPrice = viewHolder.productPriceTextView;
        productPrice.setText(String.valueOf(product.getPrice() + "€"));

        TextView productName = viewHolder.productNameTextView;
        productName.setText(product.getName());

        Button addToCart = viewHolder.addToCart;
        addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "Add item to the sopping cart!", Toast.LENGTH_LONG).show();
            }
        });

        final ImageView productImage = viewHolder.productImage;
        BitmapFlyweight.getPicture(product.getBannerPictureURL(), productImage);

        /*
         * Handle click events - Redirect to the selected product
         */
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getProduct(product); //Redirect the user to the product activity
            }
        });


    }

    @Override
    public int getItemCount() {
        return productsWishList.size();
    }

    /**
     * Populate the recycler view. Get data from the database which name is equal to the parameter.
     */
    public void addData(final String productKey) {
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference(Constants.TABLE_NAME_WEEKLY_LOOK);
        myRef.addListenerForSingleValueEvent(new ValueEventListener() { //get user recent products
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (final DataSnapshot product : dataSnapshot.getChildren()) {
                        if(Objects.equals(product.getKey(), productKey)){
                            GenericTypeIndicator<List<String>> genericTypeIndicator =
                                    new GenericTypeIndicator<List<String>>() {};
                            List<String> products = product.child("products").getValue(genericTypeIndicator);
                            if( products == null ) {
                                Log.i(TAG, "no products");
                            }
                            else {
                              for(int i = 0; i < products.size(); i++){
                                  String category = getCategory(products.get(i));
                                  Log.i("shit", " " + category);
                                  queryGetProducts(category, products.get(i));
                              }

                            }

                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}

        });
    }

    /**
     * Query to the product category database to get an specific product
     * @param productCategory
     * @param productKey
     */
    private void queryGetProducts(String productCategory, final String productKey){

        //Query to the product category database
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference(productCategory);

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot product : dataSnapshot.getChildren()) {

                        Product p = product.getValue(Product.class);
                        //If the key of the product found is equal to the recent product key
                        if (Objects.equals(product.getKey(), productKey)) {

                            productsWishList.add(0, new CategoryProduct(p.getProductName(), p.getPrice(),
                                    p.getBrand(), p.getBannerPictureUrl(), productKey));

                            // Notify the adapter that an item was inserted in position = 0
                            notifyItemInserted(0);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });

    }

    /**
     *  Provide a direct reference to each of the views
     * used to cache the views within the layout for fast access
     */
    class ViewHolder extends RecyclerView.ViewHolder {

        TextView productPriceTextView, productNameTextView;
        Button addToCart;
        ImageView productImage;

        /**
         *  We also create a constructor that does the view lookups to find each subview
         */
        ViewHolder(View itemView) {
            /*
              Stores the itemView in a public final member variable that can be used
              to access the context from any ViewHolder instance.
             */
            super(itemView);

            productPriceTextView = itemView.findViewById(R.id.product_price_look);
            addToCart = itemView.findViewById(R.id.addLookToCart);
            productImage = itemView.findViewById(R.id.product_image_look);
            productNameTextView = itemView.findViewById(R.id.product_name_look);

        }
    }

    private String getCategory(String productKey){
        String entityName;
        if (productKey.startsWith(Constants.WATCH_PREFIX))
            entityName = productKey.replaceAll(Constants.ALL_DIGITS_REGEX, "es");
        else
            entityName = productKey.replaceAll(Constants.ALL_DIGITS_REGEX, "s");

        return entityName;
    }

    /**
     * Redirect the user to the correct Product
     * @param product
     */
    private void getProduct(final CategoryProduct product) {

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(getCategory(product.getKey()));

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {

                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                        if(Objects.equals(product.getKey(), dataSnapshot1.getKey())) {

                            Product foundProduct = dataSnapshot1.getValue(Product.class);

                            Intent intent = new Intent(context, ProductActivity.class);

                            intent.putExtra(Constants.KEY_PRODUCT_KEY, product.getKey());
                            intent.putExtra(Constants.KEY_PRODUCT_NAME, foundProduct.getProductName());
                            intent.putExtra(Constants.KEY_BRAND_NAME, foundProduct.getBrand());
                            intent.putExtra(Constants.KEY_BANNER_PIC_URL, foundProduct.getBannerPictureUrl());
                            intent.putExtra(Constants.KEY_PRICE, String.valueOf(foundProduct.getPrice()));
                            intent.putExtra(Constants.KEY_LEFT_PIC_URL, foundProduct.getLeftPictureUrl());
                            intent.putExtra(Constants.KEY_RIGHT_PIC_URL, foundProduct.getRightPictureUrl());

                            context.startActivity(intent);
                        }

                    }
                    //Add product to recent activity
                    RecentProductsAdapter.addToRecent(product);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }

}
