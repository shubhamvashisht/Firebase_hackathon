
package com.hackathon.poluchecker.database;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.hackathon.poluchecker.GenericUser;


public class OUser {


    @SerializedName("user_name")
    @Expose
    public String userName;
    @SerializedName("user_fname")
    @Expose
    public String userFname;
    @SerializedName("user_password")
    @Expose
    public String userPassword;
    @SerializedName("auth")
    @Expose
    public String auth;
    @SerializedName("user_email")
    @Expose
    public String userEmail;
    @SerializedName("user_image")
    @Expose
    public String userImage;
    @SerializedName("user_created")
    @Expose
    public String userCreated;
    @SerializedName("user_role")
    @Expose
    public String userRole;
    @SerializedName("user_group")
    @Expose
    public String userGroup;
    @SerializedName("user_status")
    @Expose
    public String userStatus;
    @SerializedName("user_id")
    @Expose
    public String userId;



    public GenericUser toGenricUser(String password)
    {
        GenericUser user=new GenericUser();

        user.username=this.userName;
        user.name=this.userFname;
        user.email=this.userEmail;
        user.image=this.userImage;;
        user.email=password;
        user.uid=this.userId;
        user.password=password;


        user.userStatus=this.userStatus;
        user.userCreated=this.userCreated;
        user.userRole=this.userRole;
        user.userGroup=this.userGroup;

        user.suserId=this.auth;

        return user;
    }


    public GenericUser toGenricUser()
    {
        GenericUser user=new GenericUser();
        user.name=this.userFname;
        user.email=this.userEmail;
        user.image=this.userImage;;
        user.username=this.userName;
        user.uid=this.userId;
        user.password=this.userPassword;

        user.suserId=this.auth;

        user.userStatus=this.userStatus;
        user.userCreated=this.userCreated;
        user.userRole=this.userRole;
        user.userGroup=this.userGroup;

        return user;
    }




}
