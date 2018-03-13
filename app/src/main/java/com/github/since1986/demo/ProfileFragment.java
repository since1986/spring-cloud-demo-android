package com.github.since1986.demo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;
import static com.github.since1986.demo.LoginActivity.KEY_LOGIN_USER_AVATAR;
import static com.github.since1986.demo.LoginActivity.KEY_LOGIN_USER_USERNAME;
import static com.github.since1986.demo.LoginActivity.KEY_SHARED_PREFERENCES;

public class ProfileFragment extends Fragment {

    private CircleImageView circleImageViewAvatar;
    private TextView textViewUsername;
    private String loginUsername;
    private String loginUserAvatar;
    private LinearLayout linearLayout;
    private TextView textViewSetting;

    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }

    private void init() {
        loginUsername = getContext().getSharedPreferences(KEY_SHARED_PREFERENCES, Context.MODE_PRIVATE).getString(KEY_LOGIN_USER_USERNAME, null);
        loginUserAvatar = getContext().getSharedPreferences(KEY_SHARED_PREFERENCES, Context.MODE_PRIVATE).getString(KEY_LOGIN_USER_AVATAR, null);
        if (loginUsername == null) {
            Glide
                    .with(this)
                    .load(R.drawable.placeholder)
                    .into(circleImageViewAvatar);

            textViewUsername.setText(R.string.text_login_or_register);

            linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivityForResult(
                            new Intent(getContext(), LoginActivity.class),
                            LoginActivity.REQUEST_CODE_LOGIN
                    );
                }
            });

            textViewSetting.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivityForResult(
                            new Intent(getContext(), LoginActivity.class),
                            LoginActivity.REQUEST_CODE_LOGIN
                    );
                }
            });
        } else {
            Glide
                    .with(this)
                    .load(loginUserAvatar)
                    .into(circleImageViewAvatar);

            textViewUsername.setText(loginUsername);

            linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivityForResult(
                            new Intent(getContext(), ProfileActivity.class),
                            ProfileActivity.REQUEST_CODE_PROFILE
                    );
                }
            });

            textViewSetting.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivityForResult(
                            new Intent(getContext(), SettingsActivity.class),
                            SettingsActivity.REQUEST_CODE_LOGOUT
                    );
                }
            });
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        linearLayout = view.findViewById(R.id.linear_layout_fragment_profile);
        circleImageViewAvatar = view.findViewById(R.id.circle_image_view_fragment_profile);
        textViewUsername = view.findViewById(R.id.text_view_username_fragment_profile);
        textViewSetting = view.findViewById(R.id.text_view_setting_fragment_profile);
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SettingsActivity.REQUEST_CODE_LOGOUT) {
            init();
        } else if (requestCode == LoginActivity.REQUEST_CODE_LOGIN) {
            if (resultCode == RESULT_OK) {
                init();
            }
        } else if (requestCode == ProfileActivity.REQUEST_CODE_PROFILE) {
            init();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onResume() {
        super.onResume();
        init();
    }
}
