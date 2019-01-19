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

public class RequestsActivity extends AppCompatActivity {

    private String username;

    private Date dateFromVal = Calendar.getInstance().getTime();
    private Date dateToVal = Calendar.getInstance().getTime();
    private String nameVal = "";
    private List<RequestStatus> statusesVal = new ArrayList<>();
    private List<WorkerType> workerTypesVal = new ArrayList<>();
    private int[] priceVal = new int[]{0, 20000};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requests);

        Intent intent = getIntent();
        final String username = intent.getStringExtra("Username");
        this.username = username;

        RecyclerView recyclerView = findViewById(R.id.recyclerViewRequests);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        RequestAdapter adapter = new RequestAdapter(this, DbContext.INSTANCE.getRequestsClient(username), new RequestAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Request item) {
                requestDetails(item.getId());
            }
        }, false);

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
        Intent intent = new Intent(this, RequestActivity.class);
        intent.putExtra("Request", id);
        intent.putExtra("Username", username);
        startActivity(intent);
    }

    private void showFilter() {
        AlertDialog.Builder builder = new AlertDialog.Builder(RequestsActivity.this);
        View view = getLayoutInflater().inflate(R.layout.dialog_filter_requests, null);

        initDatePickers(view);
        initSpinners(view);

        RangeSeekBar rangeSeekBarPrice = view.findViewById(R.id.rangeSeekBarPriceRequestsFilter);
        final int[] valuesPrice = new int[]{priceVal[0], priceVal[1]};
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
        rangeSeekBarPrice.setValue(priceVal[0], priceVal[1]);

        builder.setView(view);
        final AlertDialog dialog = builder.create();
        dialog.show();

        final TextView textViewWorker = view.findViewById(R.id.textViewWorkerRequestsFilter);
        textViewWorker.setText(nameVal);
        final MultiSelectionSpinner typeSpinner = view.findViewById(R.id.spinnerTypeRequestsFilter);
        final MultiSelectionSpinner statusSpinner = view.findViewById(R.id.spinnerStatusRequestsFilter);

        Button buttonFilter = view.findViewById(R.id.buttonFilterRequestsFilter);
        buttonFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RecyclerView recyclerView = findViewById(R.id.recyclerViewRequests);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(new LinearLayoutManager(RequestsActivity.this));

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

                List<Request> requests = DbContext.INSTANCE.getRequestsClient(username, textViewWorker.getText().toString(), workTypes, requestStatuses,
                        valuesPrice[0], valuesPrice[1], dateFromVal, dateToVal);

                RequestAdapter adapter = new RequestAdapter(RequestsActivity.this, requests, new RequestAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(Request item) {
                        requestDetails(item.getId());
                    }
                }, false);

                recyclerView.setAdapter(adapter);
                saveValues(textViewWorker.getText().toString(), requestStatuses, workTypes, valuesPrice[0], valuesPrice[1], dateFromVal, dateToVal);
                dialog.dismiss();
            }
        });

        Button buttonClear = view.findViewById(R.id.buttonClearRequestsFilter);
        buttonClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RecyclerView recyclerView = findViewById(R.id.recyclerViewRequests);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(new LinearLayoutManager(RequestsActivity.this));

                RequestAdapter adapter = new RequestAdapter(RequestsActivity.this, DbContext.INSTANCE.getRequestsClient(username), new RequestAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(Request item) {
                        requestDetails(item.getId());
                    }
                }, false);

                recyclerView.setAdapter(adapter);
                clearValues();
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
        calendar.setTime(dateFromVal);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);
        textViewFrom.setText(String.format("%02d", day) + "." + String.format("%02d", month + 1) + "." + year + ".");

        calendar.setTime(dateToVal);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        month = calendar.get(Calendar.MONTH);
        year = calendar.get(Calendar.YEAR);
        textViewTo.setText(String.format("%02d", day) + "." + String.format("%02d", month + 1) + "." + year + ".");


        buttonFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(dateFromVal);
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);

                DatePickerDialog dpd = new DatePickerDialog(RequestsActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        textViewFrom.setText(String.format("%02d", dayOfMonth) + "." + String.format("%02d", month + 1) + "." + year + ".");

                        Calendar c = Calendar.getInstance();
                        c.set(year, month, dayOfMonth);
                        dateFromVal = c.getTime();
                    }
                }, year, month, day);
                dpd.show();
            }
        });

        buttonTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(dateToVal);
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);

                DatePickerDialog dpd = new DatePickerDialog(RequestsActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        textViewTo.setText(String.format("%02d", dayOfMonth) + "." + String.format("%02d", month + 1) + "." + year + ".");

                        Calendar c = Calendar.getInstance();
                        c.set(year, month, dayOfMonth);
                        dateToVal = c.getTime();
                    }
                }, year, month, day);
                dpd.show();
            }
        });
    }

    private void initSpinners(View view) {

        MultiSelectionSpinner statusSpinner = view.findViewById(R.id.spinnerStatusRequestsFilter);
        ArrayAdapter<RequestStatus> adapterRequestStatus = new ArrayAdapter<RequestStatus>(RequestsActivity.this, R.layout.spinner_item, RequestStatus.values());

        ArrayList<String> list = new ArrayList<>();
        for (int i = 0; i < adapterRequestStatus.getCount(); i++)
            list.add(adapterRequestStatus.getItem(i).toString());

        statusSpinner.setItems(list);

        list = new ArrayList<>();
        for (RequestStatus status: statusesVal) {
            list.add(status.name());
        }
        statusSpinner.setSelection(list);


        MultiSelectionSpinner typeSpinner = view.findViewById(R.id.spinnerTypeRequestsFilter);
        WorkerType[] types = WorkerType.values();

        list = new ArrayList<>();
        for (int i = 0; i < types.length; i++)
            list.add(types[i].getWorkType());

        typeSpinner.setItems(list);

        list = new ArrayList<>();
        for (WorkerType workerType: workerTypesVal) {
            list.add(workerType.getWorkType());
        }
        typeSpinner.setSelection(list);
    }

    private void saveValues(String workerName, List<RequestStatus> statuses, List<WorkerType> workTypes, int priceMin, int priceMax, Date dateFrom, Date dateTo) {
        dateFromVal = dateFrom;
        dateToVal = dateTo;
        priceVal[0] = priceMin;
        priceVal[1] = priceMax;
        workerTypesVal = workTypes;
        statusesVal = statuses;
        nameVal = workerName;
    }

    private void clearValues() {
        dateFromVal = Calendar.getInstance().getTime();
        dateToVal = Calendar.getInstance().getTime();
        nameVal = "";
        statusesVal = new ArrayList<>();
        workerTypesVal = new ArrayList<>();
        priceVal = new int[]{0, 20000};
    }
}
