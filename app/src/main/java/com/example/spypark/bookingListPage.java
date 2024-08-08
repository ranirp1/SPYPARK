package com.example.spypark;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SearchEvent;
import android.view.View;
import android.widget.Adapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.SearchView.OnQueryTextListener;

import java.util.ArrayList;

public class bookingListPage extends AppCompatActivity {

    ListView listView;
    listViewAdapter adapter;
    String[] title;
    String[] description;
    int[] icon;
    ArrayList<Model> arrayList = new ArrayList<Model>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_list_page);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Parking areas");

        title = new String[]{"Phoenix", "Orion"};
        description = new String[]{"Address", "Address"};
        icon = new int[]{R.drawable.spypark_logo, R.drawable.spypark_logo};

        listView= findViewById(R.id.bookList);

        for (int i =0; i<title.length; i++){
            Model model = new Model(title[i], description[i],icon[i]);
            arrayList.add(model);

        }
        adapter = new listViewAdapter(this, arrayList);
        listView.setAdapter(adapter);
    }

    public boolean onCreateOptionsMenu(Menu menu){

        getMenuInflater().inflate(R.menu.menu, menu);

        MenuItem myActionMenuItem = menu.findItem(R.id.actionSearch);
        SearchView searchView = (SearchView)myActionMenuItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                if(TextUtils.isEmpty(s)){
                    adapter.filter("");
                    listView.clearTextFilter();
                }
                else{

                    adapter.filter(s);
                }

                return true;
            }
        });

        return true;
    }

}