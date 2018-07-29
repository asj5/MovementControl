package com.example.aimsh.movementcontrol;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>{

    private static final String TAG = "RecyclerViewAdapter";

    private ArrayList<String> mDisplay;
    private Context mContext;

    public RecyclerViewAdapter(ArrayList<String> mDisplay, Context mContext) {
        this.mDisplay = mDisplay;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_listitem, parent, false);
        ViewHolder viewHolder =  new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.d(TAG, "onBindviewHolder: Called");
        holder.display.setText(mDisplay.get(position));

        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, mDisplay.get(position), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDisplay.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView display;
        RelativeLayout parentLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            display = itemView.findViewById(R.id.text_id);
            parentLayout = itemView.findViewById(R.id.parent_layout);
        }
    }
}
