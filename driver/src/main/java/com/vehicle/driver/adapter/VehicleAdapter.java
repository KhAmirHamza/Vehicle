package com.vehicle.driver.adapter;

import android.app.Activity;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.vehicle.driver.R;
import com.vehicle.driver.model.Trip;
import com.vehicle.driver.model.Vehicle;

import java.util.ArrayList;
import java.util.List;

public class VehicleAdapter extends RecyclerView.Adapter<VehicleAdapter.MyViewHolder> {

    Activity context;
    List<Vehicle> vehicles;
    private static final String TAG = "VehicleAdapter";
    RecyclerView recyclerView;
    public OnClickListener onClickListener;

    View view;

    public VehicleAdapter(Activity context) {
        this.context = context;
        this.vehicles = new ArrayList<>();
    }
    public void setData(List<Vehicle> vehicles, RecyclerView recyclerView ){

        this.vehicles = null;
        this.vehicles = vehicles;
        this.recyclerView = recyclerView;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public VehicleAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.iv_vehicle, parent,
                        false));
    }

    @Override
    public void onBindViewHolder(@NonNull VehicleAdapter.MyViewHolder holder, int position) {
        Vehicle vehicle = vehicles.get(position);
        if (vehicle!=null){
            if (vehicle.getType().equalsIgnoreCase("Truck")||
                    vehicle.getType().equalsIgnoreCase("Pickup")||
                    vehicle.getType().equalsIgnoreCase("Trailer")){

                holder.txtv_vehicle_model.setText(vehicle.getModel());
                holder.txtv_vehicle_number.setText(vehicle.getMetro()+"-"+vehicle.getSerial()+"-"+vehicle.getNumber());
                holder.txtv_vehicle_year.setText("Year: "+vehicle.getYear());
                holder.txtv_vehicle_description.setText(vehicle.getSize()+" ("+vehicle.getVariety()+")");
                if (vehicle.getVehicleImageUrl() != null) Picasso.get().load(vehicle.getVehicleImageUrl()).into(holder.imgv_car);

            }else {
                holder.txtv_vehicle_model.setText(vehicle.getModel());
                holder.txtv_vehicle_number.setText(vehicle.getMetro()+"-"+vehicle.getSerial()+"-"+vehicle.getNumber());
                holder.txtv_vehicle_year.setText(", Year: "+vehicle.getYear());
                holder.txtv_vehicle_description.setText(vehicle.getSeat()+" Seated"+" ("+vehicle.getVariety()+")");
                Toast.makeText(context, "Sit: "+vehicle.getSeat(), Toast.LENGTH_SHORT).show();
                if (vehicle.getVehicleImageUrl() != "") Picasso.get().load(vehicle.getVehicleImageUrl()).into(holder.imgv_car);

            }
            }
    }

    @Override
    public int getItemCount() {
        return vehicles.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView imgv_car;
        TextView txtv_vehicle_model,txtv_vehicle_number,txtv_vehicle_year,txtv_vehicle_description;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;

            imgv_car =  itemView.findViewById(R.id.imgv_car);
            txtv_vehicle_model =  findViewById(R.id.txtv_vehicle_model);
            txtv_vehicle_number =  findViewById(R.id.txtv_vehicle_number);
            txtv_vehicle_year =  findViewById(R.id.txtv_vehicle_year);
            txtv_vehicle_description =  findViewById(R.id.txtv_vehicle_description);

        }
    }
/*    String getVehicleDetails(Trip trip){
        if (trip.getVehicle()==null) return "...";
        else if (
                trip.getVehicle().getType().equalsIgnoreCase("Truck") ||
                trip.getVehicle().getType().equalsIgnoreCase("PickUp") ||
                trip.getVehicle().getType().equalsIgnoreCase("Trailer")
        ){
            return trip.getVehicle().getType()+", "+trip.getVehicle().getLength()+" Feet "
                    +trip.getVehicle().getCapacity()+" Ton ("
                    +trip.getVehicle().getOpenOrCovered()+")";
        }else {
            return trip.getVehicle().getType()+", "
                    +trip.getVehicle().getSeat()+" Seated ("
                    +trip.getVehicle().getOpenOrCovered()+")";
        }
    }*/
    public void setCountDownTimer(TextView textView, long remainingMills){
        new CountDownTimer(remainingMills, 1000) {

            public void onTick(long millisUntilFinished) {
                textView.setText("Bidding: " + millisUntilFinished / 1000/60 +":"+ (millisUntilFinished /1000)%60);
                // logic to set the EditText could go here
            }

            public void onFinish() {
                textView.setText("Time Expired");
            }

        }.start();
    }

    private TextView findViewById(int id) {
        return view.findViewById(id);
    }

    public interface OnClickListener{
        void onClick(View view, @Nullable Trip.Bid bid, int position);
    }

    public void setOnClickListener(OnClickListener onClickListener){
        this.onClickListener = onClickListener;
    }

}
