package com.example.asus.trendhimapp.wishlistPage;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.asus.trendhimapp.categoryPage.CategoryProduct;
import com.example.asus.trendhimapp.productPage.Product;
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

import java.util.ArrayList;
import java.util.List;

public class WishlistAdapter extends RecyclerView.Adapter<WishlistAdapter.ViewHolder> {
    private String userEmail;
    private Context context;
    private List<CategoryProduct> productList;

    public WishlistAdapter(Context context) {
        this.productList = new ArrayList<>();
        this.context = context;
        setEmail();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View wishlistProducts = inflater.inflate(R.layout.item_wishlist, parent, false);
        return new ViewHolder(wishlistProducts);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
        CategoryProduct currentProduct = productList.get(position);
        BitmapFlyweight.getPicture(currentProduct.getBannerPictureURL(), viewHolder.bannerImageView);
        viewHolder.productNameTextView.setText(currentProduct.getName());
        viewHolder.priceTextView.setText(currentProduct.getPrice() + "€");
        viewHolder.emailTextView.setText(userEmail);
        viewHolder.removeProductImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CategoryProduct selectedProduct = productList.get(position);
                removeItem(selectedProduct);
                productList.remove(selectedProduct);
                notifyDataSetChanged();
                Toast.makeText(context, R.string.remove_success_message, Toast.LENGTH_SHORT).show();
            }
        });
        viewHolder.addToCartImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "TODO ADD TO CART", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    void populateRecyclerView() {
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference(Constants.TABLE_NAME_WISHLIST);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String email = user.getEmail();
            Query query = myRef.orderByChild("userEmail").equalTo(email);
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            WishlistProduct wishProd = ds.getValue(WishlistProduct.class);
                            final String entityName = wishProd.getEntityName(), productKey = wishProd.getProductKey();
                            DatabaseReference myRef1 = FirebaseDatabase.getInstance().getReference(entityName);
                            myRef1.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.exists()) {
                                        for (DataSnapshot ds1 : dataSnapshot.getChildren()) {
                                            if (productKey.equals(ds1.getKey())) {
                                                Product currentProduct = ds1.getValue(Product.class);
                                                CategoryProduct categoryProduct = new CategoryProduct(currentProduct.getProductName(),
                                                        currentProduct.getPrice(), currentProduct.getBrand(),
                                                        currentProduct.getBannerPictureUrl(), ds1.getKey());
                                                productList.add(categoryProduct);
                                                notifyDataSetChanged();
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
    }

    private void setEmail() {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null) {
            this.userEmail = firebaseUser.getEmail();
        }
    }

    /**
     * Removes a product from the user's wishlist
     **/
    private void removeItem(final CategoryProduct categoryProduct) {
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference(Constants.TABLE_NAME_WISHLIST);
        final String productKey = categoryProduct.getKey();
        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        WishlistProduct wishProd = ds.getValue(WishlistProduct.class);
                        if (productKey.equals(wishProd.getProductKey()) && userEmail.equals(wishProd.getUserEmail())) {
                            ds.getRef().removeValue();
                            notifyDataSetChanged();
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }

    /**
     * View holder pattern implementation
     **/
    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView bannerImageView;
        TextView productNameTextView;
        TextView priceTextView;
        TextView emailTextView;
        ImageButton removeProductImageButton;
        ImageButton addToCartImageButton;

        ViewHolder(View itemView) {
            super(itemView);
            bannerImageView = itemView.findViewById(R.id.banner_image_view);
            productNameTextView = itemView.findViewById(R.id.product_name_text_view);
            priceTextView = itemView.findViewById(R.id.price_text_view);
            emailTextView = itemView.findViewById(R.id.email_text_field);
            removeProductImageButton = itemView.findViewById(R.id.remove_button);
            addToCartImageButton = itemView.findViewById(R.id.add_to_cart_button);
        }
    }
}
