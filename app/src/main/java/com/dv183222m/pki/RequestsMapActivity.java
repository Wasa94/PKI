package com.dv183222m.pki;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.dv183222m.pki.com.dv183222m.pki.data.DbContext;
import com.dv183222m.pki.com.dv183222m.pki.data.Request;
import com.dv183222m.pki.com.dv183222m.pki.data.User;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

public class RequestsMapActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private User user;
    private Request request;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requests_map);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Intent intent = getIntent();
        String username = intent.getStringExtra("Username");
        int requestId = intent.getIntExtra("Request", -1);
        user = DbContext.INSTANCE.getUser(username);
        if(requestId != -1) {
            request = DbContext.INSTANCE.getRequest(requestId);
        }
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        LatLng address = getLocationFromAddress(this, user.getAddress());
        if(address != null) {
            mMap.addMarker(new MarkerOptions().position(address).title("My Address").icon(BitmapDescriptorFactory
                    .defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(address, 10));
        }

        if(request == null) {
            List<Request> requests = DbContext.INSTANCE.getActiveRequests(user.getUsername());
            for (Request requestTmp : requests) {
                LatLng tmp = getLocationFromAddress(this, requestTmp.getAddress() + " " + requestTmp.getMunicipality());
                if (tmp != null) {
                    mMap.addMarker(new MarkerOptions().position(tmp).title(requestTmp.getClient().getFullName()));
                }
            }
        }
        else {
            LatLng tmp = getLocationFromAddress(this, request.getAddress() + " " + request.getMunicipality());
            if (tmp != null) {
                mMap.addMarker(new MarkerOptions().position(tmp).title(request.getClient().getFullName()));
            }
        }
    }

    public LatLng getLocationFromAddress(Context context, String strAddress)
    {
        Geocoder coder= new Geocoder(context);
        List<Address> address;
        LatLng p1 = null;

        try
        {
            address = coder.getFromLocationName(strAddress, 5);
            if(address==null)
            {
                return null;
            }
            Address location = address.get(0);
            location.getLatitude();
            location.getLongitude();

            p1 = new LatLng(location.getLatitude(), location.getLongitude());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return p1;

    }
}
