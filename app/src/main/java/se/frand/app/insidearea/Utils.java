package se.frand.app.insidearea;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polygon;

import java.util.ArrayList;

/**
 * Created by victorfrandsen on 10/9/15.
 */
public class Utils {
    public static boolean isPointInPolygon(Polygon polygon, Location location) {
        int intersectCount = 0;
        ArrayList<LatLng> vertices = (ArrayList<LatLng>) polygon.getPoints();
        for(int j=0; j<vertices.size()-1; j++) {
            if( rayCastIntersect(location, vertices.get(j), vertices.get(j + 1)) ) {
                intersectCount++;
            }
        }

        return ((intersectCount%2) == 1); // odd = inside, even = outside;
    }


    // raycasting algorithm thanks to someone on stackoverflow
    private static boolean rayCastIntersect(Location loc, LatLng vertA, LatLng vertB) {

        double aY = vertA.latitude;
        double bY = vertB.latitude;
        double aX = vertA.longitude;
        double bX = vertB.longitude;
        double pY = loc.getLatitude();
        double pX = loc.getLongitude();

        if ( (aY>pY && bY>pY) || (aY<pY && bY<pY) || (aX<pX && bX<pX) ) {
            return false; // a and b can't both be above or below pt.y, and a or b must be east of pt.x
        }

        double m = (aY-bY) / (aX-bX);               // Rise over run
        double bee = (-aX) * m + aY;                // y = mx + b
        double x = (pY - bee) / m;                  // algebra is neat!

        return x > pX;
    }
}
