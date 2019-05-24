package mimosale.com.following;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
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

public class FollowingShopActivity extends AppCompatActivity {
    RecyclerView rv_shop;
    ProgressBar p_bar;
    TextView tv_no_record;
    List<FollowingShopPojo> allShopSaleList=new ArrayList();
    TextView toolbar_title;
    ImageView iv_back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_following_shop);
        initView();
        toolbar_title.setText(getResources().getString(R.string.following));
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        getFollowingList();
    }

    public void getFollowingList()
    {
        try {
            p_bar.setVisibility(View.VISIBLE);
            RetrofitClient retrofitClient = new RetrofitClient();
            RestInterface service = retrofitClient.getAPIClient(WebServiceURLs.DOMAIN_NAME);
            service.getShopFollowingList(PrefManager.getInstance(FollowingShopActivity.this).getUserId(),"Bearer "+PrefManager.getInstance(FollowingShopActivity.this).getApiToken(),new Callback<JsonElement>() {
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
                                String discount = j1.getString("discount");
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

                                allShopSaleList.add(new FollowingShopPojo(id,name,image1));

                            }
                            if (data.length()>0)
                            {
                                FollowingShopAdapter shopSaleAdapter = new FollowingShopAdapter(allShopSaleList, FollowingShopActivity.this);
                                rv_shop.setAdapter(shopSaleAdapter);
                                shopSaleAdapter.notifyDataSetChanged();
                                rv_shop.setVisibility(View.VISIBLE);
                                tv_no_record.setVisibility(View.GONE);
                            }
                            else
                            {
                                rv_shop.setVisibility(View.GONE);
                                tv_no_record.setVisibility(View.VISIBLE);
                            }



                            tv_no_record.setVisibility(View.GONE);
                        }
                        else
                        {
                            tv_no_record.setVisibility(View.VISIBLE);
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
                    Toast.makeText(FollowingShopActivity.this, getResources().getString(R.string.check_internet), Toast.LENGTH_LONG).show();
                    Log.i("fdfdfdfdfdf", "" + error.getMessage());

                }
            });
        } catch (Exception e) {
            e.printStackTrace();

        }

    }//getFollowingListClose

    public void initView() {
        rv_shop = findViewById(R.id.rv_shop);
        toolbar_title = findViewById(R.id.toolbar_title);
        iv_back = findViewById(R.id.iv_back);
        tv_no_record = findViewById(R.id.tv_no_record);
        p_bar = findViewById(R.id.p_bar);
        GridLayoutManager gridLayoutManager1 = new GridLayoutManager(FollowingShopActivity.this, 1);
        gridLayoutManager1.setOrientation(LinearLayoutManager.VERTICAL);
        rv_shop.setLayoutManager(gridLayoutManager1);

    }
}
