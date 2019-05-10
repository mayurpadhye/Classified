package mimosale.com.home.fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import mimosale.com.R;


import mimosale.com.helperClass.PrefManager;
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
public class ShopSaleFragment extends Fragment {
    View v;
    RecyclerView rv_shop_sale;
    List<ShopSaleModel> allShopSaleList = new ArrayList<>();

    public ShopSaleFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_shop_sale, container, false);
        initView(v);
        //getAllShopAndSale();
        return v;
    }
    @Override
    public void onResume() {
        super.onResume();
        getAllShopAndSale();

    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }

    public void initView(View v) {

        rv_shop_sale = v.findViewById(R.id.rv_shop_sale);
       /* GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        gridLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
*/

        GridLayoutManager gridLayoutManager1 = new GridLayoutManager(getActivity(), 1);
        gridLayoutManager1.setOrientation(LinearLayoutManager.HORIZONTAL);
        rv_shop_sale.setLayoutManager(gridLayoutManager1);

    }//initViewClsoe


    public void getAllShopAndSale() {

        try {

            RetrofitClient retrofitClient = new RetrofitClient();
            RestInterface service = retrofitClient.getAPIClient(WebServiceURLs.DOMAIN_NAME);
            service.getAllShopAndSale(PrefManager.getInstance(getActivity()).getUserId(),new Callback<JsonElement>() {
                @Override
                public void success(JsonElement jsonElement, Response response) {
                    //this method call if webservice success
                    try {
                        allShopSaleList.clear();
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
                                String image2="";
                                String image1="";
                                if(j1.has("image1")) {

                                    image1 = j1.getString("image1");
                                }
                                if(j1.has("image2"))
                                {
                                    image2 = j1.getString("image2");
                                }

                                String image = "";

                               /* JSONArray shop_images = j1.getJSONArray("shop_images");

                                for (int k = 0; k < shop_images.length(); k++) {
                                    JSONObject j2=shop_images.getJSONObject(k);
                                    image = j2.getString("image");
                                }*/
                                allShopSaleList.add(new ShopSaleModel(id,name,user_id,preference_id,address_line1,address_line2,city,state,country,pincode,lat,lon,low_price,high_price,min_discount,max_discount,start_date,end_date,phone,hash_tags,description,web_url,image1,image2,"shop"));

                            }

                            ShopSaleAdapter shopSaleAdapter = new ShopSaleAdapter(allShopSaleList, getActivity());
                            rv_shop_sale.setAdapter(shopSaleAdapter);


                        }


                    } catch (JSONException | NullPointerException e) {
                        e.printStackTrace();

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

        }


    }//etAllShopAndSale

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
    }
}
