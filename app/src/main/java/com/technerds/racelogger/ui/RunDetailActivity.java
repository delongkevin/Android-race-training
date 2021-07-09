package com.technerds.racelogger.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.technerds.racelogger.Utils.DateFormatter;
import com.technerds.racelogger.Utils.MyShardPreferences;
import com.technerds.racelogger.Utils.StringChecker;
import com.technerds.racelogger.Utils.Urls;
import com.technerds.racelogger.dataModels.runDetailModel.RunDetailModel;
import com.technerds.racelogger.dataModels.shoeMilleage.ShoeMileageModel;
import com.technerds.racelogger.dataModels.shoeModels.getShoeProfiles.GetShoeProfiles;
import com.technerds.racelogger.dataModels.shoeModels.getShoeProfiles.ShoeProfileDataItem;
import com.technerds.racelogger.databinding.ActivityRunDetailBinding;
import com.technerds.racelogger.network.Status;
import com.technerds.racelogger.viewModels.GetRunDetailViewModel;
import com.technerds.racelogger.viewModels.GetShoeMileageRunViewModel;
import com.technerds.racelogger.viewModels.shoeProfiles.GetShoeProfilesViewModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Response;

public class RunDetailActivity extends AppCompatActivity {
    
    ActivityRunDetailBinding binding;
    View view;
    SharedPreferences.Editor editor;
    SharedPreferences mySharedPreference;
    
    GetRunDetailViewModel getRunDetailViewModel;
    RunDetailModel runDetailModel;
    
    GetShoeProfilesViewModel getShoeProfilesViewModel;
    GetShoeProfiles getShoeProfiles;
    List<ShoeProfileDataItem> shoeProfileList = new ArrayList<>();
    
    int run_id = -1;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //  getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setFlags(WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES, WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES);
        //  getWindow().setStatusBarColor(ContextCompat.getColor(this,R.color.white_opac_thirty));
        
        
        binding = ActivityRunDetailBinding.inflate(getLayoutInflater());
        view = binding.getRoot();
        setContentView(view);
        FullScreen();
        setupViewModels();
        init();
    }
    
    public void setupViewModels() {
        getRunDetailViewModel = new ViewModelProvider(this).get(GetRunDetailViewModel.class);
        runDetailObserveViewModel(getRunDetailViewModel);
    
        getShoeProfilesViewModel = new ViewModelProvider(this).get(GetShoeProfilesViewModel.class);
        getShoeProfilesObserveViewModel(getShoeProfilesViewModel);
        
    }
    
    private void init() {
        mySharedPreference = getSharedPreferences(MyShardPreferences.mPreferenceName, MODE_PRIVATE);
        editor = mySharedPreference.edit();
        
        String token = mySharedPreference.getString(MyShardPreferences.token, "");
        run_id = getIntent().getIntExtra("run_id", -1);
        
        if (run_id > 0) {
            binding.progressLayout.setVisibility(View.VISIBLE);
            getRunDetailViewModel.getRunDetail(token, run_id);
        } else {
            finish();
        }
        
    }
    
    private void runDetailObserveViewModel(final GetRunDetailViewModel viewModel) {
        // Observe project data
        viewModel.getResponse().observe(this, apiResponse -> {
            
            if (apiResponse != null) {
                if (apiResponse.status == Status.SUCCESS) {
                    binding.progressLayout.setVisibility(View.GONE);
                    Log.wtf("-this", "Run Detail API Response : " + apiResponse.data);
                    Response<ResponseBody> responseBodyResponse = (Response<ResponseBody>) apiResponse.data;
                    
                    try {
                        if (responseBodyResponse.body() != null) {
                            String json = responseBodyResponse.body().string();
                            Log.wtf("retrofit_", "Run Detail response body: " + json);
                            JSONObject jsonObject = new JSONObject(json);
                            int status = jsonObject.getInt("status");
                            Log.wtf("retrofit_", "status : " + status);
                            
                            if (status == 1) {
                                Gson gson = new Gson();
                                runDetailModel = gson.fromJson(json, RunDetailModel.class);
                                if (runDetailModel.getData() != null)
                                    setValues();
                                
                            } else {
                                String error = jsonObject.getString("error");
                            }
                            
                        }
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                        
                    }
                    
                } else if (apiResponse.status == Status.ERROR) {
                    Log.wtf("-this", "Run Detail API Response : " + apiResponse.error.getMessage());
                    binding.progressLayout.setVisibility(View.GONE);
                }
            }
        });
    }
    
    void setValues() {
        
        String token = mySharedPreference.getString(MyShardPreferences.token, "");
        
        
        if (runDetailModel.getData().getUserShoe() != null) {
            binding.progressLayout.setVisibility(View.VISIBLE);
            getShoeProfilesViewModel.getShoeProfiles(token);
        }
        
        if (StringChecker.checkString(runDetailModel.getData().getCourseType()))
            binding.textViewCourseType.setText("" + runDetailModel.getData().getCourseType());
        
        if (StringChecker.checkString(runDetailModel.getData().getTemperature()))
            binding.textViewTemperature.setText("" + runDetailModel.getData().getTemperature());
        
        if (StringChecker.checkString(runDetailModel.getData().getWeather()))
            binding.textViewWeather.setText("" + runDetailModel.getData().getWeather());
        
        
        String terrain = null;
        
        if (runDetailModel.getData().getRunTerrain() != null) {
            if (runDetailModel.getData().getRunTerrain().size() > 0) {
                for (int i = 0; i < runDetailModel.getData().getRunTerrain().size(); i++) {
                    if (i == 0) {
                        terrain = runDetailModel.getData().getRunTerrain().get(i).getName();
                    } else {
                        terrain = terrain + ", " + runDetailModel.getData().getRunTerrain().get(i).getName();
                    }
                }
            }
        }
        if (terrain != null) {
            binding.textViewTerrain.setText("" + terrain);
        }
    
        if ((!runDetailModel.getData().getHour().equalsIgnoreCase("0"))
                || (!runDetailModel.getData().getMinute().equalsIgnoreCase("0"))
                || (!runDetailModel.getData().getSecond().equalsIgnoreCase("0"))) {
        
            binding.textViewRunTime.setText(runDetailModel.getData().getHour() + ":" +
                    runDetailModel.getData().getMinute() + ":" +
                    runDetailModel.getData().getSecond());
        }
    
        if (StringChecker.checkString(runDetailModel.getData().getUser_distance()))
            binding.textViewDistance.setText("" + runDetailModel.getData().getUser_distance() + "" + runDetailModel.getData().getUnit());
        
        if(runDetailModel.getData().getRun() != null) {
    
            if (StringChecker.checkString(runDetailModel.getData().getRun().getTrainingRoute()))
                binding.textViewTrainingRoute.setText("" + runDetailModel.getData().getRun().getTrainingRoute());
    
            if (StringChecker.checkString(runDetailModel.getData().getRun().getParkName()))
                binding.textViewParkName.setText("" + runDetailModel.getData().getRun().getParkName());
    
            if (StringChecker.checkString(runDetailModel.getData().getDate()))
                binding.textViewDate.setText("" + DateFormatter.getFormattedDateFromDateTimeString(runDetailModel.getData().getDate()));
    
            if (StringChecker.checkString(runDetailModel.getData().getRun().getCity()))
                binding.textViewCity.setText("" + runDetailModel.getData().getRun().getCity());
    
            if (StringChecker.checkString(runDetailModel.getData().getRun().getState()))
                binding.textViewState.setText("" + runDetailModel.getData().getRun().getState());
    
            if (StringChecker.checkString(runDetailModel.getData().getRun().getCountry()))
                binding.textViewCountry.setText("" + runDetailModel.getData().getRun().getCountry());
    
          
    
            if (StringChecker.checkString(runDetailModel.getData().getRun().getStreetAddress()))
                binding.textViewAddress.setText("" + runDetailModel.getData().getRun().getStreetAddress());
    
    
        }
        
        String notes = runDetailModel.getData().getNotes();
        
        if (notes != null) {
            binding.layoutNotesTop.setVisibility(View.VISIBLE);
            binding.textViewNotes.setText("" + notes);
        } else {
            binding.layoutNotesTop.setVisibility(View.GONE);
        }
        
        ArrayList<SlideModel> imageList = new ArrayList<SlideModel>();
        if (runDetailModel.getData().getUserGallery() != null) {
            if (runDetailModel.getData().getUserGallery().size() > 0) {
                if (runDetailModel.getData().getUserGallery().size() == 1) {
                    binding.layoutImage.setVisibility(View.VISIBLE);
                    binding.imageViewSlider.setVisibility(View.VISIBLE);
                    binding.imageSlider.setVisibility(View.GONE);
                    Glide.with(this).load(Urls.imageBaseUrl + "" + runDetailModel.getData().getUserGallery().get(0).getImage()).centerCrop().into(binding.imageViewSlider);
                } else if (runDetailModel.getData().getUserGallery().size() > 1) {
                    for (int i = 0; i < runDetailModel.getData().getUserGallery().size(); i++) {
                        imageList.add(new SlideModel(Urls.imageBaseUrl + "" + runDetailModel.getData().getUserGallery().get(i).getImage(), ScaleTypes.CENTER_CROP));
                    }
                    
                    binding.layoutImage.setVisibility(View.VISIBLE);
                    binding.imageSlider.setVisibility(View.VISIBLE);
                    binding.imageViewSlider.setVisibility(View.GONE);
                    binding.imageSlider.setImageList(imageList, ScaleTypes.CENTER_CROP);
                }
            } else {
                binding.layoutImage.setVisibility(View.GONE);
            }
        } else {
            binding.layoutImage.setVisibility(View.GONE);
        }
        
        
    }
    
    
    public void EditRunClicked(View view) {
        Intent intent = new Intent(this, EditRunActivity.class);
        Gson gson = new Gson();
        String myJson = gson.toJson(runDetailModel);
        intent.putExtra("myjson", myJson);
        startActivityForResult(intent, 7);
    }
    
    private void getShoeProfilesObserveViewModel(final GetShoeProfilesViewModel viewModel) {
        // Observe project data
        viewModel.getResponse().observe(this, apiResponse -> {
            
            if (apiResponse != null) {
                if (apiResponse.status == Status.SUCCESS) {
                    binding.progressLayout.setVisibility(View.GONE);
                    Log.wtf("-this", "Get Shoe Profiles Response : " + apiResponse.data);
                    Response<ResponseBody> responseBodyResponse = (Response<ResponseBody>) apiResponse.data;
                    
                    try {
                        if (responseBodyResponse.body() != null) {
                            String json = responseBodyResponse.body().string();
                            Log.wtf("retrofit_", "Get Shoe Profiles response body: " + json);
                            JSONObject jsonObject = new JSONObject(json);
                            int status = jsonObject.getInt("status");
                            if (status == 1) {
                                Gson gson = new Gson();
                                Log.wtf("-this", "96 Get Shoe Profiles");
                                getShoeProfiles = gson.fromJson(json, GetShoeProfiles.class);
                                if (getShoeProfiles != null) {
                                    Log.wtf("-this", "99 IF Get Shoe Profiles");
                                    shoeProfileList = getShoeProfiles.getData();
                                    setShoeProfile();
                                }
                                
                            } else {
                                
                                String error = jsonObject.getString("error");
                                Snackbar.make(view, "" + error, Snackbar.LENGTH_SHORT).show();
                                Log.wtf("retrofit_", "Get Shoe Profiles response body: " + error);
                                
                            }
                        }
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                        
                    }
                    
                } else if (apiResponse.status == Status.ERROR) {
                    Log.wtf("-this", "Get Shoe Profiles Response : " + apiResponse.error.getMessage());
                    binding.progressLayout.setVisibility(View.GONE);
                }
            } else {
                binding.progressLayout.setVisibility(View.GONE);
            }
        });
    }
    
    private void setShoeProfile() {
        if(shoeProfileList != null && shoeProfileList.size() > 0 ){
            String prof_id = runDetailModel.getData().getUserShoe().getShoes_profile_id();
            if(StringChecker.checkString(prof_id)){
                int id = Integer.parseInt(prof_id);
                for (int i = 0; i < shoeProfileList.size(); i++){
                    if(id ==shoeProfileList.get(i).getId()){
                        binding.textViewShoeBrand.setText(shoeProfileList.get(i).getShoeProfile()+"");
                        binding.textViewShoeMileage.setText(shoeProfileList.get(i).getShoeMileage()+"");
                    }
                }
            }
        }
    }
    
    public void dummyClick(View view) {
    }
    
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        
    }
    
    public void backClicked(View view) {
        onBackPressed();
    }
    
    public void FullScreen() {
        final View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                        | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        
        decorView.setOnSystemUiVisibilityChangeListener
                (new View.OnSystemUiVisibilityChangeListener() {
                    @Override
                    public void onSystemUiVisibilityChange(int visibility) {
                        // Note that system bars will only be "visible" if none of the
                        // LOW_PROFILE, HIDE_NAVIGATION, or FULLSCREEN flags are set.
                        if ((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0) {
                            // TODO: The system bars are visible. Make any desired
//                                int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
//
//                            int a=SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
//                                decorView.setSystemUiVisibility(a);
                            decorView.setSystemUiVisibility(
                                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                                            | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
                            
                            
                        } else {
                            // TODO: The system bars are NOT visible. Make any desired
                            
                        }
                    }
                });
    }
    
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                        | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        
    }
    
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 7) {
            if (data != null) {
                boolean done = data.getBooleanExtra("Done", false);
                Log.wtf("-this", " OnActivity result Done : " + done);
                
                if (done) {
                    String token = mySharedPreference.getString(MyShardPreferences.token, "");
                    
                    if (run_id > 0) {
                        binding.progressLayout.setVisibility(View.VISIBLE);
                        getRunDetailViewModel.getRunDetail(token, run_id);
                    } else {
                        finish();
                    }
                }
            }
        }
    }
    
    
}