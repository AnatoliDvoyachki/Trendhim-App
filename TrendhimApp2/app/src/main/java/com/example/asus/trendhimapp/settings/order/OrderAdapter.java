package com.example.asus.trendhimapp.settings.order;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.asus.trendhimapp.R;
import com.example.asus.trendhimapp.settings.order.detailedOrder.DetailedOrderActivity;
import com.example.asus.trendhimapp.shoppingCartPage.credentialsPage.Credentials;
import com.example.asus.trendhimapp.util.Constants;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder> {

    private List<UserOrder> orders;
    private Context context;

    OrderAdapter(Context context, List<UserOrder> orders) {
        this.orders = orders;
        this.context = context;
    }

    @Override
    public OrderAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View newProductsView = inflater.inflate(R.layout.item_orders, parent, false);

        // Return a new holder instance
        return new OrderAdapter.ViewHolder(newProductsView);
    }

    @Override
    public void onBindViewHolder(OrderAdapter.ViewHolder viewHolder, int position) {

        // Get the data model based on position
        final UserOrder order = orders.get(position);

        // Set item views based on the views and data model
        TextView date = viewHolder.date;
        date.setText(order.getDate());

        TextView address = viewHolder.address;
        address.setText(order.getAddress());

        TextView productPrice = viewHolder.price;
        productPrice.setText(String.format("%s €", order.getGrand_total()));

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toOrder = new Intent(context, DetailedOrderActivity.class);
                toOrder.putExtra("key", order.getKey());
                context.startActivity(toOrder);
            }
        });
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    /**
     * Populate the recycler view. Get data from the order database
     */
    public void addData() {
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference(Constants.TABLE_NAME_ORDERS);
        final FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if(currentUser != null) {
            myRef.orderByChild("userEmail").equalTo(currentUser.getEmail())
                    .addListenerForSingleValueEvent(new ValueEventListener() { //get user recent products
                        @Override
                        public void onDataChange(final DataSnapshot dataSnapshot) {

                            if (dataSnapshot.exists()) {

                                for (final DataSnapshot product : dataSnapshot.getChildren()) {
                                    //Found product
                                    final UserOrder order = product.getValue(UserOrder.class);
                                    DatabaseReference myRef =
                                            FirebaseDatabase.getInstance().getReference(Constants.TABLE_NAME_USER_CREDENTIALS);
                                    myRef.addListenerForSingleValueEvent(new ValueEventListener() { //get user address
                                        @Override
                                        public void onDataChange(final DataSnapshot dataSnapshot) {

                                            if (dataSnapshot.exists()) {

                                                for (final DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                                    Credentials credentials =  dataSnapshot1.getValue(Credentials.class);
                                                    orders.add(0, new UserOrder(order.getDate(), credentials.getAddress(),
                                                            order.getGrand_total(), product.getKey()));
                                                    notifyItemInserted(0);

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

        } else {
            Toast.makeText(context, R.string.need_to_log_in_first_message, Toast.LENGTH_LONG).show();
        }

    }
    /**
     *  Provide a direct reference to each of the views
     * used to cache the views within the layout for fast access
     */
    class ViewHolder extends RecyclerView.ViewHolder {

        TextView date, address, price;

        /**
         *  We also create a constructor that does the view lookups to find each subview
         */
        ViewHolder(View itemView) {
        /*
          Stores the itemView in a public final member variable that can be used
          to access the context from any ViewHolder instance.
         */
            super(itemView);

            date = itemView.findViewById(R.id.date_order);
            address = itemView.findViewById(R.id.address_order);
            price = itemView.findViewById(R.id.price_order);
        }
    }

}