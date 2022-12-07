package com.vehicle.customer.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.vehicle.customer.R;
import com.vehicle.customer.model.Trip;

import java.util.ArrayList;
import java.util.List;

public class BidAdapter extends RecyclerView.Adapter<BidAdapter.MyViewHolder> {

    Activity context;
    List<Trip.Bid> bids;
    private static final String TAG = "BidAdapter";
    RecyclerView recyclerView;
    public OnClickListener onClickListener;

    public BidAdapter(Activity context) {
        this.context = context;
        this.bids = new ArrayList<>();


    }
    public void setData(List<Trip.Bid> bids, RecyclerView recyclerView ){

        this.bids = null;
        this.bids = bids;
        this.recyclerView = recyclerView;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public BidAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.iv_bid, parent,
                        false));
    }

    @Override
    public void onBindViewHolder(@NonNull BidAdapter.MyViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 5;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        MaterialButton btn_confirm_bid;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            btn_confirm_bid = itemView.findViewById(R.id.btn_confirm_bid);
            btn_confirm_bid.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onClickListener.onClick(view,null, getAdapterPosition());
                }
            });
        }
    }



    public interface OnClickListener{
        void onClick(View view, @Nullable Trip.Bid bid, int position);
    }

    public void setOnClickListener(OnClickListener onClickListener){
        this.onClickListener = onClickListener;
    }

}
