package com.maiajam.calljots.ui.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.maiajam.calljots.R;
import com.maiajam.calljots.ui.fragment.AllContactFrag;
import com.maiajam.calljots.util.CallServiceForGround;
import com.maiajam.calljots.util.workmanger.MyWorker;

import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;


public class welcome extends AppCompatActivity implements View.OnClickListener {


    private static OneTimeWorkRequest CallRevicerRequest ;
    TextView welcom_text,start_txt;
    ImageView start_img;
    private int OVERLAY_PERMISSION_CODE = 100;
    Intent i ;
    private int READ_PHONE_STATE = 5;
    SharedPreferences sp ;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        welcom_text = (TextView) findViewById(R.id.textWlcom);
        start_txt = (TextView) findViewById(R.id.txtStart);
        start_img = (ImageView) findViewById(R.id.img_start);

        start_img.setOnClickListener(this);
        i = new Intent(getBaseContext(), CallServiceForGround.class);

       sp = getBaseContext().getSharedPreferences("MyFirstVisit", Context.MODE_PRIVATE);
        editor = sp.edit();

        if(sp.getBoolean("firstWelcome",true))
        {
            // this is the first time visit the app
            editor.putBoolean("firstWelcome", false);
            editor.commit();
            editor.apply();
            CallRevicerRequest = new OneTimeWorkRequest.Builder(MyWorker.class).build();

        }else
        {
            startActivity(new Intent(welcome.this,MainActivity.class));
        }

    }

    @Override
    public void onClick(View view) {

        if(view == start_img)
        {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (Settings.canDrawOverlays(getBaseContext())) {
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED)
                    {
                        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, READ_PHONE_STATE);
                    } else {
                        WorkManager.getInstance().enqueue(CallRevicerRequest);
                        startActivity(new Intent(welcome.this,MainActivity.class));
                    }
                } else {

                    Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                            Uri.parse("package:" + getPackageName()));
                    startActivityForResult(intent, OVERLAY_PERMISSION_CODE);
                    return;
                }
            }else {
                WorkManager.getInstance().enqueue(CallRevicerRequest);
                startActivity(new Intent(welcome.this,MainActivity.class));
            }

            /*
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (Settings.canDrawOverlays(getBaseContext())) {
                    if (ContextCompat.checkSelfPermission(this,  Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, READ_PHONE_STATE);
                    }else {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            getBaseContext().startForegroundService(i);

                        }else
                        {
                            startActivity(new Intent(welcome.this,MainActivity.class));
                            startService(i);
                        }
                    }

                } else {
                    Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                            Uri.parse("package:" + getPackageName()));
                    startActivityForResult(intent, OVERLAY_PERMISSION_CODE);
                    return;
                }
            }else {

                startActivity(new Intent(welcome.this,MainActivity.class));
            }
            */

        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,  Intent data) {

        // If the permission has been checked
        if (requestCode == OVERLAY_PERMISSION_CODE) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (Settings.canDrawOverlays(this)) {
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED)
                    {
                        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, READ_PHONE_STATE);
                    } else {
                        WorkManager.getInstance().enqueue(CallRevicerRequest);
                        startActivity(new Intent(welcome.this,MainActivity.class));
                    }
                }
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
      switch (requestCode) {

          case 5:
              // If request is accepted, the result arrays not empty.
              if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
              {
                  WorkManager.getInstance().enqueue(CallRevicerRequest);
                  startActivity(new Intent(welcome.this,MainActivity.class));
              } else {
                    // the request is canceled then should show RequestPermissionRationale
                 ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.READ_PHONE_STATE);

              }
              return;
      }


    }
}
