package com.dv183222m.pki;

import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.dv183222m.pki.com.dv183222m.pki.data.*;

public class ProfileActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Intent intent = getIntent();
        final String username = intent.getStringExtra("Username");
        final User user = DbContext.INSTANCE.getUser(username);

        Toolbar toolbar = findViewById(R.id.toolbarProfile);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);
        actionbar.setTitle("");
        mDrawerLayout = findViewById(R.id.drawer_layout);

        NavigationView navigationView = findViewById(R.id.navViewProfile);
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {

                        Intent intent;
                        switch (menuItem.getItemId()) {
                            case R.id.nav_logout:
                                intent = new Intent(ProfileActivity.this, LoginActivity.class);
                                startActivity(intent);
                                break;
                            case R.id.nav_requests:
                                intent = new Intent(ProfileActivity.this, RequestsActivity.class);
                                intent.putExtra("Username", username);
                                startActivity(intent);
                                break;
                            case R.id.nav_edit_profile:
                                intent = new Intent(ProfileActivity.this, EditProfileActivity.class);
                                intent.putExtra("Username", username);
                                startActivity(intent);
                                break;
                            case R.id.nav_change_pass:
                                changePassword(username);
                                break;
                            case R.id.nav_workers:
                                intent = new Intent(ProfileActivity.this, WorkersActivity.class);
                                intent.putExtra("Username", username);
                                startActivity(intent);
                        }

                        mDrawerLayout.closeDrawers();

                        return true;
                    }
                });

        initProfile(user, navigationView);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initProfile(User user, NavigationView navigationView) {
        if (user.getType().equals(UserType.Worker)) {

            TextView textViewExp = findViewById(R.id.textViewExpProfile);
            textViewExp.setText(user.getWorker().getExperience() + " years of experience");
            textViewExp.setVisibility(View.VISIBLE);

            TextView textViewTypes = findViewById(R.id.textViewTypesProfile);
            StringBuilder types = new StringBuilder();
            for (WorkerType type : user.getWorker().getTypes()) {
                if (types.toString().isEmpty() == false) {
                    types.append(' ');
                }
                types.append(type.name());
            }
            textViewTypes.setText(types);
            textViewTypes.setVisibility(View.VISIBLE);

            RatingBar ratingBar = findViewById(R.id.ratingBarProfile);
            ratingBar.setRating(user.getWorker().getRating());
            ratingBar.setVisibility(View.VISIBLE);

            navigationView.getMenu().findItem(R.id.nav_requests_map).setVisible(true);
            navigationView.getMenu().findItem(R.id.nav_requests_worker).setVisible(true);
        }
        else {
            navigationView.getMenu().findItem(R.id.nav_workers).setVisible(true);
            navigationView.getMenu().findItem(R.id.nav_requests).setVisible(true);
        }

        ImageView imageView = findViewById(R.id.imageViewProfile);
        ImageView imageViewNav = navigationView.getHeaderView(0).findViewById(R.id.imageViewProfileNav);
        if (user.getImage() != 0) {
            imageView.setImageDrawable(getApplicationContext().getResources().getDrawable(user.getImage(), null));
            imageViewNav.setImageDrawable(getApplicationContext().getResources().getDrawable(user.getImage(), null));
        }

        TextView textViewFirstName = findViewById(R.id.textViewFirstNameProfile);
        textViewFirstName.setText(user.getFirstName());

        TextView textViewLastName = findViewById(R.id.textViewLastNameProfile);
        textViewLastName.setText(user.getLastName());

        TextView textViewNameNav = navigationView.getHeaderView(0).findViewById(R.id.textViewNameProfileNav);
        textViewNameNav.setText(user.getUsername());

        TextView textViewAddress = findViewById(R.id.textViewAddressProfile);
        textViewAddress.setText(user.getAddress());

        TextView textViewPhoneNumber = findViewById(R.id.textViewPhoneNumberProfile);
        textViewPhoneNumber.setText(user.getPhoneNumber());
    }

    private void changePassword(final String username) {
        AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
        View view = getLayoutInflater().inflate(R.layout.dialog_change_password, null);

        final TextView textViewOldPass = view.findViewById(R.id.textViewOldPassChangePass);
        final TextView textViewNewPass = view.findViewById(R.id.textViewNewPassChangePass);
        final TextView textViewRepeatPass = view.findViewById(R.id.textViewRepeatPassChangePass);

        builder.setView(view);
        final AlertDialog dialog = builder.create();
        dialog.show();

        Button buttonSubmit = view.findViewById(R.id.buttonSubmitChangePass);
        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String oldPassword = textViewOldPass.getText().toString();
                if (!oldPassword.equals(DbContext.INSTANCE.getUser(username).getPassword())) {
                    Toast.makeText(getApplicationContext(), "Wrong old password.", Toast.LENGTH_LONG).show();
                    return;
                }

                String newPassword = textViewNewPass.getText().toString();
                String repeatPassword = textViewRepeatPass.getText().toString();

                if (!newPassword.equals(repeatPassword)) {
                    Toast.makeText(getApplicationContext(), "New password doesn't match.", Toast.LENGTH_LONG).show();
                    return;
                }

                if(newPassword.equals(oldPassword)) {
                    Toast.makeText(getApplicationContext(), "New and old password cannot match.", Toast.LENGTH_LONG).show();
                    return;
                }

                DbContext.INSTANCE.getUser(username).setPassword(newPassword);

                Toast.makeText(getApplicationContext(), "Password changed.", Toast.LENGTH_LONG).show();

                Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
                startActivity(intent);

                dialog.dismiss();
            }
        });

        Button buttonCancel = view.findViewById(R.id.buttonCancelChangePass);
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    @Override
    public void onBackPressed() {
        
    }
}
