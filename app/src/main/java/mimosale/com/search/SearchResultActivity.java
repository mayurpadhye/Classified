package mimosale.com.search;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import mimosale.com.R;
import mimosale.com.home.fragments.AllProductPojo;
import mimosale.com.home.fragments.ShopSaleAdapter;
import mimosale.com.home.shop_sale.ProductsAdapter;
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

public class SearchResultActivity extends AppCompatActivity implements TextWatcher {
RecyclerView rv_shop;
ProgressBar p_bar;

LinearLayout ll_search;
EditText et_search;
    List<AllProductPojo> allProductPojoList=new ArrayList<>();
    List<ShopSaleModel> allShopSaleList=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);
        initView();
        ll_search.setVisibility(View.VISIBLE);
        et_search.addTextChangedListener(this);


    }

    private void serachresult(String keyword) {
        try {
            p_bar.setVisibility(View.VISIBLE);
            RetrofitClient retrofitClient = new RetrofitClient();
            RestInterface service = retrofitClient.getAPIClient(WebServiceURLs.DOMAIN_NAME);
            service.search(keyword,new Callback<JsonElement>() {
                @Override
                public void success(JsonElement jsonElement, Response response) {
                    //this method call if webservice success
                    try {
                        allShopSaleList.clear();
                        allProductPojoList.clear();
                        JSONObject jsonObject = new JSONObject(jsonElement.toString());
                        String status = jsonObject.getString("status");

                        if (status.equals("1")) {
                            JSONArray data = jsonObject.getJSONArray("data");
                            if (data.length()>0)
                            {


                                ll_search.setVisibility(View.GONE);


                                rv_shop.setVisibility(View.VISIBLE);

                            }
                            for (int i = 0; i < data.length(); i++) {
                                JSONObject j1 = data.getJSONObject(i);
                                String id = j1.getString("id");
                                String name = j1.getString("name");
                                String user_id = j1.getString("user_id");
                                String preference_id = j1.getString("preference_id");

                                String address_line1="",address_line2="",state="",country="";
                                String pincode="",lat="",lon="",low_price="",high_price="",min_discount="",max_discount="",start_date="";
                                String end_date="",phone="",hash_tags="",web_url="",description="",type="",city="";
                                if (j1.has("address_line1"))
                                {
                                     address_line1 = j1.getString("address_line1");
                                }
                                if (j1.has("address_line2"))
                                {
                                    address_line2 = j1.getString("address_line2");
                                }
                                if (j1.has("city"))
                                {
                                    city = j1.getString("city");
                                }
                                if (j1.has("state"))
                                {
                                    state = j1.getString("state");
                                }
                                if (j1.has("country"))
                                {
                                    country = j1.getString("country");
                                }
                                if (j1.has("pincode"))
                                {
                                     pincode = j1.getString("pincode");
                                }
                                if (j1.has("lat"))
                                {
                                    lat = j1.getString("lat");
                                }
                                if (j1.has("lon"))
                                {
                                    lon = j1.getString("lon");
                                }
                                if (j1.has("low_price"))
                                {
                                    low_price = j1.getString("low_price");
                                }
                                if (j1.has("high_price"))
                                {
                                    high_price = j1.getString("high_price");
                                }
                                if (j1.has("min_discount"))
                                {
                                    min_discount = j1.getString("min_discount");
                                }
                                if (j1.has("max_discount"))
                                {
                                    max_discount = j1.getString("max_discount");
                                }

                                if (j1.has("start_date"))
                                {
                                    start_date = j1.getString("start_date");
                                }

                                if (j1.has("end_date"))
                                {
                                    end_date = j1.getString("end_date");
                                }
                                if (j1.has("phone"))
                                {
                                    phone = j1.getString("phone");
                                }
                                if (j1.has("hash_tags"))
                                {
                                    hash_tags = j1.getString("hash_tags");
                                }
                                if (j1.has("description"))
                                {
                                    description = j1.getString("description");
                                }


                                if (j1.has("web_url"))
                                {
                                    web_url = j1.getString("web_url");
                                }

                                if (j1.has("type"))
                                {
                                    type = j1.getString("type");
                                }

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

                                allShopSaleList.add(new ShopSaleModel(id,name,user_id,preference_id,address_line1,address_line2,city,state,country,pincode,lat,lon,low_price,high_price,min_discount,max_discount,start_date,end_date,phone,hash_tags,description,web_url,image1,image2,type));

                            }


                            SearchAdapter shopSaleAdapter = new SearchAdapter(allShopSaleList, SearchResultActivity.this);
                            rv_shop.setAdapter(shopSaleAdapter);
                            shopSaleAdapter.notifyDataSetChanged();

                        }
                        else
                        {

                        }
                        p_bar.setVisibility(View.GONE);

                    } catch (JSONException | NullPointerException e) {
                        p_bar.setVisibility(View.GONE);
                        e.printStackTrace();
                        Log.i("fdfdfdfdfdf", "" + e.getMessage());

                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    p_bar.setVisibility(View.GONE);
                    Toast.makeText(SearchResultActivity.this, getResources().getString(R.string.check_internet), Toast.LENGTH_LONG).show();
                    Log.i("fdfdfdfdfdf", "" + error.getMessage());

                }
            });
        } catch (Exception e) {
            e.printStackTrace();

        }


    }//serchResultClose

    public void initView()
    {
        p_bar=findViewById(R.id.p_bar);
        ll_search=findViewById(R.id.ll_search);

        rv_shop=findViewById(R.id.rv_shop);






        et_search=findViewById(R.id.et_search);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(SearchResultActivity.this, 1);
        gridLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);



        GridLayoutManager gridLayoutManager1 = new GridLayoutManager(SearchResultActivity.this, 1);
        gridLayoutManager1.setOrientation(LinearLayoutManager.VERTICAL);
        rv_shop.setLayoutManager(gridLayoutManager1);


    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (s.length()>2)
        {
            serachresult(s.toString());
            ll_search.setVisibility(View.GONE);
        }
        else
        {

            ll_search.setVisibility(View.VISIBLE);
            rv_shop.setVisibility(View.GONE);

        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
