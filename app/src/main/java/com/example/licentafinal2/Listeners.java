package com.example.licentafinal2;

import android.app.DownloadManager;
import android.graphics.PointF;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ScrollView;

import androidx.fragment.app.Fragment;

import com.davemorrissey.labs.subscaleview.ImageSource;
import com.indooratlas.android.sdk.IALocation;
import com.indooratlas.android.sdk.IALocationListener;
import com.indooratlas.android.sdk.IALocationManager;
import com.indooratlas.android.sdk.IAOrientationListener;
import com.indooratlas.android.sdk.IARegion;
import com.indooratlas.android.sdk.resources.IAFloorPlan;
import com.indooratlas.android.sdk.resources.IALatLng;
import com.indooratlas.android.sdk.resources.IALocationListenerSupport;

import java.io.File;

public class Listeners {
    private static final int REQUEST_CODE_ASK_PERMISSIONS = 123;
    private final int CODE_PERMISSIONS = 1;
    private static final int REQUEST_CODE_WRITE_EXTERNAL_STORAGE = 1;
    private static final String TAG = " EXEMPLU";

    private static final float dotRadius = 1.0f;
    private Fragment fragment;

    private IALocationManager locationManager;
    private IAFloorPlan mFloorPlan;
    private DownloadManager downloadManager;
    private BlueDot imageview;
    private long mDownloadID;
    private ScrollView mScrollView;

    private static final int PERMISSION_REQUEST_COARSE_LOCATION = 456;

    private long mRequestStartTime;
    private mapFragment mapFragment;

    protected void onCreate(Bundle savedInstanceState){

    }

    public IALocationListener locationListener = new IALocationListenerSupport() {

        @Override
        public void onLocationChanged(IALocation iaLocation) {
            Log.d(TAG, "location is : " + iaLocation.getLatitude() + " , " + iaLocation.getLongitude());
            if (imageview != null && imageview.isReady()) {
                IALatLng latLng = new IALatLng(iaLocation.getLatitude(), iaLocation.getLongitude());
                PointF point = mFloorPlan.coordinateToPoint(latLng);
                imageview.setDotCenter(point);
                imageview.setRadius1(mFloorPlan.getMetersToPixels() * iaLocation.getAccuracy());
                imageview.postInvalidate();
            }
        }
    };
    //-----listener 2
    public IAOrientationListener mOrientationListener = new IAOrientationListener() {
        @Override
        public void onHeadingChanged(long timestamp, double heading) {
            if (mFloorPlan != null) {
                imageview.setHeading(heading - mFloorPlan.getBearing());
            }
        }

        @Override
        public void onOrientationChange(long l, double[] doubles) {

        }
    };

    //------- 3
    public IARegion.Listener mRegionListener = new IARegion.Listener() {
        @Override
        public void onEnterRegion(IARegion iaRegion) {
            if (iaRegion.getType() == IARegion.TYPE_FLOOR_PLAN) {
                String id = iaRegion.getId();
                Log.d(TAG, "floorPlan changed to " + id);
                fetchFloorPlan(mFloorPlan);
            }
        }

        @Override
        public void onExitRegion(IARegion iaRegion) {

        }
    };
    void showFloorPlanImage(String filePath){

        Log.w(TAG, "showFloorPlanImage: " + filePath);
        imageview.setRadius2(mFloorPlan.getMetersToPixels() * dotRadius);
        imageview.setImage(ImageSource.uri(filePath));
    }
    private void fetchFloorPlan (IAFloorPlan floorPlan)
    {
        mFloorPlan = floorPlan;
        String fileName = mFloorPlan.getId() + ".img";
        String filePath = Environment.getExternalStorageDirectory() + "/" + Environment.DIRECTORY_DOWNLOADS + "/" + fileName;

        File file = new File(filePath);
        if (!file.exists()) {
            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(floorPlan.getUrl()));
            request.setDescription("FloorPlan from IndoorAtlas");
            request.setTitle("Floor plan");
            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName);

            mDownloadID = downloadManager.enqueue(request);
        } else {
            showFloorPlanImage(filePath);
        }
    }
}





