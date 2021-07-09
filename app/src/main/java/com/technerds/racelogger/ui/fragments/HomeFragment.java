package com.technerds.racelogger.ui.fragments;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.google.gson.Gson;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.technerds.racelogger.Utils.AgeGroups;
import com.technerds.racelogger.Utils.DateFormatter;
import com.technerds.racelogger.Utils.SpeedyLinearLayoutManager;
import com.technerds.racelogger.Utils.Trivia;
import com.technerds.racelogger.adapters.RaceAdapter;
import com.technerds.racelogger.adapters.TriviaAdapter;
import com.technerds.racelogger.dataModels.WeatherModels.WeatherModel;
import com.technerds.racelogger.dataModels.dashboardModel.DashboardModel;
import com.technerds.racelogger.dataModels.raceDetailModel.RaceData;
import com.technerds.racelogger.network.Status;
import com.technerds.racelogger.R;
import com.technerds.racelogger.Utils.AppUtils;
import com.technerds.racelogger.Utils.MyShardPreferences;
import com.technerds.racelogger.Utils.TemperatureConverter;
import com.technerds.racelogger.ui.RaceDetailActivity;
import com.technerds.racelogger.viewModels.GetUserDashboardViewModel;
import com.technerds.racelogger.viewModels.GetWeatherViewModel;
import com.technerds.racelogger.databinding.FragmentHomeBinding;
import com.tsuryo.swipeablerv.SwipeLeftRightCallback;

import java.io.IOException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.ResponseBody;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;


public class HomeFragment extends Fragment implements View.OnClickListener, LocationListener {

    FragmentHomeBinding binding;
    View view;
    SharedPreferences.Editor editor;
    SharedPreferences mySharedPreference;
    GetWeatherViewModel getWeatherViewModel;
    WeatherModel weatherModel;

    GetUserDashboardViewModel getUserDashboardViewModel;
    DashboardModel dashboardModel;

    FusedLocationProviderClient mFusedLocationClient;
    Location mLastLocation = null;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;
    LocationManager locationManager;
    long timeDiff = 0;
    String raceDate = null;
    Timer T = new Timer();
    private boolean raceDatePositive = true;

    TriviaAdapter triviaAdapter;
    ArrayList<String> triviaList = new ArrayList<>();
    private String careerSelected = "All";


    public HomeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(getLayoutInflater());
        setupViewModels();
        init();
        view = binding.getRoot();
        return view;
    }

    public void setupViewModels() {
        getWeatherViewModel = new ViewModelProvider(this).get(GetWeatherViewModel.class);
        weatherObserveViewModel(getWeatherViewModel);

        getUserDashboardViewModel = new ViewModelProvider(this).get(GetUserDashboardViewModel.class);
        getDashboardObserveViewModel(getUserDashboardViewModel);
    }

    private void init() {
        mySharedPreference = getContext().getSharedPreferences(MyShardPreferences.mPreferenceName, MODE_PRIVATE);
        editor = mySharedPreference.edit();
        setTrivia();
        setOnClickListeners();
        startNewTimerStop();
        String token = mySharedPreference.getString(MyShardPreferences.token, "");
        Log.wtf("-this", " Home Token : " + token);
        binding.progressLayout.setVisibility(View.VISIBLE);
        getUserDashboardViewModel.getUserDashboard(token);

        locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getContext());

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

        getLocationNew(getContext());

        if (mLastLocation != null) {
            getWeatherViewModel.getWeatherByCoord(getString(R.string.open_weather_api_key), mLastLocation.getLatitude(), mLastLocation.getLongitude());
        } else {
            String zip = mySharedPreference.getString(MyShardPreferences.zipCode, null);
            if (zip != null) {
                getWeatherViewModel.getWeatherByZip(getString(R.string.open_weather_api_key), zip);
            }
        }
        String date = new SimpleDateFormat("MM/dd/yyyy").format(Calendar.getInstance().getTime());
        binding.textViewCurrentDate.setText("" + date);


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
                            setValues(dashboardModel);
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

    public void setValues(DashboardModel model) {

        setALLMile(model);
        binding.spinnerCareer.setItems("All", "Race", "Run");
        binding.spinnerCareer.setSelectedIndex(0);
        binding.spinnerCareer.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                if (item != null) {
                    careerSelected = item;
                    if (careerSelected.equalsIgnoreCase("All")) {
                        setALLMile(model);

                    } else if (careerSelected.equalsIgnoreCase("Race")) {
                        setRaceMile(model);

                    } else if (careerSelected.equalsIgnoreCase("Run")) {
                        setRunMile(model);
                    } else {
                        binding.textViewCareerMiles.setText("");
                        binding.textViewMonthMiles.setText("");
                        binding.textViewYearMiles.setText("");
                        binding.textViewWeekMiles.setText("");
                    }

                }
            }
        });


        int raceCount = model.getData().getRace().getTotalRaces();
        if (raceCount > 24) {
            binding.imageViewBronze.setImageResource(R.mipmap.bronze);
        } else {
            binding.imageViewBronze.setImageResource(R.mipmap.bronze_uncheck);
        }
        if (raceCount > 49) {
            binding.imageViewSilver.setImageResource(R.mipmap.silver);
        } else {
            binding.imageViewSilver.setImageResource(R.mipmap.silver_uncheck);
        }
        if (raceCount > 99) {
            binding.imageViewGold.setImageResource(R.mipmap.gold);
        } else {
            binding.imageViewGold.setImageResource(R.mipmap.gold_uncheck);
        }
        if (raceCount > 249) {
            binding.imageViewPlatinum.setImageResource(R.mipmap.platinum);
        } else {
            binding.imageViewPlatinum.setImageResource(R.mipmap.platinum_uncheck);
        }
        if (raceCount > 499) {
            binding.imageViewTitanium.setImageResource(R.mipmap.titanium);
        } else {
            binding.imageViewTitanium.setImageResource(R.mipmap.titanium_uncheck);
        }


        if (model.getData().getNextRace() != null) {
            Log.wtf("-this", " 245 SetValues : ");
            binding.layoutEvent.setVisibility(View.VISIBLE);
            binding.cardViewNoEvent.setVisibility(View.INVISIBLE);
            raceDate = dashboardModel.getData().getNextRace().getDate();
            binding.textViewRaceName.setText("" + model.getData().getNextRace().getRaeName());
            binding.textViewRaceLocation.setText("" + model.getData().getNextRace().getStreetAddress());
            binding.textViewRaceDate.setText("" + DateFormatter.getFormattedDateTimeFromDateTimeStringAmPm(model.getData().getNextRace().getDate()));
            if (dashboardModel.getData().getNextRace().getNotes() != null) {
                Log.wtf("-this", " 253 SetValues : ");
                binding.textViewRaceDescription.setText("" + model.getData().getNextRace().getNotes());
            } else {
                Log.wtf("-this", " 256 SetValues : ");
                binding.textViewRaceDescription.setText("");
            }
        } else {
            Log.wtf("-this", " 260 SetValues : ");
            binding.layoutEvent.setVisibility(View.INVISIBLE);
            binding.cardViewNoEvent.setVisibility(View.VISIBLE);
        }

        Log.wtf("-this", " 265 SetValues : ");

    }

    private void setALLMile(DashboardModel model) {
        double careerMiles = model.getData().getRace().getCarrerMiles() + model.getData().getRun().getCarrerMiles();
        double monthMiles = model.getData().getRace().getCurrentMonthMiles() + model.getData().getRun().getCurrentMonthMiles();
        double weekMiles = model.getData().getRace().getCurrentWeekMiles() + model.getData().getRun().getCurrentWeekMiles();
        double yearMiles = model.getData().getRace().getCurrentYearMiles() + model.getData().getRun().getCurrentYearMiles();
        double todayMiles = model.getData().getRace().getTodayMiles() + model.getData().getRun().getTodayMiles();

        DecimalFormat numberFormat = new DecimalFormat("#.0");

        if (careerMiles == 0.0) {
            binding.textViewCareerMiles.setText("" + String.valueOf(careerMiles));
        } else {
            binding.textViewCareerMiles.setText("" + numberFormat.format(careerMiles));
        }
        if (monthMiles == 0.0) {
            binding.textViewMonthMiles.setText("" + String.valueOf(monthMiles));
        } else {
            binding.textViewMonthMiles.setText("" + numberFormat.format(monthMiles));
        }

        if (yearMiles == 0.0) {
            binding.textViewYearMiles.setText("" + String.valueOf(yearMiles));
        } else {
            binding.textViewYearMiles.setText("" + numberFormat.format(yearMiles));
        }

        if (weekMiles == 0.0) {
            binding.textViewWeekMiles.setText("" + String.valueOf(weekMiles));
        } else {
            binding.textViewWeekMiles.setText("" + numberFormat.format(weekMiles));
        }

        // binding.textViewCareerMiles.setText("" + numberFormat.format(careerMiles));
        // binding.textViewMonthMiles.setText("" + numberFormat.format(monthMiles));
        //  binding.textViewYearMiles.setText("" + numberFormat.format(yearMiles));
        //   binding.textViewWeekMiles.setText("" + numberFormat.format(weekMiles));

    }

    private void setRaceMile(DashboardModel model) {
        double careerMiles = model.getData().getRace().getCarrerMiles();
        double monthMiles = model.getData().getRace().getCurrentMonthMiles();
        double weekMiles = model.getData().getRace().getCurrentWeekMiles();
        double yearMiles = model.getData().getRace().getCurrentYearMiles();
        double todayMiles = model.getData().getRace().getTodayMiles();

        DecimalFormat numberFormat = new DecimalFormat("#.0");

        if (careerMiles == 0.0) {
            binding.textViewCareerMiles.setText("" + String.valueOf(careerMiles));
        } else {
            binding.textViewCareerMiles.setText("" + numberFormat.format(careerMiles));
        }

        if (monthMiles == 0.0) {
            binding.textViewMonthMiles.setText("" + String.valueOf(monthMiles));
        } else {
            binding.textViewMonthMiles.setText("" + numberFormat.format(monthMiles));
        }

        if (yearMiles == 0.0) {
            binding.textViewYearMiles.setText("" + String.valueOf(yearMiles));
        } else {
            binding.textViewYearMiles.setText("" + numberFormat.format(yearMiles));
        }

        if (weekMiles == 0.0) {
            binding.textViewWeekMiles.setText("" + String.valueOf(weekMiles));
        } else {
            binding.textViewWeekMiles.setText("" + numberFormat.format(weekMiles));
        }
        // binding.textViewCareerMiles.setText("" + numberFormat.format(careerMiles));
        //      binding.textViewMonthMiles.setText("" + numberFormat.format(monthMiles));
        //   binding.textViewYearMiles.setText("" + numberFormat.format(yearMiles));
        // binding.textViewWeekMiles.setText("" + numberFormat.format(weekMiles));

    }

    private void setRunMile(DashboardModel model) {
        double careerMiles = model.getData().getRun().getCarrerMiles();
        double monthMiles = model.getData().getRun().getCurrentMonthMiles();
        double weekMiles = model.getData().getRun().getCurrentWeekMiles();
        double yearMiles = model.getData().getRun().getCurrentYearMiles();
        double todayMiles = model.getData().getRun().getTodayMiles();

        DecimalFormat numberFormat = new DecimalFormat("#.0");

        if (careerMiles == 0.0) {
            binding.textViewCareerMiles.setText("" + String.valueOf(careerMiles));
        } else {
            binding.textViewCareerMiles.setText("" + numberFormat.format(careerMiles));
        }

        if (monthMiles == 0.0) {
            binding.textViewMonthMiles.setText("" + String.valueOf(monthMiles));
        } else {
            binding.textViewMonthMiles.setText("" + numberFormat.format(monthMiles));
        }

        if (yearMiles == 0.0) {
            binding.textViewYearMiles.setText("" + String.valueOf(yearMiles));
        } else {
            binding.textViewYearMiles.setText("" + numberFormat.format(yearMiles));
        }

        if (weekMiles == 0.0) {
            binding.textViewWeekMiles.setText("" + String.valueOf(weekMiles));
        } else {
            binding.textViewWeekMiles.setText("" + numberFormat.format(weekMiles));
        }
        // binding.textViewCareerMiles.setText("" + numberFormat.format(careerMiles));
        // binding.textViewMonthMiles.setText("" + numberFormat.format(monthMiles));
        // binding.textViewYearMiles.setText("" + numberFormat.format(yearMiles));
      //  binding.textViewWeekMiles.setText("" + numberFormat.format(weekMiles));


    }

    public void RaceDetailClicked() {
        if (raceDate != null) {
            Intent intent = new Intent(getContext(), RaceDetailActivity.class);
            AppUtils.race_id=dashboardModel.getData().getNextRace().getId();
            intent.putExtra("race_id", dashboardModel.getData().getNextRace().getId());
            startActivity(intent);
        }else {
            Toast.makeText(getActivity(), "race date is null"+raceDate, Toast.LENGTH_SHORT).show();
        }
    }

    private void setCountDown() {
        //    Log.wtf("-this", " 273 setCountDown : ");
        try {
            Calendar calendar = Calendar.getInstance();
            Date date1 = calendar.getTime();

            //  Log.wtf("-this", "Date Current Date Time: " + date1);

            DateFormat f = new SimpleDateFormat("yyy-MM-dd hh:mm:ss", Locale.ENGLISH);
            Date date2 = f.parse(raceDate);
            // Log.wtf("-this", "Race Date Formatted : " + date2);

            timeDiff = printDifference(date1, date2);

        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    public long printDifference(Date startDate, Date endDate) {
        //   Log.wtf("-this", " 293 printDifference : ");
        //milliseconds
        long differentSec = endDate.getTime() - startDate.getTime();
        //  differentSec = differentSec /1000;
        long different = endDate.getTime() - startDate.getTime();


//        Log.wtf("-this", "startDate : " + startDate);
//        Log.wtf("-this", "endDate : " + endDate);
//        Log.wtf("-this", "different : " + different);

        if (different > 0) {

            long secondsInMilli = 1000;
            long minutesInMilli = secondsInMilli * 60;
            long hoursInMilli = minutesInMilli * 60;
            long daysInMilli = hoursInMilli * 24;

            long elapsedDays = different / daysInMilli;
            different = different % daysInMilli;

            long elapsedHours = different / hoursInMilli;
            different = different % hoursInMilli;

            long elapsedMinutes = different / minutesInMilli;
            different = different % minutesInMilli;

            long elapsedSeconds = different / secondsInMilli;

//        System.out.printf(
//                "%d days, %d hours, %d minutes, %d seconds%n",
//                elapsedDays, elapsedHours, elapsedMinutes, elapsedSeconds);

            if (getActivity() != null) {
                getActivity().runOnUiThread(new Runnable() {

                    @Override
                    public void run() {

                        //  Log.wtf("-this","325 Days : "+elapsedDays);
                        if (elapsedDays > 0) {
                            binding.layoutDays.setVisibility(View.VISIBLE);
                            binding.textViewCountDownDays.setText(elapsedDays + "");
                        } else {
                            binding.layoutDays.setVisibility(View.GONE);
                        }
                        binding.textViewCountDown.setText(elapsedHours + ":" + elapsedMinutes + ":" + elapsedSeconds);
                    }
                });
            }
        }

        return differentSec;
    }

    private void weatherObserveViewModel(final GetWeatherViewModel viewModel) {
        // Observe project data
        viewModel.getResponse().observe(getViewLifecycleOwner(), apiResponse -> {

            if (apiResponse != null) {
                if (apiResponse.status == Status.SUCCESS) {

                    // Log.wtf("-this", "Weather API Response : " + apiResponse.data);
                    Response<ResponseBody> responseBodyResponse = (Response<ResponseBody>) apiResponse.data;

                    try {
                        if (responseBodyResponse.body() != null) {
                            String json = responseBodyResponse.body().string();
                            //  Log.wtf("retrofit_", "Weather API response body: " + json);

                            Gson gson = new Gson();
                            weatherModel = gson.fromJson(json, WeatherModel.class);
                            String iconurl = "http://openweathermap.org/img/w/" + weatherModel.getWeather().get(0).getIcon() + ".png";
                            setWeather(weatherModel);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();

                    }

                } else if (apiResponse.status == Status.ERROR) {
                    Log.wtf("-this", "Weather API Response : " + apiResponse.error.getMessage());
                }
            }
        });
    }

    private void setWeather(WeatherModel model) {
        String iconurl = "http://openweathermap.org/img/w/" + model.getWeather().get(0).getIcon() + ".png";

        //   Log.wtf("-this", "Icon URl : " + iconurl);

        Glide.with(this)
                .load(iconurl)
                .apply(new RequestOptions().centerCrop()
                        .dontAnimate().skipMemoryCache(true))
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {

//
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {

                        return false;
                    }
                })
                .into((binding.imageViewWeather));

        binding.textViewWeather.setText("" + model.getWeather().get(0).getMain());
        double kelvin = model.getMain().getTemp();

        TemperatureConverter temperatureConverter = new TemperatureConverter();

        temperatureConverter.setKelvin(kelvin);

        int celcius = (int) temperatureConverter.getCelsius();

        binding.textViewTemp.setText(celcius + "");

        binding.textViewCurrentLoc.setText("" + model.getName());
    }

    int count = 0;
    Handler handler = new Handler();
    private Runnable runnableCode = new Runnable() {
        @Override
        public void run() {
            // Do something here on the main thread
//            Log.wtf("Handlers", "Called on main thread");
            // Repeat this the same runnable code block again another 2 seconds
            // 'this' is referencing the Runnable object
            if (getActivity() != null) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //  Log.wtf("Handlers", "Called on main thread");

                        if (count < triviaAdapter.getItemCount()) {
                            binding.recyclerTrivia.smoothScrollToPosition(count);
                            count++;
                        } else {
                            count = 0;
                        }
                    }
                });
            }

            handler.postDelayed(this, 5000);
        }
    };

    private void setTrivia() {

        triviaList = Trivia.getTriviaList();
        binding.recyclerTrivia.removeAllViews();
        binding.recyclerTrivia.setAdapter(null);

        binding.recyclerTrivia.setLayoutManager(new SpeedyLinearLayoutManager(getContext(), SpeedyLinearLayoutManager.VERTICAL, false));
        Log.wtf("-this", "430 Trivia List Size : " + triviaList.size());
        if (triviaList.size() > 0) {
            triviaAdapter = new TriviaAdapter(getContext(), triviaList, this);
            binding.recyclerTrivia.setAdapter(triviaAdapter);
            count = 0;
            handler.post(runnableCode);
        } else {
            handler.removeCallbacks(runnableCode);
        }

    }

    void setOnClickListeners() {
        binding.progressLayout.setOnClickListener(this);

        binding.cardViewEvent.setOnClickListener(this);


    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.cardViewEvent) {

            RaceDetailClicked();
        }

    }


    public void startNewTimerStop() {

        T.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Runnable rn = (new Runnable() {
                    @Override
                    public void run() {
                        if (raceDate != null) {
                            setCountDown();
                        }
                    }
                });
                rn.run();
            }
        }, 1000, 1000);


    }

    public Date getFormattedDate(String date) {
        Date newDate = null;
        if (date != null) {
            try {
                SimpleDateFormat spf = new SimpleDateFormat("dd-MM-YYYY hh:mm:ss",Locale.ENGLISH);
                newDate = spf.parse(date);

                Log.wtf("-Date", "Formatted Date : " + newDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            return newDate;
        } else {
            return newDate;
        }

    }

    public String getFormattedEXpDate(String date) {
        if (date != null) {
            try {
                SimpleDateFormat spf = new SimpleDateFormat("YYY-MM-dd",Locale.ENGLISH);
                Date newDate = spf.parse(date);
                spf = new SimpleDateFormat("dd MMM");
                date = spf.format(newDate);
                System.out.println(date);
                Log.wtf("-Date", "Splash Date : " + date);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            return date;
        } else {
            return "";
        }

    }


    //Location

    @Override
    public void onLocationChanged(Location mlocation) {
        Log.wtf("-this", "Location Changed : " + mlocation);
        if (mlocation != null) {
            locationManager.removeUpdates(this);
            getLocationNew(getContext());
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.wtf("-this", "Status Changed : " + status);

    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.wtf("-this", "Provider Enabled: " + provider);
        getLocationNew(getContext());

    }

    @Override
    public void onProviderDisabled(String provider) {


    }

    Dialog dialog;

    void getLocationNew(Context context) {
        if (!AppUtils.isLocationEnabled(context)) {


            dialog = new Dialog(context);
            dialog.setCancelable(true);
            dialog.setContentView(R.layout.prompt);

            TextView textViewMain = (TextView) dialog.findViewById(R.id.textViewMain);
            Button btnPos = (Button) dialog.findViewById(R.id.buttonOpenSettings);
            Button btnNeg = (Button) dialog.findViewById(R.id.buttonCancelSettings);

            btnPos.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(myIntent);
                }
            });

            btnNeg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            dialog.create();
            dialog.show();
        } else {
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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
                getWeatherViewModel.getWeatherByCoord(getString(R.string.open_weather_api_key), mLastLocation.getLatitude(), mLastLocation.getLongitude());
            } else {
                Criteria criteria = new Criteria();
                criteria.setAccuracy(Criteria.ACCURACY_COARSE);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (context.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && context.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    Activity#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for Activity#requestPermissions for more details.
                        return;
                    }
                }
                locationManager.requestSingleUpdate(criteria, this, null);
                mLastLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                if (mLastLocation != null) {
                    Log.wtf("-this", "Location ELSE : " + mLastLocation.getLatitude() + "," + mLastLocation.getLongitude());
                    getWeatherViewModel.getWeatherByCoord(getString(R.string.open_weather_api_key), mLastLocation.getLatitude(), mLastLocation.getLongitude());

                }
            }


        }
    }


}
