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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workers);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
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

        RangeSeekBar rangeSeekBarRating = view.findViewById(R.id.rangeSeekBarRatingWorkerFilter);
        final float[] valuesRating = new float[]{0, 5};
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
        rangeSeekBarRating.setValue(rangeSeekBarRating.getMinProgress(), rangeSeekBarRating.getMaxProgress());

        RangeSeekBar rangeSeekBarExp = view.findViewById(R.id.rangeSeekBarExperienceWorkerFilter);
        final float[] valuesExp = new float[]{0, 20};
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
        rangeSeekBarExp.setValue(rangeSeekBarExp.getMinProgress(), rangeSeekBarExp.getMaxProgress());

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

                RecyclerView recyclerView = findViewById(R.id.recyclerView);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(new LinearLayoutManager(WorkersActivity.this));

                List<String> types = typeSpinner.getSelectedStrings();
                List<WorkerType> workerTypes = new ArrayList<>();
                for (String type : types) {
                    workerTypes.add(WorkerType.valueOf(type));
                }

                List<Worker> workers = DbContext.INSTANCE.getWorkers(textViewFirstName.getText().toString(), textViewLastName.getText().toString(),
                        workerTypes, valuesRating[0], valuesRating[1], valuesExp[0], valuesExp[1]);

                WorkerAdapter adapter = new WorkerAdapter(WorkersActivity.this, workers, new WorkerAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(Worker item) {
                        workerDetails(item.getUser().getUsername());
                    }
                });

                recyclerView.setAdapter(adapter);
                dialog.dismiss();
            }
        });

        Button buttonClear = view.findViewById(R.id.buttonClearWorkerFilter);
        buttonClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RecyclerView recyclerView = findViewById(R.id.recyclerView);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(new LinearLayoutManager(WorkersActivity.this));

                WorkerAdapter adapter = new WorkerAdapter(WorkersActivity.this, DbContext.INSTANCE.getWorkers(), new WorkerAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(Worker item) {
                        workerDetails(item.getUser().getUsername());
                    }
                });

                recyclerView.setAdapter(adapter);
                dialog.dismiss();
            }
        });
    }
}
