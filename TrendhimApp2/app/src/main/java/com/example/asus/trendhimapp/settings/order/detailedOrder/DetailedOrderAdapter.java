package com.example.asus.trendhimapp.settings.order.detailedOrder;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;
import java.util.Objects;

public class DetailedOrderAdapter extends RecyclerView.Adapter<DetailedOrderAdapter.ViewHolder> {

    private List<CategoryProduct> products;
    private Context context;

    DetailedOrderAdapter(Context context, List<CategoryProduct> products) {
        this.products = products;
        this.context = context;
    }

    @Override
    public DetailedOrderAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View newProductsView = inflater.inflate(R.layout.detailed_order, parent, false);

        // Return a new holder instance
        return new DetailedOrderAdapter.ViewHolder(newProductsView);
    }

    @Override
    public void onBindViewHolder(DetailedOrderAdapter.ViewHolder viewHolder, int position) {

        // Get the data model based on position
        final CategoryProduct product = products.get(position);

        // Set item views based on the views and data model
        TextView date = viewHolder.productName;
        date.setText(product.getName());

        TextView address = viewHolder.brand;
        address.setText(product.getBrand());

        TextView productPrice = viewHolder.price;
        productPrice.setText(String.format(Constants.PRICE_FORMAT, product.getPrice()));

        ImageView productImage = viewHolder.detailed_order_image;
        BitmapFlyweight.getPicture(product.getBannerPictureURL(), productImage);

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getProduct(product);
            }
        });
    }

    @Override
    public int getItemCount() {
        return products.size();
    }


    /**
     * Populate the recycler view. Get data from the database which key is equal to one in the parameter.
     */
    public void addData(String key) {
        final DatabaseReference myRef = FirebaseDatabase.getInstance().getReference(Constants.TABLE_NAME_ORDERS);
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if(currentUser != null){
            myRef.orderByKey().equalTo(key)
                    .addListenerForSingleValueEvent(new ValueEventListener() { //get user orders
                @Override
                public void onDataChange(final DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        for (final DataSnapshot product : dataSnapshot.getChildren()) {
                            for(DataSnapshot dataSnapshot1 : product.child(Constants.KEY_PRODUCTS).getChildren()){
                                String foundProductKey = dataSnapshot1.child(Constants.KEY_PRODUCT_KEY).getValue().toString();
                                String category = getCategory(foundProductKey);
                                queryGetProducts(category, foundProductKey);
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {}

            });
        } else {
            Toast.makeText(context, R.string.need_to_log_in_first_message, Toast.LENGTH_LONG).show();
        }

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

                            Intent toProductActivity = new Intent(context, ProductActivity.class);

                            toProductActivity.putExtra(Constants.KEY_PRODUCT_KEY, product.getKey());
                            toProductActivity.putExtra(Constants.KEY_PRODUCT_NAME, foundProduct.getProductName());
                            toProductActivity.putExtra(Constants.KEY_BRAND_NAME, foundProduct.getBrand());
                            toProductActivity.putExtra(Constants.KEY_BANNER_PIC_URL, foundProduct.getBannerPictureUrl());
                            toProductActivity.putExtra(Constants.KEY_PRICE, String.valueOf(foundProduct.getPrice()));
                            toProductActivity.putExtra(Constants.KEY_LEFT_PIC_URL, foundProduct.getLeftPictureUrl());
                            toProductActivity.putExtra(Constants.KEY_RIGHT_PIC_URL, foundProduct.getRightPictureUrl());

                            context.startActivity(toProductActivity);
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

                        Product current = product.getValue(Product.class);
                        //If the key of the product found is equal to the recent product key
                        if (Objects.equals(product.getKey(), productKey)) {

                            products.add(0, new CategoryProduct(current.getProductName(), current.getPrice(),
                                    current.getBrand(), current.getBannerPictureUrl(), productKey));

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
     * @param productKey
     * @return product category based on the given product key
     */
    private String getCategory(String productKey){
        String entityName;
        if (productKey.startsWith(Constants.WATCH_PREFIX))
            entityName = productKey.replaceAll(Constants.ALL_DIGITS_REGEX, "es");
        else if(productKey.startsWith(Constants.BEARD_CARE_PREFIX))
            entityName = productKey.replaceAll(Constants.ALL_DIGITS_REGEX, "");
        else
            entityName = productKey.replaceAll(Constants.ALL_DIGITS_REGEX, "s");

        return entityName;
    }

    /**
     *  Provide a direct reference to each of the views
     * used to cache the views within the layout for fast access
     */
    class ViewHolder extends RecyclerView.ViewHolder {

        TextView productName, brand, price;
        ImageView detailed_order_image;

        /**
         *  We also create a constructor that does the view lookups to find each subview
         */
        ViewHolder(View itemView) {
        /*
          Stores the itemView in a public final member variable that can be used
          to access the context from any ViewHolder instance.
         */
            super(itemView);

            price = itemView.findViewById(R.id.detailed_price_order);
            productName = itemView.findViewById(R.id.product_name_order);
            brand = itemView.findViewById(R.id.brand_detailed_order);
            detailed_order_image = itemView.findViewById(R.id.detailed_order_image);

        }
    }

}