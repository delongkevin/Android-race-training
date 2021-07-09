package com.technerds.racelogger.network;

import android.annotation.SuppressLint;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.technerds.racelogger.dataModels.EditProfileSendModel;
import com.technerds.racelogger.dataModels.SignInSendModel;
import com.technerds.racelogger.dataModels.SignUpSendModel;
import com.technerds.racelogger.dataModels.SortedRaceListingSendModel;
import com.technerds.racelogger.dataModels.addRaceModel.AddRaceSendModel;
import com.technerds.racelogger.Utils.Urls;
import com.technerds.racelogger.dataModels.addRunModel.AddRunSendModel;
import com.technerds.racelogger.dataModels.shoeModels.AddShoeSendModel;
import com.technerds.racelogger.dataModels.updateRaceModel.UpdateRaceSendModel;
import com.technerds.racelogger.dataModels.updateRunModel.UpdateRunSendModel;


import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetworkRepository {

    private NetworkInterface mNetworkInterface;
    private static NetworkRepository mNetworkRepository;

    private NetworkRepository() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
    
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(1, TimeUnit.MINUTES)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .addInterceptor(interceptor)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Urls.baseUrl)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();
        mNetworkInterface = retrofit.create(NetworkInterface.class);
    }

    public synchronized static NetworkRepository getInstance() {
        if (mNetworkRepository == null) {
            mNetworkRepository = new NetworkRepository();
        }
        return mNetworkRepository;
    }
    
    @SuppressLint("CheckResult")
    public Observable<Response<ResponseBody>> uploadFile( MultipartBody.Part file) {
        return mNetworkInterface.uploadFile( file);
    }

    @SuppressLint("CheckResult")
    public Observable<Response<ResponseBody>> login(SignInSendModel model) {
        return mNetworkInterface.login(model);
    }
    
    
    @SuppressLint("CheckResult")
    public Observable<Response<ResponseBody>> signup(SignUpSendModel model) {
        return mNetworkInterface.signup(model);
    }
    
    @SuppressLint("CheckResult")
    public Observable<Response<ResponseBody>> forgotPassword(String email) {
        return mNetworkInterface.forgotPassword(email);
    }
    
    @SuppressLint("CheckResult")
    public Observable<Response<ResponseBody>> verifyCode(String code) {
        return mNetworkInterface.verifyCode(code);
    }
    
    @SuppressLint("CheckResult")
    public Observable<Response<ResponseBody>> resetPassword(String forget_token,  String password, String confirmation_password) {
        return mNetworkInterface.resetPassword(forget_token,  password, confirmation_password);
    }
    
    @SuppressLint("CheckResult")
    public Observable<Response<ResponseBody>> updateProfile(EditProfileSendModel model) {
        return mNetworkInterface.updateProfile(model);
    }

    @SuppressLint("CheckResult")
    public Observable<Response<ResponseBody>> addRace(AddRaceSendModel model) {
        return mNetworkInterface.addRace(model);
    }
    
    @SuppressLint("CheckResult")
    public Observable<Response<ResponseBody>> addRun(AddRunSendModel model) {
        return mNetworkInterface.addRun(model);
    }
    
    @SuppressLint("CheckResult")
    public Observable<Response<ResponseBody>> updateRace(UpdateRaceSendModel model) {
        return mNetworkInterface.updateRace(model);
    }
    
    @SuppressLint("CheckResult")
    public Observable<Response<ResponseBody>> updateRun(UpdateRunSendModel model) {
        return mNetworkInterface.updateRun(model);
    }
    
    
    @SuppressLint("CheckResult")
    public Observable<Response<ResponseBody>> getDropDown() {
        return mNetworkInterface.getDropDown();
    }
    
    @SuppressLint("CheckResult")
    public Observable<Response<ResponseBody>> getProfile(String token) {
        return mNetworkInterface.getProfile(token);
    }
    
    @SuppressLint("CheckResult")
    public Observable<Response<ResponseBody>> getShoeMileageRun(String token, String brandName, String model) {
        return mNetworkInterface.getShoeMileageRun(token, brandName, model);
    }
    
    @SuppressLint("CheckResult")
    public Observable<Response<ResponseBody>> getUserDashboard(String token) {
        return mNetworkInterface.getUserDashboard(token);
    }
    
    @SuppressLint("CheckResult")
    public Observable<Response<ResponseBody>> getRaceListing(String token, int page) {
        return mNetworkInterface.getRaceListing(token, page);
    }
    
    @SuppressLint("CheckResult")
    public Observable<Response<ResponseBody>> getRaceListing(String token, int page, int year, int month) {
        return mNetworkInterface.getRaceListing(token, page, year, month);
    }
    
    @SuppressLint("CheckResult")
    public Observable<Response<ResponseBody>> getSortedRaceListing(SortedRaceListingSendModel model) {
        return mNetworkInterface.getSortedRaceListing(model);
    }
    
    @SuppressLint("CheckResult")
    public Observable<Response<ResponseBody>> getRunListing(String token, int page) {
        return mNetworkInterface.getRunListing(token, page);
    }
    
    @SuppressLint("CheckResult")
    public Observable<Response<ResponseBody>> searchRaceName(String token) {
        return mNetworkInterface.searchRaceName(token);
    }
    
    @SuppressLint("CheckResult")
    public Observable<Response<ResponseBody>> searchRunName(String token) {
        return mNetworkInterface.searchRunName(token);
    }
    
    
    @SuppressLint("CheckResult")
    public Observable<Response<ResponseBody>> getGraphByYear(String token) {
        return mNetworkInterface.getGraphByYear(token);
    }
    
    @SuppressLint("CheckResult")
    public Observable<Response<ResponseBody>> getGraphByMonth(String token, int year) {
        return mNetworkInterface.getGraphByMonth(token, year);
    }
    
    @SuppressLint("CheckResult")
    public Observable<Response<ResponseBody>> getGraphByWeek(String token, int year, int month) {
        return mNetworkInterface.getGraphByWeek(token, year, month);
    }
    
    @SuppressLint("CheckResult")
    public Observable<Response<ResponseBody>> getGraphFirstPlaces(String token) {
        return mNetworkInterface.getGraphFirstPlaces(token);
    }
    
    @SuppressLint("CheckResult")
    public Observable<Response<ResponseBody>> deleteRunRecord(String token, int id) {
        return mNetworkInterface.deleteRunRecord(token, id);
    }
    
    @SuppressLint("CheckResult")
    public Observable<Response<ResponseBody>> deleteRaceRecord(String token, int id) {
        return mNetworkInterface.deleteRaceRecord(token, id);
    }

    @SuppressLint("CheckResult")
    public Observable<Response<ResponseBody>> deleteShowRecord(String token, int id) {
        return mNetworkInterface.deleteShowRecord(token, id);
    }
    
    @SuppressLint("CheckResult")
    public Observable<Response<ResponseBody>> getRunDetail(String token, int id) {
        return mNetworkInterface.getRunDetail(token, id);
    }
    
    @SuppressLint("CheckResult")
    public Observable<Response<ResponseBody>> getRaceDetail(String token, int id) {
        return mNetworkInterface.getRaceDetail(token, id);
    }
    
    @SuppressLint("CheckResult")
    public Observable<Response<ResponseBody>> getRaceParticipants(String token, String race_name) {
        return mNetworkInterface.getRaceParticipants(token, race_name);
    }
    
    @SuppressLint("CheckResult")
    public Observable<Response<ResponseBody>> addShoeProfile(AddShoeSendModel model) {
        return mNetworkInterface.addShoeProfile(model);
    }
    
    @SuppressLint("CheckResult")
    public Observable<Response<ResponseBody>> getShoeProfiles(String token) {
        return mNetworkInterface.getShoeProfiles(token);
    }
    
}
