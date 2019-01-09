package com.dv183222m.pki;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.dv183222m.pki.com.dv183222m.pki.data.Worker;
import com.dv183222m.pki.com.dv183222m.pki.data.WorkerType;

import java.util.List;

public class WorkerAdapter extends RecyclerView.Adapter<WorkerAdapter.WorkerViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(Worker item);
    }

    private Context mCtx;
    private List<Worker> workerList;
    private final OnItemClickListener listener;

    public WorkerAdapter(Context mCtx, List<Worker> workerList, OnItemClickListener listener) {
        this.mCtx = mCtx;
        this.workerList = workerList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public WorkerViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.worker_item, null, false);
        view.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT));
        return new WorkerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WorkerViewHolder workerViewHolder, int i) {
        Worker worker = workerList.get(i);

        workerViewHolder.textViewName.setText(worker.getUser().getFullName());
        workerViewHolder.textViewExp.setText(worker.getExperience() + " years of experience");

        StringBuilder types = new StringBuilder();
        for (WorkerType type: worker.getTypes()) {
            if(types.toString().isEmpty() == false) {
                types.append(' ');
            }
            types.append(type.name());
        }
        workerViewHolder.textViewTypes.setText(types.toString());

        workerViewHolder.ratingBar.setRating(worker.getRating());

        if(worker.getUser().getImage() != 0) {
            workerViewHolder.imageView.setImageDrawable(mCtx.getResources().getDrawable(worker.getUser().getImage(), null));
        }

        workerViewHolder.bind(workerList.get(i), listener);
    }

    @Override
    public int getItemCount() {
        return workerList.size();
    }

    class WorkerViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView textViewName, textViewExp, textViewTypes;
        RatingBar ratingBar;

        public WorkerViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.imageView);
            textViewName = itemView.findViewById(R.id.textViewName);
            textViewExp = itemView.findViewById(R.id.textViewExp);
            textViewTypes = itemView.findViewById(R.id.textViewTypes);
            ratingBar = itemView.findViewById(R.id.ratingBar);
        }

        public void bind(final Worker item, final OnItemClickListener listener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    listener.onItemClick(item);
                }
            });
        }
    }
}
