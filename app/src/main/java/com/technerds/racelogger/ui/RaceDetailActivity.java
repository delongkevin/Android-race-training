package com.technerds.racelogger.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.anychart.APIlib;
import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Cartesian;
import com.anychart.core.cartesian.series.Column;
import com.anychart.enums.HoverMode;
import com.anychart.enums.TooltipPositionMode;
import com.bumptech.glide.Glide;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.technerds.racelogger.Utils.AgeGroups;
import com.technerds.racelogger.Utils.AppUtils;
import com.technerds.racelogger.Utils.DateFormatter;
import com.technerds.racelogger.Utils.StringChecker;
import com.technerds.racelogger.dataModels.PicModel;
import com.technerds.racelogger.dataModels.graphs.getRaceParticipantsModel.UserRaceItem;
import com.technerds.racelogger.dataModels.shoeModels.getShoeProfiles.GetShoeProfiles;
import com.technerds.racelogger.dataModels.shoeModels.getShoeProfiles.ShoeProfileDataItem;
import com.technerds.racelogger.network.Status;
import com.technerds.racelogger.Utils.MyShardPreferences;
import com.technerds.racelogger.Utils.Urls;
import com.technerds.racelogger.viewModels.GetRaceDetailViewModel;
import com.technerds.racelogger.viewModels.GetShoeMileageRunViewModel;
import com.technerds.racelogger.viewModels.graphs.GetRaceParticipantsViewModel;
import com.technerds.racelogger.dataModels.graphs.getRaceParticipantsModel.RaceParticipantsModel;
import com.technerds.racelogger.dataModels.raceDetailModel.RaceDetailModel;
import com.technerds.racelogger.dataModels.shoeMilleage.ShoeMileageModel;
import com.technerds.racelogger.databinding.ActivityRaceDetailBinding;
import com.technerds.racelogger.viewModels.shoeProfiles.GetShoeProfilesViewModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Response;

public class RaceDetailActivity extends AppCompatActivity {

    ActivityRaceDetailBinding binding;
    View view;
    SharedPreferences.Editor editor;
    SharedPreferences mySharedPreference;

    GetRaceDetailViewModel getRaceDetailViewModel;
    RaceDetailModel raceDetailModel;

    GetRaceParticipantsViewModel getRaceParticipantsViewModel;
    RaceParticipantsModel raceParticipantsModel;

    int race_id = -1;


    /*Khalil Changes*/
    GetShoeProfilesViewModel getShoeProfilesViewModel;
    GetShoeProfiles getShoeProfiles;
    List<ShoeProfileDataItem> shoeProfileList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //  getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setFlags(WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES, WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES);
        //  getWindow().setStatusBarColor(ContextCompat.getColor(this,R.color.white_opac_thirty));


        binding = ActivityRaceDetailBinding.inflate(getLayoutInflater());
        view = binding.getRoot();
        setContentView(view);
        FullScreen();
        setupViewModels();
        init();
    }

    public void setupViewModels() {
        getRaceDetailViewModel = new ViewModelProvider(this).get(GetRaceDetailViewModel.class);
        raceDetailObserveViewModel(getRaceDetailViewModel);

        getRaceParticipantsViewModel = new ViewModelProvider(this).get(GetRaceParticipantsViewModel.class);
        getRaceParticipantsObserveViewModel(getRaceParticipantsViewModel);


        /*Khalil changes*/
        getShoeProfilesViewModel = new ViewModelProvider(this).get(GetShoeProfilesViewModel.class);
        getShoeProfilesObserveViewModel(getShoeProfilesViewModel);


    }

    private void init() {
        mySharedPreference = getSharedPreferences(MyShardPreferences.mPreferenceName, MODE_PRIVATE);
        editor = mySharedPreference.edit();

        String token = mySharedPreference.getString(MyShardPreferences.token, "");
        /* race_id = getIntent().getIntExtra("race_id", -1);*/
        race_id = AppUtils.race_id;
        //Toast.makeText(this, String.valueOf(race_id), Toast.LENGTH_SHORT).show();
        if (race_id > 0) {
            binding.progressLayout.setVisibility(View.VISIBLE);
            getRaceDetailViewModel.getRaceDetail(token, race_id);
        } else {
            finish();
        }

    }

    private void raceDetailObserveViewModel(final GetRaceDetailViewModel viewModel) {
        // Observe project data
        viewModel.getResponse().observe(this, apiResponse -> {

            if (apiResponse != null) {
                if (apiResponse.status == Status.SUCCESS) {
                    binding.progressLayout.setVisibility(View.GONE);
                    Log.wtf("-this", "Race Detail API Response : " + apiResponse.data);
                    Response<ResponseBody> responseBodyResponse = (Response<ResponseBody>) apiResponse.data;

                    try {
                        if (responseBodyResponse.body() != null) {
                            String json = responseBodyResponse.body().string();
                            Log.wtf("retrofit_", "Race Detail response body: " + json);
                            JSONObject jsonObject = new JSONObject(json);
                            int status = jsonObject.getInt("status");
                            Log.wtf("retrofit_", "status : " + status);

                            if (status == 1) {
                                Gson gson = new Gson();
                                raceDetailModel = gson.fromJson(json, RaceDetailModel.class);
                                if (raceDetailModel.getRaceData() != null) {
                                    setValues();
                                }

                            } else {
                                String error = jsonObject.getString("error");
                            }

                        }
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();

                    }

                } else if (apiResponse.status == Status.ERROR) {
                    Log.wtf("-this", "Race Detail API Response : " + apiResponse.error.getMessage());
                    binding.progressLayout.setVisibility(View.GONE);
                }
            }
        });
    }

    void setValues() {

        String token = mySharedPreference.getString(MyShardPreferences.token, "");

        if (raceDetailModel.getRaceData().getUserShoe() != null) {
            binding.progressLayout.setVisibility(View.VISIBLE);
            getShoeProfilesViewModel.getShoeProfiles(token);
        }


        String ageGroup = raceDetailModel.getRaceData().getAgeGroup();
        if (StringChecker.checkString(ageGroup)) {
            binding.textViewAgeGroup.setText("" + AgeGroups.getAgeGroupFromId(Integer.parseInt(ageGroup)));
        }
        if (StringChecker.checkString(raceDetailModel.getRaceData().getFinishPlace()))
            binding.textViewFinishPlace.setText("" + raceDetailModel.getRaceData().getFinishPlace());

        if (StringChecker.checkString(raceDetailModel.getRaceData().getFinishGroupPlace()))
            binding.textViewGroupFinishPlace.setText("" + raceDetailModel.getRaceData().getFinishGroupPlace());

        Log.wtf("-finish", raceDetailModel.getRaceData().getHour() + ":" +
                raceDetailModel.getRaceData().getMinute() + ":" +
                raceDetailModel.getRaceData().getSecond());

        if ((!raceDetailModel.getRaceData().getHour().equalsIgnoreCase("0"))
                || (!raceDetailModel.getRaceData().getMinute().equalsIgnoreCase("0"))
                || (!raceDetailModel.getRaceData().getSecond().equalsIgnoreCase("0"))) {

            binding.textViewFinishTime.setText(raceDetailModel.getRaceData().getHour() + ":" +
                    raceDetailModel.getRaceData().getMinute() + ":" +
                    raceDetailModel.getRaceData().getSecond());
        }

        if (raceDetailModel.getRaceData().getRace() != null) {

            if (StringChecker.checkString(raceDetailModel.getRaceData().getRace().getRaceName()))
                binding.textViewRaceName.setText("" + raceDetailModel.getRaceData().getRace().getRaceName());

            if (StringChecker.checkString(raceDetailModel.getRaceData().getRace().getParkName()))
                binding.textViewParkName.setText("" + raceDetailModel.getRaceData().getRace().getParkName());

            if (StringChecker.checkString(raceDetailModel.getRaceData().getDate()))
                binding.textViewDate.setText("" + DateFormatter.getFormattedDateFromDateTimeString(raceDetailModel.getRaceData().getDate()));

            if (StringChecker.checkString(raceDetailModel.getRaceData().getRace().getCity()))
                binding.textViewCity.setText("" + raceDetailModel.getRaceData().getRace().getCity());

            if (StringChecker.checkString(raceDetailModel.getRaceData().getRace().getState()))
                binding.textViewState.setText("" + raceDetailModel.getRaceData().getRace().getState());

            if (StringChecker.checkString(raceDetailModel.getRaceData().getRace().getCountry()))
                binding.textViewCountry.setText("" + raceDetailModel.getRaceData().getRace().getCountry());

            if (StringChecker.checkString(raceDetailModel.getRaceData().getRace().getUserDistance()))
                binding.textViewDistance.setText("" + raceDetailModel.getRaceData().getRace().getUserDistance() + "" + raceDetailModel.getRaceData().getRace().getUnit());

            if (StringChecker.checkString(raceDetailModel.getRaceData().getRace().getCourseType()))
                binding.textViewCourseType.setText("" + raceDetailModel.getRaceData().getRace().getCourseType());

            if (StringChecker.checkString(raceDetailModel.getRaceData().getRace().getTemperature())) {
                binding.textViewTemperature.setText("" + raceDetailModel.getRaceData().getRace().getTemperature());
            }

            if (StringChecker.checkString(raceDetailModel.getRaceData().getRace().getWeather()))
                binding.textViewWeather.setText("" + raceDetailModel.getRaceData().getRace().getWeather());

            String terrain = null;

            if (raceDetailModel.getRaceData().getRace().getRaceTerrain() != null) {
                if (raceDetailModel.getRaceData().getRace().getRaceTerrain().size() > 0) {
                    for (int i = 0; i < raceDetailModel.getRaceData().getRace().getRaceTerrain().size(); i++) {
                        if (i == 0) {
                            terrain = raceDetailModel.getRaceData().getRace().getRaceTerrain().get(i).getName();
                        } else {
                            terrain = terrain + ", " + raceDetailModel.getRaceData().getRace().getRaceTerrain().get(i).getName();
                        }
                    }
                }
            }

            if (terrain != null) {
                binding.textViewTerrain.setText("" + terrain);
            }


            if (StringChecker.checkString(raceDetailModel.getRaceData().getRace().getTotalRunners()))
                binding.textViewTotalRunners.setText("" + raceDetailModel.getRaceData().getRace().getTotalRunners());

            if (StringChecker.checkString(raceDetailModel.getRaceData().getAgeGroupRunner()))
                binding.textViewRunnersInMyAgeGroup.setText("" + raceDetailModel.getRaceData().getAgeGroupRunner());

            if (StringChecker.checkString(raceDetailModel.getRaceData().getRace().getMaleRunners()))
                binding.textViewMaleRunners.setText("" + raceDetailModel.getRaceData().getRace().getMaleRunners());

            if (StringChecker.checkString(raceDetailModel.getRaceData().getRace().getFemaleRunners()))
                binding.textViewFemaleRunners.setText("" + raceDetailModel.getRaceData().getRace().getFemaleRunners());


            if (StringChecker.checkString(raceDetailModel.getRaceData().getRace().getStreetAddress()))
                binding.textViewAddress.setText("" + raceDetailModel.getRaceData().getRace().getStreetAddress());
            
            /*if (StringChecker.checkString(raceDetailModel.getRaceData().getRace().getDistance() + ""))
                binding.textViewShoeMileage.setText(raceDetailModel.getRaceData().getRace().getDistance() + " " + raceDetailModel.getRaceData().getRace().getUnit());*/

          /*  if (StringChecker.checkString(raceDetailModel.getRaceData().getRace().getDistance() + ""))
                binding.textViewShoeMileage.setText(raceDetailModel.getRaceData().getRace().getDistance() + " " + raceDetailModel.getRaceData().getRace().getUnit());*/

            // binding.textViewShoeMileage.setText("" + raceDetailModel.getRaceData().getUserShoe().getShoeProfile().getShoe_mileage());


            String notes = raceDetailModel.getRaceData().getNotes();

            //   Toast.makeText(this, "Data:"+String.valueOf(notes), Toast.LENGTH_SHORT).show();


            if (notes != null) {
                binding.layoutNotesTop.setVisibility(View.VISIBLE);
                binding.textViewNotes.setText("" + notes);
            } else {
                binding.layoutNotesTop.setVisibility(View.GONE);
            }

            ArrayList<SlideModel> imageList = new ArrayList<>();
            if (raceDetailModel.getRaceData().getUserGallery() != null) {
                if (raceDetailModel.getRaceData().getUserGallery().size() > 0) {
                    if (raceDetailModel.getRaceData().getUserGallery().size() == 1) {
                        binding.layoutImage.setVisibility(View.VISIBLE);
                        binding.imageViewSlider.setVisibility(View.VISIBLE);
                        binding.imageSlider.setVisibility(View.GONE);
                        Glide.with(this).load(Urls.imageBaseUrl + "" + raceDetailModel.getRaceData().getUserGallery().get(0).getImage()).centerCrop().into(binding.imageViewSlider);

                        //Toast.makeText(this, String.valueOf( Urls.imageBaseUrl + "" + raceDetailModel.getRaceData().getUserGallery().get(0).getImage()), Toast.LENGTH_SHORT).show();
                    } else if (raceDetailModel.getRaceData().getUserGallery().size() > 1) {

                        for (int i = 0; i < raceDetailModel.getRaceData().getUserGallery().size(); i++) {
                            imageList.add(new SlideModel(Urls.imageBaseUrl + "" + raceDetailModel.getRaceData().getUserGallery().get(i).getImage(), ScaleTypes.CENTER_CROP));

                        }

                        binding.layoutImage.setVisibility(View.VISIBLE);
                        binding.imageSlider.setVisibility(View.VISIBLE);
                        binding.imageViewSlider.setVisibility(View.GONE);
                        binding.imageSlider.setImageList(imageList, ScaleTypes.CENTER_CROP);
                    }
                } else {
                    binding.layoutImage.setVisibility(View.GONE);
                }
            } else {
                binding.layoutImage.setVisibility(View.GONE);
            }

        }

        if (raceDetailModel.getRaceData().getUserShoe() != null) {

            if (StringChecker.checkString(raceDetailModel.getRaceData().getUserShoe().getShoeProfile().getShoeName()))

            binding.textViewShoeBrand.setText("" + raceDetailModel.getRaceData().getUserShoe().getShoeProfile().getShoeName());

            if (StringChecker.checkString(raceDetailModel.getRaceData().getUserShoe().getShoeProfile().getModel()))
                binding.textViewShoeModel.setText("" + raceDetailModel.getRaceData().getUserShoe().getShoeProfile().getModel());

            if (StringChecker.checkString(raceDetailModel.getRaceData().getUserShoe().getShoeProfile().getSize()))
                binding.textViewShoeSize.setText("" + raceDetailModel.getRaceData().getUserShoe().getShoeProfile().getSize());

            if (StringChecker.checkString(raceDetailModel.getRaceData().getUserShoe().getShoeProfile().getWidth()))
                binding.textViewShoeWidth.setText("" + raceDetailModel.getRaceData().getUserShoe().getShoeProfile().getWidth());

            if (StringChecker.checkString(raceDetailModel.getRaceData().getUserShoe().getShoeProfile().getDatePurchased()))
                binding.textViewPurchaseDate.setText("" + raceDetailModel.getRaceData().getUserShoe().getShoeProfile().getDatePurchased());

            /*if (StringChecker.checkString(raceDetailModel.getRaceData().getUserShoe().getShoeProfile().getShoe_mileage()))
                binding.textViewPurchaseDate.setText("" + raceDetailModel.getRaceData().getUserShoe().getShoeProfile().getDatePurchased());*/

            /*if (raceDetailModel.getRaceData().getUserShoe().getShoeProfile().getShoe_mileage() != null &&
                    raceDetailModel.getRaceData().getUserShoe().getShoeProfile().getShoe_mileage().size() > 0) {
                binding.textViewShoeMileage.setText(raceDetailModel.getRaceData().getUserShoe().getShoeProfile().getShoe_mileage().get(0).getDistance()
                        + " Mile");
            }*/

            // binding.textViewShoeMileage.setText("" + raceDetailModel.getRaceData().getUserShoe().getShoeProfile().getShoe_mileage());

            //   Toast.makeText(this, String.valueOf(raceDetailModel.getRaceData().getUserShoe().getShoeProfile().getShoe_mileage()), Toast.LENGTH_SHORT).show();

        }


      /*  String notes = raceDetailModel.getRaceData().getNotes();



        if (notes != null) {
            binding.layoutNotesTop.setVisibility(View.VISIBLE);
            binding.textViewNotes.setText("" + notes);
        } else {
            binding.layoutNotesTop.setVisibility(View.GONE);
        }

        ArrayList<SlideModel> imageList = new ArrayList<>();
        if (raceDetailModel.getRaceData().getUserGallery() != null) {
            if (raceDetailModel.getRaceData().getUserGallery().size() > 0) {
                if (raceDetailModel.getRaceData().getUserGallery().size() == 1) {
                    binding.layoutImage.setVisibility(View.VISIBLE);
                    binding.imageViewSlider.setVisibility(View.VISIBLE);
                    binding.imageSlider.setVisibility(View.GONE);
                    Glide.with(this).load(Urls.imageBaseUrl + "" + raceDetailModel.getRaceData().getUserGallery().get(0).getImage()).centerCrop().into(binding.imageViewSlider);
                } else if (raceDetailModel.getRaceData().getUserGallery().size() > 1) {
                    for (int i = 0; i < raceDetailModel.getRaceData().getUserGallery().size(); i++) {
                        imageList.add(new SlideModel(Urls.imageBaseUrl + "" + raceDetailModel.getRaceData().getUserGallery().get(i).getImage(), ScaleTypes.CENTER_CROP));
                    }

                    binding.layoutImage.setVisibility(View.VISIBLE);
                    binding.imageSlider.setVisibility(View.VISIBLE);
                    binding.imageViewSlider.setVisibility(View.GONE);
                    binding.imageSlider.setImageList(imageList, ScaleTypes.CENTER_CROP);
                }
            } else {
                binding.layoutImage.setVisibility(View.GONE);
            }
        } else {
            binding.layoutImage.setVisibility(View.GONE);
        }
*/

    }

    public void EditRaceClicked(View view) {
        Intent intent = new Intent(this, EditRaceActivity.class);
//        Gson gson = new Gson();
//        String myJson = gson.toJson(raceDetailModel);
//        intent.putExtra("myjson", myJson);
        intent.putExtra("race_id", raceDetailModel.getRaceData().getId());
        startActivityForResult(intent, 7);
    }


    private void getRaceParticipantsObserveViewModel(final GetRaceParticipantsViewModel viewModel) {
        // Observe project data
        viewModel.getResponse().observe(this, apiResponse -> {

            if (apiResponse != null) {
                if (apiResponse.status == Status.SUCCESS) {
                    binding.progressLayout.setVisibility(View.GONE);
                    Log.wtf("-this", "Race Participants API Response : " + apiResponse.data);
                    Response<ResponseBody> responseBodyResponse = (Response<ResponseBody>) apiResponse.data;

                    try {
                        if (responseBodyResponse.body() != null) {
                            String json = responseBodyResponse.body().string();
                            Log.wtf("retrofit_", "Race Participants response body: " + json);
                            JSONObject jsonObject = new JSONObject(json);
                            int status = jsonObject.getInt("status");
                            Log.wtf("retrofit_", "get race Participants status : " + status);

                            if (status == 1) {
                                Gson gson = new Gson();
                                raceParticipantsModel = gson.fromJson(json, RaceParticipantsModel.class);
                                binding.cardViewFinishPlaces.setVisibility(View.VISIBLE);
                                binding.layoutChapi.setVisibility(View.VISIBLE);
                                if (raceParticipantsModel.getData().getUserRace() != null && raceParticipantsModel.getData().getUserRace().size() > 0) {
                                    setColumnChartFinishPlaces(raceParticipantsModel.getData().getUserRace());
                                } else {
                                    binding.cardViewFinishPlaces.setVisibility(View.GONE);
                                    binding.layoutChapi.setVisibility(View.GONE);
                                }

                            } else {
                                String error = jsonObject.getString("error");
                                binding.cardViewFinishPlaces.setVisibility(View.GONE);
                                binding.layoutChapi.setVisibility(View.GONE);
                            }

                        }
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();

                    }

                } else if (apiResponse.status == Status.ERROR) {
                    Log.wtf("-this", "Race Participants API Response : " + apiResponse.error.getMessage());
                    binding.progressLayout.setVisibility(View.GONE);
                    binding.cardViewFinishPlaces.setVisibility(View.GONE);
                }
            }
        });
    }

    void setColumnChartFinishPlaces(List<UserRaceItem> list) {

        Log.wtf("-this", "Graph Finish Places Column Chart");


        APIlib.getInstance().setActiveAnyChartView(binding.chartFirstPlaces);

        Cartesian cartesian = AnyChart.column();

        List<DataEntry> data = new ArrayList<>();

        for (int i = 0; i < list.size(); i++) {
            Log.wtf("-this", "Name :  " + list.get(i).getUser().getFirstName());
            int finishPlace = Integer.parseInt(list.get(i).getFinishPlace());
            Log.wtf("-this", "Finish Place :  " + finishPlace);
            data.add(new ValueDataEntry(list.get(i).getUser().getFirstName(), finishPlace));
        }

        Log.wtf("-this", "Data Size :  " + data.size());


        Column column = cartesian.column(data);

//        column.tooltip()
//                .position(Position.CENTER_BOTTOM)
//                .anchor(Anchor.CENTER_BOTTOM)
//                .offsetX(0d)
//                .offsetY(0d)
//                .format("{%Value}{groupsSeparator: }");

        cartesian.animation(true);
        cartesian.title("Finish Places of participants ");

        //  cartesian.yAxis(0).labels().format("{%Value}{groupsSeparator: }");

        cartesian.tooltip().positionMode(TooltipPositionMode.POINT);
        cartesian.interactivity().hoverMode(HoverMode.BY_X);

        cartesian.yAxis(0).title("Finish Places");
        cartesian.xAxis(0).title("Name");
        cartesian.background().enabled(true);
        //  cartesian.background().fill("transparent");
        cartesian.background().fill("#ffffff 0.5");
        //  cartesian.yAxis(0).labels().format("{%value}{type:number, decimalsCount:0}");


        binding.chartFirstPlaces.setChart(cartesian);
        binding.chartFirstPlaces.setOnRenderedListener(new AnyChartView.OnRenderedListener() {
            @Override
            public void onRendered() {
                //   binding.chartFinishPlaces.setBackgroundColor("green"); // or "#000000"
            }
        });
        //  binding.chartFinishPlaces.setBackgroundColor("transparent"); // or "#000000"

    }

//    void setColumnChartFinishPlaces(List<UserRaceItem> list) {
//
//
//        Log.wtf("-this", "Graph Finish Places Column Chart");
//
//        // binding.chartFinishPlaces.setOnChartValueSelectedListener(this);
//
//        binding.chartFinishPlaces.setDrawBarShadow(false);
//        binding.chartFinishPlaces.setDrawValueAboveBar(true);
//
//        binding.chartFinishPlaces.getDescription().setEnabled(false);
//
//        // if more than 60 entries are displayed in the chart, no values will be
//        // drawn
//        //  binding.chartFinishPlaces.setMaxVisibleValueCount(60);
//
//        // scaling can now only be done on x- and y-axis separately
//        binding.chartFinishPlaces.setPinchZoom(false);
//
//        binding.chartFinishPlaces.setDrawGridBackground(false);
//
//
//        ArrayList<BarEntry> values = new ArrayList<>();
//
//        ArrayList<String> xAxisLabels = new ArrayList();
//
//
//        for (int i = 0; i < list.size(); i++) {
//            String name = list.get(i).getUser().getFirstName();
//            Log.wtf("-this", "Name :  " + name);
//            xAxisLabels.add(name);
//            int finishPlace = Integer.parseInt(list.get(i).getFinishPlace());
//            Log.wtf("-this", "Finish Place :  " + finishPlace);
//            values.add(new BarEntry(i, finishPlace));
//
//
//        }
//
//        XAxis xAxis = binding.chartFinishPlaces.getXAxis();
//        xAxis.setGranularity(1f);
//       // xAxis.setCenterAxisLabels(true);
//        xAxis.setLabelRotationAngle(-45);
//
//        xAxis.setValueFormatter(new IAxisValueFormatter() {
//            @Override
//            public String getFormattedValue(float value, AxisBase axis) {
//                Log.wtf("-this"," 483 xAxis value : "+value);
//                if (value >= 0) {
//                    if (value <= xAxisLabels.size() - 1) {
//                        int i = (int) value;
//                        Log.wtf("-this"," 486 xAxis i : "+i);
//                        return xAxisLabels.get(i);
//                    }
//                    return "";
//                }
//                return "";
//            }
//        });
//        binding.chartFinishPlaces.getAxisLeft().setTextColor(Color.WHITE); // left y-axis
//        binding.chartFinishPlaces.getAxisRight().setTextColor(Color.WHITE); // left y-axis
//        binding.chartFinishPlaces.getXAxis().setTextColor(Color.WHITE);
//        binding.chartFinishPlaces.getLegend().setTextColor(Color.WHITE);
//        BarDataSet set1;
//        if (binding.chartFinishPlaces.getData() != null &&
//                binding.chartFinishPlaces.getData().getDataSetCount() > 0) {
//            set1 = (BarDataSet) binding.chartFinishPlaces.getData().getDataSetByIndex(0);
//            set1.setValues(values);
//            binding.chartFinishPlaces.getData().notifyDataChanged();
//            binding.chartFinishPlaces.notifyDataSetChanged();
//
//        } else {
//            set1 = new BarDataSet(values,"Participants");
//
//            set1.setDrawIcons(false);
//            int startColor1 = ContextCompat.getColor(this, R.color.colorPrimary);
//            set1.setColor(startColor1);
//
//            ArrayList<IBarDataSet> dataSets = new ArrayList<>();
//         //   set1.setColor(R.color.colorPrimary);
//            dataSets.add(set1);
//
//
//            BarData data = new BarData(dataSets);
//            binding.chartFinishPlaces.setData(data);
//            //  binding.chartFinishPlaces.setDescription("My Chart");
//          //  binding.chartFinishPlaces.animateXY(2000, 2000);
//            binding.chartFinishPlaces.invalidate();
//        }
//
//
//
//
//        binding.chartFinishPlaces.setDrawBarShadow(false);
//        binding.chartFinishPlaces.setDrawValueAboveBar(true);
//
//        binding.chartFinishPlaces.getDescription().setEnabled(false);
//
//        binding.chartFinishPlaces.setPinchZoom(false);
//
//        binding.chartFinishPlaces.setDrawGridBackground(false);
//
//        Log.wtf("-this", "Data Size :  " + values.size());
//
//
//    }


//    private void setData(int count, float range) {
//
//        float start = 1f;
//
//        ArrayList<BarEntry> values = new ArrayList<>();
//
//        for (int i = (int) start; i < start + count; i++) {
//            float val = (float) (Math.random() * (range + 1));
//
////            if (Math.random() * 100 < 25) {
////                values.add(new BarEntry(i, val, getResources().getDrawable(R.drawable.star)));
////            } else {
////                values.add(new BarEntry(i, val));
////            }
//
//            values.add(new BarEntry(i, val));
//        }
//
//        BarDataSet set1;
//
//        if (binding.chartFinishPlaces.getData() != null &&
//                binding.chartFinishPlaces.getData().getDataSetCount() > 0) {
//            set1 = (BarDataSet) binding.chartFinishPlaces.getData().getDataSetByIndex(0);
//            set1.setValues(values);
//            binding.chartFinishPlaces.getData().notifyDataChanged();
//            binding.chartFinishPlaces.notifyDataSetChanged();
//
//        } else {
//            set1 = new BarDataSet(values, "The year 2017");
//
//            set1.setDrawIcons(false);
////
////            int startColor1 = ContextCompat.getColor(this, android.R.color.holo_orange_light);
////            int startColor2 = ContextCompat.getColor(this, android.R.color.holo_blue_light);
////            int startColor3 = ContextCompat.getColor(this, android.R.color.holo_orange_light);
////            int startColor4 = ContextCompat.getColor(this, android.R.color.holo_green_light);
////            int startColor5 = ContextCompat.getColor(this, android.R.color.holo_red_light);
////            int endColor1 = ContextCompat.getColor(this, android.R.color.holo_blue_dark);
////            int endColor2 = ContextCompat.getColor(this, android.R.color.holo_purple);
////            int endColor3 = ContextCompat.getColor(this, android.R.color.holo_green_dark);
////            int endColor4 = ContextCompat.getColor(this, android.R.color.holo_red_dark);
////            int endColor5 = ContextCompat.getColor(this, android.R.color.holo_orange_dark);
////
////            List<Fill> gradientFills = new ArrayList<>();
////            gradientFills.add(new Fill(startColor1, endColor1));
////            gradientFills.add(new Fill(startColor2, endColor2));
////            gradientFills.add(new Fill(startColor3, endColor3));
////            gradientFills.add(new Fill(startColor4, endColor4));
////            gradientFills.add(new Fill(startColor5, endColor5));
////
////            set1.setFills(gradientFills);
//
//            ArrayList<IBarDataSet> dataSets = new ArrayList<>();
//            dataSets.add(set1);
//
//            BarData data = new BarData(dataSets);
//            data.setValueTextSize(10f);
//            //  data.setValueTypeface(tfLight);
//            data.setBarWidth(0.9f);
//
//            binding.chartFinishPlaces.setData(data);
//        }
//    }

    public void dummyClick(View view) {
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        //finish();

        AppUtils.ShowRaceFragment = true;
        Intent Send = new Intent(RaceDetailActivity.this, HomeActivity.class);
        startActivity(Send);
        finish();
    }

    public void backClicked(View view) {
        // onBackPressed();
        AppUtils.ShowRaceFragment = true;
        Intent Send = new Intent(RaceDetailActivity.this, HomeActivity.class);
        startActivity(Send);
        finish();
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 7) {
            if (data != null) {
                boolean done = data.getBooleanExtra("Done", false);
                Log.wtf("-this", " OnActivity result Done : " + done);

                if (done) {
                    String token = mySharedPreference.getString(MyShardPreferences.token, "");

                    if (race_id > 0) {
                        binding.progressLayout.setVisibility(View.VISIBLE);
                        getRaceDetailViewModel.getRaceDetail(token, race_id);
                    } else {
                        finish();
                    }
                }
            }
        }


        /*khalil changes*/

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
                                Log.wtf("-this", "96 Get Shoe Profiles");
                                getShoeProfiles = gson.fromJson(json, GetShoeProfiles.class);
                                if (getShoeProfiles != null) {
                                    Log.wtf("-this", "99 IF Get Shoe Profiles");
                                    shoeProfileList = getShoeProfiles.getData();
                                    setShoeProfile();
                                }

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
        if (shoeProfileList != null && shoeProfileList.size() > 0) {
            String prof_id = raceDetailModel.getRaceData().getUserShoe().getShoesProfileId();
            if (StringChecker.checkString(prof_id)) {
                int id = Integer.parseInt(prof_id);
                for (int i = 0; i < shoeProfileList.size(); i++) {
                    if (id == shoeProfileList.get(i).getId()) {
                        binding.textViewShoeBrand.setText(shoeProfileList.get(i).getShoeProfile() + "");
                        binding.textViewShoeMileage.setText(shoeProfileList.get(i).getShoeMileage() + "");
                        AppUtils.StrRaceShoesName = shoeProfileList.get(i).getShoeProfile();
                        //Toast.makeText(this, AppUtils.StrRaceShoesName, Toast.LENGTH_SHORT).show();
                        //  Toast.makeText(this, String.valueOf(shoeProfileList.get(i).getShoeMileage()), Toast.LENGTH_SHORT).show();
                    }
                }


            }
        }
    }

}