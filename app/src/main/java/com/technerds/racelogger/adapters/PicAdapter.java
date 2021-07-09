package com.technerds.racelogger.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.technerds.racelogger.Utils.Urls;
import com.technerds.racelogger.dataModels.PicModel;
import com.technerds.racelogger.R;
import com.technerds.racelogger.listeners.MyAdapterListener;

import java.util.List;

public class PicAdapter extends RecyclerView.Adapter<PicAdapter.MyViewHolder> {

    private List<PicModel> arrayList;
    Context context;
    MyAdapterListener onClickListener;

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textViewName;
        ImageView imageViewPic;
        ImageView imageViewDelete;


        private MyViewHolder(View view) {
            super(view);

            imageViewPic = (ImageView) view.findViewById(R.id.imageViewPic);
            imageViewDelete = (ImageView) view.findViewById(R.id.imageViewDelete);

            imageViewDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onClickListener.viewAllClick(imageViewDelete, getAdapterPosition());
                }
            });

        }

    }

    public PicAdapter(Context context, List<PicModel> arrayList, MyAdapterListener listener) {
        this.context = context;
        this.arrayList = arrayList;
        onClickListener = listener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.pic_layout, parent, false);
        return new MyViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        //  holder.imageViewPic.setImageURI(model.getURI());
        Log.wtf("-picAdapter", " URL : " + arrayList.get(position).getURL());

        if(isEmpty(arrayList.get(position).getURL())){
            if(!isEmpty(arrayList.get(position).getName())){
                Glide.with(context).load(Urls.imageBaseUrl + "" +arrayList.get(position).getName()).into(holder.imageViewPic);
            }
        }else {
            Glide.with(context).load(arrayList.get(position).getURL()).into(holder.imageViewPic);
        }



       /* holder.imageViewPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, String.valueOf(arrayList.get(position).getURL())+"\n"+arrayList.get(position).getName(), Toast.LENGTH_SHORT).show();
            }
        });*/
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

    public static boolean isEmpty(CharSequence str) {
        if (str == null || str.length() == 0)
            return true;
        else
            return false;
    }
}

