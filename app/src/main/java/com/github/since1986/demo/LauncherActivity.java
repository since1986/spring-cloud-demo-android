package com.github.since1986.demo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import java.util.UUID;

import static com.github.since1986.demo.LoginActivity.KEY_SHARED_PREFERENCES;

public class LauncherActivity extends Activity {

    public static final String KEY_LAUNCH_COUNT = "LAUNCH_COUNT";
    public static final String KEY_CSRF_TOKEN = "CSRF_TOKEN";
    public static final boolean ALWAYS_SHOW_LAUNCH_SCREEN = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int launchCount = getPreferences(Context.MODE_PRIVATE).getInt(KEY_LAUNCH_COUNT, 0);
        if (ALWAYS_SHOW_LAUNCH_SCREEN || launchCount == 0) {
            startActivity(
                    new Intent(this, LaunchScreenActivity.class)
            );
        } else {
            startActivity(
                    new Intent(this, SplashActivity.class)
            );
        }
        getPreferences(Context.MODE_PRIVATE)
                .edit()
                .putInt(KEY_LAUNCH_COUNT, launchCount + 1)
                .putString(KEY_CSRF_TOKEN, UUID.randomUUID().toString())
                .apply();
        getSharedPreferences(KEY_SHARED_PREFERENCES, Context.MODE_PRIVATE)
                .edit()
                .putString(KEY_CSRF_TOKEN, UUID.randomUUID().toString())
                .apply();
        finish();
    }
}
