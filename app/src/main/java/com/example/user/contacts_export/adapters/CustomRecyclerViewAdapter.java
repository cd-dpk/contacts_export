package com.example.user.contacts_export.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.user.contacts_export.interfaces.OnRecyclerViewItemListener;


/**
 * Created by chandradasdipok on 5/30/2016.
 */
public class CustomRecyclerViewAdapter extends RecyclerView.Adapter<CustomRecyclerViewAdapter.MyViewHolder> {

    OnRecyclerViewItemListener onRecyclerViewItemListener;
    int size;
    int resId;

    public CustomRecyclerViewAdapter(OnRecyclerViewItemListener onRecyclerViewItemListener, int size, int resId) {
        this.onRecyclerViewItemListener = onRecyclerViewItemListener;
        this.size = size;
        this.resId = resId;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(resId,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        onRecyclerViewItemListener.listenRecyclerViewItem(holder.myView, position);
    }

    @Override
    public int getItemCount() {
        return size;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        View myView;
        public MyViewHolder(View itemView) {
            super(itemView);
            this.myView = itemView;
        }
    }
}
