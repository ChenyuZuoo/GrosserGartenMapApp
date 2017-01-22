package com.example.zuochenyu.myapplication;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/* This fragment shows an overview of the different places of interest */
public class GalleryFragment extends ListFragment {

    SQLiteDatabase database = null;
    Cursor dbCursor;
    DatabaseHelper dbHelper;

    private OnFragmentInteractionListener listener;
    private SimpleCursorAdapter mListAdapter;
    private ListView mListView;

    public static GalleryFragment newInstance() {
        return new GalleryFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        dbHelper = new DatabaseHelper( getActivity().getApplicationContext());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        queryDataFromDatabase();
    }

    /* Get details from the database */
    public void queryDataFromDatabase() {
        try {
            dbHelper.createDataBase();
        } catch (IOException ioe) {
        }
        List<String> list_values = new ArrayList<String>();
        try {
            database = dbHelper.getDataBase();
            String dbTable = getString(R.string.db_table_name);
            dbCursor = database.rawQuery("SELECT rowid _id, * FROM " + dbTable + ";" , null);

        } finally {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_gallery, container, false);
    }

    public static int getImageId(Context context, String imageName) {
        return context.getResources().getIdentifier("drawable/" + imageName, null, context.getPackageName());
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        // Map the database name/name_de to TextView
        // Map the database imageURI to an ImageView
        mListAdapter = new SimpleCursorAdapter(
                getActivity().getApplicationContext(),
                R.layout.list_item,
                dbCursor,
                new String[] { getString(R.string.db_col_name), "photoSmallURI" },
                new int[] { R.id.ele1, R.id.smallImage }, 0
        );

        mListAdapter.setViewBinder(new SimpleCursorAdapter.ViewBinder(){
            /** Binds the Cursor column defined by the specified index to the specified view */
            public boolean setViewValue(View view, Cursor cursor, int columnIndex) {

                if (view.getId() == R.id.smallImage) {
                    ImageView IV = (ImageView) view;
                    String image = cursor.getString(columnIndex);
                    IV.setImageResource(getImageId(getActivity().getApplicationContext(), image));
                    return true;
                }
                return false;
            }
        });

        mListView = getListView();
        mListView.setAdapter(mListAdapter);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
        dbHelper.close();
    }

    public interface OnFragmentInteractionListener {
        public void onGalleryFragmentInteraction(String string);
    }

    @Override
    public void onListItemClick(ListView l, View v, int pos, long id) {
        /* Start the activity which shows the details, passing the rowid as a paramter */
        Intent intent = new Intent(getActivity(),  GalleryDetailsActivity.class);
        intent.putExtra("rowid", pos);
        startActivity(intent);

    }

}