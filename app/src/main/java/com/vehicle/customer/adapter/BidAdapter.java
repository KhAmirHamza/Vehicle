package com.vehicle.customer.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.squareup.picasso.Picasso;
import com.vehicle.customer.R;
import com.vehicle.customer.model.Trip;
import com.vehicle.customer.model.Vehicle;

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
        Picasso.get().load(bids.get(position).getDriver().getImageUrl()).into(holder.imgv_driver);
        holder.tv_driver_name.setText(bids.get(position).getDriver().getName());
        holder.tv_price.setText(bids.get(position).getBidPrice()+" Tk");
        holder.tv_vehicle_size.setText(getVehicleDetails(bids.get(position).getVehicle()));
    }

    String getVehicleDetails(Vehicle vehicle){
        if (vehicle==null) return "...";
        else if (
                vehicle.getType().equalsIgnoreCase("Truck") ||
                        vehicle.getType().equalsIgnoreCase("Pickup") ||
                        vehicle.getType().equalsIgnoreCase("Trailer") ){

            return vehicle.getType()+", "
                    +vehicle.getSize()+" ("
                    +vehicle.getVariety()+")";
        }else {
            return vehicle.getType()+", "
                    +vehicle.getSeat()+" Seats "+" ("
                    +vehicle.getVariety()+")";
        }
    }


    @Override
    public int getItemCount() {
        return bids.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        MaterialButton btn_confirm_bid;
        ImageView imgv_driver;
        TextView tv_driver_name, tv_price, tv_vehicle_size;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            btn_confirm_bid = itemView.findViewById(R.id.btn_confirm_bid);
            tv_driver_name = itemView.findViewById(R.id.tv_driver_name);
            tv_price = itemView.findViewById(R.id.tv_price);
            imgv_driver = itemView.findViewById(R.id.imgv_driver);
            tv_vehicle_size = itemView.findViewById(R.id.tv_vehicle_size);
            btn_confirm_bid.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onClickListener.onClick(view, bids.get(getAdapterPosition()), getAdapterPosition());
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
