package com.dv183222m.pki;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.dv183222m.pki.com.dv183222m.pki.data.DbContext;

import java.util.Calendar;
import java.util.Date;

public class CreditCardActivity extends AppCompatActivity {

    String username;
    int requestId;
    Date date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credit_card);

        Intent intent = getIntent();
        username = intent.getStringExtra("Username");
        requestId = intent.getIntExtra("Request", -1);

        initDatePicker();

        getSupportActionBar().setTitle("PAYMANT");
    }

    public void createRequest(View view) {
        EditText editTextCardNumber = findViewById(R.id.editTextCardNumberCreditCard);
        EditText editTextCVV = findViewById(R.id.editTextCVVCreditCard);
        EditText editTextHolderName = findViewById(R.id.editTextHolderNameCreditCard);

        if (editTextCardNumber.getText().toString().isEmpty()
                || editTextCVV.getText().toString().isEmpty()
                || editTextHolderName.getText().toString().isEmpty()) {
            Toast.makeText(getApplicationContext(),
                    "All data is required.",
                    Toast.LENGTH_LONG).show();
            return;
        }

        if(date.before(Calendar.getInstance().getTime())){
            Toast.makeText(getApplicationContext(),
                    "Credit card is expired.",
                    Toast.LENGTH_LONG).show();
            return;
        }

        DbContext.INSTANCE.submitRequest(requestId);

        Toast.makeText(getApplicationContext(),
                "Request created.",
                Toast.LENGTH_LONG).show();

        Intent intent = new Intent(this, ProfileActivity.class);
        intent.putExtra("Username", username);
        startActivity(intent);
    }

    private void initDatePicker() {
        final TextView textViewDate = findViewById(R.id.textViewDateCreditCard);
        Button buttonDate = findViewById(R.id.buttonDateCreditCard);

        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);
        textViewDate.setText(String.format("%02d", day) + "." + String.format("%02d", month + 1) + "." + year + ".");

        date = calendar.getTime();

        buttonDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);

                DatePickerDialog dpd = new DatePickerDialog(CreditCardActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        textViewDate.setText(String.format("%02d", dayOfMonth) + "." + String.format("%02d", month + 1) + "." + year + ".");

                        Calendar c = Calendar.getInstance();
                        c.set(year, month, dayOfMonth);
                        date = c.getTime();
                    }
                }, year, month, day);
                dpd.show();
            }
        });
    }
}
