package com.example.aimsh.movementcontrol;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import static android.content.ContentValues.TAG;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Monitor.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class Monitor extends Fragment {

    private OnFragmentInteractionListener mListener;

    DateFormat store =  new SimpleDateFormat("MM-dd-yyyy");
    String storeDate = store.format(Calendar.getInstance().getTime());

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef = database.getReference(storeDate);

    private ArrayList<String> mTexts = new ArrayList<>();

    public Monitor() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_monitor, container, false);

//        mTexts.add("afjblasdf");
//        mTexts.add("asgasdg");
//        mTexts.add("asgdasdgj");

        RecyclerView recyclerView =  v.findViewById(R.id.recycler_view);
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(mTexts, getContext());

        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(getContext());
        mLinearLayoutManager.setStackFromEnd(true);
        mLinearLayoutManager.setReverseLayout(true);
        recyclerView.setLayoutManager(mLinearLayoutManager);


        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
//                StringBuilder sb = new StringBuilder();
//
                //MovementData value =  dataSnapshot.getValue(MovementData.class);
                //mTexts.add(value.getMyBadge());

                for (DataSnapshot eventSnapshot : dataSnapshot.getChildren()) {
                    MovementData mModel = eventSnapshot.getValue(MovementData.class);
                    mTexts.add(mModel.toString());
                    recyclerView.setAdapter(adapter);

                    Log.d(TAG, "DATA FROM FIREBASE" + mModel.getMyBadge());
                }

//                    Log.d(TAG, "Value from Firebase is: " + value.getMyBadge());
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });


//        mListener.monitoring();


        return v;
    }



//    // TODO: Rename method, update argument and hook method into UI event
//    public void onButtonPressed(Uri uri) {
//        if (mListener != null) {
//            mListener.onFragmentInteraction(uri);
//        }
//    }

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
        void monitoring();
    }
}
