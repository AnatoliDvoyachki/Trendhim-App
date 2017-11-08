package com.example.asus.trendhimapp.shoppingCartActivity;

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
import com.example.asus.trendhimapp.productPage.Product;
import com.example.asus.trendhimapp.util.BitmapFlyweight;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ShoppingCartAdapter extends RecyclerView.Adapter<ShoppingCartAdapter.ViewHolder> {
    private List<ShoppingCartProduct> shoppingCartProducts;
    private Context context;

    public ShoppingCartAdapter(Context context) {
        this.shoppingCartProducts = new ArrayList<>();
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View shoppingCartProds = inflater.inflate(R.layout.item_shopping_cart, parent, false);
        return new ViewHolder(shoppingCartProds);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final ShoppingCartProduct currentProduct = shoppingCartProducts.get(position);
        holder.productNameTextView.setText(currentProduct.getEntityName());
        BitmapFlyweight.getPicture(currentProduct.getEntityName(), holder.bannerImageView);
        holder.removeItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        holder.incrementButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int current = Integer.parseInt(holder.quantityTextView.getText().toString());
                ++current;
                holder.quantityTextView.setText(Integer.toString(current));
            }
        });
        holder.decrementButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int current = Integer.parseInt(holder.quantityTextView.getText().toString());
                --current;
                if (current < 0) {
                    holder.quantityTextView.setText(Integer.toString(current));
                } else {
                    Toast.makeText(context, "The quantity must be a positive number", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return shoppingCartProducts.size();
    }

    private Object[] getBannerUrlAndName(String entityName, final String productKey) {
        final Object[] input = new Object[3];
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference(entityName);
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        if (productKey.equals(ds.getKey())) {
                            Product current = ds.getValue(Product.class);
                            input[0] = current.getBannerPictureUrl();
                            input[1] = current.getProductName();
                            input[2] = current.getPrice();
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
        return input;
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
