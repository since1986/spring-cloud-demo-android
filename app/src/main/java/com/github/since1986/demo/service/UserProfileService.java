package com.github.since1986.demo.service;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by since1986 on 20/03/2018.
 */

public interface UserProfileService {

    @GET("private/user/{username}")
    Call<Map<String, Object>> get(@Path("username") String username);
}
