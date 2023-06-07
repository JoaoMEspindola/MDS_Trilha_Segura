package com.example.trilhasegura;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;

public class PinActivity extends AppCompatActivity {

    RadioButton animal, buraco, mataburro, escorregadia;
    Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);


        Bundle bundle = getIntent().getExtras();

        animal = (RadioButton) findViewById(R.id.rdbAnimal);
        buraco = (RadioButton) findViewById(R.id.rdbBuraco);
        mataburro = (RadioButton) findViewById(R.id.rdbMB);
        escorregadia = (RadioButton) findViewById(R.id.rdbEscorregadia);

        btn = (Button) findViewById(R.id.confirmButton);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (animal.isChecked()) {
                    Intent intent = new Intent(PinActivity.this, MapActivity.class);
                    intent.putExtra("TYPE", "animal");
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
                else if (buraco.isChecked()) {
                    Intent intent = new Intent(PinActivity.this, MapActivity.class);
                    intent.putExtra("TYPE", "buraco");
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
                else if (mataburro.isChecked()) {
                    Intent intent = new Intent(PinActivity.this, MapActivity.class);
                    intent.putExtra("TYPE", "mataburro");
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
                else if (escorregadia.isChecked()) {
                    Intent intent = new Intent(PinActivity.this, MapActivity.class);
                    intent.putExtra("TYPE", "escorregadia");
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            }
        });
    }
}