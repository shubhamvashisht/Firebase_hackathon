package com.hackathon.poluchecker;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.hackathon.poluchecker.database.OUser;

import org.w3c.dom.Text;

import butterknife.Bind;
import butterknife.ButterKnife;

public class Home extends AppCompatActivity {

    public static Context ctx;
    public static Activity act;

    @Bind(R.id.tmp)
    TextView tmp;

    GenericUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        ctx = this;
        act = this;

        ButterKnife.bind(this);

        user = utl.readData(ctx);
        tmp.setText("Loading...");


        utl.showDig(true);
        startlogin(user.email,user.suserId,0);
        tmp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    public void startlogin(String email,String auth,int isFacebook)
    {

        //http://35.163.210.177/api/login.php?user_email=shiveshnavin@gmail.com&auth=643045572566298


        String url=Constants.HOST+"/api/login.php?" +
                "user_email="+email +
                "&auth="+auth+
                "&facebook="+isFacebook
                ;


        utl.l("login url : "+url);
        AndroidNetworking.get(url).build().getAsString(new StringRequestListener() {
            @Override
            public void onResponse(String response) {
                utl.showDig(false);

                utl.l(response);
                OUser ou=utl.js.fromJson(response, OUser.class);
                user=ou.toGenricUser();
                utl.writeData(user,ctx);

                String txt="";
                txt="<b>"+user.name+"</b><br>"+
                        "E-Mail <b>"+user.email+"</b><br>"+
                        "User Name <b>"+user.username+"</b><br>"+
                        "Group <b>"+user.userGroup+"</b><br>"+
                        "Role <b>"+user.userRole+"</b><br>";



                tmp.setText(Html.fromHtml(txt));



            }

            @Override
            public void onError(ANError ANError) {
                utl.showDig(false);

            }
        });





    }
}
