package com.github.since1986.demo;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.bumptech.glide.Glide;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.github.since1986.demo.LoginActivity.KEY_LOGIN_USER_AVATAR;
import static com.github.since1986.demo.LoginActivity.KEY_SHARED_PREFERENCES;

public class ProfileActivity extends AppCompatActivity {

    public static final int REQUEST_CODE_PROFILE = 3;

    private Toolbar toolbar;
    private CircleImageView circleImageViewAvatar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        toolbar = findViewById(R.id.toolbar_activity_profile);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.text_user_information);

        String loginUserAvatar = getSharedPreferences(KEY_SHARED_PREFERENCES, Context.MODE_PRIVATE).getString(KEY_LOGIN_USER_AVATAR, null);

        circleImageViewAvatar = findViewById(R.id.circle_image_view_avatar_activity_profile);
        Glide
                .with(this)
                .load(loginUserAvatar)
                .into(circleImageViewAvatar);
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
