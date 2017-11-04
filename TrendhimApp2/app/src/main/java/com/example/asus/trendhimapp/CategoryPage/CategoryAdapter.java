package com.example.asus.trendhimapp.CategoryPage;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.asus.trendhimapp.MainActivities.RecentProducts.RecentProduct;
import com.example.asus.trendhimapp.ProductPage.ProductActivity;
import com.example.asus.trendhimapp.Util.BitmapFlyweight;
import com.example.asus.trendhimapp.ProductPage.Product;
import com.example.asus.trendhimapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {

    private Context context;
    private List<CategoryProduct> categoryPageList;
    private String category;

    CategoryAdapter(Context context, List<CategoryProduct> categories, String category) {
        this.categoryPageList = categories;
        this.context = context;
        this.category = category;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View categoryProductsView = inflater.inflate(R.layout.item_category, parent, false);

        // Return a new holder instance
        return new ViewHolder(categoryProductsView);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        // Get the data model based on position

        final CategoryProduct product = categoryPageList.get(position);

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
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(category);
                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                if(Objects.equals(product.getKey(), dataSnapshot1.getKey())) {
                                    Product foundProduct = dataSnapshot1.getValue(Product.class);

                                    Intent intent = new Intent(context, ProductActivity.class);

                                    intent.putExtra("productName", foundProduct.getProductName());
                                    intent.putExtra("brand", foundProduct.getBrand());
                                    intent.putExtra("bannerPictureUrl", foundProduct.getBannerPictureUrl());
                                    intent.putExtra("price", String.valueOf(foundProduct.getPrice()));
                                    intent.putExtra("leftPictureUrl", foundProduct.getLeftPictureUrl());
                                    intent.putExtra("rightPictureUrl", foundProduct.getRightPictureUrl());
                                    context.startActivity(intent);
                                }

                            }
                            //Add product to recent activity
                            addToRecent(product);

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
        return categoryPageList.size();
    }

    /**
     * Populate the recycler view. Get data from the database which name is equal to the parameter.
     * @param category
     */
    void addData(String category) {
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference(category);
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for(DataSnapshot product : dataSnapshot.getChildren()) {
                        String productKey = product.getKey();
                        Product p = product.getValue(Product.class);
                        categoryPageList.add(new CategoryProduct(
                                p.getProductName(), p.getPrice(), p.getBrand(), p.getBannerPictureUrl(), productKey));
                        // Notify the adapter that an item was inserted in position = getItemCount()
                        notifyItemInserted(getItemCount());

                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

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

    private void addToRecent(final CategoryProduct product) {

        final DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("recent_products");
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final boolean[] exists = {false};
        if(user != null){ //if the user is logged in
            Query query = myRef.orderByChild("user_email").equalTo(user.getEmail()); //get user recent products

            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                        RecentProduct recentProduct = dataSnapshot1.getValue(RecentProduct.class);
                        if(Objects.equals(recentProduct.getKey(), product.getKey())) {
                            //if the product is not added to the database
                            Log.i("shit", product.getKey());
                            exists[0] = true;
                            break;
                        }
                    }

                    if(!exists[0]){
                        Map<String, String> values = new HashMap<>();
                        values.put("key", product.getKey());
                        values.put("user_email", user.getEmail());
                        values.put("category", category);
                        myRef.push().setValue(values);
                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }
    }

}
