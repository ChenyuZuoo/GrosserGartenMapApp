package com.example.zuochenyu.myapplication;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class GalleryFragment extends Fragment {

    private OnFragmentInteractionListener listener;
//    TextView textView = (TextView) getView().findViewById(R.id.frag_text);

    public static GalleryFragment newInstance() {
        return new GalleryFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        System.out.println("here is gallery fragment OnCreate() method");
        return inflater.inflate(R.layout.fragment_gallery, container, false);
    }

    // nothing needed to be done with onAttach method right now
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
        public void onGalleryFragmentInteraction(String string);
    }
}