package me.sgonzalezbit.armypi;

import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class FetchActionDeactivate extends AsyncTask<Void, Void , Void> {

    protected String data = "";
    protected String urlAccess;
    protected String actionQuery;
    protected AlarmPi alarmPi;

    public FetchActionDeactivate(AlarmPi alarmPi, String urlAccess, String actionQuery){
        this.alarmPi = alarmPi;
        this.urlAccess = urlAccess;
        this.actionQuery = actionQuery;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        URL url;
        try {
            url = new URL(urlAccess);
            URL urlConnection = new URL(url.getProtocol() + "://" + url.getHost() + ":" + url.getPort() + "/" + actionQuery);
            HttpURLConnection httpURLConnection = (HttpURLConnection) urlConnection.openConnection();
            String auth = alarmPi.getUsername() + ":" + alarmPi.getPassword();
            byte[] encodedAuth = android.util.Base64.encode(auth.getBytes(StandardCharsets.UTF_8), Base64.DEFAULT);
            String authHeaderValue = "Basic " + new String(encodedAuth);
            httpURLConnection.setRequestProperty("Authorization", authHeaderValue);
            int responseCode = httpURLConnection.getResponseCode();
            if(responseCode >= 200){
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder stringBuilder = new StringBuilder();

                String line = null;
                while ((line = bufferedReader.readLine()) != null){
                    stringBuilder.append(line + "\n");
                }

                inputStream.close();
                data = stringBuilder.toString();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void unused) {
        JSONObject jsonObject = null;
        try {
            Log.i("QUESE", "onPostExecute: " + data);
            Boolean new_value = Boolean.valueOf(data);

            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference refAlarms = database.getReference("alarms");

            refAlarms.child(alarmPi.getAlarmId()).child("active").setValue(false);

        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
