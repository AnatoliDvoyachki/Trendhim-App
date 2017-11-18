package com.example.asus.trendhimapp.weeklyLookPage.singleWeeklyLookPage;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.asus.trendhimapp.R;
import com.example.asus.trendhimapp.categoryPage.CategoryProduct;
import com.example.asus.trendhimapp.util.BitmapFlyweight;
import com.example.asus.trendhimapp.util.Constants;
import com.example.asus.trendhimapp.weeklyLookPage.WeeklyLook;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SecondWeeklyLookAdapter extends RecyclerView.Adapter<SecondWeeklyLookAdapter.ViewHolder> {

    private Context context;
    private List<WeeklyLook> looks;
    private SingleWeeklyLookAdapter singleWeeklyLookAdapter;
    private String lookId;

    SecondWeeklyLookAdapter(Context context, List<WeeklyLook> looks, SingleWeeklyLookAdapter singleWeeklyLookAdapter, String lookId) {
        this.looks = looks;
        this.context = context;
        this.singleWeeklyLookAdapter = singleWeeklyLookAdapter;
        this.lookId = lookId;
    }

    @Override
    public SecondWeeklyLookAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View newProductsView = inflater.inflate(R.layout.activity_single_weekly_look, parent, false);

        // Return a new holder instance
        return new ViewHolder(newProductsView);
    }

    @Override
    public void onBindViewHolder(SecondWeeklyLookAdapter.ViewHolder viewHolder, int position) {
        // Get the data model based on position
        final WeeklyLook look = looks.get(position);
        // Set item views based on the views and data model
        TextView productName = viewHolder.lookPhrase;
        productName.setText(look.getPhrase());

        ImageView mainImage = viewHolder.mainLookImage;
        BitmapFlyweight.getPicture(look.getMainPictureUrl(), mainImage);

        ImageView secondImage = viewHolder.secondLookImage;
        BitmapFlyweight.getPicture(look.getSecondPictureUrl(), secondImage);

        ImageView thirdLookImage = viewHolder.thirdLookImage;
        BitmapFlyweight.getPicture(look.getThirdPictureUrl(), thirdLookImage);

        ImageView fourthLookImage = viewHolder.fourthLookImage;
        BitmapFlyweight.getPicture(look.getFourthPictureUrl(), fourthLookImage);

        ImageView fifthLookImage = viewHolder.fifthLookImage;
        BitmapFlyweight.getPicture(look.getFifthPictureUrl(), fifthLookImage);

        RecyclerView recyclerView = viewHolder.recyclerView;
        //Recommended products initialize
        ArrayList<CategoryProduct> wishListProducts = new ArrayList<>();

        // Create a new adapter for the category
        singleWeeklyLookAdapter = new SingleWeeklyLookAdapter(context, wishListProducts);

        // Attach the adapter to the recycler view
        recyclerView.setAdapter(singleWeeklyLookAdapter);

        //Populate the recycler view
        singleWeeklyLookAdapter.addData(lookId);

        GridLayoutManager layoutManagerNew = new GridLayoutManager(context, 1, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManagerNew);
    }

    @Override
    public int getItemCount() {
        return looks.size();
    }

    /**
     * Populate the recycler view. Get data from the database which key is equal to one in the parameter.
     */
    public void addData(final String lookKey) {
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference(Constants.TABLE_NAME_WEEKLY_LOOK);
        myRef.orderByKey().equalTo(lookKey).addListenerForSingleValueEvent(new ValueEventListener() { //get user recent products
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {

                    for (final DataSnapshot product : dataSnapshot.getChildren()) {
                        //Found product
                        final WeeklyLook newLook = product.getValue(WeeklyLook.class);

                        looks.add(new WeeklyLook(lookKey, newLook.getPhrase(), newLook.getMainPictureUrl(),
                                newLook.getSecondPictureUrl(), newLook.getThirdPictureUrl(), newLook.getFourthPictureUrl(),
                                newLook.getFifthPictureUrl()));

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

        ImageView mainLookImage, secondLookImage, thirdLookImage, fourthLookImage, fifthLookImage;
        TextView lookPhrase;
        RecyclerView recyclerView;

        /**
         *  We also create a constructor that does the view lookups to find each subview
         */
        ViewHolder(View itemView) {
        /*
          Stores the itemView in a public final member variable that can be used
          to access the context from any ViewHolder instance.
         */
            super(itemView);

            mainLookImage = itemView.findViewById(R.id.mainLookImage);
            secondLookImage = itemView.findViewById(R.id.secondLookImage);
            thirdLookImage = itemView.findViewById(R.id.thirdLookImage);
            fourthLookImage = itemView.findViewById(R.id.fourthLookImage);
            fifthLookImage = itemView.findViewById(R.id.fifthLookImage);
            lookPhrase = itemView.findViewById(R.id.lookPhrase);
            recyclerView = itemView.findViewById(R.id.rvLookProducts);
        }
    }

}