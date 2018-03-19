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

import com.github.since1986.demo.service.AccountService;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import static com.github.since1986.demo.LoginActivity.API_BASE_URL;
import static com.github.since1986.demo.LoginActivity.KEY_SHARED_PREFERENCES;

public class RegisterActivity extends AppCompatActivity {

    private AccountService accountService;

    private ImageView imageViewClose;
    private TextView textViewLogin;

    private TextInputLayout textInputLayoutUsername;
    private TextInputLayout textInputLayoutPassword;
    private TextInputLayout textInputLayoutEmail;
    private TextInputLayout textInputLayoutPhone;
    private TextInputEditText textInputEditTextUsername;
    private TextInputEditText textInputEditTextPassword;
    private TextInputEditText textInputEditTextEmail;
    private TextInputEditText textInputEditTextPhone;

    public RegisterActivity() {
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
                                                                .addHeader("User-Agent", WebSettings.getDefaultUserAgent(RegisterActivity.this))
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

    private void doRegister(final String username, final String password, String email, String phone) {
        accountService.register(username, password, email, phone).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == 201) {
                    Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                    new Handler().post(new Runnable() {
                        @Override
                        public void run() {
                            finish();
                        }
                    });
                } else {
                    try {
                        Log.d(RegisterActivity.class.getName(), response.errorBody().string());
                    } catch (IOException e) {
                        Log.e(RegisterActivity.class.getName(), e.getMessage());
                    }
                    Toast.makeText(RegisterActivity.this, "注册失败", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e(RegisterActivity.class.getName(), t.getMessage());
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        imageViewClose = findViewById(R.id.image_view_close_activity_register);
        imageViewClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        textViewLogin = findViewById(R.id.text_view_login_activity_register);
        textViewLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(
                        new Intent(RegisterActivity.this, LoginActivity.class)
                );
            }
        });

        textInputLayoutUsername = findViewById(R.id.text_input_layout_username_activity_register);
        textInputLayoutPassword = findViewById(R.id.text_input_layout_password_activity_register);
        textInputLayoutEmail = findViewById(R.id.text_input_layout_email_activity_register);
        textInputLayoutPhone = findViewById(R.id.text_input_layout_phone_activity_register);

        textInputEditTextUsername = findViewById(R.id.text_input_edit_text_username_activity_register);
        textInputEditTextPassword = findViewById(R.id.text_input_edit_text_password_activity_register);
        textInputEditTextEmail = findViewById(R.id.text_input_edit_text_email_activity_register);
        textInputEditTextPhone = findViewById(R.id.text_input_edit_text_phone_activity_register);

        Button buttonRegister = findViewById(R.id.button_register_activity_register);
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String username = textInputEditTextUsername.getText().toString();
                final String password = textInputEditTextPassword.getText().toString();
                String email = textInputEditTextEmail.getText().toString();
                String phone = textInputEditTextPhone.getText().toString();

                if (username.length() == 0) {
                    Toast.makeText(RegisterActivity.this, "用户名不能为空", Toast.LENGTH_SHORT).show();
                } else if (password.length() < 6) {
                    Toast.makeText(RegisterActivity.this, "密码格式不正确", Toast.LENGTH_SHORT).show();
                } else {
                    textInputLayoutUsername.setErrorEnabled(false);
                    textInputLayoutPassword.setErrorEnabled(false);
                    textInputLayoutEmail.setErrorEnabled(false);
                    textInputLayoutPhone.setErrorEnabled(false);
                    doRegister(username, password, email, phone);
                }
            }
        });
    }
}
