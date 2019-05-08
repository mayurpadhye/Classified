package mimosale.com.my_posting;


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
public class MySalePostingFragment extends Fragment {

    View v;
    RecyclerView rv_shop_sale;
    List<MySaleModel> mySaleModelList = new ArrayList<>();

    public MySalePostingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v= inflater.inflate(R.layout.fragment_my_sale_posting, container, false);
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
            service.my_sale_posting(PrefManager.getInstance(getActivity()).getUserId(),"Bearer "+PrefManager.getInstance(getActivity()).getApiToken(),new Callback<JsonElement>() {
                @Override
                public void success(JsonElement jsonElement, Response response) {
                    //this method call if webservice success
                    try {
                        mySaleModelList.clear();
                        JSONObject jsonObject = new JSONObject(jsonElement.toString());
                        String status = jsonObject.getString("status");

                        if (status.equals("1")) {
                            JSONArray data = jsonObject.getJSONArray("data");
                            for (int i = 0; i < data.length(); i++) {
                                JSONObject j1 = data.getJSONObject(i);
                                String id = j1.getString("id");
                                String name = j1.getString("title");
                                String shop_id = j1.getString("shop_id");
                                String user_id = j1.getString("user_id");
                                String start_date = j1.getString("start_date");
                                String end_date = j1.getString("end_date");
                                String min_discount = j1.getString("min_discount");
                                String max_discount = j1.getString("max_discount");
                                String description = j1.getString("description");
                                String web_url = j1.getString("web_url");
                                String hash_tags = j1.getString("hash_tags");
                                String status1 = j1.getString("status");
                                String image1 = j1.getString("image1");
                                String image2 = j1.getString("image2");
                                String shop_name = j1.getString("shop_name");
                                mySaleModelList.add(new MySaleModel(id,name,shop_id,user_id,start_date,end_date,min_discount,max_discount,description,web_url,hash_tags,status1,image1,image2,shop_name));



                            }

                            if (data.length()>0)
                            {
                                MySaleAdapter adapter=new MySaleAdapter(mySaleModelList,getActivity());
                                rv_shop_sale.setAdapter(adapter);
                            }



                        }


                    } catch (JSONException | NullPointerException e) {
                        e.printStackTrace();
                        Log.i("fdfdfdfdfdf", "" + e.toString());

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
