package com.example.zweng4.menubox;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

public class ComparisonActivity extends AppCompatActivity {
    private String[] mobileArray = {" "," "," "," "," "," "," "," "};
    private EditText compare_txt;
    private ImageButton search_btn;
    private SQLiteDatabase sqLiteDatabase;

    private ListView listView;
    ArrayAdapter adapter;

    private String compare;
    private Cursor query;
    private int i;
    private int max;
    private String name;
    private String location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comparison);

        adapter = new ArrayAdapter<String>(this, R.layout.activity_listview, mobileArray);
        compare_txt = (EditText)findViewById(R.id.compare_txt);
        search_btn = (ImageButton)findViewById(R.id.search_btn);
        sqLiteDatabase = getBaseContext().openOrCreateDatabase("sqlite-test-1.db", MODE_PRIVATE, null);
        //factory optional to instantiate a cursor when query is called.

        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS restaurants(name TEXT, location TEXT)");
        sqLiteDatabase.execSQL("DROP TABLE restaurants");
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS restaurants(name TEXT, location TEXT)");
        sqLiteDatabase.execSQL("INSERT INTO restaurants VALUES('Cravings', '603 S Wright St');");

        sqLiteDatabase.execSQL("INSERT INTO restaurants VALUES('Dominos Piazza','1108 N Cunningham Ave');");
        sqLiteDatabase.execSQL("INSERT INTO restaurants VALUES('Kofusion','701 S Gregory St');");
        sqLiteDatabase.execSQL("INSERT INTO restaurants VALUES('Sitara','114 S Race St');");
        sqLiteDatabase.close();

        search_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                compare = compare_txt.getText().toString();
                max = 0;
                sqLiteDatabase = getBaseContext().openOrCreateDatabase("sqlite-test-1.db", MODE_PRIVATE, null);
                query = sqLiteDatabase.rawQuery("SELECT * FROM restaurants WHERE name = '" + compare + "'" , null);
                if (query.moveToFirst()) {
                    do  {//cursor is empty if there is no data, else move to the first data
                        max ++;

                    } while (query.moveToNext());
                }
                else Toast.makeText(getBaseContext(), "No restaurant match", Toast.LENGTH_LONG).show();
                sqLiteDatabase.close(); //count number of selected tuples.

                sqLiteDatabase = getBaseContext().openOrCreateDatabase("sqlite-test-1.db", MODE_PRIVATE, null);
                query = sqLiteDatabase.rawQuery("SELECT * FROM restaurants WHERE name = '" + compare + "'", null);//where is filled in to the second argument
                i = 0;

                if (query.moveToFirst()) {
                    do {//cursor is empty if there is no data, else move to the first data
                        //cycle through all records.
                        name = query.getString(0);//field number
                        location = query.getString(1);


                        mobileArray[i] = name + "   " + location;
                        i++;

                    } while (query.moveToNext() && i < max);


                } else {
                    Toast.makeText(getBaseContext(), "error retrieving data", Toast.LENGTH_LONG).show();

                }
                sqLiteDatabase.close();



                listView = (ListView) findViewById(R.id.xmlListView);
                listView.setAdapter(adapter);

            }
        });





    }
}
