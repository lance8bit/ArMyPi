package me.sgonzalezbit.armypi.ui.AddAlarm;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import me.sgonzalezbit.armypi.AlarmPi;
import me.sgonzalezbit.armypi.FetchActionActivate;
import me.sgonzalezbit.armypi.FetchActionDeactivate;
import me.sgonzalezbit.armypi.R;
import me.sgonzalezbit.armypi.ui.Cameras.fragment_cameras;

public class fragment_add_alarm extends Fragment {

    private TextView idAlarmTv;
    private EditText alarmNameEt, urlAccessEt, usernameEt, passwordEt;
    private RadioButton defaultRadioActive, defaultRadioInactive;
    private Button addAlarmBtn;

    private boolean defaultStatusAlarm;

    public static fragment_add_alarm newInstance() {return new fragment_add_alarm();}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_add_alarm, container, false);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference refAlarms = database.getReference("alarms");

        String alarmId = "ALRM-" + UUID.randomUUID().toString();

        idAlarmTv = root.findViewById(R.id.alarmID_Tv);
        alarmNameEt = root.findViewById(R.id.nameEt);
        urlAccessEt = root.findViewById(R.id.urlAccessEt);
        defaultRadioActive = root.findViewById(R.id.radio_active);
        defaultRadioInactive = root.findViewById(R.id.radio_inactive);
        addAlarmBtn = root.findViewById(R.id.addAlarmBtn);
        usernameEt = root.findViewById(R.id.usernameEt);
        passwordEt = root.findViewById(R.id.passwordEt);

        idAlarmTv.setText(alarmId);

        addAlarmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(defaultRadioActive.isChecked()){
                    defaultStatusAlarm = true;
                }else if(defaultRadioInactive.isChecked()){
                    defaultStatusAlarm = false;
                }
                AlarmPi newAlarm = new AlarmPi(alarmId, alarmNameEt.getText().toString(), user.getUid(), urlAccessEt.getText().toString(), defaultStatusAlarm, usernameEt.getText().toString(), passwordEt.getText().toString());

                if(defaultStatusAlarm){
                    FetchActionActivate actionActivate = new FetchActionActivate(newAlarm, newAlarm.getUrlAccess(), "alarm_activate");
                    actionActivate.execute();
                } else {
                    FetchActionDeactivate actionDeactivate = new FetchActionDeactivate(newAlarm, newAlarm.getUrlAccess(), "alarm_deactivate");
                    actionDeactivate.execute();
                }
                //Add alarm to firebase realtime database
                refAlarms.child(alarmId).setValue(newAlarm);

                Toast.makeText(getActivity(), alarmNameEt.getText().toString() + " added correctly", Toast.LENGTH_SHORT).show();
                getFragmentManager().beginTransaction().replace(R.id.container, new fragment_cameras()).commit();
            }
        });

        return root;
    }
}