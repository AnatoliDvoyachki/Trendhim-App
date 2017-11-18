package com.example.asus.trendhimapp.shoppingCart.credentialsPage;

import android.annotation.SuppressLint;
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
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.asus.trendhimapp.R;
import com.example.asus.trendhimapp.mainActivities.BaseActivity;
import com.example.asus.trendhimapp.shoppingCart.Order;
import com.example.asus.trendhimapp.shoppingCart.ShoppingCartActivity;
import com.example.asus.trendhimapp.shoppingCart.ShoppingCartProduct;
import com.example.asus.trendhimapp.util.Constants;
import com.example.asus.trendhimapp.util.GMailSender;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class CredentialsActivity extends BaseActivity implements View.OnClickListener, View.OnKeyListener {

    EditText name, email, streetAddress, city, zipcode, country;
    String GMail = "trendhimaps@gmail.com"; // GMail address
    String GMailPass = "android11"; // GMail Password
    String str_subject, str_to, str_message;
    ArrayList<Order> orders;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_credentials, null, false);
        BaseActivity.drawer.addView(contentView, 0);

        getSupportActionBar().setDisplayShowTitleEnabled(true);

        setTitle(R.string.credentials_title);

        orders = new ArrayList<>();

        initializeComponents();
    }

    /**
     * Initialize credentials components
     */
    public void initializeComponents(){
        name = findViewById(R.id.name_credentials);
        email = findViewById(R.id.email_credentials);
        streetAddress = findViewById(R.id.address_credentials);
        zipcode = findViewById(R.id.zipcode_credentials);
        country = findViewById(R.id.country_credentials);
        city = findViewById(R.id.city_credentials);
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if(currentUser != null && currentUser.getEmail() != null)
            email.setText(currentUser.getEmail());

        getUserCredentials();

        // Set the OnClickListener to the linear layout
        LinearLayout linearLayout = findViewById(R.id.credentialsLayout);
        linearLayout.setOnClickListener(this);

        // Set the OnKeyListener to the last edit text view in the layout
        country.setOnKeyListener(this);

        str_to = email.getText().toString();
        str_subject = "Order Confirmation - Trendhim thinks you are amazing";
        str_message = "Your order is on its way!";
    }

    /**
     * Get the user credentials from firebase if the customer is a returning customer
     */
    private void getUserCredentials(){
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

                            name.setText(credentials.getName());
                            streetAddress.setText(credentials.getAddress());
                            city.setText(credentials.getCity());
                            country.setText(credentials.getCountry());
                            email.setText(credentials.getUserEmail());
                            zipcode.setText(credentials.getZipcode());

                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {}
            });
    }

    /**
     * Complete order if all the fields are filled in correctly
     * If so, send confirmation email to the user
     * @param view
     */
    public void completeOrder(View view) {
        EditText[] fields = {name, email, streetAddress, country, zipcode, city};

        if(validate(fields) && isValidEmail(email.getText().toString())) {
            //Check if 'To:' field is a valid email
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(), "Sending... Please wait", Toast.LENGTH_LONG).show();
                }
            });
            sendEmail(str_to, str_subject, str_message);
            saveUserCredentials();
            saveOrderInformation();
            removeItem();
        }

    }

    /**
     * Send email based on the method parameter
     * @param to
     * @param subject
     * @param message
     */
    private void sendEmail(final String to, final String subject, final String message) {

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

                        }
                    });



                } catch (final Exception e) {
                    Log.e("sendEmail", e.getMessage(), e);

                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "Email not sent. \n\n Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });


                }
            }

        }).start();
    }

    /**
     * Convert Date to String
     * @param date
     * @return Date formatted into a string
     */
    private static String convertDateToString(Date date) {
        @SuppressLint("SimpleDateFormat") DateFormat dateFormatter = new SimpleDateFormat("dd MMM yyyy");
        return dateFormatter.format(date);
    }


    /**
     * Save the user credentials in the credentials database when an order is successfully made
     */
    private void saveUserCredentials(){

        final DatabaseReference credentialsDatabase = FirebaseDatabase.getInstance().getReference(Constants.TABLE_NAME_USER_CREDENTIALS);
        credentialsDatabase.orderByChild(Constants.KEY_USER_EMAIL).equalTo(email.getText().toString())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(!dataSnapshot.exists()) {
                            Map<String, String> values = new HashMap<>();
                            values.put(Constants.KEY_USER_EMAIL, email.getText().toString());
                            values.put(Constants.KEY_ADDRESS, streetAddress.getText().toString());
                            values.put(Constants.KEY_ZIPCODE, zipcode.getText().toString());
                            values.put(Constants.KEY_CITY, city.getText().toString());
                            values.put(Constants.KEY_NAME, name.getText().toString());
                            values.put(Constants.KEY_COUNTRY, country.getText().toString());
                            credentialsDatabase.push().setValue(values);

                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

    }

    /**
     * Save the order information in the order database when an order is successfully made
     */
    private void saveOrderInformation() {

        //Query the shopping cart database to get the product category
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(Constants.TABLE_NAME_SHOPPING_CART);
        //get user email to save the order in the order entity
        databaseReference.orderByChild(Constants.KEY_USER_EMAIL).equalTo(email.getText().toString())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {

                            DatabaseReference orderDatabase
                                    = FirebaseDatabase.getInstance().getReference(Constants.TABLE_NAME_ORDERS);

                            final Map<String, Object> values = new HashMap<>();

                            values.put(Constants.KEY_USER_EMAIL, email.getText().toString());
                            values.put(Constants.ORDER_DATE,
                                    convertDateToString(new Date())); //save current order date
                            values.put(Constants.GRAND_TOTAL,
                                    String.valueOf(ShoppingCartActivity.getGrandTotalCost()));

                            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                                ShoppingCartProduct shoppingCartProduct
                                        = dataSnapshot1.getValue(ShoppingCartProduct.class);
                                Order order = new Order(shoppingCartProduct.getProductKey(),
                                        shoppingCartProduct.getQuantity());

                                orders.add(order);
                            }

                            //save shopping cart products as an array in firebase
                            final Map<String, Object> values1 = new HashMap<>();
                            for (int i = 0; i < orders.size(); i++)
                                values1.put(String.valueOf(i),orders.get(i));
                            values.put(Constants.KEY_PRODUCTS, values1);

                            orderDatabase.push().setValue(values);
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

    }

    /**
     * Removes a product from the user's wishlist datbase - firebase
     **/
    private void removeItem() {

        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference(Constants.TABLE_NAME_SHOPPING_CART);

        dbRef.orderByChild(Constants.KEY_USER_EMAIL).equalTo(email.getText().toString())
                .addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                    for (DataSnapshot ds : dataSnapshot.getChildren()) {

                        ds.getRef().removeValue(); // If found, remove the item from the shopping cart
                        ShoppingCartActivity.adapter.notifyDataSetChanged(); // Update the UI

                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
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
     *  Check if parameter 'emailAddress' is a valid email
     */
    boolean isValidEmail(CharSequence emailAddress) {
        return emailAddress != null && android.util.Patterns.EMAIL_ADDRESS.matcher(emailAddress).matches();
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
