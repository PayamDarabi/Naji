package ir.zelzele.general.activities;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.VectorDrawable;
import android.os.Build;
import android.os.Bundle;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import android.view.View;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import ir.zelzele.R;
import ir.zelzele.utils.Tools;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {
    String lat = "";
    String lng = "";
    String state = "";
    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        try {
            getSupportActionBar().setTitle(R.string.occurance_place);
            Bundle extras = getIntent().getExtras();
            if (extras != null) {
                lat = Tools.converNumberPersianToEnglish(getIntent().getExtras().getString("lat"));
                lng = Tools.converNumberPersianToEnglish(getIntent().getExtras().getString("lng"));
                state = getIntent().getExtras().getString("state");
            }
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.info_map);
            mapFragment.getMapAsync(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        try {
            LatLng origin = new LatLng(Double.valueOf(lat), Double.valueOf(lng));
            CameraUpdate panToOrigin = CameraUpdateFactory.newLatLng(origin);
            googleMap.moveCamera(panToOrigin);
            googleMap.getUiSettings().setRotateGesturesEnabled(false);
            googleMap.getUiSettings().setZoomControlsEnabled(false);
            googleMap.getUiSettings().setAllGesturesEnabled(false);
            // set zoom level with animation
            googleMap.animateCamera(CameraUpdateFactory.zoomTo(10), 400, null);
         //   Bitmap bitmap = getBitmap(getApplicationContext(), R.drawable.ic_location);

            googleMap.addMarker(new MarkerOptions()
                    .position(origin)
                    .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_location))
                    .title(state).flat(true));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
/*    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
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
    public void onBackPressed() {
        super.onBackPressed();
    }
}
