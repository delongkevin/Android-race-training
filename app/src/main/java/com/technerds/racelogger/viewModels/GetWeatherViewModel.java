package com.technerds.racelogger.viewModels;

import android.annotation.SuppressLint;
import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.technerds.racelogger.network.ApiResponse;
import com.technerds.racelogger.network.WeatherNetworkRepository;

import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Response;

public class GetWeatherViewModel extends AndroidViewModel {
    private MutableLiveData<ApiResponse> apiResponseMutableLiveData;
    private final CompositeDisposable mDisposables = new CompositeDisposable();
    
    public GetWeatherViewModel(@NonNull Application application) {
        super(application);
    }
    
    public LiveData<ApiResponse> getResponse() {
        if (apiResponseMutableLiveData == null) {
            apiResponseMutableLiveData = new MutableLiveData<>();
        }
        return apiResponseMutableLiveData;
    }
    
    @SuppressLint("CheckResult")
    public void getWeatherByZip(String appId, String zip) {
        
        mDisposables.add(WeatherNetworkRepository.getInstance().getWeatherByZip(appId, zip)
                .subscribeOn(Schedulers.io())
                .timeout(180, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        new Consumer<Response<ResponseBody>>() {
                            @Override
                            public void accept(Response<ResponseBody> result) throws Exception {
                                apiResponseMutableLiveData.setValue(ApiResponse.success(result));
                            }
                        },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                apiResponseMutableLiveData.setValue(ApiResponse.error(throwable));
                            }
                        }
                ));
    }
    
    @SuppressLint("CheckResult")
    public void getWeatherByCoord(String appId, double lat, double lon) {
        
        mDisposables.add(WeatherNetworkRepository.getInstance().getWeatherByCoord(appId, lat, lon)
                .subscribeOn(Schedulers.io())
                .timeout(180, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        new Consumer<Response<ResponseBody>>() {
                            @Override
                            public void accept(Response<ResponseBody> result) throws Exception {
                                apiResponseMutableLiveData.setValue(ApiResponse.success(result));
                            }
                        },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                apiResponseMutableLiveData.setValue(ApiResponse.error(throwable));
                            }
                        }
                ));
    }
    
    
}
