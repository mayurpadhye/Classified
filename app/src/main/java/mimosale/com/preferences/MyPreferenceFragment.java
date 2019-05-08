package mimosale.com.preferences;


import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import mimosale.com.R;
import mimosale.com.helperClass.PrefManager;
import mimosale.com.home.HomeFragment;
import mimosale.com.home.preferences.PreferenceListModel;
import mimosale.com.home.preferences.PreferencesListAdapter;
import mimosale.com.network.RestInterface;
import mimosale.com.network.RetrofitClient;
import mimosale.com.network.WebServiceURLs;
import com.google.gson.JsonElement;
import com.joaquimley.faboptions.FabOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyPreferenceFragment extends Fragment {
View v;
List<MyPreferencePojo> myPreferencePojoList=new ArrayList<MyPreferencePojo>();
RecyclerView rv_my_pref;
ProgressBar p_bar;


    public MyPreferenceFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v= inflater.inflate(R.layout.fragment_my_preference, container, false);
        initView(v);

        return v;
    }//onCreateClsoe

    @Override
    public void onResume() {
        super.onResume();
        if (PrefManager.getInstance(getActivity()).IS_LOGIN())
        getUserPreferences();
        else
            getDeviceIdWisePref();

    }

    public void initView(View v)
    {
        myPreferencePojoList=new ArrayList<MyPreferencePojo>();
        p_bar=v.findViewById(R.id.p_bar);

        rv_my_pref=v.findViewById(R.id.rv_my_pref);
        rv_my_pref.setLayoutManager(new GridLayoutManager(getActivity(), 2));





    }//initViewClose

    public void getDeviceIdWisePref()
    {


        try {
         String  device_id=Settings.Secure.getString(getActivity().getContentResolver(), Settings.Secure.ANDROID_ID);
            RetrofitClient retrofitClient = new RetrofitClient();
            RestInterface service = retrofitClient.getAPIClient(WebServiceURLs.DOMAIN_NAME);
            service.getDevicePref(device_id,new Callback<JsonElement>() {
                @Override
                public void success(JsonElement jsonElement, Response response) {
                    //this method call if webservice success
                    try {
                        myPreferencePojoList.clear();
                        JSONObject jsonObject = new JSONObject(jsonElement.toString());
                        String status = jsonObject.getString("status");

                        if (status.equals("1")) {
                            JSONArray data = jsonObject.getJSONArray("data");
                            for (int i = 0; i < data.length(); i++) {
                                JSONObject j1 = data.getJSONObject(i);
                                String id = j1.getString("id");
                                String name = j1.getString("name");
                                String description = j1.getString("description");
                                String image = j1.getString("image");
                                String status1 = j1.getString("status");
                                String created_at = j1.getString("created_at");
                                String updated_at = j1.getString("updated_at");
                                myPreferencePojoList.add(new MyPreferencePojo(name,image,id));
                            }

                            MyPreferencesAdapter adapter=new MyPreferencesAdapter(myPreferencePojoList,getActivity(),p_bar);
                            rv_my_pref.setAdapter(adapter);


                        }

                        p_bar.setVisibility(View.GONE);

                    } catch (JSONException | NullPointerException e) {
                        e.printStackTrace();
                        Log.i("fdfdfdfdfdf", "" + e.getMessage());


                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    Toast.makeText(getActivity(), getResources().getString(R.string.check_internet), Toast.LENGTH_LONG).show();
                    Log.i("fdfdfdfdfdf", "" + error.getMessage());

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Log.i("fdfdfdfdfdf", "" + e.getMessage());

        }


    }//getDeviceIdPref

    public void getUserPreferences() {

        try {
            p_bar.setVisibility(View.VISIBLE);
            RetrofitClient retrofitClient = new RetrofitClient();
            RestInterface service = retrofitClient.getAPIClient(WebServiceURLs.DOMAIN_NAME);
            service.getUserPreferences(PrefManager.getInstance(getActivity()).getUserId(), "Bearer " + PrefManager.getInstance(getActivity()).getApiToken(), new Callback<JsonElement>() {
                @Override
                public void success(JsonElement jsonElement, Response response) {
                    //this method call if webservice success
                    try {
                        myPreferencePojoList.clear();
                        JSONObject jsonObject = new JSONObject(jsonElement.toString());
                        String status = jsonObject.getString("status");

                        if (status.equals("1")) {
                            JSONArray data = jsonObject.getJSONArray("data");
                            for (int i = 0; i < data.length(); i++) {
                                JSONObject j1 = data.getJSONObject(i);
                                String id = j1.getString("id");
                                String name = j1.getString("name");
                                String description = j1.getString("description");
                                String image = j1.getString("image");
                                String status1 = j1.getString("status");
                                String created_at = j1.getString("created_at");
                                String updated_at = j1.getString("updated_at");
                                myPreferencePojoList.add(new MyPreferencePojo(name,image,id));
                            }

                            MyPreferencesAdapter adapter=new MyPreferencesAdapter(myPreferencePojoList,getActivity(),p_bar);
                            rv_my_pref.setAdapter(adapter);


                        }

                        p_bar.setVisibility(View.GONE);
                    } catch (JSONException | NullPointerException e) {
                        e.printStackTrace();
                        p_bar.setVisibility(View.GONE);

                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    Toast.makeText(getActivity(), getResources().getString(R.string.check_internet), Toast.LENGTH_LONG).show();
                    Log.i("fdfdfdfdfdf", "" + error.getMessage());
                    p_bar.setVisibility(View.GONE);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();

        }


    }


}
