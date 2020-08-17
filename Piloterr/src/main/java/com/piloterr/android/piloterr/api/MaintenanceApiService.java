package com.piloterr.android.piloterr.api;

import com.piloterr.android.piloterr.models.responses.MaintenanceResponse;

import io.reactivex.Flowable;
import retrofit2.http.GET;

public interface MaintenanceApiService {

    @GET("maintenance-android.json")
    Flowable<MaintenanceResponse> getMaintenanceStatus();

    @GET("deprecation-android.json")
    Flowable<MaintenanceResponse> getDepricationStatus();

}
