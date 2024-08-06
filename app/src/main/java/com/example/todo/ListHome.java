package com.example.todo;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class ListHome extends AppCompatActivity {
    FloatingActionButton fab;
    WishlistDB wishlistDB;
    Spinner spinner;
    WorkDB workDB;
    Database database;
    ShoppingDB shoppingDB;
    ArrayList<ModalSp> spinnerItems = new ArrayList<>();
    RecyclerView recyclerView;
    HomeAdpter homeAdapter;
    ArrayList<ModalAdapter> tasks = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_home);

        fab = findViewById(R.id.fab);
        spinner = findViewById(R.id.spinner1);
        recyclerView = findViewById(R.id.recy);
        database = new Database(this);
        shoppingDB=new ShoppingDB(this);
        wishlistDB=new WishlistDB(this);
        workDB=new WorkDB(this);
        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(manager);

        spinnerItems.add(new ModalSp(R.drawable.home, "All List"));
        spinnerItems.add(new ModalSp(R.drawable.list, "Personal"));
        spinnerItems.add(new ModalSp(R.drawable.list, "Shopping"));
        spinnerItems.add(new ModalSp(R.drawable.list, "Work"));
        spinnerItems.add(new ModalSp(R.drawable.list, "Wishlist"));

        AdapterSP adapterSP = new AdapterSP(this, spinnerItems);
        spinner.setAdapter(adapterSP);

        homeAdapter = new HomeAdpter(this, tasks);
        recyclerView.setAdapter(homeAdapter);


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                adapterSP.setSelectedPosition(position);
                String selectedCategory = spinnerItems.get(position).getName();
                if (selectedCategory.equals("Personal")) {
                    tasks.clear();
                    tasks.addAll(database.getAllTasks());
                } else if (selectedCategory.equals("Shopping")) {
                    tasks.clear();
                    tasks.addAll(shoppingDB.getAllTasks());

                } else if (selectedCategory.equals("Work")) {
                    tasks.clear();
                    tasks.addAll(workDB.getAllTasks());
                    // Add filtering logic for "Work" category here
                } else if (selectedCategory.equals("Wishlist")) {
                    tasks.clear();
                    tasks.addAll(wishlistDB.getAllTasks());
                } else if (selectedCategory.equals("All List")) {
                    tasks.clear();
                    tasks.addAll(database.getAllTasks());
                    tasks.addAll(shoppingDB.getAllTasks());
                    tasks.addAll(workDB.getAllTasks());
                    tasks.addAll(wishlistDB.getAllTasks());

                }
                homeAdapter.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ListHome.this, Task.class);
                startActivity(intent);
            }
        });

        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                ModalAdapter modalAdapter = tasks.get(position);
                int id = modalAdapter.getId();
                switch (direction) {
                    case ItemTouchHelper.LEFT:
                        new AlertDialog.Builder(ListHome.this)
                                .setTitle("Delete Task")
                                .setIcon(R.drawable.toolicon30)
                                .setMessage("Are you sure you want to delete this task?")
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        database.deleteTask(id);
                                        wishlistDB.deleteTask(id);
                                        shoppingDB.deleteTask(id);
                                        workDB.deleteTask(id);
                                        tasks.remove(position);
                                        homeAdapter.notifyItemRemoved(position);
                                    }
                                })
                                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        homeAdapter.notifyItemChanged(position);
                                    }
                                })
                                .setCancelable(false)
                                .show();
                        break;
                    case ItemTouchHelper.RIGHT:
                        AlertDialog.Builder builder = new AlertDialog.Builder(ListHome.this);
                        builder.setTitle("Update Task")
                                .setIcon(R.drawable.toolicon30);
                        final EditText input = new EditText(ListHome.this);
                        input.setText(modalAdapter.getA());
                        builder.setView(input);

                        builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String updatedTaskName = input.getText().toString();
                                database.updateTask(id, updatedTaskName);
                                wishlistDB.updateTask(id, updatedTaskName);
                                shoppingDB.updateTask(id, updatedTaskName);
                                workDB.updateTask(id, updatedTaskName);
                                modalAdapter.setA(updatedTaskName);
                                homeAdapter.notifyItemChanged(position);
                            }
                        });
                        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                homeAdapter.notifyItemChanged(position);
                                dialog.cancel();
                            }
                        });

                        builder.show();
                        break;
                }
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }
    @Override
    protected void onResume() {
        super.onResume();
        AdapterSP adapterSP = new AdapterSP(this, spinnerItems);
        adapterSP.setSelectedPosition(0);
        String selectedCategory = spinnerItems.get(0).getName();
        if (selectedCategory.equals("All List")){
            tasks.clear();
            tasks.addAll(database.getAllTasks());
            tasks.addAll(shoppingDB.getAllTasks());
            tasks.addAll(workDB.getAllTasks());
            tasks.addAll(wishlistDB.getAllTasks());
        }
        homeAdapter.notifyDataSetChanged();
        // Resume any paused operations, like game rendering
    }

}
