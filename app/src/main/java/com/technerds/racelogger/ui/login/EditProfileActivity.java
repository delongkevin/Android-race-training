package com.technerds.racelogger.ui.login;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.technerds.racelogger.Utils.DateFormatter;
import com.technerds.racelogger.network.Status;
import com.technerds.racelogger.R;
import com.technerds.racelogger.Utils.AppUtils;
import com.technerds.racelogger.Utils.FileUtils;
import com.technerds.racelogger.Utils.MyShardPreferences;
import com.technerds.racelogger.Utils.Urls;
import com.technerds.racelogger.viewModels.EditProfileViewModel;
import com.technerds.racelogger.viewModels.GetProfileViewModel;
import com.technerds.racelogger.viewModels.UploadFileViewModel;
import com.technerds.racelogger.dataModels.EditProfileSendModel;
import com.technerds.racelogger.dataModels.UploadFileModel;
import com.technerds.racelogger.dataModels.editProfileModel.EditProfileModel;
import com.technerds.racelogger.dataModels.getProfileModel.GetProfileModel;
import com.technerds.racelogger.databinding.ActivityEditProfileBinding;
import com.technerds.racelogger.ui.CameraActivity;
import com.vikktorn.picker.City;
import com.vikktorn.picker.CityPicker;
import com.vikktorn.picker.Country;
import com.vikktorn.picker.CountryPicker;
import com.vikktorn.picker.OnCityPickerListener;
import com.vikktorn.picker.OnCountryPickerListener;
import com.vikktorn.picker.OnStatePickerListener;
import com.vikktorn.picker.State;
import com.vikktorn.picker.StatePicker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import me.shaohui.advancedluban.Luban;
import me.shaohui.advancedluban.OnCompressListener;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Response;

public class EditProfileActivity extends AppCompatActivity implements OnCompressListener, LocationListener, OnStatePickerListener, OnCountryPickerListener, OnCityPickerListener {
    private static final int CHOSE_IMAGE_FROM_GALLERY_REQUEST_CODE = 101;
    private static final int Camera_Result_Act = 2;
    
    FusedLocationProviderClient mFusedLocationClient;
    Location mLastLocation = null;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;
    LocationManager locationManager;
    
    ActivityEditProfileBinding binding;
    View view;
    SharedPreferences.Editor editor;
    SharedPreferences mySharedPreference;
    UploadFileViewModel uploadFileViewModel;
    EditProfileViewModel editProfileViewModel;
    GetProfileViewModel getProfileViewModel;
    GetProfileModel getProfileModel;
    String fcm_token;
    String timeZoneS = "Asia";
    String profilePicURL = null;
    String profilePicName = null;
    String gender;
    String weightType = "kg";
    String birthday = null;
    Calendar calendar;
    private String year;
    
    private CountryPicker countryPicker;
    private StatePicker statePicker;
    private CityPicker cityPicker;
    // arrays of state object
    public static List<State> stateObject;
    // arrays of city object
    public static List<City> cityObject;
    public static int countryID, stateID;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //  getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setFlags(WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES, WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES);
        binding = ActivityEditProfileBinding.inflate(getLayoutInflater());
        view = binding.getRoot();
        setContentView(view);
        FullScreen();
        setupViewModels();
        init();
    }
    
    public void setupViewModels() {
        editProfileViewModel = new ViewModelProvider(this).get(EditProfileViewModel.class);
        editProfileObserveViewModel(editProfileViewModel);
        
        uploadFileViewModel = new ViewModelProvider(this).get(UploadFileViewModel.class);
        uploadFileObserveViewModel(uploadFileViewModel);
        
        getProfileViewModel = new ViewModelProvider(this).get(GetProfileViewModel.class);
        getProfileObserveViewModel(getProfileViewModel);
    }
    
    private void init() {
        mySharedPreference = getSharedPreferences(MyShardPreferences.mPreferenceName, MODE_PRIVATE);
        editor = mySharedPreference.edit();
        calendar = Calendar.getInstance();
        TimeZone timeZone = TimeZone.getDefault();
        timeZoneS = timeZone.getID().toString();
        fcm_token = FirebaseInstanceId.getInstance().getToken();
        Log.wtf("-this", "FCM Token : " + fcm_token);
        countryPickerInit();
        binding.spinnerGender.setItems("Male", "Female", "Other");
        binding.spinnerGender.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {
            
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                gender = item;
            }
        });
        
        binding.spinnerWeight.setItems("kg", "lbs");
        binding.spinnerWeight.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {
            
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                weightType = item;
            }
        });
        
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        String[] yearArray = new String[100];
        
        yearArray[0] = currentYear + "";
        for (int i = 1; i < yearArray.length; i++) {
            int val = currentYear - i;
            yearArray[i] = val + "";
        }
        
        binding.spinnerYear.setItems(yearArray);
        binding.spinnerYear.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {
            
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                year = item;
            }
        });
        
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
        String token = mySharedPreference.getString(MyShardPreferences.token, "");
        binding.progressLayout.setVisibility(View.VISIBLE);
        getProfileViewModel.getProfile(token);
        
    }
    
    private void getProfileObserveViewModel(final GetProfileViewModel viewModel) {
        // Observe project data
        viewModel.getResponse().observe(this, apiResponse -> {
            
            if (apiResponse != null) {
                if (apiResponse.status == Status.SUCCESS) {
                    binding.progressLayout.setVisibility(View.GONE);
                    Log.wtf("-this", "User Profile Response : " + apiResponse.data);
                    Response<ResponseBody> responseBodyResponse = (Response<ResponseBody>) apiResponse.data;
                    
                    try {
                        if (responseBodyResponse.body() != null) {
                            String json = responseBodyResponse.body().string();
                            Log.wtf("retrofit_", "User Profile response body: " + json);
                            
                            Gson gson = new Gson();
                            getProfileModel = gson.fromJson(json, GetProfileModel.class);
                            Log.wtf("retrofit_", "After 229: ");
                            Log.wtf("retrofit_", "After 230: ");
                            if (getProfileModel.getData() != null)
                                setValues(getProfileModel);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        
                    }
                    
                } else if (apiResponse.status == Status.ERROR) {
                    Log.wtf("-this", "User Profile Response : " + apiResponse.error.getMessage());
                    binding.progressLayout.setVisibility(View.GONE);
                }
            } else {
                binding.progressLayout.setVisibility(View.GONE);
            }
        });
    }
    
    public void setValues(GetProfileModel model) {
        
        Log.wtf("-this", "  Set Values ");
        String profile_Image = model.getData().getProfileImage();
        String imageUrl = Urls.imageBaseUrl + "" + profile_Image;
        
        
        if (profile_Image != null &&
                profile_Image != "" &&
                profile_Image.length() > 10) {
            
            Glide.with(this)
                    .load(imageUrl).circleCrop()
                    .apply(new RequestOptions()
                            .dontAnimate().skipMemoryCache(true))
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {

//                           holder.imageViewProfilePic.setImageResource(R.drawable.profileico);
                            return false;
                        }
                        
                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            
                            return false;
                        }
                    })
                    .into((binding.imageViewProfile));
        }
        
        
        binding.editTextFirstName.setText(model.getData().getFirstName() + "");
        binding.editTextLastName.setText("" + model.getData().getLastName());
        binding.textViewBirthday.setText(model.getData().getDob() + "");
        birthday = model.getData().getDob();
        binding.editTextZipCode.setText(model.getData().getZipCode() + "");
        binding.editTextWeight.setText(model.getData().getWeight() + "");
        binding.editTextStreet.setText(model.getData().getAddress() + "");
        binding.editTextCity.setText(model.getData().getCity() + "");
        binding.editTextState.setText(model.getData().getState() + "");
        binding.editTextCountry.setText(model.getData().getCountry() + "");
        
        binding.spinnerGender.setText(model.getData().getGender() + "");
        gender = model.getData().getGender();
        binding.spinnerYear.setText(model.getData().getStartRunning() + "");
        year = model.getData().getStartRunning();
        binding.spinnerWeight.setText(model.getData().getWeightUnit() + "");
        weightType = model.getData().getWeightUnit();
        
        
    }
    
    public void UpdateClicked(View view) {
        String firstName = binding.editTextFirstName.getText().toString();
        String lastName = binding.editTextLastName.getText().toString();
        String weight = binding.editTextWeight.getText().toString();
        String streetAddress = binding.editTextStreet.getText().toString();
        String city = binding.editTextCity.getText().toString();
        String state = binding.editTextState.getText().toString();
        String zipCode = binding.editTextZipCode.getText().toString();
        String country = binding.editTextCountry.getText().toString();
        
        boolean allCorrect = true;
        boolean locCorrect = true;
        
        double lat = 0.0;
        double lng = 0.0;
        if (mLastLocation != null) {
            lat = mLastLocation.getLatitude();
            lng = mLastLocation.getLongitude();
        } else {
            getLocationNew(this);
            locCorrect = false;
        }
        
        
        if (firstName != null && (!firstName.equalsIgnoreCase("")) && firstName != "") {
            binding.cardViewFirstName.setStrokeColor(getResources().getColor(R.color.colorPrimary));
        } else {
            allCorrect = false;
            binding.cardViewFirstName.setStrokeColor(getResources().getColor(R.color.stock_red));
        }
        
        if (lastName != null && (!lastName.equalsIgnoreCase("")) && lastName != "") {
            binding.cardViewLastName.setStrokeColor(getResources().getColor(R.color.colorPrimary));
        } else {
            allCorrect = false;
            binding.cardViewLastName.setStrokeColor(getResources().getColor(R.color.stock_red));
        }
        
        if (birthday != null && (!birthday.equalsIgnoreCase("")) && birthday != "") {
            binding.cardViewBirthday.setStrokeColor(getResources().getColor(R.color.colorPrimary));
        } else {
            allCorrect = false;
            binding.cardViewBirthday.setStrokeColor(getResources().getColor(R.color.stock_red));
        }
        
        if (weight != null && (!weight.equalsIgnoreCase("")) && weight != "") {
            binding.cardViewWeight.setStrokeColor(getResources().getColor(R.color.colorPrimary));
        } else {
            allCorrect = false;
            binding.cardViewWeight.setStrokeColor(getResources().getColor(R.color.stock_red));
        }
        
        if (gender != null && (!gender.equalsIgnoreCase("")) && gender != "") {
            binding.cardViewSex.setStrokeColor(getResources().getColor(R.color.colorPrimary));
        } else {
            allCorrect = false;
            binding.cardViewSex.setStrokeColor(getResources().getColor(R.color.stock_red));
        }
        
        if (year != null && (!year.equalsIgnoreCase("")) && year != "") {
            binding.cardViewYear.setStrokeColor(getResources().getColor(R.color.colorPrimary));
        } else {
            allCorrect = false;
            binding.cardViewYear.setStrokeColor(getResources().getColor(R.color.stock_red));
        }
        
        if (streetAddress != null && (!streetAddress.equalsIgnoreCase("")) && streetAddress != "") {
            binding.cardViewStreet.setStrokeColor(getResources().getColor(R.color.colorPrimary));
        } else {
            allCorrect = false;
            binding.cardViewStreet.setStrokeColor(getResources().getColor(R.color.stock_red));
        }
        
        if (city != null && (!city.equalsIgnoreCase("")) && city != "") {
            binding.cardViewCity.setStrokeColor(getResources().getColor(R.color.colorPrimary));
        } else {
            allCorrect = false;
            binding.cardViewCity.setStrokeColor(getResources().getColor(R.color.stock_red));
        }
        
        if (state != null && (!state.equalsIgnoreCase("")) && state != "") {
            binding.cardViewState.setStrokeColor(getResources().getColor(R.color.colorPrimary));
        } else {
            allCorrect = false;
            binding.cardViewState.setStrokeColor(getResources().getColor(R.color.stock_red));
        }
        
        if (zipCode != null && (!zipCode.equalsIgnoreCase("")) && zipCode != "") {
            binding.cardViewZipCode.setStrokeColor(getResources().getColor(R.color.colorPrimary));
        } else {
            allCorrect = false;
            binding.cardViewZipCode.setStrokeColor(getResources().getColor(R.color.stock_red));
        }
        
        if (country != null && (!country.equalsIgnoreCase("")) && country != "") {
            binding.cardViewCountry.setStrokeColor(getResources().getColor(R.color.colorPrimary));
        } else {
            allCorrect = false;
            binding.cardViewCountry.setStrokeColor(getResources().getColor(R.color.stock_red));
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
                            UpdateClicked(view);
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
        
        
        if (allCorrect && locCorrect) {
            // Snackbar.make(view, "All Correct", Snackbar.LENGTH_SHORT).show();
            
            if (mLastLocation != null) {
                lat = mLastLocation.getLatitude();
                lng = mLastLocation.getLongitude();
            }
            String token = mySharedPreference.getString(MyShardPreferences.token, "");
            EditProfileSendModel model = new EditProfileSendModel();
            
            model.setToken(token);
            model.setFirst_name(firstName);
            model.setLast_name(lastName);
            model.setLatitude(lat);
            model.setLongitude(lng);
            model.setFcm_token(fcm_token);
            model.setTime_zone(timeZoneS);
            model.setAddress(streetAddress);
            model.setCity(city);
            model.setState(state);
            model.setCountry(country);
            model.setZip_code(zipCode);
            model.setDob(birthday);
            model.setProfile_image(profilePicName);
            model.setWeight(Double.parseDouble(weight));
            model.setGender(gender);
            model.setWeight_unit(weightType);
            model.setStart_running(year);
            
            binding.progressLayout.setVisibility(View.VISIBLE);
            editProfileViewModel.updateProfile(model);
            
        } else {
            Snackbar.make(view, "Fill all required fields", Snackbar.LENGTH_SHORT).show();
        }
        
        
    }
    
    private void editProfileObserveViewModel(final EditProfileViewModel viewModel) {
        // Observe project data
        viewModel.getResponse().observe(this, apiResponse -> {
            
            if (apiResponse != null) {
                if (apiResponse.status == Status.SUCCESS) {
                    
                    Log.wtf("-this", "Edit Profile API Response : " + apiResponse.data);
                    Response<ResponseBody> responseBodyResponse = (Response<ResponseBody>) apiResponse.data;
                    
                    try {
                        binding.progressLayout.setVisibility(View.GONE);
                        if (responseBodyResponse.body() != null) {
                            String json = responseBodyResponse.body().string();
                            Log.wtf("retrofit_", "Edit Profile response body: " + json);
                            JSONObject jsonObject = new JSONObject(json);
                            int status = jsonObject.getInt("status");
                            Log.wtf("retrofit_", "status : " + status);
                            if (status == 1) {
                                Gson gson = new Gson();
                                EditProfileModel model = gson.fromJson(json, EditProfileModel.class);
                                
                                editor.putInt(MyShardPreferences.UserID, model.getData().getId()).commit();
                                editor.putString(MyShardPreferences.first_name, model.getData().getFirstName()).commit();
                                editor.putString(MyShardPreferences.last_name, model.getData().getLastName()).commit();
                                editor.putString(MyShardPreferences.UserEmail, model.getData().getEmail()).commit();
                                
                                Snackbar.make(view, "" + model.getMessage(), Snackbar.LENGTH_SHORT).show();
                                Intent intent = new Intent();
                                intent.putExtra("Done", true);
                                setResult(7, intent);
                                finish();//finishing activity
                                
                            } else {
                                String error = jsonObject.getString("error");
                                Snackbar.make(view, error, Snackbar.LENGTH_SHORT).show();
                                
                            }
                            
                            
                        }
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                        
                    }
                    binding.progressLayout.setVisibility(View.GONE);
                    
                } else if (apiResponse.status == Status.ERROR) {
                    Log.wtf("-this", "Edit Profile API Response : " + apiResponse.error.getMessage());
                    binding.progressLayout.setVisibility(View.GONE);
                }
            }
        });
    }
    
    public void birthdayClicked(View view) {
        hideKeyBoardClicked(view);
        binding.layoutDate.setVisibility(View.VISIBLE);

//        int yyE = calendar.get(Calendar.YEAR);
//        int mmE = calendar.get(Calendar.MONTH);
//        int ddE = calendar.get(Calendar.DAY_OF_MONTH);
//        binding.datePicker.updateDate(yyE, mmE, ddE);
        binding.datePicker.setMaxDate(new Date().getTime());
    }
    
    public void dateDoneClicked(View view) {
        int day, month, year;
        day = binding.datePicker.getDayOfMonth();
        month = binding.datePicker.getMonth() + 1;
        year = binding.datePicker.getYear();
        int currentYear = calendar.get(Calendar.YEAR);
        
        
        if (CheckDates(year, currentYear)) {
            birthday = getNumberFormat(year) + "-" + getNumberFormat(month) + "-" + getNumberFormat(day);
            binding.textViewBirthday.setText(DateFormatter.getFormattedDateFromDateString(birthday));
            binding.layoutDate.setVisibility(View.GONE);
        } else {
            Snackbar.make(view, "Please Enter Valid Date of Birth", Snackbar.LENGTH_SHORT).show();
            binding.textViewBirthday.setText("");
        }
    }
    
    public void hideKeyBoardClicked(View view) {
        binding.editTextFirstName.clearFocus();
        binding.editTextLastName.clearFocus();
        binding.editTextStreet.clearFocus();
        binding.editTextCity.clearFocus();
        binding.editTextState.clearFocus();
        binding.editTextZipCode.clearFocus();
        binding.editTextCountry.clearFocus();
        binding.editTextWeight.clearFocus();
        
        
        InputMethodManager imm = (InputMethodManager) getApplicationContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        
    }
    
    public void cameraClicked(View view) {
        binding.cameraLayout.setVisibility(View.VISIBLE);
        
    }
    
    public void openGalleryClicked(View view) {
        binding.cameraLayout.setVisibility(View.GONE);
        getFromGallery();
    }
    
    public void openCameraClicked(View view) {
        binding.cameraLayout.setVisibility(View.GONE);
        Intent intent = new Intent(this, CameraActivity.class);
        startActivityForResult(intent, Camera_Result_Act);// Activity is started with requestCode 2
    }
    
    public void getFromGallery() {
        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(i, CHOSE_IMAGE_FROM_GALLERY_REQUEST_CODE);
    }
    
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CHOSE_IMAGE_FROM_GALLERY_REQUEST_CODE && resultCode == RESULT_OK) {
            try {
                onSelectFromGalleryResult(data);
            } catch (Exception ex) {
                Toast.makeText(this, "Cant load Picture", Toast.LENGTH_SHORT).show();
            }
        }
        if (requestCode == 2) {
            if (data != null) {
                String fileName = data.getStringExtra("fileName");
                String path = data.getStringExtra("path");
                Log.wtf("-this", " OnActivity result fileName : " + fileName);
                Log.wtf("-this", " OnActivity result Path : " + path);
                profilePicURL = path;
                profilePicName = fileName;
                if (profilePicURL != null &&
                        profilePicURL != "" &&
                        profilePicURL.length() > 5) {
                    Glide.with(this).load(profilePicURL).circleCrop().into(binding.imageViewProfile);
                }
                
            }
        }
    }
    
    private void onSelectFromGalleryResult(Intent data) throws Exception {
        Uri imageUri = data.getData();
        Log.wtf("-this", "Image URI : " + imageUri);
        try {
            Uri uri = data.getData();
            String selectedFilePath = FileUtils.getPath(this, uri);
            Log.wtf("-this", "Image Path : " + selectedFilePath);
            final File file = new File(selectedFilePath);
            getCompressedImage(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        
    }
    
    public void uploadPic(File file) {
        
        Log.wtf("-this", " File After Comppressed : " + file.getPath());
        String token = mySharedPreference.getString(MyShardPreferences.token, "");
        RequestBody requestFile =
                RequestBody.create(file, MediaType.parse("image/jpg"));
        
        MultipartBody.Part body = MultipartBody.Part.createFormData("image", file.getName(), requestFile);
        binding.progressLayout.setVisibility(View.VISIBLE);
        uploadFileViewModel.uploadFile(body);
    }
    
    private void uploadFileObserveViewModel(final UploadFileViewModel viewModel) {
        // Observe project data
        viewModel.getResponse().observe(this, apiResponse -> {
            
            if (apiResponse != null) {
                if (apiResponse.status == Status.SUCCESS) {
                    
                    Log.wtf("-this", "Upload API Response : " + apiResponse.data);
                    Response<ResponseBody> responseBodyResponse = (Response<ResponseBody>) apiResponse.data;
                    
                    try {
                        binding.progressLayout.setVisibility(View.GONE);
                        if (responseBodyResponse.body() != null) {
                            String json = responseBodyResponse.body().string();
                            Log.wtf("retrofit_", "UploadFile response body: " + json);
                            JSONObject jsonObject = new JSONObject(json);
                            int status = jsonObject.getInt("status");
                            Log.wtf("retrofit_", "status : " + status);
                            UploadFileModel uploadFileModel = null;
                            if (status == 1) {
                                Gson gson = new Gson();
                                uploadFileModel = gson.fromJson(json, UploadFileModel.class);
                                
                            } else {
                                String error = jsonObject.getString("error");
                                uploadFileModel.setStatus(status);
                                uploadFileModel.setError(error);
                                
                            }
                            
                            if (uploadFileModel != null) {
                                if (uploadFileModel.getStatus() == 1) {
                                    profilePicURL = uploadFileModel.getData().getPath();
                                    profilePicName = uploadFileModel.getData().getFile_name();
                                    if (profilePicURL != null &&
                                            profilePicURL != "" &&
                                            profilePicURL.length() > 5) {
                                        Glide.with(this).load(profilePicURL).circleCrop().into(binding.imageViewProfile);
                                    }
                                    
                                } else {
                                    Snackbar.make(view, "" + uploadFileModel.getError(), Snackbar.LENGTH_SHORT).show();
                                }
                            } else {
                                Log.wtf("-this", "Upload File Camera Response : Null");
                            }
                            
                            
                        }
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                        
                    }
                    binding.progressLayout.setVisibility(View.GONE);
                    
                } else if (apiResponse.status == Status.ERROR) {
                    Log.wtf("-this", "Upload API Response : " + apiResponse.error.getMessage());
                    binding.progressLayout.setVisibility(View.GONE);
                }
            }
        });
    }
    
    public void dummyClick(View view) {
    }
    
    @Override
    public void onBackPressed() {
        if (binding.cameraLayout.getVisibility() == View.VISIBLE) {
            binding.cameraLayout.setVisibility(View.GONE);
        } else {
            super.onBackPressed();
            finish();
        }
    }
    
    public void getCompressedImage(File file) {
        Luban.compress(this, file)
                .putGear(Luban.FIRST_GEAR)      // set the compress mode, default is : THIRD_GEAR
                .launch(this);
    }
    
    public void dummyBgClicked(View view) {
    }
    
    @Override
    public void onStart() {
        super.onStart();
    }
    
    @Override
    public void onSuccess(File file) {
        uploadPic(file);
    }
    
    @Override
    public void onError(Throwable e) {
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
    
    public String getNumberFormat(int h) {
        int ct = h;
        if (h > 0) {
            int length = (int) (Math.log10(h) + 1);
            // Log.wtf("-this", "LENGTH : " + length + " CT " + ct);
            if (length > 1) {
                return (ct + "");
            } else {
                if (ct == 10) {
                    return (ct + "");
                } else {
                    return ("0" + ct);
                }
            }
        } else {
            return ("00");
        }
    }
    
    public static boolean CheckDates(int sYear, int eYear) {
        Log.wtf("-Date", "554 eYear : " + eYear + "  sYear : " + sYear);
        if (eYear > sYear) {
            Log.wtf("-Date", "554 eYear : " + eYear + "  sYear : " + sYear);
            return true;
        } else {
            return false;
        }
        
        
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
    
    
    // Country Picker
    
    public void countryPickerInit() {
        // initiate state object, parser, and arrays
        // initialize country picker
        
        stateObject = new ArrayList<>();
        cityObject = new ArrayList<>();
        
        try {
            getStateJson();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        // get City from assets JSON
        try {
            getCityJson();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        
        countryPicker = new CountryPicker.Builder().with(this).listener(this).build();
        
        // initialize listeners
        
        setCountryListener();
        //  setStateListener();
        //  setCityListener();
        
        
    }
    
    //SET COUNTRY LISTENER
    private void setCountryListener() {
        Log.wtf("-this"," Set Country Listener ");
        binding.editTextCountry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.wtf("-this"," Country Listener Clicked ");
                countryPicker.showDialog(getSupportFragmentManager());
            }
        });
    }
    
    // SET STATE LISTENER
    private void setStateListener() {
        binding.editTextState.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                statePicker.showDialog(getSupportFragmentManager());
            }
        });
    }
    
    
    //SET CITY LISTENER
    private void setCityListener() {
        binding.editTextCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cityPicker.showDialog(getSupportFragmentManager());
            }
        });
    }
    
    // ON SELECTED COUNTRY ADD STATES TO PICKER
    @Override
    public void onSelectCountry(Country country) {
        // get country name and country ID
        binding.editTextCountry.setText(country.getName());
        countryID = country.getCountryId();
        statePicker.equalStateObject.clear();
        cityPicker.equalCityObject.clear();
        
        //set state name text view and state pick button invisible
        setStateListener();
        
        // set text on main view
//        countryCode.setText("Country code: " + country.getCode());
//        countryPhoneCode.setText("Country dial code: " + country.getDialCode());
//        countryCurrency.setText("Country currency: " + country.getCurrency());
//        flagImage.setBackgroundResource(country.getFlag());
        
        
        // GET STATES OF SELECTED COUNTRY
        for (int i = 0; i < stateObject.size(); i++) {
            // init state picker
            statePicker = new StatePicker.Builder().with(this).listener(this).build();
            State stateData = new State();
            if (stateObject.get(i).getCountryId() == countryID) {
                
                stateData.setStateId(stateObject.get(i).getStateId());
                stateData.setStateName(stateObject.get(i).getStateName());
                stateData.setCountryId(stateObject.get(i).getCountryId());
                stateData.setFlag(country.getFlag());
                statePicker.equalStateObject.add(stateData);
            }
        }
    }
    
    // ON SELECTED STATE ADD CITY TO PICKER
    @Override
    public void onSelectState(State state) {
        
        setCityListener();
        cityPicker.equalCityObject.clear();
        
        binding.editTextState.setText(state.getStateName());
        stateID = state.getStateId();
        
        
        for (int i = 0; i < cityObject.size(); i++) {
            cityPicker = new CityPicker.Builder().with(this).listener(this).build();
            City cityData = new City();
            if (cityObject.get(i).getStateId() == stateID) {
                cityData.setCityId(cityObject.get(i).getCityId());
                cityData.setCityName(cityObject.get(i).getCityName());
                cityData.setStateId(cityObject.get(i).getStateId());
                
                cityPicker.equalCityObject.add(cityData);
            }
        }
    }
    
    // ON SELECTED CITY
    @Override
    public void onSelectCity(City city) {
        binding.editTextCity.setText(city.getCityName());
    }
    
    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
    
    }
    
    // GET STATE FROM ASSETS JSON
    public void getStateJson() throws JSONException {
        String json = null;
        try {
            InputStream inputStream = getAssets().open("states.json");
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();
            json = new String(buffer, "UTF-8");
            
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        
        JSONObject jsonObject = new JSONObject(json);
        JSONArray events = jsonObject.getJSONArray("states");
        for (int j = 0; j < events.length(); j++) {
            JSONObject cit = events.getJSONObject(j);
            State stateData = new State();
            
            stateData.setStateId(Integer.parseInt(cit.getString("id")));
            stateData.setStateName(cit.getString("name"));
            stateData.setCountryId(Integer.parseInt(cit.getString("country_id")));
            stateObject.add(stateData);
        }
    }
    
    // GET CITY FROM ASSETS JSON
    public void getCityJson() throws JSONException {
        String json = null;
        try {
            InputStream inputStream = getAssets().open("cities.json");
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();
            json = new String(buffer, "UTF-8");
            
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        
        JSONObject jsonObject = new JSONObject(json);
        JSONArray events = jsonObject.getJSONArray("cities");
        for (int j = 0; j < events.length(); j++) {
            JSONObject cit = events.getJSONObject(j);
            City cityData = new City();
            
            cityData.setCityId(Integer.parseInt(cit.getString("id")));
            cityData.setCityName(cit.getString("name"));
            cityData.setStateId(Integer.parseInt(cit.getString("state_id")));
            cityObject.add(cityData);
        }
    }
}