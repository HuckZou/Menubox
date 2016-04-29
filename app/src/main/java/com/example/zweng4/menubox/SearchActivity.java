package com.example.zweng4.menubox;

import android.app.ListActivity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

public class SearchActivity extends AppCompatActivity {
    private String restaurants[] = {"Cravings","Dominos Pizza","Mia Zas","Kofusion","Sitara"};

    ListView listView;

    private List<Restaurant> restaurantsWithImage = RestaurantDataProvider.restaurantList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        listView = (ListView) findViewById(R.id.list);
        Bundle extras = getIntent().getExtras();
        if (extras != null ) {
            String query_content = extras.getString("query_content");
            //call query function directly.
            if(query_content != null)
                doMySearch(query_content);
        } else {
            handleIntent(getIntent());
        }
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), RestaurantActivity.class);
                //use the class name, can change the referred name in the manifest but still
                //get to the correct class
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        // Get the SearchView and set the searchable configuration
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        // Assumes current activity is the searchable activity
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(true); // Do not iconify the widget; expand it by default

        return true;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            if(query != null)
                doMySearch(query);
        }
    }

    private void doMySearch(String query){
//        List<String> results = new ArrayList<>();
//        for (int i = 0; i < 5; i ++){
//            if(restaurants[i].toLowerCase().contains(query.toLowerCase())){
//                results.add(restaurants[i]);
//            }
//        }
//        if (results.size() == 0)
//            results.add("No Restaurants Found");

//        String[] finalResults = results.toArray(new String[results.size()]);
//        ArrayAdapter<String> adapter =
//                new ArrayAdapter<>(this, R.layout.search_adapter, finalResults);
//
//        listView.setAdapter(adapter);
        List<Restaurant> results = new ArrayList<>();
        for(Restaurant item: restaurantsWithImage)
        {
            String lowerCaseQuery = query.toLowerCase();
            //if the query matches any of the restaurant information (including name, address, etc.
            //we will add it to our result.
            if(item.getName().toLowerCase().contains(lowerCaseQuery) ||
                item.getAddress().toLowerCase().contains(lowerCaseQuery) ||
                    item.getCuisine().toLowerCase().contains(lowerCaseQuery) ||
                    item.getPrice().toLowerCase().contains(lowerCaseQuery)){

                results.add(item);
            }
        }
        if(results.size() == 0)
        {
            String[] notFound = new String[1];
            notFound[0] = new String("No Restaurants Found");
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.search_adapter, notFound);
        }
        else {
            //Restaurant[] finalResults = results.toArray(new Restaurant[results.size()]);
            RestaurantListAdapter adapter = new RestaurantListAdapter(
                    this, R.layout.list_item, results);
            listView.setAdapter(adapter);
        }
    }

    public void addToFavoriteList(View v){}
    public void addToCompareList(View v){}
}
