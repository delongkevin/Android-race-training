package com.technerds.racelogger.ui.fragments;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.technerds.racelogger.Utils.AgeGroups;
import com.technerds.racelogger.Utils.AppUtils;
import com.technerds.racelogger.Utils.DateFormatter;
import com.technerds.racelogger.Utils.StringChecker;
import com.technerds.racelogger.dataModels.SortedRaceListingSendModel;
import com.technerds.racelogger.dataModels.dropDownModels.DropDownModel;
import com.technerds.racelogger.dataModels.raceDetailModel.RaceData;
import com.technerds.racelogger.dataModels.searchRaceModel.SearchRaceModel;
import com.technerds.racelogger.viewModels.DeleteRaceViewModel;
import com.technerds.racelogger.dataModels.raceListingModel.RaceListingModel;
import com.technerds.racelogger.network.Status;
import com.technerds.racelogger.R;
import com.technerds.racelogger.Utils.MyShardPreferences;
import com.technerds.racelogger.viewModels.GetDropDownViewModel;
import com.technerds.racelogger.viewModels.GetRaceListingsViewModel;
import com.technerds.racelogger.adapters.RaceAdapter;
import com.technerds.racelogger.databinding.FragmentRaceBinding;
import com.technerds.racelogger.ui.AddRaceActivity;
import com.technerds.racelogger.ui.RaceDetailActivity;
import com.technerds.racelogger.viewModels.GetSortedRaceListingViewModel;
import com.technerds.racelogger.viewModels.SearchRaceNameViewModel;
import com.tsuryo.swipeablerv.SwipeLeftRightCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;


public class RaceFragment extends Fragment implements View.OnClickListener {
    
    FragmentRaceBinding binding;
    View view;
    SharedPreferences.Editor editor;
    SharedPreferences mySharedPreference;
    
    GetSortedRaceListingViewModel getSortedRaceListingViewModel;
    //  GetRaceListingsViewModel getRaceListingsViewModel;
    RaceListingModel raceListingModel;
    RaceAdapter adapter;
    
    DeleteRaceViewModel deleteRaceViewModel;
    
    GetDropDownViewModel getDropDownViewModel;
    DropDownModel dropDownModel;
    
    SearchRaceNameViewModel searchRaceNameViewModel;
    SearchRaceModel searchRaceModel;
    SortedRaceListingSendModel model;
    
    List<RaceData> raceList = new ArrayList<>();
    int selectedPage = 1;
    int sortSelectedPage = 1;
    int recyclerState = 0;
    private boolean endCalled = false;
    
    String raceDate = null;
    Calendar calendar;
    
    String distance = null;
    String raceName = null;
    
    boolean buttonActive = true;
    
    
    public RaceFragment() {
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentRaceBinding.inflate(getLayoutInflater());
        setupViewModels();
        init();
        view = binding.getRoot();
        return view;
    }
    
    public void setupViewModels() {

        
        deleteRaceViewModel = new ViewModelProvider(this).get(DeleteRaceViewModel.class);
        deleteRaceRecordObserveViewModel(deleteRaceViewModel);
        
        getDropDownViewModel = new ViewModelProvider(this).get(GetDropDownViewModel.class);
        getDropDownsObserveViewModel(getDropDownViewModel);
        
        searchRaceNameViewModel = new ViewModelProvider(this).get(SearchRaceNameViewModel.class);
        searchRaceNameObserveViewModel(searchRaceNameViewModel);
        
        getSortedRaceListingViewModel = new ViewModelProvider(this).get(GetSortedRaceListingViewModel.class);
        getSortedRaceListingObserveViewModel(getSortedRaceListingViewModel);
    }
    
    private void init() {
        mySharedPreference = getContext().getSharedPreferences(MyShardPreferences.mPreferenceName, MODE_PRIVATE);
        editor = mySharedPreference.edit();
        calendar = Calendar.getInstance();
        model = new SortedRaceListingSendModel();
        setOnClickListeners();
        String token = mySharedPreference.getString(MyShardPreferences.token, "");
        binding.progressLayout.setVisibility(View.VISIBLE);
        // getRaceListingsViewModel.getRaceListing(token, selectedPage);
        model.setToken(token);
        model.setPage(selectedPage);
        getSortedRaceListingViewModel.getSortedRaceListing(model);
        getDropDownViewModel.getDropDown();
        searchRaceNameViewModel.searchRaceName(token);
        
        
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
                                        setupAdapter();
                                    } else {
                                        raceList.addAll(raceListingModel.getData().getData());
                                        adapter.notifyDataSetChanged();
                                        endCalled = false;
                                    }
                                }
                                
                            } else {
                                
                                String error = jsonObject.getString("error");
                                Snackbar.make(view, "" + error, Snackbar.LENGTH_SHORT).show();
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
    
    void setupAdapter() {
        binding.recycler.removeAllViews();
        binding.recycler.setAdapter(null);
        binding.recycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                switch (newState) {
                    case RecyclerView.SCROLL_STATE_IDLE:
                        //  Log.wtf("-recycler","The RecyclerView is not scrolling");
                        break;
                    case RecyclerView.SCROLL_STATE_DRAGGING:
                        //    Log.wtf("-recycler","Scrolling now");
                        break;
                    case RecyclerView.SCROLL_STATE_SETTLING:
                        //  Log.wtf("-recycler","Scroll Settling");
                        break;
                }
            }
            
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dx > 0) {
                    //   Log.wtf("-recycler","Scrolled Right");
                } else if (dx < 0) {
                    //  Log.wtf("-recycler","Scrolled Left");
                } else {
                    //  Log.wtf("-recycler","No Horizontal Scrolled");
                }
                
                if (dy > 0) {
                    //  Log.wtf("-recycler","Scrolled Downwards");
                    if (recyclerState == RecyclerView.SCROLL_STATE_IDLE) {
                        if (endCalled == false) {
                            Log.wtf("-recycler", " End reached ");
                            callNextPage();
                            endCalled = true;
                        }
                    }
                } else if (dy < 0) {
                    //   Log.wtf("-recycler","Scrolled Upwards");
                } else {
                    // Log.wtf("-recycler","No Vertical Scrolled");
                }
            }
        });
        binding.recycler.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        Log.wtf("-this", "138 RaceList Size : " + raceList.size());
        if (raceList.size() > 0) {
            adapter = new RaceAdapter(getContext(), raceList, this);
            binding.recycler.setAdapter(adapter);
            binding.recycler.setListener(new SwipeLeftRightCallback.Listener() {
                @Override
                public void onSwipedLeft(int position) {
                    
                    Log.wtf("-conver", " Position : " + position);
                    Log.wtf("-conver", " race_id : " + raceList.get(position).getId());
                    
                    binding.progressLayout.setVisibility(View.VISIBLE);
                    deleteRaceViewModel.deleteRaceRecord(mySharedPreference.getString(MyShardPreferences.token, ""),
                            raceList.get(position).getId());
                    
                    raceList.remove(position);
                    adapter.notifyDataSetChanged();
                }
                
                @Override
                public void onSwipedRight(int position) {
                }
            });
        }
    }
    
    private void deleteRaceRecordObserveViewModel(DeleteRaceViewModel viewModel) {
        // Observe project data
        viewModel.getResponse().observe(getViewLifecycleOwner(), apiResponse -> {
            
            if (apiResponse != null) {
                if (apiResponse.status == Status.SUCCESS) {
                    binding.progressLayout.setVisibility(View.GONE);
                    
                    Log.wtf("-this", "Delete Race API Response : " + apiResponse.data);
                    Response<ResponseBody> responseBodyResponse = (Response<ResponseBody>) apiResponse.data;
                    
                    try {
                        String json = responseBodyResponse.body().string();
                        Log.wtf("retrofit_", "Delete Race response body: " + json);
                        JSONObject jsonObject = new JSONObject(json);
                        
                        if (jsonObject.getInt("status") == 1) {
                        
                        } else {
                        
                        }
                        
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }
                    
                    
                    binding.progressLayout.setVisibility(View.GONE);
                    
                } else if (apiResponse.status == Status.ERROR) {
                    Log.wtf("-this", "Delete Race API Response : " + apiResponse.error.getMessage());
                    binding.progressLayout.setVisibility(View.GONE);
                }
                binding.progressLayout.setVisibility(View.GONE);
            }
        });
    }
    
    private void callNextPage() {
        if (selectedPage < raceListingModel.getData().getTotal()) {
            selectedPage++;
            binding.progressLayout.setVisibility(View.VISIBLE);
//            String token = mySharedPreference.getString(MyShardPreferences.token, null);
//            getRaceListingsViewModel.getRaceListing(api_token, selectedPage);
          
            model.setPage(selectedPage);
            getSortedRaceListingViewModel.getSortedRaceListing(model);
        }
    }
    
    public void RaceDetailClicked(RaceData model) {
//        Intent intent = new Intent(this, ClinicApplicationDetailActivity.class);
//        Gson gson = new Gson();
//        String myJson = gson.toJson(model);
//        intent.putExtra("myjson", myJson);
//        intent.putExtra("source", "activeJobs");
//        startActivity(intent);

        Intent intent = new Intent(getContext(), RaceDetailActivity.class);
        AppUtils.race_id=model.getId();
        intent.putExtra("race_id", model.getId());
        startActivity(intent);
    }
    
    private void getDropDownsObserveViewModel(final GetDropDownViewModel viewModel) {
        // Observe project data
        viewModel.getResponse().observe(getViewLifecycleOwner(), apiResponse -> {
            
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
        
        String[] distanceList = new String[dropDownModel.getData().getDistance().size()+1];
        
        for (int i = 0; i < dropDownModel.getData().getDistance().size(); i++) {
            if(i == 0){
                distanceList[i] = "";
            }
            distanceList[i+1] = dropDownModel.getData().getDistance().get(i).getName();
        }
        
        binding.spinnerDistance.setItems(distanceList);
        binding.spinnerDistance.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {
            
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                if (item != null) {
                    int i = position + 1;
                    //   distanceType = i;
                    distance = item;
                    
                }
            }
        });
        binding.spinnerDistance.setSelectedIndex(-1);
        
        
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
                                setupRaceSpinner();
                                
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
                            binding.editTextParkName.setText(searchRaceModel.getData().getData().get(i).getParkName());
                            binding.editTextCountry.setText(searchRaceModel.getData().getData().get(i).getCountry());
                            binding.editTextCity.setText(searchRaceModel.getData().getData().get(i).getCity());
                            binding.editTextState.setText(searchRaceModel.getData().getData().get(i).getState());
                            binding.editTextZipCode.setText(searchRaceModel.getData().getData().get(i).getZipCode());
                        }
                    
                    }
                }
            });
        }
    }
    
    void setOnClickListeners() {
        binding.progressLayout.setOnClickListener(this);
        binding.floatingButtonAdd.setOnClickListener(this);
        binding.layoutFilter.setOnClickListener(this);
        binding.buttonClearFilter.setOnClickListener(this);
        binding.buttonApplyFilter.setOnClickListener(this);
        binding.imageViewFilter.setOnClickListener(this);
        binding.imageViewClosePopup.setOnClickListener(this);
        binding.textViewDateTime.setOnClickListener(this);
        binding.buttonDoneRaceDate.setOnClickListener(this);
        binding.layoutRaceDateTime.setOnClickListener(this);
        binding.layoutRaceDateTime.setOnClickListener(this);
    }
    
    @Override
    public void onClick(View v) {
        hideKeyBoardClicked(v);
        switch (v.getId()) {
            case R.id.floatingButtonAdd:
                Intent intent = new Intent(getContext(), AddRaceActivity.class);
                startActivityForResult(intent, 7);
                binding.floatingButtonAdd.setOnClickListener(null);
                break;
            
            case R.id.layoutFilter:
                break;
            
            case R.id.imageViewFilter:
                showFilterPopup();
                break;
            case R.id.buttonClearFilter:
                clearFilterPopup();
                break;
            case R.id.buttonApplyFilter:
                applyFilterPopup();
                break;
            
            case R.id.imageViewClosePopup:
                closeFilterPopup();
                break;
    
            case R.id.textViewDateTime:
                RaceDateTimeClicked(v);
                break;
    
            case R.id.buttonDoneRaceDate:
                RaceDateDoneClicked(v);
                break;
            
            default:
            
        }
        
    }
    
    void showFilterPopup() {
        binding.layoutFilter.setVisibility(View.VISIBLE);
    }
    
    void closeFilterPopup() {
        binding.layoutFilter.setVisibility(View.GONE);
    }
    
    void clearFilterPopup() {
        binding.spinnerRaceName.setSelectedIndex(0);
        binding.editTextCity.setText("");
        binding.editTextState.setText("");
        binding.editTextZipCode.setText("");
        binding.editTextCountry.setText("");
        binding.spinnerDistance.setSelectedIndex(0);
        distance = "";
        binding.editTextParkName.setText("");
        binding.editTextFinishPlace.setText("");
        binding.textViewDateTime.setText("");
        raceDate = "";
    }
    
    void applyFilterPopup() {
        String race_name = binding.spinnerRaceName.getText().toString();
        String park_name = binding.editTextParkName.getText().toString();
        String city = binding.editTextCity.getText().toString();
        String state = binding.editTextState.getText().toString();
        String country = binding.editTextCountry.getText().toString();
        String zip_code = binding.editTextZipCode.getText().toString();
        String distanceFull = binding.spinnerDistance.getText().toString();
        String finish_place = binding.editTextFinishPlace.getText().toString();
        
        
        String token = mySharedPreference.getString(MyShardPreferences.token, "");
        
        model.setToken(token);
        
        model.setPage(selectedPage);
        
        if (StringChecker.checkString(race_name)) {
            model.setRace_name(race_name);
        } else {
            model.setRace_name("");
        }
        
        if (StringChecker.checkString(park_name)) {
            model.setPark_name(park_name);
        } else {
            model.setPark_name("");
        }
        
        if (StringChecker.checkString(city)) {
            model.setCity(city);
        }else {
            model.setCity("");
        }
        
        
        if (StringChecker.checkString(state)) {
            model.setState(state);
        }else {
            model.setState("");
        }
        
        if (StringChecker.checkString(country)) {
            model.setCountry(country);
        }else {
            model.setCountry("");
        }
        
        if (StringChecker.checkString(zip_code)) {
            model.setZip_code(zip_code);
        }else {
            model.setZip_code("");
        }
        
        if (StringChecker.checkString(finish_place)) {
            model.setFinish_place(finish_place);
        }else {
            model.setFinish_place("");
        }
        
        if (StringChecker.checkString(raceDate)) {
            model.setDate(raceDate);
        }else {
            model.setDate("");
        }
    
        String distance = "";
        String unit = "KM";
    
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
                distance =distanceTemp;
            
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
                distance = distanceTemp;
            
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
                distance =distanceTemp;
            
            } else {
                Log.wtf("-this", " DistanceFull KM IF: ");
                int mIndex = -1;
            
            
                Log.wtf("-this", " DistanceFull Marathon INDEX : " + mIndex);
                String distanceTemp = distanceFull;
                unit = "Km";
            
                Log.wtf("-this", " DistanceTemp  : " + distanceTemp + "   UNIT : " + unit);
                if (distanceTemp != null) {
                    if (distanceTemp.matches("[0-9]+") && distanceTemp.length() > 0) {
                        distance = distanceTemp;
                    }
                }
            
            }
        } else {
            Log.wtf("-this", " DistanceFull : NULL");
        }
        if(StringChecker.checkString(distance)){
            model.setDistance(distance);
            model.setUnit(unit);
        } else {
            model.setDistance("");
            model.setUnit("");
        }
        
        
        closeFilterPopup();
        binding.progressLayout.setVisibility(View.VISIBLE);
        getSortedRaceListingViewModel.getSortedRaceListing(model);
    }
    
    private void getSortedRaceListingObserveViewModel(final GetSortedRaceListingViewModel viewModel) {
        // Observe project data
        viewModel.getResponse().observe(getViewLifecycleOwner(), apiResponse -> {
            
            if (apiResponse != null) {
                if (apiResponse.status == Status.SUCCESS) {
                    binding.progressLayout.setVisibility(View.GONE);
                    Log.wtf("-this", "Sorted Race Listing Response : " + apiResponse.data);
                    Response<ResponseBody> responseBodyResponse = (Response<ResponseBody>) apiResponse.data;
                    
                    try {
                        if (responseBodyResponse.body() != null) {
                            String json = responseBodyResponse.body().string();
                            Log.wtf("retrofit_", "Sorted Race Listing response body: " + json);
                            JSONObject jsonObject = new JSONObject(json);
                            int status = jsonObject.getInt("status");
                            if (status == 1) {
                                Gson gson = new Gson();
                                raceListingModel = gson.fromJson(json, RaceListingModel.class);
                                if (raceListingModel != null) {
                                    if (raceListingModel.getData().getCurrentPage() == 1) {
                                        raceList = raceListingModel.getData().getData();
                                        setupAdapter();
                                    } else {
                                        raceList.addAll(raceListingModel.getData().getData());
                                        adapter.notifyDataSetChanged();
                                        endCalled = false;
                                    }
                                } else {
                                    raceList.clear();
                                    adapter.notifyDataSetChanged();
                                }
                                
                            } else {
                                
                                String error = jsonObject.getString("error");
                                Snackbar.make(view, "" + error, Snackbar.LENGTH_SHORT).show();
                                Log.wtf("retrofit_", "Race Listing response body: " + error);
    
                                raceList.clear();
                                if(adapter != null) {
                                    adapter.notifyDataSetChanged();
                                }
                                
                            }
                        }
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                        
                    }
                    
                } else if (apiResponse.status == Status.ERROR) {
                    Log.wtf("-this", "Sorted Race Listing Response : " + apiResponse.error.getMessage());
                    binding.progressLayout.setVisibility(View.GONE);
                }
            } else {
                binding.progressLayout.setVisibility(View.GONE);
            }
        });
    }
    
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 7) {
            binding.floatingButtonAdd.setOnClickListener(this);
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
    
    public void hideKeyBoardClicked(View view) {
        
        Log.wtf("-hide", "Hide keyboard Clicked");
        
        binding.editTextCity.clearFocus();
        binding.editTextState.clearFocus();
        binding.editTextZipCode.clearFocus();
        binding.editTextCountry.clearFocus();
        binding.editTextParkName.clearFocus();
        binding.editTextFinishPlace.clearFocus();
        
        
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        
        getActivity().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );
        
        
    }
    
    public void RaceDateTimeClicked(View view) {
        hideKeyBoardClicked(view);
        binding.layoutRaceDateTime.setVisibility(View.VISIBLE);
        
        int yy = calendar.get(Calendar.YEAR);
        int mm = calendar.get(Calendar.MONTH);
        int dd = calendar.get(Calendar.DAY_OF_MONTH);
        int hh = calendar.get(Calendar.HOUR_OF_DAY);
        int mn = calendar.get(Calendar.MINUTE);
        binding.datePickerRace.updateDate(yy, mm, dd);
       
    }
    
    public void RaceDateDoneClicked(View view) {
        int day, month, year;
        
        day = binding.datePickerRace.getDayOfMonth();
        month = binding.datePickerRace.getMonth() + 1;
        year = binding.datePickerRace.getYear();
        
        raceDate = getNumberFormat(year) + "-" + getNumberFormat(month) + "-" + getNumberFormat(day);
        
        Log.wtf("-Date", "Race Date : " + raceDate);
        
        
        binding.textViewDateTime.setText(DateFormatter.getFormattedDateFromDateString(raceDate));
        
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
    
}
