package com.example.jsonparsing;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import java.util.List;


import androidx.recyclerview.widget.RecyclerView;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder> {

    private List<Dao> mList;
    private  MainActivity mainActivity;


    public RecyclerAdapter(MainActivity mainActivity, List<Dao> mList) {
        this.mList=mList;
        this.mainActivity=mainActivity;

    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        ImageView image;

        public MyViewHolder(View view) {
            super(view);
            title =  view.findViewById(R.id.text);
            image =  view.findViewById(R.id.imag);

        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.subject_list_vrow, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Dao list = mList.get(position);
        holder.title.setText(list.getName());

        Picasso.get().load(String.valueOf(list.getImage())).into(holder.image);



    }

    @Override
    public int getItemCount() {
        return mList.size();
    }
}