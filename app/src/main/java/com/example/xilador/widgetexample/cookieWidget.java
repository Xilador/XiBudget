package com.example.xilador.widgetexample;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.net.Uri;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RemoteViews;
import android.app.*;
import android.content.ComponentName;
import android.widget.TextView;
import android.widget.Button;

import java.util.Calendar;


/**
 * Created by Cody on 2/28/2019.
 */

public class cookieWidget extends AppWidgetProvider{

    private static final String ACTION_UPDATE_ADD_ONE = "addOne";
    private static final String ACTION_UPDATE_TAKE_ONE = "takeOne";
    private static final String ACTION_UPDATE_ADD_FIVE = "addFive";
    private static final String ACTION_UPDATE_TAKE_FIVE = "takeFive";
    private static final String ACTION_SCHEDULED_UPDATE = "SwiggitySwoogityUpdateThatWidgety";
    boolean saveWeeklyData = true;


    private PendingIntent getPendingSelfIntent(Context context, String action) {
        Intent intent = new Intent(context, getClass());
        intent.setAction(action);
        return PendingIntent.getBroadcast(context, 0, intent, 0);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        SharedPreferences settings = context.getSharedPreferences("cookies", 0);
        String Cookies =  Integer.toString(settings.getInt("cookieAmt", 0));
        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent openApp = PendingIntent.getActivity(context, 0, intent, 0);


        for (int appWidgetID : appWidgetIds) {
            RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
                    R.layout.widget_layout);

            remoteViews.setTextViewText(R.id.Total, Cookies);
            remoteViews.setOnClickPendingIntent(R.id.addOne, getPendingSelfIntent(context, ACTION_UPDATE_ADD_ONE));
            remoteViews.setOnClickPendingIntent(R.id.takeOne, getPendingSelfIntent(context, ACTION_UPDATE_TAKE_ONE));
            remoteViews.setOnClickPendingIntent(R.id.addFive, getPendingSelfIntent(context, ACTION_UPDATE_ADD_FIVE));
            remoteViews.setOnClickPendingIntent(R.id.takeFive, getPendingSelfIntent(context, ACTION_UPDATE_TAKE_FIVE));
            remoteViews.setOnClickPendingIntent(R.id.Total, openApp);
            appWidgetManager.updateAppWidget(appWidgetID, remoteViews);
        }
        scheduleNextUpdate(context);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        SharedPreferences settings = context.getSharedPreferences("cookies", 0);
        SharedPreferences.Editor editor = settings.edit();
        AppWidgetManager manager = AppWidgetManager.getInstance(context);
        int[] ids = manager.getAppWidgetIds(new ComponentName(context, cookieWidget.class));

        if (ACTION_UPDATE_ADD_ONE.equals(intent.getAction())) {
            editor.putInt("cookieAmt", (settings.getInt("cookieAmt", 0))+1);
            meme(context, 3);
        }
        if (ACTION_UPDATE_TAKE_ONE.equals(intent.getAction())) {
            editor.putInt("cookieAmt", (settings.getInt("cookieAmt", 0))-1);
            meme(context, 2);
        }
        if (ACTION_UPDATE_ADD_FIVE.equals(intent.getAction())) {
            editor.putInt("cookieAmt", (settings.getInt("cookieAmt", 0))+10);
            meme(context, 4);
        }
        if (ACTION_UPDATE_TAKE_FIVE.equals(intent.getAction())) {
            editor.putInt("cookieAmt", (settings.getInt("cookieAmt", 0))-10);
            meme(context, 1);
        }
        if (ACTION_SCHEDULED_UPDATE.equals(intent.getAction())) {
            Calendar cal = Calendar.getInstance();
            if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY){

                if (settings.getBoolean("changeWeekly", false)){
                    if(settings.getBoolean("resetAmount", false)){
                        editor.putInt("cookieAmt", (settings.getInt("defaultAmt", 0)));
                    }
                    else{
                        editor.putInt("cookieAmt", (settings.getInt("cookieAmt", 0) + (settings.getInt("defaultAmt", 0))));
                    }
                }

                if (settings.getBoolean("saveWeeklyData", false)){

                }
            }
        }
        editor.commit();
        onUpdate(context, manager, ids);
    }

    public void meme(Context context, int stage){
        SharedPreferences settings = context.getSharedPreferences("meme", 0);
        SharedPreferences.Editor editor = settings.edit();
        int memeStage =  settings.getInt("meme", 0);
        if (memeStage == stage || memeStage+1 != stage) memeStage = 0;
        else if(memeStage+1 == 4 && stage == 4){
            memeStage = 0;
            Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + "DLzxrzFCyOs"));
            Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=DLzxrzFCyOs"));
            try {
                context.startActivity(appIntent);
            }
            catch (ActivityNotFoundException ex) {
                context.startActivity(webIntent);
            }
        }
        else if (memeStage+1 == stage) memeStage = stage;
        editor.putInt("meme", memeStage);
        editor.commit();
    }

    private static void scheduleNextUpdate(Context context) {
        AlarmManager alarmManager =
                (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, cookieWidget.class);
        intent.setAction(ACTION_SCHEDULED_UPDATE);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);

        Calendar midnight = Calendar.getInstance();
        midnight.set(Calendar.HOUR_OF_DAY, 0);
        midnight.set(Calendar.MINUTE, 0);
        midnight.set(Calendar.SECOND, 1);
        midnight.set(Calendar.MILLISECOND, 0);
        midnight.add(Calendar.DAY_OF_YEAR, 1);


        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            alarmManager.set(AlarmManager.RTC_WAKEUP, midnight.getTimeInMillis(), pendingIntent);
        } else {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, midnight.getTimeInMillis(), pendingIntent);
        }
    }
}