package com.example.rememberwhen;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.util.List;

/**
 * Created by jgluck on 5/13/14.
 */
public class GPSDebugActivity extends Activity {

    SharedPreferences prefs;


    public static Location getLastLocation(Context context) {
        Location result = null;
        LocationManager locationManager;
        Criteria locationCriteria;
        List<String> providers;

        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        locationCriteria = new Criteria();
        locationCriteria.setAccuracy(Criteria.NO_REQUIREMENT);
        providers = locationManager.getProviders(locationCriteria, true);

        Log.w("Jon Message: ", "about to iterate through providers, there are: " + providers.size());

        for (String provider : providers) {
            Log.w("Jon Message: ", "Provider is " + provider);
            Location location = locationManager.getLastKnownLocation(provider);
            if (result == null) {
                result = location;
            }
            else if (result.getAccuracy() == 0.0) {
                if (location.getAccuracy() != 0.0) {
                    result = location;
                    break;
                } else {
                    if (result.getAccuracy() > location.getAccuracy()) {
                        result = location;
                    }
                }
            }
        }
        return result;
    }


    @Override
    protected void onDestroy()
    {
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        prefs = this.getSharedPreferences(
                "com.example.rememberwhen", Context.MODE_PRIVATE);
        setContentView(R.layout.gps_debug_layout);

        loadLocation();


    }

    protected void loadLocation(){
        TextView myText = (TextView) findViewById(R.id.gps_text);
        Location ourLocation =  getLastLocation(this);
        Log.w(this.getPackageName(), String.format("Location was not null: %b", (ourLocation != null)));
        if(ourLocation != null){
            String lat = Double.toString(ourLocation.getLatitude());
            String lon = Double.toString(ourLocation.getLongitude());
            String total = lat + " " + lon;
            Log.w(this.getPackageName(), total);
            myText.setText(total);
        }
    }
}
