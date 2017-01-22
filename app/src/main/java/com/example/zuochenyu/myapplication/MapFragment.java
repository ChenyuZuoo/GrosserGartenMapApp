package com.example.zuochenyu.myapplication;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

/* This fragment renders the map */
public class MapFragment extends Fragment  implements LocationListener {
    private double lat;
    private double lon;
    public static   boolean changePos = false;

    LocationManager locationManager;
    final String[] LOCATION_PERMS = {Manifest.permission.ACCESS_FINE_LOCATION};
    final int LOCATION_REQUEST = 1340;

    private OnFragmentInteractionListener listener;

    public static  MapFragment newInstance(double latitude, double longitude) {

        /* Obtain the latitude,longitude to pan the map to*/
        MapFragment m  = new MapFragment();
        Bundle bundle = new Bundle(2);
        bundle.putDouble("mapLat", latitude);
        bundle.putDouble("mapLon",longitude);
        m.setArguments(bundle);
        return m;
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        lat = getArguments().getDouble("mapLat");
        lon = getArguments().getDouble("mapLon");
        setHasOptionsMenu(true);

        locationManager =
                (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Using webtext in fragment
        View v=inflater.inflate(R.layout.fragment_map, container, false);
        final WebView mWebView = (WebView) v.findViewById(R.id.webview);
        mWebView.loadUrl("file:///android_asset/map.html");

        // Enable Javascript
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        // Force links and redirects to open in the WebView instead of in a browser
        mWebView.setWebViewClient(new WebViewClient(){
            public void onPageFinished(WebView view, String url){
                mWebView.loadUrl("javascript:setCenter("
                        + Double.toString(lat) + "," + Double.toString(lon) + ");");
            }
        });

        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    public interface OnFragmentInteractionListener {
        public void onMapFragmentInteraction(String string);
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_button_new, menu);

        super.onCreateOptionsMenu(menu, inflater);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        Locale locale = null;
        switch (item.getItemId()) {
            case R.id.gpsMenu:
System.out.println("gpsMenu clicked");
                if (ContextCompat.checkSelfPermission(getActivity(),Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED) {

                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                            1000, 5, this);

                } else {
                    requestPermissions(LOCATION_PERMS, LOCATION_REQUEST);
                }


                break;

        }
        return super.onOptionsItemSelected(item);
    }

    public void onLocationChanged(Location location) {

        if (location != null) {
            Log.v("Location Changed", location.getLatitude() + " and " + location.getLongitude());

            /* Remove further location updates for the time being */
            if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                locationManager.removeUpdates(this);
            }

            Intent intent = new Intent(getActivity(), MainActivity.class);
            intent.putExtra("startLat", location.getLatitude() );
            intent.putExtra("startLon",location.getLongitude() );
            startActivity(intent);

        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {

        switch (requestCode) {

            case LOCATION_REQUEST:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED
                        && ContextCompat.checkSelfPermission(getActivity(),Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED) {

                    locationManager.requestLocationUpdates(
                            LocationManager.GPS_PROVIDER, 1000, 5, this);
                }
                else {
                    Toast.makeText(getContext(), "Location cannot be obtained due to "
                            + "missing permission.", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    public void onProviderDisabled(String provider) {
    }

    public void onProviderEnabled(String provider) {
    }

}