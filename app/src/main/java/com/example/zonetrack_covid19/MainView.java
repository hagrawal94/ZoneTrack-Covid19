package com.example.zonetrack_covid19;
import java.io.File;
import java.util.List;

import com.here.android.mpa.common.ApplicationContext;
import com.here.android.mpa.common.GeoCoordinate;
import com.here.android.mpa.common.MapEngine;
import com.here.android.mpa.common.OnEngineInitListener;
import com.here.android.mpa.search.ErrorCode;
import com.here.android.mpa.search.GeocodeRequest;
import com.here.android.mpa.search.GeocodeResult;
import com.here.android.mpa.search.Location;
import com.here.android.mpa.search.ResultListener;
import com.here.android.mpa.search.ReverseGeocodeRequest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/**
 * This class demonstrates the usage of Geocoding and Reverse Geocoding request APIs
 */
public class MainView {
    private FragmentActivity m_activity;
    private TextView m_resultTextView;

    public MainView(FragmentActivity activity) {
        m_activity = activity;
        initMapEngine();
    }

    private void initMapEngine() {
        // Set path of disk cache
        String diskCacheRoot = m_activity.getFilesDir().getPath()
                + File.separator + ".isolated-here-maps";

        boolean success = com.here.android.mpa.common.MapSettings.setIsolatedDiskCacheRootPath(diskCacheRoot);
        if (!success) {
            // Setting the isolated disk cache was not successful, please check if the path is valid and
            // ensure that it does not match the default location
            // (getExternalStorageDirectory()/.here-maps).
        } else {
            /*
             * Even though we don't display a map view in this application, in order to access any
             * services that HERE Android SDK provides, the MapEngine must be initialized as the
             * prerequisite.
             */
            MapEngine.getInstance().init(new ApplicationContext(m_activity), new OnEngineInitListener() {
                @Override
                public void onEngineInitializationCompleted(Error error) {
                    if (error != Error.NONE) {
                    } else {
                        Toast.makeText(m_activity, "Map Engine initialized without error",
                                Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

//    GeoCoordinate coord = new GeoCoordinate(0,0);
    public void triggerGeocodeRequest(String query) {
        GeocodeRequest geocodeRequest = new GeocodeRequest(query);
        GeoCoordinate coordinate = new GeoCoordinate(23.113544, 78.475860);
        geocodeRequest.setSearchArea(coordinate, 5000);
        geocodeRequest.execute(new ResultListener<List<GeocodeResult>>() {
            @Override
            public void onCompleted(List<GeocodeResult> results, ErrorCode errorCode) {
                if (errorCode == ErrorCode.NONE) {
                    /*
                     * From the result object, we retrieve the location and its coordinate and
                     * display to the screen. Please refer to HERE Android SDK doc for other
                     * supported APIs.
                     */

                    //coord = results.get(0).getLocation().getCoordinate();
                    //Toast.makeText(m_activity, coord.toString(), Toast.LENGTH_SHORT).show();
                } else {
                    //updateTextView("ERROR:Geocode Request returned error code:" + errorCode);
                }
            }
        });
        //return coord;
    }

    String loc = "";

    public String triggerRevGeocodeRequest(double lat, double lang) {
        /* Create a ReverseGeocodeRequest object with a GeoCoordinate. */
        initMapEngine();
        GeoCoordinate coordinate = new GeoCoordinate(lat, lang);
        ReverseGeocodeRequest revGecodeRequest = new ReverseGeocodeRequest(coordinate);
        revGecodeRequest.execute(new ResultListener<Location>() {
            @Override
            public void onCompleted(Location location, ErrorCode errorCode) {
                if (errorCode == ErrorCode.NONE) {
                    /*
                     * From the location object, we retrieve the address and display to the screen.
                     * Please refer to HERE Android SDK doc for other supported APIs.
                     */
                    loc = location.getAddress().toString();
                    Toast.makeText(m_activity, loc, Toast.LENGTH_SHORT).show();
                } else {
                    loc = "ERROR:RevGeocode Request returned error code:" + errorCode;
                }
            }
        });
        return loc;
    }

    private void updateTextView(final String txt) {
        m_activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                m_resultTextView.setText(txt);
            }
        });
    }
}
