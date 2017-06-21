package com.hackathon.poluchecker;

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.util.UUID;

/**
 * Created by shivesh on 24/6/16.
 */
public class Constants {





    //"http://35.163.210.177"
    public static String HOST="http://35.163.210.177";

    public static boolean isPdCancelable=true;
    public static String uid(int l)
    {
        final String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        System.out.println("uuid Full= " + uuid);
        String ret= uuid.substring(0, Math.min(uuid.length(), l));;
        System.out.println("uuid "+l+" = " + ret);
        return ret;
    }



    public static Conf conf;
    public static final String TAG_EMAIL = "email";
    public static final String v = "v1";
    public static final String TAG_LOGIN = "login";
    public static  String folder = "login";
    public static  String datafile = "login";
    private static String FIRE_BASE="https://genricapp.firebaseio.com/";
    public static String fire(Context ctx)
    {
        return Constants.FIRE_BASE+ utl.refineString(ctx.getResources().getString(R.string.app_name),"");
    }


    public static String getFolder(Context ctx)
    {
        folder = Environment.getExternalStorageDirectory().getPath().toString()+"/."+ utl.refineString(ctx.getResources().getString(R.string.app_name),"");
        return folder;
    }

    public static String dataFile(Context ctx)
    {
        folder = Environment.getExternalStorageDirectory().getPath().toString()+"/."+ utl.refineString(ctx.getResources().getString(R.string.app_name),"");

        File file=new File(folder);
        if(!file.exists())
        {
            file.mkdir();
        }
        datafile=folder+"/conf.json";
        return datafile;
    }

    public static String getApp(Context ctx)
    {
        return utl.refineString(ctx.getResources().getString(R.string.app_name),"");
    }
    public static String localData(Context ctx)
    {
        folder = Environment.getExternalStorageDirectory().getPath().toString()+"/."+ utl.refineString(ctx.getResources().getString(R.string.app_name),"");

        File file=new File(folder);
        if(!file.exists())
        {
            file.mkdir();
        }
        datafile=folder+"/data.json";
        return datafile;
    }


    public static class Conf{

        public boolean isProductDigital=false;
        public boolean requireAddress=true;

        public String appname;


    }





}
