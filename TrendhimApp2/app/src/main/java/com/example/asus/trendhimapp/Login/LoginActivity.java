package com.example.asus.trendhimapp.Login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.asus.trendhimapp.BaseActivity;
import com.example.asus.trendhimapp.MainActivity;
import com.example.asus.trendhimapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity extends BaseActivity implements View.OnKeyListener, View.OnClickListener {

    private FirebaseUser user;
    private FirebaseAuth auth;
    private DatabaseReference userDatabase;
    private EditText emailEditText, passwordEditText;
    private static final String TAG = LoginActivity.class.getCanonicalName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_login, null, false);
        BaseActivity.drawer.addView(contentView, 0);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        setTitle("Log in");


        emailEditText = findViewById(R.id.input_email_login);
        passwordEditText = findViewById(R.id.input_password_login);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        userDatabase = FirebaseDatabase.getInstance().getReference("user");

        passwordEditText.setOnKeyListener(this);
        LinearLayout loginLayout = findViewById(R.id.loginLayout);
        loginLayout.setOnClickListener(this);

    }


    public void login(View view){

        EditText[] editTextFields = {emailEditText, passwordEditText};
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        if(validate(editTextFields)) {
            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                    }else{
                        Toast.makeText(getApplicationContext(), "Authentication failed", Toast.LENGTH_LONG);
                        Log.d(TAG, "sign in failed", task.getException());
                    }
                }
            });
        }



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


    /**
     * Performs logIn operation when the user clicks the Enter button on the psw textView.
     *
     * @param v, keycode, event
     * @return false
     */
    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN)
            login(v);

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
