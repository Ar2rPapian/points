package com.example.androidtesttask.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PointsAPIResponse {
    @SerializedName("result")
    @Expose
    private int result;

    @SerializedName("response")
    @Expose
    private Response response;

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }
}