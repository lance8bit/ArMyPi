package me.sgonzalezbit.armypi.adapters;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.HttpAuthHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import me.sgonzalezbit.armypi.AlarmPi;
import me.sgonzalezbit.armypi.R;
import me.sgonzalezbit.armypi.ui.Cameras.fragment_single_camera_view;
import me.sgonzalezbit.armypi.ui.Notifications.fragment_single_view_notifications;

public class AdaptersListCameras extends RecyclerView.Adapter<AdaptersListCameras.MyViewHolder>{

    ArrayList<AlarmPi> mList;
    Context context;

    public AdaptersListCameras(Context context, ArrayList<AlarmPi> mList){
        this.context = context;
        this.mList = mList;
    }

    @NonNull
    @NotNull
    @Override
    public AdaptersListCameras.MyViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.single_list_cameras, parent, false);
        return new AdaptersListCameras.MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull AdaptersListCameras.MyViewHolder holder, int position) {
        AlarmPi alarmPi = mList.get(position);
        holder.cameraNameEt.setText(alarmPi.getAlarmName());
        if(alarmPi.isActive()){
            holder.alarmStatusEt.setText("Active");
            holder.statusColorCv.setCardBackgroundColor(Color.GREEN);
        }else{
            holder.alarmStatusEt.setText("Inactive");
            holder.statusColorCv.setCardBackgroundColor(Color.RED);
        }
        holder.cardContainerCameraCv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment_single_camera_view fragment_single_camera_view = new fragment_single_camera_view();
                Bundle bundle = new Bundle();
                bundle.putSerializable("alarm", alarmPi);
                fragment_single_camera_view.setArguments(bundle);
                ((FragmentActivity)context).getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, fragment_single_camera_view)
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
        CardView statusColorCv, cardContainerCameraCv;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            cameraNameEt = itemView.findViewById(R.id.cameraNameEt);
            alarmStatusEt = itemView.findViewById(R.id.alarmStatusEt);
            statusColorCv = itemView.findViewById(R.id.statusColor);
            cardContainerCameraCv = itemView.findViewById(R.id.cardContainerCamera);
        }
    }
}
