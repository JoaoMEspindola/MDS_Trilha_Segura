package com.example.trilhasegura;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class PinActivity extends AppCompatActivity {

    RadioButton animal, buraco, mataburro, escorregadia;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    PIN pin = new PIN();

    Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        Bundle bundle = getIntent().getExtras();

        pin.setLongitude(bundle.getDouble("longitude"));
        pin.setLatitude(bundle.getDouble("latitude"));

        animal = (RadioButton) findViewById(R.id.rdbAnimal);
        buraco = (RadioButton) findViewById(R.id.rdbBuraco);
        mataburro = (RadioButton) findViewById(R.id.rdbMB);
        escorregadia = (RadioButton) findViewById(R.id.rdbEscorregadia);

        btn = (Button) findViewById(R.id.confirmButton);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Location");

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PinActivity.this, MapActivity.class);
                if (animal.isChecked()) {
                    pin.setType("animal");
                }
                else if (buraco.isChecked()) {
                    pin.setType("buraco");
                }
                else if (mataburro.isChecked()) {
                    pin.setType("mataburro");
                }
                else if (escorregadia.isChecked()) {
                    pin.setType("escorregadia");
                }
                databaseReference.push().setValue(pin);
                intent.putExtras(bundle);
                startActivity(intent);
                finish();
            }
        });
    }

}