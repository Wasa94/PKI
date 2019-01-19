package com.dv183222m.pki;

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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.dv183222m.pki.com.dv183222m.pki.data.DbContext;
import com.dv183222m.pki.com.dv183222m.pki.data.Worker;
import com.dv183222m.pki.com.dv183222m.pki.data.WorkerType;
import com.jaygoo.widget.OnRangeChangedListener;
import com.jaygoo.widget.RangeSeekBar;

import java.util.ArrayList;
import java.util.List;

public class WorkersActivity extends AppCompatActivity {

    private String user = "";

    private Boolean requestedBeforeVal = null;
    private String firstNameVal = "";
    private String lastNameVal = "";
    private float[] ratingVal = new float[]{0, 5};
    private float[] expVal = new float[]{0, 20};
    private List<WorkerType> workerTypesVal = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workers);

        RecyclerView recyclerView = findViewById(R.id.recyclerViewWorkers);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        WorkerAdapter adapter = new WorkerAdapter(this, DbContext.INSTANCE.getWorkers(), new WorkerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Worker item) {
                workerDetails(item.getUser().getUsername());
            }
        });

        recyclerView.setAdapter(adapter);

        getSupportActionBar().setTitle("WORKERS");

        user = getIntent().getStringExtra("Username");

    }

    private void initTypes(View view) {
        MultiSelectionSpinner spinner = view.findViewById(R.id.typesSpinnerWorkerFilter);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.worker_types_array, R.layout.spinner_item);

        ArrayList<String> list = new ArrayList<String>();
        for (int i = 0; i < adapter.getCount(); i++)
            list.add(adapter.getItem(i).toString());

        spinner.setItems(list);

        list = new ArrayList<>();
        for (WorkerType type : workerTypesVal) {
            list.add(type.name());
        }

        spinner.setSelection(list);
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

    public void workerDetails(String username) {
        Intent intent = new Intent(this, WorkerDetailsActivity.class);
        intent.putExtra("Worker", username);
        intent.putExtra("User", user);
        startActivity(intent);
    }

    private void showFilter() {
        AlertDialog.Builder builder = new AlertDialog.Builder(WorkersActivity.this);
        View view = getLayoutInflater().inflate(R.layout.dialog_filter, null);

        initTypes(view);
        initValues(view);

        final RadioGroup radioGroup = view.findViewById(R.id.radioGroupWorkersFilter);
        if (user != null && !user.isEmpty()) {
            radioGroup.setVisibility(View.VISIBLE);
        }

        RangeSeekBar rangeSeekBarRating = view.findViewById(R.id.rangeSeekBarRatingWorkerFilter);
        final float[] valuesRating = new float[]{ratingVal[0], ratingVal[1]};
        rangeSeekBarRating.setOnRangeChangedListener(new OnRangeChangedListener() {
            @Override
            public void onRangeChanged(RangeSeekBar view, float leftValue, float rightValue, boolean isFromUser) {
                valuesRating[0] = leftValue;
                valuesRating[1] = rightValue;
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
        rangeSeekBarRating.setValue(ratingVal[0], ratingVal[1]);

        RangeSeekBar rangeSeekBarExp = view.findViewById(R.id.rangeSeekBarExperienceWorkerFilter);
        final float[] valuesExp = new float[]{expVal[0], expVal[1]};
        rangeSeekBarExp.setOnRangeChangedListener(new OnRangeChangedListener() {
            @Override
            public void onRangeChanged(RangeSeekBar view, float leftValue, float rightValue, boolean isFromUser) {
                valuesExp[0] = leftValue;
                valuesExp[1] = rightValue;
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
        rangeSeekBarExp.setValue(expVal[0], expVal[1]);

        final TextView textViewFirstName = view.findViewById(R.id.textViewFirstNameWorkerFilter);
        final TextView textViewLastName = view.findViewById(R.id.textViewLastNameWorkerFilter);
        final MultiSelectionSpinner typeSpinner = view.findViewById(R.id.typesSpinnerWorkerFilter);

        builder.setView(view);
        final AlertDialog dialog = builder.create();
        dialog.show();
        Button buttonFilter = view.findViewById(R.id.buttonFilterWorkerFilter);
        buttonFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                RecyclerView recyclerView = findViewById(R.id.recyclerViewWorkers);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(new LinearLayoutManager(WorkersActivity.this));

                List<String> types = typeSpinner.getSelectedStrings();
                List<WorkerType> workerTypes = new ArrayList<>();
                for (String type : types) {
                    workerTypes.add(WorkerType.valueOf(type));
                }

                List<Worker> workers;

                if (user != null && user.isEmpty()) {
                    workers = DbContext.INSTANCE.getWorkers(textViewFirstName.getText().toString(), textViewLastName.getText().toString(),
                            workerTypes, valuesRating[0], valuesRating[1], valuesExp[0], valuesExp[1]);
                } else {
                    workers = DbContext.INSTANCE.getWorkersLogedin(textViewFirstName.getText().toString(), textViewLastName.getText().toString(),
                            workerTypes, valuesRating[0], valuesRating[1], valuesExp[0], valuesExp[1], requestedBeforeVal, user);
                }

                WorkerAdapter adapter = new WorkerAdapter(WorkersActivity.this, workers, new WorkerAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(Worker item) {
                        workerDetails(item.getUser().getUsername());
                    }
                });

                recyclerView.setAdapter(adapter);
                saveValues(textViewFirstName.getText().toString(), textViewLastName.getText().toString(),
                        workerTypes, valuesRating[0], valuesRating[1], valuesExp[0], valuesExp[1], requestedBeforeVal);
                dialog.dismiss();
            }
        });

        Button buttonClear = view.findViewById(R.id.buttonClearWorkerFilter);
        buttonClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RecyclerView recyclerView = findViewById(R.id.recyclerViewWorkers);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(new LinearLayoutManager(WorkersActivity.this));

                WorkerAdapter adapter = new WorkerAdapter(WorkersActivity.this, DbContext.INSTANCE.getWorkers(), new WorkerAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(Worker item) {
                        workerDetails(item.getUser().getUsername());
                    }
                });

                recyclerView.setAdapter(adapter);
                clearValues();
                dialog.dismiss();
            }
        });
    }

    private void initValues(View view) {
        TextView textViewFirstName = view.findViewById(R.id.textViewFirstNameWorkerFilter);
        TextView textViewLastName = view.findViewById(R.id.textViewLastNameWorkerFilter);
        RadioButton radioButtonAll = view.findViewById(R.id.radioButtonAllWorkersFilter);
        RadioButton radioButtonPositive = view.findViewById(R.id.radioButtonPositiveWorkersFilter);
        RadioButton radioButtonNegative = view.findViewById(R.id.radioButtonNegativeWorkersFilter);

        textViewFirstName.setText(firstNameVal);
        textViewLastName.setText(lastNameVal);
        if(requestedBeforeVal == null) {
            radioButtonAll.setChecked(true);
        } else if(requestedBeforeVal == true) {
            radioButtonPositive.setChecked(true);
        } else if(requestedBeforeVal == false) {
            radioButtonNegative.setChecked(true);
        }
    }

    private void saveValues(String firstName, String lastName, List<WorkerType> workerTypes, float ratingMin, float ratingMax, float expMin, float expMax, Boolean requestedBefore) {
        requestedBeforeVal = requestedBefore;
        firstNameVal = firstName;
        lastNameVal = lastName;
        ratingVal[0] = ratingMin;
        ratingVal[1] = ratingMax;
        expVal[0] = expMin;
        expVal[1] = expMax;
        workerTypesVal = workerTypes;
    }

    private void clearValues() {
        requestedBeforeVal = null;
        firstNameVal = "";
        lastNameVal = "";
        ratingVal = new float[]{0, 5};
        expVal = new float[]{0, 20};
        workerTypesVal = new ArrayList<>();
    }

    public void onRadioButtonClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();

        switch(view.getId()) {
            case R.id.radioButtonAllWorkersFilter:
                if (checked) {
                    requestedBeforeVal = null;
                }
                    break;
            case R.id.radioButtonNegativeWorkersFilter:
                if (checked) {
                    requestedBeforeVal = false;
                }
                    break;
            case R.id.radioButtonPositiveWorkersFilter:
                if (checked) {
                    requestedBeforeVal = true;
                }
                    break;
        }
    }
}
