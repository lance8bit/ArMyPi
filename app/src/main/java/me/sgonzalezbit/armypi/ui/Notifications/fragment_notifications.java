package me.sgonzalezbit.armypi.ui.Notifications;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import me.sgonzalezbit.armypi.AlarmPi;
import me.sgonzalezbit.armypi.FetchRegisters;
import me.sgonzalezbit.armypi.Notification;
import me.sgonzalezbit.armypi.R;
import me.sgonzalezbit.armypi.adapters.AdapterListForNotifications;
import me.sgonzalezbit.armypi.adapters.AdaptersListCameras;

public class fragment_notifications extends Fragment {

    private RecyclerView recyclerView;
    private FirebaseDatabase database;
    private DatabaseReference refAlarms, refUsers;
    private ArrayList<AlarmPi> piArrayList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_notifications, container, false);
        recyclerView = root.findViewById(R.id.listRecyclerView);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        database = FirebaseDatabase.getInstance();
        refUsers = database.getReference("notifications");
        refAlarms = database.getReference("alarms");

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        piArrayList = new ArrayList<AlarmPi>();
        AdapterListForNotifications adapterListForNotifications = new AdapterListForNotifications(getContext(), piArrayList);

        recyclerView.setAdapter(adapterListForNotifications);

        refAlarms.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    AlarmPi alarmPi = dataSnapshot.getValue(AlarmPi.class);
                    if(alarmPi.getUidUser().contains(user.getUid())){
                        piArrayList.add(alarmPi);
                    }
                }
                adapterListForNotifications.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

        return root;
    }
}