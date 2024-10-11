package com.tripbd.admin;

import static com.tripbd.admin.Constants.MAIN_URL;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.JsonObject;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import kotlin.collections.ArrayDeque;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DriverAdapter extends RecyclerView.Adapter<DriverAdapter.MyViewHolder> implements Filterable {

    Activity context;
    List<Driver> drivers;
    private static final String TAG = "DriverAdapter";
    RecyclerView recyclerView;
    public OnClickListener onClickListener;
    View view;
    List<Driver> copyDrivers;

    public DriverAdapter(Activity context) {
        this.context = context;
        this.drivers = new ArrayList<>();
        //this.copyTrips = new ArrayList<>();
    }
    @SuppressLint("NotifyDataSetChanged")
    public void setData(List<Driver> drivers, RecyclerView recyclerView ){

        this.drivers.clear();
        //this.copyTrips.clear();
        this.drivers = drivers;
        this.copyDrivers = new ArrayDeque<>(drivers);
        this.recyclerView = recyclerView;

        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.iv_driver, parent,
                    false));
    }


    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Driver driver = drivers.get(position);

        Picasso.get().load(driver.getImageUrl()).into(holder.imgvDriver);
        holder.txtv_driver_name.setText(""+driver.getName());
        holder.txtv_driver_phone_number.setText(""+driver.getPhoneNumber());
        holder.txtv_driver_due.setText("Due: "+driver.getDue()+" Tk");




    }

    @Override
    public int getItemCount() {
        return drivers.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            /* access modifiers changed from: protected */

            public FilterResults performFiltering(CharSequence constraint) {
//                CharSequence loading = constraint.subSequence(0, (constraint.toString().indexOf('+'))).toString().toLowerCase(Locale.ROOT);
//                CharSequence unloading = constraint.subSequence((constraint.toString().indexOf('+')+1), constraint.length());
//                List<Trip> filterTripList = new ArrayList<>();
//                if (constraint.equals("+")) {
//                    filterTripList.addAll(copyTrips);
//                } else {
//
//                    //String filterPattern = constraint.toString().toLowerCase().trim();
//                    for (Trip trip : copyTrips) {
//                        if(
//                                (loading.length()>1 &&
//                                        trip.getLoadingUpazilaThana().toLowerCase(Locale.ROOT).contains(loading) ||
//                                (unloading.length()>1 && trip.getUnloadingUpazilaThana().toLowerCase(Locale.ROOT).contains(unloading)))
//                        ){
//                            filterTripList.add(trip);
//                            Toast.makeText(context, "loading: "+loading+"Unloading: "+unloading, Toast.LENGTH_SHORT).show();
//
//                        }
//                    }
//                }
                FilterResults filterResults = new FilterResults();
                //filterResults.values = filterTripList;
                return filterResults;
            }

            /* access modifiers changed from: protected */

            @SuppressLint("NotifyDataSetChanged")
            public void publishResults(CharSequence constraint, FilterResults results) {
                drivers.clear();
                if(results.values!=null){
                    drivers.addAll((Collection<? extends Driver>) results.values);
                }
                //trips.addAll((Collection<? extends Trip>) results.values);
                notifyDataSetChanged();
            }
        };
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        CircleImageView imgvDriver;
        TextView txtv_driver_name, txtv_driver_phone_number, txtv_driver_due;
        MaterialButton btn_due_alert;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;

            imgvDriver =  view.findViewById(R.id.imgvDriver);
            txtv_driver_name =  findViewById(R.id.txtv_driver_name);
            txtv_driver_phone_number =  findViewById(R.id.txtv_driver_phone_number);
            txtv_driver_due =  findViewById(R.id.txtv_driver_due);

            btn_due_alert =  view.findViewById(R.id.btn_due_alert);
            btn_due_alert.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Toast.makeText(context, "Send Notification Clicked!", Toast.LENGTH_SHORT).show();
                    sendDueAlertNotificationToDriver(
                            drivers.get(getAdapterPosition()).getFcmToken(),
                            drivers.get(getAdapterPosition()).getDue(),
                            drivers.get(getAdapterPosition()).getName()
                    );
                 /*   AlertDialog.Builder builder = new AlertDialog.Builder(context);
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

                    }else{
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
                    }
                    builder.show();
                    */
                }
            });
        }
    }

    void sendDueAlertNotificationToDriver(String token, int due, String driverName){
        List<String> tokens = new ArrayList<>();
        tokens.add(token);
        FCM fcm = new FCM(driverName+", Pay the dues!",
                "আপনার "+due+" টাকা বকেয়া পরিশোধ করুন!", tokens);
        //Toast.makeText(activity, "Tokens: "+tokens.size(), Toast.LENGTH_SHORT).show();
        ApiClient.getInstance(MAIN_URL).sendNotification(fcm).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                //if (response.isSuccessful()) {
                //if (response.body() == null) {
                //    Log.d(TAG, "onResponse: Order Data is null");
                //    return;
                //}
                Log.d(TAG, "onResponse: Successful");
                Toast.makeText(context, "Notification send successfully!", Toast.LENGTH_SHORT).show();

                // }else {
                //Toast.makeText(OrderActivity.this, "ResponseError: " + response.errorBody().toString(), Toast.LENGTH_SHORT).show();
                //     Log.d(TAG, "onResponse: ResponseError: " + response.message());
                // }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                // Toast.makeText(OrderActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d(TAG, "onFailure: " + t.getLocalizedMessage());
                Log.d(TAG, "onFailure: " + t.getMessage());
                //Log.d(OrderActivity.TAG, "onFailure: " + t.getCause().toString());
            }
        });
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
