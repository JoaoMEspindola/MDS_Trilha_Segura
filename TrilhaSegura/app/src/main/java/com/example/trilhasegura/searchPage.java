package com.example.trilhasegura;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class searchPage extends AppCompatActivity {
    private RecyclerView trailsRecyclerView;
    private DatabaseReference databaseRef;
    private FirebaseRecyclerAdapter<Trail, TrailViewHolder> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_page);

        trailsRecyclerView = findViewById(R.id.trailsRecyclerView);

        // Configurar o layout manager para a RecyclerView
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        trailsRecyclerView.setLayoutManager(layoutManager);

        // Configurar o adaptador do FirebaseRecyclerAdapter
        databaseRef = FirebaseDatabase.getInstance().getReference().child("Trails");
        FirebaseRecyclerOptions<Trail> options =
                new FirebaseRecyclerOptions.Builder<Trail>()
                        .setQuery(databaseRef, Trail.class)
                        .build();

        adapter = new FirebaseRecyclerAdapter<Trail, TrailViewHolder>(options) {
            @NonNull
            @Override
            public TrailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.trail_item_layout, parent, false);
                return new TrailViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull TrailViewHolder holder, int position, @NonNull Trail model) {
                holder.bind(model);
            }
        };

        trailsRecyclerView.setAdapter(adapter);
    }
    private static class TrailViewHolder extends RecyclerView.ViewHolder {
        private TextView nameTextView;

        public TrailViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
        }

        public void bind(Trail trail) {
            nameTextView.setText(trail.getName());
        }
    }
    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

}