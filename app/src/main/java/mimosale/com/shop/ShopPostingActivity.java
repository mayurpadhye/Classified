package mimosale.com.shop;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import mimosale.com.R;
import mimosale.com.helperClass.AppController;
import mimosale.com.helperClass.CustomFileUtils;
import mimosale.com.helperClass.CustomPermissions;
import mimosale.com.helperClass.CustomUtils;
import mimosale.com.helperClass.PlaceArrayAdapter;
import mimosale.com.helperClass.PrefManager;
import mimosale.com.network.RestInterface;
import mimosale.com.network.RetrofitClient;
import mimosale.com.network.WebServiceURLs;
import mimosale.com.post.SalePostingActivity;
import mimosale.com.preferences.AddNewPrefAdapter;
import mimosale.com.preferences.AllPrefPojo;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.iceteck.silicompressorr.SiliCompressor;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.MultipartTypedOutput;
import retrofit.mime.TypedFile;
import retrofit.mime.TypedString;

import static mimosale.com.helperClass.CustomUtils.IMAGE_LIMIT;
import static mimosale.com.helperClass.CustomUtils.VIDEO_LIMIT;
import static mimosale.com.helperClass.CustomUtils.showToast;

public class ShopPostingActivity extends AppCompatActivity implements View.OnClickListener
       {
    public ArrayList<File> imageFiles;
    Button btn_upload;
    RecyclerView rv_images;
    RadioGroup radioGroup;
    Switch switchbutton_discount, switchbutton_pincode;
    LinearLayout ll_shop_details, ll_pricing, ll_address_details, ll_others;
    private static final String LOG_TAG = "MainActivity";
    private static final int GOOGLE_API_CLIENT_ID = 0;
    private AutoCompleteTextView mAutocompleteTextView;
    boolean ll_shop_visible = true;
    boolean ll_pricing_visible = false;
    boolean ll_address_visible = false;
    boolean ll_other_visible = false;
TextView tv_url;
    ProgressDialog progressDialog;
    LinearLayout ll_discount;
    ArrayList<ImageVideoData> image_thumbnails, video_thumbnails;
    EventImagesAdapter adapter;
    LinearLayoutManager llm_images;
    Button btn_preview;
    Spinner sp_category;
    TextView toolbar_title;
    ImageView iv_back;
    ProgressBar p_bar;
    Context context;
    List<String> allPrefList = new ArrayList<>();
    List<String> allPrefIds = new ArrayList<>();
    String pref_id = "";
    Button btn_save;
    String lattitude="",langitude="";
    EditText et_start_date, et_end_date,et_city,et_state,et_country;
    TextView tv_other_details, tv_address_details, tv_pricing_details, tv_shop_details;
    TextInputLayout tl_shop_name, tl_shop_desc, tl_min_discount, tl_max_discount, tl_min_price, tl_max_price, tl_pincode, tl_city, tl_address_line1;
    TextInputLayout tl_address_line2, tl_phone_no, tl_hash_tag, tl_url, tl_end_date, tl_start_date;
    EditText et_shop_name, et_shop_desc, et_min_discount, et_max_discount, et_min_price, et_max_price, et_pincode,
            et_address_line1, et_address_line2, et_phone, et_hash_tag, et_url;


    private static final LatLngBounds BOUNDS_MOUNTAIN_VIEW = new LatLngBounds(
            new LatLng(37.398160, -122.180831), new LatLng(37.430610, -121.972090));

    final Calendar myCalendar = Calendar.getInstance();
            ProgressDialog pDialog;
           @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_posting);
        context = ShopPostingActivity.this;


        initView();
        getAllPrefData();

        btn_preview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shopPostingValidation("preview");


            }
        });


        et_pincode.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {

            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if (s.length()==7 || s.length()==8)
                getDataByPincode(s.toString());
                else {
                    et_city.setText("");
                    et_country.setText("");
                    et_state.setText("");
                }
            }
        });


        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shopPostingValidation("save");
            }
        });
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub

                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };

        final DatePickerDialog.OnDateSetListener date2 = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                setEndDate();
            }

        };
        et_end_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(context, date2, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                datePickerDialog.show();
            }
        });

        et_start_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatePickerDialog datePickerDialog = new DatePickerDialog(context, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                datePickerDialog.show();

            }
        });


        switchbutton_discount.setChecked(false);
        switchbutton_pincode.setChecked(false);
        switchbutton_discount.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean bChecked) {
                if (bChecked) {
                    ll_discount.setVisibility(View.VISIBLE);
                } else {
                    ll_discount.setVisibility(View.GONE);
                }
            }
        });
        switchbutton_pincode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean bChecked) {
                if (bChecked) {
                    tl_pincode.setVisibility(View.VISIBLE);
                } else {
                    tl_pincode.setVisibility(View.GONE);
                }
            }
        });




    }//onCreateClose


           public void getDataByPincode(String pincode)
           {
               String  tag_string_req = "string_req";
               URL url1 = null;
               String url = "https://maps.googleapis.com/maps/api/geocode/json?address='"+pincode+"'&key=AIzaSyC6in7wfj-jFmh4rINHmZ8Pu13IfqNvUYw&region=JP";

               final String encodedURL;
               try {
                   encodedURL = URLEncoder.encode(url, "UTF-8");
                    url1 = new URL(encodedURL);
               } catch (UnsupportedEncodingException e) {
                   e.printStackTrace();
               } catch (MalformedURLException e) {
                   e.printStackTrace();
               }



               pDialog.show();
               getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                       WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
               StringRequest strReq = new StringRequest(Request.Method.GET,
                       url, new com.android.volley.Response.Listener<String>() {
                   @Override
                   public void onResponse(String response) {
                       try {
                           JSONObject jsonObject=new JSONObject(response);

                           JSONArray results=jsonObject.getJSONArray("results");
                           for (int k=0;k<results.length();k++)
                           {
                               JSONObject j1=results.getJSONObject(k);
                               JSONObject geometry=j1.getJSONObject("geometry");
                               JSONObject location=geometry.getJSONObject("location");
                               String lat=location.getString("lat");
                               String lng=location.getString("lng");

                               Toast.makeText(context, ""+lat, Toast.LENGTH_SHORT).show();

                               getAddress(lat,lng);
                           }


                       } catch (JSONException e) {
                           pDialog.dismiss();
                           getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                           e.printStackTrace();
                       }


                   }

               }, new com.android.volley.Response.ErrorListener() {
                   @Override
                   public void onErrorResponse(VolleyError error) {
                       pDialog.dismiss();
                       getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                   }

               });

// Adding request to request queue
               AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
           }


           public void getAddress(String lat,String lng)
           {
               lattitude=lat;
               langitude=lng;
               String  tag_string_req = "string_req";
               URL url1 = null;
               String url = "https://maps.googleapis.com/maps/api/geocode/json?latlng="+lat+","+lng+"&key=AIzaSyC6in7wfj-jFmh4rINHmZ8Pu13IfqNvUYw";
Log.i("ttdtstd",""+url);
               final String encodedURL;
               try {
                   encodedURL = URLEncoder.encode(url, "UTF-8");
                   url1 = new URL(encodedURL);
               } catch (UnsupportedEncodingException e) {
                   e.printStackTrace();
               } catch (MalformedURLException e) {
                   e.printStackTrace();
               }



               getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                       WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
               StringRequest strReq = new StringRequest(Request.Method.GET,
                       url, new com.android.volley.Response.Listener<String>() {
                   @Override
                   public void onResponse(String response) {
                       try {
                           pDialog.dismiss();
                           JSONObject jsonObject=new JSONObject(response);

                           JSONArray results=jsonObject.getJSONArray("results");
                           for (int k=0;k<results.length();k++)
                           {
                               JSONObject j1=results.getJSONObject(k);
                               JSONArray address_components=j1.getJSONArray("address_components");
                               for (int j=0;j<address_components.length();j++)
                               {
                                   JSONObject j2=address_components.getJSONObject(j);
                                   JSONArray types=j2.getJSONArray("types");
                                   for (int i = 0; i < types.length(); i++) {
                                       if (types.get(i).equals("locality")){
                                           String long_name=j2.getString("long_name");

                                           et_city.setText(long_name);
                                       }
                                       if (types.get(i).equals("administrative_area_level_1"))
                                       {
                                           String long_name=j2.getString("long_name");

                                           et_state.setText(long_name);
                                       }
                                       if (types.get(i).equals("country"))
                                       {
                                           String long_name=j2.getString("long_name");

                                           et_country.setText(long_name);
                                       }
                                       if (types.get(i).equals("sublocality_level_4"))
                                       {
                                           String long_name=j2.getString("long_name");
                                           et_address_line1.setText(long_name);
                                       }

                                       if (types.get(i).equals("sublocality_level_2"))
                                       {
                                           String long_name=j2.getString("long_name");
                                           et_address_line1.setText(long_name);
                                       }
                                       if (types.get(i).equals("locality"))
                                       {
                                           String long_name=j2.getString("long_name");
                                           et_address_line2.setText(long_name);
                                       }
                                   }


                               }

                               getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                               //getAddress(lat,lng);
                           }


                       } catch (JSONException e) {
                           pDialog.dismiss();
                           getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                           e.printStackTrace();
                       }


                   }

               }, new com.android.volley.Response.ErrorListener() {
                   @Override
                   public void onErrorResponse(VolleyError error) {
                       pDialog.dismiss();
                       getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                   }

               });

// Adding request to request queue
               AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
           }


    public void slideUp(View view){
        view.setVisibility(View.GONE);
        TranslateAnimation animate = new TranslateAnimation(
                0,                 // fromXDelta
                0,                 // toXDelta
                view.getHeight(),  // fromYDelta
                0);                // toYDelta
        animate.setDuration(500);
        animate.setFillAfter(true);
        view.startAnimation(animate);
    }

    // slide the view from its current position to below itself
    public void slideDown(View view){

        TranslateAnimation animate = new TranslateAnimation(
                0,                 // fromXDelta
                0,                 // toXDelta
                0,                 // fromYDelta
               0); // toYDelta
        animate.setDuration(500);
        animate.setFillAfter(true);
        view.startAnimation(animate);
        view.setVisibility(View.VISIBLE);
    }


    private void updateLabel() {
        String myFormat = "yyyy/MM/dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.JAPAN);

        et_start_date.setText(sdf.format(myCalendar.getTime()));
    }

    private void setEndDate() {
        String myFormat = "yyyy/MM/dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.JAPAN);

        et_end_date.setText(sdf.format(myCalendar.getTime()));
    }

    public void getAllPrefData() {
        try {
            p_bar.setVisibility(View.VISIBLE);
            RetrofitClient retrofitClient = new RetrofitClient();
            RestInterface service = retrofitClient.getAPIClient(WebServiceURLs.DOMAIN_NAME);
            service.getAllPreferences(PrefManager.getInstance(context).getUserId(), "Bearer " + PrefManager.getInstance(context).getApiToken(), new Callback<JsonElement>() {
                @Override
                public void success(JsonElement jsonElement, Response response) {
                    //this method call if webservice success
                    try {
                        JSONObject jsonObject = new JSONObject(jsonElement.toString());
                        String status = jsonObject.getString("status");

                        if (status.equals("1")) {

                            JSONArray data = jsonObject.getJSONArray("data");
                            for (int k = 0; k < data.length(); k++) {
                                JSONObject j1 = data.getJSONObject(k);
                                String id = j1.getString("id");
                                String name = j1.getString("name");
                                allPrefList.add(name);
                                allPrefIds.add(id);
                            }
                            allPrefList.add(0, getResources().getString(R.string.select_category));
                            allPrefIds.add(0, getResources().getString(R.string.select_category));
                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                                    ShopPostingActivity.this,
                                    R.layout.row_language,
                                    allPrefList
                            );

                            sp_category.setAdapter(adapter);

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


    }//

    public void initView() {

         pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");

        mAutocompleteTextView = (AutoCompleteTextView) findViewById(R.id
                .autoCompleteTextView);
        mAutocompleteTextView.setThreshold(3);
        tv_url=findViewById(R.id.tv_url);
        ll_shop_details = findViewById(R.id.ll_shop_details);
        et_state = findViewById(R.id.et_state);
        et_country = findViewById(R.id.et_country);
        et_city = findViewById(R.id.et_city);
        ll_pricing = findViewById(R.id.ll_pricing);
        ll_address_details = findViewById(R.id.ll_address_details);
        ll_others = findViewById(R.id.ll_others);
        tv_other_details = findViewById(R.id.tv_other_details);
        tv_shop_details = findViewById(R.id.tv_shop_details);
        tv_address_details = findViewById(R.id.tv_address_details);
        tv_pricing_details = findViewById(R.id.tv_pricing_details);
        btn_save = findViewById(R.id.btn_save);
        et_start_date = findViewById(R.id.et_start_date);
        et_end_date = findViewById(R.id.et_end_date);
        radioGroup = (RadioGroup) findViewById(R.id.rg_address);
        radioGroup.clearCheck();
        p_bar = findViewById(R.id.p_bar);


        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton rb = (RadioButton) group.findViewById(checkedId);
                if (null != rb && checkedId > -1) {
                    Toast.makeText(ShopPostingActivity.this, rb.getText(), Toast.LENGTH_SHORT).show();
                    if (rb.getText().equals(getResources().getString(R.string.search_by_pincode))) {
                        tl_pincode.setVisibility(View.VISIBLE);
                    } else {
                        tl_pincode.setVisibility(View.GONE);
                    }

                }

            }
        });

        toolbar_title = findViewById(R.id.toolbar_title);
        sp_category = findViewById(R.id.sp_category);
        iv_back = findViewById(R.id.iv_back);
        tl_pincode = findViewById(R.id.tl_pincode);
        switchbutton_pincode = findViewById(R.id.switchbutton_pincode);
        switchbutton_discount = findViewById(R.id.switchbutton_discount);
        ll_discount = findViewById(R.id.ll_discount);
        toolbar_title.setText(getResources().getString(R.string.shop_posting));
        toolbar_title.setTextColor(getResources().getColor(R.color.black));
        btn_preview = findViewById(R.id.btn_preview);
        imageFiles = new ArrayList<>();
        llm_images = new LinearLayoutManager(getApplicationContext());
        llm_images.setOrientation(LinearLayoutManager.HORIZONTAL);
        progressDialog = new ProgressDialog(ShopPostingActivity.this);
        progressDialog.setTitle(getString(R.string.app_name));
        progressDialog.setMessage(getString(R.string.please_wait));
        progressDialog.setCanceledOnTouchOutside(false);
        image_thumbnails = new ArrayList<ImageVideoData>();
        adapter = new EventImagesAdapter(ShopPostingActivity.this, ShopPostingActivity.this, image_thumbnails, "create");
        video_thumbnails = new ArrayList<>();
        btn_upload = findViewById(R.id.btn_upload);
        rv_images = findViewById(R.id.rv_images);
        tl_shop_name = findViewById(R.id.tl_shop_name);
        tl_shop_desc = findViewById(R.id.tl_shop_desc);
        tl_min_discount = findViewById(R.id.tl_min_discount);
        tl_max_discount = findViewById(R.id.tl_max_discount);
        tl_min_price = findViewById(R.id.tl_min_price);
        tl_max_price = findViewById(R.id.tl_max_price);
        tl_city = findViewById(R.id.tl_city);
        tl_address_line1 = findViewById(R.id.tl_address_line1);
        tl_address_line2 = findViewById(R.id.tl_address_line2);
        tl_phone_no = findViewById(R.id.tl_phone_no);
        tl_hash_tag = findViewById(R.id.tl_hash_tag);
        tl_url = findViewById(R.id.tl_url);
        et_shop_name = findViewById(R.id.et_shop_name);
        et_shop_desc = findViewById(R.id.et_shop_desc);
        et_min_discount = findViewById(R.id.et_min_discount);
        et_max_discount = findViewById(R.id.et_max_discount);
        et_min_price = findViewById(R.id.et_min_price);
        et_max_price = findViewById(R.id.et_max_price);
        et_pincode = findViewById(R.id.et_pincode);
        et_address_line1 = findViewById(R.id.et_address_line1);
        et_address_line2 = findViewById(R.id.et_address_line2);
        et_phone = findViewById(R.id.et_phone);
        et_hash_tag = findViewById(R.id.et_hash_tag);
        et_url = findViewById(R.id.et_url);
        tl_end_date = findViewById(R.id.tl_end_date);
        tl_start_date = findViewById(R.id.tl_start_date);


        btn_upload.setOnClickListener(this);
        btn_preview.setOnClickListener(this);
        iv_back.setOnClickListener(this);

        sp_category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = parent.getItemAtPosition(position).toString();
                if (selectedItem.equals(getResources().getString(R.string.select_category))) {
                    pref_id = "";
                } else {
                    pref_id = allPrefIds.get(position);
                }
            } // to close the onItemSelected

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }//initViewclose


    public void shopPostingValidation(String type) {

        if (et_shop_name.getText().toString().trim().length() == 0) {
            if (!ll_shop_visible)
                slideDown(ll_shop_details);
            et_shop_name.requestFocus();
            tl_shop_name.setError(getResources().getString(R.string.enter_shop_name));
            return;
        } else {
            tl_shop_name.setError(null);
        }

        if (et_shop_desc.getText().toString().trim().length() ==0) {
            if (!ll_shop_visible)
                slideDown(ll_shop_details);
            et_shop_desc.requestFocus();
            tl_shop_desc.setError(getResources().getString(R.string.shop_desc_error));

            return;
        } else {
            tl_shop_desc.setError(null);
        }

        if (pref_id.equals("")) {
            if (!ll_shop_visible)
                slideDown(ll_shop_details);
            Toast.makeText(context, getResources().getString(R.string.please_select_pref_category), Toast.LENGTH_SHORT).show();
            return;
        }


        if (et_min_price.getText().toString().trim().length() == 0) {
            if (!ll_pricing_visible)
                slideDown(ll_pricing);
            et_min_price.requestFocus();
            tl_min_price.setError(getResources().getString(R.string.min_price_error));
            return;
        } else {
            tl_min_price.setError(null);
        }

        if (et_max_price.getText().toString().trim().length() == 0) {
            if (!ll_pricing_visible)
                slideDown(ll_pricing);
            et_max_price.requestFocus();
            tl_max_price.setError(getResources().getString(R.string.max_price_error));
            return;
        } else {
            tl_max_price.setError(null);
        }

        if (!et_min_price.getText().toString().trim().isEmpty() && !et_max_price.getText().toString().trim().isEmpty()) {
            if (Integer.parseInt(et_min_price.getText().toString().trim()) > Integer.parseInt(et_max_price.getText().toString().trim())) {
                showErrorDialog("Oops", getResources().getString(R.string.min_price_greater));
                return;
            } else if (Integer.parseInt(et_max_price.getText().toString().trim()) < Integer.parseInt(et_min_price.getText().toString().trim())) {
                showErrorDialog("Oops", getResources().getString(R.string.max_price_smaller));
                return;

            }
        }

        if (et_pincode.getText().toString().trim().length() == 0) {
            if (!ll_address_visible)
                slideDown(ll_address_details);
            et_pincode.requestFocus();
            tl_pincode.setError(getResources().getString(R.string.enter_pincode));
            return;

        } else {
            tl_pincode.setError(null);
        }

        if (et_city.getText().toString().trim().length()==0)
        {
            et_city.requestFocus();
            tl_city.setError(getResources().getString(R.string.enter_city));
            return;
        }
        else {
            tl_city.setError(null);
        }

        if (et_address_line1.getText().toString().trim().length() == 0) {
            if (!ll_address_visible)
                slideDown(ll_address_details);
            et_address_line1.requestFocus();
            tl_address_line1.setError(getResources().getString(R.string.enter_address));
            return;

        } else {
            tl_address_line1.setError(null);
        }


        if (et_address_line2.getText().toString().trim().length() == 0) {
            if (!ll_address_visible)
                slideDown(ll_address_details);
            et_address_line2.requestFocus();
            tl_address_line2.setError(getResources().getString(R.string.enter_complete_address));
            return;

        } else {
            tl_address_line2.setError(null);
        }

        if (!isValidMobile(et_phone.getText().toString().trim())) {
            if (!ll_address_visible)
                slideDown(ll_address_details);
            et_phone.requestFocus();
            if (et_phone.getText().toString().trim().length() == 0) {
                tl_phone_no.setError(getResources().getString(R.string.enter_mobile_no));
            } else {
                tl_phone_no.setError(getResources().getString(R.string.mobile_error));
            }

            return;
        }

        if (et_hash_tag.getText().toString().trim().length() == 0) {
            if (!ll_other_visible)
                slideDown(ll_others);
            et_hash_tag.requestFocus();
            tl_hash_tag.setError(getResources().getString(R.string.enter_hash_tag));
            return;
        } else {
            tl_hash_tag.setError(null);
        }

        if (imageFiles.size() <= 1) {
            if (!ll_shop_visible)
                slideDown(ll_shop_details);
            Toast.makeText(context, ""+getResources().getString(R.string.please_select_atleast_two_images), Toast.LENGTH_SHORT).show();
            return;
        }

        if (et_min_discount.getText().toString().length() != 0 || et_max_discount.getText().toString().length() != 0) {
            if (!ll_pricing_visible)
                slideDown(ll_pricing);
            if (et_min_discount.getText().toString().trim().length() > et_max_discount.getText().toString().trim().length()) {
                tl_min_discount.setError(""+getResources().getString(R.string.min_discount_error));
                return;
            } else if (et_max_discount.getText().toString().trim().length() < et_min_discount.getText().toString().trim().length()) {
                tl_max_discount.setError(""+getResources().getString(R.string.max_dis_error));
                return;
            }
            if (et_start_date.getText().toString().trim().length() != 0 || et_end_date.getText().toString().trim().length() != 0) {
                if (!ll_pricing_visible)
                    slideDown(ll_pricing);
                if (et_start_date.getText().toString().trim().length() == 0) {
                    tl_start_date.setError(""+getResources().getString(R.string.enter_start_date));
                    return;
                } else if (et_end_date.getText().toString().trim().length() == 0) {
                    tl_end_date.setError(getResources().getString(R.string.enter_end_date));
                    return;
                }

                if (!isDateAfter(et_start_date.getText().toString().trim(), et_end_date.getText().toString().trim())) {
                    if (!ll_pricing_visible)
                        slideDown(ll_pricing);
                    tl_start_date.setError(getResources().getString(R.string.end_date_must_be_greater));
                    return;
                }
            }

        }

if (type.equals("save"))
{
    addShopDetails();
}
else
{
    Intent i=new Intent(ShopPostingActivity.this,ShopPreviewActivity.class);
    i.putExtra("shop_name",et_shop_name.getText().toString());
    JsonArray jsonElements = (JsonArray) new Gson().toJsonTree(image_thumbnails);
    i.putExtra("shop_images",jsonElements.toString());
    i.putExtra("shop_desc",et_shop_desc.getText().toString());
    i.putExtra("shop_category",sp_category.getSelectedItem().toString());
    i.putExtra("min_discount",et_min_discount.getText().toString());
    i.putExtra("max_discount",et_max_discount.getText().toString());
    i.putExtra("start_date",et_start_date.getText().toString());
    i.putExtra("end_date",et_end_date.getText().toString());
    i.putExtra("min_price",et_min_price.getText().toString());
    i.putExtra("max_price",et_max_price.getText().toString());
    i.putExtra("pincode",et_pincode.getText().toString());
    i.putExtra("city",et_city.getText().toString());
    i.putExtra("address_line_1",et_address_line1.getText().toString());
    i.putExtra("address_line_2",et_address_line2.getText().toString());
    i.putExtra("phone_number",et_phone.getText().toString());
    i.putExtra("hash_tag",et_hash_tag.getText().toString());
    i.putExtra("web_url",et_url.getText().toString());
    i.putExtra("pref_id",pref_id);
    i.putExtra("state",et_state.getText().toString());
    i.putExtra("country",et_country.getText().toString());
    i.putExtra("lat",lattitude);
    i.putExtra("lan",langitude);
    JsonArray jsonElements1 = (JsonArray) new Gson().toJsonTree(imageFiles);
    i.putExtra("image_thumbnail",jsonElements1.toString());


    startActivity(i);
}


    }


    public static boolean isDateAfter(String startDate, String endDate) {
        try {
            String myFormatString = "yyyy/MM/dd"; // for example
            SimpleDateFormat df = new SimpleDateFormat(myFormatString);
            Date date1 = df.parse(endDate);
            Date startingDate = df.parse(startDate);

            if (date1.after(startingDate))
                return true;
            else
                return false;
        } catch (Exception e) {

            return false;
        }
    }

    public void addShopDetails() {
        try {
            PrefManager.getInstance(context).getUserId();
            p_bar.setVisibility(View.VISIBLE);

            MultipartTypedOutput multipartTypedOutput = new MultipartTypedOutput();
            multipartTypedOutput.addPart("name", new TypedString(et_shop_name.getText().toString().trim()));
            multipartTypedOutput.addPart("preference_id", new TypedString(pref_id));
            multipartTypedOutput.addPart("address_line1", new TypedString(et_address_line1.getText().toString().trim()));
            multipartTypedOutput.addPart("address_line2", new TypedString(et_address_line2.getText().toString().trim()));
            multipartTypedOutput.addPart("city", new TypedString(et_city.getText().toString()));
            multipartTypedOutput.addPart("state", new TypedString("AM"));
            multipartTypedOutput.addPart("country", new TypedString("AM"));
            multipartTypedOutput.addPart("pincode", new TypedString(et_pincode.getText().toString()));
            multipartTypedOutput.addPart("lat", new TypedString("22.22"));
            multipartTypedOutput.addPart("lon", new TypedString("20.22"));
            multipartTypedOutput.addPart("low_price", new TypedString(et_min_price.getText().toString()));
            multipartTypedOutput.addPart("high_price", new TypedString(et_max_price.getText().toString()));
            multipartTypedOutput.addPart("min_discount", new TypedString(et_min_discount.getText().toString()));
            multipartTypedOutput.addPart("max_discount", new TypedString(et_max_discount.getText().toString()));
            multipartTypedOutput.addPart("phone", new TypedString(et_phone.getText().toString()));
            multipartTypedOutput.addPart("hash_tags", new TypedString(et_hash_tag.getText().toString()));
            multipartTypedOutput.addPart("description", new TypedString(et_shop_desc.getText().toString()));
            multipartTypedOutput.addPart("web_url", new TypedString(tv_url.getText().toString()+""+et_url.getText().toString()));
            multipartTypedOutput.addPart("status", new TypedString("1"));
            multipartTypedOutput.addPart("user_id", new TypedString(PrefManager.getInstance(context).getUserId()));


            if (imageFiles.size() > 0) {
                for (int i = 0; i < imageFiles.size(); i++) {
                    multipartTypedOutput.addPart("shop_photos[]", new TypedFile("application/octet-stream", new File(imageFiles.get(i).getAbsolutePath())));
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


    private boolean isValidMobile(String phone) {
        boolean check = false;
        if (!Pattern.matches("[a-zA-Z]+", phone)) {
            if (phone.length() < 8 || phone.length() > 15) {
                // if(phone.length() != 10) {
                check = false;
                tl_phone_no.setError(getResources().getString(R.string.not_a_valid_number));
            } else {
                check = true;
            }
        } else {
            check = false;
        }
        return check;
    }

    public void showErrorDialog(String title, String msg) {
        new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                .setTitleText("Oops...")
                .setContentText(msg)

                .show();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_upload:
                CustomUtils.hideKeyboard(v, getApplicationContext());
                if (imageFiles.size() >= IMAGE_LIMIT) {
                    CustomUtils.showAlertDialog(ShopPostingActivity.this, getString(R.string.can_not_share_more_than_five_images));
                } else {
                    selectImage();
                }
                break;

            case R.id.btn_preview:
                startActivity(new Intent(ShopPostingActivity.this, ShopPreviewActivity.class));
                //SaveShopPostData();
                break;
            case R.id.iv_back:
                finish();
                break;
        }

    }

    private void selectImage() {
        final String[] items = new String[]{ getString(R.string.camera), getString(R.string.gallery)};

        AlertDialog.Builder ad = new AlertDialog.Builder(this);
        ad.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                switch (i) {
                    case 0:
                        boolean result_camera = CustomPermissions.checkCameraPermission(ShopPostingActivity.this);
                        if (result_camera) {cameraImageIntent();}
                        break;

                    case 1:
                        boolean result_gallery = CustomPermissions.checkPermissionForFileAccess(ShopPostingActivity.this);
                        if (result_gallery) {
                            galleryImageIntent();
                        }
                        break;
                }
            }
        });
        ad.show();
    }

    private void galleryImageIntent() {


        try {
            //Open the specific App Info page:
            Intent intent = new Intent(
                    Intent.ACTION_PICK,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
            intent.setData(Uri.parse("package:" + "com.android.providers.downloads"));
            intent.setType("image/*");
            startActivityForResult(Intent.createChooser(intent, getString(R.string.select)), CustomPermissions.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);

        } catch ( ActivityNotFoundException e ) {
            e.printStackTrace();

            //Open the generic Apps page:
            Intent intent = new Intent(
                    Intent.ACTION_PICK,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
            intent.setType("image/*");
            startActivityForResult(Intent.createChooser(intent, getString(R.string.select)), CustomPermissions.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
        }

      /*  Intent intent = new Intent(
                Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, getString(R.string.select)), CustomPermissions.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);*/
    }

    private void cameraImageIntent() {

        Intent i = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            PackageManager pm = getPackageManager();

            final ResolveInfo mInfo = pm.resolveActivity(i, 0);

            Intent intent = new Intent();
            intent.setComponent(new ComponentName(mInfo.activityInfo.packageName, mInfo.activityInfo.name));
            intent.setAction(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            startActivityForResult(i, CustomPermissions.MY_PERMISSIONS_REQUEST_CAMERA);
        } catch (Exception e) {
            Log.i("launch_camera", "Unable to launch camera: " + e);
        }


    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onActivityResult(int requestCode, int resultCode, final Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case CustomPermissions.MY_PERMISSIONS_REQUEST_CAMERA:
                    Bitmap bitmap_thumbnail = (Bitmap) data.getExtras().get("data");
                    bitmap_thumbnail.getByteCount();
                    Uri tempUri = CustomUtils.getImageUri(getApplicationContext(), bitmap_thumbnail);
                    File finalFile = new File(CustomUtils.getRealPathFromURI(getApplicationContext(), tempUri));
                    CustomUtils.showLog("Camera File path ", finalFile.getAbsolutePath() + "");
                    if (imageFiles.size() <= CustomUtils.IMAGE_LIMIT) {
                        ArrayList<String> selected_image = new ArrayList<>();
                        selected_image.add(tempUri.toString());
                        new ImageCompressAsyncTask(this).execute(selected_image);
                    } else {
                        CustomUtils.showAlertDialog(ShopPostingActivity.this, getString(R.string.can_not_share_more_than_five_images));
                    }
                    break;

                case CustomPermissions.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                    if (data.getClipData() != null) {
                        int count = data.getClipData().getItemCount();
                        int imageCount = imageFiles.size();
                        int selectedImages = imageCount + count;
                        if (selectedImages > IMAGE_LIMIT) {
                            CustomUtils.showAlertDialog(ShopPostingActivity.this, getString(R.string.can_not_share_more_than_five_images));
                            return;
                        } else {
                            ArrayList<String> images = new ArrayList<>();

                            for (int i = 0; i < count; i++) {
                                images.add(data.getClipData().getItemAt(i).getUri().toString());
                                //  CustomUtil.showLog("Image selected ", data.getClipData().getItemAt(i).getUri().toString());

                                String picturePath = CustomFileUtils.getPath(getApplicationContext(), data.getClipData().getItemAt(i).getUri());
                                File file = new File(picturePath);
                                imageFiles.add(file);
                                long lengthInMB = lengthInMB = (file.length() / 1024) / 1024;
                                ArrayList<String> image = new ArrayList<>();
                                image.add(data.getClipData().getItemAt(i).getUri().toString());

                                if (lengthInMB >= 20) {
                                    new ImageCompressAsyncTask(this).execute(image);
                                } else {

                                    Bitmap bitmap = BitmapFactory.decodeFile(picturePath);
                                    ImageVideoData image_v = new ImageVideoData();
                                  /* image_v.setBitmap(bitmap);
                                    image_v.setPath(picturePath);
                                    image_thumbnails.add(image_v);
*/

                                    ExifInterface ei = null;
                                    try {
                                        ei = new ExifInterface(picturePath);
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                                            ExifInterface.ORIENTATION_UNDEFINED);

                                    Bitmap rotatedBitmap = null;
                                    switch (orientation) {

                                        case ExifInterface.ORIENTATION_ROTATE_90:
                                            rotatedBitmap = rotateImage(bitmap, 90);
                                            break;

                                        case ExifInterface.ORIENTATION_ROTATE_180:
                                            rotatedBitmap = rotateImage(bitmap, 180);
                                            break;

                                        case ExifInterface.ORIENTATION_ROTATE_270:
                                            rotatedBitmap = rotateImage(bitmap, 270);
                                            break;

                                        case ExifInterface.ORIENTATION_NORMAL:
                                        default:
                                            rotatedBitmap = bitmap;
                                    }
                                    //File file1 = new File(String.valueOf(data.getData()));

                                    image_v.setBitmap(rotatedBitmap);
                                    image_v.setPath(picturePath);
                                    image_thumbnails.add(image_v);

                                       /* adapter = new EventImagesAdapter(getApplicationContext(), CreateEventActivity.this, image_thumbnails);
                                        rv_event_image.setLayoutManager(llm_images);
                                        rv_event_image.setAdapter(adapter);*/
                                }


                            }

                            //    new ImageCompressAsyncTask(CreateEventActivity.this).execute(images);
                            if (imageFiles.size() <= IMAGE_LIMIT) {
                                adapter = new EventImagesAdapter(ShopPostingActivity.this, ShopPostingActivity.this, image_thumbnails, "create");
                                rv_images.setLayoutManager(llm_images);
                                rv_images.setAdapter(adapter);
                            } else {
                                //showToast(getString(R.string.can_not_share_more_than_five_images), CreateEventActivity.this);
                                if (imageCount <= imageFiles.size()) {
                                    for (int i = imageFiles.size(); i > imageCount; i--) {
                                        image_thumbnails.remove(i - 1);
                                        imageFiles.remove(i - 1);
                                    }

                                }
                                CustomUtils.showAlertDialog(ShopPostingActivity.this, getString(R.string.can_not_share_more_than_five_images));
                            }
                        }
                    } else if (data.getData() != null) {
                        Uri selectedImage = data.getData();
                        String picturePath = CustomFileUtils.getPath(getApplicationContext(), selectedImage);
                        File file = new File(picturePath);
                        imageFiles.add(file);
                        if (imageFiles.size() <= IMAGE_LIMIT) {
                            long lengthInMB = (file.length() / 1024) / 1024;
                            ArrayList<String> image = new ArrayList<>();
                            image.add(selectedImage.toString());
                            if (lengthInMB >= 20) {
                                new ImageCompressAsyncTask(this).execute(image);
                            } else {

                                Bitmap bitmap = BitmapFactory.decodeFile(picturePath);
                                ImageVideoData image_v = new ImageVideoData();
                             /* image_v.setBitmap(bitmap);
                                image_v.setPath(picturePath);
                                image_thumbnails.add(image_v);*/

                                ExifInterface ei = null;
                                try {
                                    ei = new ExifInterface(picturePath);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                                        ExifInterface.ORIENTATION_UNDEFINED);

                                Bitmap rotatedBitmap = null;
                                switch (orientation) {

                                    case ExifInterface.ORIENTATION_ROTATE_90:
                                        rotatedBitmap = rotateImage(bitmap, 90);
                                        break;

                                    case ExifInterface.ORIENTATION_ROTATE_180:
                                        rotatedBitmap = rotateImage(bitmap, 180);
                                        break;

                                    case ExifInterface.ORIENTATION_ROTATE_270:
                                        rotatedBitmap = rotateImage(bitmap, 270);
                                        break;

                                    case ExifInterface.ORIENTATION_NORMAL:
                                    default:
                                        rotatedBitmap = bitmap;
                                }
                                //File file1 = new File(String.valueOf(data.getData()));

                                image_v.setBitmap(rotatedBitmap);
                                image_v.setPath(picturePath);
                                image_thumbnails.add(image_v);
                                adapter = new EventImagesAdapter(ShopPostingActivity.this, ShopPostingActivity.this, image_thumbnails, "create");
                                rv_images.setLayoutManager(llm_images);
                                rv_images.setAdapter(adapter);
                            }
                        } else {
                            CustomUtils.showAlertDialog(ShopPostingActivity.this, getString(R.string.can_not_share_more_than_five_images));
                        }
                    }
                    break;


            }
        }

    }

    public class ImageCompressAsyncTask extends AsyncTask<List<String>, String, String> {
        Context mContext;
        Uri selectedImage;

        public ImageCompressAsyncTask(Context context) {
            mContext = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(ShopPostingActivity.this);
            progressDialog.setTitle(getString(R.string.app_name));
            progressDialog.setMessage(getString(R.string.please_wait));
            progressDialog.setCanceledOnTouchOutside(false);
            //  progressDialog.show();
        }

        @Override
        protected String doInBackground(List<String>... paths) {
            //final ArrayList<String> filePaths=
            //getImages(paths[0]);
            progressDialog.dismiss();
            for (String filePaths : paths[0]) {
                //   final int finalI = i;
                String filePath = null;
                try {
                    filePath = SiliCompressor.with(ShopPostingActivity.this).compress(filePaths, new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + getPackageName() + "/media/images"));
                    File imageFile = new File(filePath);
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    byte[] bt = bos.toByteArray();
                    String encodedImageString1 = Base64.encodeToString(bt, Base64.DEFAULT);
                    byte[] decodedString1 = Base64.decode(encodedImageString1.getBytes(), Base64.NO_WRAP);
                    Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString1, 0, decodedString1.length);
                    ImageVideoData image_v = new ImageVideoData();
                    image_v.setBitmap(decodedByte);
                    image_v.setPath(filePath);
                    image_thumbnails.add(image_v);
                    imageFiles.add(imageFile);
                    Thread.sleep(500);
                    progressDialog.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return "";
        }


        @Override
        protected void onPostExecute(String compressedFilePath) {
            super.onPostExecute(compressedFilePath);
            adapter.notifyDataSetChanged();
            progressDialog.dismiss();
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
            progressDialog.dismiss();
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
