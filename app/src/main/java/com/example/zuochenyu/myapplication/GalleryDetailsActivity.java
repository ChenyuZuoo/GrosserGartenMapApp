package com.example.zuochenyu.myapplication;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

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
        setContentView(R.layout.activity_gallery_details);

        dbHelper = new DatabaseHelper(this);

        /* Figure out which Gallery Item details is required */
        Intent intent = getIntent();
        int pos = intent.getIntExtra("rowid", 0);

        /* Get the details for this row from the database */
        getRowFromDatabase(pos);

        /* Populate the view */
        TextView textview1 = (TextView) findViewById(R.id.details_title);
        System.out.println(title);
        textview1.setText(title);

        TextView textview2 = (TextView) findViewById(R.id.details_description);
        textview2.setText(desc);

        ImageView imageview1 = (ImageView)  findViewById(R.id.details_image);
        int imageid = getResources().getIdentifier("drawable/" + imgPath, null, getPackageName());

        imageview1.setImageResource(imageid);
    }

    /* Back button action */
    public void onClickStartGalleryFragment(View view) {
        finish();
    }

    /* Show map action */
    public void onClickShowMapFragment (View view) {

        /* Start the main activity again. This will load the MapFragment by default and
        use the gallery item's latitude, longitude to center the leaflet map
         */

        Intent intent = new Intent(this,  MainActivity.class);
        intent.putExtra("startLat", lat );
        intent.putExtra("startLon",lon);
        startActivity(intent);

    }

    /* Helper function to read details of a particular rowid from the database */
    public void getRowFromDatabase(int row) {
        try {
            database = dbHelper.getDataBase();
            String dbTable = getString(R.string.db_table_name);

            String query = "SELECT * FROM " + dbTable + " where id=" + Integer.toString(row+1)+ ";";
            //System.out.println(query);

            dbCursor = database.rawQuery(query, null);

            if(dbCursor.getCount() > 0) {

                dbCursor.moveToFirst();

                title = dbCursor.getString(  dbCursor.getColumnIndex(getString(R.string.db_col_name)));
                desc = dbCursor.getString(  dbCursor.getColumnIndex(getString(R.string.db_col_desc)));

                imgPath = dbCursor.getString(dbCursor.getColumnIndex("photoLargeURI"));
                lat = dbCursor.getDouble(dbCursor.getColumnIndex("latitude"));
                lon = dbCursor.getDouble(dbCursor.getColumnIndex("longitude"));
            }

        } finally {
            dbHelper.close();
        }

    }

}