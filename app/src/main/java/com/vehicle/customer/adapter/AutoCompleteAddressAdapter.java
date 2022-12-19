package com.vehicle.customer.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.vehicle.customer.R;

import java.util.ArrayList;
import java.util.List;


public class AutoCompleteAddressAdapter extends ArrayAdapter<String> {
    private List<String> addressListFull;

    public AutoCompleteAddressAdapter(@NonNull Context context, @NonNull List<String> addressList) {
        super(context, 0, addressList);
        addressListFull = new ArrayList<>(addressList);
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return addressFilter;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item_simple, parent, false
            );
        }

        TextView txtv_address = convertView.findViewById(R.id.txtv_address);
        //ImageView imageViewFlag = convertView.findViewById(R.id.image_view_flag);

        String countryItem = getItem(position);

        if (countryItem != null) {
           txtv_address.setText(countryItem);
        }

        return convertView;
    }

    private Filter addressFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            List<String> suggestions = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                suggestions.addAll(addressListFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (String item : addressListFull) {
                    if (item.toLowerCase().contains(filterPattern)) {
                        suggestions.add(item);
                    }
                }
            }

            results.values = suggestions;
            results.count = suggestions.size();

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            clear();
            addAll((List) results.values);
            notifyDataSetChanged();
        }

        @Override
        public CharSequence convertResultToString(Object resultValue) {
            return ((String) resultValue);
        }
    };
}