package com.dv183222m.pki;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.dv183222m.pki.com.dv183222m.pki.data.*;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getSupportActionBar().setTitle("");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_layout, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.workersItem:
                Intent intent = new Intent(this, WorkersActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void register(View view) {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    public void login(View view) {
        EditText editTextUsername = findViewById(R.id.editTextUsernameLogin);
        EditText editTextPassword = findViewById(R.id.editTextPasswordLogin);

        if (editTextUsername.getText().toString().isEmpty()
                || editTextPassword.getText().toString().isEmpty()) {
            Toast.makeText(getApplicationContext(),
                    "Please enter username and password.",
                    Toast.LENGTH_LONG).show();
            return;
        }

        User user = DbContext.INSTANCE.getUser(editTextUsername.getText().toString());

        if (user == null) {
            Toast.makeText(getApplicationContext(),
                    "\"" + editTextUsername.getText().toString()
                            + "\" is not associated with any account.",
                    Toast.LENGTH_LONG).show();
        } else if (user.getPassword().equals(editTextPassword.getText().toString())) {
            Intent intent = new Intent(this, ProfileActivity.class);
            intent.putExtra("Username", user.getUsername());
            startActivity(intent);
        } else {
            Toast.makeText(getApplicationContext(),
                    "The password you’ve entered is incorrect.",
                    Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onBackPressed() {

    }
}
