package com.tripbd.admin;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.firestore.FirebaseFirestore;

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

    public TripAdapter(Activity context) {
        this.context = context;
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
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.iv_trip, parent,
                    false));
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
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Trip trip = trips.get(position);

        holder.txtv_loading_trip_address.setText(trip.getLoadingUpazilaThana());
        holder.txtv_unloading_trip_address.setText(trip.getUnloadingUpazilaThana());

        holder.txtv_trip_id.setText(trip.getId());

        Customer customer = trip.getCustomer();
        holder.txtv_user_name.setText(""+customer.getName());
        holder.txtv_user_phone_number.setText(""+customer.getPhoneNumber());
        Driver driver  = trip.getDriver();
        if(driver!=null){
            holder.txtv_driver_name.setText(""+driver.getName());
            holder.txtv_driver_phone_number.setText(""+driver.getPhoneNumber());
        }



    }

    @Override
    public int getItemCount() {
        return trips.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            /* access modifiers changed from: protected */

            public FilterResults performFiltering(CharSequence constraint) {
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
                FilterResults filterResults = new FilterResults();
                filterResults.values = filterTripList;
                return filterResults;
            }

            /* access modifiers changed from: protected */

            @SuppressLint("NotifyDataSetChanged")
            public void publishResults(CharSequence constraint, FilterResults results) {
                trips.clear();
                if(results.values!=null){
                    trips.addAll((Collection<? extends Trip>) results.values);
                }
                //trips.addAll((Collection<? extends Trip>) results.values);
                notifyDataSetChanged();
            }
        };
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView txtv_trip_id, txtv_driver_name, txtv_driver_phone_number, txtv_user_name,
                txtv_user_phone_number, txtv_loading_trip_address, txtv_unloading_trip_address;
        MaterialButton btn_delete_trip;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;

            txtv_trip_id =  findViewById(R.id.txtv_trip_id);
            txtv_driver_name =  findViewById(R.id.txtv_driver_name);
            txtv_driver_phone_number =  findViewById(R.id.txtv_driver_phone_number);
            txtv_user_name =  findViewById(R.id.txtv_user_name);
            txtv_user_phone_number =  findViewById(R.id.txtv_user_phone_number);
            txtv_loading_trip_address =  findViewById(R.id.txtv_loading_trip_address);
            txtv_unloading_trip_address =  findViewById(R.id.txtv_unloading_trip_address);

            btn_delete_trip =  view.findViewById(R.id.btn_delete_trip);
            btn_delete_trip.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("Do you want to delete this trip?");
                    builder.setCancelable(true);

                    if (trips.get(getAdapterPosition()).getStatus().equalsIgnoreCase("Trip Confirmed")||
                            trips.get(getAdapterPosition()).getStatus().equalsIgnoreCase("Deleted")||
                            trips.get(getAdapterPosition()).getStatus().equalsIgnoreCase("Placed")||
                            trips.get(getAdapterPosition()).getStatus().equalsIgnoreCase("Cancelled")||
                            trips.get(getAdapterPosition()).getStatus().equalsIgnoreCase("Confirm")||
                            trips.get(getAdapterPosition()).getStatus().equalsIgnoreCase("Delete")||
                            trips.get(getAdapterPosition()).getStatus().equalsIgnoreCase("Running")||
                            trips.get(getAdapterPosition()).getStatus().equalsIgnoreCase("Cancel")){

                        builder.setMessage("NB. Your trip is now Active!");

                    }
                        builder.setPositiveButton("Click to Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //todo...

                                FirebaseFirestore db = FirebaseFirestore.getInstance();
                                db.collection("trip").document(trips.get(getAdapterPosition()).getId()).delete()
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                //Toast.makeText(context, "Trip successfully  deleted", Toast.LENGTH_SHORT).show();
                                                //trips.remove(getAdapterPosition());
                                                //notifyItemRemoved(getAdapterPosition());
                                                //notifyItemRangeChanged(getAdapterPosition(), trips.size());
                                                Toast.makeText(context, "Trip successfully  deleted", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            }
                        });

                    builder.show();
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
