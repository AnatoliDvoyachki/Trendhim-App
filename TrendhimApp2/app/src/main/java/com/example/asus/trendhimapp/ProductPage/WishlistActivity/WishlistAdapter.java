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

import com.example.asus.trendhimapp.ProductPage.Product;
import com.example.asus.trendhimapp.R;
import com.example.asus.trendhimapp.Util.BitmapFlyweight;

import java.util.List;

public class WishlistAdapter extends RecyclerView.Adapter<WishlistAdapter.ViewHolder> {
    private String userEmail;
    private Context context;
    private List<Product> productList;

    public WishlistAdapter(Context context, List<Product> productList, String userEmail) {
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
        Product currentProduct = productList.get(position);
        BitmapFlyweight.getPicture(currentProduct.getBannerPictureUrl(), viewHolder.bannerImageView);
        viewHolder.productNameTextView.setText(currentProduct.getProductName());
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
