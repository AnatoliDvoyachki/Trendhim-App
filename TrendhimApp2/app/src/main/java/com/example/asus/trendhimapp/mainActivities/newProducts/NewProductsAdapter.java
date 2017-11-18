package com.example.asus.trendhimapp.mainActivities.newProducts;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

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
import com.google.firebase.database.ValueEventListener;

import java.util.List;
import java.util.Objects;

public class NewProductsAdapter extends RecyclerView.Adapter<NewProductsAdapter.ViewHolder> {

    private Context context;
    private List<CategoryProduct> newProducts;

    public NewProductsAdapter(Context context, List<CategoryProduct> newProducts) {
        this.newProducts = newProducts;
        this.context = context;
    }

    @Override
    public NewProductsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View newProductsView = inflater.inflate(R.layout.item_category, parent, false);

        // Return a new holder instance
        return new ViewHolder(newProductsView);
    }

    @Override
    public void onBindViewHolder(NewProductsAdapter.ViewHolder viewHolder, int position) {

        // Get the data model based on position
        final CategoryProduct product = newProducts.get(position);

        // Set item views based on the views and data model
        TextView productName = viewHolder.productNameTextView;
        productName.setText(product.getName());

        TextView productBrand = viewHolder.productBrandTextView;
        productBrand.setText(product.getBrand());

        TextView productPrice = viewHolder.productPriceTextView;
        productPrice.setText(String.valueOf(product.getPrice()));

        ImageView productImage = viewHolder.productImage;
        BitmapFlyweight.getPicture(product.getBannerPictureURL(), productImage);

        /*
        Handle touch event - Redirect to the selected product
        */
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getProduct(product, view);
            }
        });
    }

    @Override
    public int getItemCount() {
        return newProducts.size();
    }

    /**
     * Populate the recycler view. Get data from the Recommended products database
     */
    public void addData() {
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference(Constants.TABLE_NAME_RECOMMENDED_PRODUCTS);
        myRef.orderByChild(Constants.KEY_ORDER).addListenerForSingleValueEvent(new ValueEventListener() { //get user recent products
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {

                    for (final DataSnapshot product : dataSnapshot.getChildren()) {
                        //Found product
                        final NewProduct newProduct = product.getValue(NewProduct.class);
                        final String productKey = newProduct.getKey(); //Product key
                        String productCategory = newProduct.getCategory(); //Product category

                        queryGetProducts(productCategory, productKey);

                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}

        });

    }

    /**
     * Query to the product category entity to get a specific product
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
                        //If the key of the product found is equal to the product key
                        if (Objects.equals(product.getKey(), productKey)) {

                            newProducts.add(0, new CategoryProduct(p.getProductName(), p.getPrice(),
                                    p.getBrand(), p.getBannerPictureUrl(), productKey));

                            // Notify the adapter that an item was inserted in the first position = 0
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
     * Redirect the user to the correct Product
     * @param product
     */
    private void getProduct(final CategoryProduct product, final View view) {

        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(Constants.TABLE_NAME_RECOMMENDED_PRODUCTS);
        //get product which key is equal to the one clicked
        databaseReference.orderByChild(Constants.KEY_ATTR_KEY).equalTo(product.getKey())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                                //Found product
                                final NewProduct newProduct = dataSnapshot1.getValue(NewProduct.class);

                                //Query to the product category to get the product information
                                DatabaseReference category_products_database =
                                        FirebaseDatabase.getInstance().getReference(newProduct.getCategory());

                                category_products_database.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        if (dataSnapshot.exists()) {
                                            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                                //If the object key is correct
                                                if(Objects.equals(product.getKey(), dataSnapshot1.getKey())) {

                                                    Product foundProduct = dataSnapshot1.getValue(Product.class);
                                                    Intent intent = new Intent(context, ProductActivity.class);

                                                    ActivityOptionsCompat options = ActivityOptionsCompat.
                                                            makeSceneTransitionAnimation((Activity) context, view, "profile");

                                                    intent.putExtra(Constants.KEY_PRODUCT_KEY, product.getKey());
                                                    intent.putExtra(Constants.KEY_PRODUCT_NAME, foundProduct.getProductName());
                                                    intent.putExtra(Constants.KEY_BRAND_NAME, foundProduct.getBrand());
                                                    intent.putExtra(Constants.KEY_BANNER_PIC_URL, foundProduct.getBannerPictureUrl());
                                                    intent.putExtra(Constants.KEY_PRICE, String.valueOf(foundProduct.getPrice()));
                                                    intent.putExtra(Constants.KEY_LEFT_PIC_URL, foundProduct.getLeftPictureUrl());
                                                    intent.putExtra(Constants.KEY_RIGHT_PIC_URL, foundProduct.getRightPictureUrl());

                                                    context.startActivity(intent, options.toBundle());

                                                }
                                            }

                                           RecentProductsAdapter.addToRecent(product);

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
     *  Provide a direct reference to each of the views
     * used to cache the views within the layout for fast access
     */
    class ViewHolder extends RecyclerView.ViewHolder {

        TextView productNameTextView;
        TextView productPriceTextView;
        TextView productBrandTextView;
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

            productNameTextView = itemView.findViewById(R.id.product_name_category);
            productPriceTextView = itemView.findViewById(R.id.product_price_category);
            productBrandTextView = itemView.findViewById(R.id.brand_name_category);
            productImage = itemView.findViewById(R.id.product_image_category);
        }
    }

}