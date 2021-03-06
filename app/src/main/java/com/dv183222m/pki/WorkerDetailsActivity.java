package com.dv183222m.pki;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.dv183222m.pki.com.dv183222m.pki.data.DbContext;
import com.dv183222m.pki.com.dv183222m.pki.data.User;
import com.dv183222m.pki.com.dv183222m.pki.data.WorkerType;

import org.w3c.dom.Text;

public class WorkerDetailsActivity extends AppCompatActivity {

    String user;
    String workerUsername;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_worker_details);

        getSupportActionBar().setTitle("WORKER DETAILS");

        Intent intent = getIntent();

        workerUsername = intent.getStringExtra("Worker");
        User worker = DbContext.INSTANCE.getUser(workerUsername);

        user = intent.getStringExtra("User");
        if(user != null && !user.isEmpty()) {
            FloatingActionButton buttonCreateRequest = findViewById(R.id.buttonCreateRequestWorker);
            buttonCreateRequest.show();
        }

        ImageView imageView = findViewById(R.id.imageViewWorker);
        if(worker.getImage() != 0) {
            imageView.setImageDrawable(getApplicationContext().getResources().getDrawable(worker.getImage(), null));
        }

        TextView textViewFirstName = findViewById(R.id.textViewFirstNameWorker);
        textViewFirstName.setText(worker.getFirstName());

        TextView textViewLastName = findViewById(R.id.textViewLastNameWorker);
        textViewLastName.setText(worker.getLastName());

        TextView textViewAddress = findViewById(R.id.textViewAddressWorker);
        textViewAddress.setText(worker.getAddress());

        TextView textViewPhoneNumber = findViewById(R.id.textViewPhoneNumberWorker);
        textViewPhoneNumber.setText(worker.getPhoneNumber());

        TextView textViewExp = findViewById(R.id.textViewExpWorker);
        String years = worker.getWorker().getExperience() == 1 ? " year" : " years";
        textViewExp.setText(worker.getWorker().getExperience() + years + " of experience");

        TextView textViewTypes = findViewById(R.id.textViewTypesWorker);
        StringBuilder types = new StringBuilder();
        for (WorkerType type: worker.getWorker().getTypes()) {
            if(types.toString().isEmpty() == false) {
                types.append(' ');
            }
            types.append(type.name());
        }
        textViewTypes.setText(types);

        RatingBar ratingBar = findViewById(R.id.ratingBarWorker);
        ratingBar.setRating(worker.getWorker().getRating());
    }

    public void createRequest(View view) {
        Intent intent = new Intent(this, CreateRequestActivity.class);
        intent.putExtra("User", user);
        intent.putExtra("Worker", workerUsername);
        startActivity(intent);
    }
}
