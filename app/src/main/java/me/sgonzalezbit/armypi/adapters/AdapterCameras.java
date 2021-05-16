package me.sgonzalezbit.armypi.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Picture;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.HttpAuthHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.io.EOFException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;

import me.sgonzalezbit.armypi.AlarmPi;
import me.sgonzalezbit.armypi.R;

import static android.view.View.GONE;

public class AdapterCameras extends RecyclerView.Adapter<AdapterCameras.MyViewHolder>{

    ArrayList<AlarmPi> mList;
    Context context;

    public AdapterCameras(Context context, ArrayList<AlarmPi> mList){
        this.context = context;
        this.mList = mList;
    }

    @NonNull
    @NotNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.single_camera_recycler_card, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull MyViewHolder holder, int position) {
        AlarmPi alarmPi = mList.get(position);

        holder.cameraName.setText(alarmPi.getAlarmName());
        holder.cameraWeb.getSettings().setLoadWithOverviewMode(true);
        holder.cameraWeb.getSettings().setUseWideViewPort(true);
        holder.cameraWeb.loadUrl(alarmPi.getUrlAccess());
        holder.cameraWeb.setWebViewClient(new WebViewClient(){
            @Override
            public void onReceivedHttpAuthRequest(WebView view, HttpAuthHandler handler, String host, String realm) {
                handler.proceed(alarmPi.getUsername(), alarmPi.getPassword());
            }
        });
        holder.cameraName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Picture picture = holder.cameraWeb.capturePicture();
                Bitmap bitmap = Bitmap.createBitmap(picture.getWidth(), picture.getHeight(), Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(bitmap);
                holder.cameraWeb.draw(canvas);

                FileOutputStream fos = null;
                try {
                    fos = new FileOutputStream( ((FragmentActivity)context).getFilesDir().getAbsolutePath() + File.separator + "photos_alarms/"+ getAlphaNumericString(10)+".png" );
                    if ( fos != null ) {
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fos );
                        fos.close();
                    }
                }
                catch( Exception e ) {
                    System.out.println("-----error--"+e);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView cameraName;
        WebView cameraWeb;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            cameraName = itemView.findViewById(R.id.nameCamera_Tv);
            cameraWeb = itemView.findViewById(R.id.webviewCamera);
        }
    }

    public static String getAlphaNumericString(int n)
    {
        String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                + "0123456789"
                + "abcdefghijklmnopqrstuvxyz";

        StringBuilder sb = new StringBuilder(n);

        for (int i = 0; i < n; i++) {

            int index
                    = (int)(AlphaNumericString.length()
                    * Math.random());

            sb.append(AlphaNumericString
                    .charAt(index));
        }

        return sb.toString();
    }
}
