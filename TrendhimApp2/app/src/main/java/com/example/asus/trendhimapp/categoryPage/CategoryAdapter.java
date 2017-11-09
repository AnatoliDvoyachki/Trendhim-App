package com.example.asus.trendhimapp.categoryPage;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.asus.trendhimapp.mainActivities.recentProducts.RecentProductsAdapter;
import com.example.asus.trendhimapp.productPage.Product;
import com.example.asus.trendhimapp.productPage.ProductActivity;
import com.example.asus.trendhimapp.R;
import com.example.asus.trendhimapp.util.BitmapFlyweight;
import com.example.asus.trendhimapp.util.Constants;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;
import java.util.Objects;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {

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

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        // Get the data model based on position
        final CategoryProduct product = categoryPageList.get(position);

        // Set item views based on the views and data model
        TextView productName = viewHolder.productNameTextView;
        productName.setText(product.getName());

        TextView productBrand = viewHolder.productBrandTextView;
        productBrand.setText(product.getBrand());

        TextView productPrice = viewHolder.productPriceTextView;
        productPrice.setText(String.valueOf(product.getPrice()) + "€");

        final ImageView productImage = viewHolder.productImage;
        BitmapFlyweight.getPicture(product.getBannerPictureURL(), productImage);

        /*
         * Handle touch events - Change the image alpha
         */
        viewHolder.itemView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_UP:
                        productImage.animate().alpha(1f);
                        getProduct(product); //Redirect the user to the product activity
                        break;
                    case MotionEvent.ACTION_DOWN:
                        productImage.animate().alpha(0.7f);
                        break;
                }
                return true;
            }
        });

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

    /**
     * Redirect the user to the correct Product
     * @param product
     */
    private void getProduct(final CategoryProduct product) {

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(category);

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
