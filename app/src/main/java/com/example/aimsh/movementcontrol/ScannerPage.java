package com.example.aimsh.movementcontrol;

import android.content.Context;
import android.content.Intent;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ScannerPage.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class ScannerPage extends Fragment {

    private OnFragmentInteractionListener mListener;

    private String myChosenLocation = "Default";
    public ScannerPage() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_scanner_page, container, false);


        RadioGroup radioGroup = (RadioGroup) v.findViewById(R.id.radiogroupID);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioButton = (RadioButton) v.findViewById(checkedId);
                // checkedId is the RadioButton selected
                myChosenLocation = radioButton.getText().toString();
                mListener.setLocation(myChosenLocation);
                //mListener.setLocation(myChosenLocation);
                Toast.makeText(getActivity(), "Scanned: " + myChosenLocation, Toast.LENGTH_LONG).show();
            }
        });


        Button carScan = (Button) v.findViewById(R.id.carID);
        carScan.setOnClickListener(view->mListener.scanResult(view, myChosenLocation));

        Button badgeScan = (Button) v.findViewById(R.id.badgeID);
        badgeScan.setOnClickListener(view->mListener.scanResult(view, myChosenLocation));

        Button monitor = (Button) v.findViewById(R.id.monitorID);
        monitor.setOnClickListener(view->mListener.startMonitor());

        Button carHistory = (Button) v.findViewById(R.id.car_historyID);
        carHistory.setOnClickListener(view->mListener.startCarHistory());

        Button scan = (Button) v.findViewById(R.id.scan_id);
        scan.setOnClickListener(view -> mListener.startScanning());

        Button badgeHistory = (Button) v.findViewById(R.id.badge_historyID);
        badgeHistory.setOnClickListener(view->mListener.startBadgeHistory());


        return v;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
//        void setLocation(String location);
//        void scanBarcode(View v);
        void scanResult(View v, String Location);
        void startMonitor();
        void startCarHistory();
        void startScanning();
        void startBadgeHistory();
        void setLocation(String loc);
    }
}
