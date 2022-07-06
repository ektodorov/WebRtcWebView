package com.example.webrtc;

import android.app.Activity;
import android.webkit.PermissionRequest;
import android.webkit.WebChromeClient;

import java.lang.ref.WeakReference;

public class WebChromeClientT extends WebChromeClient {

    private PermissionRequest mPermissionRequest;
    private WeakReference<Activity> weakActivity;

    public WebChromeClientT(Activity aActivity) {
        weakActivity = new WeakReference<>(aActivity);
    }

    @Override
    public void onPermissionRequest(final PermissionRequest request) {
        Activity activity = weakActivity.get();
        if(activity == null) {
            return;
        }
        //type 1
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                request.grant(request.getResources());
            }
        });

        //type 2
//        request.grant(request.getResources());

        //type 3
//        mPermissionRequest = request;
//        String[] permissionList = new String[]{Manifest.permission.RECORD_AUDIO,
//                Manifest.permission.MODIFY_AUDIO_SETTINGS,
//                Manifest.permission.CAMERA,
//                Manifest.permission.WRITE_EXTERNAL_STORAGE};
//        activity.requestPermissions(permissionList, ConstantsT.REQUEST_CODE_WEBVIEW);

        //type 4
//        String[] PERMISSIONS = {
//                PermissionRequest.RESOURCE_AUDIO_CAPTURE,
//                PermissionRequest.RESOURCE_VIDEO_CAPTURE};
//        request.grant(PERMISSIONS);
    }

    public void onPermissionResult() {
        mPermissionRequest.grant(mPermissionRequest.getResources());
        mPermissionRequest = null;

        //or if we want to grant permission that we know we asked for
//        String[] PERMISSIONS = {
//                PermissionRequest.RESOURCE_AUDIO_CAPTURE,
//                PermissionRequest.RESOURCE_VIDEO_CAPTURE};
//        mPermissionRequest.grant(PERMISSIONS);
    }
}
