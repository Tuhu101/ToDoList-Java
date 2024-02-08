package com.example.todolist;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CustomArrayAdapter extends ArrayAdapter<String> {

    private final Context context;
    private final List<String> values;
    private List<Boolean> isCheckedList;

    public CustomArrayAdapter(Context context, List<String> values) {
        super(context, R.layout.list_item, values);
        this.context = context;
        this.values = values;

        // Initialize isCheckedList with default states or load from SharedPreferences
        isCheckedList = loadCheckboxStates();
    }

    public List<Boolean> getIsCheckedList() {
        return isCheckedList;
    }

    public void updateIsCheckedList(List<Boolean> newList) {
        isCheckedList.clear();
        isCheckedList.addAll(newList);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.checkBox = convertView.findViewById(R.id.checkBox);
            viewHolder.itemTextView = convertView.findViewById(R.id.textView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        String currentItem = getItem(position);

        if (currentItem != null) {
            viewHolder.itemTextView.setText(currentItem);
        }

       /* // Set the checkbox state based on your data model
        viewHolder.checkBox.setChecked(isCheckedList.get(position));*/

        // Ensure that isCheckedList is not empty before accessing it
        if (!isCheckedList.isEmpty() && position < isCheckedList.size()) {
            viewHolder.checkBox.setChecked(isCheckedList.get(position));
        }

        // Handle checkbox click events
        viewHolder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            // Update your data model accordingly
            if (!isCheckedList.isEmpty() && position < isCheckedList.size()) {
                isCheckedList.set(position, isChecked);
                // Save the updated state to SharedPreferences
                saveCheckboxStates(isCheckedList);
            }
        });

        return convertView;
    }





    private static class ViewHolder {
        CheckBox checkBox;
        TextView itemTextView;
    }

    private List<Boolean> loadCheckboxStates() {
        // Load checkbox states from SharedPreferences
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        List<Boolean> loadedStates = new ArrayList<>();

        for (int i = 0; i < values.size(); i++) {
            // Use a unique key for each checkbox state (e.g., "checkbox_state_0", "checkbox_state_1", ...)
            boolean isChecked = preferences.getBoolean("checkbox_state_" + i, false);
            loadedStates.add(isChecked);
        }

        return loadedStates;
    }

    private void saveCheckboxStates(List<Boolean> checkboxStates) {
        // Save checkbox states to SharedPreferences
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();

        for (int i = 0; i < checkboxStates.size(); i++) {
            // Use a unique key for each checkbox state (e.g., "checkbox_state_0", "checkbox_state_1", ...)
            editor.putBoolean("checkbox_state_" + i, checkboxStates.get(i));
        }

        editor.apply();
    }
}
