package com.example.todolist;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import android.util.Log;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import android.widget.Toast;

// This part is for the SharedPrefrense. \\
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

// TODO Liste                               "Done"
// TODO Oprette nye og slette og redigere   "Done"
// TODO Gemmes på enheden                   "Done"
// TODO Checkbox (Done)                     "Done"
// TODO Tid oprettet
// TODO Tid planlagt
// TODO Type
// TODO Beskrivelse
// TODO Icon
// TODO Notification
// TODO Alarm med lyd

public class MainActivity extends AppCompatActivity {

    // For sharedprefrences. \\
    private static final String KEY_ITEMS = "key_items";

    private ListView listView;
    private List<String> itemList;
    private ArrayAdapter<String> adapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.listView);
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        Button addButton = findViewById(R.id.addButton);
        Button editButton = findViewById(R.id.editButton);
        Button deleteButton = findViewById(R.id.deleteButton);

        // Load items from SharedPreferences
        itemList = loadItems();

        // Log the loaded items
        Log.d("MainActivity", "Items on create: " + itemList.toString());

        // Initialize the list with default items only if SharedPreferences is empty
        if (itemList.isEmpty()) {
            itemList = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.my_list)));
        }

        // Set up the adapter
        adapter = new CustomArrayAdapter(this, itemList);
        listView.setAdapter(adapter);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddDialog();
            }
        });

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedPosition = listView.getCheckedItemPosition();
                if (selectedPosition != ListView.INVALID_POSITION) {
                    showEditDialog(selectedPosition);
                } else {
                    // If no item is selected, you can handle it accordingly
                    // For example, show a Toast message
                    Toast.makeText(MainActivity.this, "Please select an item to edit",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Add an onItemClick listener to the listView
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                listView.setItemChecked(position, true);
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedPosition = listView.getCheckedItemPosition();
                if (selectedPosition != ListView.INVALID_POSITION) {
                    showDeleteDialog(selectedPosition);
                }
            }
        });
    }

    /*
        Methods for shared prefrensces.
    */
    private void saveItems(List<String> itemList) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putStringSet(KEY_ITEMS, new HashSet<>(itemList));
        editor.apply();
        Log.d("SaveItems", "Items saved: " + itemList.toString());
    }

    private List<String> loadItems() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        Set<String> itemSet = preferences.getStringSet(KEY_ITEMS, new HashSet<>());
        List<String> loadedItems = new ArrayList<>(itemSet);
        Log.d("LoadItems", "Items loaded: " + loadedItems.toString());
        return loadedItems;
    }

    private void addItem(String newItem) {
        itemList.add(newItem);
        adapter.notifyDataSetChanged();
        saveItems(itemList);
    }

    private void editItem(int position, String editedItem) {
        itemList.set(position, editedItem);
        adapter.notifyDataSetChanged();
        saveItems(itemList);
    }

    private void deleteItem(int position) {
        itemList.remove(position);
        adapter.notifyDataSetChanged();
        saveItems(itemList);
    }

    private void showAddDialog() {
        final EditText editText = new EditText(this);
        new AlertDialog.Builder(this)
                .setTitle("Add Item")
                .setMessage("Enter item name:")
                .setView(editText)
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String newItem = editText.getText().toString();
                        addItem(newItem);
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
    private void showEditDialog(final int position) {
        final EditText editText = new EditText(this);
        editText.setText(itemList.get(position));
        new AlertDialog.Builder(this)
                .setTitle("Edit Item")
                .setMessage("Edit item name:")
                .setView(editText)
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String editedItem = editText.getText().toString();
                        editItem(position, editedItem);
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
    private void showDeleteDialog(final int position) {
        new AlertDialog.Builder(this)
                .setTitle("Delete Item")
                .setMessage("Are you sure you want to delete this item?")
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteItem(position);
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
}
