package com.example.aimsh.movementcontrol;

import android.content.Intent;
import android.hardware.Camera;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import static android.content.ContentValues.TAG;

/**
 * Application for the movement control
 *
 * @author Aimal Shir Jan
 * @version 7 July 2018
 */

public class MainActivity extends AppCompatActivity implements LoginPage.OnFragmentInteractionListener,
                                                                ScannerPage.OnFragmentInteractionListener,
                                                                Monitor.OnFragmentInteractionListener,
                                                                CarHistory.OnFragmentInteractionListener,
                                                                Scanner.OnFragmentInteractionListener,
                                                                BadgeHistory.OnFragmentInteractionListener{

//    FirebaseDatabase databse = FirebaseDatabase.getInstance();
//    DatabaseReference myRef = databse.getReference();
//    private ArrayList<String> mTexts = new ArrayList<>();

    private FirebaseAuth mAuth;

    private DatabaseReference myRef;

    DateFormat df = new SimpleDateFormat("MM.dd.yyyy G 'at' HH:mm:ss");

    DateFormat store =  new SimpleDateFormat("MM-dd-yyyy");

    private static String result_Location = "Default";

    private static String car;

    private static String badge;

    private int turn = 1;

    private static boolean isBadge = true;

    private static boolean isCarVin = false;

    public static String carVinNum;

    public final int CUSTOMIZED_REQUEST_CODE = 0x0000ffff;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myRef = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        if(savedInstanceState == null) {
            if (findViewById(R.id.fragmentContainer) != null) {
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.fragmentContainer, new LoginPage())
                        .commit();

            }
        }
    }


    public void startMonitor() {

        Monitor monitorFragment = (Monitor) getSupportFragmentManager().findFragmentById(R.id.monitor_fragmentID);

        if(monitorFragment == null) {
            monitorFragment = new Monitor();
            FragmentTransaction transaction = getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragmentContainer, monitorFragment)
                    .addToBackStack(null);

            transaction.commit();
        }
    }

    public void startCarHistory(){


        CarHistory carHistoryFragment = (CarHistory) getSupportFragmentManager().findFragmentById(R.id.carHistory_fragmentID);

        if(carHistoryFragment == null) {
            carHistoryFragment = new CarHistory();
            FragmentTransaction transaction = getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragmentContainer, carHistoryFragment)
                    .addToBackStack(null);

            transaction.commit();
        }

    }

    @Override
    public void startScanning() {

        Scanner scannerFragment = (Scanner) getSupportFragmentManager().findFragmentById(R.id.scanner_FragmentID);

        if(scannerFragment == null) {
            scannerFragment = new Scanner();
            FragmentTransaction transaction = getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragmentContainer, scannerFragment)
                    .addToBackStack(null);

            transaction.commit();
        }
    }

    @Override
    public void Scanning(View v) {
        scanBarcode(v);
    }

    public void carVin(View v, boolean carVinNum) {
        isCarVin = true;
        isBadge = true;
        scanBarcode(v);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    public void startBadgeHistory(){
        BadgeHistory badgeFragment = (BadgeHistory) getSupportFragmentManager().findFragmentById(R.id.badgeHistory_Fragment);

        if(badgeFragment == null) {
            badgeFragment = new BadgeHistory();
            FragmentTransaction transaction = getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragmentContainer, badgeFragment)
                    .addToBackStack(null);

            transaction.commit();
        }
    }

    @Override
    public void setLocation(String loc) {
        result_Location = loc;
    }


    @Override
    public void onFragmentInteraction(String username, String password) {

        mAuth.signInWithEmailAndPassword(username, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            ScannerPage scannerFragment = new ScannerPage();


                            FragmentTransaction transaction = getSupportFragmentManager()
                                    .beginTransaction()
                                    .replace(R.id.fragmentContainer, scannerFragment)
                                    .addToBackStack(null);

                            // Commit the transaction
                            transaction.commit();

                        } else {
                            // If sign in fails, display a message to the user.
//                            Log.w(TAG, "signInWithEmail:failure", task.getException());
//                            Toast.makeText(EmailPasswordActivity.this, "Authentication failed.",
//                                    Toast.LENGTH_SHORT).show();
//                            updateUI(null);
                            Toast.makeText(getBaseContext(), "WRONG USERNAME OR PASSWORD", Toast.LENGTH_LONG).show();
                        }

                    }
                });


    }


    public void scanResult(View view, String theLocation){
        result_Location = theLocation;
        scanBarcode(view);
    }

    public void scanBarcode(View view) {
//        result_Location = location;
        new IntentIntegrator(this).initiateScan();
    }

    public void scanBarcodeFrontCamera(View view) {
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setCameraId(Camera.CameraInfo.CAMERA_FACING_FRONT);
        integrator.initiateScan();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode != CUSTOMIZED_REQUEST_CODE && requestCode != IntentIntegrator.REQUEST_CODE) {
            // This is important, otherwise the result will not be passed to the fragment
            super.onActivityResult(requestCode, resultCode, data);
            return;
        }
        switch (requestCode) {
            case CUSTOMIZED_REQUEST_CODE: {
                Toast.makeText(this, "REQUEST_CODE = " + requestCode, Toast.LENGTH_LONG).show();
                break;
            }
            default:
                break;
        }

        IntentResult result = IntentIntegrator.parseActivityResult(resultCode, data);

        if(result.getContents() == null) {
            Log.d("MainActivity", "Cancelled scan");
            Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
        } else {
//            Log.d("MainActivity", "Scanned");
//            Toast.makeText(this, "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();
//            String car = result.getContents();
//            String date = df.format(Calendar.getInstance().getTime());
//            MovementData md = new MovementData(car, car, date, result_Location);

//            myRef.child("Test1").push().setValue(md);

            if(isBadge){
                Log.d(TAG, "IN TURN 1");
                isBadge = false;
                car = result.getContents();
            }else {
                isBadge = true;
                Log.d(TAG, "IN TURN 2");
                badge = result.getContents();
                String date = df.format(Calendar.getInstance().getTime());
                String storeDate = store.format(Calendar.getInstance().getTime());
                MovementData md = new MovementData(car, badge, date, result_Location);
                myRef.child(storeDate).push().setValue(md);
                Toast.makeText(this, "SUCCESS: ", Toast.LENGTH_LONG).show();
            }

            if(isCarVin) {
                isCarVin = false;
                carVinNum = result.getContents();
            }


        }
    }

    @Override
    public void monitoring() {
//        mTexts.add("fasfa");
//        mTexts.add("fsadgsadg");
//        RecyclerView recyclerView =  findViewById(R.id.recycler_view);
//        RecyclerViewAdapter adapter = new RecyclerViewAdapter(mTexts,this);
//        recyclerView.setAdapter(adapter);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));


    }

}
