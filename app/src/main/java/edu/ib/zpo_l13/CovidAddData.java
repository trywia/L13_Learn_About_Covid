package edu.ib.zpo_l13;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.xml.transform.Result;

public class CovidAddData extends AppCompatActivity {

    SQLiteDatabase database;
    int[] columnIndices = new int[5];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_covid_add_data);
    }

    public void onBtnAddDataClick(View view) {
        final EditText country = (EditText) findViewById(R.id.edtCountry);
        String url = "https://coronavirus-19-api.herokuapp.com/countries/" + country.getText().toString();

        database = openOrCreateDatabase("COUNTRIES", MODE_PRIVATE, null);
        String sqlDB = "CREATE TABLE IF NOT EXISTS COUNTRIES (Country VARCHAR PRIMARY KEY, Cases INTEGER, " +
                "Active INTEGER, CasesPerMillion Integer, TestsPerOneMillion INTEGER)";
        database.execSQL(sqlDB);

        String sqlCount = "SELECT count(*) FROM COUNTRIES"; // liczba wierszy w tabeli
        Cursor c = database.rawQuery(sqlCount, null);
        // Za pomocą kursora możemy otrzymać dane z bazy. Po wykonaniu metody rawQuery() dla
        // zapytania sqlCount zwracany jest kursor do wyników
        // Musimy ustawić kursor na pierwszym wierszu wyniku zapytania, gdyż zwracany jest jeden
        // wiersz i jedna kolumna. Dlatego zwracamy wartość z kolumny o indeksie zero. Na końcu
        // należy pamiętać o zamknięciu kursora i zwolnieniu zasobów
        c.moveToFirst();
        c.close();

        RequestQueue queue = Volley.newRequestQueue(this); // kolejka żądań
        Gson gson = new GsonBuilder().setPrettyPrinting().create(); // import Gson'a
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, response ->
        {
            COVIDData dataToAdd = gson.fromJson(response, COVIDData.class); // mapowanie odpowiedzi do klasy COVIDData
                // dodawanie nowych rekordów
                // parametry indeksowane od 1, nie od 0
                String sqlCountry = "INSERT INTO COUNTRIES VALUES (?,?,?,?,?)";
                SQLiteStatement statement = database.compileStatement(sqlCountry);

                statement.bindString(1, dataToAdd.getCountry());
                statement.bindLong(2, dataToAdd.getCases());
                statement.bindLong(3, dataToAdd.getActive());
                statement.bindLong(4, dataToAdd.getCasesPerOneMillion());
                statement.bindLong(5, dataToAdd.getTestsPerOneMillion());
                statement.executeInsert();

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("Error " + url);
            }
        }
        );

        queue.add(stringRequest); // dodanie do kolejki
    }

    public void obBtnReturnClick(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}