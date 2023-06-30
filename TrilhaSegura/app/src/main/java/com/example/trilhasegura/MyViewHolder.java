package com.example.trilhasegura;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MyViewHolder extends RecyclerView.ViewHolder {

    TextView keyView;


    public MyViewHolder(@NonNull View itemView) {
        super(itemView);
        keyView = itemView.findViewById(R.id.key);

    }
}
