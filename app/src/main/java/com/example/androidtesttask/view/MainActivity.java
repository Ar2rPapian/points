package com.example.androidtesttask.view;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidtesttask.R;
import com.example.androidtesttask.adapter.CoordinateRecyclerViewAdapter;
import com.example.androidtesttask.data.Point;
import com.example.androidtesttask.databinding.MainActivityDataBinding;
import com.example.androidtesttask.viewmodel.PointsViewModel;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import static android.os.Environment.DIRECTORY_PICTURES;
import static java.lang.Integer.valueOf;
import static java.lang.String.format;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "GVIDON";

    private MainActivityDataBinding mBinding;
    private PointsViewModel pointsViewModel;
    private LineChart chart;
    private RecyclerView.LayoutManager layoutManager;
    private CoordinateRecyclerViewAdapter recyclerViewAdapter;
    private DividerItemDecoration itemDecor;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        mBinding.setLifecycleOwner(this);
        pointsViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication()).create(PointsViewModel.class);
        mBinding.setPointsViewModel(pointsViewModel);
        chart = mBinding.lineChart;
        setUpChartUi();
        pointsViewModel.getCountOfPoints().observe( this, count -> {
            if (!count.isEmpty()) {
                pointsViewModel.getAllPoints(valueOf(count)).observe(this, points -> {
                    if (points.size() > 0) {
                        chart.setData(getChartData(points));
                        initializeRecyclerView(points);
                        updateChart();
                    }
                });
                pointsViewModel.getErrors().observe( this, error -> {
                    invalidateChart();
                    setMessageToCart(format("There is an error \"%s\"", error));
                });
            } else {
                invalidateChart();
            }
        });

        mBinding.button.setOnClickListener(view -> {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
            saveImage(chart.getChartBitmap());

        });
    }

    @SuppressLint("WrongConstant")
    private void initializeRecyclerView(List<Point> data) {
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mBinding.recyclerView.setLayoutManager(layoutManager);
        mBinding.recyclerView.setHasFixedSize(true);
        recyclerViewAdapter = new CoordinateRecyclerViewAdapter(data,this);
        mBinding.recyclerView.setAdapter(recyclerViewAdapter);

    }

    private void updateChart(){
        chart.animateXY(2000, 2000);
        chart.invalidate();
    }

    private void setUpChartUi() {
        setMessageToCart(getString(R.string.no_data_chart_string));
        chart.setViewPortOffsets(0, 0, 0, 0);
        chart.getDescription().setEnabled(false);
        chart.setTouchEnabled(true);
        chart.setDragEnabled(true);
        chart.setScaleEnabled(true);
        chart.setPinchZoom(false);
        chart.setDrawGridBackground(false);
        chart.getAxisRight().setEnabled(false);
        chart.getLegend().setEnabled(false);
        XAxis x = chart.getXAxis();
        YAxis y = chart.getAxisLeft();
        x.setEnabled(false);
        y.setLabelCount(6, false);
        y.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART);
        y.setDrawGridLines(false);
        updateChart();
    }

    private LineData getChartData(List<Point> points) {
        Collections.sort(points, (point1, point2) -> point1.x.compareTo(point2.x));
        List<Entry> entries = new ArrayList<>();
        for (Point point : points) {
            Entry e = new Entry();
            e.setX(point.getX());
            e.setY(point.getY());
            entries.add(e);
        }
        LineDataSet lineDataSet = new LineDataSet(entries, "Points");
        lineDataSet.setMode(LineDataSet.Mode.HORIZONTAL_BEZIER);
        lineDataSet.setDrawCircles(true);
        lineDataSet.setCircleColor(Color.BLACK);
        lineDataSet.setLineWidth(1f);
        lineDataSet.setColor(Color.MAGENTA);
        LineData data = new LineData(lineDataSet);
        data.setValueTextSize(5f);
        return data;
    }

    private void invalidateChart() {
        mBinding.lineChart.clear();
        mBinding.lineChart.invalidate();
    }

    private void setMessageToCart(String message) {
        mBinding.lineChart.setNoDataText(message);
    }


    @SuppressLint("NewApi")
    private void saveImage(Bitmap finalBitmap) {
        String root = getExternalMediaDirs()[0].toString();
        Log.d(TAG, "saveImage: " + root);
        File myDir = new File(root + "/charts");
        myDir.mkdirs();
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String fname = "Chart_"+ timeStamp +".jpg";
        File file = new File(myDir, fname);
        if (file.exists()) file.delete ();
        try {
            FileOutputStream out = new FileOutputStream(file);
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
