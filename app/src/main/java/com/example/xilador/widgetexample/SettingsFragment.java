package com.example.xilador.widgetexample;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class SettingsFragment extends Fragment {

    SharedPreferences settings;
    SharedPreferences.Editor editor;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        settings = this.getContext().getSharedPreferences("cookies", 0);
        editor = settings.edit();
        View view = inflater.inflate(R.layout.settings_layout, container, false);




        return view;
    }
}
