package com.technerds.racelogger.ui.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.technerds.racelogger.ui.RaceDetailActivity;
import com.technerds.racelogger.ui.RunDetailActivity;
import com.technerds.racelogger.viewModels.DeleteRunViewModel;
import com.technerds.racelogger.dataModels.runListingModel.RunListingDataItem;
import com.technerds.racelogger.dataModels.runListingModel.RunListingModel;
import com.technerds.racelogger.network.Status;
import com.technerds.racelogger.R;
import com.technerds.racelogger.Utils.MyShardPreferences;
import com.technerds.racelogger.viewModels.GetRunListingsViewModel;
import com.technerds.racelogger.adapters.RunAdapter;
import com.technerds.racelogger.databinding.FragmentRunBinding;
import com.technerds.racelogger.ui.AddRunActivity;
import com.tsuryo.swipeablerv.SwipeLeftRightCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;


public class RunFragment extends Fragment implements View.OnClickListener {
    
    FragmentRunBinding binding;
    View view;
    SharedPreferences.Editor editor;
    SharedPreferences mySharedPreference;
    
    GetRunListingsViewModel getRunListingsViewModel;
    RunListingModel runListingModel;
    RunAdapter adapter;
    
    DeleteRunViewModel deleteRunViewModel;
    
    List<RunListingDataItem> runList = new ArrayList<>();
    int selectedPage = 1;
    int recyclerState = 0;
    private boolean endCalled = false;
  
    
    
    public RunFragment() {
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentRunBinding.inflate(getLayoutInflater());
        setupViewModels();
        init();
        view = binding.getRoot();
        return view;
    }
    
    public void setupViewModels() {
        
        getRunListingsViewModel = new ViewModelProvider(this).get(GetRunListingsViewModel.class);
        getRunListingObserveViewModel(getRunListingsViewModel);
    
        deleteRunViewModel = new ViewModelProvider(this).get(DeleteRunViewModel.class);
        deleteRunRecordObserveViewModel(deleteRunViewModel);
    }
    
    private void init() {
        mySharedPreference = getContext().getSharedPreferences(MyShardPreferences.mPreferenceName, MODE_PRIVATE);
        editor = mySharedPreference.edit();
        
        setOnClickListeners();
        String token = mySharedPreference.getString(MyShardPreferences.token,"");
        binding.progressLayout.setVisibility(View.VISIBLE);
        getRunListingsViewModel.getRunListing(token,selectedPage);
        
        
    }
    
    private void getRunListingObserveViewModel(final GetRunListingsViewModel viewModel) {
        // Observe project data
        viewModel.getResponse().observe(getViewLifecycleOwner(), apiResponse -> {
            
            if (apiResponse != null) {
                if (apiResponse.status == Status.SUCCESS) {
                    binding.progressLayout.setVisibility(View.GONE);
                    Log.wtf("-this", "Run Listing Response : " + apiResponse.data);
                    Response<ResponseBody> responseBodyResponse = (Response<ResponseBody>) apiResponse.data;
                    
                    try {
                        if (responseBodyResponse.body() != null) {
                            String json = responseBodyResponse.body().string();
                            Log.wtf("retrofit_", "Run Listing response body: " + json);
                            JSONObject jsonObject = new JSONObject(json);
                            int status = jsonObject.getInt("status");
                            if (status == 1) {
                                Gson gson = new Gson();
                                runListingModel = gson.fromJson(json, RunListingModel.class);
                                if (runListingModel != null) {
                                    if(runListingModel.getData().getCurrentPage() == 1) {
                                        runList = runListingModel.getData().getData();
                                        setupAdapter();
                                    } else {
                                        runList.addAll(runListingModel.getData().getData());
                                        adapter.notifyDataSetChanged();
                                        endCalled = false;
                                    }
                                }
        
                            } else {
        
                                String error = jsonObject.getString("error");
                                Snackbar.make(view, "" + error, Snackbar.LENGTH_SHORT).show();
                                Log.wtf("retrofit_", "Run Listing response body: " + error);
        
                            }
                        }
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                        
                    }
                    
                } else if (apiResponse.status == Status.ERROR) {
                    Log.wtf("-this", "Run Listing Response : " + apiResponse.error.getMessage());
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
                    if(recyclerState == RecyclerView.SCROLL_STATE_IDLE){
                        if(endCalled == false) {
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
        Log.wtf("-this", "138 RunListing Size : " + runList.size());
        if (runList.size() > 0) {
            adapter = new RunAdapter(getContext(), runList, this);
            binding.recycler.setAdapter(adapter);
            binding.recycler.setListener(new SwipeLeftRightCallback.Listener() {
                @Override
                public void onSwipedLeft(int position) {
            
                    Log.wtf("-conver", " Position : " + position);
                    Log.wtf("-conver", " run_id : " + runList.get(position).getId());
            
                    binding.progressLayout.setVisibility(View.VISIBLE);
                    deleteRunViewModel.deleteRunRecord(mySharedPreference.getString(MyShardPreferences.token, ""),
                            runList.get(position).getId());
    
                    runList.remove(position);
                    adapter.notifyDataSetChanged();
                }
        
                @Override
                public void onSwipedRight(int position) {
                }
            });
        }
    }
    
    private void deleteRunRecordObserveViewModel(DeleteRunViewModel viewModel) {
        // Observe project data
        viewModel.getResponse().observe(getViewLifecycleOwner(), apiResponse -> {
            
            if (apiResponse != null) {
                if (apiResponse.status == Status.SUCCESS) {
                    binding.progressLayout.setVisibility(View.GONE);
                    
                    Log.wtf("-this", "Delete Run API Response : " + apiResponse.data);
                    Response<ResponseBody> responseBodyResponse = (Response<ResponseBody>) apiResponse.data;
                    
                    try {
                        String json = responseBodyResponse.body().string();
                        Log.wtf("retrofit_", "Delete Run response body: " + json);
                        JSONObject jsonObject = new JSONObject(json);
                        
                        if (jsonObject.getInt("status") == 1) {
                        
                        } else {
                        
                        }
                        
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }
                    
                    
                    binding.progressLayout.setVisibility(View.GONE);
                    
                } else if (apiResponse.status == Status.ERROR) {
                    Log.wtf("-this", "Delete Run API Response : " + apiResponse.error.getMessage());
                    binding.progressLayout.setVisibility(View.GONE);
                }
                binding.progressLayout.setVisibility(View.GONE);
            }
        });
    }
    
    private void callNextPage() {
        if(selectedPage < runListingModel.getData().getTotal()){
            selectedPage++;
            binding.progressLayout.setVisibility(View.VISIBLE);
            String api_token = mySharedPreference.getString(MyShardPreferences.token,null);
            getRunListingsViewModel.getRunListing(api_token,selectedPage);
        }
    }
    
    public void RunDetailClicked(RunListingDataItem model) {
//        Intent intent = new Intent(this, ClinicApplicationDetailActivity.class);
//        Gson gson = new Gson();
//        String myJson = gson.toJson(model);
//        intent.putExtra("myjson", myJson);
//        intent.putExtra("source", "activeJobs");
//        startActivity(intent);
    
        Intent intent = new Intent(getContext(), RunDetailActivity.class);
        intent.putExtra("run_id", model.getId());
        startActivity(intent);
    }
    
    
    void setOnClickListeners() {
        binding.progressLayout.setOnClickListener(this);
        binding.floatingButtonAdd.setOnClickListener(this);
        
    }
    
    
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.floatingButtonAdd:
                Intent intent = new Intent(getContext(), AddRunActivity.class);
                startActivityForResult(intent,7);
                binding.floatingButtonAdd.setOnClickListener(null);
                break;
        
            default:
            
        }
        
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
    
}
