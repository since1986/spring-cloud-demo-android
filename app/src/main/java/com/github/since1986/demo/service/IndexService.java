package com.github.since1986.demo.service;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by since1986 on 13/03/2018.
 */

public interface IndexService {

    @GET("private/time")
    Call<Long> time();
}
