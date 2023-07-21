package com.example.as_flexifuel_firebase_2023;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;
import android.widget.TextView;

public class PackageUpdateReceiver extends BroadcastReceiver {
    private TextView tvVersion;

    public void setTextView(TextView tvVersion) {
        this.tvVersion = tvVersion;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("PackageUpdateReceiver", "Received package replaced broadcast.");

        if (intent.getAction().equals(Intent.ACTION_PACKAGE_REPLACED)) {
            String packageName = intent.getData().getEncodedSchemeSpecificPart();
            if (packageName.equals(context.getPackageName())) {
                // App package was updated
                String versionName = getAppVersionName(context);
                // Update the TextView with the new version name
                if (tvVersion != null) {
                    tvVersion.setText("Version " + versionName);
                }
            }
        }
    }

    // ...

    // ...



    private String getAppVersionName(Context context) {
        PackageManager packageManager = context.getPackageManager();
        String packageName = context.getPackageName();
        String versionName = "";
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(packageName, 0);
            versionName = packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionName;
    }
}

