package com.example.asus.trendhimapp.settings.shippingAddress;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.asus.trendhimapp.R;
import com.example.asus.trendhimapp.mainActivities.BaseActivity;
import com.example.asus.trendhimapp.util.Constants;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class IndividualAddress extends BaseActivity {

    TextView nameTextView, addressTextView, zipcodeTextView, cityTextView, countryTextView, emailTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.user_address, null, false);

        BaseActivity.drawer.addView(contentView, 0);
        getSupportActionBar().setDisplayShowTitleEnabled(true);

        setTitle(getString(R.string.shipping_address));

        nameTextView = findViewById(R.id.name_address);
        emailTextView = findViewById(R.id.email_address);
        addressTextView = findViewById(R.id.address);
        zipcodeTextView = findViewById(R.id.zipcode_address_user);
        cityTextView = findViewById(R.id.city_address_user);
        countryTextView = findViewById(R.id.country_user);

        Intent intent = getIntent();

        if(intent != null){
            String name = intent.getStringExtra(Constants.KEY_NAME);
            String email = intent.getStringExtra(Constants.KEY_EMAIL);
            String city = intent.getStringExtra(Constants.KEY_CITY);
            String address = intent.getStringExtra(Constants.KEY_ADDRESS);
            String zipcode = intent.getStringExtra(Constants.KEY_ZIPCODE);
            String country = intent.getStringExtra(Constants.KEY_COUNTRY);

            if(name != null && email != null && city != null
                    && address != null &&  zipcode != null && country != null) {
                nameTextView.setText(name);
                emailTextView.setText(email);
                addressTextView.setText(address);
                zipcodeTextView.setText(zipcode);
                cityTextView.setText(city);
                countryTextView.setText(country);
            }

        }

    }


    public void updateCredentials(View view) {
        DatabaseReference credentialsReference = FirebaseDatabase.getInstance().getReference(Constants.TABLE_NAME_USER_CREDENTIALS);
        credentialsReference.orderByChild(Constants.KEY_USER_EMAIL).equalTo(emailTextView.getText().toString())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()) {
                            for (DataSnapshot credentials : dataSnapshot.getChildren()) {
                                credentials.getRef().child(Constants.KEY_USER_EMAIL).setValue(emailTextView.getText().toString());
                                credentials.getRef().child(Constants.KEY_ADDRESS).setValue(addressTextView.getText().toString());
                                credentials.getRef().child(Constants.KEY_CITY).setValue(cityTextView.getText().toString());
                                credentials.getRef().child(Constants.KEY_NAME).setValue(nameTextView.getText().toString());
                                credentials.getRef().child(Constants.KEY_ZIPCODE).setValue(zipcodeTextView.getText().toString());
                                credentials.getRef().child(Constants.KEY_COUNTRY).setValue(countryTextView.getText().toString());

                                Toast.makeText(IndividualAddress.this, R.string.credentials_updated, Toast.LENGTH_SHORT).show();
                                Intent toUserAddress = new Intent(getApplicationContext(), UserAddress.class);
                                startActivity(toUserAddress);

                            }

                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {}
                });
    }
}
