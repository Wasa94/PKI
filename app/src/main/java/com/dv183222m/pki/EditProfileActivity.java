package com.dv183222m.pki;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.dv183222m.pki.com.dv183222m.pki.data.DbContext;
import com.dv183222m.pki.com.dv183222m.pki.data.User;
import com.dv183222m.pki.com.dv183222m.pki.data.UserType;
import com.dv183222m.pki.com.dv183222m.pki.data.WorkerType;

import java.util.ArrayList;

public class EditProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        getSupportActionBar().setTitle("EDIT PROFILE");

        Intent intent = getIntent();
        String username = intent.getStringExtra("Username");
        final User user = DbContext.INSTANCE.getUser(username);

        final MultiSelectionSpinner spinnerTypes = findViewById(R.id.spinnerTypesEditProfile);

        if (user.getType().equals(UserType.Worker)) {
            initSpinner();

            ArrayList<String> list = new ArrayList<>();
            for (WorkerType type : user.getWorker().getTypes()) {
                list.add(type.name());
            }

            spinnerTypes.setSelection(list);

            spinnerTypes.setVisibility(View.VISIBLE);
        }

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

                if(user.getType().equals(UserType.Worker)) {
                    ArrayList<WorkerType> list = new ArrayList<>();
                    for (String type:spinnerTypes.getSelectedStrings()) {
                        list.add(WorkerType.valueOf(type));
                    }
                    user.getWorker().setTypes(list);
                }

                Toast.makeText(getApplicationContext(),
                        "Profile changes saved.",
                        Toast.LENGTH_LONG).show();

                Intent intentNew = new Intent(EditProfileActivity.this, ProfileActivity.class);
                intentNew.putExtra("Username", user.getUsername());
                startActivity(intentNew);
            }
        });
    }

    private void initSpinner() {
        MultiSelectionSpinner spinnerTypes = findViewById(R.id.spinnerTypesEditProfile);
        ArrayAdapter<WorkerType> adapterWorkerTypes = new ArrayAdapter<WorkerType>(EditProfileActivity.this, R.layout.spinner_item, WorkerType.values());

        ArrayList<String> list = new ArrayList<>();
        for (int i = 0; i < adapterWorkerTypes.getCount(); i++)
            list.add(adapterWorkerTypes.getItem(i).toString());

        spinnerTypes.setItems(list);

    }
}
