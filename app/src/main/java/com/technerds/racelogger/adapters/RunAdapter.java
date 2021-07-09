package com.technerds.racelogger.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.technerds.racelogger.Utils.DateFormatter;
import com.technerds.racelogger.Utils.StringChecker;
import com.technerds.racelogger.dataModels.runListingModel.RunListingDataItem;
import com.technerds.racelogger.R;
import com.technerds.racelogger.ui.fragments.RunFragment;

import java.util.List;

public class RunAdapter extends RecyclerView.Adapter<RunAdapter.MyViewHolder> {

    private List<RunListingDataItem> arrayList;
    Context context;
    Fragment fragment;

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textViewName, textViewLocation, textViewDistance,
                textViewDescription, textViewTime, textViewDate;
        CardView cardViewMain;


        private MyViewHolder(View view) {
            super(view);
    
            textViewName = view.findViewById(R.id.textViewName);
            textViewLocation = view.findViewById(R.id.textViewLocation);
            textViewDistance = view.findViewById(R.id.textViewDistance);
            textViewDescription =  view.findViewById(R.id.textViewDescription);
            textViewDate =  view.findViewById(R.id.textViewDate);
            textViewTime =  view.findViewById(R.id.textViewTime);
            cardViewMain =  view.findViewById(R.id.cardViewMain);
           

        }

    }

    public RunAdapter(Context context, List<RunListingDataItem> arrayList, Fragment fragment) {
        this.context = context;
        this.arrayList = arrayList;
        this.fragment = fragment;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_run, parent, false);
        return new MyViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

       holder.textViewName.setText(arrayList.get(position).getTrainingRoute());
        String unit = arrayList.get(position).getUnit();
        if (StringChecker.checkString(unit)) {
            if (unit.equalsIgnoreCase("KM") || unit.equalsIgnoreCase("K")) {
                unit = "K";
            }
        }
        if (StringChecker.checkString(arrayList.get(position).getUser_distance()) && StringChecker.checkString(unit))
        {
            String distance = arrayList.get(position).getUser_distance() + "" + unit;
            setCardBg(distance,holder.cardViewMain);
        
        }
        holder.textViewDistance.setText(arrayList.get(position).getUser_distance() + "" + unit);
    
        holder.textViewDescription.setText(arrayList.get(position).getParkName());
        holder.textViewLocation.setText(arrayList.get(position).getStreetAddress());
        holder.textViewDate.setText(DateFormatter.getFormattedDateTimeFromDateTimeStringAmPm(arrayList.get(position).getDate()));
    
        Log.wtf("--runAdp"," Date : "+ arrayList.get(position).getDate());
        
        holder.cardViewMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((RunFragment) fragment).RunDetailClicked(arrayList.get(position));
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
    
    public void setCardBg(String distance, CardView cardView) {
        if (distance.equalsIgnoreCase("1 mile") || distance.equalsIgnoreCase("1mile")) {
            cardView.setCardBackgroundColor(context.getResources().getColor(R.color.green_opac_thirty));
            
        } else if (distance.equalsIgnoreCase("5K") || (distance.equalsIgnoreCase("5 K"))) {
            cardView.setCardBackgroundColor(context.getResources().getColor(R.color.blue_opac_thirty));
            
        } else if (distance.equalsIgnoreCase("10K") || distance.equalsIgnoreCase("10 K")) {
            cardView.setCardBackgroundColor(context.getResources().getColor(R.color.red_opac_thirty));
            
        } else if (distance.equalsIgnoreCase("15K") || (distance.equalsIgnoreCase("15 K"))) {
            cardView.setCardBackgroundColor(context.getResources().getColor(R.color.purple_opac_thirty));
            
        } else if (distance.equalsIgnoreCase("20K") || distance.equalsIgnoreCase("20 K")) {
            cardView.setCardBackgroundColor(context.getResources().getColor(R.color.yellow_opac_thirty));
            
        } else if (distance.equalsIgnoreCase("1 Marathon") || (distance.equalsIgnoreCase("1Marathon"))) {
            cardView.setCardBackgroundColor(context.getResources().getColor(R.color.cyan_opac_thirty));
            
        } else if (distance.equalsIgnoreCase("1.5 Marathon") || distance.equalsIgnoreCase("1.5Marathon")) {
            cardView.setCardBackgroundColor(context.getResources().getColor(R.color.brown_opac_thirty));
            
        } else if (distance.equalsIgnoreCase("30K") || (distance.equalsIgnoreCase("30 K"))) {
            cardView.setCardBackgroundColor(context.getResources().getColor(R.color.pink_opac_thirty));
            
        } else if (distance.equalsIgnoreCase("50 K") || distance.equalsIgnoreCase("50K")) {
            cardView.setCardBackgroundColor(context.getResources().getColor(R.color.orange_opac_thirty));
            
        } else if (distance.equalsIgnoreCase("50mile") || (distance.equalsIgnoreCase("50 mile"))) {
            cardView.setCardBackgroundColor(context.getResources().getColor(R.color.dull_blue_opac_thirty));
            
        } else if (distance.equalsIgnoreCase("100 mile") || distance.equalsIgnoreCase("100mile")) {
            cardView.setCardBackgroundColor(context.getResources().getColor(R.color.white_opac_thirty));
            
        } else  {
            cardView.setCardBackgroundColor(context.getResources().getColor(R.color.white_opac_thirty));
        }
    }

}

