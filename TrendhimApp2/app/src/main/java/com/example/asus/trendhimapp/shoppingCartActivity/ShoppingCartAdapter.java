package com.example.asus.trendhimapp.shoppingCartActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.asus.trendhimapp.R;
import com.example.asus.trendhimapp.categoryPage.CategoryProduct;
import com.example.asus.trendhimapp.productPage.Product;
import com.example.asus.trendhimapp.util.Constants;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class ShoppingCartAdapter extends RecyclerView.Adapter<ShoppingCartAdapter.ViewHolder> {

    private List<CategoryProduct> shoppingCartProducts;
    private Context context;

    ShoppingCartAdapter(Context context, List<CategoryProduct> shoppingCartProducts) {
        this.context = context;
        this.shoppingCartProducts = shoppingCartProducts;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View shoppingCartProds = inflater.inflate(R.layout.item_shopping_cart, parent, false);

        return new ViewHolder(shoppingCartProds);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final CategoryProduct currentProduct = shoppingCartProducts.get(position);

        ImageView bannerImageView = holder.bannerImageView;
        BitmapFlyweight.getPicture(currentProduct.getBannerPictureURL(), bannerImageView);

        TextView productNameTextView = holder.productNameTextView;
        productNameTextView.setText(currentProduct.getName());

        final TextView quantityTextView = holder.quantityTextView;
        quantityTextView.setText("1");
       // quantityTextView.setText(String.valueOf(currentProduct.getQuantity()));

        final TextView total_products_price = holder.totalPriceTextView;
        total_products_price.setText(String.valueOf(currentProduct.getPrice()) + "€");

        ImageButton removeItemButton = holder.removeItemButton;
        removeItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                   Toast.makeText(context, "UNIMPLEMENTED", Toast.LENGTH_SHORT).show();
              }
            });

        //TextView totalPriceTextView = holder.totalPriceTextView;
        Button incrementButton = holder.incrementButton;
        incrementButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentQuantity = Integer.parseInt(quantityTextView.getText().toString());
                if (currentQuantity + 1 < Integer.MAX_VALUE) {
                    quantityTextView.setText(String.valueOf(++currentQuantity));
                    double totalPrice = currentQuantity * currentProduct.getPrice();
                    total_products_price.setText(String.valueOf(totalPrice));
                }
            }
        });

        Button decrementButton = holder.decrementButton;
        decrementButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentQuantity = Integer.parseInt(quantityTextView.getText().toString());
                if (currentQuantity - 1 >= 1) {
                    quantityTextView.setText(String.valueOf(--currentQuantity));
                    double totalPrice = currentQuantity * currentProduct.getPrice();
                    total_products_price.setText(String.valueOf(totalPrice));
                }
            }
        });
    }

    private String getCategory(String productKey){
        String entityName;
        if (productKey.startsWith(Constants.WATCH_REGEX))
            entityName = productKey.replaceAll(Constants.ALL_DIGITS_REGEX, "es");
        else
            entityName = productKey.replaceAll(Constants.ALL_DIGITS_REGEX, "s");

        return entityName;
    }

    void populateRecyclerView() {

        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference(Constants.TABLE_NAME_SHOPPING_CART);

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final String email = user.getEmail();

        myRef.orderByChild("userEmail").equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {

                        final ShoppingCartProduct scp = ds.getValue(ShoppingCartProduct.class);

                            DatabaseReference myRef1 =
                                    FirebaseDatabase.getInstance().getReference(getCategory(scp.getProductKey()));
                            myRef1.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.exists()) {
                                        for (DataSnapshot ds1 : dataSnapshot.getChildren()) {

                                            if (scp.getProductKey().equals(ds1.getKey())) {

                                                Product product = ds1.getValue(Product.class);

                                                shoppingCartProducts.add(0, new CategoryProduct(product.getProductName(), product.getPrice(),
                                                        product.getBrand(), product.getBannerPictureUrl(), ds1.getKey()));

                                                notifyItemInserted(getItemCount());
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

    @Override
    public int getItemCount() {
        return shoppingCartProducts.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView bannerImageView;
        TextView productNameTextView;
        TextView quantityTextView;
        Button incrementButton;
        Button decrementButton;
        TextView totalPriceTextView;
        ImageButton removeItemButton;

        ViewHolder(View itemView) {
            super(itemView);
            bannerImageView = itemView.findViewById(R.id.banner_image_view_shopping_cart);
            productNameTextView = itemView.findViewById(R.id.product_name_text_view);
            quantityTextView = itemView.findViewById(R.id.quantity_text_view);
            incrementButton = itemView.findViewById(R.id.increment_button);
            decrementButton = itemView.findViewById(R.id.decrement_button);
            totalPriceTextView = itemView.findViewById(R.id.total_products_price_text_view);
            removeItemButton = itemView.findViewById(R.id.remove_item_button);
        }
    }
}
