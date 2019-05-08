package mimosale.com.products;

import android.app.Dialog;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import mimosale.com.R;
import mimosale.com.home.fragments.AllProductPojo;
import mimosale.com.network.RestInterface;
import mimosale.com.network.RetrofitClient;
import mimosale.com.network.WebServiceURLs;
import mimosale.com.shop.ShopDetailActivity;
import mimosale.com.shop.adapter.ShopImageDetailsAdapter;
import com.github.siyamed.shapeimageview.RoundedImageView;
import com.google.gson.JsonElement;
import com.ms.square.android.expandabletextview.ExpandableTextView;
import com.romainpiel.shimmer.Shimmer;
import com.romainpiel.shimmer.ShimmerTextView;
import com.viewpagerindicator.CirclePageIndicator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

public class ProductDetailsActivity extends AppCompatActivity implements View.OnClickListener {
    //TextView toolbar_title;

    //ImageView iv_back;
    ShimmerTextView tv_avg_rating;
    Shimmer shimmer;
    RecyclerView rv_shop_products;

    Button btn_submit, btn_back;
    Dialog dialog;
    private static ViewPager mPager;
    private static int currentPage = 0;
    private static int NUM_PAGES = 0;
    ViewPager view_pager_shop_details;
    TabLayout tabLayout;
    LinearLayout ll_highlight, ll_info;
    TextView tv_discount, tv_price_range, tv_price_range_detail, tv_location, tv_website, tv_category, tv_tag, tv_product_name;
    String product_id = "";

    List<String> shopImagesPojoList = new ArrayList<>();
    List<AllProductPojo> allProductPojoList = new ArrayList<>();
    ExpandableTextView expTv1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);
        initView();
        getProductDetails();
    }

    public void initView() {

        shimmer = new Shimmer();
        tv_avg_rating = findViewById(R.id.tv_avg_rating);

        product_id = getIntent().getStringExtra("product_id");
        shimmer.start(tv_avg_rating);

        ll_highlight = findViewById(R.id.ll_highlight);
        ll_info = findViewById(R.id.ll_info);

        btn_submit = findViewById(R.id.btn_submit);
        btn_back = findViewById(R.id.btn_back);

        btn_submit.setOnClickListener(this);
        btn_back.setOnClickListener(this);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        // Add five tabs.  Three have icons and two have text titles
        tabLayout.addTab(tabLayout.newTab().setText(getResources().getString(R.string.highlight)));
        tabLayout.addTab(tabLayout.newTab().setText(getResources().getString(R.string.info)));
        expTv1 = (ExpandableTextView) findViewById(R.id.expand_text_view)
                .findViewById(R.id.expand_text_view);

        // IMPORTANT - call setText on the ExpandableTextView to set the text content to display
        expTv1.setText(getString(R.string.lorem_epsum));
        tv_discount = findViewById(R.id.tv_discount);
        tv_product_name = findViewById(R.id.tv_product_name);
        tv_category = findViewById(R.id.tv_category);
        tv_price_range_detail = findViewById(R.id.tv_price_range_detail);
        tv_price_range = findViewById(R.id.tv_price_range);
        tv_location = findViewById(R.id.tv_location);
        tv_website = findViewById(R.id.tv_website);
        tv_tag = findViewById(R.id.tv_tag);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tabLayout.getSelectedTabPosition() == 0) {
                    ll_highlight.setVisibility(View.VISIBLE);
                    ll_info.setVisibility(View.GONE);

                } else if (tabLayout.getSelectedTabPosition() == 1) {
                    ll_highlight.setVisibility(View.GONE);
                    ll_info.setVisibility(View.VISIBLE);

                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


        mPager = (ViewPager) findViewById(R.id.pager);


    }


    public void getProductDetails() {
        try {

            RetrofitClient retrofitClient = new RetrofitClient();
            RestInterface service = retrofitClient.getAPIClient(WebServiceURLs.DOMAIN_NAME);
            service.getProductDetails(product_id, new Callback<JsonElement>() {
                @Override
                public void success(JsonElement jsonElement, Response response) {
                    //this method call if webservice success
                    try {

                        allProductPojoList.clear();
                        JSONObject jsonObject = new JSONObject(jsonElement.toString());
                        String status = jsonObject.getString("status");

                        if (status.equals("1")) {
                            JSONArray data = jsonObject.getJSONArray("data");
                            for (int i = 0; i < data.length(); i++) {
                                JSONObject j1 = data.getJSONObject(i);
                                String id = j1.getString("id");
                                String name = j1.getString("name");
                                String description = j1.getString("description");
                                String price = j1.getString("price");
                                String hash_tag = j1.getString("hash_tags");
                                String status1 = j1.getString("status");

                                tv_product_name.setText(name);
                                expTv1.setText(description);
                                tv_price_range.setText(price);
                                tv_tag.setText(hash_tag);


                                JSONArray product_images = j1.getJSONArray("product_images");
                                for (int j = 0; j < product_images.length(); j++) {

                                    JSONObject j2 = product_images.getJSONObject(j);
                                    String image_id = j2.getString("id");
                                    String image = j2.getString("image");
                                    shopImagesPojoList.add(image);
                                }
                                ShopImageDetailsAdapter bannerAdapter = new ShopImageDetailsAdapter(ProductDetailsActivity.this, shopImagesPojoList);
                                mPager.setAdapter(bannerAdapter);

                                CirclePageIndicator indicator = (CirclePageIndicator)
                                        findViewById(R.id.indicator);

                                indicator.setViewPager(mPager);

                                final float density = getResources().getDisplayMetrics().density;

//Set circle indicator radius
                                indicator.setRadius(5 * density);


                            }
                        }


                    } catch (JSONException | NullPointerException e) {
                        e.printStackTrace();
                        Log.i("fdfdfdfdfdf", "" + e.getMessage());
                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    Toast.makeText(ProductDetailsActivity.this, getResources().getString(R.string.check_internet), Toast.LENGTH_LONG).show();
                    Log.i("fdfdfdfdfdf", "" + error.getMessage());

                }
            });
        } catch (Exception e) {
            e.printStackTrace();

        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
        }
    }
}
