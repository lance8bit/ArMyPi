package me.sgonzalezbit.armypi.ui.Notifications;

import android.app.Activity;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import me.sgonzalezbit.armypi.AlarmPi;
import me.sgonzalezbit.armypi.FetchRegisters;
import me.sgonzalezbit.armypi.Notification;
import me.sgonzalezbit.armypi.R;
import me.sgonzalezbit.armypi.adapters.AdapterListNotifications;

public class fragment_single_view_notifications extends Fragment {

    public RecyclerView recyclerView;
    public ArrayList<Notification> registersArrayList;
    public AdapterListNotifications adapterListNotifications;
    private AlarmPi alarmPi;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_single_view_notifications, container, false);
        registersArrayList = new ArrayList<Notification>();
        recyclerView = root.findViewById(R.id.recyclerListNotifications);

        Bundle bundle = this.getArguments();
        alarmPi = (AlarmPi) bundle.getSerializable("alarm");

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        adapterListNotifications = new AdapterListNotifications(getContext(), registersArrayList);

        recyclerView.setAdapter(adapterListNotifications);

        FetchRegisters fetchRegisters = new FetchRegisters(alarmPi,alarmPi.getUrlAccess(), "alarm_registers", this);
        fetchRegisters.execute();

        for (int i = 0; i < registersArrayList.size(); i++) {
            Log.i("CONTENTARR", registersArrayList.get(i).getDate());
        }

        return root;
    }

}