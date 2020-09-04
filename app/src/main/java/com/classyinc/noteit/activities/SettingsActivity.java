package com.classyinc.noteit.activities;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;


import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;

import com.classyinc.noteit.R;


import java.util.Objects;

public class SettingsActivity extends AppCompatActivity {

    private AlertDialog Settings_dialog;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Toolbar toolbar = findViewById(R.id.Settings_toolbar_id);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        SwitchCompat switchCompat = findViewById(R.id.switch_mode_id);
        switchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {

                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);

                }else {

                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

                }
                overridePendingTransition(R.anim.fadein,R.anim.fadeoout);
            }
        });

        Button btninstall = findViewById(R.id.Install_id);
        btninstall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               showdialog();
            }
        });

    }

    private void showdialog() {


        if(Settings_dialog == null) {

            AlertDialog.Builder builder = new AlertDialog.Builder(SettingsActivity.this);
            View view = LayoutInflater.from(this).inflate(
                    R.layout.layout_settings_alert,
                    (ViewGroup) findViewById(R.id.layout_settings_container_id)
            );
            builder.setView(view);
            Settings_dialog = builder.create();
            if(Settings_dialog.getWindow() != null) {
                Settings_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
            }
            view.findViewById(R.id.txt_YES_id).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    try {
                        startActivity(new Intent(Intent.ACTION_VIEW,
                                Uri.parse("market://details?id=" + "com.classyinc.classytreasurer"))); //market://details?id=" + "com.classyinc.classytreasurer
                    } catch (ActivityNotFoundException e) {
                        startActivity(new Intent(Intent.ACTION_VIEW,
                                Uri.parse("http://play.google.com/store/apps/details?id=" + getPackageName())));
                    }
                    Settings_dialog.dismiss();

                }
            });

            view.findViewById(R.id.txt_NO_id).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Settings_dialog.dismiss();
                }
            });
        }

        Settings_dialog.show();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}