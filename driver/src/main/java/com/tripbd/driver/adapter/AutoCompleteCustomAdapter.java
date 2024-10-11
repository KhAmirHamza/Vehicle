package com.tripbd.driver.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.tripbd.driver.R;

import java.util.ArrayList;
import java.util.List;


public class AutoCompleteCustomAdapter extends ArrayAdapter<String> {
    private List<String> dataListFull;

    public AutoCompleteCustomAdapter(@NonNull Context context, @NonNull List<String> dataListFull) {
        super(context, 0, dataListFull);
        this.dataListFull = new ArrayList<>(dataListFull);
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return dataFilter;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.simple_list_item, parent, false
            );
        }

        TextView txtv_title = convertView.findViewById(R.id.txtv_title);
        //ImageView imageViewFlag = convertView.findViewById(R.id.image_view_flag);

        String dataItem = getItem(position);

        if (dataItem != null) {
           txtv_title.setText(dataItem);
        }

        return convertView;
    }

    private Filter dataFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            List<String> suggestions = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                suggestions.addAll(dataListFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (String item : dataListFull) {
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