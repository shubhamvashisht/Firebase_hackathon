package com.hackathon.poluchecker;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;

import java.net.URLEncoder;

public class MainActivity extends AppCompatActivity {

    public   Context ctx;
    public   Activity act;
    Gson js;

    Firebase fb,user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ctx=this;
        act=this;
        js=new Gson();

        fb=new Firebase(Constants.fire(ctx));
        user=fb.child("userdb").child(utl.refineString(utl.readData(ctx).email,""));
        user.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        utl.toast(ctx,"FCM ONNN");
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        //Log the token
        Log.d("", "Refreshed token: " + refreshedToken);


        user.child("pin").setValue(refreshedToken);
        AlertDialog.Builder adb = new AlertDialog.Builder(this);
        CharSequence items[] = new CharSequence[] {"Delhi", "Mumbai", "Banglore"};
        adb.setSingleChoiceItems(items, 0, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface d, int n) {



            }

        });
        adb.setPositiveButton("ok",null);
        adb.setNegativeButton("Cancel", null);
        adb.setTitle("Where are you ?");
        adb.show();








    }

    void getJson(String city)
    {

        String url=Constants.HOST+"/poluCheck.php?city="+ URLEncoder.encode(city);
        AndroidNetworking.get(url).build().getAsString(new StringRequestListener() {
            @Override
            public void onResponse(String response) {

            }

            @Override
            public void onError(ANError ANError) {

            }
        });





    }


}
