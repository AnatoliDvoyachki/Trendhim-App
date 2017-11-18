package com.example.asus.trendhimapp.weeklyLookPage;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.asus.trendhimapp.R;
import com.example.asus.trendhimapp.util.BitmapFlyweight;
import com.example.asus.trendhimapp.util.Constants;
import com.example.asus.trendhimapp.weeklyLookPage.singleWeeklyLookPage.SecondWeeklyLookActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;
import java.util.Objects;

public class WeeklyLookAdapter extends RecyclerView.Adapter<WeeklyLookAdapter.ViewHolder> {

    private Context context;
    private List<WeeklyLook> weeklyLooks;

    WeeklyLookAdapter(Context context, List<WeeklyLook> weeklyLooks) {
        this.weeklyLooks = weeklyLooks;
        this.context = context;
    }

    @Override
    public WeeklyLookAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View looksProductsView = inflater.inflate(R.layout.item_weekly_look, parent, false);

        // Return a new holder instance
        return new WeeklyLookAdapter.ViewHolder(looksProductsView);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onBindViewHolder(WeeklyLookAdapter.ViewHolder viewHolder, final int position) {

        // Get the data model based on position
        final WeeklyLook product = weeklyLooks.get(position);

        // Set item views based on the views and data model

        final ImageView productImage = viewHolder.weeklyLookImage;
        BitmapFlyweight.getPicture(product.getMainPictureUrl(), productImage);

        /*
         * Handle touch events - Change the image alpha
         */
        viewHolder.itemView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_UP:
                        productImage.animate().alpha(1f);
                        getLook(product, view); //Redirect the user to the product activity
                        break;
                    case MotionEvent.ACTION_DOWN:
                        productImage.animate().alpha(0.7f);
                        productImage.animate().alpha(1f);
                        break;
                }
                return true;
            }
        });

        /*
         * Handle click events - Redirect to the selected product
         */
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getLook(product, view); //Redirect the user to the right product activity
            }
        });

    }

    @Override
    public int getItemCount() {
        return weeklyLooks.size();
    }

    /**
     * Populate the recycler view.
     */
    void addData() {
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference(Constants.TABLE_NAME_WEEKLY_LOOK);
        myRef.orderByKey().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for(DataSnapshot look : dataSnapshot.getChildren()) {
                        String lookKey = look.getKey();
                        WeeklyLook weeklyLook = look.getValue(WeeklyLook.class);
                        weeklyLooks.add(new WeeklyLook(lookKey,weeklyLook.getPhrase(), weeklyLook.getMainPictureUrl(), weeklyLook.getSecondPictureUrl(),
                                weeklyLook.getThirdPictureUrl(), weeklyLook.getFourthPictureUrl(), weeklyLook.getFifthPictureUrl()));
                        // Notify the adapter that an item was inserted in the last position = getItemCount()
                        notifyItemInserted(getItemCount());

                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }

    /**
     *  Provide a direct reference to each of the views
     * used to cache the views within the layout for fast access
     */
    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView weeklyLookImage;

        /**
         *  We also create a constructor that does the view lookups to find each subview
         */
        ViewHolder(View itemView) {
            /*
              Stores the itemView in a public final member variable that can be used
              to access the context from any ViewHolder instance.
             */
            super(itemView);

            weeklyLookImage = itemView.findViewById(R.id.weeklyLookImage);

        }
    }

    /**
     * Redirect the user to the correct Look
     * @param look
     */
    private void getLook(final WeeklyLook look, final View view) {

        DatabaseReference weeklyLookReference = FirebaseDatabase.getInstance().getReference(Constants.TABLE_NAME_WEEKLY_LOOK);

        weeklyLookReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {

                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                        if(Objects.equals(look.getKey(), dataSnapshot1.getKey())) {

                            Intent toSecondWeeklyLook = new Intent(context, SecondWeeklyLookActivity.class);

                            ActivityOptionsCompat options = ActivityOptionsCompat.
                                    makeSceneTransitionAnimation((Activity) context, view, "profile");

                            toSecondWeeklyLook.putExtra(Constants.KEY_LOOK_KEY, look.getKey());

                            context.startActivity(toSecondWeeklyLook, options.toBundle());
                        }

                    }

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }

}
