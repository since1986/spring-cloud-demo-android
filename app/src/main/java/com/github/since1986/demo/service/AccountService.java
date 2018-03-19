package com.github.since1986.demo.service;

import com.github.since1986.demo.model.User;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by since1986 on 13/03/2018.
 */

public interface AccountService {

    @FormUrlEncoded
    @POST("private/login")
    Call<User> login(@Field("username") String username, @Field("password") String password);

    @FormUrlEncoded
    @POST("private/register")
    Call<ResponseBody> register(@Field("username") String username, @Field("password") String password, @Field("email") String email, @Field("phone") String phone);
}
