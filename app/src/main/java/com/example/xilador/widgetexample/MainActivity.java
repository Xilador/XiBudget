package com.example.xilador.widgetexample;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

//https://stackoverflow.com/questions/41613497/android-studio-how-to-set-a-fragment-from-navigation-activity-as-startup-page
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button addOne, addFive, subOne, subFive, add, sub;
    EditText custom;
    TextView text;
    SharedPreferences settings;
    SharedPreferences.Editor editor;
    SpendingData fragmentData;
    FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        settings = getSharedPreferences("cookies", 0);

        if (settings.getBoolean("useDarMode", false))setTheme(R.style.darkTheme);
        else setTheme(R.style.AppTheme);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        editor = settings.edit();
        fragmentData = new SpendingData();
        fragmentManager = getSupportFragmentManager();

        addOne = findViewById(R.id.addOne);
        addFive = findViewById(R.id.addFive);
        subOne = findViewById(R.id.takeOne);
        subFive = findViewById(R.id.takeFive);
        add = findViewById(R.id.Add);
        sub = findViewById(R.id.Subtract);

        custom = findViewById(R.id.custom);
        text = findViewById(R.id.Total);

        addOne.setOnClickListener(MainActivity.this);
        addFive.setOnClickListener(MainActivity.this);
        subOne.setOnClickListener(MainActivity.this);
        subFive.setOnClickListener(MainActivity.this);
        add.setOnClickListener(MainActivity.this);
        sub.setOnClickListener(MainActivity.this);

        String Cookies =  Integer.toString(settings.getInt("cookieAmt", 0));
        text.setText(Cookies);
        fragmentManager.beginTransaction().replace(R.id.infoFragment, fragmentData).commit();
    }

    @Override
    public void onClick(View v){

        switch (v.getId()){
            case R.id.addOne:
                editor.putInt("cookieAmt", (settings.getInt("cookieAmt", 0))+1);
                break;
            case R.id.addFive:
                editor.putInt("cookieAmt", (settings.getInt("cookieAmt", 0))+10);
                break;
            case R.id.takeOne:
                editor.putInt("cookieAmt", (settings.getInt("cookieAmt", 0))-1);
                break;
            case R.id.takeFive:
                editor.putInt("cookieAmt", (settings.getInt("cookieAmt", 0))-10);
                break;
            case R.id.Add:
                if (!custom.getText().toString().equals(""))
                    editor.putInt("cookieAmt", (settings.getInt("cookieAmt", 0))
                            + Integer.parseInt(custom.getText().toString()));
                break;
            case R.id.Subtract:
                if (!custom.getText().toString().equals(""))
                    editor.putInt("cookieAmt", (settings.getInt("cookieAmt", 0))
                            - Integer.parseInt(custom.getText().toString()));
                break;
        }
        editor.commit();
        String Cookies =  Integer.toString(settings.getInt("cookieAmt", 0));
        text.setText(Cookies);
        Intent intent = new Intent(this,cookieWidget.class);
        intent.setAction("android.appwidget.action.APPWIDGET_UPDATE");
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int appWidgetIds[] = appWidgetManager.getAppWidgetIds(new ComponentName(this, cookieWidget.class));
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS,appWidgetIds);
        sendBroadcast(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                SettingsFragment settings = new SettingsFragment();
                fragmentManager.beginTransaction().replace(R.id.infoFragment, settings).commit();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
