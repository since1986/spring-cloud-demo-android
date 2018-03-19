package com.github.since1986.demo;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.widget.TextView;
import android.widget.Toast;

import com.github.since1986.demo.service.IndexService;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import static com.github.since1986.demo.LoginActivity.API_BASE_URL;
import static com.github.since1986.demo.LoginActivity.KEY_SHARED_PREFERENCES;
import static com.github.since1986.demo.LoginActivity.KEY_TOKEN;

public class Test1Fragment extends Fragment {

    private TextView textViewTimeFragmentTest;

    private IndexService indexService;

    public static Test1Fragment newInstance() {
        return new Test1Fragment();
    }

    public Test1Fragment() {
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
                                                                .addHeader("User-Agent", WebSettings.getDefaultUserAgent(getContext()))
                                                                .addHeader("X-NNED-X-NEED-CSRF-PROTECTION", "true")
                                                                .addHeader("X-CSRF-TOKEN", getContext().getSharedPreferences(KEY_SHARED_PREFERENCES, Context.MODE_PRIVATE).getString(LauncherActivity.KEY_CSRF_TOKEN, ""))
                                                                .addHeader("Authorization", "Bearer " + getContext().getSharedPreferences(KEY_SHARED_PREFERENCES, Context.MODE_PRIVATE).getString(KEY_TOKEN, ""))
                                                                .build()
                                                );
                                            }
                                        }
                                )
                                .build()
                )

                .build();
        indexService = retrofit.create(IndexService.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_test1, container, false);
        textViewTimeFragmentTest = view.findViewById(R.id.text_view_time_fragment_test1);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        indexService.time().enqueue(new Callback<Long>() {
            @Override
            public void onResponse(Call<Long> call, final Response<Long> response) {
                textViewTimeFragmentTest.setText("");
                if (response.isSuccessful()) {
                    new Handler().post(new Runnable() {
                        @Override
                        public void run() {
                            textViewTimeFragmentTest.setText(String.valueOf(response.body()));
                        }
                    });
                } else {
                    try {
                        Log.d(Test1Fragment.class.getName(), response.errorBody().string());
                    } catch (IOException e) {
                        Log.e(Test1Fragment.class.getName(), e.getMessage());
                    }
                    Toast.makeText(getContext(), "接口调用失败", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Long> call, Throwable t) {
                Log.e(Test1Fragment.class.getName(), t.getMessage());
            }
        });
    }
}
