package com.example.asus.trendhimapp.settings.shippingAddress;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.asus.trendhimapp.R;
import com.example.asus.trendhimapp.mainActivities.BaseActivity;
import com.example.asus.trendhimapp.shoppingCart.credentialsPage.Credentials;
import com.example.asus.trendhimapp.util.Constants;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class UserAddress extends BaseActivity {

    ListView listView;
    ArrayList<Credentials> addresses;
    AddressAdapter addressAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.shipping_addresses_list_view, null, false);

        BaseActivity.drawer.addView(contentView, 0);
        getSupportActionBar().setDisplayShowTitleEnabled(true);

        setTitle(getString(R.string.shipping_address));

        listView = findViewById(R.id.shipping_addresses_listView);
        addresses = new ArrayList<>();
        addressAdapter = new AddressAdapter(this, addresses);

        addressAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        listView.setAdapter(addressAdapter);

        //Get user shipping addresses from firebase
        getUserCredentials();

        //Handle list view on click events
        goToShippingAddress();
    }

    /**
     * Get the user credentials from firebase if the customer is a returning customer
     */
    private void getUserCredentials(){
        DatabaseReference userCredentialsDatabase =
                FirebaseDatabase.getInstance().getReference(Constants.TABLE_NAME_USER_CREDENTIALS);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null) {
            userCredentialsDatabase.orderByChild(Constants.KEY_USER_EMAIL).equalTo(user.getEmail())
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if(dataSnapshot.exists()){
                                for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                                    Credentials credentials = dataSnapshot1.getValue(Credentials.class);

                                    addresses.add(credentials);
                                    addressAdapter.notifyDataSetChanged();

                                }
                            } else {
                                Toast.makeText(getApplicationContext(), "No address", Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {}
                    });
        }

    }

    private void goToShippingAddress(){
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                DatabaseReference userCredentialsDatabase =
                        FirebaseDatabase.getInstance().getReference(Constants.TABLE_NAME_USER_CREDENTIALS);

                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                userCredentialsDatabase.orderByChild(Constants.KEY_USER_EMAIL).equalTo(user.getEmail())
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if(dataSnapshot.exists()){
                                    for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                                        Credentials credentials = dataSnapshot1.getValue(Credentials.class);

                                        Intent intent = new Intent(getApplicationContext(), IndividualAddress.class);
                                        intent.putExtra("name",credentials.getName());
                                        intent.putExtra("address",credentials.getAddress());
                                        intent.putExtra("city",credentials.getCity());
                                        intent.putExtra("country",credentials.getCountry());
                                        intent.putExtra("email",credentials.getUserEmail());
                                        intent.putExtra("zipcode",credentials.getZipcode());
                                        startActivity(intent);

                                    }
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {}
                        });
            }
        });
    }

}
