package com.example.asus.trendhimapp.settings;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.example.asus.trendhimapp.R;
import com.example.asus.trendhimapp.mainActivities.BaseActivity;
import com.example.asus.trendhimapp.util.Constants;

public class Contact extends BaseActivity {

    private EditText subject, message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_contact_us, null, false);
        BaseActivity.drawer.addView(contentView, 0);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        setTitle(getString(R.string.contact_us));

        subject = findViewById(R.id.subject_contact);
        message = findViewById(R.id.question_contact);
    }


    public void submitContact(View view){

        Intent mail = new Intent(Intent.ACTION_SEND);
        mail.putExtra(Intent.EXTRA_EMAIL,new String[]{Constants.GMAIL_EMAIL});
        mail.putExtra(Intent.EXTRA_SUBJECT, subject.getText().toString());
        mail.putExtra(Intent.EXTRA_TEXT, message.getText().toString());
        mail.setType("message/rfc822");
        startActivity(Intent.createChooser(mail, getString(R.string.send_email_via)));
    }

}
