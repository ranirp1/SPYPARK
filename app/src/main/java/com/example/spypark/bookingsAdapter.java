package com.example.spypark;

import android.content.Context;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;

import java.net.FileNameMap;
import java.util.ArrayList;

public class bookingsAdapter extends RecyclerView.Adapter<bookingsAdapter.bookingsViewHolder> {

    Context context;
    ArrayList<bookings> list;

    public bookingsAdapter(Context context, ArrayList<bookings> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public bookingsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item,parent,false);
        return new bookingsViewHolder(v);
    }

    @Override
    public void onBindViewHolder(bookingsViewHolder holder, int position) {
        bookings bookings = list.get(position);
        holder.vechNum.setText(bookings.getVechNum());
        holder.parkName.setText(bookings.getParkName());
        holder.bookTime.setText(bookings.getBookTime());
        holder.endTime.setText(bookings.getEndTime());
        holder.money.setText(bookings.getMoney());


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class bookingsViewHolder extends RecyclerView.ViewHolder {

        TextView vechNum, parkName, bookTime, endTime, money;

        public bookingsViewHolder(View itemView) {
            super(itemView);
            vechNum = itemView.findViewById(R.id.vechNum);
            parkName = itemView.findViewById(R.id.parkName);
            bookTime = itemView.findViewById(R.id.bookTime);
            endTime = itemView.findViewById(R.id.endTime);
            money = itemView.findViewById(R.id.money);

        }
    }
}
