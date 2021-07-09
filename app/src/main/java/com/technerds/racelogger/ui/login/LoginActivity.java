package com.technerds.racelogger.ui.login;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;
import com.technerds.racelogger.dataModels.SignInSendModel;
import com.technerds.racelogger.dataModels.signupModel.SignupRecieveModel;
import com.technerds.racelogger.network.Status;
import com.technerds.racelogger.R;
import com.technerds.racelogger.Utils.AppUtils;
import com.technerds.racelogger.Utils.MyShardPreferences;
import com.technerds.racelogger.viewModels.authentication.SigninViewModel;
import com.technerds.racelogger.databinding.ActivityLoginBinding;
import com.technerds.racelogger.ui.HomeActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.TimeZone;

import okhttp3.ResponseBody;
import retrofit2.Response;


public class LoginActivity extends AppCompatActivity implements LocationListener {
    ActivityLoginBinding binding;
    View view;
    SigninViewModel signinViewModel;
    SharedPreferences.Editor editor;
    SharedPreferences mySharedPreference;

    String fcm_token;
    String timeZoneS = "Asia";
    FusedLocationProviderClient mFusedLocationClient;
    Location mLastLocation = null;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;
    LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //  getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setFlags(WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES, WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        view = binding.getRoot();
        setContentView(view);
        FullScreen();
        setupViewModels();
        init();
    }

    public void setupViewModels() {
        signinViewModel = new ViewModelProvider(this).get(SigninViewModel.class);
        signInObserveViewModel(signinViewModel);

    }

    private void init() {
        mySharedPreference = getSharedPreferences(MyShardPreferences.mPreferenceName, MODE_PRIVATE);
        editor = mySharedPreference.edit();

        TimeZone timeZone = TimeZone.getDefault();
        timeZoneS = timeZone.getID().toString();
        fcm_token = FirebaseInstanceId.getInstance().getToken();
        Log.wtf("-this", "FCM Token : " + fcm_token);


        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(20 * 1000);
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    if (location != null) {
                        mLastLocation = location;
                        Log.wtf("-GPS", "locationCallback 130 : " + mLastLocation.getLatitude() + "," + mLastLocation.getLongitude());

                        if (mFusedLocationClient != null) {
                            mFusedLocationClient.removeLocationUpdates(locationCallback);
                        }
                    }
                }
            }
        };
        getLocationNew(this);

    }


    public void LoginClicked(View view) {
        hideKeyBoardClicked(view);

        String email = binding.editTextEmail.getText().toString();
        String password = binding.editTextPassword.getText().toString();

        boolean allCorrect = true;
        boolean locCorrect = true;
        boolean fcmCorrect = true;

        double lat = 0.0;
        double lng = 0.0;
        if (mLastLocation != null) {
            lat = mLastLocation.getLatitude();
            lng = mLastLocation.getLongitude();
        } else {
            getLocationNew(this);
            locCorrect = false;
        }

        if (email != null && (!email.equalsIgnoreCase("")) && email != "") {
            binding.cardViewEmail.setStrokeColor(getResources().getColor(R.color.colorPrimary));
        } else {
            allCorrect = false;
            binding.cardViewEmail.setStrokeColor(getResources().getColor(R.color.stock_red));
        }

        if (password != null && (!password.equalsIgnoreCase("")) && password != "") {
            binding.cardViewPassword.setStrokeColor(getResources().getColor(R.color.colorPrimary));
        } else {
            allCorrect = false;
            binding.cardViewPassword.setStrokeColor(getResources().getColor(R.color.stock_red));
        }
    
        if (fcm_token != null && (!fcm_token.equalsIgnoreCase("")) && fcm_token != "") {
        
        } else {
            fcmCorrect = false;
        }

        if (mLastLocation == null) {
            Log.wtf("-GPS", "getLocation 306 Null ");

            getLocationNew(this);
            if (AppUtils.isLocationEnabled(this)) {
                binding.progressLayout.setVisibility(View.VISIBLE);
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        binding.progressLayout.setVisibility(View.GONE);
                        if (mLastLocation != null) {
                            Log.wtf("-GPS", "getLocation 281 : " + mLastLocation.getLatitude() + "," + mLastLocation.getLongitude());
                            SignUpClicked(view);
                        } else {
                            Log.wtf("-GPS", "getLocation 284 : " + mLastLocation);
                            Snackbar.make(view, "Please Make sure Location is Enabled or Try Disable and Enable it again.", Snackbar.LENGTH_SHORT).show();
                        }
                        // emailLoginClicked(view);
                    }
                }, 3000);

                return;
            }
        } else {
            locCorrect = true;
        }


        if (allCorrect && locCorrect && fcmCorrect) {
           // Snackbar.make(view, "All Correct", Snackbar.LENGTH_SHORT).show();

            if (mLastLocation != null) {
                lat = mLastLocation.getLatitude();
                lng = mLastLocation.getLongitude();
            }

            SignInSendModel model = new SignInSendModel();
            model.setEmail(email);
            model.setLogin_type("Email");
            model.setLatitude(lat);
            model.setLongitude(lng);
            model.setPassword(password);
            model.setFcm_token(fcm_token);
            model.setTime_zone(timeZoneS);

            binding.progressLayout.setVisibility(View.VISIBLE);
            signinViewModel.login(model);

        } else {
            if(fcmCorrect == false){
                Snackbar.make(view, "Connection Error.Please make sure you are connected to the internet.", Snackbar.LENGTH_SHORT).show();
                fcm_token = FirebaseInstanceId.getInstance().getToken();
            } else  if(locCorrect == false){
                Snackbar.make(view, "Cannot find location. Please Turn on the GPS.", Snackbar.LENGTH_SHORT).show();
    
            }  else {
                Snackbar.make(view, "Fill all required fields", Snackbar.LENGTH_SHORT).show();
            }
        }

    }


    private void signInObserveViewModel(final SigninViewModel viewModel) {
        // Observe project data
        viewModel.getResponse().observe(this, apiResponse -> {

            if (apiResponse != null) {
                if (apiResponse.status == Status.SUCCESS) {

                    Log.wtf("-this", "Signin API Response : " + apiResponse.data);
                    Response<ResponseBody> responseBodyResponse = (Response<ResponseBody>) apiResponse.data;
                    binding.progressLayout.setVisibility(View.GONE);
                    try {
                        if (responseBodyResponse.body() != null) {
                            String json = responseBodyResponse.body().string();
                            Log.wtf("retrofit_", "Signin response body: " + json);
                            JSONObject jsonObject = new JSONObject(json);
                            int status = jsonObject.getInt("status");
                            Log.wtf("retrofit_", "status : " + status);
                            if (status == 1) {
                                Gson gson = new Gson();
                                SignupRecieveModel model = gson.fromJson(json, SignupRecieveModel.class);
                                editor.putInt(MyShardPreferences.UserID,model.getData().getUser().getId()).commit();
                                editor.putString(MyShardPreferences.token,model.getData().getToken()).commit();
                                editor.putString(MyShardPreferences.first_name,model.getData().getUser().getFirstName()).commit();
                                editor.putString(MyShardPreferences.last_name,model.getData().getUser().getLastName()).commit();
                                editor.putString(MyShardPreferences.UserEmail,model.getData().getUser().getEmail()).commit();
                                editor.putString(MyShardPreferences.DOB,model.getData().getUser().getDob()).commit();

                                startActivity(new Intent(this, HomeActivity.class));
                                finish();

                              // binding.editTextEmail.setText(model.getData().getToken());


                            } else {
                                String error = jsonObject.getString("error");
                                Snackbar.make(view,error,Snackbar.LENGTH_SHORT).show();
                                

                            }
                        } else {
                            Log.wtf("-this", "Signin Response : Null");
                        }


                    } catch (IOException | JSONException e) {
                        e.printStackTrace();

                    }

                } else if (apiResponse.status == Status.ERROR) {
                    Log.wtf("-this", "Signin API Response : " + apiResponse.error.getMessage());
                    binding.progressLayout.setVisibility(View.GONE);
                }
            }
        });
    }

    public void hideKeyBoardClicked(View view) {
        binding.editTextEmail.clearFocus();
        binding.editTextPassword.clearFocus();


        InputMethodManager imm = (InputMethodManager) getApplicationContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

    }

    public void SignUpClicked(View view) {
        startActivity(new Intent(this, SignupActivity.class));
        //  finish();
    }
    
    public void forgotPasswordClicked(View view) {
        startActivity(new Intent(this, ForgotPasswordActivity.class));
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


    //Location

    @Override
    public void onLocationChanged(Location mlocation) {
        Log.wtf("-this", "Location Changed : " + mlocation);
        if (mlocation != null) {
            locationManager.removeUpdates(this);
            getLocationNew(this);
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.wtf("-this", "Status Changed : " + status);

    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.wtf("-this", "Provider Enabled: " + provider);
        getLocationNew(this);

    }

    @Override
    public void onProviderDisabled(String provider) {


    }

    void getLocationNew(Context context) {
        if (!AppUtils.isLocationEnabled(context)) {
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            View promptView = layoutInflater.inflate(R.layout.prompt, null);
            TextView textViewMain = (TextView) promptView.findViewById(R.id.textViewMain);
            Button btnPos = (Button) promptView.findViewById(R.id.buttonOpenSettings);
            final AlertDialog.Builder dialog = new AlertDialog.Builder(context);
            dialog.setCancelable(true);

            dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
                @Override
                public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        dialog.dismiss();
                        return true;
                    }
                    return false;
                }
            });
            btnPos.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(myIntent);
                }
            });
            dialog.setView(promptView);
            dialog.create();
            dialog.show();
        } else {
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                return;
            }
            mFusedLocationClient.getLastLocation()
                    .addOnSuccessListener((Activity) context, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            // Got last known location. In some rare situations, this can be null.
                            if (location != null) {
                                // Logic to handle location object
                                mLastLocation = location;
                                Log.wtf("-GPS", "getLocation 169 : " + mLastLocation.getLatitude() + "," + mLastLocation.getLongitude());

                            } else {
                                Log.wtf("-GPS", "getLocation 171 : " + mLastLocation);

                            }
                        }
                    });

            if (mLastLocation != null) {
                Log.wtf("-this", "Location : " + mLastLocation.getLatitude() + "," + mLastLocation.getLongitude());
            } else {
                Criteria criteria = new Criteria();
                criteria.setAccuracy(Criteria.ACCURACY_COARSE);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (context.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                            && context.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                        return;
                    }
                }
                locationManager.requestSingleUpdate(criteria, this, null);
                mLastLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                if (mLastLocation != null) {
                    Log.wtf("-this", "Location ELSE : " + mLastLocation.getLatitude() + "," + mLastLocation.getLongitude());
                }
            }

        }
    }
    
    
   
}