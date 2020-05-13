package com.example.licentafinal2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.app.DownloadManager;
import android.content.Context;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

import com.indooratlas.android.sdk.IALocation;
import com.indooratlas.android.sdk.IALocationListener;
import com.indooratlas.android.sdk.IALocationManager;
import com.indooratlas.android.sdk.IALocationRequest;
import com.indooratlas.android.sdk.IAOrientationListener;
import com.indooratlas.android.sdk.IAOrientationRequest;
import com.indooratlas.android.sdk.IARegion;
import com.indooratlas.android.sdk.resources.IAFloorPlan;

public class MainActivity extends AppCompatActivity implements IALocationListener, IAOrientationListener{
    private static final int REQUEST_CODE_WRITE_EXTERNAL_STORAGE = 123;
    private TextView textView;
    private IALocationManager locationManager;
    private IAFloorPlan mFloorPlan;
    private DownloadManager downloadManager;
    private BlueDot imageview;
    private long mDownloadId;
    private ScrollView mScrollView;

    private static final int PERMISSION_REQUEST_COARSE_LOCATION = 456;
    private mapFragment fragment;
    private long mRequestStartTime;
    private Listeners listeners;
    private EditText editText;
    private Button button;
    mapFragment.OnMessageReadListener messageReadListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_);


            mapFragment messageFragment = new mapFragment();

            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, messageFragment, null);

            fragmentTransaction.commit();
        downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        locationManager = IALocationManager.create(this);


    }
    public void onDestroy ()
    {
        super.onDestroy();
        locationManager.destroy();
    }
    public void onResume ()

    {
        super.onResume();
        ensurePermissions();
        //receive location update


        locationManager.requestLocationUpdates(IALocationRequest.create(), (IALocationListener) this);
        locationManager.registerRegionListener(listeners.mRegionListener);
        IAOrientationRequest orientationRequest = new IAOrientationRequest(10f, 10f);
        locationManager.registerOrientationListener(orientationRequest, listeners.mOrientationListener);
        registerReceiver(fragment.onComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    }



    public void onPause ()
    {
        super.onPause();
        locationManager.removeLocationUpdates(listeners.locationListener);
        locationManager.unregisterRegionListener(listeners.mRegionListener);
        locationManager.unregisterOrientationListener(listeners.mOrientationListener);
        unregisterReceiver(fragment.onComplete);
    }

    void ensurePermissions() {

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)

                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,

                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},

                    REQUEST_CODE_WRITE_EXTERNAL_STORAGE);

        }

    }

    @Override
    public void onLocationChanged(IALocation iaLocation) {

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onHeadingChanged(long timestamp, double heading) {
        if (mFloorPlan != null) {
            imageview.setHeading(heading - mFloorPlan.getBearing());
        }
    }

    @Override
    public void onOrientationChange(long l, double[] doubles) {

    }
}
