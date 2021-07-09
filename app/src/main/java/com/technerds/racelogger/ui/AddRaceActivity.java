package com.technerds.racelogger.ui;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.technerds.racelogger.Utils.DateFormatter;
import com.technerds.racelogger.Utils.StringChecker;
import com.technerds.racelogger.adapters.ShoeAdapter;
import com.technerds.racelogger.dataModels.shoeModels.getShoeProfiles.GetShoeProfiles;
import com.technerds.racelogger.dataModels.shoeModels.getShoeProfiles.ShoeProfileDataItem;
import com.technerds.racelogger.viewModels.SearchRaceNameViewModel;
import com.technerds.racelogger.dataModels.PicModel;
import com.technerds.racelogger.dataModels.UploadFileModel;
import com.technerds.racelogger.dataModels.addRaceModel.AddRaceSendModel;
import com.technerds.racelogger.dataModels.addRaceModel.UserRace;
import com.technerds.racelogger.dataModels.addRaceModel.UserShoes;
import com.technerds.racelogger.dataModels.dropDownModels.DropDownModel;
import com.technerds.racelogger.dataModels.searchRaceModel.SearchRaceModel;
import com.technerds.racelogger.dataModels.shoeMilleage.ShoeMileageModel;
import com.technerds.racelogger.network.Status;
import com.technerds.racelogger.R;
import com.technerds.racelogger.Utils.AgeGroups;
import com.technerds.racelogger.Utils.FileUtils;
import com.technerds.racelogger.Utils.MyShardPreferences;
import com.technerds.racelogger.viewModels.AddRaceViewModel;
import com.technerds.racelogger.viewModels.GetDropDownViewModel;
import com.technerds.racelogger.viewModels.GetShoeMileageRunViewModel;
import com.technerds.racelogger.viewModels.UploadFileViewModel;
import com.technerds.racelogger.adapters.PicAdapter;
import com.technerds.racelogger.databinding.ActivityAddRaceBinding;
import com.technerds.racelogger.listeners.MyAdapterListener;
import com.technerds.racelogger.viewModels.shoeProfiles.GetShoeProfilesViewModel;
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
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import me.shaohui.advancedluban.Luban;
import me.shaohui.advancedluban.OnCompressListener;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Response;

public class AddRaceActivity extends AppCompatActivity implements OnCompressListener, OnStatePickerListener, OnCountryPickerListener, OnCityPickerListener {
    
    private static final int CHOSE_IMAGE_FROM_GALLERY_REQUEST_CODE = 101;
    private static final int Camera_Result_Act = 2;
    ActivityAddRaceBinding binding;
    View view;
    SharedPreferences.Editor editor;
    SharedPreferences mySharedPreference;
    FusedLocationProviderClient mFusedLocationClient;
    Location mLastLocation = null;
    
    PicAdapter picAdapter;
    ArrayList<PicModel> picList = new ArrayList<>();
    
    GetDropDownViewModel getDropDownViewModel;
    DropDownModel dropDownModel;
    
    SearchRaceNameViewModel searchRaceNameViewModel;
    SearchRaceModel searchRaceModel;
    
    AddRaceViewModel addRaceViewModel;
    
    GetShoeProfilesViewModel getShoeProfilesViewModel;
    GetShoeProfiles getShoeProfiles;
    
    UploadFileViewModel uploadFileViewModel;
    int c = 0;
    
    String raceDate = null;
    String raceTime = null;
    Calendar calendar;
    
    String distance = null;
    String raceName = null;
    String temperature = null;
    int temperature_index = -1;
    String temperatureUnit = "F";
    String weather = null;
    ArrayList<String> terrainSendList = new ArrayList<>();
    String courseType = null;
    String shoeProfile = null;
    int shoeProfileID = -1;
    int ageGroup = 1;
    int enjoyment_rating = 0;
    int feel_meter_rating = 0;
    int toughness_rating = 0;
    
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
        //  getWindow().setStatusBarColor(ContextCompat.getColor(this,R.color.white_opac_thirty));

//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        
        binding = ActivityAddRaceBinding.inflate(getLayoutInflater());
        view = binding.getRoot();
        setContentView(view);
        FullScreen();
        setupViewModels();
        init();
    }
    
    public void setupViewModels() {
        getDropDownViewModel = new ViewModelProvider(this).get(GetDropDownViewModel.class);
        getDropDownsObserveViewModel(getDropDownViewModel);
        
        uploadFileViewModel = new ViewModelProvider(this).get(UploadFileViewModel.class);
        uploadFileObserveViewModel(uploadFileViewModel);
        
        addRaceViewModel = new ViewModelProvider(this).get(AddRaceViewModel.class);
        addRaceObserveViewModel(addRaceViewModel);
        
        
        searchRaceNameViewModel = new ViewModelProvider(this).get(SearchRaceNameViewModel.class);
        searchRaceNameObserveViewModel(searchRaceNameViewModel);
        
        getShoeProfilesViewModel = new ViewModelProvider(this).get(GetShoeProfilesViewModel.class);
        getShoeProfilesObserveViewModel(getShoeProfilesViewModel);
        
    }
    
    private void init() {
        mySharedPreference = getSharedPreferences(MyShardPreferences.mPreferenceName, MODE_PRIVATE);
        editor = mySharedPreference.edit();
        calendar = Calendar.getInstance();
        
        setPicAdapter();
        String token = mySharedPreference.getString(MyShardPreferences.token, "");
        countryPickerInit();
        binding.progressLayout.setVisibility(View.VISIBLE);
        searchRaceNameViewModel.searchRaceName(token);
        getShoeProfilesViewModel.getShoeProfiles(token);
        getDropDownViewModel.getDropDown();
        
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations, this can be null.
                        if (location != null) {
                            // Logic to handle location object
                            mLastLocation = location;
                            Log.wtf("-GPS", "getLocation 228 : " + mLastLocation.getLatitude() + "," + mLastLocation.getLongitude());
                            getAddressFromLocation(mLastLocation.getLatitude(), mLastLocation.getLongitude());
                            
                        } else {
                            Log.wtf("-GPS", "getLocation 231 : " + mLastLocation);
                            
                        }
                    }
                });
        
    }
    
    private void getAddressFromLocation(double latitude, double longitude) {
        
        Geocoder geocoder = new Geocoder(this, Locale.ENGLISH);
        
        try {
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            
            if (addresses.size() > 0) {
                Address fetchedAddress = addresses.get(0);
                StringBuilder strAddress = new StringBuilder();
                for (int i = 0; i < fetchedAddress.getMaxAddressLineIndex(); i++) {
                    strAddress.append(fetchedAddress.getAddressLine(i)).append(" ");
                }
                
                String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                String city = addresses.get(0).getLocality();
                String state = addresses.get(0).getAdminArea();
                String country = addresses.get(0).getCountryName();
                String postalCode = addresses.get(0).getPostalCode();
                String knownName = addresses.get(0).getFeatureName(); // Only if available else return NULL
                
                //   binding.editTextStreet.setText(address+"");
                binding.editTextCity.setText(city + "");
                binding.editTextState.setText(state + "");
                binding.editTextCountry.setText(country + "");
                if (StringChecker.checkString(postalCode))
                    binding.editTextZipCode.setText(postalCode + "");
                
            } else {
            
            }
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private void setPicAdapter() {
        
        binding.recyclerProfilePic.removeAllViews();
        binding.recyclerProfilePic.setAdapter(null);
        binding.recyclerProfilePic.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        Log.wtf("-this", "150 PicLits Size : " + picList.size());
        if (picList.size() > 0) {
            picAdapter = new PicAdapter(this, picList, new MyAdapterListener() {
                @Override
                public void viewAllClick(View v, int position) {
                    //    Toast.makeText(CompleteYourProfileActivity.this, "POS : " + position, Toast.LENGTH_SHORT).show();
                    picList.remove(position);
                    setPicAdapter();
                }
            });
            binding.recyclerProfilePic.setAdapter(picAdapter);
        }
    }
    
    private void searchRaceNameObserveViewModel(final SearchRaceNameViewModel viewModel) {
        // Observe project data
        viewModel.getResponse().observe(this, apiResponse -> {
            
            if (apiResponse != null) {
                if (apiResponse.status == Status.SUCCESS) {
                    Log.wtf("-this", "Race names API Response : " + apiResponse.data);
                    Response<ResponseBody> responseBodyResponse = (Response<ResponseBody>) apiResponse.data;
                    
                    try {
                        if (responseBodyResponse.body() != null) {
                            String json = responseBodyResponse.body().string();
                            Log.wtf("retrofit_", "Race names response body: " + json);
                            JSONObject jsonObject = new JSONObject(json);
                            int status = jsonObject.getInt("status");
                            Log.wtf("retrofit_", "status : " + status);
                            
                            if (status == 1) {
                                Gson gson = new Gson();
                                searchRaceModel = gson.fromJson(json, SearchRaceModel.class);
                                if (searchRaceModel.getData() != null && searchRaceModel.getData().getData() != null) {
                                    setupRaceSpinner();
                                }
                                
                            } else {
                                String error = jsonObject.getString("error");
                            }
                            
                        }
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                        
                    }
                    
                } else if (apiResponse.status == Status.ERROR) {
                    Log.wtf("-this", "Race names API Response : " + apiResponse.error.getMessage());
                    binding.progressLayout.setVisibility(View.GONE);
                }
            }
        });
    }
    
    private void setupRaceSpinner() {
        if (searchRaceModel.getData().getData().size() > 0) {
            String[] raceNameList = new String[searchRaceModel.getData().getData().size() + 1];
    
            for (int i = 0; i < searchRaceModel.getData().getData().size(); i++) {
                if (i == 0) {
                    raceNameList[i] = "";
                }
                raceNameList[i + 1] = searchRaceModel.getData().getData().get(i).getRaceName();
            }
    
            binding.spinnerRaceName.setItems(raceNameList);
            binding.spinnerRaceName.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {
        
                @Override
                public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                    if (item != null) {
                        if(position > 0) {
                            raceName = item;
                            int i = position - 1;
                            binding.editTextRaceName.setText("" + raceName);
                            binding.spinnerRaceName.setText("");
                            binding.editTextParkName.setText(searchRaceModel.getData().getData().get(i).getParkName());
                            binding.editTextStreet.setText(searchRaceModel.getData().getData().get(i).getStreetAddress());
                            binding.editTextCountry.setText(searchRaceModel.getData().getData().get(i).getCountry());
                            binding.editTextCity.setText(searchRaceModel.getData().getData().get(i).getCity());
                            binding.editTextState.setText(searchRaceModel.getData().getData().get(i).getState());
                            binding.editTextZipCode.setText(searchRaceModel.getData().getData().get(i).getZipCode());
                            binding.editTextTotalRunners.setText(searchRaceModel.getData().getData().get(i).getTotalRunners());
                            binding.editTextMaleRunners.setText(searchRaceModel.getData().getData().get(i).getMaleRunners());
                            binding.editTextFemaleRunners.setText(searchRaceModel.getData().getData().get(i).getFemaleRunners());
                            binding.editTextDistance.setText(searchRaceModel.getData().getData().get(i).getUser_distance() + "" + searchRaceModel.getData().getData().get(i).getUnit());
                            binding.editTextCourseType.setText(searchRaceModel.getData().getData().get(i).getCourseType());
                            binding.editTextWeather.setText(searchRaceModel.getData().getData().get(i).getWeather());
                        }
                
                    }
                }
            });
        }
    }
    
    private void getDropDownsObserveViewModel(final GetDropDownViewModel viewModel) {
        // Observe project data
        viewModel.getResponse().observe(this, apiResponse -> {
            
            if (apiResponse != null) {
                if (apiResponse.status == Status.SUCCESS) {
                    
                    Log.wtf("-this", "Get Drop Down API Response : " + apiResponse.data);
                    Response<ResponseBody> responseBodyResponse = (Response<ResponseBody>) apiResponse.data;
                    
                    try {
                        if (responseBodyResponse.body() != null) {
                            binding.progressLayout.setVisibility(View.GONE);
                            String json = responseBodyResponse.body().string();
                            Log.wtf("retrofit_", "Get Drop Down response body: " + json);
                            JSONObject jsonObject = new JSONObject(json);
                            int status = jsonObject.getInt("status");
                            Log.wtf("retrofit_", "status : " + status);
                            if (status == 1) {
                                Gson gson = new Gson();
                                dropDownModel = gson.fromJson(json, DropDownModel.class);
                                setupSpinners();
                                
                            } else {
                                String error = jsonObject.getString("error");
                                binding.progressLayout.setVisibility(View.GONE);
                            }
                        } else {
                            Log.wtf("-this", "Get Drop Down Response : Null");
                            binding.progressLayout.setVisibility(View.GONE);
                        }
                        
                        
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                        
                    }
                    
                } else if (apiResponse.status == Status.ERROR) {
                    Log.wtf("-this", "Get Drop Down API Response : " + apiResponse.error.getMessage());
                    binding.progressLayout.setVisibility(View.GONE);
                }
            }
        });
    }
    
    private void setupSpinners() {
        
        
        Log.wtf("-this", "Spinner called");
        
        String[] distanceList = new String[dropDownModel.getData().getDistance().size() + 1];
        
        for (int i = 0; i < dropDownModel.getData().getDistance().size(); i++) {
            if (i == 0) {
                distanceList[i] = "";
            }
            distanceList[i + 1] = dropDownModel.getData().getDistance().get(i).getName();
        }
        
        binding.spinnerDistance.setItems(distanceList);
        binding.spinnerDistance.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {
            
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                if (item != null) {
                    distance = item;
                    binding.editTextDistance.setText("" + distance);
                    binding.spinnerDistance.setText("");
                    
                }
            }
        });
        
        String[] temperatureListF = new String[dropDownModel.getData().getTemperatureF().size()];
        
        for (int i = 0; i < dropDownModel.getData().getTemperatureF().size(); i++) {
//            if(i == 0){
//                temperatureListF[i] = "";
//            }
            temperatureListF[i] = dropDownModel.getData().getTemperatureF().get(i).getName();
        }
        
        String[] temperatureListC = new String[dropDownModel.getData().getTemperatureC().size()];
        
        for (int i = 0; i < dropDownModel.getData().getTemperatureC().size(); i++) {
//            if(i==0){
//                temperatureListC[i] = "";
//            }
            temperatureListC[i] = dropDownModel.getData().getTemperatureC().get(i).getName();
        }
        
        binding.spinnerTemperature.setItems(temperatureListF);
        binding.spinnerTemperature.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {
            
            
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                if (item != null) {
                    temperature_index = position;
                    temperature = item;
                    
                }
            }
        });
        
        binding.spinnerTemperatureUnit.setItems("F", "C");
        binding.spinnerTemperatureUnit.setSelectedIndex(0);
        binding.spinnerTemperatureUnit.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {
            
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                if (item != null) {
                    int i = position + 1;
                    //   distanceType = i;
                    temperatureUnit = item;
                    
                    if (temperatureUnit.equalsIgnoreCase("F")) {
                        binding.spinnerTemperature.setItems(temperatureListF);
                        if (temperature_index > -1) {
                            binding.spinnerTemperature.setSelectedIndex(temperature_index);
                        }
                    } else if (temperatureUnit.equalsIgnoreCase("C")) {
                        binding.spinnerTemperature.setItems(temperatureListC);
                        if (temperature_index > -1) {
                            binding.spinnerTemperature.setSelectedIndex(temperature_index);
                        }
                    }
                    
                }
            }
        });
        
        String[] courseTypeList = new String[dropDownModel.getData().getCourseType().size() + 1];
        
        for (int i = 0; i < dropDownModel.getData().getCourseType().size(); i++) {
            if (i == 0) {
                courseTypeList[i] = "";
            }
            courseTypeList[i + 1] = "" + dropDownModel.getData().getCourseType().get(i).getName();
        }
        
        binding.spinnerCourseType.setItems(courseTypeList);
        binding.spinnerCourseType.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {
            
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                if (item != null) {
                    int i = position + 1;
                    //   distanceType = i;
                    courseType = item;
                    binding.spinnerCourseType.setText("");
                    binding.editTextCourseType.setText("" + item);
                    
                }
            }
        });
        
        String[] weatherList = new String[dropDownModel.getData().getWeather().size() + 1];
        
        for (int i = 0; i < dropDownModel.getData().getWeather().size(); i++) {
            if (i == 0) {
                weatherList[i] = "";
            }
            weatherList[i + 1] = dropDownModel.getData().getWeather().get(i).getName();
        }
        
        binding.spinnerWeather.setItems(weatherList);
        binding.spinnerWeather.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {
            
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                if (item != null) {
                    int i = position + 1;
                    //   distanceType = i;
                    weather = item;
                    binding.editTextWeather.setText("" + weather);
                    binding.spinnerWeather.setText("");
                    
                }
            }
        });
        
        String[] terrainList = new String[dropDownModel.getData().getTerrain().size() + 1];
        
        for (int i = 0; i < dropDownModel.getData().getTerrain().size(); i++) {
            if (i == 0) {
                terrainList[i] = "";
            }
            terrainList[i + 1] = dropDownModel.getData().getTerrain().get(i).getName();
        }
        
        binding.spinnerTerrain.setItems(terrainList);
        binding.spinnerTerrain.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {
            
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                if (item != null) {
                    int i = position + 1;
                    //   distanceType = i;
                    terrainSendList.clear();
                    terrainSendList.add(item);
                    binding.editTextTerrain.setText("" + item);
                    binding.spinnerTerrain.setText("");
                }
            }
        });
        
        
        binding.spinnerAgeGroup.setItems(AgeGroups.getAgrGroups());
        binding.spinnerAgeGroup.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {
            
            
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                if (item != null) {
                    int i = position + 1;
                    //   distanceType = i;
                    ageGroup = i;
                    
                }
            }
        });
        setAgeGroup();
        
    }
    
    private void setAgeGroup() {
        String dob = mySharedPreference.getString(MyShardPreferences.DOB, null);
        Log.wtf("-datee", "Shared DOB : " + dob);
        if (StringChecker.checkString(dob)) {
            try {
                Calendar calendar = Calendar.getInstance();
                Date currentDate = calendar.getTime();
                
                Log.wtf("-datee", "Date Current Date Time: " + currentDate);
                
                DateFormat f = new SimpleDateFormat("yyyy-MM-dd");
                Date dobDate = f.parse(dob);
                Log.wtf("-datee", "DOB Date Formatted : " + dobDate);
                
                printDifference(dobDate, currentDate);
                
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }
    
    public long printDifference(Date startDate, Date endDate) {
        //   Log.wtf("-this", " 293 printDifference : ");
        //milliseconds
        long differentSec = endDate.getTime() - startDate.getTime();
        //  differentSec = differentSec /1000;
        long different = endDate.getTime() - startDate.getTime();
        
        
        Log.wtf("-datee", "startDate : " + startDate);
        Log.wtf("-datee", "endDate : " + endDate);
        Log.wtf("-datee", "different : " + different);
        
        if (different > 0) {
            
            long secondsInMilli = 1000;
            long minutesInMilli = secondsInMilli * 60;
            long hoursInMilli = minutesInMilli * 60;
            long daysInMilli = hoursInMilli * 24;
            long yearsInMilli = daysInMilli * 365;
            
            long elapsedYears = different / yearsInMilli;
            different = different % yearsInMilli;
            
            long elapsedDays = different / daysInMilli;
            different = different % daysInMilli;
            
            long elapsedHours = different / hoursInMilli;
            different = different % hoursInMilli;
            
            long elapsedMinutes = different / minutesInMilli;
            different = different % minutesInMilli;
            
            long elapsedSeconds = different / secondsInMilli;
            
            
            Log.wtf("-datee", " Year : " + elapsedYears);
            Log.wtf("-datee", " days : " + elapsedDays);
            Log.wtf("-datee", " hours : " + elapsedHours);
            Log.wtf("-datee", " minutes : " + elapsedMinutes);
            Log.wtf("-datee", " seconds : " + elapsedSeconds);
            
            ageGroup = AgeGroups.getAgeGroupFromDOB(elapsedYears);
            binding.spinnerAgeGroup.setSelectedIndex(ageGroup - 1);
            
            
        }
        
        return differentSec;
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
                                getShoeProfiles = gson.fromJson(json, GetShoeProfiles.class);
                                
                                setShoeProfile();
                                
                                
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
        if (getShoeProfiles.getData() != null) {
            if (getShoeProfiles.getData().size() > 0) {
                String[] profileNames = new String[getShoeProfiles.getData().size() + 2];
                Log.wtf("-this", "profileMName Array Size : " + profileNames.length);
                Log.wtf("-this", "getShoeProfiles List Size : " + getShoeProfiles.getData().size());
                profileNames[0] = "";
                profileNames[1] = "Add Shoe profile";
                
                for (int i = 0; i < getShoeProfiles.getData().size(); i++) {
                    Log.wtf("-this", "getShoeProfiles Item : " + getShoeProfiles.getData().get(i).getShoeProfile());
                    profileNames[i + 2] = getShoeProfiles.getData().get(i).getShoeProfile();
                }
                
                binding.spinnerShoeProfile.setItems(profileNames);
                binding.spinnerShoeProfile.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {
                    
                    
                    @Override
                    public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                        if (item != null) {
                            // int i = position + 1;
                            //   distanceType = i;
                            if (item.equalsIgnoreCase("Add Shoe profile")) {
                                Intent intent = new Intent(AddRaceActivity.this, AddShoeActivity.class);
                                startActivityForResult(intent, 7);
                            } else {
                                shoeProfile = item;
                            }
                            
                        }
                    }
                });
            } else {
                binding.spinnerShoeProfile.setItems("", "Add Shoe profile");
                binding.spinnerShoeProfile.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {
                    
                    
                    @Override
                    public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                        if (item != null) {
                            binding.spinnerShoeProfile.setText("");
                            Intent intent = new Intent(AddRaceActivity.this, AddShoeActivity.class);
                            startActivityForResult(intent, 7);
                        }
                    }
                });
            }
        } else {
            binding.spinnerShoeProfile.setItems("", "Add Shoe profile");
            binding.spinnerShoeProfile.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {
                
                
                @Override
                public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                    if (item != null) {
                        binding.spinnerShoeProfile.setText("");
                        Intent intent = new Intent(AddRaceActivity.this, AddShoeActivity.class);
                        startActivityForResult(intent, 7);
                    }
                }
            });
        }
        
        
    }
    
    public void hideKeyBoardClicked(View view) {
        
        Log.wtf("-hide", "Hide keyboard Clicked");
        
        binding.editTextRaceName.clearFocus();
        binding.editTextStreet.clearFocus();
        binding.editTextCity.clearFocus();
        binding.editTextState.clearFocus();
        binding.editTextZipCode.clearFocus();
        binding.editTextCountry.clearFocus();
        binding.editTextDistance.clearFocus();
        binding.editTextNotes.clearFocus();
        binding.editTextTerrain.clearFocus();
        binding.editTextWeather.clearFocus();
        binding.editTextDistance.clearFocus();
        binding.editTextRunTimeSecond.clearFocus();
        binding.editTextRunTimeMinute.clearFocus();
        binding.editTextRunTimeHour.clearFocus();
        binding.editTextCourseType.clearFocus();
        binding.editTextParkName.clearFocus();
        binding.editTextMaleRunners.clearFocus();
        binding.editTextFemaleRunners.clearFocus();
        binding.editTextTotalRunners.clearFocus();
        binding.editTextRunnersInMyAgeGroup.clearFocus();
        binding.editTextFinishPlace.clearFocus();
        
        
        InputMethodManager imm = (InputMethodManager) getApplicationContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );
        
        
    }
    
    public void AddRaceClicked(View view) {
        String race_name = binding.editTextRaceName.getText().toString();
        String park_name = binding.editTextParkName.getText().toString();
        String street_address = binding.editTextStreet.getText().toString();
        String city = binding.editTextCity.getText().toString();
        String state = binding.editTextState.getText().toString();
        String country = binding.editTextCountry.getText().toString();
        String zip_code = binding.editTextZipCode.getText().toString();
        String distanceFull = binding.editTextDistance.getText().toString();
        String courseType = binding.editTextCourseType.getText().toString();
        String weather = binding.editTextWeather.getText().toString();
        String total_runners = binding.editTextTotalRunners.getText().toString();
        String male_runners = binding.editTextMaleRunners.getText().toString();
        String female_runners = binding.editTextFemaleRunners.getText().toString();
        String notes = binding.editTextNotes.getText().toString();
        
        
        String hour = binding.editTextRunTimeHour.getText().toString();
        String min = binding.editTextRunTimeMinute.getText().toString();
        String sec = binding.editTextRunTimeSecond.getText().toString();
        String finish_place = binding.editTextFinishPlace.getText().toString();
        String group_finish_place = binding.editTextGroupFinishPlace.getText().toString();
        String age_group_runner = binding.editTextRunnersInMyAgeGroup.getText().toString();
        
        enjoyment_rating = (int) binding.ratingBarEnjoyment.getRating();
        feel_meter_rating = (int) binding.ratingBarFeelMeter.getRating();
        toughness_rating = (int) binding.ratingBarToughness.getRating();
        
        UserRace userRaceModel = new UserRace();
        
        
        double distance = 0.0;
        String unit = "KM";
        
        boolean allCorrect = true;
        
        
        if (raceDate != null && (!raceDate.equalsIgnoreCase("")) && raceDate != "") {
            binding.cardViewDateTime.setStrokeColor(getResources().getColor(R.color.colorPrimary));
        } else {
            allCorrect = false;
            binding.cardViewDateTime.setStrokeColor(getResources().getColor(R.color.stock_red));
        }
        
        if (race_name != null && (!race_name.equalsIgnoreCase("")) && race_name != "") {
            binding.cardViewRaceName.setStrokeColor(getResources().getColor(R.color.colorPrimary));
        } else {
            allCorrect = false;
            binding.cardViewRaceName.setStrokeColor(getResources().getColor(R.color.stock_red));
        }

//        if (park_name != null && (!park_name.equalsIgnoreCase("")) && park_name != "") {
//            binding.cardViewParkName.setStrokeColor(getResources().getColor(R.color.colorPrimary));
//        } else {
//            allCorrect = false;
//            binding.cardViewParkName.setStrokeColor(getResources().getColor(R.color.stock_red));
//        }

//        if (street_address != null && (!street_address.equalsIgnoreCase("")) && street_address != "") {
//            binding.cardViewStreet.setStrokeColor(getResources().getColor(R.color.colorPrimary));
//        } else {
//            allCorrect = false;
//            binding.cardViewStreet.setStrokeColor(getResources().getColor(R.color.stock_red));
//        }
//
//        if (city != null && (!city.equalsIgnoreCase("")) && city != "") {
//            binding.cardViewCity.setStrokeColor(getResources().getColor(R.color.colorPrimary));
//        } else {
//            allCorrect = false;
//            binding.cardViewCity.setStrokeColor(getResources().getColor(R.color.stock_red));
//        }
//
//        if (state != null && (!state.equalsIgnoreCase("")) && state != "") {
//            binding.cardViewState.setStrokeColor(getResources().getColor(R.color.colorPrimary));
//        } else {
//            allCorrect = false;
//            binding.cardViewState.setStrokeColor(getResources().getColor(R.color.stock_red));
//        }
//
//        if (country != null && (!country.equalsIgnoreCase("")) && country != "") {
//            binding.cardViewCountry.setStrokeColor(getResources().getColor(R.color.colorPrimary));
//        } else {
//            allCorrect = false;
//            binding.cardViewCountry.setStrokeColor(getResources().getColor(R.color.stock_red));
//        }
//
//        if (zip_code != null && (!zip_code.equalsIgnoreCase("")) && zip_code != "") {
//            binding.cardViewZipCode.setStrokeColor(getResources().getColor(R.color.colorPrimary));
//        } else {
//            allCorrect = false;
//            binding.cardViewZipCode.setStrokeColor(getResources().getColor(R.color.stock_red));
//        }
        
        
        if (distanceFull != null && (!distanceFull.equalsIgnoreCase("")) && distanceFull.length() > 1) {
            
            Log.wtf("-this", " DistanceFull : " + distanceFull);
            if ((distanceFull.contains("M") || distanceFull.contains("Mile")
                    || distanceFull.contains("mile") || distanceFull.contains("mi"))
                    &&
                    ((!distanceFull.contains("K")) && (!distanceFull.contains("k"))
                            && (!distanceFull.contains("Marathon")) && (!distanceFull.contains("marathon"))
                    )) {
                Log.wtf("-this", " DistanceFull Mile IF: ");
                int mIndex = -1;
                mIndex = distanceFull.indexOf('M');
                if (mIndex < 1) {
                    mIndex = distanceFull.indexOf('m');
                }
                
                Log.wtf("-this", " DistanceFull Mile INDEX : " + mIndex);
                String distanceTemp = distanceFull.substring(0, mIndex);
                unit = "Mile";
                
                Log.wtf("-this", " DistanceTemp  : " + distanceTemp + "   UNIT : " + unit);
                distance = Double.parseDouble(distanceTemp);
                
            } else if (distanceFull.contains("K") || distanceFull.contains("KM")
                    || distanceFull.contains("km") || distanceFull.contains("Km")) {
                Log.wtf("-this", " DistanceFull KM IF: ");
                int mIndex = -1;
                mIndex = distanceFull.indexOf('K');
                if (mIndex < 1) {
                    mIndex = distanceFull.indexOf('k');
                }
                
                Log.wtf("-this", " DistanceFull KM INDEX : " + mIndex);
                String distanceTemp = distanceFull.substring(0, mIndex);
                unit = "Km";
                
                Log.wtf("-this", " DistanceTemp  : " + distanceTemp + "   UNIT : " + unit);
                distance = Double.parseDouble(distanceTemp);
                
            } else if (distanceFull.contains("Marathon") || distanceFull.contains("marathon")
                    || distanceFull.contains("mara") || distanceFull.contains("Mara")) {
                Log.wtf("-this", " DistanceFull Marathon IF: ");
                int mIndex = -1;
                mIndex = distanceFull.indexOf('M');
                if (mIndex < 1) {
                    mIndex = distanceFull.indexOf('m');
                }
                
                Log.wtf("-this", " DistanceFull Marathon INDEX : " + mIndex);
                String distanceTemp = distanceFull.substring(0, mIndex);
                unit = "Marathon";
                
                Log.wtf("-this", " DistanceTemp  : " + distanceTemp + "   UNIT : " + unit);
                distance = Double.parseDouble(distanceTemp);
                
            } else {
                Log.wtf("-this", " DistanceFull KM IF: ");
                int mIndex = -1;
                
                
                Log.wtf("-this", " DistanceFull Marathon INDEX : " + mIndex);
                String distanceTemp = distanceFull;
                unit = "Km";
                
                Log.wtf("-this", " DistanceTemp  : " + distanceTemp + "   UNIT : " + unit);
                if (distanceTemp != null) {
                    if (distanceTemp.matches("[0-9]+") && distanceTemp.length() > 0) {
                        distance = Double.parseDouble(distanceTemp);
                    }
                }
                
            }
        } else {
            Log.wtf("-this", " DistanceFull : NULL");
        }
        
        if (hour != null && (!hour.equalsIgnoreCase("")) && hour.length() > 0) {
            userRaceModel.setHour(Integer.parseInt(hour));
        }
        
        if (min != null && (!min.equalsIgnoreCase("")) && min.length() > 0) {
            userRaceModel.setMinute(Integer.parseInt(min));
        }
        
        if (sec != null && (!sec.equalsIgnoreCase("")) && sec.length() > 0) {
            userRaceModel.setSecond(Integer.parseInt(sec));
        }
        if (finish_place != null && (!finish_place.equalsIgnoreCase("")) && finish_place.length() > 0) {
            userRaceModel.setFinish_place(Integer.parseInt(finish_place));
        }
        if (group_finish_place != null && (!group_finish_place.equalsIgnoreCase("")) && group_finish_place.length() > 0) {
            userRaceModel.setFinish_group_place(Integer.parseInt(group_finish_place));
        }
        userRaceModel.setAge_group(ageGroup);
        
        if (age_group_runner != null && (!age_group_runner.equalsIgnoreCase("")) && age_group_runner.length() > 0) {
            userRaceModel.setAge_group_runner(Integer.parseInt(age_group_runner));
        }
        userRaceModel.setNotes(notes);
        userRaceModel.setDate(raceDate + " " + raceTime);
        
        
        String token = mySharedPreference.getString(MyShardPreferences.token, "");
        
        AddRaceSendModel addRaceSendModel = new AddRaceSendModel();
        
        addRaceSendModel.setToken(token);
        addRaceSendModel.setRaceName(race_name);
        addRaceSendModel.setPark_name(park_name);
        addRaceSendModel.setStreet_address(street_address);
        addRaceSendModel.setCity(city);
        addRaceSendModel.setState(state);
        addRaceSendModel.setCountry(country);
        addRaceSendModel.setZip_code(zip_code);
        addRaceSendModel.setDistance(distance);
        addRaceSendModel.setUnit(unit);
        addRaceSendModel.setCourse_type(courseType);
        addRaceSendModel.setTemperature(temperature);
        addRaceSendModel.setTemperature_unit(temperatureUnit);
        addRaceSendModel.setWeather(weather);
        if (total_runners != null && (!total_runners.equalsIgnoreCase("")) && total_runners.length() > 0) {
            addRaceSendModel.setTotal_runners(Integer.parseInt(total_runners));
        }
        
        if (male_runners != null && (!male_runners.equalsIgnoreCase("")) && male_runners.length() > 0) {
            addRaceSendModel.setMale_runners(Integer.parseInt(male_runners));
        }
        
        if (female_runners != null && (!female_runners.equalsIgnoreCase("")) && female_runners.length() > 0) {
            addRaceSendModel.setFemale_runners(Integer.parseInt(female_runners));
        }
        if (StringChecker.checkString(shoeProfile)) {
            for (int i = 0; i < getShoeProfiles.getData().size(); i++) {
                if (getShoeProfiles.getData().get(i).getShoeProfile().equalsIgnoreCase(shoeProfile)) {
                    shoeProfileID = getShoeProfiles.getData().get(i).getId();
                }
            }
        }
        
        terrainSendList.clear();
        String raceTerrain = binding.editTextTerrain.getText().toString();
        if(StringChecker.checkString(raceTerrain)){
            if(raceTerrain.contains(",")){
                String[] split = raceTerrain.split(",");
                if(split != null && split.length > 0){
                    for (int i = 0; i < split.length; i++){
                        terrainSendList.add(split[i]);
                    }
                }
            } else {
                terrainSendList.add(raceTerrain);
            }
        }
        
        addRaceSendModel.setEnjoyment_rating(enjoyment_rating);
        addRaceSendModel.setFeel_meter_rating(feel_meter_rating);
        addRaceSendModel.setToughness_rating(toughness_rating);
        addRaceSendModel.setUser_race(userRaceModel);
        if (shoeProfileID > 0) {
            addRaceSendModel.setShoes_profile_id(shoeProfileID);
        }
        addRaceSendModel.setRace_terrain(terrainSendList);
        
        ArrayList<String> picNameList = new ArrayList<>();
        if (picList.size() > 0) {
            
            for (int i = 0; i < picList.size(); i++) {
                picNameList.add(picList.get(i).getName());
            }
        }
        addRaceSendModel.setUserGallery(picNameList);
        
        if (allCorrect) {
            binding.progressLayout.setVisibility(View.VISIBLE);
            addRaceViewModel.addRace(addRaceSendModel);
        } else {
            Snackbar.make(view, "Please fill required fields", Snackbar.LENGTH_SHORT).show();
        }
        
        
    }
    
    private void addRaceObserveViewModel(final AddRaceViewModel viewModel) {
        // Observe project data
        viewModel.getResponse().observe(this, apiResponse -> {
            
            if (apiResponse != null) {
                if (apiResponse.status == Status.SUCCESS) {
                    
                    Log.wtf("-this", "Add Race API Response : " + apiResponse.data);
                    Response<ResponseBody> responseBodyResponse = (Response<ResponseBody>) apiResponse.data;
                    binding.progressLayout.setVisibility(View.GONE);
                    try {
                        if (responseBodyResponse.body() != null) {
                            String json = responseBodyResponse.body().string();
                            Log.wtf("retrofit_", "Add Race response body: " + json);
                            JSONObject jsonObject = new JSONObject(json);
                            int status = jsonObject.getInt("status");
                            Log.wtf("retrofit_", "status : " + status);
                            
                            if (status == 1) {
                                String msg = jsonObject.getString("message");
                                Snackbar.make(view, "" + msg, Snackbar.LENGTH_SHORT).show();
                                Intent intent = new Intent();
                                intent.putExtra("Done", true);
                                setResult(7, intent);
                                finish();//finishing activity
                                
                            } else {
                                String error = jsonObject.getString("error");
                                Snackbar.make(view, "" + error, Snackbar.LENGTH_SHORT).show();
                            }
                            
                            
                        }
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                        
                    }
                    //  binding.progressLayout.setVisibility(View.GONE);
                    
                } else if (apiResponse.status == Status.ERROR) {
                    Log.wtf("-this", "Add Race API Error Response : " + apiResponse.error.getMessage());
                    binding.progressLayout.setVisibility(View.GONE);
                    Intent intent = new Intent();
                    intent.putExtra("Done", true);
                    setResult(7, intent);
                    finish();//finishing activity
                }
            } else {
                binding.progressLayout.setVisibility(View.GONE);
                
            }
        });
    }
    
    // Camera
    
    public void cameraClicked(View view) {
        if (picList != null) {
            binding.cameraLayout.setVisibility(View.VISIBLE);
//            if(picList.size() < 4){
//                binding.cameraLayout.setVisibility(View.VISIBLE);
//            } else {
//                Snackbar.make(view,"Maximum four pictures",Snackbar.LENGTH_SHORT).show();
//            }
        } else {
            binding.cameraLayout.setVisibility(View.VISIBLE);
        }
        
    }
    
    public void openGalleryClicked(View view) {
        binding.cameraLayout.setVisibility(View.GONE);
        getFromGallery();
    }
    
    public void openCameraClicked(View view) {
        binding.cameraLayout.setVisibility(View.GONE);
        Intent intent = new Intent(AddRaceActivity.this, CameraActivity.class);
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
                
                PicModel picModel = new PicModel();
                picModel.setName(fileName);
                picModel.setURL(path);
                picList.add(picModel);
                setPicAdapter();
            }
        }
        
        if (requestCode == 7) {
            if (data != null) {
                boolean done = data.getBooleanExtra("Done", false);
                Log.wtf("-this", " OnActivity result Done : " + done);
                
                if (done) {
                    binding.progressLayout.setVisibility(View.VISIBLE);
                    String token = mySharedPreference.getString(MyShardPreferences.token, "");
                    getShoeProfilesViewModel.getShoeProfiles(token);
                    
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
        c = 0;
        uploadFileViewModel.uploadFile(body);
    }
    
    private void uploadFileObserveViewModel(final UploadFileViewModel viewModel) {
        // Observe project data
        viewModel.getResponse().observe(this, apiResponse -> {
            
            if (apiResponse != null) {
                if (apiResponse.status == Status.SUCCESS) {
                    
                    Log.wtf("-this", "UploadFile API Response : " + apiResponse.data);
                    Response<ResponseBody> responseBodyResponse = (Response<ResponseBody>) apiResponse.data;
                    
                    try {
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
                                    if (c == 0) {
                                        c++;
                                        Snackbar.make(view, "" + uploadFileModel.getMessage(), Snackbar.LENGTH_SHORT).show();
                                        c++;
                                        Log.wtf("-this", "Upload 520 File URL : " + c + "   " + uploadFileModel.getData().getPath());
                                        
                                        PicModel picModel = new PicModel();
                                        picModel.setName(uploadFileModel.getData().getFile_name());
                                        picModel.setURL(uploadFileModel.getData().getPath());
                                        picList.add(picModel);
                                        setPicAdapter();
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
                    //  binding.progressLayout.setVisibility(View.GONE);
                    
                } else if (apiResponse.status == Status.ERROR) {
                    Log.wtf("-this", "UploadFile API Response : " + apiResponse.error.getMessage());
                    //  binding.progressLayout.setVisibility(View.GONE);
                }
            }
        });
    }
    
    public void dummyClick(View view) {
    }
    
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent();
        intent.putExtra("Done", false);
        setResult(7, intent);
        finish();//finishing activity
        
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
    
    public void RaceDateTimeClicked(View view) {
        hideKeyBoardClicked(view);
        binding.layoutRaceDateTime.setVisibility(View.VISIBLE);

//        int yy = calendar.get(Calendar.YEAR);
//        int mm = calendar.get(Calendar.MONTH);
//        int dd = calendar.get(Calendar.DAY_OF_MONTH);
//        int hh = calendar.get(Calendar.HOUR_OF_DAY);
//        int mn = calendar.get(Calendar.MINUTE);
//        binding.datePickerRace.updateDate(yy, mm, dd);

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            binding.timePickerRace.setHour(hh);
//            binding.timePickerRace.setMinute(mn);
//        }
        binding.timePickerRace.setIs24HourView(true);
    }
    
    public void RaceDateDoneClicked(View view) {
        int startHour = 9, startMinute = 0;
        int day, month, year;
        
        if (Build.VERSION.SDK_INT >= 23) {
            startHour = binding.timePickerRace.getHour();
            startMinute = binding.timePickerRace.getMinute();
        } else {
            startHour = binding.timePickerRace.getCurrentHour();
            startMinute = binding.timePickerRace.getCurrentMinute();
        }
        day = binding.datePickerRace.getDayOfMonth();
        month = binding.datePickerRace.getMonth() + 1;
        year = binding.datePickerRace.getYear();
        
        raceDate = getNumberFormat(year) + "-" + getNumberFormat(month) + "-" + getNumberFormat(day);
        raceTime = getNumberFormat(startHour) + ":" + getNumberFormat(startMinute);
        
        Log.wtf("-Date", "Race Date : " + raceDate);
        
        
        binding.textViewDateTime.setText(DateFormatter.getFormattedDateFromDateTimeString(raceDate + " " + raceTime));
        
        binding.layoutRaceDateTime.setVisibility(View.GONE);
        
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
    
    public void getCompressedImage(File file) {
        Luban.compress(this, file)
                .putGear(Luban.FIRST_GEAR)      // set the compress mode, default is : THIRD_GEAR
                .launch(this);
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
        Log.wtf("-this", " Set Country Listener ");
        binding.editTextCountry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.wtf("-this", " Country Listener Clicked ");
                if (countryPicker != null)
                    countryPicker.showDialog(getSupportFragmentManager());
            }
        });
    }
    
    // SET STATE LISTENER
    private void setStateListener() {
        binding.editTextState.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (statePicker != null)
                    statePicker.showDialog(getSupportFragmentManager());
            }
        });
    }
    
    
    //SET CITY LISTENER
    private void setCityListener() {
        binding.editTextCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cityPicker != null)
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
        
        
        // set text on main view
//        countryCode.setText("Country code: " + country.getCode());
//        countryPhoneCode.setText("Country dial code: " + country.getDialCode());
//        countryCurrency.setText("Country currency: " + country.getCurrency());
//        flagImage.setBackgroundResource(country.getFlag());
        
        //  Log.wtf("-CP","1306 on Country Selected  : "+country.getName());
        
        //   Log.wtf("-CP","1308 stateObject Size : "+stateObject.size());
        // GET STATES OF SELECTED COUNTRY
        for (int i = 0; i < stateObject.size(); i++) {
            // init state picker
            statePicker = new StatePicker.Builder().with(this).listener(this).build();
            
            State stateData = new State();
            if (stateObject.get(i).getCountryId() == countryID) {
                //     Log.wtf("-CP","1316 Index i : "+i+"   :  State :  "+stateObject.get(i).getStateName());
                stateData.setStateId(stateObject.get(i).getStateId());
                stateData.setStateName(stateObject.get(i).getStateName());
                stateData.setCountryId(stateObject.get(i).getCountryId());
                stateData.setFlag(country.getFlag());
                statePicker.equalStateObject.add(stateData);
            }
        }
        
        setStateListener();
        
        
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