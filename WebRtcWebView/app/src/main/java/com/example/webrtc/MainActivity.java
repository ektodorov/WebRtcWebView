package com.example.webrtc;

import android.Manifest;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Gravity;
import android.view.View;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final String LOG = "MainActivity";

    private Toolbar mToolbar;
    private DrawerLayout mDrawerLayout;
    private ListView mListViewMenu;
    private WebView mWebViewMain;

    private ListAdapterMenu mAdapterMenu;
    private WebViewClientTemplate mWebViewClient;
    private WebChromeClientT mWebChromeClient;
    private SharedPreferences mPrefs;
    private ArrayList<MenuItemT> mArrayMenuItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mToolbar = (Toolbar)findViewById(R.id.toolbarMain);
        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawerLayoutMain);
        mListViewMenu = (ListView)findViewById(R.id.listViewMenuMain);
        mWebViewMain = (WebView)findViewById(R.id.webViewMain);
        ProgressBar progressBar = (ProgressBar)findViewById(R.id.progressBar);

        mToolbar.setTitle(R.string.app_name);

        mPrefs = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        PrefsHelper prefsHelper = PrefsHelper.getInstance();
        prefsHelper.init(mPrefs, getResources());

        mArrayMenuItems = prefsHelper.getMenuItems(mPrefs);
        mAdapterMenu = new ListAdapterMenu(MainActivity.this, mArrayMenuItems);
        mListViewMenu.setAdapter(mAdapterMenu);
        mListViewMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mWebViewMain.loadUrl(mArrayMenuItems.get(position).getUrl());
                mAdapterMenu.setSelectedItem(position);
                mDrawerLayout.closeDrawer(Gravity.LEFT);
                PrefsHelper.getInstance().setMenuSelectedPosition(mPrefs, position);
            }
        });
        mListViewMenu.setBackgroundColor(prefsHelper.getMenuBackgroundColor(mPrefs));

        mWebViewClient = new WebViewClientTemplate(progressBar);
        mWebChromeClient = new WebChromeClientT(this);

        mWebViewMain.getSettings().setJavaScriptEnabled(true);
        mWebViewMain.getSettings().setMediaPlaybackRequiresUserGesture(false);
        mWebViewMain.setWebViewClient(mWebViewClient);
        mWebViewMain.setWebChromeClient(mWebChromeClient);
        mWebViewMain.loadUrl(mArrayMenuItems.get(0).getUrl());
        mAdapterMenu.setSelectedItem(0);
    }

    @Override
    public void onResume() {
        super.onResume();
        checkPermissions();
    }

    private void checkPermissions() {
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
//            requestPermissions(new String[]{Manifest.permission.RECORD_AUDIO}, ConstantsT.REQUEST_CODE_AUDIO);
//            return;
//        }
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.MODIFY_AUDIO_SETTINGS) != PackageManager.PERMISSION_GRANTED) {
//            requestPermissions(new String[]{Manifest.permission.MODIFY_AUDIO_SETTINGS}, ConstantsT.REQUEST_CODE_AUDIO_SETTINGS);
//            return;
//        }
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
//            requestPermissions(new String[]{Manifest.permission.CAMERA}, ConstantsT.REQUEST_CODE_CAMERA);
//            return;
//        }
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, ConstantsT.REQUEST_CODE_WRITE_STORAGE);
//        }

        String[] permissionList = new String[]{Manifest.permission.RECORD_AUDIO,
                Manifest.permission.MODIFY_AUDIO_SETTINGS,
                Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE};
        requestPermissions(permissionList, ConstantsT.REQUEST_CODE_WRITE_STORAGE);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.app_name, R.string.app_name);
        mDrawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
    }

    @Override
    public void onBackPressed() {
        if(mDrawerLayout.isDrawerOpen(Gravity.LEFT)) {
            mDrawerLayout.closeDrawer(Gravity.LEFT);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch(requestCode) {
            case ConstantsT.REQUEST_CODE_WRITE_STORAGE:
            case ConstantsT.REQUEST_CODE_AUDIO:
            case ConstantsT.REQUEST_CODE_AUDIO_SETTINGS:
            case ConstantsT.REQUEST_CODE_CAMERA:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                }
                break;
            case ConstantsT.REQUEST_CODE_WEBVIEW:
                mWebChromeClient.onPermissionResult();
                break;
            default:
                //do nothing
        }
    }
}
