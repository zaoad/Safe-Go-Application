package com.example.safego.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.webkit.DownloadListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebStorage;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;

import com.example.safego.R;
import com.example.safego.utils.Commons;
import com.example.safego.utils.Constants;
import com.example.safego.utils.SharedPrefHelper;

public class SafeGoMap extends AppCompatActivity {
    WebView wb;
    String serverUrl = "http://137.184.224.153/90.4086,23.773/90.387334,23.751071";

    String sourceLat;

    String sourceLongi;

    String destinationLat;

    String destinationLongi;

    SharedPrefHelper sharedPrefHelper;

    /**
     * Called when the activity is first created.
     */
    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.safe_go_map);
        sharedPrefHelper = new SharedPrefHelper(getApplicationContext());
        sourceLat = sharedPrefHelper.getStringFromSharedPref(Constants.SOURCE_LATITUDE);
        sourceLongi = sharedPrefHelper.getStringFromSharedPref(Constants.SOURCE_LONGITUDE);
        destinationLat = sharedPrefHelper.getStringFromSharedPref(Constants.DESTINATION_LATITUDE);
        destinationLongi = sharedPrefHelper.getStringFromSharedPref(Constants.DESTINATION_LONGITUDE);
        if(sourceLat==null)
        {
            sourceLat="";
        }
        if(sourceLongi==null)
        {
            sourceLongi="";
        }
        if(destinationLat==null)
        {
            destinationLat="";
        }
        if(destinationLongi==null)
        {
            destinationLongi="";
        }
        if(sourceLat.equals("0.0")||sourceLongi.equals("0.0")|| destinationLat.equals("0.0")||destinationLongi.equals("0.0") )
        {
            Commons.showToast(getApplicationContext(),"Source or destination not valid");
            Intent mySuperIntent = new Intent(getApplicationContext(), PickLocationSafeRoute.class);
            startActivity(mySuperIntent);
            finish();
        }
        else{
            serverUrl = "http://159.223.208.19/"+sourceLongi+","+sourceLat+"/"+destinationLongi+","+destinationLat;

        }
        wb = (WebView) findViewById(R.id.webView1);
        wb.getSettings().setJavaScriptEnabled(true);
        wb.getSettings().setLoadWithOverviewMode(true);
        wb.getSettings().setUseWideViewPort(true);
        wb.getSettings().setBuiltInZoomControls(true);
        wb.getSettings().setPluginState(WebSettings.PluginState.ON);
        Log.d("server url", serverUrl);
        wb.loadUrl(serverUrl);

        wb.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;
            }
        });

        wb.setDownloadListener(new DownloadListener() {
            public void onDownloadStart(String url, String userAgent,
                                        String contentDisposition, String mimetype,
                                        long contentLength) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });
        WebSettings settings = wb.getSettings();
        settings.setDatabaseEnabled(true);
        settings.setDomStorageEnabled(true);
        String databasePath = this.getApplicationContext().getDir("database", Context.MODE_PRIVATE).getPath();
        settings.setDatabasePath(databasePath);
        wb.setWebChromeClient(new WebChromeClient() {
            public void onExceededDatabaseQuota(String url, String databaseIdentifier, long currentQuota, long estimatedSize, long totalUsedQuota, WebStorage.QuotaUpdater quotaUpdater) {
                quotaUpdater.updateQuota(5 * 1024 * 1024);
            }
        });
    }
}
