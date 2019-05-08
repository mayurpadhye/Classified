package mimosale.com.helperClass;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import java.util.Map;

public class PrefManager {
   public static  PrefManager prefManager=null;
    private static SharedPreferences mSharedPref;
    public static final String NAME = "NAME";
    public static final String AGE = "AGE";
    public static final String IS_LOGIN = "is_login";
    public static final String IS_FIRST_LAUNCH = "is_first_launch";
    public static final String USER_ID = "user_id";
    public static final String API_TOKEN = "api_token";
    public static final String FIRST_NAME = "first_name";
    public static final String LAST_NAME = "last_name";
    public static final String MOBILE = "mobile";
    public static final String EMAIL = "email";
    public static final String PROFILE_PIC = "profile_pic";
    public static final String BANNER = "banner";
    private static final String LANGUAGE = "language";
    private static final String FEEDBACK = "feedback";
    private static final String LANGUAGE_CODE = "language_code";

    private PrefManager()
    {

    }

    public static PrefManager getInstance(Context context)
    {
        if(prefManager==null) {
            prefManager=new PrefManager();
            if (mSharedPref == null) {
                mSharedPref = context.getSharedPreferences(context.getPackageName(), Activity.MODE_PRIVATE);
            }
        }
        return prefManager;
    }

    public static boolean IS_LOGIN() {
        return mSharedPref.getBoolean(IS_LOGIN, false);
    }

    public static void setIsLogin( boolean value) {
        SharedPreferences.Editor prefsEditor = mSharedPref.edit();
        prefsEditor.putBoolean(IS_LOGIN, value);
        prefsEditor.commit();
    }


    public static boolean IsFirstLaunch() {
        return mSharedPref.getBoolean(IS_FIRST_LAUNCH, false);
    }

    public static void setIsFirstLaunch( boolean value) {
        SharedPreferences.Editor prefsEditor = mSharedPref.edit();
        prefsEditor.putBoolean(IS_FIRST_LAUNCH, value);
        prefsEditor.commit();
    }


    public static String getUserId() {
        return mSharedPref.getString(USER_ID, "");
    }

    public static void setUserId( String value) {
        SharedPreferences.Editor prefsEditor = mSharedPref.edit();
        prefsEditor.putString(USER_ID, value);
        prefsEditor.commit();
    }

    public static String getApiToken() {
        return mSharedPref.getString(API_TOKEN, "");
    }

    public static void setApiToken( String value) {
        SharedPreferences.Editor prefsEditor = mSharedPref.edit();
        prefsEditor.putString(API_TOKEN, value);
        prefsEditor.commit();
    }

    public static String getFirstName() {
        return mSharedPref.getString(FIRST_NAME, "");
    }

    public static void setFirstName( String value) {
        SharedPreferences.Editor prefsEditor = mSharedPref.edit();
        prefsEditor.putString(FIRST_NAME, value);
        prefsEditor.commit();
    }
    public static String getLastName() {
        return mSharedPref.getString(LAST_NAME, "");
    }

    public static void setLastName( String value) {
        SharedPreferences.Editor prefsEditor = mSharedPref.edit();
        prefsEditor.putString(LAST_NAME, value);
        prefsEditor.commit();
    }

    public static String getEmail() {
        return mSharedPref.getString(EMAIL, "");
    }

    public static void setEmail( String value) {
        SharedPreferences.Editor prefsEditor = mSharedPref.edit();
        prefsEditor.putString(EMAIL, value);
        prefsEditor.commit();
    }
    public static String getMobile() {
        return mSharedPref.getString(MOBILE, "");
    }

    public static void setMobile( String value) {
        SharedPreferences.Editor prefsEditor = mSharedPref.edit();
        prefsEditor.putString(MOBILE, value);
        prefsEditor.commit();
    }

    public static String getProfilePic() {
        return mSharedPref.getString(PROFILE_PIC, "");
    }

    public static void setProfilePic( String value) {
        SharedPreferences.Editor prefsEditor = mSharedPref.edit();
        prefsEditor.putString(PROFILE_PIC, value);
        prefsEditor.commit();
    }


    public static String getBanner() {
        return mSharedPref.getString(BANNER, "");
    }

    public static void setBanner( String value) {
        SharedPreferences.Editor prefsEditor = mSharedPref.edit();
        prefsEditor.putString(BANNER, value);
        prefsEditor.commit();
    }


    public String getLanguage()
    {
        return mSharedPref.getString(LANGUAGE, "1");
    }

    public void setLanguage(String value)
    {
        SharedPreferences.Editor prefsEditor = mSharedPref.edit();
        prefsEditor.putString(LANGUAGE, value);
        prefsEditor.commit();
    }

    public String getAppLanguageCode()
    {
        return mSharedPref.getString(LANGUAGE_CODE, "");
    }

    public void setAppLanguageCode(String value)
    {
        SharedPreferences.Editor prefsEditor = mSharedPref.edit();
        prefsEditor.putString(LANGUAGE_CODE, value);
        prefsEditor.commit();
    }



    public String getFeedbackStatus()
    {
        return mSharedPref.getString(FEEDBACK, "0");
    }

    public void setFeedbackStatus(String value)
    {
        SharedPreferences.Editor prefsEditor = mSharedPref.edit();
        prefsEditor.putString(FEEDBACK, value);
        prefsEditor.commit();
    }


    public void Logout()
    {

        SharedPreferences.Editor prefsEditor = mSharedPref.edit();
       prefsEditor.clear().commit();

    }
}
