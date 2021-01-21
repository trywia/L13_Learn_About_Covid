package edu.ib.zpo_l13;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class CovidStats extends AppCompatActivity {

    SQLiteDatabase database;
    int[] columnIndices = new int[5];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_covid_stats);

        final TextView sumOfCases = (TextView) findViewById(R.id.tvSum);
        final TextView highestCuredPercent = (TextView) findViewById(R.id.tvCured);
        final TextView testsPerMillion = (TextView) findViewById(R.id.tvTests);

        database = openOrCreateDatabase("COUNTRIES", MODE_PRIVATE, null);
        String sqlDB = "CREATE TABLE IF NOT EXISTS COUNTRIES (Country VARCHAR, Cases INTEGER, " +
                "Active INTEGER, CasesPerMillion Integer, TestsPerOneMillion INTEGER)";
        database.execSQL(sqlDB);

        // sum of cases
        String selectQuery1 = "SELECT SUM(Cases) FROM COUNTRIES";
        Cursor c1 = database.rawQuery(selectQuery1, null);

        if (c1.moveToFirst()) { // przejście do pierwszego wiersza zapytania i zwraca wartość true,
            // jeśli jest kolejna pozycja, jeśli nie ma, kończy wyświetlanie
            do {
                sumOfCases.setText("SUM OF CASES: " + c1.getString(c1.getColumnIndex("SUM(Cases)")));
            } while (c1.moveToNext());
        }
        c1.close();

        // highest cured percent
        String selectQuery2 = "SELECT Country FROM COUNTRIES ORDER BY ((Cases - Active) / Cases) DESC LIMIT 1";
        Cursor c2 = database.rawQuery(selectQuery2, null);
        if (c2.moveToFirst()) { // przejście do pierwszego wiersza zapytania i zwraca wartość true,
            // jeśli jest kolejna pozycja, jeśli nie ma, kończy wyświetlanie
            do {
                highestCuredPercent.setText("HIGHEST CURED PERCENT IN: " + c2.getString(c2.getColumnIndex("Country")));
            } while (c2.moveToNext());
        }
        c2.close();

        ArrayList countries = new ArrayList();
        String result = "";

        // tests per one million
        String selectQuery3 = "SELECT Country FROM COUNTRIES ORDER BY TestsPerOneMillion DESC";
        Cursor c3 = database.rawQuery(selectQuery3, null);
        if (c3.moveToFirst()) { // przejście do pierwszego wiersza zapytania i zwraca wartość true,
            // jeśli jest kolejna pozycja, jeśli nie ma, kończy wyświetlanie
            do {
                countries.add(c3.getString(c3.getColumnIndex("Country")));
            } while (c3.moveToNext());

            if (countries.size() > 1) { // jeśli coś jeszcze jest w bazie
                for (int i = 0; i < countries.size() - 1; i++) {
                    result += countries.get(i) + ", ";
                }
            }

            result += countries.get(countries.size() - 1);
            testsPerMillion.setText("TESTS PER MILION (DESCENDING): " + result);
        }
        c3.close();

    }

    public void obBtnReturnClick(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}