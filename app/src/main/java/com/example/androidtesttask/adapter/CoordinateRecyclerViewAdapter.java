package com.example.androidtesttask.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidtesttask.R;
import com.example.androidtesttask.data.Point;
import com.example.androidtesttask.databinding.RowItemBinding;

import java.util.List;

public class CoordinateRecyclerViewAdapter extends RecyclerView.Adapter<CoordinateRecyclerViewAdapter.MyViewHolder> {
    private RowItemBinding mBinding;
    private List<Point> data;
    private Context context;

    public CoordinateRecyclerViewAdapter(List<Point> data, Context context) {
        this.data = data;
        this.context = context;
    }

    @Override
    public  MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
          mBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.row_item, parent, false);
        return new MyViewHolder(mBinding.getRoot());
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        mBinding.setPointsViewModel(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        public MyViewHolder(View view) {
            super(view);
        }
    }

}
