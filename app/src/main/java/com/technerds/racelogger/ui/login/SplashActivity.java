package com.technerds.racelogger.ui.login;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ViewModelProvider;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.technerds.racelogger.dataModels.getProfileModel.GetProfileModel;
import com.technerds.racelogger.network.Status;
import com.technerds.racelogger.Utils.MyShardPreferences;
import com.technerds.racelogger.viewModels.GetProfileViewModel;
import com.technerds.racelogger.databinding.ActivitySplashBinding;
import com.technerds.racelogger.ui.HomeActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Calendar;

import okhttp3.ResponseBody;
import retrofit2.Response;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class SplashActivity extends AppCompatActivity {
    ActivitySplashBinding binding;
    View view;
    private static final int PERMISSION_REQUEST_CODE = 200;
    GetProfileViewModel getProfileViewModel;
    SharedPreferences.Editor editor;
    SharedPreferences mySharedPreference;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //  getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setFlags(WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES, WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES);
        binding = ActivitySplashBinding.inflate(getLayoutInflater());
        view = binding.getRoot();
        setContentView(view);
        FullScreen();
        setupViewModels();
        mySharedPreference = getSharedPreferences(MyShardPreferences.mPreferenceName, MODE_PRIVATE);
        editor = mySharedPreference.edit();
//        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
//        Log.wtf("-Date"," Current year : "+currentYear);
        
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                 ActivityCompat.requestPermissions(SplashActivity.this, new String[]{ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION, READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE, CAMERA}, PERMISSION_REQUEST_CODE);
            }
        }, 100);
    }
    
    public void setupViewModels() {
        getProfileViewModel = new ViewModelProvider(this).get(GetProfileViewModel.class);
        getProfileObserveViewModel(getProfileViewModel);
        
    }
    
    void callNext() {
        
        String api_token = mySharedPreference.getString(MyShardPreferences.token, null);
//        String api_token = "ifwF4U4ooUIn551cb15F88n6l9flQWHxRRXVW63o1601632593";
//        editor.putString(MyShardPreferences.token, api_token).commit();
        Log.wtf("-this", "Splash Token : " + api_token);

//        startActivity(new Intent(SplashActivity.this, HomeActivity.class));
//        finish();
        
        if (api_token != null) {
            getProfileViewModel.getProfile(api_token);
        } else {
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                    finish();
                }
            }, 1000);
        }
        
    }
    
    private void getProfileObserveViewModel(GetProfileViewModel viewModel) {
        // Observe project data
        viewModel.getResponse().observe(this, apiResponse -> {
            
            if (apiResponse != null) {
                if (apiResponse.status == Status.SUCCESS) {
                    
                    Log.wtf("-this", "Validate APIToken response API Response : " + apiResponse.data);
                    Response<ResponseBody> responseBodyResponse = (Response<ResponseBody>) apiResponse.data;
                    try {
                        if (responseBodyResponse.body() != null) {
                            String json = responseBodyResponse.body().string();
                            Log.wtf("retrofit_", "Validate APIToken response body: " + json);
                            JSONObject jsonObject = new JSONObject(json);
                            int status = jsonObject.getInt("status");
                            GetProfileModel model = null;
                            if (status == 1) {
                                Gson gson = new Gson();
                                model = gson.fromJson(json, GetProfileModel.class);
                                
                                if (model != null) {
                                    
                                    editor.putInt(MyShardPreferences.UserID, model.getData().getId()).commit();
                                    editor.putString(MyShardPreferences.first_name, model.getData().getFirstName()).commit();
                                    editor.putString(MyShardPreferences.last_name, model.getData().getLastName()).commit();
                                    editor.putString(MyShardPreferences.UserEmail, model.getData().getEmail()).commit();
                                    editor.putString(MyShardPreferences.DOB,model.getData().getDob()).commit();
                                    editor.commit();
                                    
                                    Snackbar.make(view, "" + model.getMessage(), Snackbar.LENGTH_SHORT).show();
                                    
                                    Intent intent = new Intent(this, HomeActivity.class);
                                    startActivity(intent);
                                    finish();
                                    
                                } else {
                                    Log.wtf("-this", "Validate APIToken Response : Null");
                                    Snackbar.make(view, "Invalid email and/or password", Snackbar.LENGTH_SHORT).show();
                                    Intent intent = new Intent(this, LoginActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                                
                            } else {
                                String error = jsonObject.getString("error");
                                Snackbar.make(view, "" + error, Snackbar.LENGTH_SHORT).show();
                                Log.wtf("retrofit_", "Validate APIToken response body: " + error);
                                mySharedPreference.edit().clear().apply();
                                Intent intent = new Intent(this, LoginActivity.class);
                                startActivity(intent);
                                finish();
                                
                            }
                            
                        }
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                        
                    }
                    
                } else if (apiResponse.status == Status.ERROR) {
                    Log.wtf("-this", "Validate APIToken response API Response : " + apiResponse.error.getMessage());
                }
            }
        });
    }
    
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0) {
                    Log.wtf("-permi", "171");
                    boolean locationAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean locationAccepted1 = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    boolean readEXt = grantResults[2] == PackageManager.PERMISSION_GRANTED;
                    boolean writeExt = grantResults[3] == PackageManager.PERMISSION_GRANTED;
                    boolean camera = grantResults[4] == PackageManager.PERMISSION_GRANTED;
                    
                    
                    if (locationAccepted && locationAccepted1) {
                        Log.wtf("-permi", "180");
                        if (readEXt && writeExt && camera) {
                            Log.wtf("-permi", "182");
                            
                            callNext();
                            
                        }
                        //  Log.wtf("-permi", "187");
                        
                    } else {
                        //  Log.wtf("-permi", "190");
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            Log.wtf("-permi", "192");
                            if (shouldShowRequestPermissionRationale(ACCESS_FINE_LOCATION)) {
                                Log.wtf("-permi", "194");
                                showMessageOKCancel("You need to allow the permissions",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                    requestPermissions(new String[]{ACCESS_COARSE_LOCATION, ACCESS_FINE_LOCATION, READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE, CAMERA},
                                                            PERMISSION_REQUEST_CODE);
                                                    Log.wtf("-permi", "202");
                                                    
                                                }
                                            }
                                        });
                                Log.wtf("-permi", "207");
                                return;
                            }
                            Log.wtf("-permi", "210");
                            //
                            
                        }
                        //   Log.wtf("-permi", "214");
                        
                    }
                    //  Log.wtf("-permi", "217");
                } else {
                    Log.wtf("-permi", "219");
                    //  callNext();
                }
                // Log.wtf("-permi", "222");
                break;
        }
        //   Log.wtf("-permi", "225");
    }
    
    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(SplashActivity.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", okListener)
                .create()
                .show();
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
}