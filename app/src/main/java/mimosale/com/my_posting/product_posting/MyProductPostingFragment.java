package mimosale.com.my_posting.product_posting;


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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import mimosale.com.R;
import mimosale.com.helperClass.PrefManager;
import mimosale.com.home.fragments.AllProductPojo;
import mimosale.com.home.shop_sale.ProductsAdapter;
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
public class MyProductPostingFragment extends Fragment {

View v;
RecyclerView rv_products;
ProgressBar p_bar;
List<AllProductPojo>allProductPojoList=new ArrayList<>();
TextView tv_no_posting;
    public MyProductPostingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v= inflater.inflate(R.layout.fragment_my_product_posting, container, false);
     initView(v);
    return v;
    }
    @Override
    public void onResume() {
        super.onResume();

        getAllProducts();
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }
    public void initView(View v)
    {
        rv_products=v.findViewById(R.id.rv_products);
        p_bar=v.findViewById(R.id.p_bar);
        tv_no_posting=v.findViewById(R.id.tv_no_posting);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 1);
        gridLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        LinearLayoutManager llm = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rv_products.setLayoutManager(llm);
        rv_products.setHasFixedSize(false);
    }//initViewClose


    @Override public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
    }
    public void getAllProducts() {

        try {
            p_bar.setVisibility(View.VISIBLE);
            RetrofitClient retrofitClient = new RetrofitClient();
            RestInterface service = retrofitClient.getAPIClient(WebServiceURLs.DOMAIN_NAME);
            service.my_product_posting(PrefManager.getInstance(getActivity()).getUserId(),"Bearer "+PrefManager.getInstance(getActivity()).getApiToken(),new Callback<JsonElement>() {
                @Override
                public void success(JsonElement jsonElement, Response response) {
                    //this method call if webservice success
                    try {
                        allProductPojoList.clear();
                        JSONObject jsonObject = new JSONObject(jsonElement.toString());
                        String status = jsonObject.getString("status");
                        p_bar.setVisibility(View.GONE);
                        if (status.equals("1")) {
                            JSONArray data = jsonObject.getJSONArray("data");
                            for (int i = 0; i < data.length(); i++) {
                                JSONObject j1 = data.getJSONObject(i);
                                String id = j1.getString("id");
                                String name = j1.getString("name");
                                String shop_id = j1.getString("shop_id");
                                String user_id = j1.getString("user_id");
                                String description = j1.getString("description");
                                String price = j1.getString("price");
                                String hash_tags = j1.getString("hash_tags");
                                String status1 = j1.getString("status");
                                String image1 = j1.getString("image1");
                                String image2 = j1.getString("image2");
                                String image= "";
                               /* JSONArray product_images=j1.getJSONArray("product_images");
                                for (int k=0;k<product_images.length();k++)
                                {
                                    JSONObject j2=product_images.getJSONObject(k);
                                     image=j2.getString("image");
                                }
                                */
                                allProductPojoList.add(new AllProductPojo(id,name,shop_id,user_id,description,price,hash_tags,status1,image1,image2));
                            }
                            if (data.length()>0)
                            {
                                tv_no_posting.setVisibility(View.GONE);
                                MyProductPostingAdapter shopSaleAdapter = new MyProductPostingAdapter(allProductPojoList, getActivity(),p_bar);
                                rv_products.setAdapter(shopSaleAdapter);

                            }
                            else
                            {
                                tv_no_posting.setVisibility(View.VISIBLE);
                            }



                        }


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
