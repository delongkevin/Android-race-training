package com.technerds.racelogger.adapters;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.technerds.racelogger.R;
import com.technerds.racelogger.Utils.DateFormatter;
import com.technerds.racelogger.Utils.StringChecker;
import com.technerds.racelogger.dataModels.runListingModel.RunListingDataItem;
import com.technerds.racelogger.dataModels.shoeModels.getShoeProfiles.ShoeProfileDataItem;
import com.technerds.racelogger.ui.ShoeActivity;
import com.technerds.racelogger.ui.fragments.RunFragment;

import java.util.List;

public class ShoeAdapter extends RecyclerView.Adapter<ShoeAdapter.MyViewHolder> {
    
    private List<ShoeProfileDataItem> arrayList;
    Context context;
    Activity activity;
    
    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textViewName, textViewDistance, textViewBrand, textViewModel,
                textViewDate, textViewSize, textViewWidth;
        CardView cardViewMain;
        
        
        private MyViewHolder(View view) {
            super(view);
            
            textViewName = view.findViewById(R.id.textViewName);
            textViewBrand = view.findViewById(R.id.textViewBrand);
            textViewDistance = view.findViewById(R.id.textViewDistance);
            textViewModel = view.findViewById(R.id.textViewModel);
            textViewDate = view.findViewById(R.id.textViewDate);
            textViewSize = view.findViewById(R.id.textViewSize);
            textViewWidth = view.findViewById(R.id.textViewWidth);
            cardViewMain = view.findViewById(R.id.cardViewMain);
            
            
        }
        
    }
    
    public ShoeAdapter(Context context, List<ShoeProfileDataItem> arrayList, Activity activity) {
        this.context = context;
        this.arrayList = arrayList;
        this.activity = activity;
    }
    
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_shoe, parent, false);
        return new MyViewHolder(itemView);
        
    }
    
    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        
        holder.textViewName.setText(arrayList.get(position).getShoeProfile());
        holder.textViewDistance.setText(arrayList.get(position).getShoeMileage());
        holder.textViewBrand.setText("Brand Name : " + arrayList.get(position).getShoeName());
        holder.textViewModel.setText("Model : " + arrayList.get(position).getModel());
        holder.textViewDate.setText(arrayList.get(position).getDatePurchased());
        holder.textViewSize.setText("Size  : " + arrayList.get(position).getSize());
        holder.textViewWidth.setText("Width : " + arrayList.get(position).getWidth());
        
        
        holder.cardViewMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    ((ShoeActivity) activity).editShoeClicked(arrayList.get(position));
            }
        });
    }
    
    
    @Override
    public int getItemCount() {
        if (arrayList != null) {
            if (arrayList.size() > 0) {
                return arrayList.size();
            } else {
                return 0;
            }
        } else {
            return 0;
        }
        
    }
    
}

