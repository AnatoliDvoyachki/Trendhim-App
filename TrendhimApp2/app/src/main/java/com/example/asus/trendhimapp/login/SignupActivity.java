package com.example.asus.trendhimapp.login;

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

import com.example.asus.trendhimapp.mainActivities.BaseActivity;
import com.example.asus.trendhimapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignupActivity extends BaseActivity implements View.OnKeyListener, View.OnClickListener{

    private EditText emailEditText, passwordEditText, confirmEditText;
    private FirebaseAuth auth;
    private static final String TAG = SignupActivity.class.getCanonicalName();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View contentView = inflater.inflate(R.layout.activity_signup, null, false);
        BaseActivity.drawer.addView(contentView, 0);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        setTitle("Sign up");

        auth = FirebaseAuth.getInstance();

        emailEditText = findViewById(R.id.input_email_signUp);
        passwordEditText = findViewById(R.id.input_password_signUp);
        confirmEditText = findViewById(R.id.confirmPassword);

        confirmEditText.setOnKeyListener(this);

        LinearLayout signUpLayout = findViewById(R.id.signupLayout);
        signUpLayout.setOnClickListener(this);

    }

    /**
     * Method listening for sign up button clicks.
     * Sign up users with email and password and save its information the the database
     * @param view
     */
    public void signUp(View view) {
        final EditText[] fields = {passwordEditText, confirmEditText, emailEditText};
        validate(fields);

        assert passwordEditText != null;
        if (!(passwordEditText.getText().toString().equals(confirmEditText.getText().toString())))
            confirmEditText.setError("Passwords don't match");
        if (!isEmailValid(emailEditText.getText().toString()))
            emailEditText.setError("Invalid Email");
        else {
            if (passwordEditText.getText().toString().equals(confirmEditText.getText().toString())) {
                auth.createUserWithEmailAndPassword(emailEditText.getText().toString(), passwordEditText.getText().toString())
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d(TAG, "createUserWithEmail:success");

                                    FirebaseUser user = auth.getCurrentUser();
                                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                                    intent.putExtra("email",user.getEmail());
                                    startActivity(intent);
                                    for (EditText editText : fields)
                                        editText.setText(null);
                                } else {
                                    if(!task.isSuccessful()) {
                                        Toast.makeText(SignupActivity.this, "Ups! Authentication failed.", Toast.LENGTH_SHORT).show();
                                        Log.w(TAG, "createUserWithEmail:failure", task.getException());

                                        // If sign in fails, display a message to the user.
                                        try {
                                            throw task.getException();
                                        } catch(FirebaseAuthWeakPasswordException e) {
                                            passwordEditText.setError("Password has to be at least 6 characters");
                                            passwordEditText.requestFocus();
                                        } catch(FirebaseAuthInvalidCredentialsException e) {
                                            emailEditText.setError("Invalid email format");
                                            emailEditText.requestFocus();
                                        } catch(FirebaseAuthUserCollisionException e) {
                                            emailEditText.setError("User already exists");
                                            emailEditText.requestFocus();
                                        } catch(Exception e) {
                                            Log.w("SignInLog", "createUserWithEmail:failure", task.getException());
                                        }
                                    }
                                }
                            }
                        });

            }
        }
    }

    /**
     * Check if all fields are filled in
     *
     * @param fields Array of EditText containing all the text fields in the Sign Up class
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
     * Check if the email format is valid .
     *
     * @param email
     * @return true for valid - false for invalid
     */
    public static boolean isEmailValid (String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    /**
     * Perform Sign Up operation when the user clicks the Enter button on the keyboard.
     *
     * @param v, keycode, event
     * @return false
     */
    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN)
            signUp(v);

        return false;
    }

    /**
     * Hide the keyboard whenever the user clicks somewhere in the layout.
     *
     * @param v View to witch the method is associated to
     */
    @Override
    public void onClick(View v) {

        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

    }
}
