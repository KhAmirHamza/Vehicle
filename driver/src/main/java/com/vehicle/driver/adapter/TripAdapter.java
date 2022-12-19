package com.vehicle.driver.adapter;

import android.app.Activity;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;

import com.google.android.material.card.MaterialCardView;
import com.vehicle.driver.R;
import com.vehicle.driver.model.Trip;
import com.vehicle.driver.model.Vehicle;

import java.util.ArrayList;
import java.util.List;

public class TripAdapter extends RecyclerView.Adapter<TripAdapter.MyViewHolder> {

    Activity context;
    List<Trip> trips;
    private static final String TAG = "TripAdapter";
    RecyclerView recyclerView;
    public OnClickListener onClickListener;

    View view;

    public TripAdapter(Activity context) {
        this.context = context;
        this.trips = new ArrayList<>();


    }
    public void setData(List<Trip> trips, RecyclerView recyclerView ){

        this.trips = null;
        this.trips = trips;
        this.recyclerView = recyclerView;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TripAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.iv_trip, parent,
                        false));
    }

    @Override
    public void onBindViewHolder(@NonNull TripAdapter.MyViewHolder holder, int position) {
        Trip trip = trips.get(position);
        String details = getVehicleDetails(trip);
        holder.tv_car_details.setText(details==null?"...": details);
        holder.tv_loading_date_time.setText("( "+trip.getLoadingTime()+", "+trip.getLoadingDate()+" )");
        holder.txtv_trip_id.setText(""+trip.getId());
        long rMills = trip.getCreatedAtMills()+1000*30*60;
        long cMills = System.currentTimeMillis();

        if(cMills>rMills && trip.getStatus().equalsIgnoreCase("Bidding")){
            String status = trip.getStatus().isEmpty()?"":
                    cMills>rMills && trip.getStatus().equalsIgnoreCase("Bidding")?
                            "Time Expired": trip.getStatus();
            holder.tv_trip_status.setText(status);
        }else{
            setCountDownTimer(holder.tv_trip_status, rMills-cMills);
        }

        holder.tv_loading_upazila_district.setText("Area-->"+trip.getLoadingUpazilaThana());
        holder.tv_loading_full_address.setText("Full Address-->"+trip.getLoadingFullAddress() +"\nLandMark-->"+trip.getLoadingLandmark());
        holder.tv_unloading_upazila_district.setText("Area-->"+trip.getUnloadingUpazilaThana());
        holder.tv_unloading_full_address.setText("Full Address-->"+trip.getUnloadingFullAddress() +"\nLandMark-->"+trip.getUnloadingLandmark());
        holder.tv_description.setText(trip.getDescription().isEmpty()?"":trip.getDescription());
        holder.tv_up_down_trip.setVisibility(trip.getUpDownTrip()==1? View.VISIBLE:View.GONE);
        holder.tv_contain_animal.setVisibility(trip.getContainAnimal()==1? View.VISIBLE:View.GONE);
        holder.tv_fragile_product.setVisibility(trip.getFragile()==1? View.VISIBLE:View.GONE);
        holder.tv_perishable_product.setVisibility(trip.getPerishable()==1? View.VISIBLE:View.GONE);
        holder.tv_labor_needed.setVisibility(trip.getLaborNeeded()==1? View.VISIBLE:View.GONE);


        /*Driver driver = trip.getDriver();
        if (driver!=null) {
            String vehicleImageUrl = "";

            for (int i = 0; i < trip.getDriver().getCars().size(); i++) {
                if (trip.getSelectedCar().contains(trip.getDriver().getCars().get(i).getOpenOrCovered()) &&
                        trip.getSelectedCar().contains(trip.getDriver().getCars().get(i).getType()) &&
                        (trip.getSelectedCar().contains(trip.getDriver().getCars().get(i).getCapacity() + "")
                                || trip.getSelectedCar().contains(trip.getDriver().getCars().get(i).getSize() + ""))
                ) {
                    vehicleImageUrl = trip.getDriver().getCars().get(i).getImageUrl();
                }


            }
            String driverImageUrl = driver.getImageUrl();
            if (driverImageUrl != null) Picasso.get().load(driverImageUrl).into(imgv_driver);
            txtv_driver_email.setText(driver.getEmail());
            txtv_driver_name.setText(driver.getName());
            txtv_driver_phone_number.setText(driver.getPhoneNumber());
            Picasso.get().load(driverImageUrl).into(imgv_driver);
            Picasso.get().load(vehicleImageUrl).into(imgv_vehicle);*/
        //}
    }

    @Override
    public int getItemCount() {
        return trips.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        MaterialButton btn_start_bidding;
        LinearLayout lay_trip_details;
        MaterialCardView cv_trip_header;
        TextView tv_car_details,tv_loading_date_time,tv_loading_upazila_district,
                tv_loading_full_address,tv_unloading_upazila_district,
                tv_unloading_full_address,tv_description,tv_up_down_trip,
                tv_contain_animal,tv_fragile_product,tv_perishable_product,
                tv_labor_needed, txtv_trip_id, tv_trip_status;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;

            tv_car_details =  findViewById(R.id.tv_car_details);
            tv_loading_date_time =  findViewById(R.id.tv_loading_date_time);
            tv_loading_upazila_district =  findViewById(R.id.tv_loading_upazila_district);
            tv_loading_full_address =  findViewById(R.id.tv_loading_full_address);
            tv_unloading_upazila_district =  findViewById(R.id.tv_unloading_upazila_district);
            tv_unloading_full_address =  findViewById(R.id.tv_unloading_full_address);
            tv_description =  findViewById(R.id.tv_description);
            tv_up_down_trip =  findViewById(R.id.tv_up_down_trip);
            tv_contain_animal =  findViewById(R.id.tv_contain_animal);
            tv_fragile_product =  findViewById(R.id.tv_fragile_product);
            tv_perishable_product =  findViewById(R.id.tv_perishable_product);
            tv_labor_needed =  findViewById(R.id.tv_labor_needed);
            txtv_trip_id =  findViewById(R.id.txtv_trip_id);
            tv_trip_status =  findViewById(R.id.tv_trip_status);

            lay_trip_details =  view.findViewById(R.id.lay_trip_details);
            btn_start_bidding =  view.findViewById(R.id.btn_start_bidding);
            cv_trip_header =  view.findViewById(R.id.cv_trip_header);
            cv_trip_header.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    lay_trip_details.setVisibility(lay_trip_details.getVisibility()==View.VISIBLE?View.GONE: View.VISIBLE);
                }
            });

            btn_start_bidding.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //todo...



                }
            });






        }
    }
    String getVehicleDetails(Trip trip){
        if (trip.getVehicle()==null) return "...";
        else if (
                trip.getVehicle().getType().equalsIgnoreCase("Truck") ||
                trip.getVehicle().getType().equalsIgnoreCase("PickUp") ||
                trip.getVehicle().getType().equalsIgnoreCase("Trailer")
        ){
            return trip.getVehicle().getType()+", "
                    +trip.getVehicle().getSize()+" Feet "
                    +trip.getVehicle().getCapacity()+" Ton ("
                    +trip.getVehicle().getOpenOrCovered()+")";
        }else {
            return trip.getVehicle().getType()+", "
                    +trip.getVehicle().getSize()+" Seated ";
        }
    }
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
