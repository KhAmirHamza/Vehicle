package com.tripbd.customer.view;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;

import com.tripbd.customer.R;
import com.tripbd.customer.adapter.BidAdapter;
import com.tripbd.customer.model.Trip;

public class BiddingActivity extends AppCompatActivity {
    RecyclerView recy_bid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bidding);

        recy_bid = findViewById(R.id.recy_bid);
        recy_bid.setHasFixedSize(true);
        recy_bid.setLayoutManager(new LinearLayoutManager(this));
        BidAdapter bidAdapter = new BidAdapter(BiddingActivity.this);
        recy_bid.setAdapter(bidAdapter);

        bidAdapter.setOnClickListener(new BidAdapter.OnClickListener() {
            @Override
            public void onClick(View view, @Nullable Trip.Bid bid, int position) {
                AlertDialog.Builder builder = new AlertDialog.Builder(BiddingActivity.this);
                builder.setMessage("Do you want to confirm this Driver for your Trip?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Todo... Update Trip with Driver Details



                            }
                        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        }).setCancelable(true).show();
            }
        });
    }
}