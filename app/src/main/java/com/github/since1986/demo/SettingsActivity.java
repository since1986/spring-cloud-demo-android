package com.github.since1986.demo;

import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class SettingsActivity extends AppCompatActivity {

    public static final int REQUEST_CODE_LOGOUT = 2;
    private TextView textViewLogout;

    private void doLogout() {
        getSharedPreferences(LoginActivity.KEY_SHARED_PREFERENCES, Context.MODE_PRIVATE)
                .edit()
                .remove(LoginActivity.KEY_TOKEN)
                .remove(LoginActivity.KEY_LOGIN_USER_ID)
                .remove(LoginActivity.KEY_LOGIN_USER_USERNAME)
                .remove(LoginActivity.KEY_LOGIN_USER_AVATAR)
                .apply();

        setResult(
                RESULT_OK
        );

        new Handler().post(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Toolbar toolbar = findViewById(R.id.toolbar_activity_settings);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.text_settings);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        TextView textViewVersion = findViewById(R.id.text_view_version_activity_settings);
        String version = null;
        try {
            version = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        textViewVersion.setText(version);

        //退出登录
        textViewLogout = findViewById(R.id.text_view_logout_activity_settings);
        textViewLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(SettingsActivity.this)
                        .setTitle("退出登录")
                        .setMessage("是否确认退出登录？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                doLogout();
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .create()
                        .show();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
