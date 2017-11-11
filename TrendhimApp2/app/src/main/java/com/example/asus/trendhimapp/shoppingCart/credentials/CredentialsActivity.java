package com.example.asus.trendhimapp.shoppingCart.credentials;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.example.asus.trendhimapp.R;
import com.example.asus.trendhimapp.mainActivities.BaseActivity;
import com.example.asus.trendhimapp.util.Constants;
import com.example.asus.trendhimapp.util.GMailSender;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CredentialsActivity extends BaseActivity implements View.OnClickListener, View.OnKeyListener {

    EditText name, email, streetAddress, city, zipcode, country;
    String GMail = "trendhimaps@gmail.com"; //replace with your GMail
    String GMailPass = "android11"; // replace with your GMail Password
    String str_subject, str_to, str_message;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_credentials, null, false);
        BaseActivity.drawer.addView(contentView, 0);

        getSupportActionBar().setDisplayShowTitleEnabled(true);

        setTitle(R.string.credentials_title);

        name = findViewById(R.id.name_credentials);
        email = findViewById(R.id.email_credentials);
        streetAddress = findViewById(R.id.address_credentials);
        zipcode = findViewById(R.id.zipcode_credentials);
        country = findViewById(R.id.country_credentials);
        city = findViewById(R.id.city_credentials);

        str_to = email.getText().toString();
        str_subject = "Order Confirmation - Trendhim thinks you are amazing";
        str_message = "To be implemented";

        getUserCredentials();
    }

    private void getUserCredentials(){
        DatabaseReference userCredentialsDatabase =
                FirebaseDatabase.getInstance().getReference(Constants.TABLE_NAME_USER_CREDENTIALS);
        final FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if(currentUser != null) {
            userCredentialsDatabase.orderByChild("email").equalTo(currentUser.getEmail())
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()){
                        for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                            Credentials credentials = dataSnapshot1.getValue(Credentials.class);

                            name.setText(credentials.getName());
                            streetAddress.setText(credentials.getAddress());
                            city.setText(credentials.getCity());
                            country.setText(credentials.getCountry());
                            email.setText(currentUser.getEmail());
                            zipcode.setText(credentials.getZipcode());

                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {}
            });
        }
    }

    public void completeOrder(View view) {
        EditText[] fields = {name, email, streetAddress, country, zipcode, city};

        if(validate(fields)) {
            //Check if 'To:' field is a valid email
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {

                    Toast.makeText(getApplicationContext(), "Sending... Please wait", Toast.LENGTH_LONG).show();
                }
            });
            sendEmail(str_to, str_subject, str_message);
        }

    }
    private void sendEmail(final String to, final String subject, final String message) {
        final EditText[] fields = {name, email, streetAddress, country, zipcode, city};

        new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    GMailSender sender = new GMailSender(GMail,
                            GMailPass);
                    sender.sendMail(subject,
                            message,
                            GMail,
                            to);
                    Log.w("sendEmail","Email successfully sent!");

                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "Email successfully sent!", Toast.LENGTH_LONG).show();
                            for(EditText editText : fields)
                                editText.setText("");
                        }
                    });

                } catch (final Exception e) {
                    Log.e("sendEmail", e.getMessage(), e);

                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "Email not sent. \n\n Error: " + e.getMessage().toString(), Toast.LENGTH_LONG).show();
                        }
                    });


                }
            }

        }).start();
    }

    /**
     * Check if all fields are filled in
     *
     * @param fields Array of EditText containing all the text fields in the Credentials Activity
     * @return true if the fields are correctly filled in
     */
    private boolean validate(EditText[] fields) {
        boolean complete = true;
        for (EditText currentField : fields) {
            if (currentField.getText().toString().matches("")) {
                currentField.setError("This field cannot be empty");
                complete = false;
            }
        }
        return complete;
    }


    /**
     * Performs complete order operation when the user clicks the Enter button on the keyboard.
     *
     * @param v, keycode, event
     * @return false
     */
    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN)
            completeOrder(v);

        return false;
    }

    /**
     * Hides the keyboard whenever the user clicks somewhere in the layout.
     *
     * @param v View to witch the method is associated to
     */
    @Override
    public void onClick(View v) {

        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

    }


}
