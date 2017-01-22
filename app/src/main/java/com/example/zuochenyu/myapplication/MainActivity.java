package com.example.zuochenyu.myapplication;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    public static final double greatGardenLatitude = 51.0380304;
    public static final double greatGardenLongitude = 13.7567629;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        // Use latitude,longitude from GalleryFragment, otherwise use default
        double startLat = intent.getDoubleExtra("startLat", greatGardenLatitude);
        double startLon = intent.getDoubleExtra("startLon", greatGardenLongitude);

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, MapFragment.newInstance(startLat, startLon));
        fragmentTransaction.commit();

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment = null;
                switch (item.getItemId()) {
                    case R.id.action_map:
                        fragment = MapFragment.newInstance(greatGardenLatitude, greatGardenLongitude);
                        //System.out.println("here is main activity, map item selected");
                        break;
                    case R.id.action_gallery:
                        fragment = GalleryFragment.newInstance();
                        //System.out.println("here is main activity, gallery item selected");
                        break;
                    case R.id.action_about:
                        fragment = AboutFragment.newInstance();
                        //System.out.println("here is main activity, about item selected");
                        break;
                }
                if (fragment != null) {
                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.frameLayout, fragment);
                    fragmentTransaction.commit();
                }
                return true;
            }
        });
    }


}
