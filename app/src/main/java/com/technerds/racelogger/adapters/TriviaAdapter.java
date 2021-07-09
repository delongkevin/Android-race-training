package com.technerds.racelogger.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.technerds.racelogger.R;

import java.util.List;

public class TriviaAdapter extends RecyclerView.Adapter<TriviaAdapter.MyViewHolder> {

    private List<String> arrayList;
    Context context;
    Fragment fragment;

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textViewMain;


        private MyViewHolder(View view) {
            super(view);
    
            textViewMain = view.findViewById(R.id.textViewMain);
        }

    }

    public TriviaAdapter(Context context, List<String> arrayList, Fragment fragment) {
        this.context = context;
        this.arrayList = arrayList;
        this.fragment = fragment;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_trivia, parent, false);
        return new MyViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

       holder.textViewMain.setText(arrayList.get(position));
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

