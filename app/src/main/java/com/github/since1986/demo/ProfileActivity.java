package com.github.since1986.demo;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.webkit.WebSettings;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.github.since1986.demo.service.UserProfileService;

import java.io.IOException;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import static com.github.since1986.demo.LoginActivity.API_BASE_URL;
import static com.github.since1986.demo.LoginActivity.KEY_LOGIN_USER_AVATAR;
import static com.github.since1986.demo.LoginActivity.KEY_LOGIN_USER_USERNAME;
import static com.github.since1986.demo.LoginActivity.KEY_SHARED_PREFERENCES;
import static com.github.since1986.demo.LoginActivity.KEY_TOKEN;

public class ProfileActivity extends AppCompatActivity {

    public static final int REQUEST_CODE_PROFILE = 3;

    private UserProfileService userProfileService;

    private Toolbar toolbar;
    private CircleImageView circleImageViewAvatar;
    private TextView textViewEmail;

    public ProfileActivity() {
        Retrofit retrofit = new Retrofit
                .Builder()
                .baseUrl(API_BASE_URL)
                .addConverterFactory(JacksonConverterFactory.create())
                .client(
                        new OkHttpClient.Builder()
                                .addInterceptor(
                                        new HttpLoggingInterceptor()
                                                .setLevel(HttpLoggingInterceptor.Level.BODY)
                                )
                                .addInterceptor(
                                        new Interceptor() {
                                            @Override
                                            public okhttp3.Response intercept(Chain chain) throws IOException {
                                                return chain.proceed(
                                                        chain.request()
                                                                .newBuilder()
                                                                .addHeader("User-Agent", WebSettings.getDefaultUserAgent(getApplication()))
                                                                .addHeader("X-NNED-X-NEED-CSRF-PROTECTION", "true")
                                                                .addHeader("X-CSRF-TOKEN", getApplication().getSharedPreferences(KEY_SHARED_PREFERENCES, Context.MODE_PRIVATE).getString(LauncherActivity.KEY_CSRF_TOKEN, ""))
                                                                .addHeader("Authorization", "Bearer " + getApplication().getSharedPreferences(KEY_SHARED_PREFERENCES, Context.MODE_PRIVATE).getString(KEY_TOKEN, ""))
                                                                .build()
                                                );
                                            }
                                        }
                                )
                                .build()
                )

                .build();
        userProfileService = retrofit.create(UserProfileService.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        toolbar = findViewById(R.id.toolbar_activity_profile);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.text_user_information);

        textViewEmail = findViewById(R.id.text_view_email_activity_profile);
        userProfileService.get(getSharedPreferences(KEY_SHARED_PREFERENCES, Context.MODE_PRIVATE).getString(KEY_LOGIN_USER_USERNAME, null)).enqueue(new Callback<Map<String, Object>>() {
            @Override
            public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {
                if (response.isSuccessful()) {
                    Map<String, Object> profile = (Map<String, Object>) response.body().get("profile");
                    textViewEmail.setText(profile.get("email").toString());
                } else {
                    try {
                        Toast.makeText(ProfileActivity.this, response.errorBody().string(), Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        Toast.makeText(ProfileActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<Map<String, Object>> call, Throwable t) {
                Toast.makeText(ProfileActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

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
