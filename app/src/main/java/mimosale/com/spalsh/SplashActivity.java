package mimosale.com.spalsh;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import mimosale.com.R;
import mimosale.com.helperClass.CustomUtils;
import mimosale.com.helperClass.PrefManager;
import mimosale.com.home.HomeActivity;
import mimosale.com.home.preferences.PreferenceListModel;
import mimosale.com.home.preferences.PreferencesListAdapter;
import mimosale.com.login.LoginActivity;
import mimosale.com.network.RestInterface;
import mimosale.com.network.RetrofitClient;
import mimosale.com.network.WebServiceURLs;
import mimosale.com.noInternet.NoInternetActivity;
import com.google.gson.JsonElement;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import static mimosale.com.helperClass.PrefManager.IS_LOGIN;
import static mimosale.com.helperClass.PrefManager.getInstance;

public class SplashActivity extends AppCompatActivity {
    Context context;
    private static int SPLASH_TIME_OUT = 5000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        context = this;
       // getBannerImage();

          new Handler().postDelayed(new Runnable() {



                @Override
                public void run() {
                    if (CustomUtils.isNetworkAvailable(SplashActivity.this)) {
                        PrefManager.getInstance(SplashActivity.this);

                        Intent i = new Intent(SplashActivity.this, HomeActivity.class);
                        startActivity(i);
                        finish();


                    } else {
                        // startActivity(new Intent(SplashActivity.this, NoInternetActivity.class));
                        Intent i = new Intent(SplashActivity.this, LoginActivity.class);
                        startActivity(i);
                        finish();
                    }
                }
            }, SPLASH_TIME_OUT);



    }

    public void getBannerImage()
    {

        try{

            RetrofitClient retrofitClient = new RetrofitClient();
            RestInterface service = retrofitClient.getAPIClient(WebServiceURLs.DOMAIN_NAME);
            service.GetBannerImages( new Callback<JsonElement>() {
                @Override
                public void success(JsonElement jsonElement, Response response) {
                    //this method call if webservice success
                    try {
                        JSONObject jsonObject = new JSONObject(jsonElement.toString());
                        String status=jsonObject.getString("status");

                        if (status.equals("1"))
                        {
                            JSONArray data=jsonObject.getJSONArray("data");
                            PrefManager.getInstance(SplashActivity.this).setBanner(data.toString());
                            Intent i = new Intent(SplashActivity.this, HomeActivity.class);
                            startActivity(i);
                            finish();




                        }



                    } catch (JSONException | NullPointerException e) {
                        e.printStackTrace();

                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    Toast.makeText(SplashActivity.this ,getResources().getString(R.string.check_internet), Toast.LENGTH_LONG ).show();
                    Log.i("fdfdfdfdfdf",""+error.getMessage());

                }
            });
        }
        catch ( Exception e){
            e.printStackTrace();

        }


    }//userPreferencesClose
}
