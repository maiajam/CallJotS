package com.maiajam.calljots.ui.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.maiajam.calljots.R;
import com.maiajam.calljots.util.CallService;


public class welcome extends AppCompatActivity implements View.OnClickListener {


    TextView welcom_text,start_txt;
    ImageView start_img;
    private int OVERLAY_PERMISSION_CODE = 100;
    Intent i ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        welcom_text = (TextView) findViewById(R.id.textWlcom);
        start_txt = (TextView) findViewById(R.id.txtStart);
        start_img = (ImageView) findViewById(R.id.img_start);


        start_img.setOnClickListener(this);
        i = new Intent(getBaseContext(), CallService.class);


    }

    @Override
    public void onClick(View view) {

        if(view == start_img)
        {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (Settings.canDrawOverlays(getBaseContext())) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        getBaseContext().startForegroundService(i);
                        startActivity(new Intent(welcome.this,MainActivity.class));
                        startService(i);
                    }else
                    {
                        startActivity(new Intent(welcome.this,MainActivity.class));
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

        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,  Intent data) {

        // If the permission has been checked
        if (requestCode == OVERLAY_PERMISSION_CODE) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (Settings.canDrawOverlays(this)) {

                    Intent i = new Intent(getBaseContext(),CallService.class);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        getBaseContext().startForegroundService(i);
                        startActivity(new Intent(welcome.this,MainActivity.class));
                    }else
                    {
                        startActivity(new Intent(welcome.this,MainActivity.class));
                    }
                }
            }
        }
    }
}
