package me.sgonzalezbit.armypi.ui.Photos;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FilenameFilter;

import me.sgonzalezbit.armypi.R;

public class fragment_photos extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_photos, container, false);

        File dir = new File(getContext().getFilesDir().getAbsolutePath()+"/photos_alarms/");
        for (final File f : dir.listFiles()) {
            Bitmap myBitmap = BitmapFactory.decodeFile(f.getAbsolutePath());
            ImageView iv = new ImageView(getContext());
            iv.setImageBitmap(myBitmap);
        }

        return root;
    }
}