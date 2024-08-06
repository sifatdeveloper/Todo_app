package com.example.todo;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class Task extends AppCompatActivity {
    Database database;
    ShoppingDB shoppingDB;
    WishlistDB wishlistDB;
    WorkDB workDB;
    ImageView backArrow;
    EditText here;
    FloatingActionButton fab;
    Spinner taskspinner;
    ArrayList<ModalSp> spinnerItems = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);

        fab = findViewById(R.id.fab);
        here = findViewById(R.id.here);
        taskspinner = findViewById(R.id.taskspinner);
        backArrow = findViewById(R.id.backArrow);
        database=new Database(this);
        shoppingDB=new ShoppingDB(this);
        workDB=new WorkDB(this);
        wishlistDB=new WishlistDB(this);

        spinnerItems.add(new ModalSp(R.drawable.list, "Personal"));
        spinnerItems.add(new ModalSp(R.drawable.list, "Shopping"));
        spinnerItems.add(new ModalSp(R.drawable.list, "Work"));
        spinnerItems.add(new ModalSp(R.drawable.list, "Wishlist"));

        AdapterSP adapterSP1 = new AdapterSP(this, spinnerItems);
        taskspinner.setAdapter(adapterSP1);

        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                finish();
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String task = here.getText().toString();
                if (!task.isEmpty()) {
                    int selectedItemPosition = taskspinner.getSelectedItemPosition();
                    ModalSp selectedItem = spinnerItems.get(selectedItemPosition);

                    String selectedCategory = selectedItem.getName();
                    if (selectedCategory.equals("Personal")) {
                        database.insertTask(task);
                        finish();
                        Toast.makeText(Task.this, "saved", Toast.LENGTH_SHORT).show();
                    } else if (selectedCategory.equals("Shopping")){
                        shoppingDB.insertTask(task);
                        finish();
                        Toast.makeText(Task.this, "saved", Toast.LENGTH_SHORT).show();
                    } else if (selectedCategory.equals("Work")) {
                        workDB.insertTask(task);
                        finish();
                        Toast.makeText(Task.this, "saved", Toast.LENGTH_SHORT).show();
                    } else if (selectedCategory.equals("Wishlist")) {
                        wishlistDB.insertTask(task);
                        finish();
                        Toast.makeText(Task.this, "saved", Toast.LENGTH_SHORT).show();

                    } else if (selectedCategory.isEmpty()) {
                        Toast.makeText(Task.this, "Please select category", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    here.setError("Please enter a task");
                }
            }
        });

    }
}
