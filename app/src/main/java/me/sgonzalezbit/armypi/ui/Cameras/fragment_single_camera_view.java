package me.sgonzalezbit.armypi.ui.Cameras;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import me.sgonzalezbit.armypi.AlarmPi;
import me.sgonzalezbit.armypi.FetchActionActivate;
import me.sgonzalezbit.armypi.FetchActionDeactivate;
import me.sgonzalezbit.armypi.R;
import me.sgonzalezbit.armypi.adapters.AdaptersListCameras;
import me.sgonzalezbit.armypi.ui.Cameras.fragment_cameras;

public class fragment_single_camera_view extends Fragment {

    TextView TvName, TvStatus, TvId, TvUrlAccess, TvUsername, TvPassword;
    Button deactivateBtn, activateBtn, removeBtn;
    public AdaptersListCameras adaptersListCameras;
    private AlarmPi alarmPi;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_single_camera_view, container, false);
        Bundle bundle = this.getArguments();
        alarmPi = (AlarmPi) bundle.getSerializable("alarm");

        TvName = root.findViewById(R.id.alarmNameTv);
        TvStatus = root.findViewById(R.id.alarmStatusTv);
        TvId = root.findViewById(R.id.alarmID_Tv);
        TvUrlAccess = root.findViewById(R.id.alarmUrlAccess_Tv);
        TvUsername = root.findViewById(R.id.alarmUsernameTv);
        TvPassword = root.findViewById(R.id.alarmPasswordTv);
        deactivateBtn = root.findViewById(R.id.deactivateAlarmBtn);
        activateBtn = root.findViewById(R.id.activateAlarmBtn);
        removeBtn = root.findViewById(R.id.RemoveAlarmBtn);

        TvName.setText(alarmPi.getAlarmName());
        if(alarmPi.isActive()){
            TvStatus.setText("Active");
        }else{
            TvStatus.setText("Inactive");
        }
        TvId.setText(alarmPi.getAlarmId());
        TvUrlAccess.setText(alarmPi.getUrlAccess());
        TvPassword.setText(alarmPi.getPassword());

        deactivateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FetchActionDeactivate actionDeactivate = new FetchActionDeactivate(alarmPi, alarmPi.getUrlAccess(), "alarm_deactivate");
                actionDeactivate.execute();
                TvStatus.setText("Inactive");
            }
        });

        activateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FetchActionActivate actionActivate = new FetchActionActivate(alarmPi, alarmPi.getUrlAccess(), "alarm_activate");
                actionActivate.execute();
                TvStatus.setText("Active");
            }
        });

        removeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference refAlarms = database.getReference("alarms");
                refAlarms.child(alarmPi.getAlarmId()).removeValue();
                getFragmentManager().beginTransaction()
                        .replace(R.id.container, new fragment_cameras())
                        .commit();
            }
        });

        return root;
    }
}