package org.a10019420sbstudents.gps;

import android.location.Address;
import android.location.Location;

/**
 * Created by 10019420 on 1/17/2017.
 */
public class FavoriteLocation {
    Address address;
    long time;

    public FavoriteLocation(Address address, long time){
        this.address = address;
        this.time = time;
    }

    public Address getAddress() {
        return address;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time){
        this.time = time;
    }
}
