package com.example.zuochenyu.myapplication;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GalleryDetailsActivity extends AppCompatActivity {
    SQLiteDatabase database = null;
    Cursor dbCursor;
    DatabaseHelper dbHelper;

    private String title;
    private String desc;
    private String imgPath;
    private double lat;
    private double lon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Remove title bar
        //requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_gallery_details);

        dbHelper = new DatabaseHelper(this);

        Intent intent = getIntent();
        int pos = intent.getIntExtra("rowid", 0);
        getRowFromDatabase(pos);

        TextView textview1 = (TextView) findViewById(R.id.details_title);
        System.out.println(title);
        textview1.setText(title);

        TextView textview2 = (TextView) findViewById(R.id.details_description);
        textview2.setText(desc);

        ImageView imageview1 = (ImageView)  findViewById(R.id.details_image);
        int imageid = getResources().getIdentifier("drawable/" + imgPath, null, getPackageName());

        imageview1.setImageResource(imageid);
    }

    public void onClickStartGalleryFragment(View view) {
        finish();
    }

    public void onClickShowMapFragment (View view) {
        /*System.out.println("im here");
        WebView mWebView = (WebView) findViewById(R.id.webview);


        mWebView.loadUrl("javascript:setCenter(48.1,11.58);");
*/

        Intent intent = new Intent(this,  MainActivity.class);
        intent.putExtra("startLat", lat );
        intent.putExtra("startLon",lon);
        startActivity(intent);

    }

    public void getRowFromDatabase(int row) {
        try {
            database = dbHelper.getDataBase();
            String dbTable = getString(R.string.db_table_name);
            String query = "SELECT * FROM " + dbTable + " where id=" + Integer.toString(row+1)+ ";";
            //System.out.println(query);

            dbCursor = database.rawQuery(query, null);

            if(dbCursor.getCount() > 0) {

                dbCursor.moveToFirst();

                title = dbCursor.getString(  dbCursor.getColumnIndex("name"));
                desc = dbCursor.getString(  dbCursor.getColumnIndex("desc"));
                imgPath = dbCursor.getString(dbCursor.getColumnIndex("photoLargeURI"));
                lat = dbCursor.getDouble(dbCursor.getColumnIndex("latitude"));
                lon = dbCursor.getDouble(dbCursor.getColumnIndex("longitude"));

                //System.out.println(title);
                //  System.out.println(desc);
                //  System.out.println(imgPath);
            }

        } finally {
            dbHelper.close();
        }

    }

}