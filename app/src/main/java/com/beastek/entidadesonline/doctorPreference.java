package com.beastek.entidadesonline;

import android.content.Context;
import android.content.SharedPreferences;

public class doctorPreference {

    public static String ISLOGGEDIN = "loggedin";
    public static String USERNAME = "username";
    public static String PUSHID = "userpushid";
    public static String PHONENO = "phoneno";
    public static String RESTORE_DATA = "restoreData";
    public static String TAPTARGET = "tapTarget";

    public static void saveBooleanInSP(Context _context, boolean value){
        SharedPreferences preferences = _context.getSharedPreferences("ENTIDADESONLINE", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(ISLOGGEDIN, value);
        editor.commit();
    }

    public static boolean getBooleanFromSP(Context _context) {
// TODO Auto-generated method stub
        SharedPreferences preferences = _context.getSharedPreferences("ENTIDADESONLINE", Context.MODE_PRIVATE);
        return preferences.getBoolean(ISLOGGEDIN, false);

        //false es el valor a retornar si la preferencia no existe. Devuelve false si no existe la preferencia.
        // Si existe devuelve el valor que tenga la variable ISSLOGGEDIN
    }

    public static void saveUsernameInSP(Context _context, String value){
        SharedPreferences preferences = _context.getSharedPreferences("ENTIDADESONLINE", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(USERNAME, value);
        editor.commit();
    }

    public static String getUsernameFromSP(Context context) {
// TODO Auto-generated method stub
        if(context == null){
            return null;
        }else {
            SharedPreferences preferences = context.getSharedPreferences("ENTIDADESONLINE", Context.MODE_PRIVATE);
            return preferences.getString(USERNAME, null);
        }
    }

    public static void savePhoneNumberInSP(Context _context, String value){
        SharedPreferences preferences = _context.getSharedPreferences("ENTIDADESONLINE", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(PHONENO, value);


        // diferencias entre editor.apply() que hace I/O a disco de manera as??ncrona
        // mientras commit() es s??ncrono. So you really shouldn't call commit() from the UI thread.
    }

    public static String getPhoneNumberFromSP(Context context) {
// TODO Auto-generated method stub
        if(context == null){
            return null;
        }else {
            SharedPreferences preferences = context.getSharedPreferences("ENTIDADESONLINE", Context.MODE_PRIVATE);
            return preferences.getString(PHONENO, null);
        }
    }

    public static void saveUserPushId(Context _context, String value){
        SharedPreferences preferences = _context.getSharedPreferences("ENTIDADESONLINE", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(PUSHID, value);
        editor.commit();
    }

    public static String getUserPushId(Context context) {
// TODO Auto-generated method stub
        if(context == null){
            return null;
        }else {
            SharedPreferences preferences = context.getSharedPreferences("ENTIDADESONLINE", Context.MODE_PRIVATE);
            return preferences.getString(PUSHID, null);
        }
    }

    public static void saveWantToRestoreData(Context _context, boolean value){
        SharedPreferences preferences = _context.getSharedPreferences("ENTIDADESONLINE", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(RESTORE_DATA, value);
        editor.apply();
    }

    public static boolean getWantToRestoreData(Context _context) {
// TODO Auto-generated method stub
        SharedPreferences preferences = _context.getSharedPreferences("ENTIDADESONLINE", Context.MODE_PRIVATE);
        return preferences.getBoolean(RESTORE_DATA, true);
    }

    public static void saveIsTapTargetShown(Context _context, boolean value){
        SharedPreferences preferences = _context.getSharedPreferences("ENTIDADESONLINE", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(TAPTARGET, value);
        editor.apply();
    }

    public static boolean getIsTapTargetShown(Context _context) {
// TODO Auto-generated method stub
        SharedPreferences preferences = _context.getSharedPreferences("ENTIDADESONLINE", Context.MODE_PRIVATE);
        return preferences.getBoolean(TAPTARGET, false);
    }
}
