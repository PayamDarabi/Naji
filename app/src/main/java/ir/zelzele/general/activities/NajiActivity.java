package ir.zelzele.general.activities;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.VectorDrawable;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.wang.avi.AVLoadingIndicatorView;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import ir.zelzele.R;
import ir.zelzele.customview.CustomButtonView;
import ir.zelzele.customview.CustomTextView;
import ir.zelzele.models.SendLocation;
import ir.zelzele.storage.PreferenceManager;
import ir.zelzele.utils.AppInfoProvider;
import ir.zelzele.utils.Tools;
import ir.zelzele.webservices.ApiMethodCaller;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NajiActivity extends AppCompatActivity implements
        OnMapReadyCallback,
        LocationListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        ResultCallback<LocationSettingsResult> {

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 1000;
    private static final int ACCESS_FINE_LOCATION_INTENT_ID = 1047;

    private static int UPDATE_INTERVAL = 2400000;
    private static int UPDATE_FAST_INTERVAL = 1800000;


    protected GoogleApiClient mGoogleApiClient;
    protected LocationRequest locationRequest;
    int REQUEST_CHECK_SETTINGS = 100;

    CustomButtonView btn_sendLocationManual;
    CustomTextView ctv_address, ctv_lastlocTime;
    AVLoadingIndicatorView prg_sendLoc;

    private Location mLastLocation;
    private GoogleMap googleMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_naji);
        try {
            if (Tools.internetConnect()) {
                SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.naji_map);
                mapFragment.getMapAsync(this);
                View mapView = mapFragment.getView();
                if (mapView != null &&
                        mapView.findViewById(1) != null) {
                    // Get the button view
                    View locationButton = ((View) mapView.findViewById(1).getParent()).findViewById(2);
                    // and next place it, on bottom right (as Google Maps app)
                    RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams)
                            locationButton.getLayoutParams();
                    int[] ruleList = layoutParams.getRules();
                    for (int i = 0; i < ruleList.length; i++) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                            layoutParams.removeRule(i);
                        }
                    }
                    // position on right bottom
                    layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
                    layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
                    layoutParams.setMargins(200, 50, 30, 30);
                }
                checkPermissions();

                mGoogleApiClient = new GoogleApiClient.Builder(this)
                        .addApi(LocationServices.API)
                        .addConnectionCallbacks(this)
                        .addOnConnectionFailedListener(this).build();
                mGoogleApiClient.connect();
                locationRequest = LocationRequest.create();
                locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                locationRequest.setInterval(UPDATE_INTERVAL);
                locationRequest.setFastestInterval(UPDATE_FAST_INTERVAL);

                btn_sendLocationManual = (CustomButtonView)
                        findViewById(R.id.btn_sendLocationManual);
                ctv_address = (CustomTextView) findViewById(R.id.ctv_curLocAddress);
                ctv_lastlocTime = (CustomTextView) findViewById(R.id.ctv_lastLocTime);
                prg_sendLoc = (AVLoadingIndicatorView) findViewById(R.id.prg_sendLoc);
                prg_sendLoc.show();

                try {
                    if (!PreferenceManager.getInstance(getApplicationContext()).getLastServerUpdateTime().isEmpty())
                        ctv_lastlocTime.setText(PreferenceManager.getInstance(getApplicationContext()).getLastServerUpdateTime());

                    if (!PreferenceManager.getInstance(getApplicationContext()).getLastServerLatLng().isEmpty()) {
                        String[] latlng = PreferenceManager.getInstance(getApplicationContext()).getLastServerLatLng().split("-");
                        LatLng latLngs = new LatLng(Double.valueOf(latlng[0]), Double.valueOf(latlng[1]));
                        ctv_address.setText(getAddressFromLatLng(latLngs));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                btn_sendLocationManual.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (Tools.internetConnect()) {
                            if (Tools.requestcheck(view.getContext()))
                                sendLocationManual(true);
                            //    else
                            //      Toast.makeText(NajiActivity.this, R.string.request_exceed_error, Toast.LENGTH_LONG).show();}
                            // } else
                        }
                        Toast.makeText(NajiActivity.this, R.string.no_internet_conection, Toast.LENGTH_SHORT).show();
                    }
                });

                SwitchCompat switchCompat = (SwitchCompat) findViewById(R.id.swc_autoLoc);
                if (PreferenceManager.getInstance(getApplicationContext()).isNajiOn())
                    switchCompat.setChecked(true);
                else
                    switchCompat.setChecked(false);

                switchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        if (b) {
                            PreferenceManager.getInstance(getApplicationContext()).setNajioN(true);
                            startLocationUpdates();

                        } else {
                            PreferenceManager.getInstance(getApplicationContext()).setNajioN(false);
                            stopLocationUpdates();
                        }
                    }
                });
        /* موقعیت مکانی فعلی شما*/
            } else {
                Toast.makeText(NajiActivity.this, R.string.no_internet_conection, Toast.LENGTH_SHORT).show();
                finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void checkPermissions() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (ContextCompat.checkSelfPermission(NajiActivity.this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED)
                requestLocationPermission();
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);
        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(
                        mGoogleApiClient,
                        builder.build()
                );
        result.setResultCallback(this);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Location mLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);//getting last know location

        if (mLocation != null) {
            //Last known location got
        }

        //Start Location Update on successful connection
        startLocationUpdates();
    }

    protected void startLocationUpdates() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestLocationPermission();
            return;
        }
        if (Tools.requestcheck(getApplicationContext()))
            LocationServices.FusedLocationApi.requestLocationUpdates(
                    mGoogleApiClient, locationRequest, this);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onResult(@NonNull LocationSettingsResult locationSettingsResult) {
        final Status status = locationSettingsResult.getStatus();
        switch (status.getStatusCode()) {
            case LocationSettingsStatusCodes.SUCCESS:
                // NO need to show the dialog;
                displayLocation();
                break;

            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                //  GPS disabled show the user a dialog to turn it on
                try {
                    // Show the dialog by calling startResolutionForResult(), and check the result
                    // in onActivityResult().

                    status.startResolutionForResult(NajiActivity.this, REQUEST_CHECK_SETTINGS);

                } catch (IntentSender.SendIntentException e) {
                    finish();
                    //failed to show dialog
                }
                break;
            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                finish();
                // Location settings are unavailable so not possible to show any dialog now
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CHECK_SETTINGS) {
            if (resultCode == RESULT_OK) {
                //     displayLocation();
                startLocationUpdates();
            } else {
                Toast.makeText(getApplicationContext(), R.string.location_shouldOn, Toast.LENGTH_LONG).show();
                finish();
            }

        }
    }


    private void sendLocationManual(final boolean showToast) {
        try {
            Location curLocation = mLastLocation;
            final String lat = String.valueOf(curLocation.getLatitude());
            final String lng = String.valueOf(curLocation.getLongitude());
            //    PreferenceManager.getInstance(getApplicationContext()).setL//ngLat(lat + "-" + lng);
            prg_sendLoc.show();
            btn_sendLocationManual.setEnabled(false);
            ApiMethodCaller.getInstance(false, getApplicationContext()).sendUserLocationToServer(
                    PreferenceManager.getInstance(getApplicationContext()).getUsername(),
                    lat, lng, PreferenceManager.getInstance(this).getFirebaseToken(), AppInfoProvider.getDeviceName(), "android", new Callback<SendLocation>() {
                        @Override
                        public void onResponse(Call<SendLocation> call, Response<SendLocation> response) {
                            prg_sendLoc.hide();
                            SendLocation respones = response.body();
                            btn_sendLocationManual.setEnabled(true);
                            try {
                                if (Integer.valueOf(respones.getResult()) == 1) {
                                    if (showToast)
                                        Toast.makeText(NajiActivity.this, R.string.location_send_successfully,
                                                Toast.LENGTH_LONG).show();

                                    PreferenceManager.getInstance(getApplicationContext()).setLastServerLatLng(lat + "-" + lng);
                                    ctv_lastlocTime.setVisibility(View.VISIBLE);
                                    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
                                    String currentDateandTime = sdf.format(new Date());
                                    String cur_time = Tools.getCurrentShamsidate() + " " +
                                            Tools.convertEnglishNumbersToPersian(currentDateandTime);
                                    ctv_lastlocTime.setText(cur_time);
                                    Log.d("sendLocationAuto", lat + lng + "-" + cur_time);
                                    PreferenceManager.getInstance(getApplicationContext()).setLastServerUpdateTime(cur_time);
                                    PreferenceManager.getInstance(getApplicationContext()).setRequestDate(System.currentTimeMillis());

                                } else {
                                    if (showToast)
                                        Toast.makeText(NajiActivity.this, response.message(),
                                                Toast.LENGTH_LONG).show();
                                }
                            } catch (Exception e) {
                                if (showToast)
                                    Toast.makeText(NajiActivity.this, R.string.error_in_webservices,
                                            Toast.LENGTH_LONG).show();
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailure(Call<SendLocation> call, Throwable t) {
                            if (showToast)
                                prg_sendLoc.hide();
                            btn_sendLocationManual.setEnabled(true);
                            Toast.makeText(NajiActivity.this, R.string.error_in_webservices,
                                    Toast.LENGTH_LONG).show();

                        }
                    }
            );
        } catch (Exception e) {
            e.printStackTrace();
            if (showToast)
                Toast.makeText(NajiActivity.this, R.string.error_in_webservices,
                        Toast.LENGTH_LONG).show();
        }
    }


    private String getAddressFromLatLng(LatLng latLng) {
        Geocoder geocoder = new Geocoder(this, new Locale("fa"));

        String address = "";
        try {
            address = geocoder
                    .getFromLocation(latLng.latitude, latLng.longitude, 1)
                    .get(0).getAddressLine(0);
            address = address.replace("، ایران", "");
        } catch (IOException e) {
            e.printStackTrace();
        }

        return address;
    }

    protected void stopLocationUpdates() {

        LocationServices.FusedLocationApi.removeLocationUpdates(
                mGoogleApiClient, this);
    }

    private void requestLocationPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(NajiActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION)) {
            ActivityCompat.requestPermissions(NajiActivity.this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    ACCESS_FINE_LOCATION_INTENT_ID);

        } else {
            ActivityCompat.requestPermissions(NajiActivity.this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    ACCESS_FINE_LOCATION_INTENT_ID);
        }
    }

    private void displayLocation() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            //requestPermission();
            requestLocationPermission();
            return;
        }


        //   mLastLocation = LocationServices.FusedLocationApi
        //        .getLastLocation(mGoogleApiClient);
        mLastLocation = LocationServices.FusedLocationApi
                .getLastLocation(mGoogleApiClient);

        if (mLastLocation != null) {
            double latitude = mLastLocation.getLatitude();
            double longitude = mLastLocation.getLongitude();

            googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            LatLng latLng = new LatLng(latitude, longitude);
            CameraUpdate panToOrigin = CameraUpdateFactory.newLatLng(latLng);
            ctv_address.setVisibility(View.VISIBLE);

            ctv_address.setText(getAddressFromLatLng(latLng));
            googleMap.clear();
            googleMap.moveCamera(panToOrigin);
            googleMap.getUiSettings().setRotateGesturesEnabled(false);
            googleMap.getUiSettings().setZoomControlsEnabled(false);
            googleMap.getUiSettings().setAllGesturesEnabled(false);
            // Bitmap bitmap = getBitmap(getApplicationContext(), R.drawable.ic_location);
            googleMap.addMarker(new MarkerOptions()
                    .position(latLng)
                    .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_location))
                    .flat(true));

            googleMap.animateCamera(CameraUpdateFactory.zoomTo(16), 400, null);
            prg_sendLoc.hide();
            btn_sendLocationManual.setVisibility(View.VISIBLE);
            if (Tools.requestcheck(getApplicationContext()))
                sendLocationManual(false);
            else
                Toast.makeText(NajiActivity.this, R.string.request_exceed_error, Toast.LENGTH_LONG).show();

            //     lblLocation.setText(latitude + ", " + longitude);

        } else {

            Toast.makeText(this, R.string.cantfind_location, Toast.LENGTH_LONG).show();
            finish();
        }
    }

    /*   @TargetApi(Build.VERSION_CODES.LOLLIPOP)
       private static Bitmap getBitmap(VectorDrawable vectorDrawable) {
           Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(),
                   vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
           Canvas canvas = new Canvas(bitmap);
           vectorDrawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
           vectorDrawable.draw(canvas);
           return bitmap;
       }
       private static Bitmap getBitmap(Context context, int drawableId) {
           Drawable drawable = ContextCompat.getDrawable(context, drawableId);
           if (drawable instanceof BitmapDrawable) {
               return BitmapFactory.decodeResource(context.getResources(), drawableId);
           } else if (drawable instanceof VectorDrawable) {
               return getBitmap((VectorDrawable) drawable);
           } else {
               throw new IllegalArgumentException("unsupported drawable type");
           }
       }*/
    @Override
    public void onLocationChanged(Location location) {
        // Assign the new location
        mLastLocation = location;

        // Displaying the new location on UI
        displayLocation();

    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkPlayServices();
    }

    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil
                .isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Toast.makeText(getApplicationContext(),
                        R.string.notSupportedPhone, Toast.LENGTH_LONG)
                        .show();
                finish();
            }
            return false;
        }
        return true;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestLocationPermission();
            return;
        }
        this.googleMap.setMyLocationEnabled(true);
        this.googleMap.setOnMyLocationClickListener(new GoogleMap.OnMyLocationClickListener() {
            @Override
            public void onMyLocationClick(@NonNull Location location) {
                mLastLocation = location;
                displayLocation();
            }
        });

        this.googleMap.setOnMyLocationClickListener(new GoogleMap.OnMyLocationClickListener() {
            @Override
            public void onMyLocationClick(@NonNull Location location) {
                mLastLocation = location;
                displayLocation();
            }
        });


    }

    /* On Request permission method to check the permisison is granted or not for Marshmallow+ Devices  */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case ACCESS_FINE_LOCATION_INTENT_ID: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {
                    Toast.makeText(NajiActivity.this, R.string.location_needed, Toast.LENGTH_SHORT).show();
                    finish();
                    // permission denied
                }
                return;
            }
        }
    }
}