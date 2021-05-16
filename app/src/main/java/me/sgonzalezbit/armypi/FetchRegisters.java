package me.sgonzalezbit.armypi;

import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import me.sgonzalezbit.armypi.ui.Notifications.fragment_single_view_notifications;

public class FetchRegisters extends AsyncTask<Void, Void , Void> {

    protected String data = "";
    protected String urlAccess;
    protected String searchQuery;
    protected AlarmPi alarmPi;

    private WeakReference<fragment_single_view_notifications> fragment_single_view_notificationsWeakReference;

    public FetchRegisters(AlarmPi alarmPi, String urlAccess, String queryUrl, fragment_single_view_notifications context){
        fragment_single_view_notificationsWeakReference = new WeakReference<>(context);
        this.alarmPi = alarmPi;
        this.urlAccess = urlAccess;
        this.searchQuery = queryUrl;
    }


    @Override
    protected Void doInBackground(Void... voids) {
        URL url = null;
        try {
            url = new URL(urlAccess);
            URL urlConnection = new URL(url.getProtocol() + "://" + url.getHost() + ":" + url.getPort() + "/" + searchQuery);
            Log.i("URLOGTEST", urlConnection.toString());
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
    protected void onPostExecute(Void cVoid) {
        JSONArray jsonArray = null;
        JSONObject jsonObject = null;

        try {
            jsonObject = new JSONObject(data);
            jsonArray = jsonObject.getJSONArray("motions");

            fragment_single_view_notifications fragment_single_view_notifications = fragment_single_view_notificationsWeakReference.get();
            fragment_single_view_notifications.registersArrayList.clear();

            for (int i = 0; i < jsonArray.length(); i++) {
                fragment_single_view_notifications.registersArrayList.add(new Notification(jsonArray.get(i).toString()));
                Log.i("RESULTPOST", jsonArray.get(i).toString());
            }

            fragment_single_view_notifications.adapterListNotifications.notifyDataSetChanged();

            for (int i = 0; i < fragment_single_view_notifications.registersArrayList.size(); i++) {
                Log.i("CONTENTARR", fragment_single_view_notifications.registersArrayList.get(i).getDate());
            }

        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
