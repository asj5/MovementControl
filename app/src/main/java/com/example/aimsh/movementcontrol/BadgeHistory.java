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
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;

import static android.content.ContentValues.TAG;
import static com.example.aimsh.movementcontrol.MainActivity.carVinNum;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link BadgeHistory.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class BadgeHistory extends Fragment {

    private OnFragmentInteractionListener mListener;

    FirebaseDatabase database = FirebaseDatabase.getInstance();

    DateFormat store =  new SimpleDateFormat("MM-dd-yyyy");
    String storeDate = store.format(Calendar.getInstance().getTime());

    private DatabaseReference myRef = database.getReference(storeDate);

    private ArrayList<String> mTexts = new ArrayList<>();

    private static int totalCars = 1;

    public BadgeHistory() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_badge_history, container, false);

        EditText et = (EditText) v.findViewById(R.id.enter_badgeID);

        Button search = (Button) v.findViewById(R.id.search_badgeID);
        //search.setOnClickListener(view->searchBadgeId(badgeId, v));
        RecyclerView recyclerView =  v.findViewById(R.id.badge_recycler_view);
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(mTexts, getContext());
//        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(getContext());
        mLinearLayoutManager.setStackFromEnd(true);
        mLinearLayoutManager.setReverseLayout(true);
        recyclerView.setLayoutManager(mLinearLayoutManager);

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = et.getText().toString();
                Log.d(TAG, "Badge ID from edit text2: " + content);
                totalCars = 1;
                myRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        for (DataSnapshot eventSnapshot : dataSnapshot.getChildren()) {

                            MovementData mModel = eventSnapshot.getValue(MovementData.class);

                            if(Objects.equals(mModel.getMyBadge(), content)) {

                                String str = String.valueOf(totalCars) + ") " + mModel.toString() ;
                                totalCars++;
                                mTexts.add(str);
                                recyclerView.setAdapter(adapter);
                            }


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

            }
        });


        return v;
    }

    public void searchBadgeId(String theBadgeId, View view) {
        Log.d(TAG, "Badge ID from edit text2: " + theBadgeId);
        RecyclerView recyclerView =  view.findViewById(R.id.badge_recycler_view);
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(mTexts, getContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot eventSnapshot : dataSnapshot.getChildren()) {
                    MovementData mModel = eventSnapshot.getValue(MovementData.class);

                    if(Objects.equals(mModel.getMyBadge(), theBadgeId)) {
                        mTexts.add(mModel.toString());
                        recyclerView.setAdapter(adapter);
                    }


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
        void onFragmentInteraction(Uri uri);
    }
}
