package com.technerds.racelogger.ui.login;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.snackbar.Snackbar;
import com.technerds.racelogger.network.Status;
import com.technerds.racelogger.R;
import com.technerds.racelogger.Utils.MyShardPreferences;
import com.technerds.racelogger.viewModels.authentication.ForgotPassowrdViewModel;
import com.technerds.racelogger.databinding.ActivityForgotPasswordBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Response;


public class ForgotPasswordActivity extends AppCompatActivity {
    ActivityForgotPasswordBinding binding;
    View view;
    ForgotPassowrdViewModel forgotPassowrdViewModel;
    SharedPreferences.Editor editor;
    SharedPreferences mySharedPreference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //  getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setFlags(WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES, WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES);
        binding = ActivityForgotPasswordBinding.inflate(getLayoutInflater());
        view = binding.getRoot();
        setContentView(view);
        FullScreen();
        setupViewModels();
        init();
    }

    public void setupViewModels() {
        forgotPassowrdViewModel = new ViewModelProvider(this).get(ForgotPassowrdViewModel.class);
        ForgotPasswordObserveViewModel(forgotPassowrdViewModel);

    }

    private void init() {
        mySharedPreference = getSharedPreferences(MyShardPreferences.mPreferenceName, MODE_PRIVATE);
        editor = mySharedPreference.edit();

    }


    public void RequestClicked(View view) {
        hideKeyBoardClicked(view);
        String email = binding.editTextEmail.getText().toString();
        boolean allCorrect = true;

       

        if (email != null && (!email.equalsIgnoreCase("")) && email != "") {
            binding.cardViewEmail.setStrokeColor(getResources().getColor(R.color.colorPrimary));
        } else {
            allCorrect = false;
            binding.cardViewEmail.setStrokeColor(getResources().getColor(R.color.stock_red));
        }
        

      

        if (allCorrect ) {
            Snackbar.make(view, "All Correct", Snackbar.LENGTH_SHORT).show();

            binding.progressLayout.setVisibility(View.VISIBLE);
            forgotPassowrdViewModel.forgotPassword(email);

        } else {
            Snackbar.make(view, "Fill all required fields", Snackbar.LENGTH_SHORT).show();
        }

    }


    private void ForgotPasswordObserveViewModel(final ForgotPassowrdViewModel viewModel) {
        // Observe project data
        viewModel.getResponse().observe(this, apiResponse -> {

            if (apiResponse != null) {
                if (apiResponse.status == Status.SUCCESS) {

                    Log.wtf("-this", "Forgot Password API Response : " + apiResponse.data);
                    Response<ResponseBody> responseBodyResponse = (Response<ResponseBody>) apiResponse.data;
                    binding.progressLayout.setVisibility(View.GONE);
                    try {
                        if (responseBodyResponse.body() != null) {
                            String json = responseBodyResponse.body().string();
                            Log.wtf("retrofit_", "Forgot Password response body: " + json);
                            JSONObject jsonObject = new JSONObject(json);
                            int status = jsonObject.getInt("status");
                            Log.wtf("retrofit_", "status : " + status);
                            if (status == 1) {
                                startActivity(new Intent(this, VerifyCodeActivity.class));
                                finish();
                            
                            } else {
                                String error = jsonObject.getString("error");
                                Snackbar.make(view,error,Snackbar.LENGTH_SHORT).show();
                            }
                        } else {
                            Log.wtf("-this", "Forgot Password Response : Null");
                        }


                    } catch (IOException | JSONException e) {
                        e.printStackTrace();

                    }

                } else if (apiResponse.status == Status.ERROR) {
                    Log.wtf("-this", "Forgot Password API Response : " + apiResponse.error.getMessage());
                    binding.progressLayout.setVisibility(View.GONE);
                }
            }
        });
    }

    public void hideKeyBoardClicked(View view) {
        binding.editTextEmail.clearFocus();


        InputMethodManager imm = (InputMethodManager) getApplicationContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

    }

    public void SignUpClicked(View view) {
        startActivity(new Intent(this, SignupActivity.class));
        //  finish();
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
    

}