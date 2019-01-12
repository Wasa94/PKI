package com.dv183222m.pki;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.dv183222m.pki.com.dv183222m.pki.data.DbContext;
import com.dv183222m.pki.com.dv183222m.pki.data.Request;
import com.dv183222m.pki.com.dv183222m.pki.data.User;
import com.dv183222m.pki.com.dv183222m.pki.data.WorkerType;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class CreateRequestActivity extends AppCompatActivity {

    private String username, workerUsername;
    private Date dateFrom, dateTo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_request);

        Intent intent = getIntent();
        username = intent.getStringExtra("User");
        workerUsername = intent.getStringExtra("Worker");

        initTypes();
        initPaymant();
        initDatePickers();

        getSupportActionBar().setTitle("CREATE REQUEST");
    }

    public void createRequest(View view) {

        Spinner spinnerType = findViewById(R.id.spinnerTypeCreateRequest);
        Spinner spinnerPayment = findViewById(R.id.spinnerPaymentCreateRequest);
        EditText editTextPrice = findViewById(R.id.editTextPriceCreateRequest);
        EditText editTextDetails = findViewById(R.id.editTextDetailsCreateRequest);
        EditText editTextMunicipality = findViewById(R.id.editTextMunicipalityCreateRequest);
        EditText editTextAddress = findViewById(R.id.editTextAddressCreateRequest);

        if (editTextPrice.getText().toString().isEmpty()
                || editTextDetails.getText().toString().isEmpty()
                || editTextMunicipality.getText().toString().isEmpty()
                || editTextAddress.getText().toString().isEmpty()) {
            Toast.makeText(getApplicationContext(),
                    "All data is required.",
                    Toast.LENGTH_LONG).show();
            return;
        }

        if (dateFrom.after(dateTo)) {
            Toast.makeText(getApplicationContext(),
                    "Date \"TO\" must be after \"BEFORE\".",
                    Toast.LENGTH_LONG).show();
            return;
        }

        User user = DbContext.INSTANCE.getUser(username);
        User worker = DbContext.INSTANCE.getUser(workerUsername);

        String type = spinnerType.getSelectedItem().toString();
        int price = 0;
        try {
            price = Integer.parseInt(editTextPrice.getText().toString());
        }
        catch(NumberFormatException e)
        {
            return;
        }
        String details = editTextDetails.getText().toString();
        boolean creditCard = spinnerPayment.getSelectedItem().toString().equals("Credit card");
        String municipality = editTextMunicipality.getText().toString();
        String address = editTextAddress.getText().toString();

        Request request = new Request(user, worker, municipality, address, dateFrom, dateTo, WorkerType.getType(type), creditCard, price, details);
        DbContext.INSTANCE.createRequest(request);

        if (creditCard) {
            Intent intent = new Intent(this, CreditCardActivity.class);
            intent.putExtra("Username", username);
            intent.putExtra("Request", request.getId());
            startActivity(intent);
        } else {
            DbContext.INSTANCE.submitRequest(request.getId());
            Toast.makeText(getApplicationContext(),
                    "Request created.",
                    Toast.LENGTH_LONG).show();

            Intent intent = new Intent(this, ProfileActivity.class);
            intent.putExtra("Username", username);
            startActivity(intent);
        }
    }

    private void initTypes() {
        Spinner spinnerType = findViewById(R.id.spinnerTypeCreateRequest);

        List<String> types = new ArrayList<>();
        for (WorkerType workerType : WorkerType.values()) {
            types.add(workerType.getWorkType());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.spinner_item, types);
        spinnerType.setAdapter(adapter);
    }

    private void initPaymant() {
        Spinner spinnerPaymant = findViewById(R.id.spinnerPaymentCreateRequest);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.paymant_array, R.layout.spinner_item);
        spinnerPaymant.setAdapter(adapter);
    }


    private void initDatePickers() {
        final TextView textViewFrom = findViewById(R.id.textViewFromCreateRequest);
        final TextView textViewTo = findViewById(R.id.textViewToCreateRequest);
        Button buttonFrom = findViewById(R.id.buttonFromCreateRequest);
        Button buttonTo = findViewById(R.id.buttonToCreateRequest);

        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);
        textViewFrom.setText(String.format("%02d", day) + "." + String.format("%02d", month + 1) + "." + year + ".");
        textViewTo.setText(String.format("%02d", day) + "." + String.format("%02d", month + 1) + "." + year + ".");

        dateFrom = calendar.getTime();
        dateTo = calendar.getTime();

        buttonFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);

                DatePickerDialog dpd = new DatePickerDialog(CreateRequestActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        textViewFrom.setText(String.format("%02d", dayOfMonth) + "." + String.format("%02d", month + 1) + "." + year + ".");

                        Calendar c = Calendar.getInstance();
                        c.set(year, month, dayOfMonth);
                        dateFrom = c.getTime();
                    }
                }, year, month, day);
                dpd.show();
            }
        });

        buttonTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);

                DatePickerDialog dpd = new DatePickerDialog(CreateRequestActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        textViewTo.setText(String.format("%02d", dayOfMonth) + "." + String.format("%02d", month + 1) + "." + year + ".");

                        Calendar c = Calendar.getInstance();
                        c.set(year, month, dayOfMonth);
                        dateTo = c.getTime();
                    }
                }, year, month, day);
                dpd.show();
            }
        });
    }
}
