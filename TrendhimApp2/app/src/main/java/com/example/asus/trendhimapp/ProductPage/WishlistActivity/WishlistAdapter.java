package com.example.asus.trendhimapp.ProductPage.WishlistActivity;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.asus.trendhimapp.ProductPage.Product;
import com.example.asus.trendhimapp.R;
import com.example.asus.trendhimapp.Util.BitmapFlyweight;

import java.util.List;

public class WishlistAdapter extends ArrayAdapter<Product> {


    public WishlistAdapter(@NonNull Context context, @NonNull List<Product> objects) {
        super(context, 0, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_wishlist, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.bannerImageView = convertView.findViewById(R.id.banner_image_view);
            viewHolder.productNameTextView = convertView.findViewById(R.id.product_name_text_view);
            viewHolder.priceTextView = convertView.findViewById(R.id.price_text_view);
            viewHolder.emailLabelTextView = convertView.findViewById(R.id.email_label_text_view);
            viewHolder.emailTextView = convertView.findViewById(R.id.email_text_field);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Product currentProduct = getItem(position);
        if (currentProduct != null) {
            BitmapFlyweight.getPicture(currentProduct.getBannerPictureUrl(), viewHolder.bannerImageView);
            viewHolder.productNameTextView.setText(currentProduct.getProductName());
            viewHolder.priceTextView.setText(Integer.toString(currentProduct.getPrice()));
            // Unfinished
        }


        return super.getView(position, convertView, parent);
    }


    static class ViewHolder {
        ImageView bannerImageView;
        TextView productNameTextView;
        TextView priceTextView;
        TextView emailLabelTextView;
        TextView emailTextView;
    }
}
