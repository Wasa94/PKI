package com.dv183222m.pki;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.dv183222m.pki.com.dv183222m.pki.data.DbContext;
import com.dv183222m.pki.com.dv183222m.pki.data.User;
import com.dv183222m.pki.com.dv183222m.pki.data.UserType;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initTypes();

        getSupportActionBar().setTitle("REGISTRATION");
    }

    private void initTypes() {
        Spinner typesSpinner = findViewById(R.id.typesSpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.types_array, R.layout.spinner_item);
        typesSpinner.setAdapter(adapter);
    }

    public void register(View view) {
        EditText firstNameEditText = findViewById(R.id.firstNameEditText);
        EditText lastNameEditText = findViewById(R.id.lastNameEditText);
        Spinner typesSpinner = findViewById(R.id.typesSpinner);
        EditText addressEditText = findViewById(R.id.addressEditText);
        EditText phoneNumberEditText = findViewById(R.id.phoneNumberEditText);
        EditText usernameEditText = findViewById(R.id.usernameEditText);
        EditText passwordEditText = findViewById(R.id.passwordEditText);

        if (usernameEditText.getText().toString().isEmpty()
                || passwordEditText.getText().toString().isEmpty()
                || firstNameEditText.getText().toString().isEmpty()
                || lastNameEditText.getText().toString().isEmpty()
                || addressEditText.getText().toString().isEmpty()
                || phoneNumberEditText.getText().toString().isEmpty()
                || typesSpinner.getSelectedItem().toString().isEmpty()) {
            Toast.makeText(getApplicationContext(),
                    "All data is required.",
                    Toast.LENGTH_LONG).show();
            return;
        }


        User user = new User(firstNameEditText.getText().toString(),
                lastNameEditText.getText().toString(),
                UserType.valueOf(typesSpinner.getSelectedItem().toString()),
                addressEditText.getText().toString(),
                phoneNumberEditText.getText().toString(), usernameEditText.getText().toString(),
                passwordEditText.getText().toString());

        boolean result = DbContext.INSTANCE.addUser(user);

        if (result == false) {
            Toast.makeText(getApplicationContext(),
                    "User \"" + user.getUsername() + "\" already exists.",
                    Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getApplicationContext(),
                    "SUCCESS",
                    Toast.LENGTH_LONG).show();
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }
    }
}
