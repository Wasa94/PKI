package com.dv183222m.pki;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.dv183222m.pki.com.dv183222m.pki.data.DbContext;
import com.dv183222m.pki.com.dv183222m.pki.data.User;

public class EditProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        getSupportActionBar().setTitle("EDIT PROFILE");

        Intent intent = getIntent();
        String username = intent.getStringExtra("Username");
        final User user = DbContext.INSTANCE.getUser(username);

        final EditText editTextFirstName = findViewById(R.id.editTextFirstNameEditProfile);
        editTextFirstName.setText(user.getFirstName());

        final EditText editTextLastName = findViewById(R.id.editTextLastNameEditProfile);
        editTextLastName.setText(user.getLastName());

        final EditText editTextAddress = findViewById(R.id.editTextAddressEditProfile);
        editTextAddress.setText(user.getAddress());

        final EditText editTextPhoneNumber = findViewById(R.id.editTextPhoneNumberEditProfile);
        editTextPhoneNumber.setText(user.getPhoneNumber());

        ImageView imageView = findViewById(R.id.imageViewEditProfile);
        if (user.getImage() != 0) {
            imageView.setImageDrawable(getApplicationContext().getResources().getDrawable(user.getImage(), null));
        }

        FloatingActionButton buttonSave = findViewById(R.id.buttonSaveEditProfile);
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user.setFirstName(editTextFirstName.getText().toString());
                user.setLastName(editTextLastName.getText().toString());
                user.setAddress(editTextAddress.getText().toString());
                user.setPhoneNumber(editTextPhoneNumber.getText().toString());


                Toast.makeText(getApplicationContext(),
                        "Profile changes saved.",
                        Toast.LENGTH_LONG).show();

                Intent intentNew = new Intent(EditProfileActivity.this, ProfileActivity.class);
                intentNew.putExtra("Username", user.getUsername());
                startActivity(intentNew);
            }
        });
    }
}
