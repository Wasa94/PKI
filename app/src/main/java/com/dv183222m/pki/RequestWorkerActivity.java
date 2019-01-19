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
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dv183222m.pki.com.dv183222m.pki.data.DbContext;
import com.dv183222m.pki.com.dv183222m.pki.data.Request;
import com.dv183222m.pki.com.dv183222m.pki.data.RequestStatus;

import java.util.Calendar;

public class RequestWorkerActivity extends AppCompatActivity {
    Request request;
    String username;

    TextView textViewStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_worker);

        getSupportActionBar().setTitle("REQUEST");

        Intent intent = getIntent();
        int requestId = intent.getIntExtra("Request", -1);
        username = intent.getStringExtra("Username");

        request = DbContext.INSTANCE.getRequest(requestId);

        if (request != null) {
            TextView textViewType = findViewById(R.id.textViewTypeRequestWorkerDetails);
            textViewType.setText(request.getType());

            TextView textViewFirstName = findViewById(R.id.textViewWorkerFirstNameRequestWorkerDetails);
            textViewFirstName.setText(request.getClient().getFirstName());

            TextView textViewWorkerLastName = findViewById(R.id.textViewWorkerLastNameRequestWorkerDetails);
            textViewWorkerLastName.setText(request.getClient().getLastName());

            Calendar calendar = Calendar.getInstance();

            calendar.setTime(request.getFrom());
            TextView textViewFrom = findViewById(R.id.textViewFromRequestWorkerDetails);
            textViewFrom.setText(String.format("%02d", calendar.get(Calendar.DAY_OF_MONTH)) + "." + String.format("%02d", calendar.get(Calendar.MONTH) + 1) + "." + calendar.get(Calendar.YEAR) + ".");

            calendar.setTime(request.getTo());
            TextView textViewTo = findViewById(R.id.textViewToRequestWorkerDetails);
            textViewTo.setText(String.format("%02d", calendar.get(Calendar.DAY_OF_MONTH)) + "." + String.format("%02d", calendar.get(Calendar.MONTH) + 1) + "." + calendar.get(Calendar.YEAR) + ".");

            TextView textViewPrice = findViewById(R.id.textViewPriceRequestWorkerDetails);
            String cashOrCreditCard = request.isCreditCard() ? "Credit Card" : "Cash";
            textViewPrice.setText(request.getPrice() + " RSD - " + cashOrCreditCard);

            TextView textViewDetails = findViewById(R.id.textViewDetailsRequestWorkerDetails);
            textViewDetails.setText(request.getDetails());

            textViewStatus = findViewById(R.id.textViewStatusRequestWorkerDetails);
            textViewStatus.setText(request.getStatus().name());

            initButtons();
        }
    }

    private void initButtons() {
        TableLayout buttonsNew = findViewById(R.id.buttonsNewRequestWorker);
        TableLayout buttonsOngoing = findViewById(R.id.buttonsOngoingRequestWorker);

        if (request.getStatus() == RequestStatus.New) {
            buttonsNew.setVisibility(View.VISIBLE);
        } else if (request.getStatus() == RequestStatus.Ongoing) {
            buttonsOngoing.setVisibility(View.VISIBLE);
        } else {
            buttonsNew.setVisibility(View.GONE);
            buttonsOngoing.setVisibility(View.GONE);
        }
    }

    public void requestReject(View view) {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        request.setStatus(RequestStatus.Rejected);

                        Toast.makeText(getApplicationContext(),
                                "Request rejected.",
                                Toast.LENGTH_LONG).show();

                        Intent intent = new Intent(RequestWorkerActivity.this, ProfileActivity.class);
                        intent.putExtra("Username", username);
                        startActivity(intent);
                        break;
                    case DialogInterface.BUTTON_NEGATIVE:
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(RequestWorkerActivity.this);
        builder.setMessage("Are you sure?").setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();
    }

    public void requestAccept(View view) {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        request.setStatus(RequestStatus.Ongoing);

                        Toast.makeText(getApplicationContext(),
                                "Request accepted.",
                                Toast.LENGTH_LONG).show();

                        Intent intent = new Intent(RequestWorkerActivity.this, ProfileActivity.class);
                        intent.putExtra("Username", username);
                        startActivity(intent);
                        break;
                    case DialogInterface.BUTTON_NEGATIVE:
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(RequestWorkerActivity.this);
        builder.setMessage("Are you sure?").setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();
    }

    public void requestSuccessful(View view) {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        request.setStatus(RequestStatus.Successful);

                        Toast.makeText(getApplicationContext(),
                                "Request successful.",
                                Toast.LENGTH_LONG).show();

                        Intent intent = new Intent(RequestWorkerActivity.this, ProfileActivity.class);
                        intent.putExtra("Username", username);
                        startActivity(intent);
                        break;
                    case DialogInterface.BUTTON_NEGATIVE:
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(RequestWorkerActivity.this);
        builder.setMessage("Are you sure?").setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();
    }

    public void requestUnsuccessful(View view) {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        request.setStatus(RequestStatus.Unsuccessful);

                        Toast.makeText(getApplicationContext(),
                                "Request unsuccessful.",
                                Toast.LENGTH_LONG).show();

                        Intent intent = new Intent(RequestWorkerActivity.this, ProfileActivity.class);
                        intent.putExtra("Username", username);
                        startActivity(intent);
                        break;
                    case DialogInterface.BUTTON_NEGATIVE:
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(RequestWorkerActivity.this);
        builder.setMessage("Are you sure?").setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();
    }

    public void requestMap(View view) {
        Intent intent = new Intent(RequestWorkerActivity.this, RequestsMapActivity.class);
        intent.putExtra("Request", request.getId());
        intent.putExtra("Username", username);
        startActivity(intent);
    }
}
