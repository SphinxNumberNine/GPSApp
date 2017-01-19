package org.a10019420sbstudents.gps;

import android.location.Address;
import android.location.Location;

/**
 * Created by 10019420 on 1/17/2017.
 */
public class FavoriteLocation {
    Location location;
    long time;

    public FavoriteLocation(Location location, long time){
        this.location = location;
        this.time = time;
    }

    public FavoriteLocation(Location location){
        this.location = location;
        this.time = 0;
    }

    public Location getLocation() {
        return location;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time){
        this.time = time;
    }
}
