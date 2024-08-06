package com.example.todo;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class AdapterSP extends BaseAdapter {

    private Context context;
    private ArrayList<ModalSp> spinnerItems;
    private int selectedPosition = -1;

    public AdapterSP(Context context, ArrayList<ModalSp> spinnerItems) {
        this.context = context;
        this.spinnerItems = spinnerItems;
    }

    @Override
    public int getCount() {
        return spinnerItems.size();
    }

    @Override
    public Object getItem(int position) {
        return spinnerItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.spinner_layout, parent, false);
        }

        ImageView icon = convertView.findViewById(R.id.icon);
        TextView name = convertView.findViewById(R.id.name);

        ModalSp item = spinnerItems.get(position);
       // icon.setImageResource(item.getIcon());
        name.setText(item.getName());

        return convertView;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.spinner_layout2, parent, false);
        }

        ImageView icon = convertView.findViewById(R.id.icon);
        TextView name = convertView.findViewById(R.id.name);

        ModalSp item = spinnerItems.get(position);
        icon.setImageResource(item.getIcon());
        name.setText(item.getName());
        if (position == selectedPosition) {
            convertView.setBackgroundColor(Color.LTGRAY); // Change to desired color
        } else {
            convertView.setBackgroundColor(Color.WHITE); // Default color
        }

        return convertView;
    }
    public void setSelectedPosition(int position) {
        selectedPosition = position;
        notifyDataSetChanged();
    }
}
