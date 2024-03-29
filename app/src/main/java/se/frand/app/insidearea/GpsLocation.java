package se.frand.app.insidearea;

import android.content.Context;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.maps.model.Polygon;

/**
 * Created by victorfrandsen on 9/24/15.
 */
public class GpsLocation implements LocationListener {

    private static final long MIN_TIME_BW_UPDATES = 60000;
    private static final float MIN_DISTANCE_CHANGE_FOR_UPDATES = 20;
    Context mContext;
    TextView gpsStatusTextView;
    boolean canGetLocation;
    private double latitude;
    private double longitude;
    private Polygon mPolygon;

    public GpsLocation(Context mContext, TextView gpsStatusTextView) {
        this.mContext = mContext;
        this.gpsStatusTextView = gpsStatusTextView;
        getLocation();
    }

    @Override
    public void onLocationChanged(Location location) {
        // on location changed: check if current location is inside polygon

        boolean contained = Utils.isPointInPolygon(mPolygon,location);

        if(contained) {
            mPolygon.setFillColor(Color.GREEN);
            mPolygon.setStrokeColor(Color.GREEN);
        } else {
            mPolygon.setFillColor(Color.RED);
            mPolygon.setStrokeColor(Color.RED);
        }


    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    public Location getLocation(){
        LocationManager locationManager;
        Location location = null;
        boolean isGPSEnabled;
        boolean isNetworkEnabled;

        try {
            locationManager = (LocationManager) mContext
                    .getSystemService(Context.LOCATION_SERVICE);

            isGPSEnabled = locationManager
                    .isProviderEnabled(LocationManager.GPS_PROVIDER);

            isNetworkEnabled = locationManager
                    .isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if(!isGPSEnabled && !isNetworkEnabled) {

            } else {
                this.canGetLocation = true;
                if (isNetworkEnabled) {
                    locationManager.requestLocationUpdates(
                            LocationManager.NETWORK_PROVIDER,
                            MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                    if (locationManager != null) {
                        location = locationManager
                                .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        if (location != null) {
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                        }
                    }
                }
                if (isGPSEnabled) {
                    if (location == null) {
                        locationManager.requestLocationUpdates(
                                LocationManager.GPS_PROVIDER,
                                MIN_TIME_BW_UPDATES,
                                MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                        Log.d("GPS Enabled", "GPS Enabled");
                        if (locationManager != null) {
                            location = locationManager
                                    .getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            if (location != null) {
                                latitude = location.getLatitude();
                                longitude = location.getLongitude();
                            }
                        }
                    }
                }
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
        return location;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public boolean canGetLocation() {
        return canGetLocation;
    }


    public void setPolygon(Polygon polygon) {
        mPolygon = polygon;
        onLocationChanged(this.getLocation());
    }

}
