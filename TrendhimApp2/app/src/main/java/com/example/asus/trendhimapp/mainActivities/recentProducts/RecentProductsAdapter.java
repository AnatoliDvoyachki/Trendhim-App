package com.example.asus.trendhimapp.mainActivities.recentProducts;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.asus.trendhimapp.categoryPage.CategoryProduct;
import com.example.asus.trendhimapp.mainActivities.MainActivity;
import com.example.asus.trendhimapp.productPage.Product;
import com.example.asus.trendhimapp.productPage.ProductActivity;
import com.example.asus.trendhimapp.R;
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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class RecentProductsAdapter extends RecyclerView.Adapter<RecentProductsAdapter.ViewHolder> {

    private Context context;
    private List<CategoryProduct> recentProducts;

    public RecentProductsAdapter(Context context, List<CategoryProduct> recentProducts) {
        this.recentProducts = recentProducts;
        this.context = context;
    }

    @Override
    public RecentProductsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View recentProductsView = inflater.inflate(R.layout.item_product, parent, false);

        // Return a new holder instance
        return new ViewHolder(recentProductsView);
    }

    @Override
    public void onBindViewHolder(RecentProductsAdapter.ViewHolder viewHolder, int position) {

        // Get the data model based on position
        final CategoryProduct product = recentProducts.get(position);
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
                //Query the recent_product database to get the product category
                final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("recent_products");
                //get product which key is equal to the one clicked
                databaseReference.orderByChild("order")
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if(dataSnapshot.exists()){
                                    for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                        //Found product
                                        final RecentProduct recentProduct = dataSnapshot1.getValue(RecentProduct.class);
                                        if(Objects.equals(recentProduct.getKey(), product.getKey())) {
                                            //Query to the product category to get the product information
                                            DatabaseReference category_products_database =
                                                    FirebaseDatabase.getInstance().getReference(getCategory(recentProduct.getKey()));

                                            category_products_database.addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                    if (dataSnapshot.exists()) {
                                                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                                            //If the object key is correct
                                                            if(Objects.equals(product.getKey(), dataSnapshot1.getKey())) {

                                                                Product foundProduct = dataSnapshot1.getValue(Product.class);
                                                                Intent intent = new Intent(context, ProductActivity.class);

                                                                intent.putExtra("productKey", product.getKey());
                                                                intent.putExtra("productName", foundProduct.getProductName());
                                                                intent.putExtra("brand", foundProduct.getBrand());
                                                                intent.putExtra("bannerPictureUrl", foundProduct.getBannerPictureUrl());
                                                                intent.putExtra("price", String.valueOf(foundProduct.getPrice()));
                                                                intent.putExtra("leftPictureUrl", foundProduct.getLeftPictureUrl());
                                                                intent.putExtra("rightPictureUrl", foundProduct.getRightPictureUrl());

                                                                context.startActivity(intent);

                                                            }
                                                        }

                                                        addToRecent(product);
                                                        MainActivity.adapter.notifyDataSetChanged();

                                                    }
                                                }

                                                @Override
                                                public void onCancelled(DatabaseError databaseError) {

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
        });

    }

    @Override
    public int getItemCount() {
        return recentProducts.size();
    }

    private String getCategory(String productKey){
        String entityName;
        if (productKey.startsWith(Constants.WATCH_REGEX))
            entityName = productKey.replaceAll(Constants.ALL_DIGITS_REGEX, "es");
        else
            entityName = productKey.replaceAll(Constants.ALL_DIGITS_REGEX, "s");

        return entityName;
    }


    /**
     * Populate the recycler view. Get data from the database which name is equal to the parameter.
     */
    public void addData() {
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference(Constants.TABLE_NAME_RECENT_PRODUCTS);
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null) { //Add products to the recent products recycler view if the user is logged in
            Query query = myRef.orderByChild("order");
            query.addListenerForSingleValueEvent(new ValueEventListener() { //get user recent products
                @Override
                public void onDataChange(final DataSnapshot dataSnapshot) {

                    if (dataSnapshot.exists()) {

                        for (final DataSnapshot product : dataSnapshot.getChildren()) {
                            //Found product
                            final RecentProduct recentProduct = product.getValue(RecentProduct.class);
                            if (Objects.equals(recentProduct.getEmail(), user.getEmail())) {
                                final String productKey = recentProduct.getKey(); //Product key

                                getProducts(user, getCategory(productKey), productKey);
                            }

                        }
                    } else {
                        MainActivity.noRecentProducts.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }

            });
        }

    }

    public static void addToRecent(final CategoryProduct product) {

        final DatabaseReference myRef = FirebaseDatabase.getInstance().getReference(Constants.TABLE_NAME_RECENT_PRODUCTS);
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        final boolean[] exists = {false};

        if(user != null){ //if the user is logged in

            myRef.orderByChild("order").addListenerForSingleValueEvent(new ValueEventListener() { // get user recent products

                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                        RecentProduct recentProduct = dataSnapshot1.getValue(RecentProduct.class);

                        if(Objects.equals(recentProduct.getKey(), product.getKey())
                                && Objects.equals(recentProduct.getEmail(), user.getEmail())) {

                            //if the product is added to the database
                            Date curDate = new Date();

                            dataSnapshot1.getRef().child("visit").setValue(convertDateToString(curDate));
                            dataSnapshot1.getRef().child("order").setValue("-" + convertDateToString(curDate));
                            exists[0] = true;
                            break;
                        }
                    }

                    if(!exists[0]){
                        Date curDate = new Date();
                        Map<String, String> values = new HashMap<>();
                        values.put("key", product.getKey());
                        values.put("email", user.getEmail());
                        values.put("order", "-" + convertDateToString(curDate)); //order the elements in descending date
                        myRef.push().setValue(values);
                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }

    private void getProducts(FirebaseUser user, String productCategory, final String productKey){

        //Query to the product category database
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference(productCategory);

        if(user != null){
            myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        for (DataSnapshot product : dataSnapshot.getChildren()) {
                            Product p = product.getValue(Product.class);
                            //If the key of the product found is equal to the recent product key
                            if (Objects.equals(product.getKey(), productKey)) {

                                recentProducts.add(0, new CategoryProduct(p.getProductName(), p.getPrice(),
                                        p.getBrand(), p.getBannerPictureUrl(), productKey));

                                // Notify the adapter that an item was inserted in position = 0
                                notifyItemInserted(0);
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

    /**
     * Convert Date to String
     * @param date
     * @return Date formatted into a date
     */
    private static String convertDateToString(Date date) {
        @SuppressLint("SimpleDateFormat") DateFormat dateFormatter = new SimpleDateFormat("ddhhmmss");
        return dateFormatter.format(date);
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

            productNameTextView = itemView.findViewById(R.id.product_name_main);
            productPriceTextView = itemView.findViewById(R.id.product_price_main);
            productBrandTextView = itemView.findViewById(R.id.brand_name_main);
            productImage = itemView.findViewById(R.id.product_image_main);
        }
    }

}