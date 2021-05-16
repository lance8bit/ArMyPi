package me.sgonzalezbit.armypi.adapters;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import me.sgonzalezbit.armypi.AlarmPi;
import me.sgonzalezbit.armypi.R;
import me.sgonzalezbit.armypi.ui.Notifications.fragment_single_view_notifications;

public class AdapterListForNotifications extends RecyclerView.Adapter<AdapterListForNotifications.MyViewHolder> {
    ArrayList<AlarmPi> mList;
    Context context;

    public AdapterListForNotifications(Context context, ArrayList<AlarmPi> mList){
        this.context = context;
        this.mList = mList;
    }

    @NonNull
    @NotNull
    @Override
    public AdapterListForNotifications.MyViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.single_list_for_notifications, parent, false);
        return new AdapterListForNotifications.MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull AdapterListForNotifications.MyViewHolder holder, int position) {
        AlarmPi alarmPi = mList.get(position);
        holder.cameraNameEt.setText(alarmPi.getAlarmName());
        if(alarmPi.isActive()){
            holder.alarmStatusEt.setText("Active");
        }else{
            holder.alarmStatusEt.setText("Inactive");
        }
        holder.cardViewCont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment_single_view_notifications fragment_single_view_notifications = new fragment_single_view_notifications();
                Bundle bundle = new Bundle();
                bundle.putSerializable("alarm", alarmPi);
                fragment_single_view_notifications.setArguments(bundle);
                ((FragmentActivity)context).getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, fragment_single_view_notifications)
                        .commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView cameraNameEt, alarmStatusEt;
        CardView cardViewCont;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            cameraNameEt = itemView.findViewById(R.id.cameraNameEt);
            alarmStatusEt = itemView.findViewById(R.id.alarmStatusEt);
            cardViewCont = itemView.findViewById(R.id.cardview_cont);
        }
    }
}
