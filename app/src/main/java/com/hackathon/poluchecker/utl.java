package com.hackathon.poluchecker;

import android.app.Activity;
import android.app.Dialog;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.facebook.login.LoginManager;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.nineoldandroids.animation.Animator;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


import static android.content.Context.MODE_PRIVATE;

/**
 * Created by shivesh on 8/4/16.
 */
public class utl {




    public static boolean DISPLAY_ENABLED=true;
    public static boolean DEBUG_ENABLED=true;
    public static String CITY_DEBUG=null;//"delhi"
    public static String CITY_DEF="-";//
    public static String ACTION_FINISH="nf.co.hoptec.ayi.close";


    public static String getRealPathFromUri(Context ctx,Uri uri) {
        String result = "";
        String documentID;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            String[] pathParts = uri.getPath().split("/");
            documentID = pathParts[pathParts.length - 1];
        } else {
            String pathSegments[] = uri.getLastPathSegment().split(":");
            documentID = pathSegments[pathSegments.length - 1];
        }
        String mediaPath = MediaStore.Images.Media.DATA;
        Cursor imageCursor = ctx.getContentResolver().query(uri, new String[]{mediaPath}, MediaStore.Images.Media._ID + "=" + documentID, null, null);
        if (imageCursor.moveToFirst()) {
            result = imageCursor.getString(imageCursor.getColumnIndex(mediaPath));
        }
        return result;
    }










    public static  ProgressDialog pd;
    public static void pd(boolean show)
    {
        l("PD : "+show);
        if(pd==null) {
            pd = new ProgressDialog(ctx);
            pd.setCancelable(Constants.isPdCancelable);
        }pd.setMessage("Loading..");
        if(show) {
            if (!pd.isShowing())
                pd.show();
        }else
            pd.dismiss();
    }


    public static void pd(boolean show,String msg)
    {
        l("PD : "+show);
        if(pd==null) {
            pd = new ProgressDialog(ctx);
            pd.setCancelable(Constants.isPdCancelable);
        }pd.setMessage(""+msg);
        if(show)
            pd.show();
        else
            pd.dismiss();
    }




    public static final String MY_PREFS_NAME = "wootwoot";
    public static  SharedPreferences.Editor editor;// = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();

    public static void setShared(Context ctx)
    {
        /*
        editor.putString("name", "Elena");
        editor.putInt("idName", 12);
        editor.commit();*/


        SharedPreferences prefs = ctx.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        String restoredText = prefs.getString("installed", null);
        if (restoredText != null) {

            String name = prefs.getString("installed", "true");//"No name defined" is the default value.
      //      avenir-next-regular
         //   Avenir-Medium
            Log.d("INSTALL "," ALREADY INSTALL ");
        }
        else {
            Log.d("INSTALL "," FIRST INSTALL ");
            File folder=new File(Constants.getFolder(ctx));
            if(folder.exists())
            {
                utl.log("INSTALL : Deleting folder");
                utl.deleteDir(folder);
            }
            editor = ctx.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
            editor.putString("installed", "true");

            editor.commit();
        }

    }




    public static boolean match(String parent,String query)
    {
        boolean match=false;
        if(parent.toLowerCase().contains(query.toLowerCase()))
        {
            match=true;
        }


        return match;
    }



    public static void SlideDownHide(final View view,Context context)
    {
        YoYo.with(Techniques.SlideOutDown).duration(1600).withListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {

                view.setVisibility(View.GONE);

            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        }).playOn(view);
      //  view.startAnimation(AnimationUtils.loadAnimation(context, R.anim.slid_down_t_hide));
    }




    public static int getH(Context ctx)
    {
        WindowManager windowManager = (WindowManager)ctx.getSystemService(Context.WINDOW_SERVICE);
       return windowManager.getDefaultDisplay().getHeight();

    }


    public static int getW(Context ctx)
    {
        WindowManager windowManager = (WindowManager)ctx.getSystemService(Context.WINDOW_SERVICE);
         return windowManager.getDefaultDisplay().getWidth();

    }


    public  static void SlideUP(View view, Context context)
    {
        view.startAnimation(AnimationUtils.loadAnimation(context,
                R.anim.slid_up));
    }

    public static void SlideDown(View view,Context context)
    {
        view.startAnimation(AnimationUtils.loadAnimation(context,
                R.anim.slid_down));
    }


    private static  boolean isValidMobile(String phone)
    {
        return android.util.Patterns.PHONE.matcher(phone).matches();
    }

    public static  boolean removeData(Context ctx)
    {
        String data= Constants.dataFile(ctx);
        File f=new File(data);
        f.delete();
        return  true;
    }

    public static  boolean writeData(GenericUser guser,Context ctx)
    {
        String data= Constants.dataFile(ctx);
        FileOperations fop=new FileOperations();
        Gson g=new Gson();
        fop.write(data,g.toJson(guser));
        Log.d("DATA WROTE","");
        //Log.d("DATA WROTE",""+fop.read(data));
        return  true;
    }

    public static boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (int i=0; i<children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }

        // The directory is now empty so delete it
        return dir.delete();
    }


    public static void logout( Context ctx)
    {
        String data= Constants.dataFile(ctx);
        if( new File(data).exists())
            new File(data).delete();

        try {
            LoginManager.getInstance().logOut();;
        }catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    public static String chatRoom(String mEmail,String bEmail)
    {
        String cr;
        cr=utl.refineString(mEmail+"_"+bEmail,"");
        return  cr;

    }

/*

Intent intent = new Intent( this , ChatActivity. class );
            String  cz2=remoteMessage.getData().get("req");

            if(cz2!=null)
            {
                intent = new Intent( this , ChatListActivity. class );
            }
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP| Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
            PendingIntent resultIntent = PendingIntent.getActivity( this , 0, intent,
                    PendingIntent.FLAG_ONE_SHOT);


 */
    public static void notify(Context ctx, PendingIntent resultIntent,String title, String text)
    {
        Uri notificationSoundURI = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        Notification.Builder b = new Notification.Builder(ctx)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(""+title)
                //.setColor(getResources().getColor(R.color.green_400))
                .setContentText(""+text)
                .setAutoCancel( true )
                .setSound(notificationSoundURI)
                .setContentInfo("Wootout")
                .setContentIntent(resultIntent);


        Notification n = NotificationExtras.buildWithBackgroundColor(ctx, b, ctx.getResources().getColor(R.color.green_400));
        // Notification n = NotificationExtras.buildWithBackgroundResource(this, b, R.drawable.bg_notification);
        NotificationManagerCompat.from(ctx).notify(1, n);



    }




    public static GenericUser readData( Context ctx)
    {
        String data= Constants.dataFile(ctx);
        if(!new File(data).exists())
            return null;
        FileOperations fop=new FileOperations();
        Gson g=new Gson();
        try {
            Log.d("DATA READ","");
            //Log.d("DATA READ",""+fop.read(data));
            GenericUser guser=g.fromJson(fop.read(data),GenericUser.class);
            return  guser;


        } catch (JsonSyntaxException e) {

            e.printStackTrace();

            return  null;
        }
    }




    public static int calculateNoOfColumns(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        int noOfColumns = (int) (dpWidth / 180);
        return noOfColumns;
    }

    public static class Util
    {

        public   Dialog dialog;
        public   void showDig(boolean show,Context ctx)
        {
            try {
                if(show)
                {

                    utl.log("Util.DIAG_OPEN : "+ctx.getClass());
                    dialog = new Dialog(ctx);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.gen_load);
                    final Window window = dialog.getWindow();
                    window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
                    window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                    window.setBackgroundDrawable(new ColorDrawable(Color.GRAY));
                    dialog.getWindow().getAttributes().alpha = 0.7f;



                    dialog.setTitle("Select Content Language");

                    dialog.setContentView(R.layout.gen_load);
                    //  dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialog.setCanceledOnTouchOutside(true);
                    dialog.setCancelable(true);
                    dialog.show();

                }
                else   {



                    utl.log("Util.DIAG_CLOSE : "+ctx.getClass());
                    if(dialog!=null)
                        if(dialog.isShowing())
                        {
                            dialog.dismiss();
                        }

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public static void showDig(boolean show,Context ctx) {

    }

    public static void showDig(boolean show )
    {

    }


   public static Dialog dialog;
    public static void showDig1(boolean show,Context ctx)
    {
        try {
            if(dialog!=null)
                if(dialog.isShowing()&&show)
                {
                    return;
                }
            if(show)
            {

                utl.log("DIAG_OPEN : "+ctx.getClass());
                dialog = new Dialog(ctx);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.gen_load);
                final Window window = dialog.getWindow();
                window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
                window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                window.setBackgroundDrawable(new ColorDrawable(Color.GRAY));
                dialog.getWindow().getAttributes().alpha = 0.7f;



                dialog.setTitle("Select Content Language");

                dialog.setContentView(R.layout.gen_load);
                //  dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.setCanceledOnTouchOutside(true);
                dialog.setCancelable(true);
                dialog.show();

            }
            else   {



                utl.log("DIAG_CLOSE: "+ctx.getClass());
                if(dialog!=null)
                    if(dialog.isShowing())
                    {
                        dialog.dismiss();
                    }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static Context ctx;
    public static void init(Context ctxx)
    {

//       TAG=""+ctx.getClass().toString();
        ctx=ctxx;
        File f=new File(Constants.getFolder(ctx)+"/debug.txt");
        if(f.exists()){
            CITY_DEBUG=(new FileOperations().read(Constants.getFolder(ctx)+"/debug.txt")).replace(".n","");

            utl.l("Debug City set to : "+CITY_DEBUG);
    }


          f=new File(Environment.getExternalStorageDirectory().getPath()+"/Android/debug.txt");
        if(f.exists()){
            DEBUG_ENABLED=true;
         }


    }

    public static void showDig1(boolean show )
    {
        try {
            if(dialog!=null)
                if(dialog.isShowing()&&show)
                {
                    return;
                }
            if(show)
            {

                utl.log("DIAG_OPEN : "+ctx.getClass());
                dialog = new Dialog(ctx);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.gen_load);
                final Window window = dialog.getWindow();
                window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
                window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                window.setBackgroundDrawable(new ColorDrawable(Color.GRAY));
                dialog.getWindow().getAttributes().alpha = 0.7f;



                dialog.setTitle("Select Content Language");

                dialog.setContentView(R.layout.gen_load);
                //  dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.setCanceledOnTouchOutside(true);
                dialog.setCancelable(true);
                dialog.show();

            }
            else   {



                utl.log("DIAG_CLOSE: "+ctx.getClass());
                if(dialog!=null)
                    if(dialog.isShowing())
                    {
                        dialog.dismiss();
                    }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void load(boolean show )
    {
       // pd(show);

    }


    public static void load1(boolean show )
    {
        try {
            if(dialog!=null)
                if(dialog.isShowing()&&show)
                {
                    return;
                }
            if(show)
            {

                utl.log("DIAG_OPEN : "+ctx.getClass());
                dialog = new Dialog(ctx);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.gen_load);
                final Window window = dialog.getWindow();
                window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
                window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                window.setBackgroundDrawable(new ColorDrawable(Color.GRAY));
                dialog.getWindow().getAttributes().alpha = 0.7f;



                dialog.setTitle("Select Content Language");

                dialog.setContentView(R.layout.gen_load);
                //  dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.setCanceledOnTouchOutside(true);
                dialog.setCancelable(true);
                dialog.show();

            }
            else   {



                utl.log("DIAG_CLOSE: "+ctx.getClass());
                if(dialog!=null)
                    if(dialog.isShowing())
                    {
                        dialog.dismiss();
                    }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void showDig(boolean show,Context ctx,String label)
    {
        try {
            if(show)
            {

                utl.log("DIAG_OPEN : "+ctx.getClass());
                dialog = new Dialog(ctx);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.gen_load);
                final Window window = dialog.getWindow();
                window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
                window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                window.setBackgroundDrawable(new ColorDrawable(Color.GRAY));
                dialog.getWindow().getAttributes().alpha = 0.7f;



                dialog.setTitle("Select Content Language");

                dialog.setContentView(R.layout.gen_load);
                //  dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.setCanceledOnTouchOutside(true);
                dialog.setCancelable(true);
                dialog.show();

            }
            else   {



                utl.log("DIAG_CLOSE: "+ctx.getClass());
                if(dialog!=null)
                    if(dialog.isShowing())
                    {
                        dialog.dismiss();
                    }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static String refineString(String red,String rep)
    {
        red = red.replaceAll("[^a-zA-Z0-9]", rep);
        return  red;
    }


    private boolean isValidMail(String email)
    {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }










    public static Bitmap convertBitmap(String path)   {
                Bitmap bitmap=null;
                BitmapFactory.Options bfOptions=new BitmapFactory.Options();
                bfOptions.inDither=false;                     //Disable Dithering mode
                bfOptions.inPurgeable=true;                   //Tell to gc that whether itneeds free memory, the Bitmap can be cleared
                bfOptions.inInputShareable=true;              //Which kind of reference will be                used to recover the Bitmap data after being clear, when it will be used in the future
                bfOptions.inTempStorage=new byte[32 * 1024];
                File file=new File(path);
                FileInputStream fs=null;
                try {
                    fs = new FileInputStream(file);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                try {
                    if(fs!=null)
                    {
                        bitmap=BitmapFactory.decodeFileDescriptor(fs.
                                getFD(), null, bfOptions);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally{
                    if(fs!=null) {
                        try {
                            fs.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
                return bitmap;
            }




    public static Bitmap convertBitmap(Context ctx,  String  path,int q)   {


        Log.d("path",path);

        Bitmap bitmap=null;
        BitmapFactory.Options bfOptions=new BitmapFactory.Options();
        bfOptions.inDither=false;                     //Disable Dithering mode
        bfOptions.inPurgeable=true;                   //Tell to gc that whether itneeds free memory, the Bitmap can be cleared
        bfOptions.inInputShareable=true;              //Which kind of reference will be                used to recover the Bitmap data after being clear, when it will be used in the future
        bfOptions.inTempStorage=new byte[32 * 1024];
        File file=new File(path);
        FileInputStream fs=null;
        try {
            fs = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            if(fs!=null)
            {
                bitmap=BitmapFactory.decodeFileDescriptor(fs.
                        getFD(), null, bfOptions);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally{
            if(fs!=null) {
                try {
                    fs.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }


        Bitmap bitmap1=Bitmap.createScaledBitmap(bitmap,(int)(bitmap.getWidth()*(q/100)),
                (int)(bitmap.getHeight()*(q/100)),true);

        try{

            bitmap.recycle();;
            bitmap=null;

        }catch (Exception e)
        {
            Log.d("Recycle fail","Never Ming at convertBitmap()")
;            e.printStackTrace();
        }
        return bitmap1;
    }




    public static String getDateTime() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        return dateFormat.format(date);
    }


    public static  void saveScaledBitmap(Bitmap bmp,int h,int w,String dest){

        Bitmap bmm=getResizedBitmap(bmp,h,w);


        File de=new File(dest);

       /*     try {  if(!de.exists())
                de.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }*/
        FileOutputStream out= null;
        try {
            out = new FileOutputStream(de);
        bmp.compress(Bitmap.CompressFormat.PNG,1,out);
        out.flush();;
        out.close();;
            Log.d("SAVED",dest);


        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public static Bitmap overlay(Bitmap b1,Bitmap b2)
    {

        b2=utl.getResizedBitmap(b2,b1.getHeight(),b1.getWidth());
        Bitmap ov=Bitmap.createBitmap(b1.getWidth(), b1.getHeight(), b1.getConfig());
        Canvas cv=new Canvas(ov);
        cv.drawBitmap(b1,new Matrix(),null);
        cv.drawBitmap(b2,new Matrix(),null);
        return ov;

    }


    public static void bitmapToFile(Bitmap bmp,String dest)
    {

        File f=new File(dest);
        try {
            FileOutputStream os;

            os=new FileOutputStream(f);
            bmp.compress(Bitmap.CompressFormat.PNG,100,os);
            os.flush();
            os.close();
            bmp.recycle();


        }catch (Exception e)
        {
            e.printStackTrace();
        }



    }

    public static Float dpFromPx(final Context context, final float px) {
        return px / context.getResources().getDisplayMetrics().density;
    }

    public static Float pxFromDp(final Context context, final float dp) {
        return dp * context.getResources().getDisplayMetrics().density;
    }

    public static String resizePng(Context ctx,String src,String dest, int quality)
    {

        try {
            Bitmap bmp = convertBitmap(ctx,src,quality);
            if (bmp.getWidth()<512) {

                File de=new File(dest);
                FileOutputStream out=new FileOutputStream(de);
                bmp.compress(Bitmap.CompressFormat.PNG,90,out);
                out.flush();;
                out.close();;

                return dest;
            }


            File de=new File(dest);
            FileOutputStream out=new FileOutputStream(de);
            bmp.compress(Bitmap.CompressFormat.PNG,quality,out);
            out.flush();;
            out.close();;







        }catch (Exception e)
        {
            e.printStackTrace();
        }
        return dest;
    }
    public static void showSoftKeyboard(Activity activity, View linearLayout) {

        InputMethodManager inputMethodManager =
                (InputMethodManager)activity. getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInputFromWindow(
                linearLayout.getApplicationWindowToken(),
                InputMethodManager.SHOW_FORCED, 0);
    }






    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager)  activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }


    public static void toast(Context c,String t) {


        try {
            if(DISPLAY_ENABLED)
            Toast.makeText(c, t, Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static int getApkVerison(Context ctx)
    {
        try{
        int versionCode = BuildConfig.VERSION_CODE;
        String versionName = BuildConfig.VERSION_NAME;
        return versionCode;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;

        }
    }

    public static Gson js=new Gson();

    public static void snack(Activity act,String t)
    {



        View rootView = act.getWindow().getDecorView().getRootView();
        Snackbar snackbar = Snackbar.make(rootView,  ""+t, Snackbar.LENGTH_LONG);
       // snackbar.setActionTextColor(act.getResources().getColor(R.color.material_red_A400));
        View snackbarView = snackbar.getView();
        snackbarView.setBackgroundColor(act.getResources().getColor(R.color.red_300));


        snackbar.setActionTextColor(act.getResources().getColor(R.color.white));

// change snackbar text color
        int snackbarTextId = android.support.design.R.id.snackbar_text;
        TextView textView = (TextView)snackbarView.findViewById(snackbarTextId);
        textView.setTextColor(Color.WHITE);


        if(DISPLAY_ENABLED)
        snackbar.show();

    }





    public static void snack(View rootView,String t)
    {

        Context act=rootView.getContext();
          Snackbar snackbar = Snackbar.make(rootView, Html.fromHtml("<font color=\"#fff\">"+t+"</font>" ), Snackbar.LENGTH_LONG);
        // snackbar.setActionTextColor(act.getResources().getColor(R.color.material_red_A400));
        View snackbarView = snackbar.getView();
        snackbarView.setBackgroundColor(act.getResources().getColor(R.color.red_300));

// change snackbar text color
        int snackbarTextId = android.support.design.R.id.snackbar_text;
        TextView textView = (TextView)snackbarView.findViewById(snackbarTextId);
        textView.setTextColor(act.getResources().getColor(R.color.white));


        if(DISPLAY_ENABLED)
            snackbar.show();

    }




    public static void toast(Context c,String tzz,Integer col)
    { 
        Toast t=Toast.makeText(c, tzz, Toast.LENGTH_LONG);
        t.getView().setBackgroundColor(c.getResources().getColor(col));
        if(DISPLAY_ENABLED)
        t.show();



    }


    public static String TAG="TAH UTL";
    public static void log(String t)
    {

        Log.d(""+TAG, ""+t);
    }


    public static void log(String t,String tt)
    {

        Log.d(""+t, ""+tt);
    }


    public static void l(String t)
    {

        Log.d(""+TAG, ""+t);
    }


    public static void l(String t,String tt)
    {

        Log.d(""+t, ""+tt);
    }




    public static void e(String t)
    {

        Log.e(""+TAG, ""+t);
    }

    public static void e(String t,String tt)
    {

        Log.e(""+t, ""+tt);
    }

    public static void l(Object t)
    {

        Log.d(""+TAG, ""+t);
    }


    public static void l(String t,Object tt)
    {

        Log.d(""+t, ""+tt);
    }


    public static void diag(Context c,String title,String desc,String action,DialogInterface.OnClickListener click)
    {



        final AlertDialog.Builder
                alertDialogBuilder = new AlertDialog.Builder
                (c);
        alertDialogBuilder.setTitle(title);
        alertDialogBuilder.setMessage(desc);
        alertDialogBuilder.setNeutralButton(action,click);


        AlertDialog alertDialog
                = alertDialogBuilder.create();


        alertDialog.show();
    }




    public static void copyFile(File src,File dst)
    {
        try{

        InputStream in=new FileInputStream(src);
        OutputStream os=new FileOutputStream(dst);

            byte []buf=new byte[1024];
            int len;
            while((len=in.read(buf))>0)
            {
                os.write(buf,0,len);
            }
            in.close();
            os.close();




        }catch (Exception e)
        {
            e.printStackTrace();
        }



    }




    public static  String folder=Environment.getExternalStorageDirectory().getPath();;
    public  utl(){

        folder= Environment.getExternalStorageDirectory().getPath();


    }
    public static void diag(Context c,String title,String desc)
    {
        try {
            final AlertDialog.Builder
                    alertDialogBuilder = new AlertDialog.Builder
                    (c);
            alertDialogBuilder.setTitle(title);
            alertDialogBuilder.setMessage(Html.fromHtml(desc));
            alertDialogBuilder.setNeutralButton("Close", new
                    DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface
                                                    dialog, int id) {
                            dialog.cancel();
                        }
                    });


            AlertDialog alertDialog
                    = alertDialogBuilder.create();


            alertDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Bitmap getResizedBitmap(Bitmap bm, int newHeight, int newWidth) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // create a matrix for the manipulation
        Matrix matrix = new Matrix();
        // resize the bit map
        matrix.postScale(scaleWidth, scaleHeight);
        // recreate the new Bitmap
        Bitmap resizedBitmap = Bitmap.createBitmap
                (bm, 0, 0, width, height, matrix, false);
        return resizedBitmap;
    }

    public static Bitmap getResizedBitmap(Bitmap bm, int newHeight, int newWidth,int i) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) ;
        float scaleHeight = ((float) newHeight) ;
        // create a matrix for the manipulation
        Matrix matrix = new Matrix();
        // resize the bit map
        matrix.postScale(scaleWidth, scaleHeight);
        // recreate the new Bitmap
        Bitmap resizedBitmap = Bitmap.createBitmap
                (bm, 0, 0, width, height, matrix, false);
        return resizedBitmap;
    }
public Bitmap circle(Bitmap bmp)
{
    Bitmap out=Bitmap.createBitmap(bmp.getWidth(),bmp.getHeight(), Bitmap.Config.ARGB_8888);
    Canvas cv=new Canvas(out);
    int color= Color.RED;
    Paint paint=new Paint();
    Rect rect=new Rect(0,0,bmp.getWidth(),bmp.getHeight());
    RectF rectF=new RectF(rect);
    paint.setAntiAlias(true);
    cv.drawARGB(0, 0, 0, 0);
    paint.setColor(color);
    cv.drawOval(rectF, paint);
    paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
    cv.drawBitmap(bmp,rect,rect,paint);


    return out;


}


}
