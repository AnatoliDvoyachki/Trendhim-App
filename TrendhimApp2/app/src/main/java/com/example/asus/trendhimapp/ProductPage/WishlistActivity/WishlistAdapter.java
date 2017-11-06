package com.example.asus.trendhimapp.ProductPage.WishlistActivity;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.asus.trendhimapp.CategoryPage.CategoryProduct;
import com.example.asus.trendhimapp.ProductPage.Product;
import com.example.asus.trendhimapp.R;
import com.example.asus.trendhimapp.Util.BitmapFlyweight;
import com.example.asus.trendhimapp.Util.Constants;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.List;
import java.util.Objects;

public class WishlistAdapter extends RecyclerView.Adapter<WishlistAdapter.ViewHolder> {
    private String userEmail;
    private Context context;
    private List<CategoryProduct> productList;

    public WishlistAdapter(Context context, List<CategoryProduct> productList, String userEmail) {
        this.productList = productList;
        this.context = context;
        this.userEmail = userEmail;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View wishlistProducts = inflater.inflate(R.layout.item_wishlist, parent, false);
        return new ViewHolder(wishlistProducts);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int position) {
        CategoryProduct currentProduct = productList.get(position);
        BitmapFlyweight.getPicture(currentProduct.getBannerPictureURL(), viewHolder.bannerImageView);
        viewHolder.productNameTextView.setText(currentProduct.getName());
        viewHolder.priceTextView.setText(Integer.toString(currentProduct.getPrice()));
        viewHolder.emailTextView.setText(userEmail);
        viewHolder.removeImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Soon to be implemented - Remove button", Toast.LENGTH_SHORT).show();
            }
        });
        viewHolder.addToCartImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Soon to be implemented - Add to cart", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    /**
     * Populate the recycler view. Get data from the database which name is equal to the parameter.
     */
    void addData() {
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference(Constants.TABLE_NAME_WISHLIST);
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if(user != null) { //Add products to the recent products recycler view if the user is logged in
            Query query = myRef.orderByKey();
            query.addListenerForSingleValueEvent(new ValueEventListener() { //get user recent products
                @Override
                public void onDataChange(final DataSnapshot dataSnapshot) {

                    if (dataSnapshot.exists()) {

                        for (final DataSnapshot product : dataSnapshot.getChildren()) {
                            //Found product
                            final WishlistProduct wishlistProduct = product.getValue(WishlistProduct.class);

                            final String productKey = wishlistProduct.getProductKey(); //Product key
                            String productCategory = wishlistProduct.getEntityName(); //Product category

                            //Query to the product category database
                            DatabaseReference myRef = FirebaseDatabase.getInstance().getReference(productCategory);

                            if(user != null){
                                Query query = myRef.orderByChild(productKey);
                                query.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        if (dataSnapshot.exists()) {
                                            for (DataSnapshot product : dataSnapshot.getChildren()) {

                                                Product p = product.getValue(Product.class);
                                                //If the key of the product found is equal to the recent product key
                                                if (Objects.equals(product.getKey(), productKey)) {

                                                    productList.add(0, new CategoryProduct(p.getProductName(), p.getPrice(),
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
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }

            });
        }

    }
    /**
     * View holder pattern implementation
     **/
    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView bannerImageView;
        TextView productNameTextView;
        TextView priceTextView;
        TextView emailTextView;
        ImageButton removeImageButton;
        ImageButton addToCartImageButton;

        ViewHolder(View itemView) {
            super(itemView);
            bannerImageView = itemView.findViewById(R.id.banner_image_view);
            productNameTextView = itemView.findViewById(R.id.product_name_text_view);
            priceTextView = itemView.findViewById(R.id.price_text_view);
            emailTextView = itemView.findViewById(R.id.email_text_field);
            removeImageButton = itemView.findViewById(R.id.remove_button);
            addToCartImageButton = itemView.findViewById(R.id.add_to_cart_button);
        }

    }
}
