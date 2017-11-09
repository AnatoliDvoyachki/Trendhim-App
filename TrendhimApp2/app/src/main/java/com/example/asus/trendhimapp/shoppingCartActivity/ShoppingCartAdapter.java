package com.example.asus.trendhimapp.shoppingCartActivity;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import com.example.asus.trendhimapp.util.Constants;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ShoppingCartAdapter extends RecyclerView.Adapter<ShoppingCartAdapter.ViewHolder> {
    private List<ShoppingCartProduct> shoppingCartProducts;
    private List<Product> realProducts;
    private Context context;

    public ShoppingCartAdapter(Context context) {
        this.shoppingCartProducts = new ArrayList<>();
        this.realProducts = new ArrayList<>();
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
//        Toast.makeText(context, "YES", Toast.LENGTH_SHORT).show();
//        holder.productNameTextView.setText(currentProduct.getEntityName());
//        BitmapFlyweight.getPicture(currentProduct.getEntityName(), holder.bannerImageView);
//        holder.removeItemButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(context, "UNIMPLEMENTED", Toast.LENGTH_SHORT).show();
//            }
//        });
//        holder.incrementButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                int currentQuantity = Integer.parseInt(holder.quantityTextView.getText().toString());
//                if (currentQuantity + 1 < Integer.MAX_VALUE) {
//                    holder.quantityTextView.setText(++currentQuantity);
//                }
//            }
//        });
//        holder.decrementButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                int currentQuantity = Integer.parseInt(holder.quantityTextView.getText().toString());
//                if (currentQuantity - 1 >= 0) {
//                    holder.quantityTextView.setText(--currentQuantity);
//                }
//            }
//        });
    }

    public void populateRecyclerView() {
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference(Constants.TABLE_NAME_SHOPPING_CART);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final String email = user.getEmail();
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        final ShoppingCartProduct scp = ds.getValue(ShoppingCartProduct.class);
                         if (email.equals(scp.getUserEmail())) {
                            DatabaseReference myRef1 = FirebaseDatabase.getInstance().getReference(scp.getEntityName());
                            myRef1.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.exists()) {
                                        for (DataSnapshot ds1 : dataSnapshot.getChildren()) {
                                            Product product = ds1.getValue(Product.class);
                                            if (scp.getProductKey().equals(ds1.getKey())) {
                                                Log.d("PRODUCT:", product.toString());
                                                realProducts.add(product);
                                                notifyDataSetChanged();
                                                Toast.makeText(context, "YES", Toast.LENGTH_SHORT).show();
                                            }

                                        }
                                    }

                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {}
                            });

                        }
                        notifyDataSetChanged();
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
