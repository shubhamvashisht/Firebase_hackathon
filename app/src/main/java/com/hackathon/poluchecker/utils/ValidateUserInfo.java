package com.hackathon.poluchecker.utils;

import java.util.regex.Pattern;

/**
 * Created by AndreBTS on 25/09/2015.
 */
public class ValidateUserInfo {
    public static boolean isEmailValid(String email) {
        //TODO change for your own logic
        String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        Pattern p = Pattern.compile(ePattern);
        java.util.regex.Matcher m = p.matcher(email);
        return m.matches();
    }

    public static boolean isPhoneValid(String phone2) {
        //TODO change for your own logic
       // return android.util.Patterns.PHONE.matcher(phone).matches();

        boolean check=false;
        if(!Pattern.matches("[a-zA-Z]+", phone2))
        {
            if(phone2.length() < 6 || phone2.length() > 13)
            {
                check = false;
                //txtPhone.setError("Not Valid Number");
            }
            else
            {
                check = true;
            }
        }
        else
        {
            check=false;
        }
        return check;
    }




    public static boolean isPasswordValid(String password) {
        //TODO change for your own logic
        return password.length() > 4;
    }
}
