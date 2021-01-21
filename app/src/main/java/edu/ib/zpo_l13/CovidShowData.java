package edu.ib.zpo_l13;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class CovidShowData extends AppCompatActivity {

    SQLiteDatabase database;
    int[] columnIndices = new int[5];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_covid_data);

        database = openOrCreateDatabase("COUNTRIES", MODE_PRIVATE, null);
        String sqlDB = "CREATE TABLE IF NOT EXISTS COUNTRIES (Country VARCHAR, Cases INTEGER, " +
                "Active INTEGER, CasesPerMillion Integer, TestsPerOneMillion INTEGER)";
        database.execSQL(sqlDB);

        // wyświetlenie wszystkich rekordów
        ArrayList<String> results = new ArrayList<>();
        Cursor c = database.rawQuery("SELECT Country, Cases, Active, CasesPerMillion, TestsPerOneMillion FROM COUNTRIES", null);

        if (c.moveToFirst()) { // przejście do pierwszego wiersza zapytania i zwraca wartość true,
            // jeśli jest kolejna pozycja, jeśli nie ma, kończy wyświetlanie
            do {
                String country = c.getString(c.getColumnIndex("Country"));
                int cases = c.getInt(c.getColumnIndex("Cases"));
                int active = c.getInt(c.getColumnIndex("Active"));
                int cpm = c.getInt(c.getColumnIndex("CasesPerMillion"));
                int tpm = c.getInt(c.getColumnIndex("TestsPerOneMillion"));

                results.add("Country: " + country + ", C/A " + cases + "/" + active + ", cPerOneM: " + cpm + ", tPerOneM: " + tpm);

            } while (c.moveToNext());
        }

        ListView listView = (ListView) findViewById(R.id.lvData);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, results);
        listView.setAdapter(adapter);
        c.close();

    }

    public void obBtnReturnClick(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}