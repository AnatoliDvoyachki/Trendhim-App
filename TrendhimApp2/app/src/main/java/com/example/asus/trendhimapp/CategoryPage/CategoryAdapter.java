package com.example.asus.trendhimapp.CategoryPage;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.asus.trendhimapp.R;

import java.util.List;

class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {

    private Context context;
    private  List<CategoryPage> categoryPageList;

    CategoryAdapter(Context context, List<CategoryPage> categories) {
        this.categoryPageList = categories;
        this.context = context;
    }

    // Access to the context object in the Recycler View
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
        ViewHolder viewHolder = new ViewHolder(contactView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        // Get the data model based on position

        CategoryPage category = categoryPageList.get(position);

        // Set item views based on your views and data model
        TextView productName = viewHolder.productNameTextView;
        productName.setText(category.getName());

        TextView productBrand = viewHolder.productBrandTextView;
        productBrand.setText(category.getBrand());

        TextView productPrice = viewHolder.productPriceTextView;
        productPrice.setText(String.valueOf(category.getPrice()));

       //ImageView productImage = viewHolder.productImage;
        //productImage.setImageResource(category.getImage());

    }

    @Override
    public int getItemCount() {
        return categoryPageList.size();
    }

    // Provide a direct reference to each of the views
    // Used to cache the views within the layout for fast access
    class ViewHolder extends RecyclerView.ViewHolder {

        TextView productNameTextView;
        TextView productPriceTextView;
        TextView productBrandTextView;
        ImageView productImage;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        ViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);

            productNameTextView = itemView.findViewById(R.id.product_name_category);
            productPriceTextView = itemView.findViewById(R.id.product_price_category);
            productBrandTextView = itemView.findViewById(R.id.brand_name_category);
            productImage = itemView.findViewById(R.id.product_image_category);

        }
    }
}
