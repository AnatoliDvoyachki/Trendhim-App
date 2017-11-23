package com.example.asus.trendhimapp.loginPage;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.InputType;
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
import com.example.asus.trendhimapp.mainActivities.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends Fragment implements View.OnKeyListener, View.OnClickListener {

    private FirebaseAuth auth;
    public static EditText emailEditText, passwordEditText;

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.activity_login, container, false);

        emailEditText = rootView.findViewById(R.id.input_email_login);
        passwordEditText = rootView.findViewById(R.id.input_password_login);

        auth = FirebaseAuth.getInstance();

        passwordEditText.setOnKeyListener(this);

        LinearLayout loginLayout = rootView.findViewById(R.id.loginLayout);
        loginLayout.setOnClickListener(this);

        Button logInButton = rootView.findViewById(R.id.btn_login);
        logInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login(view);
            }
        });

        TextView link_forgot_password = rootView.findViewById(R.id.link_forgot_password);
        link_forgot_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                forgotPassword();
            }
        });

        TextView link_sign_up = rootView.findViewById(R.id.link_sign_up);
        link_sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TabbedActivity.mViewPager.setCurrentItem(1, true);

            }
        });
        return rootView;
    }

    /**
     * Method listening for login button clicks.
     *
     * Sign in users with email and password
     */
    public void login(final View view) {
        //Hide the keyboard
        InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);

        EditText[] editTextFields = {emailEditText, passwordEditText};
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        validate(editTextFields);
        if(validate(editTextFields)) {
            auth.signInWithEmailAndPassword(email, password).
                addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()) {
                        FirebaseUser user = auth.getCurrentUser();
                        if(user.isEmailVerified()) {
                            Intent toMain = new Intent(getActivity(), MainActivity.class);
                            ActivityOptionsCompat options = ActivityOptionsCompat.
                                    makeSceneTransitionAnimation(getActivity(), view, "profile");
                            startActivity(toMain, options.toBundle());
                        } else if(!user.isEmailVerified())
                            Toast.makeText(getActivity(), R.string.email_verification_message, Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getActivity(), R.string.incorrect_credentials_message, Toast.LENGTH_LONG).show();
                    }
                }
                });
        }
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
                currentField.setError(getString(R.string.must_fill_field_messave));
                complete = false;
            }
        }
        return complete;
    }

    /**
     * Perform logIn operation when the user clicks the Enter button on the keyboard.
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
     * Hide the keyboard whenever the user clicks somewhere in the layout.
     *
     * @param v View to witch the method is associated to
     */
    @Override
    public void onClick(View v) {

        InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);

    }

    /**
     * Send forget password email so the user can reset his password
     */
    public void forgotPassword() {
        final EditText editText = new EditText(getActivity());
        editText.setHint(R.string.email_hint);
        editText.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);

        // Create the confirmation dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setCancelable(true);
        builder.setTitle(R.string.reset_password_title);
        builder.setMessage(R.string.email_to_reset_password_message);

        builder.setView(editText);

        // Yes option
        builder.setPositiveButton(R.string.reset_password_title, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                auth.sendPasswordResetEmail(editText.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(getActivity(),
                                            "Reset password email has been sent to " + editText.getText().toString(),
                                            Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getActivity(),
                                            R.string.something_went_wrong,
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
        btnPositive.setTextColor(ContextCompat.getColor(getActivity(), R.color.black));
        Button btnNegative = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
        btnNegative.setTextColor(ContextCompat.getColor(getActivity(), R.color.black));

        // Align the views in the center of the dialog window
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) btnPositive.getLayoutParams();
        layoutParams.weight = R.dimen.center_position;
        btnPositive.setLayoutParams(layoutParams);
        btnNegative.setLayoutParams(layoutParams);
    }

}
