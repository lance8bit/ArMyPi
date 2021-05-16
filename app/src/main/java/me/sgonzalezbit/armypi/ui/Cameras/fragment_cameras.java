package me.sgonzalezbit.armypi.ui.Cameras;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

import me.sgonzalezbit.armypi.AlarmPi;
import me.sgonzalezbit.armypi.R;
import me.sgonzalezbit.armypi.adapters.AdaptersListCameras;

public class fragment_cameras extends Fragment {

    private RecyclerView recyclerView;
    private FirebaseDatabase database;
    private DatabaseReference refAlarms;
    private ArrayList<AlarmPi> piArrayList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_cameras, container, false);
        recyclerView = root.findViewById(R.id.recyclerListCameras);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        database = FirebaseDatabase.getInstance();
        refAlarms = database.getReference("alarms");

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        piArrayList = new ArrayList<AlarmPi>();
        AdaptersListCameras adaptersListCameras = new AdaptersListCameras(getContext(), piArrayList);

        recyclerView.setAdapter(adaptersListCameras);

        refAlarms.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    AlarmPi alarmPi = dataSnapshot.getValue(AlarmPi.class);
                    if(alarmPi.getUidUser().contains(user.getUid())){
                        piArrayList.add(alarmPi);
                    }
                }
                adaptersListCameras.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

        return root;
    }
}