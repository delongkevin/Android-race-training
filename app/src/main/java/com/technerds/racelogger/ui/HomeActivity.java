package com.technerds.racelogger.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentTransaction;

import com.technerds.racelogger.R;
import com.technerds.racelogger.Utils.AppUtils;
import com.technerds.racelogger.Utils.MyShardPreferences;
import com.technerds.racelogger.databinding.ActivityHomeBinding;
import com.technerds.racelogger.ui.login.SplashActivity;
import com.technerds.racelogger.ui.fragments.AccountFragment;
import com.technerds.racelogger.ui.fragments.AnalyticsFragment;
import com.technerds.racelogger.ui.fragments.HomeFragment;
import com.technerds.racelogger.ui.fragments.RaceFragment;
import com.technerds.racelogger.ui.fragments.RunFragment;

public class HomeActivity extends AppCompatActivity {

    ActivityHomeBinding binding;
    View view;
    SharedPreferences.Editor editor;
    SharedPreferences mySharedPreference;
    private String selectedFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //  getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setFlags(WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES, WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES);
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.white_opac_thirty));
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        view = binding.getRoot();
        setContentView(view);
        FullScreen();
        setupViewModels();

        mySharedPreference = getSharedPreferences(MyShardPreferences.mPreferenceName, MODE_PRIVATE);
        editor = mySharedPreference.edit();
        if (!AppUtils.ShowShoeActivity) {
            init();
        } else {


            AppUtils.ShowShoeActivity = false;
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(binding.fragmentContainer.getId(), new AccountFragment());
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
            resetAllColor();
            binding.imageViewAccount.setImageResource(R.mipmap.account_green);
            binding.textViewAccount.setTextColor(getResources().getColor(R.color.colorPrimary));
            selectedFragment = "Account";
        }
    }

    public void setupViewModels() {

    }

    private void init() {


        if (AppUtils.ShowRaceFragment) {
            AppUtils.ShowRaceFragment = false;
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(binding.fragmentContainer.getId(), new RaceFragment());
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();

            resetAllColor();
            binding.imageViewRace.setImageResource(R.mipmap.race_green);
            binding.textViewRace.setTextColor(getResources().getColor(R.color.colorPrimary));
            selectedFragment = "Race";
        } else {
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(binding.fragmentContainer.getId(), new HomeFragment());
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
            resetAllColor();
            binding.imageViewHome.setImageResource(R.mipmap.home_green);
            binding.textViewHome.setTextColor(getResources().getColor(R.color.colorPrimary));
            selectedFragment = "Home";
        }

    }

    void resetAllColor() {
        binding.imageViewHome.setImageResource(R.mipmap.home);
        binding.textViewHome.setTextColor(getResources().getColor(R.color.home_text_color));

        binding.imageViewRace.setImageResource(R.mipmap.race);
        binding.textViewRace.setTextColor(getResources().getColor(R.color.home_text_color));

        binding.imageViewRun.setImageResource(R.mipmap.run);
        binding.textViewRun.setTextColor(getResources().getColor(R.color.home_text_color));

        binding.imageViewAnalyze.setImageResource(R.mipmap.analyze);
        binding.textViewAnalyze.setTextColor(getResources().getColor(R.color.home_text_color));

        binding.imageViewAccount.setImageResource(R.mipmap.account);
        binding.textViewAccount.setTextColor(getResources().getColor(R.color.home_text_color));

    }

    public void HomeClicked(View view) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(binding.fragmentContainer.getId(), new HomeFragment());
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
        resetAllColor();
        binding.imageViewHome.setImageResource(R.mipmap.home_green);
        binding.textViewHome.setTextColor(getResources().getColor(R.color.colorPrimary));
        selectedFragment = "Home";
    }

    public void RaceClicked(View view) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(binding.fragmentContainer.getId(), new RaceFragment());
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

        resetAllColor();
        binding.imageViewRace.setImageResource(R.mipmap.race_green);
        binding.textViewRace.setTextColor(getResources().getColor(R.color.colorPrimary));
        selectedFragment = "Race";
    }

    public void RunClicked(View view) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(binding.fragmentContainer.getId(), new RunFragment());
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

        resetAllColor();
        binding.imageViewRun.setImageResource(R.mipmap.run_green);
        binding.textViewRun.setTextColor(getResources().getColor(R.color.colorPrimary));
        selectedFragment = "Run";
    }

    public void AnalyzeClicked(View view) {
        Log.wtf("-this", "ANalytics Called");
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(binding.fragmentContainer.getId(), new AnalyticsFragment());
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
        resetAllColor();
        binding.imageViewAnalyze.setImageResource(R.mipmap.analyze_green);
        binding.textViewAnalyze.setTextColor(getResources().getColor(R.color.colorPrimary));
        selectedFragment = "Analyze";
    }

    public void SettingsClicked(View view) {
        Log.wtf("-this", "Account Called");
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(binding.fragmentContainer.getId(), new AccountFragment());
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
        resetAllColor();
        binding.imageViewAccount.setImageResource(R.mipmap.account_green);
        binding.textViewAccount.setTextColor(getResources().getColor(R.color.colorPrimary));
        selectedFragment = "Account";
    }

    public void LogoutClicked() {
        mySharedPreference.edit().clear().apply();
        Intent intent = new Intent(HomeActivity.this, SplashActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
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
    protected void onResume() {
        super.onResume();

      /*  if (AppUtils.ShowRaceFragment) {
            AppUtils.ShowRaceFragment=false;
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(binding.fragmentContainer.getId(), new RaceFragment());
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();

            resetAllColor();
            binding.imageViewRace.setImageResource(R.mipmap.race_green);
            binding.textViewRace.setTextColor(getResources().getColor(R.color.colorPrimary));
            selectedFragment = "Race";
         //   Toast.makeText(this, "Yes Shows Please", Toast.LENGTH_SHORT).show();
        }*/
    }
}