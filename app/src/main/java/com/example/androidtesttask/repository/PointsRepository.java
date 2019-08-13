package com.example.androidtesttask.repository;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.androidtesttask.data.Point;
import com.example.androidtesttask.data.PointsAPIResponse;
import com.example.androidtesttask.network.APIClient;
import com.example.androidtesttask.network.PointsAPIService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static java.lang.String.valueOf;

public class PointsRepository {

    private static final String TAG = "GVIDON";
    private static final String VERSION = "1.1";


    private PointsAPIService service;

    private MutableLiveData<List<Point>> points = new MutableLiveData<>();
    private MutableLiveData<String> error = new MutableLiveData<>();

    public PointsRepository(Context context){
        service = APIClient.getClient(context).create(PointsAPIService.class);
    }

    public LiveData<List<Point>> getPoints(int count){
        service.getPointsList(VERSION, valueOf(count)).enqueue(new Callback<PointsAPIResponse>() {
            @Override
            public void onResponse(Call<PointsAPIResponse> call, Response<PointsAPIResponse> response) {
                PointsAPIResponse body = response.body();
                if (body != null) {
                    points.postValue(body.getResponse().getPoints());
                    if(body.getResponse().getResult() == -100){
                        error.postValue(body.getResponse().getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<PointsAPIResponse> call, Throwable t) {
                error.postValue(t.getMessage());
            }
        });
        return points;
    }

    public LiveData<String> getError() {
        return error;
    }
}
