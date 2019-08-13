package com.example.androidtesttask.network;

import com.example.androidtesttask.data.PointsAPIResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface PointsAPIService {

  @FormUrlEncoded
  @POST("mobws/json/pointsList")
  Call<PointsAPIResponse> getPointsList(@Field("version") String version, @Field("count") String count);

}

