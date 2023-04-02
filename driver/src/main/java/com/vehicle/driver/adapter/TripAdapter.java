package com.vehicle.driver.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.DialogCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;

import com.google.android.material.card.MaterialCardView;
import com.vehicle.driver.Constants;
import com.vehicle.driver.R;
import com.vehicle.driver.model.Trip;
import com.vehicle.driver.view.BiddingDialog;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

import kotlin.collections.ArrayDeque;

public class TripAdapter extends RecyclerView.Adapter<TripAdapter.MyViewHolder> implements Filterable {

    Activity context;
    List<Trip> trips;
    private static final String TAG = "TripAdapter";
    RecyclerView recyclerView;
    public OnClickListener onClickListener;
    String driverPhoneNumber;
    View view;
    List<Trip> copyTrips;

    public TripAdapter(Activity context, String driverPhoneNumber) {
        this.context = context;
        this.driverPhoneNumber = driverPhoneNumber;
        this.trips = new ArrayList<>();
        //this.copyTrips = new ArrayList<>();
    }
    @SuppressLint("NotifyDataSetChanged")
    public void setData(List<Trip> trips, RecyclerView recyclerView ){

        this.trips.clear();
        //this.copyTrips.clear();
        this.trips = trips;
        this.copyTrips = new ArrayDeque<>(trips);
        this.recyclerView = recyclerView;

        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TripAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if(viewType == 0){
            return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.iv_trip_taken, parent,
                    false));
        }else{
            return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.iv_trip, parent,
                    false));
        }



    }
    boolean isBidPlaced( int position){
        boolean bidPlaced = false;
        if (trips.get(position).getBids()!=null && trips.get(position).getBids().size()>0){
            for (Trip.Bid bid : trips.get(position).getBids()) {
                if (bid.getDriver().getPhoneNumber().equalsIgnoreCase(driverPhoneNumber)) {
                    bidPlaced = true;
                    break;
                }
            }
            return  bidPlaced;
        }else{
            return false;
        }
    }



    @Override
    public int getItemViewType(int position) {
            if (isBidPlaced(position)){
                return 0;
            }else{
                return 1;
            }




      /*  if (trips.get(position).getDriver() != null &&
                trips.get(position).getDriver().getPhoneNumber()!=null &&
                trips.get(position).getDriver().getPhoneNumber().equalsIgnoreCase(driverPhoneNumber)){

        }*/

        //return super.getItemViewType(position);
    }

    @Override
    public void onBindViewHolder(@NonNull TripAdapter.MyViewHolder holder, int position) {
        Trip trip = trips.get(position);
        String details = getVehicleDetails(trip);
        holder.tv_car_details.setText(details==null?"...": details);
        holder.tv_loading_date_time.setText("( "+trip.getLoadingTime()+", "+trip.getLoadingDate()+" )");
        holder.txtv_trip_id.setText(""+trip.getId());
        long rMills = trip.getCreatedAtMills()+1000* Constants.BIDDING_TIME_MINUTES *60;
        long cMills = System.currentTimeMillis();

        if(cMills>rMills && trip.getStatus().equalsIgnoreCase("Bidding")){
            /*String status = trip.getStatus().isEmpty()?"":
                    cMills>rMills && trip.getStatus().equalsIgnoreCase("Bidding")?
                            "Time Expired": trip.getStatus();*/
            String status = trip.getStatus().isEmpty()?"" : "Time Expired";

            if (isBidPlaced(position)){
                holder.tv_trip_status.setText("Bid Confirmed");
                holder.btn_start_bidding.setVisibility(View.GONE);
                //holder.tv_trip_status.setText(""+bidPlaced);
                holder.tv_trip_remainingTime.setVisibility(View.GONE);
            }else{
                holder.tv_trip_status.setText(status);
                holder.tv_trip_remainingTime.setVisibility(View.GONE);
            }

        }else{
            if (isBidPlaced(position)){
                holder.tv_trip_status.setText("Bid Confirmed");
                holder.btn_start_bidding.setVisibility(View.GONE);
                holder.tv_trip_remainingTime.setVisibility(View.GONE);

                //holder.tv_trip_status.setText(""+bidPlaced);
            }else{
                holder.tv_trip_status.setText("Bid Continue - Bid Continue - Bid Continue -");
                setCountDownTimer(holder.tv_trip_remainingTime, rMills-cMills);
                holder.tv_trip_remainingTime.setVisibility(View.VISIBLE);
            }
        }
        holder.tv_trip_status.setSelected(true);


        holder.tv_loading_upazila_district.setText("Area: "+trip.getLoadingUpazilaThana());
        holder.tv_loading_full_address.setText("Full Address: "+trip.getLoadingFullAddress() +"\nNa/O Nearby School/ Mosque/ other: "+trip.getLoadingLandmark());
        holder.tv_unloading_upazila_district.setText("Area: "+trip.getUnloadingUpazilaThana());
        holder.tv_unloading_full_address.setText("Full Address: "+trip.getUnloadingFullAddress() +"\nNa/O Nearby School/ Mosque/ other: "+trip.getUnloadingLandmark());
        holder.tv_description.setText(trip.getDescription().isEmpty()?"":trip.getDescription());
        holder.tv_up_down_trip.setVisibility(trip.getUpDownTrip()==1? View.VISIBLE:View.GONE);
        holder.tv_contain_animal.setVisibility(trip.getContainAnimal()==1? View.VISIBLE:View.GONE);
        holder.tv_fragile_product.setVisibility(trip.getFragile()==1? View.VISIBLE:View.GONE);
        holder.tv_perishable_product.setVisibility(trip.getPerishable()==1? View.VISIBLE:View.GONE);
        holder.tv_labor_needed.setVisibility(trip.getLaborNeeded()==1? View.VISIBLE:View.GONE);
        holder.tv_lengthAlert.setVisibility(trip.getLengthAlert()==1? View.VISIBLE:View.GONE);
        holder.tv_weightAlert.setVisibility(trip.getWeightAlert()==1? View.VISIBLE:View.GONE);

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

    @Override
    public Filter getFilter() {
        return new Filter() {
            /* access modifiers changed from: protected */

            public Filter.FilterResults performFiltering(CharSequence constraint) {
                CharSequence loading = constraint.subSequence(0, (constraint.toString().indexOf('+'))).toString().toLowerCase(Locale.ROOT);
                CharSequence unloading = constraint.subSequence((constraint.toString().indexOf('+')+1), constraint.length());
                List<Trip> filterTripList = new ArrayList<>();
                if (constraint.equals("+")) {
                    filterTripList.addAll(copyTrips);
                } else {

                    //String filterPattern = constraint.toString().toLowerCase().trim();
                    for (Trip trip : copyTrips) {
                        if(
                                (loading.length()>1 &&
                                        trip.getLoadingUpazilaThana().toLowerCase(Locale.ROOT).contains(loading) ||
                                (unloading.length()>1 && trip.getUnloadingUpazilaThana().toLowerCase(Locale.ROOT).contains(unloading)))
                        ){
                            filterTripList.add(trip);
                            Toast.makeText(context, "loading: "+loading+"Unloading: "+unloading, Toast.LENGTH_SHORT).show();

                        }
                    }
                }
                Filter.FilterResults filterResults = new Filter.FilterResults();
                filterResults.values = filterTripList;
                return filterResults;
            }

            /* access modifiers changed from: protected */

            @SuppressLint("NotifyDataSetChanged")
            public void publishResults(CharSequence constraint, Filter.FilterResults results) {
                trips.clear();
                trips.addAll((Collection<? extends Trip>) results.values);
                notifyDataSetChanged();
            }
        };
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        MaterialButton btn_start_bidding;
        RelativeLayout lay_trip_details;
        MaterialCardView cv_trip_header;
        TextView tv_car_details,tv_loading_date_time,tv_loading_upazila_district,
                tv_loading_full_address,tv_unloading_upazila_district,
                tv_unloading_full_address,tv_description,tv_up_down_trip,
                tv_contain_animal,tv_fragile_product,tv_perishable_product,
                tv_labor_needed, txtv_trip_id, tv_trip_status, tv_trip_remainingTime,
                tv_weightAlert, tv_lengthAlert;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;

            tv_trip_remainingTime =  findViewById(R.id.tv_trip_remainingTime);
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
            tv_lengthAlert =  view.findViewById(R.id.tv_lengthAlert);
            tv_weightAlert =  view.findViewById(R.id.tv_weightAlert);
            cv_trip_header.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //lay_trip_details.setVisibility(lay_trip_details.getVisibility()==View.VISIBLE?View.GONE: View.VISIBLE);
                    tv_loading_full_address.setVisibility(tv_loading_full_address.getVisibility()==View.VISIBLE?View.GONE: View.VISIBLE);
                    tv_unloading_full_address.setVisibility(tv_unloading_full_address.getVisibility()==View.VISIBLE?View.GONE: View.VISIBLE);
                }
            });

            btn_start_bidding.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // todo...
                    // Dialog dialog = new Dialog.B
                    BiddingDialog biddingDialog = new BiddingDialog(context,
                            trips.get(getAdapterPosition()).getVehicle().getType(),
                            trips.get(getAdapterPosition()).getVehicle().getVariety(),
                            trips.get(getAdapterPosition()).getId()
                    );

                    biddingDialog.setCancelable(true);
                    biddingDialog.show();
                }
            });
        }
    }
    String getVehicleDetails(Trip trip){
        if (trip.getVehicle()==null) return "...";
        else if (
                trip.getVehicle().getType().equalsIgnoreCase("Truck") ||
                trip.getVehicle().getType().equalsIgnoreCase("PickUp") ||
                trip.getVehicle().getType().equalsIgnoreCase("Trailer")){

            return trip.getVehicle().getType()+", "+trip.getVehicle().getSize()+" ("
                    +trip.getVehicle().getVariety()+")";
        }else {
            return trip.getVehicle().getType()+", "
                    +trip.getVehicle().getSeat()+" Seated ("
                    +trip.getVehicle().getVariety()+")";
        }
    }
    public void setCountDownTimer(TextView textView, long remainingMills){
        new CountDownTimer(remainingMills, 1000) {

            public void onTick(long millisUntilFinished) {
                textView.setText("" + millisUntilFinished / 1000/60 +":"+ (millisUntilFinished /1000)%60);
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
