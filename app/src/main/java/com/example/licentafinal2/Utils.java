package com.example.licentafinal2;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.nfc.Tag;
import android.util.Log;
import android.view.View;

import com.google.android.material.snackbar.Snackbar;
import com.indooratlas.android.sdk.IALocationManager;
import com.example.licentafinal2.R;
public class Utils {
    private static final String TAG= " EXEMPLU";

    public static void shareTraceId(View view , final Context context , final IALocationManager manager)
    {
        view.setOnLongClickListener(new View.OnLongClickListener()
        {
            @Override
            public boolean onLongClick(View view)
            {
                Log.d(TAG,"bIANCA");
                shareText(context, manager.getExtraInfo().traceId,"traceID");
                return true;
            }
        });
    }
    public static void shareText(Context context , String text, String title)
    {
        Intent sendIntent= new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT,text);
        sendIntent.setType("text/plain");
    }
    public static void showInfo(Activity activity , String text)
    {
        final Snackbar snackbar = Snackbar.make(activity.findViewById(android.R.id.content),text,Snackbar.LENGTH_INDEFINITE);
        snackbar.setAction(R.string.button_close,new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                snackbar.dismiss();
            }
        });
        snackbar.show();
    }

}
