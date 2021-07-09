package com.technerds.racelogger.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.technerds.racelogger.Utils.DateFormatter;
import com.technerds.racelogger.Utils.StringChecker;
import com.technerds.racelogger.R;
import com.technerds.racelogger.dataModels.raceDetailModel.RaceData;
import com.technerds.racelogger.ui.fragments.RaceFragment;

import java.util.List;

public class RaceAdapter extends RecyclerView.Adapter<RaceAdapter.MyViewHolder> {
    
    private List<RaceData> arrayList;
    Context context;
    Fragment fragment;
    
    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textViewRaceName, textViewRaceDistance,
                textViewFinishTime, textViewFinishPlace, textViewAgeGroupFinishPlace,
                textViewRaceLocation, textViewRaceDate;
        LinearLayout layoutRaceLocation, layoutRating;
        CardView cardViewMain;
        ImageView imageStar1, imageStar2, imageStar3, imageStar4, imageStar5;
        
        
        private MyViewHolder(View view) {
            super(view);
            
            textViewRaceName = view.findViewById(R.id.textViewRaceName);
            textViewRaceDistance = view.findViewById(R.id.textViewRaceDistance);
            textViewFinishTime = view.findViewById(R.id.textViewFinishTime);
            textViewFinishPlace = view.findViewById(R.id.textViewFinishPlace);
            textViewAgeGroupFinishPlace = view.findViewById(R.id.textViewAgeGroupFinishPlace);
            textViewRaceLocation = view.findViewById(R.id.textViewRaceLocation);
            textViewRaceDate = view.findViewById(R.id.textViewRaceDate);
            layoutRaceLocation = view.findViewById(R.id.layoutRaceLocation);
            cardViewMain = view.findViewById(R.id.cardViewMain);
            
            layoutRating = view.findViewById(R.id.layoutRating);
            imageStar1 = view.findViewById(R.id.imageStar1);
            imageStar2 = view.findViewById(R.id.imageStar2);
            imageStar3 = view.findViewById(R.id.imageStar3);
            imageStar4 = view.findViewById(R.id.imageStar4);
            imageStar5 = view.findViewById(R.id.imageStar5);
        }
        
    }
    
    public RaceAdapter(Context context, List<RaceData> arrayList, Fragment fragment) {
        this.context = context;
        this.arrayList = arrayList;
        this.fragment = fragment;
    }
    
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_race, parent, false);
        return new MyViewHolder(itemView);
        
    }
    
    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        if(arrayList.get(position).getRace() != null) {
            holder.textViewRaceName.setText(arrayList.get(position).getRace().getRaceName());
    
            String unit = arrayList.get(position).getRace().getUnit();
            if (StringChecker.checkString(unit)) {
                if (unit.equalsIgnoreCase("KM") || unit.equalsIgnoreCase("K")) {
                    unit = "K";
                }
            }
            if (StringChecker.checkString(arrayList.get(position).getRace().getUserDistance()) && StringChecker.checkString(unit)) {
                String distance = arrayList.get(position).getRace().getUserDistance() + "" + unit;
                holder.textViewRaceDistance.setText(distance);
                setCardBg(distance, holder.cardViewMain);
        
            }
            String parkF = arrayList.get(position).getRace().getParkName() + ", " +
                    arrayList.get(position).getRace().getCity() + ", " +
                    arrayList.get(position).getRace().getState() + ", " +
                    arrayList.get(position).getRace().getCountry();
            holder.textViewRaceLocation.setText(parkF);
    
            String enjoyString = arrayList.get(position).getRace().getEnjoymentRating().getRating();
    
            if (StringChecker.checkString(enjoyString)) {
                double enjoyD = Double.parseDouble(enjoyString);
                setStars(enjoyD, holder.imageStar1, holder.imageStar2, holder.imageStar3, holder.imageStar4, holder.imageStar5);
            }
        }
        holder.textViewFinishPlace.setText(arrayList.get(position).getFinishPlace() + "");
        holder.textViewAgeGroupFinishPlace.setText(arrayList.get(position).getFinishGroupPlace() + "");
        
        if ((!arrayList.get(position).getHour().equalsIgnoreCase("0"))
                || (!arrayList.get(position).getMinute().equalsIgnoreCase("0"))
                || (!arrayList.get(position).getSecond().equalsIgnoreCase("0"))) {
            
            holder.textViewFinishTime.setText(arrayList.get(position).getHour() + ":" +
                    arrayList.get(position).getMinute() + ":" +
                    arrayList.get(position).getSecond());
        }
        
        
        holder.textViewRaceDate.setText(DateFormatter.getFormattedDateTimeFromDateTimeStringAmPm(arrayList.get(position).getDate()));
        
        
        holder.cardViewMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((RaceFragment) fragment).RaceDetailClicked(arrayList.get(position));
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
            
        } else {
            cardView.setCardBackgroundColor(context.getResources().getColor(R.color.white_opac_thirty));
        }
    }
    
    public void setStars(double stars, ImageView m1, ImageView m2, ImageView m3, ImageView m4, ImageView m5) {
        if (stars > 0 && stars < 2) {
            m1.setImageResource(R.drawable.filled_star);
            m2.setImageResource(R.drawable.unfilled_star);
            m3.setImageResource(R.drawable.unfilled_star);
            m4.setImageResource(R.drawable.unfilled_star);
            m5.setImageResource(R.drawable.unfilled_star);
        } else if (stars > 1 && stars < 3) {
            m1.setImageResource(R.drawable.filled_star);
            m2.setImageResource(R.drawable.filled_star);
            m3.setImageResource(R.drawable.unfilled_star);
            m4.setImageResource(R.drawable.unfilled_star);
            m5.setImageResource(R.drawable.unfilled_star);
        } else if (stars > 2 && stars < 4) {
            m1.setImageResource(R.drawable.filled_star);
            m2.setImageResource(R.drawable.filled_star);
            m3.setImageResource(R.drawable.filled_star);
            m4.setImageResource(R.drawable.unfilled_star);
            m5.setImageResource(R.drawable.unfilled_star);
        } else if (stars > 3 && stars < 5) {
            m1.setImageResource(R.drawable.filled_star);
            m2.setImageResource(R.drawable.filled_star);
            m3.setImageResource(R.drawable.filled_star);
            m4.setImageResource(R.drawable.filled_star);
            m5.setImageResource(R.drawable.unfilled_star);
        } else if (stars > 4 && stars < 6) {
            m1.setImageResource(R.drawable.filled_star);
            m2.setImageResource(R.drawable.filled_star);
            m3.setImageResource(R.drawable.filled_star);
            m4.setImageResource(R.drawable.filled_star);
            m5.setImageResource(R.drawable.filled_star);
        } else {
            m1.setImageResource(R.drawable.unfilled_star);
            m2.setImageResource(R.drawable.unfilled_star);
            m3.setImageResource(R.drawable.unfilled_star);
            m4.setImageResource(R.drawable.unfilled_star);
            m5.setImageResource(R.drawable.unfilled_star);
        }
        
    }
    
    
}

