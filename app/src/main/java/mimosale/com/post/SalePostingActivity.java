package mimosale.com.post;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import mimosale.com.R;
import mimosale.com.helperClass.PrefManager;
import mimosale.com.network.RestInterface;
import mimosale.com.network.RetrofitClient;
import mimosale.com.network.WebServiceURLs;
import mimosale.com.products.AddProductsActivity;
import com.google.gson.JsonElement;
import com.thomashaertel.widget.MultiSpinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.MultipartTypedOutput;
import retrofit.mime.TypedFile;
import retrofit.mime.TypedString;

public class SalePostingActivity extends AppCompatActivity {
    TextView toolbar_title;
    ImageView iv_back;
    Spinner sp_shops;
    MultiSpinner sp_products;

    ProgressBar p_bar;
    Spinner sp_products_details;
    List<String> shopnameList = new ArrayList<>();
    List<String> shopId = new ArrayList<>();
    List<String> product_id_list = new ArrayList<>();
    List<String> product_name_list = new ArrayList<>();
    final Calendar myCalendar = Calendar.getInstance();
    String shop_id = "";
    String product_id = "";
    Button btn_submit;
    TextInputLayout tl_add_url, tl_short_desc, tl_hash_tag, tl_min_discount, tl_max_discount, tl_start_date, tl_end_date, tl_title;
    EditText et_title, et_start_date, et_end_date, et_min_discount, et_max_discount, et_short_desc, et_hash_tag, et_add_url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sale_posting);
        initView();


        getUserShop();

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateForm();
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

        sp_shops.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (!sp_shops.getSelectedItem().toString().equals(getResources().getString(R.string.select_shop))) {
                    shop_id = shopId.get(position);
                    getShopWiseProductList(shopId.get(position));
                } else {
                    shop_id = "";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        sp_products_details.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (!sp_products_details.getSelectedItem().toString().equals(getResources().getString(R.string.select_product))) {
                    product_id = product_id_list.get(position);

                } else {
                    product_id = "";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        et_start_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatePickerDialog datePickerDialog = new DatePickerDialog(SalePostingActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                datePickerDialog.show();

            }
        });

        et_end_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(SalePostingActivity.this, date2, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                datePickerDialog.show();
            }
        });


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


    public void validateForm() {
        if (et_title.getText().toString().trim().length() == 0) {

            et_title.requestFocus();
            tl_title.setError(getResources().getString(R.string.enter_sale_title));
            return;
        } else {
            tl_title.setError(null);
        }

        if (et_short_desc.getText().toString().trim().length() ==0) {

            et_short_desc.requestFocus();
            tl_short_desc.setError(getResources().getString(R.string.enter_sale_desc));

            return;
        } else {
            tl_short_desc.setError(null);
        }

        if (et_start_date.getText().toString().trim().length() != 0 || et_end_date.getText().toString().trim().length() != 0) {

            if (et_start_date.getText().toString().trim().length() == 0) {
                tl_start_date.setError("" + getResources().getString(R.string.enter_start_date));
                return;
            } else if (et_end_date.getText().toString().trim().length() == 0) {
                tl_end_date.setError(getResources().getString(R.string.enter_end_date));
                return;
            }

            if (!isDateAfter(et_start_date.getText().toString().trim(), et_end_date.getText().toString().trim())) {

                tl_start_date.setError(getResources().getString(R.string.end_date_must_be_greater));
                return;
            }
        }


        if (et_hash_tag.getText().toString().trim().length() == 0) {

            et_hash_tag.requestFocus();
            tl_hash_tag.setError(getResources().getString(R.string.enter_hash_tag));

            return;
        } else {
            tl_hash_tag.setError(null);
        }

        if (sp_shops.getSelectedItem().toString().equals(getResources().getString(R.string.select_shop))) {
            Toast.makeText(this, "" + getResources().getString(R.string.select_shop), Toast.LENGTH_SHORT).show();
            return;
        }

        if (sp_products_details.getSelectedItem().toString().equals(getResources().getString(R.string.select_product))) {
            Toast.makeText(this, "" + getResources().getString(R.string.select_product), Toast.LENGTH_SHORT).show();
            return;
        }
        if (shop_id.equals("")) {
            Toast.makeText(this, "" + getResources().getString(R.string.select_shop), Toast.LENGTH_SHORT).show();
            return;

        }
        if (product_id.equals("")) {
            Toast.makeText(this, "" + getResources().getString(R.string.select_product), Toast.LENGTH_SHORT).show();
            return;

        }

        if (et_min_discount.getText().toString().trim().length()>0 && et_max_discount.getText().toString().trim().length()>0)
        {
            if (et_min_discount.getText().toString().trim().length() > et_max_discount.getText().toString().trim().length()) {
                tl_min_discount.setError(""+getResources().getString(R.string.min_discount_error));
                return;
            } else if (et_max_discount.getText().toString().trim().length() < et_min_discount.getText().toString().trim().length()) {
                tl_max_discount.setError(""+getResources().getString(R.string.max_dis_error));
                return;
            }
        }
        else
        {
            Toast.makeText(this, ""+getResources().getString(R.string.please_add_discount), Toast.LENGTH_SHORT).show();
            return;
        }


        saveSalePosting();
    }

    public void saveSalePosting() {
        try {
p_bar.setVisibility(View.VISIBLE);
            String title = et_title.getText().toString();
            String min_discount = et_min_discount.getText().toString();
            String max_discount = et_max_discount.getText().toString();
            String hash_tags = et_hash_tag.getText().toString();
            String description = et_short_desc.getText().toString();
            String web_url = et_add_url.getText().toString();
            String user_id = PrefManager.getInstance(SalePostingActivity.this).getUserId();

JSONArray product_ids=new JSONArray();
product_ids.put(product_id);
            RetrofitClient retrofitClient = new RetrofitClient();
            RestInterface service = retrofitClient.getAPIClient(WebServiceURLs.DOMAIN_NAME);
            service.addSalePosting(title,shop_id,product_ids.toString(),min_discount,max_discount,hash_tags,description,web_url,user_id,"Bearer " + PrefManager.getInstance(SalePostingActivity.this).getApiToken(),
                    new Callback<JsonElement>() {
                        @Override
                        public void success(JsonElement jsonElement, Response response) {
                            //this method call if webservice success
                            try {
                                p_bar.setVisibility(View.GONE);
                                JSONObject jsonObject = new JSONObject(jsonElement.toString());
                                String status = jsonObject.getString("status");

                                if (status.equals("1")) {

                                    Toast.makeText(SalePostingActivity.this, "" + jsonObject.getString("message"), Toast.LENGTH_SHORT).show();

                                    new SweetAlertDialog(SalePostingActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                                            .setTitleText(getResources().getString(R.string.success))

                                            .setContentText("" + jsonObject.getString("message"))
                                            .setConfirmText(getResources().getString(R.string.ok))
                                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                @Override
                                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                    finish();
                                                }
                                            })
                                            .show();

                                } else {
                                    p_bar.setVisibility(View.GONE);
                                    Toast.makeText(SalePostingActivity.this, "" + jsonObject.getString("message"), Toast.LENGTH_SHORT).show();

                                }

                                p_bar.setVisibility(View.GONE);
                            } catch (JSONException | NullPointerException e) {
                                e.printStackTrace();

                            }
                        }

                        @Override
                        public void failure(RetrofitError error) {
                            p_bar.setVisibility(View.GONE);
                            Toast.makeText(SalePostingActivity.this, getString(R.string.check_internet), Toast.LENGTH_LONG).show();
                            Log.i("fdfdfdfdfdf", "" + error.getMessage());

                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();

        }

    }//


    public void getUserShop() {
        try {
            p_bar.setVisibility(View.VISIBLE);
            Log.i("show_details", "Bearer " + PrefManager.getInstance(SalePostingActivity.this).getApiToken());
            RetrofitClient retrofitClient = new RetrofitClient();
            RestInterface service = retrofitClient.getAPIClient(WebServiceURLs.DOMAIN_NAME);
            service.getUserShop(PrefManager.getInstance(SalePostingActivity.this).getUserId(), "Bearer " + PrefManager.getInstance(SalePostingActivity.this).getApiToken(), new Callback<JsonElement>() {
                @Override
                public void success(JsonElement jsonElement, Response response) {
                     try {

                        JSONObject jsonObject = new JSONObject(jsonElement.toString());
                        String status = jsonObject.getString("status");

                        if (status.equals("1")) {
                            JSONArray data = jsonObject.getJSONArray("data");
                            for (int i = 0; i < data.length(); i++) {
                                JSONObject j1 = data.getJSONObject(i);
                                String id = j1.getString("id");
                                String name = j1.getString("name");
                                shopnameList.add(name);
                                shopId.add(id);


                            }
                            if (data.length()>0) {
                                shopnameList.add(0, "" + getResources().getString(R.string.select_shop));
                                shopId.add(0, "selct id");
                                ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                                        SalePostingActivity.this,
                                        android.R.layout.simple_spinner_item,
                                        shopnameList
                                );
                                sp_shops.setAdapter(adapter);
                            }

                            else
                            {
                                new SweetAlertDialog(SalePostingActivity.this, SweetAlertDialog.WARNING_TYPE)
                                        .setTitleText("No Shop Available")

                                        .setContentText("Please Add Shop to continue")
                                        .setConfirmText(getResources().getString(R.string.ok))
                                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                finish();
                                            }
                                        })
                                        .show();
                            }
                        }

                        else
                        {

                        }
                        p_bar.setVisibility(View.GONE);

                    } catch (JSONException | NullPointerException e) {
                        e.printStackTrace();

                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    p_bar.setVisibility(View.GONE);
                    Toast.makeText(SalePostingActivity.this, getResources().getString(R.string.check_internet), Toast.LENGTH_LONG).show();
                    Log.i("fdfdfdfdfdf", "" + error.getMessage());

                }
            });
        } catch (Exception e) {
            p_bar.setVisibility(View.GONE);
            e.printStackTrace();

        }

    }//getUserShopClose

    private void initView() {
        btn_submit = findViewById(R.id.btn_submit);

        tl_add_url = findViewById(R.id.tl_add_url);
        tl_short_desc = findViewById(R.id.tl_short_desc);
        tl_hash_tag = findViewById(R.id.tl_hash_tag);
        tl_min_discount = findViewById(R.id.tl_min_discount);
        tl_max_discount = findViewById(R.id.tl_start_date);
        tl_start_date = findViewById(R.id.tl_start_date);
        tl_end_date = findViewById(R.id.tl_end_date);
        tl_title = findViewById(R.id.tl_title);

        et_title = findViewById(R.id.et_title);
        et_start_date = findViewById(R.id.et_start_date);
        et_end_date = findViewById(R.id.et_end_date);
        et_min_discount = findViewById(R.id.et_min_discount);
        et_max_discount = findViewById(R.id.et_max_discount);
        et_short_desc = findViewById(R.id.et_short_desc);
        et_hash_tag = findViewById(R.id.et_hash_tag);
        et_add_url = findViewById(R.id.et_add_url);
        p_bar = findViewById(R.id.p_bar);
        toolbar_title = findViewById(R.id.toolbar_title);
        iv_back = findViewById(R.id.iv_back);
        sp_products = findViewById(R.id.sp_products);
        sp_shops = findViewById(R.id.sp_shops);

        toolbar_title.setText(getResources().getString(R.string.sale_posting));
        sp_products_details = findViewById(R.id.sp_products_details);
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

    public void getShopWiseProductList(String shop_id) {

        try {
            p_bar.setVisibility(View.VISIBLE);
            Log.i("show_details", "Bearer " + PrefManager.getInstance(SalePostingActivity.this).getApiToken());

            RetrofitClient retrofitClient = new RetrofitClient();
            RestInterface service = retrofitClient.getAPIClient(WebServiceURLs.DOMAIN_NAME);
            service.getShopWiseProdct(shop_id, PrefManager.getInstance(SalePostingActivity.this).getUserId(), "Bearer " + PrefManager.getInstance(SalePostingActivity.this).getApiToken(), new Callback<JsonElement>() {
                @Override
                public void success(JsonElement jsonElement, Response response) {
                    product_name_list.clear();
                    product_id_list.clear();
                    //this method call if webservice success
                    try {

                        JSONObject jsonObject = new JSONObject(jsonElement.toString());
                        String status = jsonObject.getString("status");

                        if (status.equals("1")) {
                            JSONArray data = jsonObject.getJSONArray("data");
                            for (int i = 0; i < data.length(); i++) {
                                JSONObject j1 = data.getJSONObject(i);
                                String id = j1.getString("id");
                                String name = j1.getString("name");
                                product_id_list.add(id);
                                product_name_list.add(name);


                            }

                            final ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                                    SalePostingActivity.this,
                                    android.R.layout.simple_spinner_item,
                                    product_name_list
                            );
                            product_id_list.add(0, "idd");
                            product_name_list.add(0, "Select Product");
                            sp_products_details.setAdapter(adapter);
                            sp_products.setAdapter(adapter, false, new MultiSpinner.MultiSpinnerListener() {
                                public void onItemsSelected(boolean[] selected) {

                                    boolean[] selectedItems = new boolean[adapter.getCount()];
                                    //  Toast.makeText(SalePostingActivity.this, ""+adapter.getPosition(sele), Toast.LENGTH_SHORT).show();
                                }
// do something here when u item is selected from that spinner
                            });
                        }
                        else
                        {

                        }
                        p_bar.setVisibility(View.GONE);

                    } catch (JSONException | NullPointerException e) {
                        e.printStackTrace();

                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    p_bar.setVisibility(View.GONE);
                    Toast.makeText(SalePostingActivity.this, getResources().getString(R.string.check_internet), Toast.LENGTH_LONG).show();
                    Log.i("fdfdfdfdfdf", "" + error.getMessage());

                }
            });
        } catch (Exception e) {
            p_bar.setVisibility(View.GONE);
            e.printStackTrace();

        }
    }//getShopWiseProductListClose


    public void postSaleDetails() {

    }


}


