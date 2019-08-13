package com.example.androidtesttask.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.androidtesttask.data.Point;
import com.example.androidtesttask.repository.PointsRepository;

import java.util.List;

public class PointsViewModel extends AndroidViewModel {

    private PointsRepository repository;

    private MutableLiveData<String> countOfPoints = new MutableLiveData<>();

    public PointsViewModel(Application application) {
        super(application);
        repository = new PointsRepository(application);
        countOfPoints.postValue("10");
    }

    public MutableLiveData<String> getCountOfPoints() {
        return countOfPoints;
    }

    public LiveData<List<Point>> getAllPoints(int count){
        return repository.getPoints(count);
    }

    public LiveData<String> getErrors() {
        return repository.getError();
    }
}
