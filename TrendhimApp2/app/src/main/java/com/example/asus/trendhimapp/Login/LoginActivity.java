package com.example.asus.trendhimapp.Login;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.example.asus.trendhimapp.BaseActivity;
import com.example.asus.trendhimapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity extends BaseActivity {

    private FirebaseUser user;
    private FirebaseAuth auth;
    private DatabaseReference userDatabase;
    private EditText emailEditText, passwordEditText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_login, null, false);
        BaseActivity.drawer.addView(contentView, 0);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        setTitle("Log in");


        emailEditText = findViewById(R.id.input_email);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        userDatabase = FirebaseDatabase.getInstance().getReference("user");



    }


    public void login(View view){

        Query query =
    }

    /**
     * Check if all fields are filled in
     *
     * @param fields Array of EditText containing all the text fields in the MainActivity class
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

}
