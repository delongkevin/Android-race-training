package com.technerds.racelogger.network;

import android.annotation.SuppressLint;
import io.reactivex.Observable;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.technerds.racelogger.Utils.Urls;

import java.util.concurrent.TimeUnit;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class WeatherNetworkRepository {

    private NetworkInterface mNetworkInterface;
    private static WeatherNetworkRepository mNetworkRepository;

    private WeatherNetworkRepository() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();

        
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(1, TimeUnit.MINUTES)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Urls.openWeatherBseUrl)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();
        mNetworkInterface = retrofit.create(NetworkInterface.class);
    }

    public synchronized static WeatherNetworkRepository getInstance() {
        if (mNetworkRepository == null) {
            mNetworkRepository = new WeatherNetworkRepository();
        }
        return mNetworkRepository;
    }
    
    
    @SuppressLint("CheckResult")
    public Observable<Response<ResponseBody>> getWeatherByZip(String APPID, String zip) {
        return mNetworkInterface.getWeatherByZip(APPID, zip);
    }
    
    @SuppressLint("CheckResult")
    public Observable<Response<ResponseBody>> getWeatherByCoord(String APPID, double lat, double lon) {
        return mNetworkInterface.getWeatherByCoord(APPID, lat, lon);
    }

}
