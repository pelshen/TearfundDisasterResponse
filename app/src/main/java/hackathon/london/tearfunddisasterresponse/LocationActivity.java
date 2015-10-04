package hackathon.london.tearfunddisasterresponse;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.Date;

import hackathon.london.tearfunddisasterresponse.questions.QuestionsActivity;


public class LocationActivity extends AppCompatActivity {

    private static final String TAG = "LocationActivity";

    private ProgressDialog progressDialog = new ProgressDialog(LocationActivity.this);

    private String category;

    // Acquire a reference to the system Location Manager
    private LocationManager locationManager;

    // Define a listener that responds to location updates
    private LocationListener locationListener = new LocationListener() {
        public void onLocationChanged(Location location) {
            // Called when a new location is found by the network location provider.
            makeUseOfNewLocation(location);
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
            Log.d(TAG, "status changed, provider: " + provider + ", status: " + status);
        }

        public void onProviderEnabled(String provider) {
            Log.d(TAG, "provider enabled");
        }

        public void onProviderDisabled(String provider) {
            Log.d(TAG, "provider disabled");
        }
    };

    private void makeUseOfNewLocation(Location location) {
        locationManager.removeUpdates(locationListener);
        Log.d(TAG, "making use of new location...");
        // change screen state "got location!"
        TextView locationStateTextView = (TextView) findViewById(R.id.locationState);

        // move to next app
        // wait a bit before changing so flow is not incomprehensible
        Handler handler = new Handler();
        /* TODO add tick */

        int millisToWait = 1000;
        handler.postDelayed(new Runnable() {
            public void run() {
                Intent moveToQuestionsIntent = new Intent(LocationActivity.this, QuestionsActivity.class);
                moveToQuestionsIntent.putExtra("Category", category);

                findViewById(R.id.loadingPanel).setVisibility(View.GONE);
                startActivity(moveToQuestionsIntent);
            }
        }, millisToWait);

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        Log.d(TAG, "onCreate");

        category = getIntent().getStringExtra("Category");

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        // Register the listener with the Location Manager to receive location updates
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, (long) 0, (float) 0, locationListener);
        Log.d(TAG, "location updates requested");
    }




}