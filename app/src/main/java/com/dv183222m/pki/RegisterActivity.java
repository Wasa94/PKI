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
        Spinner spinnerType = findViewById(R.id.spinnerTypeRegister);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.types_array, R.layout.spinner_item);
        spinnerType.setAdapter(adapter);
    }

    public void register(View view) {
        EditText editTextFirstName = findViewById(R.id.editTextFirstNameRegister);
        EditText editTextLastName = findViewById(R.id.editTextLastNameRegister);
        Spinner spinnerType = findViewById(R.id.spinnerTypeRegister);
        EditText editTextAddress = findViewById(R.id.editTextAddressRegister);
        EditText editTextPhoneNumber = findViewById(R.id.editTextPhoneNumberRegister);
        EditText editTextUsername = findViewById(R.id.editTextUsernameRegister);
        EditText editTextPassword = findViewById(R.id.editTextPasswordRegister);

        if (editTextUsername.getText().toString().isEmpty()
                || editTextPassword.getText().toString().isEmpty()
                || editTextFirstName.getText().toString().isEmpty()
                || editTextLastName.getText().toString().isEmpty()
                || editTextAddress.getText().toString().isEmpty()
                || editTextPhoneNumber.getText().toString().isEmpty()
                || spinnerType.getSelectedItem().toString().isEmpty()) {
            Toast.makeText(getApplicationContext(),
                    "All data is required.",
                    Toast.LENGTH_LONG).show();
            return;
        }


        User user = new User(editTextFirstName.getText().toString(),
                editTextLastName.getText().toString(),
                UserType.valueOf(spinnerType.getSelectedItem().toString()),
                editTextAddress.getText().toString(),
                editTextPhoneNumber.getText().toString(), editTextUsername.getText().toString(),
                editTextPassword.getText().toString());

        boolean result = DbContext.INSTANCE.addUser(user);

        if (result == false) {
            Toast.makeText(getApplicationContext(),
                    "User \"" + user.getUsername() + "\" already exists.",
                    Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getApplicationContext(),
                    "User created.",
                    Toast.LENGTH_LONG).show();
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }
    }
}
