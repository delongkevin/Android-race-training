package com.technerds.racelogger.network;

import com.technerds.racelogger.dataModels.EditProfileSendModel;
import com.technerds.racelogger.dataModels.SignInSendModel;
import com.technerds.racelogger.dataModels.SignUpSendModel;
import com.technerds.racelogger.dataModels.SortedRaceListingSendModel;
import com.technerds.racelogger.dataModels.addRaceModel.AddRaceSendModel;
import com.technerds.racelogger.dataModels.addRunModel.AddRunSendModel;
import com.technerds.racelogger.dataModels.shoeModels.AddShoeSendModel;
import com.technerds.racelogger.dataModels.updateRaceModel.UpdateRaceSendModel;
import com.technerds.racelogger.dataModels.updateRunModel.UpdateRunSendModel;

import io.reactivex.Observable;


import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface NetworkInterface {
    
    
    @POST("weather")
    Observable<Response<ResponseBody>> getWeatherByZip(
            @Query("APPID") String APPID,
            @Query("zip") String zip
    
    );
    
    @POST("weather")
    Observable<Response<ResponseBody>> getWeatherByCoord(
            @Query("APPID") String APPID,
            @Query("lat") double lat,
            @Query("lon") double lon
    
    );
    
    @Multipart
    @POST("upload-file")
    Observable<Response<ResponseBody>> uploadFile(@Part MultipartBody.Part image);
    
    @POST("signin")
    Observable<Response<ResponseBody>> login(@Body SignInSendModel model);
    
    @POST("signup")
    Observable<Response<ResponseBody>> signup(@Body SignUpSendModel model);
    
    @POST("forget-password")
    Observable<Response<ResponseBody>> forgotPassword(@Query("email") String email);
    
    @POST("verify-code")
    Observable<Response<ResponseBody>> verifyCode(@Query("forget_token") String forget_token);
    
    
    @POST("reset-password")
    Observable<Response<ResponseBody>> resetPassword(@Query("forget_token") String forget_token,
                                                     @Query("password") String password,
                                                     @Query("password_confirmation") String password_confirmation
    );
    
    @POST("update-user-profile")
    Observable<Response<ResponseBody>> updateProfile(@Body EditProfileSendModel model);
    
    @POST("add-race-record")
    Observable<Response<ResponseBody>> addRace(@Body AddRaceSendModel model);
    
    @POST("add-run-record")
    Observable<Response<ResponseBody>> addRun(@Body AddRunSendModel model);
    
    @POST("update-race-record")
    Observable<Response<ResponseBody>> updateRace(@Body UpdateRaceSendModel model);
    
    @POST("update-run-record")
    Observable<Response<ResponseBody>> updateRun(@Body UpdateRunSendModel model);
    
    @GET("get-dropdown-data")
    Observable<Response<ResponseBody>> getDropDown();
    
    @POST("get-user-profile")
    Observable<Response<ResponseBody>> getProfile(@Query("token") String api_token);
    
    
    @POST("get-shoe-mileage-run")
    Observable<Response<ResponseBody>> getShoeMileageRun(
            @Query("token") String api_token,
            @Query("brand_name") String brand_name,
            @Query("model") String model
    
    );
    
    
    @POST("user-dashboard")
    Observable<Response<ResponseBody>> getUserDashboard(@Query("token") String token);
    
    @POST("get-race-listing")
    Observable<Response<ResponseBody>> getRaceListing(@Query("token") String token,
                                                      @Query("page") int page);
    
    @POST("get-race-listing")
    Observable<Response<ResponseBody>> getRaceListing(@Query("token") String token,
                                                      @Query("page") int page,
                                                      @Query("year") int year,
                                                      @Query("month") int month
    
    );
    
    @POST("sort-race-listing")
    Observable<Response<ResponseBody>> getSortedRaceListing(@Body SortedRaceListingSendModel model);
    
    @POST("get-run-listing")
    Observable<Response<ResponseBody>> getRunListing(@Query("token") String token,
                                                     @Query("page") int page);
    
    @POST("search-race-name")
    Observable<Response<ResponseBody>> searchRaceName(@Query("token") String token);
    
    @POST("search-run-name")
    Observable<Response<ResponseBody>> searchRunName(@Query("token") String token);
    
    @POST("get-graph-by-year")
    Observable<Response<ResponseBody>> getGraphByYear(@Query("token") String token);
    
    @POST("get-graph-by-month")
    Observable<Response<ResponseBody>> getGraphByMonth(@Query("token") String token,
                                                       @Query("year") int year);
    
    @POST("get-graph-by-week")
    Observable<Response<ResponseBody>> getGraphByWeek(@Query("token") String token,
                                                      @Query("year") int year,
                                                      @Query("month") int month
    
    );
    
    @POST("get-first-place")
    Observable<Response<ResponseBody>> getGraphFirstPlaces(@Query("token") String token);
    
    @POST("delete-run-record")
    Observable<Response<ResponseBody>> deleteRunRecord(@Query("token") String token,
                                                       @Query("id") int id);
    
    @POST("delete-race-record")
    Observable<Response<ResponseBody>> deleteRaceRecord(@Query("token") String token,
                                                        @Query("id") int id);

    @POST("delete-shoe-profile")
    Observable<Response<ResponseBody>> deleteShowRecord(@Query("token") String token,
                                                        @Query("id") int id);


    
    @POST("get-run-detail")
    Observable<Response<ResponseBody>> getRunDetail(@Query("token") String token,
                                                    @Query("id") int id);
    
    @POST("get-race-detail")
    Observable<Response<ResponseBody>> getRaceDetail(@Query("token") String token,
                                                     @Query("id") int id);
    
    @POST("get-race-participant")
    Observable<Response<ResponseBody>> getRaceParticipants(@Query("token") String token,
                                                           @Query("race_name") String race_name);
    
    @POST("add-shoe-profile")
    Observable<Response<ResponseBody>> addShoeProfile(@Body AddShoeSendModel model);
    
    @POST("get-shoe-profile")
    Observable<Response<ResponseBody>> getShoeProfiles(@Query("token") String token);
    
}
