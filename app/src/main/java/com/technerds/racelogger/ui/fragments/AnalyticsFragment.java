package com.technerds.racelogger.ui.fragments;

import android.content.SharedPreferences;
import android.icu.text.DecimalFormat;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.anychart.APIlib;
import com.anychart.AnyChart;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.chart.common.listener.Event;
import com.anychart.chart.common.listener.ListenersInterface;
import com.anychart.charts.Cartesian;
import com.anychart.charts.Pie;
import com.anychart.core.cartesian.series.Column;
import com.anychart.enums.Align;
import com.anychart.enums.Anchor;
import com.anychart.enums.HoverMode;
import com.anychart.enums.LegendLayout;
import com.anychart.enums.Position;
import com.anychart.enums.TooltipPositionMode;
import com.applandeo.materialcalendarview.EventDay;
import com.google.gson.Gson;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.technerds.racelogger.dataModels.raceDetailModel.RaceData;
import com.technerds.racelogger.network.Status;
import com.technerds.racelogger.R;
import com.technerds.racelogger.Utils.Months;
import com.technerds.racelogger.Utils.MyShardPreferences;
import com.technerds.racelogger.viewModels.GetRaceListingsViewModel;
import com.technerds.racelogger.viewModels.SearchRaceNameViewModel;
import com.technerds.racelogger.viewModels.graphs.GetGraphByMonthViewModel;
import com.technerds.racelogger.viewModels.graphs.GetGraphByWeekViewModel;
import com.technerds.racelogger.viewModels.graphs.GetGraphByYearViewModel;
import com.technerds.racelogger.viewModels.graphs.GetGraphFirstPlacesViewModel;
import com.technerds.racelogger.dataModels.raceListingModel.RaceListingModel;
import com.technerds.racelogger.dataModels.graphs.graphByFirstPlaces.FirstPlacesDataItem;
import com.technerds.racelogger.dataModels.graphs.graphByFirstPlaces.GraphByFirstPlacesModel;
import com.technerds.racelogger.dataModels.graphs.graphByMonth.GraphByMonthDataItem;
import com.technerds.racelogger.dataModels.graphs.graphByMonth.GraphByMonthModel;
import com.technerds.racelogger.dataModels.graphs.graphByWeek.GraphByWeekDataItem;
import com.technerds.racelogger.dataModels.graphs.graphByWeek.GraphByWeekModel;
import com.technerds.racelogger.dataModels.graphs.graphByYear.GraphByYearDataItem;
import com.technerds.racelogger.dataModels.graphs.graphByYear.GraphByYearModel;
import com.technerds.racelogger.dataModels.searchRaceModel.DataItem;
import com.technerds.racelogger.dataModels.searchRaceModel.SearchRaceModel;
import com.technerds.racelogger.databinding.FragmentAnalyticsBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;


public class AnalyticsFragment extends Fragment implements View.OnClickListener {
    
    FragmentAnalyticsBinding binding;
    View view;
    SharedPreferences.Editor editor;
    SharedPreferences mySharedPreference;
    GetGraphByYearViewModel getGraphByYearViewModel;
    GetGraphByMonthViewModel getGraphByMonthViewModel;
    GetGraphByWeekViewModel getGraphByWeekViewModel;
    GetGraphFirstPlacesViewModel getGraphFirstPlacesViewModel;
    
    GetRaceListingsViewModel getRaceListingsViewModel;
    RaceListingModel raceListingModel;
    
    SearchRaceNameViewModel searchRaceNameViewModel;
    SearchRaceModel searchRaceModel;
    
    List<RaceData> raceList = new ArrayList<>();
    int selectedPage = 1;
    int calendarYear = 2020;
    int calendarMonth = 9;
    int year = 0;
    int month = 0;
    
    
    public AnalyticsFragment() {
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentAnalyticsBinding.inflate(getLayoutInflater());
        setupViewModels();
        init();
        view = binding.getRoot();
        return view;
    }
    
    public void setupViewModels() {
        getRaceListingsViewModel = new ViewModelProvider(this).get(GetRaceListingsViewModel.class);
        getRaceListingObserveViewModel(getRaceListingsViewModel);
        
        getGraphByYearViewModel = new ViewModelProvider(this).get(GetGraphByYearViewModel.class);
        getGraphByYearObserveViewModel(getGraphByYearViewModel);
        
        getGraphByMonthViewModel = new ViewModelProvider(this).get(GetGraphByMonthViewModel.class);
        getGraphByMonthObserveViewModel(getGraphByMonthViewModel);
        
        getGraphByWeekViewModel = new ViewModelProvider(this).get(GetGraphByWeekViewModel.class);
        getGraphByWeekObserveViewModel(getGraphByWeekViewModel);
        
        getGraphFirstPlacesViewModel = new ViewModelProvider(this).get(GetGraphFirstPlacesViewModel.class);
        getGraphByFirstPlacesObserveViewModel(getGraphFirstPlacesViewModel);
    
        searchRaceNameViewModel = new ViewModelProvider(this).get(SearchRaceNameViewModel.class);
        searchRaceNameObserveViewModel(searchRaceNameViewModel);
    }
    
    private void init() {
        mySharedPreference = getContext().getSharedPreferences(MyShardPreferences.mPreferenceName, MODE_PRIVATE);
        editor = mySharedPreference.edit();
        
        String token = mySharedPreference.getString(MyShardPreferences.token, "");
        
        getRaceListingsViewModel.getRaceListing(token,selectedPage, calendarYear, calendarMonth);
        
        setOnClickListeners();
        setupSpinners();
        
        binding.progressLayout.setVisibility(View.VISIBLE);
        getGraphByYearViewModel.getGraphByYear(token);
        getGraphFirstPlacesViewModel.getGraphFirstPlaces(token);
        searchRaceNameViewModel.searchRaceName(token);
        
//        setPieChart();
        
    }
    
    void setupSpinners() {
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        String[] yearArray = new String[100];
    
        yearArray[0] = currentYear +"";
        for (int i = 1; i < yearArray.length; i++){
            int val = currentYear - i;
            yearArray[i] = val +"";
        }
        
        binding.spinnerYear.setItems(yearArray);
        binding.spinnerYear.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {
            
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                year = Integer.parseInt(item);
                setYearBarChart();
                
            }
        });
        
        binding.spinnerMonth.setItems(Months.getMonths());
        binding.spinnerMonth.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {
            
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                int pos = position + 1;
                month = pos;
                setYearBarChart();
            }
        });
        
    }
    
    private void getRaceListingObserveViewModel(final GetRaceListingsViewModel viewModel) {
        // Observe project data
        viewModel.getResponse().observe(getViewLifecycleOwner(), apiResponse -> {
            
            if (apiResponse != null) {
                if (apiResponse.status == Status.SUCCESS) {
                    binding.progressLayout.setVisibility(View.GONE);
                    Log.wtf("-this", "Race Listing Response : " + apiResponse.data);
                    Response<ResponseBody> responseBodyResponse = (Response<ResponseBody>) apiResponse.data;
                    
                    try {
                        if (responseBodyResponse.body() != null) {
                            String json = responseBodyResponse.body().string();
                            Log.wtf("retrofit_", "Race Listing response body: " + json);
                            JSONObject jsonObject = new JSONObject(json);
                            int status = jsonObject.getInt("status");
                            if (status == 1) {
                                Gson gson = new Gson();
                                raceListingModel = gson.fromJson(json, RaceListingModel.class);
                                if (raceListingModel != null) {
                                    if (raceListingModel.getData().getCurrentPage() == 1) {
                                        raceList = raceListingModel.getData().getData();
                                        callNextPage();
                                    } else {
                                        raceList.addAll(raceListingModel.getData().getData());
                                        callNextPage();
                                    }
                                }
                                
                            } else {
                                
                                String error = jsonObject.getString("error");
                              //  Snackbar.make(view, "" + error, Snackbar.LENGTH_SHORT).show();
                                Log.wtf("retrofit_", "Race Listing response body: " + error);
                                
                            }
                        }
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                        
                    }
                    
                } else if (apiResponse.status == Status.ERROR) {
                    Log.wtf("-this", "Race Listing Response : " + apiResponse.error.getMessage());
                    binding.progressLayout.setVisibility(View.GONE);
                }
            } else {
                binding.progressLayout.setVisibility(View.GONE);
            }
        });
    }
    
    private void callNextPage() {
        Log.wtf("-this"," CallNext Called");
        if (selectedPage < raceListingModel.getData().getTotal()) {
            selectedPage++;
            binding.progressLayout.setVisibility(View.VISIBLE);
            String api_token = mySharedPreference.getString(MyShardPreferences.token, null);
            getRaceListingsViewModel.getRaceListing(api_token, selectedPage);
        } else {
            
            setCalender();
        }
    }
    
    private void setCalender() {
        Log.wtf("-this"," Calendar Called");
        List<EventDay> events = new ArrayList<>();
        if(raceList != null && raceList.size() > 0){
            Log.wtf("-this"," List Size : "+raceList.size());
            for(int i = 0; i < raceList.size(); i++){
                Calendar calendar = getCalenderDate(raceList.get(i).getDate());
                Log.wtf("-this"," Calendar object : "+calendar.getTime());
                if(calendar != null) {
                    events.add(new EventDay(calendar, R.mipmap.race_green));
                }
            }
        }
        
        
        //or
        //  events.add(new EventDay(calendar, new Drawable()));
        //or if you want to specify event label color
        // events.add(new EventDay(calendar, R.drawable.sample_icon, Color.parseColor("#228B22")));
        
        
        binding.calendarView.setEvents(events);
      //  binding.calendarView.setBackgroundColor(getResources().getColor(R.color.stock_red));
      //  binding.calendarView.setBackgroundResource(R.color.stock_red);
      //  binding.calendarView.setBackground(getResources().getDrawable(R.drawable.account_bg));
      
      
    }
    
    public Calendar getCalenderDate(String raceDate) {
        Calendar calendar = null;
        Log.wtf("-calendar","Given Date : "+raceDate);
        try {
            DateFormat f = new SimpleDateFormat("yyy-MM-dd hh:mm:ss");
            //   Log.wtf("-this", "race Date Not Formatted : " + raceDate);
            Date date = f.parse(raceDate);
    
            Log.wtf("-calendar","converted Date : "+date);
            
            calendar = Calendar.getInstance(); //calendar object with current date
            
            String[] dateTimeString = raceDate.split(" ");
            String[] dateString = dateTimeString[0].split("-");
            String[] timeString = dateTimeString[1].split(":");
            
            int year = Integer.parseInt(dateString[0]);
            int month = Integer.parseInt(dateString[1]);
            month = month -1;
            int day = Integer.parseInt(dateString[2]);
            day = day -1;
            int hour = Integer.parseInt(timeString[0]);
            int min = Integer.parseInt(timeString[1]);
            int sec = Integer.parseInt(timeString[2]);
//
//            Log.wtf("-calendar"," Date Year : "+year);
//            Log.wtf("-calendar"," Date Month : "+month);
//            Log.wtf("-calendar"," Date Day : "+day);
//            Log.wtf("-calendar"," Date Hours : "+hour);
//            Log.wtf("-calendar"," Date Minute : "+min);
//            Log.wtf("-calendar"," Date Seconds : "+sec);
    
            calendar.set(Calendar.SECOND, sec);
            calendar.set(Calendar.MINUTE, min);
            calendar.set(Calendar.HOUR, hour);
            calendar.set(Calendar.MONTH,month);
            calendar.set(Calendar.DAY_OF_MONTH, day);
            calendar.set(Calendar.YEAR,year);
            date = calendar.getTime();
            Log.wtf("-calendar","Date : "+date);
        } catch (Exception e) {
        
        }
        
        return calendar;
    }
    
    void setYearBarChart() {
        String token = mySharedPreference.getString(MyShardPreferences.token, "");
        if (year != 0) {
            if (month != 0) {
                binding.progressLayout.setVisibility(View.VISIBLE);
                getGraphByWeekViewModel.getGraphByWeek(token, year, month);
            } else {
                binding.progressLayout.setVisibility(View.VISIBLE);
                getGraphByMonthViewModel.getGraphByMonth(token, year);
            }
        } else {
            if (month != 0) {
                binding.progressLayout.setVisibility(View.VISIBLE);
                getGraphByWeekViewModel.getGraphByWeek(token, 2020, month);
            } else {
                binding.progressLayout.setVisibility(View.VISIBLE);
                getGraphByYearViewModel.getGraphByYear(token);
            }
            
        }
    }
    
    private void getGraphByYearObserveViewModel(final GetGraphByYearViewModel viewModel) {
        // Observe project data
        viewModel.getResponse().observe(getViewLifecycleOwner(), apiResponse -> {
            
            if (apiResponse != null) {
                if (apiResponse.status == Status.SUCCESS) {
                    binding.progressLayout.setVisibility(View.GONE);
                    Log.wtf("-this", "Graph By Year Response : " + apiResponse.data);
                    Response<ResponseBody> responseBodyResponse = (Response<ResponseBody>) apiResponse.data;
                    
                    try {
                        if (responseBodyResponse.body() != null) {
                            String json = responseBodyResponse.body().string();
                            Log.wtf("retrofit_", "Graph By Year response body: " + json);
                            GraphByYearModel model = null;
                            
                            Gson gson = new Gson();
                            model = gson.fromJson(json, GraphByYearModel.class);
                            if (model != null && model.getData() != null && model.getData().size() > 0) {
                                binding.layoutYearMonthGraph.setVisibility(View.VISIBLE);
                                binding.layoutChapi1.setVisibility(View.VISIBLE);
                                binding.textViewNoRecordFound.setVisibility(View.GONE);
                                setColumnChartYear(model.getData());
                                
                            } else {
                                binding.layoutYearMonthGraph.setVisibility(View.GONE);
                                binding.layoutChapi1.setVisibility(View.GONE);
                                binding.textViewNoRecordFound.setVisibility(View.VISIBLE);
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        
                    }
                    
                } else if (apiResponse.status == Status.ERROR) {
                    Log.wtf("-this", "Graph By Year Response : " + apiResponse.error.getMessage());
                    binding.progressLayout.setVisibility(View.GONE);
                }
            } else {
                binding.progressLayout.setVisibility(View.GONE);
            }
        });
    }
    
    private void getGraphByMonthObserveViewModel(final GetGraphByMonthViewModel viewModel) {
        // Observe project data
        viewModel.getResponse().observe(getViewLifecycleOwner(), apiResponse -> {
            
            if (apiResponse != null) {
                if (apiResponse.status == Status.SUCCESS) {
                    binding.progressLayout.setVisibility(View.GONE);
                    Log.wtf("-this", "Graph By Month Response : " + apiResponse.data);
                    Response<ResponseBody> responseBodyResponse = (Response<ResponseBody>) apiResponse.data;
                    
                    try {
                        if (responseBodyResponse.body() != null) {
                            String json = responseBodyResponse.body().string();
                            Log.wtf("retrofit_", "Graph By Month response body: " + json);
                            
                            GraphByMonthModel model = null;
                            
                            Gson gson = new Gson();
                            model = gson.fromJson(json, GraphByMonthModel.class);
                            if (model != null && model.getData() != null && model.getData().size() > 0) {
                                binding.layoutYearMonthGraph.setVisibility(View.VISIBLE);
                                binding.layoutChapi1.setVisibility(View.VISIBLE);
                                binding.textViewNoRecordFound.setVisibility(View.GONE);
                                setColumnChartMonth(model.getData());
                            } else {
                                binding.layoutYearMonthGraph.setVisibility(View.GONE);
                                binding.layoutChapi1.setVisibility(View.GONE);
                                binding.textViewNoRecordFound.setVisibility(View.VISIBLE);
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        
                    }
                    
                } else if (apiResponse.status == Status.ERROR) {
                    Log.wtf("-this", "Graph By Month Response : " + apiResponse.error.getMessage());
                    binding.progressLayout.setVisibility(View.GONE);
                }
            } else {
                binding.progressLayout.setVisibility(View.GONE);
            }
        });
    }
    
    private void getGraphByWeekObserveViewModel(final GetGraphByWeekViewModel viewModel) {
        // Observe project data
        viewModel.getResponse().observe(getViewLifecycleOwner(), apiResponse -> {
            
            if (apiResponse != null) {
                if (apiResponse.status == Status.SUCCESS) {
                    binding.progressLayout.setVisibility(View.GONE);
                    Log.wtf("-this", "Graph By Week Response : " + apiResponse.data);
                    Response<ResponseBody> responseBodyResponse = (Response<ResponseBody>) apiResponse.data;
                    
                    try {
                        if (responseBodyResponse.body() != null) {
                            String json = responseBodyResponse.body().string();
                            Log.wtf("retrofit_", "Graph By Week response body: " + json);
                            
                            GraphByWeekModel model = null;
                            
                            Gson gson = new Gson();
                            model = gson.fromJson(json, GraphByWeekModel.class);
                            if (model != null && model.getData() != null && model.getData().size() > 0) {
                                binding.layoutYearMonthGraph.setVisibility(View.VISIBLE);
                                binding.layoutChapi1.setVisibility(View.VISIBLE);
                                binding.textViewNoRecordFound.setVisibility(View.GONE);
                                setColumnChartWeek(model.getData());
                            }  else {
                                binding.layoutYearMonthGraph.setVisibility(View.GONE);
                                binding.layoutChapi1.setVisibility(View.GONE);
                                binding.textViewNoRecordFound.setVisibility(View.VISIBLE);
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        
                    }
                    
                } else if (apiResponse.status == Status.ERROR) {
                    Log.wtf("-this", "Graph By Week Response : " + apiResponse.error.getMessage());
                    binding.progressLayout.setVisibility(View.GONE);
                }
            } else {
                binding.progressLayout.setVisibility(View.GONE);
            }
        });
    }
    
    private void getGraphByFirstPlacesObserveViewModel(final GetGraphFirstPlacesViewModel viewModel) {
        // Observe project data
        viewModel.getResponse().observe(getViewLifecycleOwner(), apiResponse -> {
            
            if (apiResponse != null) {
                if (apiResponse.status == Status.SUCCESS) {
                    binding.progressLayout.setVisibility(View.GONE);
                    Log.wtf("-this", "Graph By First Place Response : " + apiResponse.data);
                    Response<ResponseBody> responseBodyResponse = (Response<ResponseBody>) apiResponse.data;
                    
                    try {
                        if (responseBodyResponse.body() != null) {
                            String json = responseBodyResponse.body().string();
                            Log.wtf("retrofit_", "Graph By First Place response body: " + json);
                            
                            GraphByFirstPlacesModel model = null;
                            
                            Gson gson = new Gson();
                            model = gson.fromJson(json, GraphByFirstPlacesModel.class);
                            if (model.getData() != null) {
                                if(model.getData().getRace().size() > 0) {
                                    binding.layoutFistPlaces.setVisibility(View.VISIBLE);
                                    binding.layoutChapi2.setVisibility(View.VISIBLE);
                                    setColumnChartFirstPlaces(model.getData().getRace());
                                } else {
                                    binding.layoutFistPlaces.setVisibility(View.GONE);
                                    binding.layoutChapi2.setVisibility(View.GONE);
                                }
                            } else {
                                binding.layoutFistPlaces.setVisibility(View.GONE);
                                binding.layoutChapi2.setVisibility(View.GONE);
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        
                    }
                    
                } else if (apiResponse.status == Status.ERROR) {
                    Log.wtf("-this", "Graph By First Place Response : " + apiResponse.error.getMessage());
                    binding.progressLayout.setVisibility(View.GONE);
                }
            } else {
                binding.progressLayout.setVisibility(View.GONE);
            }
        });
    }
    
    private void searchRaceNameObserveViewModel(final SearchRaceNameViewModel viewModel) {
        // Observe project data
        viewModel.getResponse().observe(getViewLifecycleOwner(), apiResponse -> {
            
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
                                if(searchRaceModel.getData() != null && searchRaceModel.getData().getData() != null &&searchRaceModel.getData().getData().size() > 0) {
                                    binding.layoutPieChart.setVisibility(View.VISIBLE);
                                    binding.layoutChapi3.setVisibility(View.VISIBLE);
                                    setPieChart(searchRaceModel.getData().getData());
                                } else {
                                    binding.layoutPieChart.setVisibility(View.GONE);
                                    binding.layoutChapi3.setVisibility(View.GONE);
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
    
    void setPieChart(List<DataItem> raceNameList) {
        APIlib.getInstance().setActiveAnyChartView(binding.chart1);
        Log.wtf("-this", "Pie Chart Called");
        Pie pie = AnyChart.pie();
        
        pie.setOnClickListener(new ListenersInterface.OnClickListener(new String[]{"x", "value"}) {
            @Override
            public void onClick(Event event) {
                Toast.makeText(getContext(), event.getData().get("x") + ":" + event.getData().get("value"), Toast.LENGTH_SHORT).show();
            }
        });
        
        List<DataEntry> data = new ArrayList<>();
        for (int i = 0; i < raceNameList.size(); i++){
            double distance = Double.parseDouble(raceNameList.get(i).getDistance());
            String unit = raceNameList.get(i).getUnit();
            
            data.add(new ValueDataEntry(raceNameList.get(i).getRaceName(),getMileDistance(unit,distance)));
        }
        
        pie.data(data);
        
        pie.labels().position("inside");
        
        pie.legend().title().enabled(true);
        pie.legend().title()
                .text("Races")
                .padding(0d, 0d, 10d, 0d);
        
        pie.legend()
                .position("center-bottom")
                .itemsLayout(LegendLayout.HORIZONTAL)
                .align(Align.CENTER);
        
        binding.chart1.setChart(pie);
    }
    
    double getMileDistance(String unit, double distance){
//        DecimalFormat twoDForm = new DecimalFormat("#.##");
        if (unit.equalsIgnoreCase("Mile")) {
            Log.wtf("-this", " unit Mile IF: ");
    
            
            return Double.parseDouble(String.format("%.2f", distance));
    
        } else if (unit.equalsIgnoreCase("KM")) {
            Log.wtf("-this", " unit KM IF: ");
            distance = distance * 0.621371;
            return Double.parseDouble(String.format("%.2f", distance));
        } else if (unit.equalsIgnoreCase("KM")) {
            Log.wtf("-this", " unit Marathon IF: ");
            distance = distance * 26.2188;
            return Double.parseDouble(String.format("%.2f", distance));
        }
    
        return Double.parseDouble(String.format("%.2f", distance));
    }
    
    void setColumnChartYear(List<GraphByYearDataItem> list) {
        
        Log.wtf("-this", "Graph By Year Column Chart");
        
        binding.chartYear.setVisibility(View.VISIBLE);
        binding.chartMonth.setVisibility(View.GONE);
        binding.chartWeek.setVisibility(View.GONE);
        APIlib.getInstance().setActiveAnyChartView(binding.chartYear);
        
        Cartesian cartesian = AnyChart.column();
        
        List<DataEntry> data = new ArrayList<>();
        
        for (int i = 0; i < list.size(); i++) {
            Log.wtf("-this", "Year :  " + list.get(i).getYear());
            double distance = Double.parseDouble(list.get(i).getDistanceMile());
            Log.wtf("-this", "Distance :  " + list.get(i).getDistanceMile());
            data.add(new ValueDataEntry(list.get(i).getYear(), distance));
        }
        
        Log.wtf("-this", "Data Size :  " + data.size());
        
        Column column = cartesian.column(data);
        
        column.tooltip()
                .titleFormat("Mile")
                .position(Position.CENTER_BOTTOM)
                .anchor(Anchor.CENTER_BOTTOM)
                .offsetX(0d)
                .offsetY(5d)
                .format("{%Value}{groupsSeparator: }");
        
        cartesian.animation(true);
        // cartesian.title("Races with 1st place finish by distance ");
        
        cartesian.yScale().minimum(0d);
        
      //  cartesian.yAxis(0).labels().format("{%Value}{groupsSeparator: }Mile");
        
        cartesian.tooltip().positionMode(TooltipPositionMode.POINT);
        cartesian.interactivity().hoverMode(HoverMode.BY_X);
        
        cartesian.yAxis(0).title("Distance (Mile)");
        cartesian.xAxis(0).title("Year");
        
        binding.chartYear.setChart(cartesian);
    }
    
    void setColumnChartMonth(List<GraphByMonthDataItem> list) {
        
        Log.wtf("-this", "Graph By Month Column Chart");
        
        binding.chartYear.setVisibility(View.GONE);
        binding.chartMonth.setVisibility(View.VISIBLE);
        binding.chartWeek.setVisibility(View.GONE);
        
        APIlib.getInstance().setActiveAnyChartView(binding.chartMonth);
        
        Cartesian cartesian = AnyChart.column();
        
        List<DataEntry> data = new ArrayList<>();
        
        for (int i = 0; i < list.size(); i++) {
            String month = Months.getMonthFromId(Integer.parseInt(list.get(i).getMonth()));
            Log.wtf("-this", "Month :  " + month);
            double distance = Double.parseDouble(list.get(i).getDistanceMile());
            Log.wtf("-this", "Distance :  " + list.get(i).getDistanceMile());
            data.add(new ValueDataEntry(month, distance));
        }
        
        Log.wtf("-this", "Data Size :  " + data.size());
        
        Column column = cartesian.column(data);
    
        column.tooltip()
                .titleFormat("Mile")
                .position(Position.CENTER_BOTTOM)
                .anchor(Anchor.CENTER_BOTTOM)
                .offsetX(0d)
                .offsetY(5d)
                .format("{%Value}{groupsSeparator: }");
        
        cartesian.animation(true);
        // cartesian.title("Races with 1st place finish by distance ");
        
        cartesian.yScale().minimum(0d);
        
     //   cartesian.yAxis(0).labels().format("{%Value}{groupsSeparator: }Mile");
        
        cartesian.tooltip().positionMode(TooltipPositionMode.POINT);
        cartesian.interactivity().hoverMode(HoverMode.BY_X);
        
        cartesian.yAxis(0).title("Distance (Mile)");
        cartesian.xAxis(0).title("Month");
        
        binding.chartMonth.setChart(cartesian);
    }
    
    void setColumnChartWeek(List<GraphByWeekDataItem> list) {
        
        Log.wtf("-this", "Graph By Week Column Chart");
        
        binding.chartYear.setVisibility(View.GONE);
        binding.chartMonth.setVisibility(View.GONE);
        binding.chartWeek.setVisibility(View.VISIBLE);
        
        APIlib.getInstance().setActiveAnyChartView(binding.chartWeek);
        
        Cartesian cartesian = AnyChart.column();
        
        List<DataEntry> data = new ArrayList<>();
        
        for (int i = 0; i < list.size(); i++) {
            Log.wtf("-this", "Week :  " + list.get(i).getWeek());
            double distance = Double.parseDouble(list.get(i).getDistanceMile());
            Log.wtf("-this", "Distance :  " + list.get(i).getDistanceMile());
            data.add(new ValueDataEntry(list.get(i).getWeek(), distance));
        }
        
        Log.wtf("-this", "Data Size :  " + data.size());
        
        Column column = cartesian.column(data);
    
        column.tooltip()
                .titleFormat("Mile")
                .position(Position.CENTER_BOTTOM)
                .anchor(Anchor.CENTER_BOTTOM)
                .offsetX(0d)
                .offsetY(5d)
                .format("{%Value}{groupsSeparator: }");
        
        cartesian.animation(true);
        // cartesian.title("Races with 1st place finish by distance ");
        
        cartesian.yScale().minimum(0d);
        
       // cartesian.yAxis(0).labels().format("{%Value}{groupsSeparator: }Mile");
        
        cartesian.tooltip().positionMode(TooltipPositionMode.POINT);
        cartesian.interactivity().hoverMode(HoverMode.BY_X);
        
        cartesian.yAxis(0).title("Distance (Mile)");
        cartesian.xAxis(0).title("Week");
        
        binding.chartWeek.setChart(cartesian);
    }
    
    void setColumnChartFirstPlaces(List<FirstPlacesDataItem> list) {
        
        Log.wtf("-this", "Graph By First places Column Chart");
        
        APIlib.getInstance().setActiveAnyChartView(binding.chartFirstPlaces);
        
        Cartesian cartesian = AnyChart.column();
        
        List<DataEntry> data = new ArrayList<>();
        
        for (int i = 0; i < list.size(); i++) {
            Log.wtf("-this", "Race Name :  " + list.get(i).getRaceName());
            double hour = Double.parseDouble(list.get(i).getHours());
            double min = Double.parseDouble(list.get(i).getMinute());
            double sec = Double.parseDouble(list.get(i).getSecond());
            
            double timeInt = (hour * 60) + (min) + (sec / 60);
            DecimalFormat formater = null;
            String timeString = null;
//            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
//                formater = new DecimalFormat("#.##");
//                timeString = formater.format(timeInt);
//            }
            timeString = String.format("%.2f", timeInt);
           
    
            String totalTime = hour + ":" + min + ":" + sec;
            Log.wtf("-this", "Time :  " + timeString);
            timeInt = Double.parseDouble(timeString);
            data.add(new ValueDataEntry(list.get(i).getRaceName(), timeInt));
        }
        
        Log.wtf("-this", "Data Size :  " + data.size());
        
        Column column = cartesian.column(data);
        
        column.tooltip()
                .titleFormat("Min")
                .position(Position.CENTER_BOTTOM)
                .anchor(Anchor.CENTER_BOTTOM)
                .offsetX(0d)
                .offsetY(5d)
                .format("{%Value}{groupsSeparator: }");
        
        cartesian.animation(true);
        // cartesian.title("Races with 1st place finish by distance ");
        
        cartesian.yScale().minimum(0d);
        
     //   cartesian.yAxis(0).labels().format("{%Value}{groupsSeparator: }Min");
        
        cartesian.tooltip().positionMode(TooltipPositionMode.POINT);
        cartesian.interactivity().hoverMode(HoverMode.BY_X);
        
        cartesian.yAxis(0).title("Finish Time (Minutes)");
        cartesian.xAxis(0).title("Races");
        
        binding.chartFirstPlaces.setChart(cartesian);
    }
    
    void setOnClickListeners() {
        binding.progressLayout.setOnClickListener(this);
        binding.imageViewRefreshBarChart.setOnClickListener(this);
        binding.layoutTopCalendar.setOnClickListener(this);
        
    }
    
    @Override
    public void onClick(View v) {
        String api_token = mySharedPreference.getString(MyShardPreferences.token, null);
        switch (v.getId()) {
            case R.id.imageViewRefreshBarChart:
                refreshYearBarChart();
                break;
    
            case R.id.layoutTopCalendar:
                hideShoeCalendar();
                break;
            
            default:
            
        }
        
    }
    
    void hideShoeCalendar(){
        if(binding.layoutCalendar.getVisibility() == View.VISIBLE){
            binding.imageViewCollapse.setImageResource(R.drawable.expand);
            binding.imageViewCalendar.setImageResource(R.drawable.calendar);
            binding.layoutCalendar.setVisibility(View.GONE);
        } else {
            binding.imageViewCollapse.setImageResource(R.drawable.collapse);
            binding.imageViewCalendar.setImageResource(R.drawable.calendar_green);
            binding.layoutCalendar.setVisibility(View.VISIBLE);
        }
    }
    
    void refreshYearBarChart() {
        String token = mySharedPreference.getString(MyShardPreferences.token, "");
        binding.progressLayout.setVisibility(View.VISIBLE);
        getGraphByYearViewModel.getGraphByYear(token);
        year = 0;
        month = 0;
        binding.spinnerMonth.setText("");
        binding.spinnerYear.setText("");
    }
    
    
}
