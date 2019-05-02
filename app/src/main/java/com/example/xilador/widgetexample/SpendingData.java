package com.example.xilador.widgetexample;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.content.SharedPreferences;


//https://stackoverflow.com/questions/18207470/adding-table-rows-dynamically-in-android
public class SpendingData extends Fragment{

    SharedPreferences settings;
    SharedPreferences.Editor editor;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        settings = this.getContext().getSharedPreferences("cookies", 0);
        editor = settings.edit();
        View view;
        if (settings.getBoolean("saveWeeklyData", false)){
            view = inflater.inflate(R.layout.spending_layout, container, false);
        }
        else view = inflater.inflate(R.layout.no_data_saved, container, false);
        return view;
    }

}
