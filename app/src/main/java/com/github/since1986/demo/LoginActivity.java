package com.github.since1986.demo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.since1986.demo.model.User;
import com.github.since1986.demo.service.AccountService;

import org.apache.commons.lang3.StringUtils;

import java.io.IOException;

import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class LoginActivity extends AppCompatActivity {

    public static final int REQUEST_CODE_LOGIN = 1;

    public static final String API_BASE_URL = "http://10.0.2.2:8081/"; //Android中10.0.2.2代表localhost
    public static final String KEY_SHARED_PREFERENCES = "com.github.since1986.demo";
    public static final String KEY_LOGIN_USER_USERNAME = "LOGIN_USER_USERNAME";
    public static final String KEY_TOKEN = "TOKEN";
    public static final String KEY_LOGIN_USER_ID = "LOGIN_USER_ID";
    public static final String KEY_LOGIN_USER_AVATAR = "LOGIN_USER_AVATAR";
    public static final String KEY_LOGIN_USER = "LOGIN_USER";


    private ImageView imageViewClose;
    private TextView textViewRegister;

    private TextInputLayout textInputLayoutUsername;
    private TextInputLayout textInputLayoutPassword;
    private TextInputEditText textInputEditTextUsername;
    private TextInputEditText textInputEditTextPassword;

    private AccountService accountService;

    public LoginActivity() {
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
                                                                .addHeader("User-Agent", WebSettings.getDefaultUserAgent(LoginActivity.this))
                                                                .addHeader("X-NNED-X-NEED-CSRF-PROTECTION", "true")
                                                                .addHeader("X-CSRF-TOKEN", getSharedPreferences(KEY_SHARED_PREFERENCES, Context.MODE_PRIVATE).getString(LauncherActivity.KEY_CSRF_TOKEN, ""))
                                                                .build()
                                                );
                                            }
                                        }
                                )
                                .build()
                )

                .build();
        accountService = retrofit.create(AccountService.class);
    }

    private void doLogin(final String username, final String password) {
        Call<User> result = accountService.login(username, password);
        result.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    Headers responseHeader = response.headers();
                    String headerString = responseHeader.get("Authorization");
                    if (StringUtils.isNoneBlank(headerString)) {
                        String token = StringUtils.removeFirst(headerString, "Bearer ");
                        getSharedPreferences(KEY_SHARED_PREFERENCES, Context.MODE_PRIVATE)
                                .edit()
                                .putString(KEY_LOGIN_USER_USERNAME, response.body().getUsername())
                                .putString(KEY_TOKEN, token)
                                .apply();
                        setResult(
                                RESULT_OK,
                                new Intent(LoginActivity.this, MainActivity.class)
                                        .putExtra(KEY_TOKEN, token)
                                        .putExtra(KEY_LOGIN_USER, response.body())
                        );
                        new Handler().post(new Runnable() {
                            @Override
                            public void run() {
                                finish();
                            }
                        });
                    }
                } else if (response.code() == 401) {
                    Toast.makeText(LoginActivity.this, "登录失败，请检查用户名密码", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.e(LoginActivity.class.getName(), t.getMessage());
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        imageViewClose = findViewById(R.id.image_view_close_activity_login);
        imageViewClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        textViewRegister = findViewById(R.id.text_view_register_activity_login);
        textViewRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(
                        new Intent(LoginActivity.this, RegisterActivity.class)
                );
            }
        });

        textInputLayoutUsername = findViewById(R.id.text_input_layout_username_activity_login);
        textInputLayoutPassword = findViewById(R.id.text_input_layout_password_activity_login);

        textInputEditTextUsername = findViewById(R.id.text_input_edit_text_username_activity_login);
        textInputEditTextPassword = findViewById(R.id.text_input_edit_text_password_activity_login);

        Button buttonLogin = findViewById(R.id.button_login_activity_login);
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String username = textInputEditTextUsername.getText().toString();
                final String password = textInputEditTextPassword.getText().toString();

                if (username.length() == 0) {
                    textInputEditTextUsername.setError("用户名不能为空");
                    Toast.makeText(LoginActivity.this, "用户名不能为空", Toast.LENGTH_SHORT).show();
                } else if (password.length() < 6) {
                    textInputEditTextPassword.setError("密码格式不正确");
                    Toast.makeText(LoginActivity.this, "密码格式不正确", Toast.LENGTH_SHORT).show();
                } else {
                    textInputLayoutUsername.setErrorEnabled(false);
                    textInputLayoutPassword.setErrorEnabled(false);
                    doLogin(username, password);
                }
            }
        });
    }
}
