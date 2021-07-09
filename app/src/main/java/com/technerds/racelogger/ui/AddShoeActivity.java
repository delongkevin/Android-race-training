package com.technerds.racelogger.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.technerds.racelogger.R;
import com.technerds.racelogger.Utils.AppUtils;
import com.technerds.racelogger.Utils.DateFormatter;
import com.technerds.racelogger.Utils.MyShardPreferences;
import com.technerds.racelogger.Utils.StringChecker;
import com.technerds.racelogger.dataModels.dropDownModels.DropDownModel;
import com.technerds.racelogger.dataModels.shoeModels.AddShoeSendModel;
import com.technerds.racelogger.databinding.ActivityAddShoeBinding;
import com.technerds.racelogger.network.Status;
import com.technerds.racelogger.viewModels.GetDropDownViewModel;
import com.technerds.racelogger.viewModels.shoeProfiles.AddShoeProfileViewModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

import okhttp3.ResponseBody;
import retrofit2.Response;

public class AddShoeActivity extends AppCompatActivity {
    
    ActivityAddShoeBinding binding;
    View view;
    
    SharedPreferences.Editor editor;
    SharedPreferences mySharedPreference;
    
    AddShoeProfileViewModel addShoeProfileViewModel;
    
    GetDropDownViewModel getDropDownViewModel;
    DropDownModel dropDownModel;
    
    String shoeBrand = null;
    
    String purchaseDate = null;
    Calendar calendar;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES, WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES);
        
        binding = ActivityAddShoeBinding.inflate(getLayoutInflater());
        view = binding.getRoot();
        setContentView(view);
        FullScreen();
        setupViewModels();
        init();

    }
    
    public void setupViewModels() {



        getDropDownViewModel = new ViewModelProvider(this).get(GetDropDownViewModel.class);
        getDropDownsObserveViewModel(getDropDownViewModel);
        
        addShoeProfileViewModel = new ViewModelProvider(this).get(AddShoeProfileViewModel.class);
        addShoeProfileObserveViewModel(addShoeProfileViewModel);
        
    }
    
    private void init() {
        mySharedPreference = getSharedPreferences(MyShardPreferences.mPreferenceName, MODE_PRIVATE);
        editor = mySharedPreference.edit();
        calendar = Calendar.getInstance();
        
        String token = mySharedPreference.getString(MyShardPreferences.token, "");
        binding.progressLayout.setVisibility(View.VISIBLE);
        getDropDownViewModel.getDropDown();
        
        
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
        
        String[] shoeList = new String[dropDownModel.getData().getShoes().size()];
        
        for (int i = 0; i < dropDownModel.getData().getShoes().size(); i++) {
            shoeList[i] = dropDownModel.getData().getShoes().get(i).getName();
        }
        
        binding.spinnerShoeBrand.setItems(shoeList);
        binding.spinnerShoeBrand.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {
            
            
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                if (item != null) {
                    int i = position + 1;
                    //   distanceType = i;
                    shoeBrand = item;
                    
                }
            }
        });
        
    }
    
    public void hideKeyBoardClicked(View view) {
        Log.wtf("-hide", "Hide keyboard Clicked");
        
        binding.editTextShoeWidth.clearFocus();
        binding.editTextShoeSize.clearFocus();
        binding.editTextProfileName.clearFocus();
        binding.editTextShoeModel.clearFocus();
        
        
        InputMethodManager imm = (InputMethodManager) getApplicationContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );
        
        
    }
    
    public void AddShoeClicked(View view) {
        
        String profile_name = binding.editTextProfileName.getText().toString();
        String model = binding.editTextShoeModel.getText().toString();
        String size = binding.editTextShoeSize.getText().toString();
        String width = binding.editTextShoeWidth.getText().toString();
        int comfort_rating = 0;
        comfort_rating = (int) binding.ratingBarShoeComfortWear.getRating();
        String comfortString = comfort_rating+"";
        
        
        boolean allCorrect = true;
        
        if (StringChecker.checkString(profile_name)) {
            binding.cardViewShoeBrand.setStrokeColor(getResources().getColor(R.color.colorPrimary));
        } else {
            allCorrect = false;
            binding.cardViewShoeBrand.setStrokeColor(getResources().getColor(R.color.stock_red));
        }
        
        if (StringChecker.checkString(shoeBrand)) {
            binding.cardViewShoeBrand.setStrokeColor(getResources().getColor(R.color.colorPrimary));
        } else {
            allCorrect = false;
            binding.cardViewShoeBrand.setStrokeColor(getResources().getColor(R.color.stock_red));
        }
        
        if (StringChecker.checkString(model)) {
            binding.cardViewShoeModel.setStrokeColor(getResources().getColor(R.color.colorPrimary));
        } else {
            allCorrect = false;
            binding.cardViewShoeModel.setStrokeColor(getResources().getColor(R.color.stock_red));
        }
    
        if (StringChecker.checkString(size)) {
            binding.cardViewShoeSize.setStrokeColor(getResources().getColor(R.color.colorPrimary));
        } else {
            allCorrect = false;
            binding.cardViewShoeSize.setStrokeColor(getResources().getColor(R.color.stock_red));
        }
    
        if (StringChecker.checkString(width)) {
            binding.cardViewShoeWidth.setStrokeColor(getResources().getColor(R.color.colorPrimary));
        } else {
            allCorrect = false;
            binding.cardViewShoeWidth.setStrokeColor(getResources().getColor(R.color.stock_red));
        }
    
        if (StringChecker.checkString(purchaseDate)) {
            binding.cardViewDatePurchased.setStrokeColor(getResources().getColor(R.color.colorPrimary));
        } else {
            allCorrect = false;
            binding.cardViewDatePurchased.setStrokeColor(getResources().getColor(R.color.stock_red));
        }
    
        AddShoeSendModel addShoesModel = new AddShoeSendModel();
        
        String token = mySharedPreference.getString(MyShardPreferences.token, "");
        
        addShoesModel.setToken(token);
       addShoesModel.setShoe_profile(profile_name);
        addShoesModel.setShoe_name(shoeBrand);
        addShoesModel.setModel(model);
        addShoesModel.setDate_purchased(purchaseDate);
        addShoesModel.setSize(size);
        addShoesModel.setWidth(width);
        addShoesModel.setComfort_rating(comfortString);
        
        
        if (allCorrect) {
            binding.progressLayout.setVisibility(View.VISIBLE);
            addShoeProfileViewModel.addShoeProfile(addShoesModel);
        } else {
            Snackbar.make(view, "Please fill required fields", Snackbar.LENGTH_SHORT).show();
        }
        
        
    }
    
    private void addShoeProfileObserveViewModel(final AddShoeProfileViewModel viewModel) {
        // Observe project data
        viewModel.getResponse().observe(this, apiResponse -> {
            
            if (apiResponse != null) {
                if (apiResponse.status == Status.SUCCESS) {
                    
                    Log.wtf("-this", "Add Shoe API Response : " + apiResponse.data);
                    Response<ResponseBody> responseBodyResponse = (Response<ResponseBody>) apiResponse.data;
                    binding.progressLayout.setVisibility(View.GONE);
                    try {
                        if (responseBodyResponse.body() != null) {
                            String json = responseBodyResponse.body().string();
                            Log.wtf("retrofit_", "Add Shoe response body: " + json);
                            JSONObject jsonObject = new JSONObject(json);
                            int status = jsonObject.getInt("status");
                            Log.wtf("retrofit_", "status : " + status);
                            
                            if (status == 1) {
                                String msg = jsonObject.getString("message");
                                Snackbar.make(view, "" + msg, Snackbar.LENGTH_SHORT).show();
                                //AppUtils.ShowShoeActivity=true;
                                Intent intent = new Intent(AddShoeActivity.this,ShoeActivity.class);
                              //  Intent intent = new Intent();
                              //  intent.putExtra("Done", true);
                                startActivity(intent);
                                finish();
                               // setResult(7, intent);

                               // intent.putExtra("Done", true);

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
                    Log.wtf("-this", "Add Shoe API Error Response : " + apiResponse.error.getMessage());
                    binding.progressLayout.setVisibility(View.GONE);
                   // Toast.makeText(this, "error", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(AddShoeActivity.this,ShoeActivity.class);
                 //   intent.putExtra("Done", true);
                    startActivity(intent);
                    //setResult(7, intent);
                    finish();//finishing activity
                }
            } else {
                binding.progressLayout.setVisibility(View.GONE);
                
            }
        });
    }
    
    public void ShoeDateClicked(View view) {
        hideKeyBoardClicked(view);
        binding.layoutPurchaseDate.setVisibility(View.VISIBLE);
        
//        int yy = calendar.get(Calendar.YEAR);
//        int mm = calendar.get(Calendar.MONTH);
//        int dd = calendar.get(Calendar.DAY_OF_MONTH);
//        binding.datePickerShoe.updateDate(yy, mm, dd);
        binding.datePickerShoe.setMaxDate(new Date().getTime());
    }
    
    public void ShoeDateDoneClicked(View view) {
        int day, month, year;
        
        day = binding.datePickerShoe.getDayOfMonth();
        month = binding.datePickerShoe.getMonth() + 1;
        year = binding.datePickerShoe.getYear();
        
        purchaseDate = getNumberFormat(year) + "-" + getNumberFormat(month) + "-" + getNumberFormat(day);
        
        Log.wtf("-Date", "purchase Date : " + purchaseDate);
        
        
        binding.textViewShoeDatePurchased.setText(DateFormatter.getFormattedDateFromDateString(purchaseDate));
        
        binding.layoutPurchaseDate.setVisibility(View.GONE);
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
}