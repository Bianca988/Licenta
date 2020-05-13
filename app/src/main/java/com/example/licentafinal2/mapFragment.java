package com.example.licentafinal2;

import android.Manifest;
import android.app.Activity;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;

import com.indooratlas.android.sdk.IALocation;
import com.indooratlas.android.sdk.IALocationListener;
import com.indooratlas.android.sdk.IALocationManager;
import com.indooratlas.android.sdk.IALocationRequest;
import com.indooratlas.android.sdk.IAOrientationRequest;
import com.indooratlas.android.sdk.resources.IAFloorPlan;

import java.util.Locale;




public class mapFragment extends Fragment {
    private static final int REQUEST_CODE_ASK_PERMISSIONS = 123;
    private final int CODE_PERMISSIONS = 1;
    private static final int REQUEST_CODE_WRITE_EXTERNAL_STORAGE =1;
    private static final String TAG= " EXEMPLU";

    private static final float dotRadius=1.0f;
    private Fragment fragment;

    private IALocationManager locationManager;
    private IAFloorPlan mFloorPlan;
    private DownloadManager downloadManager;
    private BlueDot imageview;
    private long mDownloadId;
    private ScrollView mScrollView;

    private static final int PERMISSION_REQUEST_COARSE_LOCATION = 456;

    private long mRequestStartTime;
    private Listeners listeners;
    private EditText editText;
    private Button button;
    OnMessageReadListener messageReadListener;

    public mapFragment() {
        // Required empty public constructor
    }
    public interface OnMessageReadListener
    {
        public void onMessageRead(String message);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_, container, false);


        button = view.findViewById(R.id.bn);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Listeners();
                imageview = imageview.findViewById(R.id.imageView);
            }

        });


        Utils.shareTraceId(view.findViewById(R.id.imageView), getContext(), locationManager);

        return view;
    }


       public BroadcastReceiver onComplete = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, 0L);

                if (id != mDownloadId) {

                    Log.w(TAG, "Ignore unrelated download");

                    return;

                }

                Log.w(TAG, "Image download completed");

                Bundle extras = intent.getExtras();



                if (extras == null) {

                    Log.w(TAG, "Extras null: can't show floor plan");

                    return;

                }



                DownloadManager.Query q = new DownloadManager.Query();

                q.setFilterById(extras.getLong(DownloadManager.EXTRA_DOWNLOAD_ID));

                Cursor c = downloadManager.query(q);



                if (c.moveToFirst()) {

                    int status = c.getInt(c.getColumnIndex(DownloadManager.COLUMN_STATUS));

                    if (status == DownloadManager.STATUS_SUCCESSFUL) {

                        // process download

                        String filePath = c.getString(c.getColumnIndex(

                                DownloadManager.COLUMN_LOCAL_URI));

                        listeners.showFloorPlanImage(filePath);

                    }

                }

                c.close();

            }

        };
        public void onAttach(Context context)
        {
            super.onAttach(context);

            Activity activity = (Activity) context;
            messageReadListener = (OnMessageReadListener) activity;
            try {
                messageReadListener = (OnMessageReadListener) activity;
            }catch (ClassCastException e)
            {
                throw new ClassCastException(activity.toString()+"must override onMessageRead.");
            }
        }

}
