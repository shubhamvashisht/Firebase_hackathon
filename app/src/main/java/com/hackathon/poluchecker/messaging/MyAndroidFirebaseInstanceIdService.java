package com.hackathon.poluchecker.messaging;

/**
 * Created by shivesh on 21/2/17.
 */

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.androidnetworking.AndroidNetworking;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

public class MyAndroidFirebaseInstanceIdService extends FirebaseInstanceIdService {

    private static final String TAG = "MyAndroidFCMIIDService";

    public static Context ctx;
    public static Activity act;
    @Override
    public void onTokenRefresh() {
        ctx=getApplicationContext();
        //Get hold of the registration token
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        //Log the token
        Log.d(TAG, "Refreshed token: " + refreshedToken);
        AndroidNetworking.initialize(ctx);


    }
    private void sendRegistrationToServer(String token) {
        //Implement this method if you want to store the token on your server
    }
}
