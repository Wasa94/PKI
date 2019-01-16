package com.dv183222m.pki;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.dv183222m.pki.com.dv183222m.pki.data.DbContext;
import com.dv183222m.pki.com.dv183222m.pki.data.Request;
import com.dv183222m.pki.com.dv183222m.pki.data.RequestStatus;

import java.util.Calendar;

public class RequestActivity extends AppCompatActivity {

    private AlertDialog dialogShare;
    private View viewDialogShare;

    private AlertDialog dialogReview;
    private View viewDialogReview;

    Request request;
    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request);

        getSupportActionBar().setTitle("REQUEST");

        Intent intent = getIntent();
        int requestId = intent.getIntExtra("Request", -1);
        username = intent.getStringExtra("Username");

        request = DbContext.INSTANCE.getRequest(requestId);

        if (request != null) {
            TextView textViewType = findViewById(R.id.textViewTypeRequestDetails);
            textViewType.setText(request.getType());

            TextView textViewFirstName = findViewById(R.id.textViewWorkerFirstNameRequestDetails);
            textViewFirstName.setText(request.getWorker().getFirstName());

            TextView textViewWorkerLastName = findViewById(R.id.textViewWorkerLastNameRequestDetails);
            textViewWorkerLastName.setText(request.getWorker().getLastName());

            Calendar calendar = Calendar.getInstance();

            calendar.setTime(request.getFrom());
            TextView textViewFrom = findViewById(R.id.textViewFromRequestDetails);
            textViewFrom.setText(String.format("%02d", calendar.get(Calendar.DAY_OF_MONTH)) + "." + String.format("%02d", calendar.get(Calendar.MONTH) + 1) + "." + calendar.get(Calendar.YEAR) + ".");

            calendar.setTime(request.getTo());
            TextView textViewTo = findViewById(R.id.textViewToRequestDetails);
            textViewTo.setText(String.format("%02d", calendar.get(Calendar.DAY_OF_MONTH)) + "." + String.format("%02d", calendar.get(Calendar.MONTH) + 1) + "." + calendar.get(Calendar.YEAR) + ".");

            TextView textViewPrice = findViewById(R.id.textViewPriceRequestDetails);
            textViewPrice.setText(request.getPrice() + " RSD");

            TextView textViewDetails = findViewById(R.id.textViewDetailsRequestDetails);
            textViewDetails.setText(request.getDetails());

            TextView textViewReview = findViewById(R.id.textViewReviewRequestDetails);
            textViewReview.setText(request.getReview());

            RatingBar ratingBar = findViewById(R.id.ratingBarRequestDetails);
            ratingBar.setRating(request.getRating());

            TextView textViewStatus = findViewById(R.id.textViewStatusRequestDetails);
            textViewStatus.setText(request.getStatus().name());
        }

        CardView cardViewReview = findViewById(R.id.cardViewReviewRequestDetails);
        Button buttonCancelRequest = findViewById(R.id.buttonCancelRequest);

        if(request.getStatus() == RequestStatus.New) {
            buttonCancelRequest.setVisibility(View.VISIBLE);
        } else if(request.getStatus() == RequestStatus.Successful || request.getStatus() == RequestStatus.Unsuccessful) {
            cardViewReview.setVisibility(View.VISIBLE);
        }
    }

    public void shareRequest(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(RequestActivity.this);
        viewDialogShare = getLayoutInflater().inflate(R.layout.share_dialog, null);

        builder.setView(viewDialogShare);
        dialogShare = builder.create();
        dialogShare.show();
    }

    public void createRequest(View view) {
        EditText editTextUsername = viewDialogShare.findViewById(R.id.editTextUsernameShare);
        EditText editTextPassword = viewDialogShare.findViewById(R.id.editTextPasswordShare);

        if (editTextUsername.getText().toString().isEmpty() || editTextPassword.getText().toString().isEmpty()) {
            Toast.makeText(getApplicationContext(),
                    "Please enter username and password.",
                    Toast.LENGTH_LONG).show();
            return;
        }

        Toast.makeText(getApplicationContext(),
                "Worker shared succesfully.",
                Toast.LENGTH_LONG).show();
        dialogShare.dismiss();
    }

    public void reviewRequest(View view) {
        if(!request.getDetails().isEmpty() && request.getRating() != 0 ) {
            return;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(RequestActivity.this);
        viewDialogReview = getLayoutInflater().inflate(R.layout.review_dialog, null);

        builder.setView(viewDialogReview);
        dialogReview = builder.create();
        dialogReview.show();
    }

    public void reviewSubmit(View view) {
        EditText EditTextReview = viewDialogReview.findViewById(R.id.editTextReview);
        request.setReview(EditTextReview.getText().toString());

        RatingBar ratingBar = viewDialogReview.findViewById(R.id.ratingBarReview);
        request.setRating(ratingBar.getRating());

        TextView textViewReview = findViewById(R.id.textViewReviewRequestDetails);
        textViewReview.setText(request.getReview());

        RatingBar ratingBarRD = findViewById(R.id.ratingBarRequestDetails);
        ratingBarRD.setRating(request.getRating());

        dialogReview.dismiss();
    }

    public void reviewCancel(View view) {
        dialogReview.dismiss();
    }

    public void requestCancel(View view) {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        boolean status = DbContext.INSTANCE.cancelRequest(request.getId());
                        if(status) {

                            Toast.makeText(getApplicationContext(),
                                    "Request canceled.",
                                    Toast.LENGTH_LONG).show();

                            Intent intent = new Intent(RequestActivity.this, ProfileActivity.class);
                            intent.putExtra("Username", username);
                            startActivity(intent);
                        } else {
                            Toast.makeText(getApplicationContext(),
                                    "Error: Could not cancel request.",
                                    Toast.LENGTH_LONG).show();
                        }
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(RequestActivity.this);
        builder.setMessage("Are you sure?").setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();
    }
}
