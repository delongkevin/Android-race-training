package com.technerds.racelogger.viewModels.graphs;

import android.annotation.SuppressLint;
import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.technerds.racelogger.network.ApiResponse;
import com.technerds.racelogger.network.NetworkRepository;

import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Response;

public class GetRaceParticipantsViewModel extends AndroidViewModel {
    private MutableLiveData<ApiResponse> apiResponseMutableLiveData;
    private final CompositeDisposable mDisposables = new CompositeDisposable();

    public GetRaceParticipantsViewModel(@NonNull Application application) {
        super(application);
    }
    
    public LiveData<ApiResponse> getResponse() {
        if (apiResponseMutableLiveData == null) {
            apiResponseMutableLiveData = new MutableLiveData<>();
        }
        return apiResponseMutableLiveData;
    }
    
    @SuppressLint("CheckResult")
    public void getRaceParticipants(String token, String race_name) {
        mDisposables.add(NetworkRepository.getInstance().getRaceParticipants(token, race_name)
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
