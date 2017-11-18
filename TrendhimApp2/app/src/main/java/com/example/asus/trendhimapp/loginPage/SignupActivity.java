package com.example.asus.trendhimapp.loginPage;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.asus.trendhimapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;

public class SignupActivity extends Fragment implements View.OnKeyListener, View.OnClickListener{

    private EditText emailEditText, passwordEditText, confirmEditText;
    private FirebaseAuth auth;

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.activity_signup, container, false);

        auth = FirebaseAuth.getInstance();

        emailEditText = rootView.findViewById(R.id.input_email_signUp);
        passwordEditText = rootView.findViewById(R.id.input_password_signUp);
        confirmEditText = rootView.findViewById(R.id.confirmPassword);

        confirmEditText.setOnKeyListener(this);

        LinearLayout signUpLayout = rootView.findViewById(R.id.signupLayout);
        signUpLayout.setOnClickListener(this);

        Button btn_signUp = rootView.findViewById(R.id.btn_signUp);
        btn_signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signUp();
            }
        });

        TextView link_log_in = rootView.findViewById(R.id.link_log_in);
        link_log_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TabbedActivity.mViewPager.setCurrentItem(0, true);
            }
        });
        return rootView;
    }

    /**
     * Method listening for sign up button clicks.
     * Sign up users with email and password and save its information the the database
     */
    public void signUp() {
        final EditText[] fields = {passwordEditText, confirmEditText, emailEditText};
        validate(fields);

        assert passwordEditText != null;
        if (!(passwordEditText.getText().toString().equals(confirmEditText.getText().toString())))
            confirmEditText.setError(getString(R.string.password_mismatch_message));
        if (!isValidEmail(emailEditText.getText().toString()))
            emailEditText.setError(getString(R.string.email_invalid_message));
        else {
            if (passwordEditText.getText().toString().equals(confirmEditText.getText().toString())) {
                auth.createUserWithEmailAndPassword(emailEditText.getText().toString(), passwordEditText.getText().toString())
                        .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    final FirebaseUser user = auth.getCurrentUser();
                                    user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(getActivity(),
                                                        "Verification email sent to " + user.getEmail(),
                                                        Toast.LENGTH_SHORT).show();
                                                TabbedActivity.mViewPager.setCurrentItem(0, true);
                                                LoginActivity.emailEditText.setText(emailEditText.getText().toString());
                                                for (EditText editText : fields)
                                                    editText.setText(null);

                                            } else {
                                                Toast.makeText(getActivity(),
                                                        R.string.email_send_fail,
                                                        Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });



                                } else {
                                    if(!task.isSuccessful()) {
                                        Toast.makeText(getActivity(), R.string.authentication_fail_message, Toast.LENGTH_SHORT).show();
                                        // If sign in fails, display a message to the user.
                                        try {
                                            throw task.getException();
                                        } catch(FirebaseAuthWeakPasswordException e) {
                                            passwordEditText.setError(getString(R.string.short_password_message));
                                            passwordEditText.requestFocus();
                                        } catch(FirebaseAuthInvalidCredentialsException e) {
                                            emailEditText.setError(getString(R.string.email_format_error_message));
                                            emailEditText.requestFocus();
                                        } catch(FirebaseAuthUserCollisionException e) {
                                            emailEditText.setError(getString(R.string.existing_user_message));
                                            emailEditText.requestFocus();
                                        } catch(Exception e) {
                                            e.printStackTrace();
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
                currentField.setError(getString(R.string.must_be_filled_message));
                complete = false;
            }
        }
        return complete;
    }


    /**
     * Check if the email format is valid
     *
     * @param emailAddress
     * @return true for valid - false for invalid
     */
    // Check if parameter 'emailAddress' is a valid email
    boolean isValidEmail(CharSequence emailAddress) {
        return emailAddress != null && android.util.Patterns.EMAIL_ADDRESS.matcher(emailAddress).matches();
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
            signUp();

        return false;
    }

    /**
     * Hide the keyboard whenever the user clicks somewhere in the layout.
     *
     * @param v View to witch the method is associated to
     */
    @Override
    public void onClick(View v) {

        InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);

    }
}
