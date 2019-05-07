package com.death.jitsidemo;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.calendarevents.CalendarEventsPackage;
import com.facebook.react.bridge.UiThreadUtil;

import org.jitsi.meet.sdk.JitsiMeet;
import org.jitsi.meet.sdk.JitsiMeetActivity;
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions;
import org.jitsi.meet.sdk.JitsiMeetView;
import org.jitsi.meet.sdk.JitsiMeetViewListener;
//import org.jitsi.meet.sdk.invite.AddPeopleController;
//import org.jitsi.meet.sdk.invite.AddPeopleControllerListener;
//import org.jitsi.meet.sdk.invite.InviteController;
//import org.jitsi.meet.sdk.invite.InviteControllerListener;

import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * https://github.com/jitsi/jitsi-meet/blob/master/android/README.md
 */
public class MainActivity extends JitsiMeetActivity implements JitsiMeetViewListener {
    private static String TAG = "MainActivity";

    private JitsiMeetView view;
    /**
     * The request code identifying requests for the permission to draw on top
     * of other apps. The value must be 16-bit and is arbitrarily chosen here.
     */
    private static final int OVERLAY_PERMISSION_REQUEST_CODE
            = (int) (Math.random() * Short.MAX_VALUE);

//    @Override
//    public void onBackPressed() {
//        if (!JitsiMeetView.onBackPressed()) {
//            // Invoke the default handler if it wasn't handled by React.
//            super.onBackPressed();
//        }
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //view = new JitsiMeetView(this);

//        Bundle config = new Bundle();
//        config.putBoolean("startWithAudioMuted", false);
//        config.putBoolean("startWithVideoMuted", true);
//        config.putBoolean("startAudioOnly", true);
//        Bundle urlObject = new Bundle();
//        urlObject.putBundle("config", config);
//        urlObject.putString("url", "https://meet.jit.si/k2k2");
//        view.loadURLObject(urlObject);
//
//        view.loadURLString("https://meet.jit.si/k2k2");
        //setContentView(view);
        //view.setListener(this);
        // Use broadcast receiver from the XMPP service, to close this activity once other side user
        // disconnects the call.
    }

    @Override
    protected boolean extraInitialize() {
        // Setup Crashlytics and Firebase Dynamic Links
        // Here we are using reflection since it may have been disabled at compile time.
        try {
            Class<?> cls = Class.forName("org.jitsi.meet.GoogleServicesHelper");
            Method m = cls.getMethod("initialize", JitsiMeetActivity.class);
            m.invoke(null, this);
        } catch (Exception e) {
            // Ignore any error, the module is not compiled when LIBRE_BUILD is enabled.
        }

        // In Debug builds React needs permission to write over other apps in
        // order to display the warning and error overlays.
        if (BuildConfig.DEBUG) {
            if (canRequestOverlayPermission() && !Settings.canDrawOverlays(this)) {
                Intent intent
                        = new Intent(
                        Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:" + getPackageName()));

                startActivityForResult(intent, OVERLAY_PERMISSION_REQUEST_CODE);

                return true;
            }
        }
        return false;
    }
    @Override
    protected void initialize() {
        // Set default options
        JitsiMeetConferenceOptions defaultOptions
                = new JitsiMeetConferenceOptions.Builder()
                .setWelcomePageEnabled(true)
                .setServerURL(buildURL("https://192.168.0.102:7443/ofmeet"))//https://meet.jit.si
                .build();
        JitsiMeet.setDefaultConferenceOptions(defaultOptions);

//        URL url = new URL("https://192.168.0.102:7443/ofmeet");
//        JitsiMeetConferenceOptions options = new JitsiMeetConferenceOptions.Builder()
//                .setServerURL(url)
//                .setRoom("hello")
//                .setAudioMuted(false)
//                .setVideoMuted(false)
//                .setAudioOnly(false)
//                .setWelcomePageEnabled(true)
//                .build();
//        JitsiMeet.setDefaultConferenceOptions(options);





        super.initialize();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == OVERLAY_PERMISSION_REQUEST_CODE
                && canRequestOverlayPermission()) {
            if (Settings.canDrawOverlays(this)) {
                initialize();
                return;
            }

            throw new RuntimeException("Overlay permission is required when running in Debug mode.");
        }

        super.onActivityResult(requestCode, resultCode, data);
    }


    private boolean canRequestOverlayPermission() {
        return
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                        && getApplicationInfo().targetSdkVersion >= Build.VERSION_CODES.M;
    }
    private @Nullable URL buildURL(String urlStr) {
        try {
            return new URL(urlStr);
        } catch (MalformedURLException e) {
            return null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(view != null){
            view.dispose();
        }
        view = null;

        //JitsiMeetView.onHostDestroy(this);
    }

    @Override
    public void onNewIntent(Intent intent) {
        //JitsiMeetView.onNewIntent(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();

        //JitsiMeetView.onHostResume(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        //JitsiMeetView.onHostPause(this);
    }



    @Override
    public void onConferenceJoined(Map<String, Object> map) {
        Log.e(TAG,"onConferenceJoined");
    }

    @Override
    public void onConferenceTerminated(Map<String, Object> map) {
        Log.e(TAG,"onConferenceTerminated");
    }


    @Override
    public void onConferenceWillJoin(Map<String, Object> map) {
        Log.e(TAG,"onConferenceWillJoin");
    }

   }
