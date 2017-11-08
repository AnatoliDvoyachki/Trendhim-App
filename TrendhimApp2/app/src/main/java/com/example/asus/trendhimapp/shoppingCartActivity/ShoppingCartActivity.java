package com.example.asus.trendhimapp.shoppingCartActivity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.asus.trendhimapp.R;
import com.example.asus.trendhimapp.mainActivities.BaseActivity;

public class ShoppingCartActivity extends BaseActivity {

    private RecyclerView recyclerView;
    private TextView productNameTextView, quantityTextView, priceTextView,
            shippingLabelTextView, subtotalLabelTextView;
    private Button checkoutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_shopping_cart, null, false);
        BaseActivity.drawer.addView(contentView, 0);
        initializeComponents();

    }

    private void initializeComponents() {
        recyclerView = findViewById(R.id.the_recycler_view);
        productNameTextView = findViewById(R.id.product_name_text_view);
        quantityTextView = findViewById(R.id.quantity_text_view);
        priceTextView = findViewById(R.id.price_text_view);
        shippingLabelTextView = findViewById(R.id.shipping_value_text_view);
        subtotalLabelTextView = findViewById(R.id.subtotal_value_text_view);
        checkoutButton = findViewById(R.id.checkout_button);
    }
}
