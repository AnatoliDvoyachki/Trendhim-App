package com.example.asus.trendhimapp.loginPage;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.asus.trendhimapp.R;
import com.example.asus.trendhimapp.mainActivities.BaseActivity;
import com.example.asus.trendhimapp.mainActivities.MainActivity;
import com.example.asus.trendhimapp.util.Constants;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends BaseActivity implements View.OnKeyListener, View.OnClickListener {

    private FirebaseAuth auth;
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
        setTitle(R.string.log_in);

        emailEditText = findViewById(R.id.input_email_login);
        passwordEditText = findViewById(R.id.input_password_login);

        auth = FirebaseAuth.getInstance();

        passwordEditText.setOnKeyListener(this);

        LinearLayout loginLayout = findViewById(R.id.loginLayout);
        loginLayout.setOnClickListener(this);

        // Get the user's email from the Sign up activity - if exists
        Intent intent = getIntent();

        if(intent != null){
            String email = intent.getStringExtra(Constants.KEY_EMAIL);

            if(email != null)
                emailEditText.setText(email);
        }
    }

    /**
     * Method listening for login button clicks.
     * Sign in users with email and password
     * @param view
     */
    public void login(View view){

        EditText[] editTextFields = {emailEditText, passwordEditText};
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        validate(editTextFields);
        if(validate(editTextFields)) {
            auth.signInWithEmailAndPassword(email, password).
                addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()) {
                        FirebaseUser user = auth.getCurrentUser();
                        if(user.isEmailVerified()) {
                            Intent toMain = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(toMain);
                        } else if(!user.isEmailVerified())
                            Toast.makeText(LoginActivity.this, "Please verify your email address", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getApplicationContext(), R.string.incorrect_credentials_message, Toast.LENGTH_LONG).show();
                        Log.d(TAG, "Sign in failed", task.getException());
                    }
                }
                });
        }
    }

    /**
     * Redirect the user to the Sign Up Activity
     * @param view
     */
    public void goToSignUp(View view){
        Intent toSignUp = new Intent(getApplicationContext(), SignupActivity.class);
        startActivity(toSignUp);
    }

    /**
     * Check if all fields are filled in
     *
     * @param fields Array of EditText containing all the text fields in the Log In class
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
     * Performs logIn operation when the user clicks the Enter button on the keyboard.
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

    /**
     * Send forget password email so the user can reset his password
     * @param view
     */
    public void forgotPassword(View view) {
        final EditText editText = new EditText(getApplicationContext());
        editText.setHint("Email");
        editText.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);

        // Create the confirmation dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle("Reset Password");
        builder.setMessage("Enter email to reset your password");

        builder.setView(editText);

        // Yes option
        builder.setPositiveButton("Reset Password", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                auth.sendPasswordResetEmail(editText.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(LoginActivity.this,
                                            "Reset password email has been sent to " + editText.getText().toString(),
                                            Toast.LENGTH_SHORT).show();
                                    Log.d(TAG, "Email sent.");
                                } else {
                                    Toast.makeText(LoginActivity.this,
                                            "UPS! Something went wrong!",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });

        // Cancel option
        builder.setNegativeButton(R.string.negative_option, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        // Show the dialog window to the user
        AlertDialog dialog = builder.create();
        dialog.show();

        // Customize button text
        Button btnPositive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
        btnPositive.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.black));
        Button btnNegative = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
        btnNegative.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.black));

        // Align the views in the center of the dialog window
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) btnPositive.getLayoutParams();
        layoutParams.weight = 10;
        btnPositive.setLayoutParams(layoutParams);
        btnNegative.setLayoutParams(layoutParams);
    }
}
