package com.hackathon.poluchecker;

import android.support.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.Bind;


/**
 * Created by shivesh on 16/6/16.
 */



public class GenericUser {


    public String userCreated;
    @SerializedName("user_role")
    @Expose
    public String userRole;
    @SerializedName("user_group")
    @Expose
    public String userGroup;

    @Nullable @SerializedName("check_out_time")
    @Expose
    public String checkOutTime="";

    @SerializedName("user_status")
    @Expose
    public String userStatus="";

/*********************************************/


    @Nullable
    public String uid="11";
    public String name="UserName";
    @Nullable
    public String profile="Profile URL";
    @Nullable
    public String age="24";
    @Nullable
    public String birth="66-03-1992";
    @Nullable
    public String username="username";
    @Nullable
    public String gender="M";
    @Nullable
    public String address="-";

    @Nullable
    public String country="India";

    @Nullable
    public String city="";


    @Nullable
    public String latlng="28,37";

    @Nullable
    public String pin="110027";

    public String email="example@gmail.com";
    public String password="password";

    @Nullable
    public String phone="91";
    @Nullable
    public String notifications ="";

    @Nullable
    public String image ="";
   @Nullable
    public String dp1 ="";
    @Nullable
    public String dp2 ="";
    @Nullable
    public String dp3 ="";

    @Nullable
    public String isPvt="true";
    @Nullable
    public String work ="Unspecified";

    @Nullable
    public String social="native";
    @Nullable
    public String suserId="101415094322388583253";



    @Override
    public String toString()
    {
        Gson js=new Gson();
        return js.toJson(this).toString();
    }






/*
public  GenericUser(String jstr)
{
    try {
        JSONObject jo = new JSONObject(jstr);
        uid=jo.getString("uid");
        name=jo.getString("name");
        username=jo.getString("username");
        gender=jo.getString("gender");
        email=jo.getString("email");
        password=jo.getString("password");
        points =jo.getString("points");
        previous_purchases=jo.getString("previous_purchases");
        phone=jo.getString("phone");
        notifications=jo.getString("notifications");


    }catch (Exception e)
    {
        e.printStackTrace();
    }


}
*/

    public GenericUser()
    {

    }







}
