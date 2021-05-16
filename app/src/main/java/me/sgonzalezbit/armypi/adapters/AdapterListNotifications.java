package me.sgonzalezbit.armypi.adapters;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import me.sgonzalezbit.armypi.Notification;
import me.sgonzalezbit.armypi.R;

public class AdapterListNotifications extends RecyclerView.Adapter<AdapterListNotifications.MyViewHolder> {
    ArrayList<Notification> mList;
    Context context;

    public AdapterListNotifications(Context context, ArrayList<Notification> mList){
        this.context = context;
        this.mList = mList;
    }

    @NonNull
    @NotNull
    @Override
    public AdapterListNotifications.MyViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.single_list_notification, parent, false);
        return new AdapterListNotifications.MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull AdapterListNotifications.MyViewHolder holder, int position) {
        Notification notification = mList.get(position);
        holder.messageNotifyTv.setText(notification.getMessage());
        holder.dateNotifyTv.setText(notification.getDate());
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView messageNotifyTv, dateNotifyTv;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            messageNotifyTv = itemView.findViewById(R.id.textNotify);
            dateNotifyTv = itemView.findViewById(R.id.dateNotify);
        }
    }
}