package mimosale.com.preferences;


import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import mimosale.com.R;
import mimosale.com.helperClass.PrefManager;
import mimosale.com.network.RestInterface;
import mimosale.com.network.RetrofitClient;
import mimosale.com.network.WebServiceURLs;
import com.google.gson.JsonElement;

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
public class AddNewPreferenceFragment extends Fragment {
List<AllPrefPojo> allPrefPojoList=new ArrayList<>();
RecyclerView rv_my_pref_add;
Button btn_add;
ProgressBar p_bar;
JSONArray array_pref;

View v;
    public AddNewPreferenceFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v= inflater.inflate(R.layout.fragment_add_new_preference, container, false);
        initView(v);
        if (PrefManager.getInstance(getActivity()).IS_LOGIN())
        getAllPrefData();
        else
            getDevicePref();

        array_pref=new JSONArray();
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* */

               for (int k=0;k<allPrefPojoList.size();k++)
               {
                   if (allPrefPojoList.get(k).getSelected().equals("true"))
                   {
                       array_pref.put(""+allPrefPojoList.get(k).getPref_id());
                   }
               }
               Log.i("PrefData",""+array_pref);

               if (array_pref.length()>0) {
                if (PrefManager.getInstance(getActivity()).IS_LOGIN())
                   AddPrefData();
                else
                    AddDeviceIdPrefData();
               }
               else
                   showNothingToUpdate();

            }
        });
        return v;
    }

    public void showNothingToUpdate()
    {
        final AlertDialog.Builder builder = new AlertDialog.Builder(
                getActivity());
        builder.setTitle(getResources().getString(R.string.nothing_to_update));
        builder.setMessage(getResources().getString(R.string.no_update_msg))
                .setCancelable(false)
                .setPositiveButton(getResources().getString(R.string.ok),
                        new DialogInterface.OnClickListener() {
                            //do something
                            public void onClick(DialogInterface dialog,
                                                int id) {
                               dialog.cancel();
                            }
                        });

        final AlertDialog alert = builder.create();
        alert.show();
    }

    public void AddPrefData()
    {
        try {
            p_bar.setVisibility(View.VISIBLE);
            RetrofitClient retrofitClient = new RetrofitClient();
            RestInterface service = retrofitClient.getAPIClient(WebServiceURLs.DOMAIN_NAME);
            service.addUserPrefData(PrefManager.getInstance(getActivity()).getUserId(), "Bearer " + PrefManager.getInstance(getActivity()).getApiToken(),array_pref.toString(), new Callback<JsonElement>() {
                @Override
                public void success(JsonElement jsonElement, Response response) {
                    //this method call if webservice success
                    try {
                        JSONObject jsonObject = new JSONObject(jsonElement.toString());
                        String status = jsonObject.getString("status");

                        if (status.equals("1")) {

                            final Dialog dialog=new Dialog(getActivity());
                            dialog.setContentView(R.layout.dialog_preferences_updated);
                            Button btn_ok=dialog.findViewById(R.id.btn_ok);
                            btn_ok.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog.dismiss();
                                }
                            });
                            dialog.show();
                        }

                        p_bar.setVisibility(View.GONE);
                    } catch (JSONException | NullPointerException e) {
                        e.printStackTrace();

                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    p_bar.setVisibility(View.GONE);
                    Toast.makeText(getActivity(), getString(R.string.check_internet), Toast.LENGTH_LONG).show();
                    Log.i("fdfdfdfdfdf", "" + error.getMessage());

                }
            });
        } catch (Exception e) {
            e.printStackTrace();

        }

    }


    public void AddDeviceIdPrefData()
    {
        try {
            String  device_id=Settings.Secure.getString(getActivity().getContentResolver(), Settings.Secure.ANDROID_ID);
            p_bar.setVisibility(View.VISIBLE);
            RetrofitClient retrofitClient = new RetrofitClient();
            RestInterface service = retrofitClient.getAPIClient(WebServiceURLs.DOMAIN_NAME);
            service.addPrefAgainstDeviceId(device_id, "Bearer " + PrefManager.getInstance(getActivity()).getApiToken(),array_pref.toString(), new Callback<JsonElement>() {
                @Override
                public void success(JsonElement jsonElement, Response response) {
                    //this method call if webservice success
                    try {
                        JSONObject jsonObject = new JSONObject(jsonElement.toString());
                        String status = jsonObject.getString("status");

                        if (status.equals("1")) {

                            final Dialog dialog=new Dialog(getActivity());
                            dialog.setContentView(R.layout.dialog_preferences_updated);
                            Button btn_ok=dialog.findViewById(R.id.btn_ok);
                            btn_ok.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog.dismiss();
                                }
                            });
                            dialog.show();
                        }

                        p_bar.setVisibility(View.GONE);
                    } catch (JSONException | NullPointerException e) {
                        e.printStackTrace();

                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    p_bar.setVisibility(View.GONE);
                    Toast.makeText(getActivity(), getString(R.string.check_internet), Toast.LENGTH_LONG).show();
                    Log.i("fdfdfdfdfdf", "" + error.getMessage());

                }
            });
        } catch (Exception e) {
            e.printStackTrace();

        }

    }


    public void getDevicePref()
    {
        try {
            String  device_id=Settings.Secure.getString(getActivity().getContentResolver(), Settings.Secure.ANDROID_ID);
            p_bar.setVisibility(View.VISIBLE);
            RetrofitClient retrofitClient = new RetrofitClient();
            RestInterface service = retrofitClient.getAPIClient(WebServiceURLs.DOMAIN_NAME);
            service.getDivicePrefList(device_id, "Bearer " + PrefManager.getInstance(getActivity()).getApiToken(), new Callback<JsonElement>() {
                @Override
                public void success(JsonElement jsonElement, Response response) {
                    //this method call if webservice success
                    try {
                        JSONObject jsonObject = new JSONObject(jsonElement.toString());
                        String status = jsonObject.getString("status");

                        if (status.equals("1")) {

                            JSONArray data=jsonObject.getJSONArray("data");
                            for (int k=0;k<data.length();k++)
                            {
                                JSONObject j1=data.getJSONObject(k);
                                String id=j1.getString("id");
                                String name=j1.getString("name");
                                String description=j1.getString("description");
                                String image=j1.getString("image");
                                String status1=j1.getString("status");
                                String selected=j1.getString("selected");
                                allPrefPojoList.add(new AllPrefPojo(name,image,id,selected));
                            }

                            AddNewPrefAdapter adapter=new AddNewPrefAdapter(allPrefPojoList,getActivity());
                            rv_my_pref_add.setAdapter(adapter);
                        }

                        p_bar.setVisibility(View.GONE);
                    } catch (JSONException | NullPointerException e) {
                        e.printStackTrace();

                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    p_bar.setVisibility(View.GONE);
                    Toast.makeText(getActivity(), getString(R.string.check_internet), Toast.LENGTH_LONG).show();
                    Log.i("fdfdfdfdfdf", "" + error.getMessage());

                }
            });
        } catch (Exception e) {
            e.printStackTrace();

        }


    }//


    public void getAllPrefData()
    {
        try {
            p_bar.setVisibility(View.VISIBLE);
            RetrofitClient retrofitClient = new RetrofitClient();
            RestInterface service = retrofitClient.getAPIClient(WebServiceURLs.DOMAIN_NAME);
            service.getAllPreferences(PrefManager.getInstance(getActivity()).getUserId(), "Bearer " + PrefManager.getInstance(getActivity()).getApiToken(), new Callback<JsonElement>() {
                @Override
                public void success(JsonElement jsonElement, Response response) {
                    //this method call if webservice success
                    try {
                        JSONObject jsonObject = new JSONObject(jsonElement.toString());
                        String status = jsonObject.getString("status");

                        if (status.equals("1")) {

                            JSONArray data=jsonObject.getJSONArray("data");
                            for (int k=0;k<data.length();k++)
                            {
                                JSONObject j1=data.getJSONObject(k);
                                String id=j1.getString("id");
                                String name=j1.getString("name");
                                String description=j1.getString("description");
                                String image=j1.getString("image");
                                String status1=j1.getString("status");
                                String selected=j1.getString("selected");
                                allPrefPojoList.add(new AllPrefPojo(name,image,id,selected));
                            }

                            AddNewPrefAdapter adapter=new AddNewPrefAdapter(allPrefPojoList,getActivity());
                            rv_my_pref_add.setAdapter(adapter);
                        }

                        p_bar.setVisibility(View.GONE);
                    } catch (JSONException | NullPointerException e) {
                        e.printStackTrace();

                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    p_bar.setVisibility(View.GONE);
                    Toast.makeText(getActivity(), getString(R.string.check_internet), Toast.LENGTH_LONG).show();
                    Log.i("fdfdfdfdfdf", "" + error.getMessage());

                }
            });
        } catch (Exception e) {
            e.printStackTrace();

        }


    }//


    public void initView(View v)
    {

        btn_add=v.findViewById(R.id.btn_add);
        p_bar=v.findViewById(R.id.p_bar);
        rv_my_pref_add=v.findViewById(R.id.rv_my_pref_add);
        rv_my_pref_add.setLayoutManager(new GridLayoutManager(getActivity(), 2));







    }//initViewClose

}
