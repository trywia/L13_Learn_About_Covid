package edu.ib.zpo_l13;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void obBtnAddDataClick(View view) {
        Intent intent = new Intent(this, CovidAddData.class);
        startActivity(intent);
    }

    public void onBtnShowDataClick(View view) {
        Intent intent = new Intent(this, CovidShowData.class);
        startActivity(intent);
    }

    public void onBtnGetStatsClick(View view) {
        Intent intent = new Intent(this, CovidStats.class);
        startActivity(intent);
    }
}