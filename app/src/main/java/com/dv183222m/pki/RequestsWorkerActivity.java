package com.dv183222m.pki;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import com.dv183222m.pki.com.dv183222m.pki.data.*;
import com.jaygoo.widget.OnRangeChangedListener;
import com.jaygoo.widget.RangeSeekBar;

import java.util.*;

public class RequestsWorkerActivity extends AppCompatActivity {

    private String username;
    private Date dateFrom, dateTo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requests_worker);

        Intent intent = getIntent();
        final String username = intent.getStringExtra("Username");
        this.username = username;

        RecyclerView recyclerView = findViewById(R.id.recyclerViewRequestsWorker);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        RequestAdapter adapter = new RequestAdapter(this, DbContext.INSTANCE.getRequestsWorker(username), new RequestAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Request item) {
                requestDetails(item.getId());
            }
        }, true);

        recyclerView.setAdapter(adapter);

        getSupportActionBar().setTitle("REQUESTS");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.filter_layout, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.filterItem:
                showFilter();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void requestDetails(int id) {
        Intent intent = new Intent(this, RequestWorkerActivity.class);
        intent.putExtra("Request", id);
        intent.putExtra("Username", username);
        startActivity(intent);
    }

    private void showFilter() {
        AlertDialog.Builder builder = new AlertDialog.Builder(RequestsWorkerActivity.this);
        View view = getLayoutInflater().inflate(R.layout.dialog_filter_requests, null);

        initDatePickers(view);
        initSpinners(view);

        RangeSeekBar rangeSeekBarPrice = view.findViewById(R.id.rangeSeekBarPriceRequestsFilter);
        final int[] valuesPrice = new int[]{0, 5};
        rangeSeekBarPrice.setOnRangeChangedListener(new OnRangeChangedListener() {
            @Override
            public void onRangeChanged(RangeSeekBar view, float leftValue, float rightValue, boolean isFromUser) {
                valuesPrice[0] = (int)leftValue;
                valuesPrice[1] = (int)rightValue;
            }

            @Override
            public void onStartTrackingTouch(RangeSeekBar view, boolean isLeft) {
                //start tracking touch
            }

            @Override
            public void onStopTrackingTouch(RangeSeekBar view, boolean isLeft) {
                //stop tracking touch
            }
        });
        rangeSeekBarPrice.setValue(rangeSeekBarPrice.getMinProgress(), rangeSeekBarPrice.getMaxProgress());

        builder.setView(view);
        final AlertDialog dialog = builder.create();
        dialog.show();

        final TextView textViewClient = view.findViewById(R.id.textViewWorkerRequestsFilter);
        textViewClient.setHint("Client");

        final MultiSelectionSpinner typeSpinner = view.findViewById(R.id.spinnerTypeRequestsFilter);
        final MultiSelectionSpinner statusSpinner = view.findViewById(R.id.spinnerStatusRequestsFilter);

        Button buttonFilter = view.findViewById(R.id.buttonFilterRequestsFilter);
        buttonFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RecyclerView recyclerView = findViewById(R.id.recyclerViewRequestsWorker);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(new LinearLayoutManager(RequestsWorkerActivity.this));

                List<String> types = typeSpinner.getSelectedStrings();
                List<WorkerType> workTypes = new ArrayList<>();
                for (String type : types) {
                    workTypes.add(WorkerType.getType(type));
                }

                List<String> statuses = statusSpinner.getSelectedStrings();
                List<RequestStatus> requestStatuses = new ArrayList<>();
                for (String status : statuses) {
                    requestStatuses.add(RequestStatus.valueOf(status));
                }

                List<Request> requests = DbContext.INSTANCE.getRequestsWorker(username, textViewClient.getText().toString(), workTypes, requestStatuses,
                        valuesPrice[0], valuesPrice[1], dateFrom, dateTo);

                RequestAdapter adapter = new RequestAdapter(RequestsWorkerActivity.this, requests, new RequestAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(Request item) {
                        requestDetails(item.getId());
                    }
                }, true);

                recyclerView.setAdapter(adapter);
                dialog.dismiss();
            }
        });

        Button buttonClear = view.findViewById(R.id.buttonClearRequestsFilter);
        buttonClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RecyclerView recyclerView = findViewById(R.id.recyclerViewRequestsWorker);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(new LinearLayoutManager(RequestsWorkerActivity.this));

                RequestAdapter adapter = new RequestAdapter(RequestsWorkerActivity.this, DbContext.INSTANCE.getRequestsWorker(username), new RequestAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(Request item) {
                        requestDetails(item.getId());
                    }
                }, true);

                recyclerView.setAdapter(adapter);
                dialog.dismiss();
            }
        });
    }

    private void initDatePickers(View view) {
        final TextView textViewFrom = view.findViewById(R.id.textViewFromRequestsFilter);
        final TextView textViewTo = view.findViewById(R.id.textViewToRequestsFilter);
        Button buttonFrom = view.findViewById(R.id.buttonFromRequestsFilter);
        Button buttonTo = view.findViewById(R.id.buttonToRequestsFilter);

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

                DatePickerDialog dpd = new DatePickerDialog(RequestsWorkerActivity.this, new DatePickerDialog.OnDateSetListener() {
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

                DatePickerDialog dpd = new DatePickerDialog(RequestsWorkerActivity.this, new DatePickerDialog.OnDateSetListener() {
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

    private void initSpinners(View view) {

        MultiSelectionSpinner statusSpinner = view.findViewById(R.id.spinnerStatusRequestsFilter);
        ArrayAdapter<RequestStatus> adapterRequestStatus = new ArrayAdapter<RequestStatus>(RequestsWorkerActivity.this, R.layout.spinner_item, RequestStatus.values());

        ArrayList<String> list = new ArrayList<>();
        for (int i = 0; i < adapterRequestStatus.getCount(); i++)
            list.add(adapterRequestStatus.getItem(i).toString());

        statusSpinner.setItems(list);


        MultiSelectionSpinner typeSpinner = view.findViewById(R.id.spinnerTypeRequestsFilter);
        List<WorkerType> types = DbContext.INSTANCE.getUser(username).getWorker().getTypes();

        list = new ArrayList<>();
        for (WorkerType type: types) {
            list.add(type.getWorkType());
        }

        typeSpinner.setItems(list);
    }
}