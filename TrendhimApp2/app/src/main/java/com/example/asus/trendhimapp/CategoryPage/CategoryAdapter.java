package com.example.asus.trendhimapp.CategoryPage;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.asus.trendhimapp.ProductPage.Products.BitmapFactory;
import com.example.asus.trendhimapp.ProductPage.Products.Product;
import com.example.asus.trendhimapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {

    private Context context;
    private List<CategoryPage> categoryPageList;

    CategoryAdapter(Context context, List<CategoryPage> categories) {
        this.categoryPageList = categories;
        this.context = context;
    }

    /**
     * Access to the context object in the Recycler View
     * @return context
     */
    private Context getContext() {
        return context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.item_category, parent, false);

        // Return a new holder instance
        return new ViewHolder(contactView);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        // Get the data model based on position

        CategoryPage product = categoryPageList.get(position);

        // Set item views based on the views and data model
        TextView productName = viewHolder.productNameTextView;
        productName.setText(product.getName());

        TextView productBrand = viewHolder.productBrandTextView;
        productBrand.setText(product.getBrand());

        TextView productPrice = viewHolder.productPriceTextView;
        productPrice.setText(String.valueOf(product.getPrice()));

        ImageView productImage = viewHolder.productImage;
        BitmapFactory.getPicture(product.getBannerPictureURL(), productImage);

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
                        Product p = product.getValue(Product.class);
                        categoryPageList.add(new CategoryPage(p.getProductName(), p.getPrice(), p.getBrand(), p.getBannerPictureUrl()));
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
}
