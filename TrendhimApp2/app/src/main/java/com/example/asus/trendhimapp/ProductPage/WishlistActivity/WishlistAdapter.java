package com.example.asus.trendhimapp.ProductPage.WishlistActivity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.asus.trendhimapp.ProductPage.Product;
import com.example.asus.trendhimapp.R;
import com.example.asus.trendhimapp.Util.BitmapFlyweight;

import java.util.List;

public class WishlistAdapter extends ArrayAdapter<Product> {
    private String email;

    public WishlistAdapter(Context context, List<Product> productList, String userEmail) {
        super(context, 0, productList);
        this.email = userEmail;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_wishlist, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.bannerImageView = convertView.findViewById(R.id.banner_image_view);
            viewHolder.productNameTextView = convertView.findViewById(R.id.product_name_text_view);
            viewHolder.priceTextView = convertView.findViewById(R.id.price_text_view);
            viewHolder.emailTextView = convertView.findViewById(R.id.email_text_field);
            viewHolder.removeImageButton = convertView.findViewById(R.id.remove_button);
            viewHolder.addToCartImageButton = convertView.findViewById(R.id.add_to_cart_button);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Product currentProduct = getItem(position);
        if (currentProduct != null) {
            BitmapFlyweight.getPicture(currentProduct.getBannerPictureUrl(), viewHolder.bannerImageView);
            viewHolder.productNameTextView.setText(currentProduct.getProductName());
            viewHolder.priceTextView.setText(Integer.toString(currentProduct.getPrice()));
            viewHolder.emailTextView.setText(email);
            viewHolder.removeImageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(WishlistAdapter.super.getContext(), "The best is yet to come", Toast.LENGTH_SHORT).show();
                }
            });
            viewHolder.addToCartImageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(WishlistAdapter.super.getContext(), "Yep", Toast.LENGTH_SHORT).show();
                }
            });
        }
        return convertView;
    }

    /**
     * View holder pattern implementation
     **/
    static class ViewHolder {
        ImageView bannerImageView;
        TextView productNameTextView;
        TextView priceTextView;
        TextView emailTextView;
        ImageButton removeImageButton;
        ImageButton addToCartImageButton;
    }
}
