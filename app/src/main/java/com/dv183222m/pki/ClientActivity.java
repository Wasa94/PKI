package com.dv183222m.pki;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.dv183222m.pki.com.dv183222m.pki.data.DbContext;
import com.dv183222m.pki.com.dv183222m.pki.data.User;

public class ClientActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client);

        getSupportActionBar().setTitle("CLIENT");

        Intent intent = getIntent();
        String username = intent.getStringExtra("Username");
        User user = DbContext.INSTANCE.getUser(username);

        ImageView imageView = findViewById(R.id.imageViewClient);
        if (user.getImage() != 0) {
            imageView.setImageDrawable(getApplicationContext().getResources().getDrawable(user.getImage(), null));
        }

        TextView textViewFirstName = findViewById(R.id.textViewFirstNameClient);
        textViewFirstName.setText(user.getFirstName());

        TextView textViewLastName = findViewById(R.id.textViewLastNameClient);
        textViewLastName.setText(user.getLastName());

        TextView textViewAddress = findViewById(R.id.textViewAddressClient);
        textViewAddress.setText(user.getAddress());

        TextView textViewPhoneNumber = findViewById(R.id.textViewPhoneNumberClient);
        textViewPhoneNumber.setText(user.getPhoneNumber());
    }
}
