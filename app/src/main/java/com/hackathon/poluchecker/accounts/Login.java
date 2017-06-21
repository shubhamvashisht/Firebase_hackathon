package com.hackathon.poluchecker.accounts;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.StringRequestListener;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Arrays;

import com.google.gson.Gson;
import com.hackathon.poluchecker.Constants;
import com.hackathon.poluchecker.GenericUser;
import com.hackathon.poluchecker.Home;
import com.hackathon.poluchecker.MainActivity;
import com.hackathon.poluchecker.R;
import com.hackathon.poluchecker.utils.ValidateUserInfo;
import com.hackathon.poluchecker.utl;

public class Login extends AppCompatActivity {

    View logins;

    public static Context ctx;
    public static Activity act;


    /* Request code used to invoke sign in user interactions. */
    private static final int RC_SIGN_IN = 0;
    /* Client used to interact with Google APIs. */
    private GoogleApiClient mGoogleApiClient,mGoogleApiClient2;

    /* Is there a ConnectionResult resolution in progress? */
    private boolean mIsResolving = false;
    /* Should we automatically resolve ConnectionResults when possible? */
    private boolean mShouldResolve = false;


    private SignInButton mPlusSignInButton;
    private Button mEmailSignInButton;

    private TextView txt_create, txt_forgot;
    //private LoginButton facebookLoginButton;

    private CallbackManager callbackManager;
    private AccessTokenTracker accessTokenTracker;
    private ProfileTracker profileTracker;


    private FacebookCallback<LoginResult> callback = new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {
            Profile profile = Profile.getCurrentProfile();
            Log.d("deb11",""+profile.toString());
            // nextActivity(profile);
        }
        @Override
        public void onCancel() {        }
        @Override
        public void onError(FacebookException e) {      }
    };
    private SurfaceHolder holder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        } catch (Exception e) {
            e.printStackTrace();
        }
        FacebookSdk.sdkInitialize(getApplicationContext());
        Firebase.setAndroidContext(getApplicationContext());

        AppEventsLogger.activateApp(this);

        setContentView(R.layout.activity_login);
        ctx=this;
        act=this;
        //surfaceV();
        initViews();
        initLogins();


      //  logins.setVisibility(View.GONE);
       // YoYo.with(Techniques.SlideInUp).duration(2000).playOn(logins);
        utl.setShared(this);
        if(utl.readData(this)!=null)
        {
            startActivity(new Intent(this,MainActivity.class));
            finish();
        }
       // utl.SlideUP(logins,ctx);


        fbl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                loginButton.performClick();



            }
        });
        gl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // signIn();
                signIn();
             //   mPlusSignInButton.performClick();


            }
        });
        fbl.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {


                loginButton.performLongClick();
                return  true;
            }
        });
        gl.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                utl.toast(ctx,"Signed Out");
                signOut();
               // mPlusSignInButton.performLongClick();
                return  true;
            }
        });








    }


    LoginButton loginButton;
    ImageView fbl,gl;
    public void initViews()
    {

        logins=(View)findViewById(R.id.logins);
        fbl=(ImageView)findViewById(R.id.fbl);;
        gl=(ImageView)findViewById(R.id.gl);
        mPlusSignInButton = (SignInButton) findViewById(R.id.g_sign_in_button);
        mPlusSignInButton.setSize(SignInButton.SIZE_WIDE);
        loginButton = (LoginButton)findViewById(R.id.facebook_sign_in_button);




    }

    public void initLogins()
    {
        //Google+ Login

        /******************************************************************************/
        //Google Login

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(ConnectionResult connectionResult) {

                        utl.toast(ctx,"Failed..");
                    }
                })
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();


        mPlusSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // utl.toast(ctx,"Sign In using Google");


              signIn();


            }
        });

        mPlusSignInButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                utl.toast(ctx,"Signed Out");
                signOut();
                return  true;
            }
        });



        /******************************************************************************/
        //Facebook Login
        callbackManager=CallbackManager.Factory.create();
  /*     facebookLoginButton = (LoginButton)findViewById(R.id.facebook_sign_in_button);
//, email, user_birthday, user_friends/*
     facebookLoginButton.setReadPermissions(Arrays.asList("public_profile"));
*/


        loginButton.setReadPermissions(Arrays.asList("public_profile", "email", "user_birthday"));
        //LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile", "user_friends"));
        // Callback registration
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code
                Log.e("onSuccess", "--------" + loginResult.getAccessToken());
                Log.e("Token", "--------" + loginResult.getAccessToken().getToken());
                Log.e("Permision", "--------" + loginResult.getRecentlyGrantedPermissions());
                Profile profile = Profile.getCurrentProfile();
           //     Log.e("ProfileDataNameF", "--" + profile.toString());
             //   Log.e("ProfileDataNameL", "--" + profile.getLastName());
//
           //     Log.e("Image URI", "--" + profile.getLinkUri());
//
                GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {

                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        Log.i("LoginActivity", response.toString());
                        // Get facebook data from login
                        try {
                            user = getFacebookData(object);
                            Log.d("BUNDLE",""+user.toString());

                            sighup();



                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
                Bundle parameters = new Bundle();
                // parameters.putString("fields", "id, first_name, last_name, email,gender, birthday, location"); // Parámetros que pedimos a facebook
                parameters.putString("fields", "id, first_name, last_name, email,gender, birthday, location"); // Parámetros que pedimos a facebook
                request.setParameters(parameters);
                request.executeAsync();

            }

            @Override
            public void onCancel() {
                // App code
                System.out.println("onCancel");
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
                System.out.println("onError");
                Log.v("LoginActivity", ""+exception.toString());
            }
        });


        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

    }


    private GenericUser getFacebookData(JSONObject object) throws JSONException {

        try {
            Bundle bundle = new Bundle();
            String id = object.getString("id");

            try {
                URL profile_pic = new URL("https://graph.facebook.com/" + id + "/picture?width=256&height=256");
                Log.i("profile_pic", profile_pic + "");
                bundle.putString("profile_pic", profile_pic.toString());

            } catch (MalformedURLException e) {
                e.printStackTrace();
                return null;
            }

            if(user==null)
                user=new GenericUser();

            user.suserId =id;
            user.image="https://graph.facebook.com/" + id + "/picture?width=256&height=256";
            user.password="facebook";
            user.social="facebook";

            ;
            user.uid= ""+System.currentTimeMillis();


            bundle.putString("idFacebook", id);
            if (object.has("first_name"))
                user.name= ( object.getString("first_name"));
            if (object.has("last_name"))
                user.name=user.name+" "+( object.getString("last_name"));
            if (object.has("email"))
                user.email=( object.getString("email"));
            if (object.has("gender"))
                user.gender=object.getString("gender");
            if (object.has("birthday"))
                user.birth=( object.getString("birthday"));
            if (object.has("location"))
                user.address=( object.getJSONObject("location").getString("name"));

            Log.d("USER IS",""+user.toString());
            return user;
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        return  null;
    }


    private void signIn() {
        utl.showDig(true,ctx);
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void signOut() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        //tv_username.setText("");
                    }
                });
    }

    /////////////////////////////////////////?GOGOOOOOOOOOOOOOOOOOOOOOOOOGLEEEEEEEEEEEE///////////////////////////////
    private void handleSignInResult(GoogleSignInResult result) {

        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();


            Log.d("acct",""+acct.toString());

            try {

                user = new GenericUser();
                user.suserId = acct.getId();
                user.email = acct.getEmail();
                user.name = acct.getDisplayName();
                user.social="google";
                try {

                    /// user.imageurl = acct.getPhotoUrl().toString();
                }catch (Exception e)
                {

                    e.printStackTrace();
                }
                user.password="google";
                user.phone="null";

                String imur="https://www.googleapis.com/plus/v1/people/"+user.suserId +"?fields=image&key=AIzaSyC-c-N3bL7QGrChpW3PeOZSmAlRNir5zsY";

                utl.log(""+imur);
                showDig(true);

                AndroidNetworking.get(imur).build().getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {

                        showDig(false);
                        Log.d("Image API"," "+response);


                        try{
                            JSONObject k=new JSONObject(response);
                            JSONObject i=k.getJSONObject("image");
                            user.image=i.getString("url") .replace("sz=50","sz=250");
                            user.uid= ""+System.currentTimeMillis();

                            sighup();

                        }catch (Exception e)
                        {
                            e.printStackTrace();
                        }


                    }

                    @Override
                    public void onError(ANError ANError) {


                        showDig(false);
                        Log.d("Image API"," "+ANError.getErrorDetail());

                        try{
                            user.image="https://www.iconfinder.com/icons/1564534/download/png/128";
                            user.uid= ""+System.currentTimeMillis();

                            sighup();

                        }catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                    }
                });





            }catch (Exception e){
                e.printStackTrace();
            }
            Toast.makeText(ctx,"welcome "+user.name, Toast.LENGTH_LONG).show();



        } else {
            // Signed out, show unauthenticated UI.
            // updateUI(false);
        }
    }


    /********************************REGISTRATION*****************************************************/



    public void sendSms(String message)
    {



        String smsurl="http://sandeshlive.in/API/WebSMS/Http/v1.0a/index.php?username=ctmart&password=orissa123&sender=CTMART&to="+URLEncoder.encode(user.phone)+"&message="+URLEncoder.encode(message)+"&reqid=1&format=json&route_id=23";
        ;
        String  ph=user.phone;
        if(ph.length()==10&&!ph.contains("+91"))
        {
            ph="+91"+ph;
        }
        smsurl="http://sandeshlive.in/API/WebSMS/Http/v1.0a/index.php?username=ctmart&password=orissa123&sender=CTMART&to="+ URLEncoder.encode(ph)+"&message="+ (message)+"&reqid=1&format=json&route_id=23";

        utl.log("SENDING LINK "+smsurl+"\n"+user.phone);
        AndroidNetworking.initialize(ctx);
        AndroidNetworking.get(smsurl).build().getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject response) {
                utl.log(""+response);

            }

            @Override
            public void onError(ANError ANError) {
                utl.log(""+ANError.getErrorDetail());

            }
        });


    }

    public void getPhone()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
        builder.setTitle("Enter Your Phone.");
        builder.setView(R.layout.dialog_input);
        builder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked OK button

                EditText button=(EditText)dig.findViewById(R.id.search_et);
                final String phone=button.getText().toString();
                if(ValidateUserInfo.isPhoneValid(phone)) {
                    user.phone=phone;
                    newUser();
                }
                else {
                    getPhone();
                }

                dialog.dismiss();


            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog

                dialog.dismiss();;
            }
        });
        dig = builder.create();
        dig.show();



    }
    Firebase fireUsers;Firebase  fireUser;
    public void newUser()
    {


        if(user!=null)
        {
/*
            final Firebase sms=new Firebase(Constants.fire(ctx)+"/sms");
            sms.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    String ms=dataSnapshot.getValue(String.class);
                    ms=ms.replace("<name>",user.name);
                    utl.log("SENDING SMS TO "+ms+"\n"+user.name);
                    if(!ms.contains("<disablesms>"))
                    sendSms(ms);
                    sms.removeEventListener(this);
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {

                    sms.removeEventListener(this);
                }
            });*/



        }



        fireUser  =new Firebase(Constants.fire(this)+"/userdb");
        fireUser.child(utl.refineString(user.email,"")).setValue(user);
        fireUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                utl.log("SUCESS IN CREATING NEW USER");
                fireUsers.child(utl.refineString(user.email,"")).setValue(user.password);

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

                System.out.println("The read failed: " + firebaseError.getMessage());
            }
        });
    }


    public void sighupFirebase()
    {
        fireUsers =new Firebase(Constants.fire(this)+"/userlist");
        showDig(true);

        fireUsers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                boolean notfound=true;
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {


                    try {
                        if(postSnapshot.getKey().equalsIgnoreCase(utl.refineString(user.email,"")))
                        {
                            notfound=false;


                            showDig(false);

                            utl.log("ALREADY PRESENT "+postSnapshot.getValue());
                            Intent iz=new Intent(Login.this, MainActivity.class);
                            iz.putExtra("email",postSnapshot.getKey());
                            if(
                                    utl.writeData(user,ctx)){
                                if(getIntent().getBooleanExtra("comeback",false)==false)
                                    startActivity(iz);
                                finish();}
                            break;

                        }
                    }catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                    Log.d("RES",postSnapshot.toString());



                }
                if(notfound)
                {
                    getPhone();;
                    // fireUser.child(utl.refineString(user.email,"")).setValue(user);
                }
            }
            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("The read failed: " + firebaseError.getMessage());
            }
        });



    }
    GenericUser user;




    public void sighup()
    {


        sighupFirebase();
        return;
        //  utl.diag(act,"Welcome ! ","Hi ! "+user.name+" Login Was Successful !");
       //startlogin(user.email,user.suserId,user.social.equalsIgnoreCase("facebook")?1:0);
//emailLogin(false,false);

    }

    public void getPassword()
    {

        AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
        builder.setTitle("Create Password");
        builder.setView(R.layout.dialog_input);
        builder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked OK button

                EditText button=(EditText)dig.findViewById(R.id.search_et);
                final String phone=button.getText().toString();
                if( (""+phone).length()>4) {
                    user.password=phone;


                    //register(user);




                }
                else {
                    getPassword();
                }

                dialog.dismiss();


            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog

                dialog.dismiss();;
            }
        });
        dig = builder.create();
        dig.show();

    }


    AlertDialog dig;Profile profile;
    public String noOnce=null;
    /*************************EMAIL LOGIN**************************************/
    View v=null;


    public void emailLogin(final boolean isInvalid,final boolean alreadyRegistered){

      AlertDialog.Builder di;
        if(!isInvalid||v==null){

            if(dig!=null)
            {
                if(dig.isShowing())
                    dig.dismiss();
            }
            v=getLayoutInflater().inflate(R.layout.dig_reg,null);
            di = new AlertDialog.Builder(ctx);
            di.setView(v);
            dig=di.create();
            dig.show();

        }
        if(v!=null) {
            final TextView register = (TextView) v.findViewById(R.id.register);
            final TextView log = (TextView) v.findViewById(R.id.log);
            final EditText username = (EditText) v.findViewById(R.id.username);
            final EditText email = (EditText) v.findViewById(R.id.email);
            final EditText fname = (EditText) v.findViewById(R.id.fname);
            final EditText password = (EditText) v.findViewById(R.id.password);
            final EditText password2 = (EditText) v.findViewById(R.id.password2);
            final Button login = (Button) v.findViewById(R.id.login);

            email.setText(user.email);
            fname.setText(user.name);


            final View reg = v.findViewById(R.id.reg);
            final View scrl = v.findViewById(R.id.scrl);
            final View load = v.findViewById(R.id.load);

            scrl.setVisibility(View.VISIBLE);
            load.setVisibility(View.GONE);
            reg.setVisibility(View.GONE);



            register.setVisibility(View.VISIBLE);

            reg.setVisibility(View.VISIBLE);
            login.setText("REGISTER");
            log.setText("REGISTER");
            register.setText(" ");
            register.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                  //  emailLogin(false,false);

                }
            });
            login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                     String uname=username.getText().toString();
                    String em=email.getText().toString();
                    String fn=fname.getText().toString();
                    String ps1=password.getText().toString();
                    String ps2=password2.getText().toString();

                    user.name=fn;
                    user.username=uname;
                    user.password=ps1;
                    user.email=em;
                    if(user.password.equals(ps2))
                    {
                        register(user);
                        scrl.setVisibility(View.GONE);
                        load.setVisibility(View.VISIBLE);
                    }
                    else {
                        password.setError("Password donot match !");
                    }




                }
            });


            register.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });

           /* login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    String ps1=password.getText().toString();
                    String em=email.getText().toString();

                    scrl.setVisibility(View.GONE);
                    load.setVisibility(View.VISIBLE);

                    startlogin(em,ps1,0);

                }
            });

*/
            Dialog dig;

            if(isInvalid)
            {
                password.setError("Invalid Credentials !");
            }


            if(alreadyRegistered)
            {
                email.setError("Username or E-Mail already registered !");
            }





        }




    }





    /****************************RESTFUL*************************************************/
    public void getNoOnce()
    {
        //http://feelinglone.com/api/get_nonce/?controller=user&method=register
        String url=Constants.HOST+"/api/get_nonce/?controller=user&method=register";
        AndroidNetworking.get(url).build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            noOnce=response.getString("nonce");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError ANError) {

                        utl.l("err 543"+ANError.getErrorBody());
                    }
                });

    }

    ;
    public void register( final GenericUser tmpusr)
    {
//   http://127.0.0.1/users/api/createuser.php?
// user_name=shiveshnavin&user_fname=shivesh%20navin
// &user_password=pass&user_email=shiveshnavin@gmail.com
// &user_role=ADMIN&user_group=1002&user_status=PENDING




        boolean isfb=tmpusr.social.equalsIgnoreCase("facebook");
        String url=Constants.HOST+"/api/createuser.php?"
                +
                "user_name="+ URLEncoder.encode(tmpusr.username) +
                "&user_email="+ URLEncoder.encode(tmpusr.email) +
                "&user_fname="+ URLEncoder.encode(tmpusr.name) +
                "&user_password="+ URLEncoder.encode(tmpusr.password) +
                "&user_image="+ URLEncoder.encode(tmpusr.image) +
                "&user_status="+ URLEncoder.encode("PENDING") +
                "&user_role="+ URLEncoder.encode("USER") +
                "&auth="+ URLEncoder.encode(tmpusr.suserId) +
                "&user_group="+ URLEncoder.encode("10001");



        utl.l("reg url : "+url);

        AndroidNetworking.get(url).build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        response=""+response;
                        utl.l("reg resp "+response);
                        if(response.contains("error"))
                        {

                            emailLogin(false,true);

                        }
                        else {

                            startlogin(tmpusr.email,tmpusr.suserId,tmpusr.social.equalsIgnoreCase("facebook")?1:0);
                        }

                    }

                    @Override
                    public void onError(ANError ANError) {

                        utl.e("err 566"+ANError.getErrorBody());
                    }
                });

    }


    Gson js=new Gson();
    public void startlogin(String email,String auth,int isFacebook)
    {

        //http://35.163.210.177/api/login.php?user_email=shiveshnavin@gmail.com&auth=643045572566298


        String url=Constants.HOST+"/api/login.php?" +
                "user_email="+email +
                "&auth="+auth+
                "&facebook="+isFacebook
                ;


         utl.l("login url : "+url);
        AndroidNetworking.get(url)
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        try {   utl.l("login resp "+response);

                            if(response.contains("error"))
                            {
                                JSONObject jsonObject=new JSONObject(response);
                                emailLogin(false,false);
                               // utl.snack(act,""+jsonObject.getString("error"));
                            }
                            else {
                                if(dig!=null)
                                {
                                    dig.dismiss();;
                                }
                                com.hackathon.poluchecker.database.OUser oUser;
                                oUser=js.fromJson(response, com.hackathon.poluchecker.database.OUser.class);
                                user=oUser.toGenricUser();
                                utl.writeData(user,ctx);
                                startActivity(new Intent(ctx,Home.class));
                            }

                        } catch (Exception e) {

                            if(dig!=null)
                            {
                                dig.dismiss();;
                            }

                            //utl.snack(  act,"Invalid Credentials !"  );
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError ANError) {
                        utl.l("reg err "+ANError.getErrorDetail());
                        if(dig!=null)
                        {
                            dig.dismiss();;
                        }
                        emailLogin(false,false);
                    }
                });


    }






    /*************************************************************************************/




    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, requestCode, data);
        //Facebook login

        utl.showDig(false,ctx);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            try {
                handleSignInResult(result);
            } catch (Exception e) {
                utl.snack(act,"Google Signin Failed ! Please update Google Play Services on our Phone !");
                e.printStackTrace();
            }
        } else {
            // callbackManager.onActivityResult(requestCode, resultCode, data);

            if (callbackManager.onActivityResult(requestCode, resultCode, data)) {
                return;
            }

        }

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {

            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
              //  populateAutoComplete();

        }
    }

    /**
     * Shows the progress UI and hides the login form.
     */

    public void getUser(final String email){

        final Firebase detail=new Firebase(Constants.fire(ctx)+"/userdb/"+utl.refineString(email,""));
        detail.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                showProgress(false);

                if(dataSnapshot!=null) {
                    detail.removeEventListener(this);
                    user=dataSnapshot.getValue(GenericUser.class);
                    utl.writeData(user,ctx);
                    Intent iz=new Intent(Login.this, Home.class);
                    iz.putExtra("email", email);
                    if(getIntent().getBooleanExtra("comeback",false)==false)
                        startActivity(iz);
                    finish();

                }





            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });



    }



    public void showProgress(boolean tt)
    {
utl.showDig(tt,ctx);
    }
    public void   showDig(boolean tt)
    {
        utl.showDig(tt,ctx);
    }



}
