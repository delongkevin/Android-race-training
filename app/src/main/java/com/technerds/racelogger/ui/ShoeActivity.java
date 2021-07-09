package com.technerds.racelogger.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.technerds.racelogger.Utils.AppUtils;
import com.technerds.racelogger.Utils.MyShardPreferences;
import com.technerds.racelogger.adapters.ShoeAdapter;
import com.technerds.racelogger.dataModels.shoeModels.getShoeProfiles.GetShoeProfiles;
import com.technerds.racelogger.dataModels.shoeModels.getShoeProfiles.ShoeProfileDataItem;
import com.technerds.racelogger.databinding.ActivityShoeBinding;
import com.technerds.racelogger.network.Status;
import com.technerds.racelogger.viewModels.DeleteShowViewModel;
import com.technerds.racelogger.viewModels.shoeProfiles.GetShoeProfilesViewModel;
import com.tsuryo.swipeablerv.SwipeLeftRightCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Response;

public class ShoeActivity extends AppCompatActivity {

    ActivityShoeBinding binding;
    View view;

    SharedPreferences.Editor editor;
    SharedPreferences mySharedPreference;

    GetShoeProfilesViewModel getShoeProfilesViewModel;
    GetShoeProfiles getShoeProfiles;
    ShoeAdapter adapter;
    List<ShoeProfileDataItem> shoeProfileList = new ArrayList<>();


    /*Khalil Changes*/

    DeleteShowViewModel deleteShowViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES, WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES);

        binding = ActivityShoeBinding.inflate(getLayoutInflater());
        view = binding.getRoot();
        setContentView(view);
        FullScreen();
        setupViewModels();
        init();
    }

    public void setupViewModels() {

        getShoeProfilesViewModel = new ViewModelProvider(this).get(GetShoeProfilesViewModel.class);
        getShoeProfilesObserveViewModel(getShoeProfilesViewModel);

        deleteShowViewModel = new ViewModelProvider(this).get(DeleteShowViewModel.class);
        deleteRaceRecordObserveViewModel(deleteShowViewModel);
    }

    private void init() {
        mySharedPreference = getSharedPreferences(MyShardPreferences.mPreferenceName, MODE_PRIVATE);
        editor = mySharedPreference.edit();
        String token = mySharedPreference.getString(MyShardPreferences.token, "");

        binding.progressLayout.setVisibility(View.VISIBLE);
        getShoeProfilesViewModel.getShoeProfiles(token);


       // Toast.makeText(this, "Calling this"+token, Toast.LENGTH_SHORT).show();


    }

    private void getShoeProfilesObserveViewModel(final GetShoeProfilesViewModel viewModel) {
        // Observe project data
        viewModel.getResponse().observe(this, apiResponse -> {

            if (apiResponse != null) {
                if (apiResponse.status == Status.SUCCESS) {

                   // Toast.makeText(this, "responce found:", Toast.LENGTH_SHORT).show();

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
                                    setupAdapter();
                                   // Toast.makeText(this, "Size:"+String.valueOf(shoeProfileList.size()), Toast.LENGTH_SHORT).show();

                                    //   Toast.makeText(this, "data fetched", Toast.LENGTH_SHORT).show();
                                }

                            } else {

                                String error = jsonObject.getString("error");
                                Snackbar.make(view, "" + error, Snackbar.LENGTH_SHORT).show();
                                Log.wtf("retrofit_", "Get Shoe Profiles response body: " + error);

                            }
                        }
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();

                      //  Toast.makeText(this, "exception:"+e.getMessage(), Toast.LENGTH_SHORT).show();

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


    void setupAdapter() {
        binding.recycler.removeAllViews();
        binding.recycler.setAdapter(null);

        binding.recycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        Log.wtf("-this", "138 shoeProfileList Size : " + shoeProfileList.size());
        if (shoeProfileList.size() > 0) {
            adapter = new ShoeAdapter(this, shoeProfileList, this);
            binding.recycler.setAdapter(adapter);


        }


        binding.recycler.setListener(new SwipeLeftRightCallback.Listener() {
            @Override
            public void onSwipedLeft(int position) {

                Log.wtf("-conver", " Position : " + position);
                //Log.wtf("-conver", " race_id : " + raceList.get(position).getId());

                //   Toast.makeText(ShoeActivity.this, String.valueOf(shoeProfileList.get(position).getId()), Toast.LENGTH_SHORT).show();
                binding.progressLayout.setVisibility(View.VISIBLE);
                deleteShowViewModel.deleteShowRecord(mySharedPreference.getString(MyShardPreferences.token, ""),
                        shoeProfileList.get(position).getId());

                shoeProfileList.remove(position);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onSwipedRight(int position) {
            }
        });
    }

    public void AddShoeClicked(View view) {
        Intent intent = new Intent(this, AddShoeActivity.class);
        startActivityForResult(intent, 7);
        startActivity(intent);
        finish();
    }

    public void editShoeClicked(ShoeProfileDataItem model) {
//        Intent intent = new Intent(this, ClinicApplicationDetailActivity.class);
//        Gson gson = new Gson();
//        String myJson = gson.toJson(model);
//        intent.putExtra("myjson", myJson);
//        intent.putExtra("source", "activeJobs");
//        startActivity(intent);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Toast.makeText(this, "requestcode:"+requestCode, Toast.LENGTH_SHORT).show();
        if (requestCode == 7) {
            if (data != null) {
                boolean done = data.getBooleanExtra("Done", false);
                Log.wtf("-this", " OnActivity result Done : " + done);

                if (done) {
                    // if(shoeProfileList.size()>0){
                    setupViewModels();
                   init();

                    //}


                }
            }
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        /*Intent intent = new Intent();
        intent.putExtra("Done", false);
        setResult(7, intent);
        finish();//finishing activity*/
        AppUtils.ShowShoeActivity=true;
         Intent intent = new Intent(ShoeActivity.this,HomeActivity.class);
         startActivity(intent);
       // intent.putExtra("Done", false);
     //   setResult(7, intent);
        finish();//finishing activity


    }

    public void backClicked(View view) {
       /* onBackPressed();*/
        AppUtils.ShowShoeActivity=true;
        Intent intent = new Intent(ShoeActivity.this,HomeActivity.class);
        startActivity(intent);

        finish();//finishing activity
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

    public void dummyClick(View view) {
    }


    ///////////My Changes////////

    private void deleteRaceRecordObserveViewModel(DeleteShowViewModel viewModel) {
        // Observe project data
        /*.getViewLifecycleOwner()*/
        viewModel.getResponse().observe(this, apiResponse -> {

            if (apiResponse != null) {
                if (apiResponse.status == Status.SUCCESS) {
                    binding.progressLayout.setVisibility(View.GONE);

                    Log.wtf("-this", "Delete Race API Response : " + apiResponse.data);
                    Response<ResponseBody> responseBodyResponse = (Response<ResponseBody>) apiResponse.data;

                    try {
                        String json = responseBodyResponse.body().string();
                        Log.wtf("retrofit_", "Delete Race response body: " + json);
                        JSONObject jsonObject = new JSONObject(json);

                        if (jsonObject.getInt("status") == 1) {

                        } else {

                        }

                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }


                    binding.progressLayout.setVisibility(View.GONE);

                } else if (apiResponse.status == Status.ERROR) {
                    Log.wtf("-this", "Delete Race API Response : " + apiResponse.error.getMessage());
                    binding.progressLayout.setVisibility(View.GONE);
                }
                binding.progressLayout.setVisibility(View.GONE);
            }
        });
    }
}