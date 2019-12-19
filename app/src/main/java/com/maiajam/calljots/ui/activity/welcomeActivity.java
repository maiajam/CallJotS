package com.maiajam.calljots.ui.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.provider.Settings;
import android.provider.Telephony;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.telecom.TelecomManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;
import com.maiajam.calljots.R;
import com.maiajam.calljots.data.local.room.RoomManger;
import com.maiajam.calljots.helper.Constant;
import com.maiajam.calljots.util.workmanger.MyWorker;

import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import io.fabric.sdk.android.Fabric;

import static com.maiajam.calljots.helper.Constant.MY_IGNORE_OPTIMIZATION_REQUEST;


public class welcomeActivity extends AppCompatActivity implements View.OnClickListener {


    private static final String[] READ_CONTACT_PERMISSIONS =
            new String[]{Manifest.permission.READ_CONTACTS,
                    Manifest.permission.WRITE_CONTACTS};
    private static OneTimeWorkRequest CallRevicerRequest;
    TextView welcom_text, start_txt;
    ImageView start_img;
    SharedPreferences sp;
    SharedPreferences.Editor editor;
    private int OVERLAY_PERMISSION_CODE = 100;
    private int READ_PHONE_STATE = 5;
    private PowerManager pm;
    private int DefultCallHandler_requestCode = 120;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_welcome);

        initialView();
        initialFirstTime();
    }

    private void initialView() {
        welcom_text = (TextView) findViewById(R.id.textWlcom);
        start_txt = (TextView) findViewById(R.id.txtStart);
        start_img = (ImageView) findViewById(R.id.img_start);

        start_img.setOnClickListener(this);
        start_txt.setOnClickListener(this);
    }

    private void initialFirstTime() {
        sp = getBaseContext().getSharedPreferences("MyFirstVisit", Context.MODE_PRIVATE);
        editor = sp.edit();

        if (sp.getBoolean("firstWelcome", true)) { // this is the first time visit the app
            editor.putBoolean("firstWelcome", false);
            editor.commit();
            editor.apply();
            setDefultCallHandler();
        } else {
            startActivity(new Intent(welcomeActivity.this, MainActivity.class));
        }
    }

    private void setDefultCallHandler() {

        Intent setCallAppIntent =
                new Intent(TelecomManager.ACTION_CHANGE_DEFAULT_DIALER);
        setCallAppIntent.putExtra(TelecomManager.EXTRA_CHANGE_DEFAULT_DIALER_PACKAGE_NAME, getPackageName());
        startActivity(setCallAppIntent);
        startActivityForResult(setCallAppIntent, DefultCallHandler_requestCode);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onClick(View view) {

        if (view == start_img || view == start_txt) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                openPowerSettings();

                if (Settings.canDrawOverlays(getBaseContext())) {
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, READ_PHONE_STATE);
                    } else {
                        WorkManager.getInstance().enqueue(CallRevicerRequest);
                        startActivity(new Intent(welcomeActivity.this, MainActivity.class));
                    }
                } else {
                    Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                            Uri.parse("package:" + getPackageName()));
                    startActivityForResult(intent, OVERLAY_PERMISSION_CODE);
                    return;
                }
            } else {
                WorkManager.getInstance().enqueue(CallRevicerRequest);
                startActivity(new Intent(welcomeActivity.this, MainActivity.class));
            }
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        // If the permission has been checked
        if (requestCode == OVERLAY_PERMISSION_CODE) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (Settings.canDrawOverlays(this)) {
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, READ_PHONE_STATE);
                    } else {
                        WorkManager.getInstance().enqueue(CallRevicerRequest);
                        startActivity(new Intent(welcomeActivity.this, MainActivity.class));
                    }
                }
            }
        } else if (requestCode == MY_IGNORE_OPTIMIZATION_REQUEST) {
            pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
            boolean isIgnoringBatteryOptimizations = pm.isIgnoringBatteryOptimizations(getPackageName());
            if (isIgnoringBatteryOptimizations) {
                // Ignoring battery optimization
            } else {
                // Not ignoring battery optimization
            }

        } else if (requestCode == DefultCallHandler_requestCode) {
            checkCallLogPermission();
        }

    }

    private void checkCallLogPermission() {
        CallRevicerRequest = new OneTimeWorkRequest.Builder(MyWorker.class).build();
        if (ContextCompat.checkSelfPermission(this, String.valueOf(READ_CONTACT_PERMISSIONS)) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, READ_CONTACT_PERMISSIONS,
                    Constant.REQUEST_CODE_READ_WRITE);
        } else {
            // initiate the room manger get instance to creat the database where we will get all phone contact and then add them to our db
            RoomManger.getInstance(getBaseContext());
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case Constant.REQ_READ_PHONE_STATE:
                // If request is accepted, the result arrays not empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    WorkManager.getInstance().enqueue(CallRevicerRequest);
                    startActivity(new Intent(welcomeActivity.this, MainActivity.class));
                } else {
                    // the request is canceled then should show RequestPermissionRationale
                    ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_PHONE_STATE);
                }
                break;
            case Constant.REQUEST_CODE_READ_WRITE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    RoomManger.getInstance(getBaseContext());
                } else {

                }
                break;

        }

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void openPowerSettings() {
        pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        boolean isIgnoringBatteryOptimizations = pm.isIgnoringBatteryOptimizations(getPackageName());
        if (!isIgnoringBatteryOptimizations) {
            Intent intent = new Intent();
            intent.setAction(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
            intent.setData(Uri.parse("package:" + getPackageName()));
            startActivityForResult(intent, MY_IGNORE_OPTIMIZATION_REQUEST);
        }
    }

}
