package com.github.since1986.demo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebSettings;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.since1986.demo.service.IndexService;
import com.github.since1986.demo.service.UserService;

import org.apache.commons.lang3.StringUtils;

import java.io.IOException;

import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class LoginActivity extends AppCompatActivity {

    public static final int REQUEST_CODE_LOGIN = 1;

    public static final String API_BASE_URL = "http://10.0.2.2:8002/"; //Android中10.0.2.2代表localhost
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

    private UserService userService;
    private IndexService indexService;

    public LoginActivity() {
        Retrofit retrofit = new Retrofit.Builder()
                .client(
                        new OkHttpClient.Builder()
                                .addInterceptor(
                                        new Interceptor() {
                                            @Override
                                            public okhttp3.Response intercept(Chain chain) throws IOException {
                                                Request request = chain.request()
                                                        .newBuilder()
                                                        .removeHeader("User-Agent")//移除旧的
                                                        .addHeader("User-Agent", WebSettings.getDefaultUserAgent(LoginActivity.this))//添加真正的头部
                                                        .build();
                                                return chain.proceed(request);
                                            }
                                        })
                                .build())
                .baseUrl(API_BASE_URL)
                .build();
        userService = retrofit.create(UserService.class);
        indexService = retrofit.create(IndexService.class);
    }

    private void doLogin(final String username, final String password) {
        Call<Long> timeCall = indexService.time();
        timeCall.enqueue(new Callback<Long>() {
            @Override
            public void onResponse(Call<Long> call, Response<Long> response) {
                Toast.makeText(LoginActivity.this, response.body() + "", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<Long> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "调用失败 [" + t.getMessage() + "]", Toast.LENGTH_SHORT).show();
            }
        });

        Call<ResponseBody> result = userService.login(username, password);
        result.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Headers responseHeader = response.headers();
                String headerString = responseHeader.get("Authorization");
                if (StringUtils.isNoneBlank(headerString)) {
                    getSharedPreferences(KEY_SHARED_PREFERENCES, Context.MODE_PRIVATE)
                            .edit()
                            .putString(KEY_LOGIN_USER_USERNAME, response.message())
                            .putString(KEY_TOKEN, StringUtils.removeFirst(headerString, "Bearer "))
                            .apply();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "登录失败 [" + t.getMessage() + "]", Toast.LENGTH_SHORT).show();
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