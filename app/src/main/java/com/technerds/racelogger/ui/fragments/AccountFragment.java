package com.technerds.racelogger.ui.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.google.gson.Gson;
import com.technerds.racelogger.Utils.AppUtils;
import com.technerds.racelogger.Utils.DateFormatter;
import com.technerds.racelogger.dataModels.dashboardModel.DashboardModel;
import com.technerds.racelogger.network.Status;
import com.technerds.racelogger.R;
import com.technerds.racelogger.Utils.MyShardPreferences;
import com.technerds.racelogger.Utils.Urls;
import com.technerds.racelogger.ui.ShoeActivity;
import com.technerds.racelogger.viewModels.GetProfileViewModel;
import com.technerds.racelogger.dataModels.getProfileModel.GetProfileModel;
import com.technerds.racelogger.databinding.FragmentAccountBinding;
import com.technerds.racelogger.ui.HomeActivity;
import com.technerds.racelogger.ui.login.EditProfileActivity;
import com.technerds.racelogger.viewModels.GetUserDashboardViewModel;

import java.io.IOException;
import java.text.DecimalFormat;

import okhttp3.ResponseBody;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;


public class AccountFragment extends Fragment implements View.OnClickListener {
    
    FragmentAccountBinding binding;
    View view;
    SharedPreferences.Editor editor;
    SharedPreferences mySharedPreference;
    
    GetProfileViewModel getProfileViewModel;
    GetProfileModel getProfileModel;
    
    GetUserDashboardViewModel getUserDashboardViewModel;
    DashboardModel dashboardModel;
    
    
    public AccountFragment() {
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentAccountBinding.inflate(getLayoutInflater());
        setupViewModels();
        init();
        view = binding.getRoot();
        return view;
    }
    
    public void setupViewModels() {
        
        getProfileViewModel = new ViewModelProvider(this).get(GetProfileViewModel.class);
        getProfileObserveViewModel(getProfileViewModel);
    
        getUserDashboardViewModel = new ViewModelProvider(this).get(GetUserDashboardViewModel.class);
        getDashboardObserveViewModel(getUserDashboardViewModel);
    }
    
    private void init() {
        mySharedPreference = getContext().getSharedPreferences(MyShardPreferences.mPreferenceName, MODE_PRIVATE);
        editor = mySharedPreference.edit();
        
        setOnClickListeners();
        String token = mySharedPreference.getString(MyShardPreferences.token, "");
        binding.progressLayout.setVisibility(View.VISIBLE);
        
        getProfileViewModel.getProfile(token);
        getUserDashboardViewModel.getUserDashboard(token);
        
    }
    
    private void getProfileObserveViewModel(final GetProfileViewModel viewModel) {
        // Observe project data
        viewModel.getResponse().observe(getViewLifecycleOwner(), apiResponse -> {
            
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
        Log.wtf("-this", "  profile_Image  : "+profile_Image);
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
        
        
        binding.textViewName.setText(model.getData().getFirstName() + " " + model.getData().getLastName());
        binding.textViewEmail.setText(model.getData().getEmail() + "");
        binding.textViewDOB.setText(DateFormatter.getFormattedDateFromDateString(model.getData().getDob()) + "");
        binding.textViewSex.setText(model.getData().getGender() + "");
        binding.textViewWeight.setText(model.getData().getWeight() + " "+model.getData().getWeightUnit());
        binding.textViewAddress.setText(model.getData().getAddress() + "");
        binding.textViewYearRunning.setText(model.getData().getStartRunning() + "");
        
        
    }
    
    private void getDashboardObserveViewModel(final GetUserDashboardViewModel viewModel) {
        // Observe project data
        viewModel.getResponse().observe(getViewLifecycleOwner(), apiResponse -> {
            
            if (apiResponse != null) {
                if (apiResponse.status == Status.SUCCESS) {
                    binding.progressLayout.setVisibility(View.GONE);
                    Log.wtf("-this", "User Dashboard Response : " + apiResponse.data);
                    Response<ResponseBody> responseBodyResponse = (Response<ResponseBody>) apiResponse.data;
                    
                    try {
                        if (responseBodyResponse.body() != null) {
                            String json = responseBodyResponse.body().string();
                            Log.wtf("retrofit_", "User Dashboard response body: " + json);
                            
                            Gson gson = new Gson();
                            dashboardModel = gson.fromJson(json, DashboardModel.class);
                            setProgressValues(dashboardModel);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        
                    }
                    
                } else if (apiResponse.status == Status.ERROR) {
                    Log.wtf("-this", "User Dashboard Response : " + apiResponse.error.getMessage());
                    binding.progressLayout.setVisibility(View.GONE);
                }
            } else {
                binding.progressLayout.setVisibility(View.GONE);
            }
        });
    }
    
    public void setProgressValues(DashboardModel model) {
    
        double careerMiles = model.getData().getRace().getCarrerMiles() + model.getData().getRun().getCarrerMiles();
        double monthMiles = model.getData().getRace().getCurrentMonthMiles() + model.getData().getRun().getCurrentMonthMiles();
        double weekMiles = model.getData().getRace().getCurrentWeekMiles() + model.getData().getRun().getCurrentWeekMiles();
        double yearMiles = model.getData().getRace().getCurrentYearMiles() + model.getData().getRun().getCurrentYearMiles();
        double todayMiles = model.getData().getRace().getTodayMiles() + model.getData().getRun().getTodayMiles();
    
        DecimalFormat numberFormat = new DecimalFormat("#.0");

        if(careerMiles==0.0){
            binding.textViewTotalMiles.setText(""+String.valueOf(careerMiles));
        }else {
            binding.textViewTotalMiles.setText(""+numberFormat.format(careerMiles));
        }

        if(monthMiles==0.0){
            binding.textViewMonthMiles.setText(""+String.valueOf(monthMiles));
        }else {
            binding.textViewMonthMiles.setText(""+numberFormat.format(monthMiles));
        }

        if(yearMiles==0.0){
            binding.textViewYearMiles.setText(""+String.valueOf(yearMiles));
        }else {
            binding.textViewYearMiles.setText(""+numberFormat.format(yearMiles));
        }

        if(weekMiles==0.0){
            binding.textViewWeekMiles.setText(""+String.valueOf(weekMiles));
        }else {
            binding.textViewWeekMiles.setText(""+numberFormat.format(weekMiles));
        }

        if(todayMiles==0.0){
            binding.textViewDayMiles.setText(""+String.valueOf(todayMiles));
        }else {
            binding.textViewDayMiles.setText(""+numberFormat.format(todayMiles));
        }
    
        //binding.textViewTotalMiles.setText(""+numberFormat.format(careerMiles));
        //binding.textViewMonthMiles.setText(""+numberFormat.format(monthMiles));
       // binding.textViewYearMiles.setText(""+numberFormat.format(yearMiles));
       // binding.textViewWeekMiles.setText(""+numberFormat.format(weekMiles));
      //  binding.textViewDayMiles.setText(""+numberFormat.format(todayMiles));
        
    }
    
    
    void setOnClickListeners() {
        binding.progressLayout.setOnClickListener(this);
        binding.textViewLogout.setOnClickListener(this);
        binding.imageViewEdit.setOnClickListener(this);
        binding.cardViewShoe.setOnClickListener(this);
        
    }
    
    
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.textViewLogout:
                ((HomeActivity) getActivity()).LogoutClicked();
                break;
            
            case R.id.imageViewEdit:
                Intent intent = new Intent(getContext(), EditProfileActivity.class);
                startActivityForResult(intent, 7);
                break;
    
            case R.id.cardViewShoe:
                AppUtils.ShowShoeActivity=true;
                Intent intent1 = new Intent(getContext(), ShoeActivity.class);
                startActivity(intent1);
                getActivity().finishAffinity();
                break;
            
            default:
            
        }
        
    }
    
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 7) {
            if (data != null) {
                
                boolean done = data.getBooleanExtra("Done", false);
                Log.wtf("-this", " OnActivity result  220 : " + done);
                
                if (done) {
                    Log.wtf("-this", " OnActivity result  220 : Deleted");
                    init();
                    
                } else {
                
                }
            }
        }
    }
    
}
