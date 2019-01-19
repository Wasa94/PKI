package com.dv183222m.pki;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dv183222m.pki.com.dv183222m.pki.data.Request;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

public class RequestAdapter extends RecyclerView.Adapter<RequestAdapter.RequestViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(Request item);
    }

    private Context mCtx;
    private List<Request> requestsList;
    private final RequestAdapter.OnItemClickListener listener;
    private boolean isWorker;

    public RequestAdapter(Context mCtx, List<Request> requestsList, RequestAdapter.OnItemClickListener listener, boolean isWorker) {
        this.mCtx = mCtx;
        this.requestsList = requestsList;
        this.listener = listener;
        this.isWorker = isWorker;
    }

    @NonNull
    @Override
    public RequestAdapter.RequestViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.request_item, null, false);
        view.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT));
        return new RequestAdapter.RequestViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RequestAdapter.RequestViewHolder requestViewHolder, int i) {
        Request request = requestsList.get(i);

        requestViewHolder.textViewType.setText(request.getType());
        if(isWorker) {
            requestViewHolder.textViewWorker.setText(request.getClient().getFullName());
        } else {
            requestViewHolder.textViewWorker.setText(request.getWorker().getFullName());
        }
        DateFormat df = new SimpleDateFormat("dd.MM.yyyy.");
        requestViewHolder.textViewFrom.setText(df.format(request.getFrom()));
        requestViewHolder.textViewTo.setText(df.format(request.getTo()));
        requestViewHolder.textViewStatus.setText(request.getStatus().name());

        requestViewHolder.bind(requestsList.get(i), listener);
    }

    @Override
    public int getItemCount() {
        return requestsList.size();
    }

    class RequestViewHolder extends RecyclerView.ViewHolder {

        TextView textViewWorker, textViewFrom, textViewTo, textViewStatus, textViewType;

        public RequestViewHolder(@NonNull View itemView) {
            super(itemView);

            textViewWorker = itemView.findViewById(R.id.textViewWorkerNameRequest);
            textViewFrom = itemView.findViewById(R.id.textViewFromRequest);
            textViewTo = itemView.findViewById(R.id.textViewToRequest);
            textViewStatus = itemView.findViewById(R.id.textViewStatusRequest);
            textViewType = itemView.findViewById(R.id.textViewTypeRequest);
        }

        public void bind(final Request item, final RequestAdapter.OnItemClickListener listener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    listener.onItemClick(item);
                }
            });
        }
    }
}