package mimosale.com.shop;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import mimosale.com.R;

import mimosale.com.helperClass.CustomUtils;
import mimosale.com.helperClass.PrefManager;
import mimosale.com.home.HomeActivity;
import mimosale.com.network.RestInterface;
import mimosale.com.network.RetrofitClient;
import mimosale.com.network.WebServiceURLs;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.iceteck.silicompressorr.SiliCompressor;
import com.ms.square.android.expandabletextview.ExpandableTextView;
import com.viewpagerindicator.CirclePageIndicator;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.MultipartTypedOutput;
import retrofit.mime.TypedFile;
import retrofit.mime.TypedString;

public class ShopPreviewActivity extends AppCompatActivity implements View.OnClickListener {

ImageView iv_back;
Button btn_submit,btn_back;
Context context;
    Dialog dialog;
    private static ViewPager mPager;
    private static int currentPage = 0;
    private static int NUM_PAGES = 0;
    ViewPager view_pager_shop_details;
    ArrayList<ImageVideoData> image_thumbnail=new ArrayList<>();
    ArrayList<ImageVideoData> IMAGES;
    List<File> image_thumbnail1=new ArrayList<>();
     TabLayout tabLayout;
     LinearLayout ll_highlight,ll_info;
     String pref_id="";
     ProgressBar p_bar;
   // private static final Integer[] IMAGES= {R.drawable.sofa,R.drawable.prefences1,R.drawable.prefences2,R.drawable.prefences3};
    private ArrayList<Integer> ImagesArray = new ArrayList<Integer>();
    Intent i;
    String shop_name,shop_images,shop_desc,shop_category,min_discount,max_discount,start_date,end_date,min_price,max_price,pincode,city,address_line_1;
String state="",country="",lat="",lan="";
    String address_line_2,phone_number,hash_tag,web_url;

    TextView tv_discount,tv_price_range,tv_price_range_detail,tv_location,tv_website,tv_category,tv_tag,tv_shop_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_preview);
        i=getIntent();
        context=this;
        p_bar=findViewById(R.id.p_bar);
        shop_name=i.getStringExtra("shop_name");
        shop_desc=i.getStringExtra("shop_desc");
        pref_id=i.getStringExtra("pref_id");
        shop_category=i.getStringExtra("shop_category");
        min_discount=i.getStringExtra("min_discount");
        max_discount=i.getStringExtra("max_discount");
        start_date=i.getStringExtra("start_date");
        end_date=i.getStringExtra("end_date");
        min_price=i.getStringExtra("min_price");
        max_price=i.getStringExtra("max_price");
        pincode=i.getStringExtra("pincode");
        city=i.getStringExtra("city");
        address_line_1=i.getStringExtra("address_line_1");
        address_line_2=i.getStringExtra("address_line_2");
        phone_number=i.getStringExtra("phone_number");
        hash_tag=i.getStringExtra("hash_tag");
        web_url=i.getStringExtra("web_url");
       // image_thumbnail1=(List<File>) getIntent().getSerializableExtra("image_thumbnail");
        parseJSON();
        initView();





    }
    private void parseJSON() {
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<ImageVideoData>>(){}.getType();
        image_thumbnail = gson.fromJson(i.getStringExtra("shop_images"), type);
        for (ImageVideoData contact : image_thumbnail){
            Log.i("Contact Details",  ""+contact.getBitmap());
        }

        Type type2 = new TypeToken<ArrayList<File>>(){}.getType();
        image_thumbnail1 = gson.fromJson(i.getStringExtra("image_thumbnail"), type2);

    }
    public void initView()
    {
        tv_discount=findViewById(R.id.tv_discount);
        tv_shop_name=findViewById(R.id.tv_shop_name);
        tv_category=findViewById(R.id.tv_category);
        tv_price_range_detail=findViewById(R.id.tv_price_range_detail);
        tv_price_range=findViewById(R.id.tv_price_range);
        tv_location=findViewById(R.id.tv_location);
        tv_website=findViewById(R.id.tv_website);
        tv_tag=findViewById(R.id.tv_tag);
       tv_discount.setText(min_discount+"% - "+max_discount+"%");
       tv_price_range.setText(min_price+" - "+max_price);
       tv_price_range_detail.setText("\u00A5"+min_price+" - \u00A5"+max_price);
       tv_location.setText(address_line_1+" "+address_line_2+"\n"+city+" - "+pincode);
       tv_category.setText(shop_category);
       tv_tag.setText(hash_tag);
       tv_website.setText(web_url);
        tv_shop_name.setText(shop_name);
        ll_highlight=findViewById(R.id.ll_highlight);
        ll_info=findViewById(R.id.ll_info);

        iv_back=findViewById(R.id.iv_back);
        btn_submit=findViewById(R.id.btn_submit);
        btn_back=findViewById(R.id.btn_back);

        btn_submit.setOnClickListener(this);
        btn_back.setOnClickListener(this);
        iv_back.setOnClickListener(this);
          tabLayout = (TabLayout) findViewById(R.id.tabs);
        // Add five tabs.  Three have icons and two have text titles
        tabLayout.addTab(tabLayout.newTab().setText("Highlight"));
        tabLayout.addTab(tabLayout.newTab().setText("Info"));
        ExpandableTextView expTv1 = (ExpandableTextView) findViewById(R.id.expand_text_view)
                .findViewById(R.id.expand_text_view);


// IMPORTANT - call setText on the ExpandableTextView to set the text content to display
        expTv1.setText(shop_desc);


     /* for(int i=0;i<image_thumbnail.size();i++)
            ImagesArray.add(image_thumbnail[i]);*/

        mPager = (ViewPager) findViewById(R.id.pager);
        LayoutInflater  layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        mPager.setAdapter(new ShopSlidingImagesAdapter(image_thumbnail,layoutInflater,ShopPreviewActivity.this));


        CirclePageIndicator indicator = (CirclePageIndicator)
                findViewById(R.id.indicator);

        indicator.setViewPager(mPager);

        final float density = getResources().getDisplayMetrics().density;

//Set circle indicator radius
        indicator.setRadius(5 * density);

        NUM_PAGES =image_thumbnail.size();

        // Auto start of viewpager
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == NUM_PAGES) {
                    currentPage = 0;
                }
                mPager.setCurrentItem(currentPage++, true);
            }
        };
        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, 3000, 3000);

        // Pager listener over indicator
        indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                currentPage = position;

            }

            @Override
            public void onPageScrolled(int pos, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int pos) {

            }
        });

    }//initViewClose

    public void showPremimumDialog()
    {
         dialog=new Dialog(ShopPreviewActivity.this);
        dialog.setContentView(R.layout.dialog_go_premium);
        Button skip=dialog.findViewById(R.id.skip);
        Button btn_submit_dialog=dialog.findViewById(R.id.btn_submit_dialog);
        skip.setOnClickListener(this);
        btn_submit_dialog.setOnClickListener(this);
        dialog.show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.btn_back:
                finish();
                break;
            case R.id.btn_submit:
                addShopDetails();
                // showPremimumDialog();
                break;
            case R.id.skip:
                dialog.dismiss();
                break;
            case R.id.btn_submit_dialog:
                dialog.dismiss();
                break;
            case R.id.iv_back:
                finish();
                break;
        }
    }


    public void addShopDetails() {
        try {
            PrefManager.getInstance(context).getUserId();
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
            multipartTypedOutput.addPart("lat", new TypedString(lat));
            multipartTypedOutput.addPart("lon", new TypedString(lan));
            multipartTypedOutput.addPart("low_price", new TypedString(min_price));
            multipartTypedOutput.addPart("high_price", new TypedString(max_price));
            multipartTypedOutput.addPart("min_discount", new TypedString(min_discount));
            multipartTypedOutput.addPart("max_discount", new TypedString(max_discount));
            multipartTypedOutput.addPart("phone", new TypedString(phone_number));
            multipartTypedOutput.addPart("hash_tags", new TypedString(hash_tag));
            multipartTypedOutput.addPart("description", new TypedString(shop_desc));
            multipartTypedOutput.addPart("web_url", new TypedString(web_url));
            multipartTypedOutput.addPart("status", new TypedString("1"));
            multipartTypedOutput.addPart("user_id", new TypedString(PrefManager.getInstance(context).getUserId()));


            if (image_thumbnail.size() > 0) {
                for (int i = 0; i < image_thumbnail.size(); i++) {
                    multipartTypedOutput.addPart("shop_photos[]", new TypedFile("application/octet-stream", new File(image_thumbnail1.get(i).getAbsolutePath())));
                }
            } else {
                multipartTypedOutput.addPart("shop_photos", new TypedString(""));
            }


            RetrofitClient retrofitClient = new RetrofitClient();
            RestInterface service = retrofitClient.getAPIClient(WebServiceURLs.DOMAIN_NAME);
            service.addShopPosting("Bearer " + PrefManager.getInstance(context).getApiToken(),
                    multipartTypedOutput, new Callback<JsonElement>() {
                        @Override
                        public void success(JsonElement jsonElement, Response response) {
                            //this method call if webservice success
                            try {
                                JSONObject jsonObject = new JSONObject(jsonElement.toString());
                                String status = jsonObject.getString("status");

                                if (status.equals("1")) {

                                    Toast.makeText(context, "" + jsonObject.getString("message"), Toast.LENGTH_SHORT).show();

                                    new SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE)
                                            .setTitleText(getResources().getString(R.string.success))
                                            .setContentText(getResources().getString(R.string.shop_posting_success))
                                            .setConfirmText(getResources().getString(R.string.ok))

                                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                @Override
                                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                   startActivity(new Intent(ShopPreviewActivity.this,HomeActivity.class));
                                                   finish();
                                                }
                                            })

                                            .show();

                                } else {
                                    Toast.makeText(context, "" + jsonObject.getString("message"), Toast.LENGTH_SHORT).show();

                                }

                                p_bar.setVisibility(View.GONE);
                            } catch (JSONException | NullPointerException e) {
                                e.printStackTrace();

                            }
                        }

                        @Override
                        public void failure(RetrofitError error) {
                            p_bar.setVisibility(View.GONE);
                            Toast.makeText(context, getString(R.string.check_internet), Toast.LENGTH_LONG).show();
                            Log.i("fdfdfdfdfdf", "" + error.getMessage());

                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();

        }


    }


    class ImageCompressAsyncTask extends AsyncTask<List<String>, String, String> {
        Context mContext;
        Uri selectedImage;

        public ImageCompressAsyncTask(Context context) {
            mContext = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //  progressDialog.show();
        }

        @Override
        protected String doInBackground(List<String>... paths) {
            //final ArrayList<String> filePaths=
            //getImages(paths[0]);

            for (String filePaths : paths[0]) {
                //   final int finalI = i;
                String filePath = null;
                try {
                    filePath = SiliCompressor.with(ShopPreviewActivity.this).compress(filePaths, new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + getPackageName() + "/media/images"));
                    File imageFile = new File(filePath);
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    byte[] bt = bos.toByteArray();
                    String encodedImageString1 = Base64.encodeToString(bt, Base64.DEFAULT);
                    byte[] decodedString1 = Base64.decode(encodedImageString1.getBytes(), Base64.NO_WRAP);
                    Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString1, 0, decodedString1.length);
                    ImageVideoData image_v = new ImageVideoData();
                    image_v.setBitmap(decodedByte);
                    image_v.setPath(filePath);
                    IMAGES.add(image_v);
                  //  imageFiles.add(imageFile);
                    Thread.sleep(500);
                    //progressDialog.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return "";
        }


        @Override
        protected void onPostExecute(String compressedFilePath) {
            super.onPostExecute(compressedFilePath);
           /* adapter.notifyDataSetChanged();
            progressDialog.dismiss();
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
            progressDialog.dismiss();*/
            Log.i("Silicompressor", "Path: " + compressedFilePath);
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
        }
    }

    public static Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix,
                true);
    }
}
