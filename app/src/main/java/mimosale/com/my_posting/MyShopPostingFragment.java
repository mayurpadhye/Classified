package mimosale.com.my_posting;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import mimosale.com.R;
import mimosale.com.helperClass.PrefManager;
import mimosale.com.home.fragments.ShopSaleAdapter;
import mimosale.com.home.shop_sale.ShopSaleModel;
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
public class MyShopPostingFragment extends Fragment {
ProgressBar p_bar;
View v;
RecyclerView rv_shop_sale;
List<MyShopPojo> myShopPojoList=new ArrayList<>();
    public MyShopPostingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v= inflater.inflate(R.layout.fragment_my_shop_posting, container, false);

        initView(v);
        getAllShopAndSale();


        //getAllShopAndSale();
        return v;
    }

    public void initView(View v)
    {
        p_bar=v.findViewById(R.id.p_bar);
        rv_shop_sale=v.findViewById(R.id.rv_shop_sale);
        GridLayoutManager gridLayoutManager1 = new GridLayoutManager(getActivity(), 1);
        gridLayoutManager1.setOrientation(LinearLayoutManager.HORIZONTAL);
        rv_shop_sale.setLayoutManager(gridLayoutManager1);

    }

    public void getAllShopAndSale() {

        try {
            p_bar.setVisibility(View.VISIBLE);
            RetrofitClient retrofitClient = new RetrofitClient();
            RestInterface service = retrofitClient.getAPIClient(WebServiceURLs.DOMAIN_NAME);
            service.getUserShop(PrefManager.getInstance(getActivity()).getUserId(),"Bearer "+PrefManager.getInstance(getActivity()).getApiToken(),new Callback<JsonElement>() {
                @Override
                public void success(JsonElement jsonElement, Response response) {
                    //this method call if webservice success
                    try {
                        myShopPojoList.clear();
                        JSONObject jsonObject = new JSONObject(jsonElement.toString());
                        String status = jsonObject.getString("status");

                        if (status.equals("1")) {
                            JSONArray data = jsonObject.getJSONArray("data");
                            for (int i = 0; i < data.length(); i++) {
                                JSONObject j1 = data.getJSONObject(i);
                                String id = j1.getString("id");
                                String name = j1.getString("name");
                                String user_id = j1.getString("user_id");
                                String preference_id = j1.getString("preference_id");
                                String address_line1 = j1.getString("address_line1");
                                String address_line2 = j1.getString("address_line2");
                                String city = j1.getString("city");
                                String state = j1.getString("state");
                                String country = j1.getString("country");
                                String pincode = j1.getString("pincode");
                                String lat = j1.getString("lat");
                                String lon = j1.getString("lon");
                                String low_price = j1.getString("low_price");
                                String high_price = j1.getString("high_price");
                                String min_discount = j1.getString("min_discount");
                                String max_discount = j1.getString("max_discount");
                                String start_date = j1.getString("start_date");
                                String end_date = j1.getString("end_date");
                                String phone = j1.getString("phone");
                                String hash_tags = j1.getString("hash_tags");
                                String description = j1.getString("description");
                                String web_url = j1.getString("web_url");
                                String image1 = j1.getString("image1");
                                String image2 = j1.getString("image2");
                                String image = "";

                                myShopPojoList.add(new MyShopPojo(id,name,user_id,preference_id,address_line1,address_line2,city,state,country,pincode,lat,lon,low_price,high_price,min_discount,max_discount,start_date,end_date,phone,hash_tags,description,web_url,image1,image2));

                            }
                            MyShopPostingAdapter shopSaleAdapter = new MyShopPostingAdapter(myShopPojoList, getActivity());
                            rv_shop_sale.setAdapter(shopSaleAdapter);
                            shopSaleAdapter.notifyDataSetChanged();



                        }
                        p_bar.setVisibility(View.GONE);

                    } catch (JSONException | NullPointerException e) {
                        p_bar.setVisibility(View.GONE);
                        e.printStackTrace();

                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    p_bar.setVisibility(View.GONE);
                    Toast.makeText(getActivity(), getResources().getString(R.string.check_internet), Toast.LENGTH_LONG).show();
                    Log.i("fdfdfdfdfdf", "" + error.getMessage());

                }
            });
        } catch (Exception e) {
            e.printStackTrace();

        }


    }//etAllShopAndSale

}
