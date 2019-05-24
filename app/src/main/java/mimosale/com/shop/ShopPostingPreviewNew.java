package mimosale.com.shop;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import mimosale.com.R;
import mimosale.com.helperClass.PrefManager;
import mimosale.com.home.fragments.AllProductPojo;
import mimosale.com.network.RestInterface;
import mimosale.com.network.RetrofitClient;
import mimosale.com.network.WebServiceURLs;
import mimosale.com.shop.adapter.ShopImageDetailsAdapter;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.MultipartTypedOutput;
import retrofit.mime.TypedFile;
import retrofit.mime.TypedString;

public class ShopPostingPreviewNew extends AppCompatActivity implements View.OnClickListener {
    RecyclerView rv_shop_products;
    TextView toolbar_title;
    ImageView iv_back, iv_phone;
    Button btn_submit, btn_back;
    Dialog dialog;
    private static ViewPager mPager;
    private static int currentPage = 0;
    private static int NUM_PAGES = 0;
    ViewPager view_pager_shop_details;
    TextView tv_complete_address;
    ImageView iv_navigate;
    // TextView tv_photo_count;
    LinearLayout ll_highlight, ll_info;
    TextView tv_discount, tv_price_range, tv_price_range_detail, tv_location, tv_website, tv_category, tv_tag;
    String shop_id = "";
    String lat = "", lan = "";
    //  NestedScrollView nv_main;
    ProgressDialog pDialog;
    TextView tv_desc;
    Intent i;
    List<String> shopImagesPojoList = new ArrayList<>();
    List<AllProductPojo> allProductPojoList = new ArrayList<>();
    ArrayList<ImageVideoData> image_thumbnail=new ArrayList<>();
    ArrayList<ImageVideoData> IMAGES;
String discount="";
    RecyclerView rv_products;
    private GoogleMap gmap;
    String type="";
    String shop_name,shop_images,shop_desc,shop_category,min_discount,max_discount,start_date,end_date,min_price,max_price,pincode,city,address_line_1;
    String state="",country="",pref_id="";
    String address_line_2,phone_number,hash_tag,web_url;
    RelativeLayout rl_follow;
    TextView tv_follow_text,tv_phone_no;
    String phone = "";
    RelativeLayout rl_like;
    TextView tv_photos;
    String status_follow = "follow";
    String lati="",longi="";
    ImageView iv_follow;
    List<File> image_thumbnail1=new ArrayList<>();
    ProgressBar p_bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_posting_preview_new);
        initView();
        i=getIntent();
        shop_name=i.getStringExtra("shop_name");
        shop_desc=i.getStringExtra("shop_desc");
        pref_id=i.getStringExtra("pref_id");
        shop_category=i.getStringExtra("shop_category");
        type=i.getStringExtra("type");

        discount=i.getStringExtra("discount");
        start_date=i.getStringExtra("start_date");
        end_date=i.getStringExtra("end_date");
        min_price=i.getStringExtra("min_price");
        max_price=i.getStringExtra("max_price");
        pincode=i.getStringExtra("pincode");
        city=i.getStringExtra("city");
        address_line_1=i.getStringExtra("address_line_1");
        address_line_2=i.getStringExtra("address_line_2");
        phone_number=i.getStringExtra("phone_number");
        lati=i.getStringExtra("lati");
        longi=i.getStringExtra("longi");
        hash_tag=i.getStringExtra("hash_tag");
        web_url=i.getStringExtra("web_url");
        // image_thumbnail1=(List<File>) getIntent().getSerializableExtra("image_thumbnail");
        parseJSON();

    }
    public void initView() {

        p_bar=findViewById(R.id.p_bar);
        tv_follow_text = findViewById(R.id.tv_follow_text);
        tv_phone_no = findViewById(R.id.tv_phone_no);
        tv_desc = findViewById(R.id.tv_desc);
        tv_complete_address = findViewById(R.id.tv_complete_address);
        iv_navigate = findViewById(R.id.iv_navigate);
        toolbar_title = findViewById(R.id.toolbar_title);
        toolbar_title.setText(getIntent().getStringExtra("shop_name"));
        iv_follow = findViewById(R.id.iv_follow);
        rl_like = findViewById(R.id.rl_like);
        tv_photos = findViewById(R.id.tv_photos);
        rl_follow = findViewById(R.id.rl_follow);
        shop_id = getIntent().getStringExtra("shop_id");
        rv_shop_products = findViewById(R.id.rv_shop_products);
        GridLayoutManager gridLayoutManager1 = new GridLayoutManager(ShopPostingPreviewNew.this, 1);
        gridLayoutManager1.setOrientation(LinearLayoutManager.VERTICAL);
        rv_shop_products.setLayoutManager(gridLayoutManager1);
        iv_back = findViewById(R.id.iv_back);
        iv_phone = findViewById(R.id.iv_phone);
        btn_submit = findViewById(R.id.btn_submit);
        btn_back = findViewById(R.id.btn_back);
        iv_back.setOnClickListener(this);
        tv_discount = findViewById(R.id.tv_discount);

        tv_category = findViewById(R.id.tv_category);
        tv_price_range_detail = findViewById(R.id.tv_price_range_detail);
        tv_price_range = findViewById(R.id.tv_price_range);
        tv_location = findViewById(R.id.tv_location);
        mPager = (ViewPager) findViewById(R.id.pager);


        toolbar_title.setText(shop_name);
        tv_desc.setText(shop_desc);
        tv_complete_address.setText(address_line_1+" "+address_line_2);
tv_phone_no.setText(phone_number);
tv_discount.setText(discount+"%");
if (type.equals("save"))
{
    btn_submit.setText(getResources().getString(R.string.save));
}
else
{
    btn_submit.setText(getResources().getString(R.string.update));

}

btn_submit.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        if (type.equals("save"))
        {

        }
        else
        {
           updateShopDetails();

        }
    }
});


    }//initViewClose

    private void parseJSON() {
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<ImageVideoData>>(){}.getType();
        image_thumbnail = gson.fromJson(i.getStringExtra("shop_images"), type);
        for (ImageVideoData contact : image_thumbnail){
            Log.i("Contact Details",  ""+contact.getBitmap());
        }

        Type type2 = new TypeToken<ArrayList<File>>(){}.getType();
        image_thumbnail1 = gson.fromJson(i.getStringExtra("image_thumbnail"), type2);
        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        ShopSlidingImagesAdapter bannerAdapter = new ShopSlidingImagesAdapter(image_thumbnail,layoutInflater,ShopPostingPreviewNew.this);
        mPager.setAdapter(bannerAdapter);

    }

    @Override
    public void onClick(View v) {

    }


    public void updateShopDetails() {
        try {


            PrefManager.getInstance(ShopPostingPreviewNew.this).getUserId();
            p_bar.setVisibility(View.VISIBLE);

            MultipartTypedOutput multipartTypedOutput = new MultipartTypedOutput();
            multipartTypedOutput.addPart("name", new TypedString(shop_name));
            multipartTypedOutput.addPart("preference_id", new TypedString(pref_id));
            multipartTypedOutput.addPart("address_line1", new TypedString(address_line_1));
            multipartTypedOutput.addPart("address_line2", new TypedString(address_line_2));
            multipartTypedOutput.addPart("city", new TypedString(city));
            multipartTypedOutput.addPart("state", new TypedString(state));
            multipartTypedOutput.addPart("country", new TypedString(country));
            multipartTypedOutput.addPart("pincode", new TypedString(pincode));
            multipartTypedOutput.addPart("lat", new TypedString(lati));
            multipartTypedOutput.addPart("lon", new TypedString(longi));
            multipartTypedOutput.addPart("low_price", new TypedString(min_price));
            multipartTypedOutput.addPart("high_price", new TypedString(max_price));
            multipartTypedOutput.addPart("min_discount", new TypedString(""));
            multipartTypedOutput.addPart("max_discount", new TypedString(""));
            multipartTypedOutput.addPart("discount", new TypedString(discount));
            multipartTypedOutput.addPart("phone", new TypedString(phone_number));
            multipartTypedOutput.addPart("hash_tags", new TypedString(hash_tag));
            multipartTypedOutput.addPart("description", new TypedString(shop_desc));
            multipartTypedOutput.addPart("web_url", new TypedString(web_url));
            multipartTypedOutput.addPart("status", new TypedString("1"));
            multipartTypedOutput.addPart("user_id", new TypedString(PrefManager.getInstance(ShopPostingPreviewNew.this).getUserId()));
            multipartTypedOutput.addPart("shop_id", new TypedString(shop_id));


            if (image_thumbnail1.size() > 0) {
                for (int i = 0; i < image_thumbnail1.size(); i++) {
                    multipartTypedOutput.addPart("shop_photos[]", new TypedFile("application/octet-stream", new File(image_thumbnail1.get(i).getAbsolutePath())));
                }
            } else {
                multipartTypedOutput.addPart("shop_photos", new TypedString(""));
            }


            RetrofitClient retrofitClient = new RetrofitClient();
            RestInterface service = retrofitClient.getAPIClient(WebServiceURLs.DOMAIN_NAME);
            service.update_shop("Bearer " + PrefManager.getInstance(ShopPostingPreviewNew.this).getApiToken(),
                    multipartTypedOutput, new Callback<JsonElement>() {
                        @Override
                        public void success(JsonElement jsonElement, Response response) {
                            //this method call if webservice success
                            try {
                                JSONObject jsonObject = new JSONObject(jsonElement.toString());
                                String status = jsonObject.getString("status");

                                if (status.equals("1")) {

                                    Toast.makeText(ShopPostingPreviewNew.this, "Shop Successfully Updated", Toast.LENGTH_SHORT).show();

                                    new SweetAlertDialog(ShopPostingPreviewNew.this, SweetAlertDialog.SUCCESS_TYPE)
                                            .setTitleText(getResources().getString(R.string.success))
                                            .setContentText(getResources().getString(R.string.shop_posting_success))
                                            .setConfirmText(getResources().getString(R.string.ok))
                                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                @Override
                                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                    finish();
                                                }
                                            })
                                            .show();

                                } else {
                                    Toast.makeText(ShopPostingPreviewNew.this, "Unable to update Shop", Toast.LENGTH_SHORT).show();

                                }

                                p_bar.setVisibility(View.GONE);
                            } catch (JSONException | NullPointerException e) {
                                e.printStackTrace();

                            }
                        }

                        @Override
                        public void failure(RetrofitError error) {
                            p_bar.setVisibility(View.GONE);
                            Toast.makeText(ShopPostingPreviewNew.this, getString(R.string.check_internet), Toast.LENGTH_LONG).show();
                            Log.i("fdfdfdfdfdf", "" + error.getMessage());

                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();

        }

    }
}
