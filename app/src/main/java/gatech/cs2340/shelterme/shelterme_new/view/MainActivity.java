package gatech.cs2340.shelterme.shelterme_new.view;

import android.os.Bundle;
import android.renderscript.Sampler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import gatech.cs2340.shelterme.shelterme_new.R;
import gatech.cs2340.shelterme.shelterme_new.model.Shelter;
import gatech.cs2340.shelterme.shelterme_new.model.User;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    DatabaseReference mShelter = FirebaseDatabase.getInstance().getReference().child("shelters");
    DatabaseReference mUser = FirebaseDatabase.getInstance().getReference().child("users");
    public static Map<String, Shelter> shelters = new HashMap<>();
    public static Map<String, User> users = new HashMap<>();
    private static final String TAG = MainActivity.class.getSimpleName();
    private ValueEventListener mShelterListener;
    private ValueEventListener mUserListener;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mAuth = FirebaseAuth.getInstance();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Button logoutButton = (Button) findViewById(R.id.logout_button);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        Button mapButton = (Button) findViewById(R.id.maps_button);
        mapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, MapsActivity.class);
                startActivity(intent);
            }
        });

        Button searchButton = (Button) findViewById(R.id.search_button);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ShelterListActivity.class);
                startActivity(intent);
            }
        });

        //populate shelter hashmap
        ValueEventListener shelterListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot child : dataSnapshot.getChildren()){
                    Shelter shelter = child.getValue(Shelter.class);
                    shelters.put(shelter.getShelter_name(), shelter);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "Shelter Update failed", databaseError.toException());
            }
        };
        mShelter.addValueEventListener(shelterListener);
        mShelterListener = shelterListener;

        //populate user hashmap
        ValueEventListener userListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot child : dataSnapshot.getChildren()){
                    User user = child.getValue(User.class);
                    users.put(user.getUser_name(), user);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "User Update failed", databaseError.toException());
            }
        };
        mUser.addValueEventListener(userListener);
        mUserListener = userListener;
    }

//    public void onStop(){
//        super.onStop();
//        if(mShelterListener != null){
//            mShelter.removeEventListener(mShelterListener);
//            Log.d(TAG, "removed listener");
//        }
//
//    }

    /**
     * getter for shelter map from database
     * @return shelters map of shelters from database
     */
    public Map<String, Shelter> getShelters() {
        return shelters;
    }

    //Back button. NOTE: When this back button is pressed, it takes user out of the app.
    // May want to fix this in the future.
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        this.finish();
        return true;
    }
}
