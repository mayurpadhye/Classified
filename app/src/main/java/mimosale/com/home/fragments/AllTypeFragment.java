package mimosale.com.home.fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import mimosale.com.R;


import mimosale.com.helperClass.PrefManager;
import mimosale.com.home.CatProductsPojo;
import mimosale.com.home.ItemClickListener;
import mimosale.com.home.Section;
import mimosale.com.home.SectionedExpandableLayoutHelper;
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

/**
 * A simple {@link Fragment} subclass.
 */
public class AllTypeFragment extends Fragment implements ItemClickListener {
  View v;
  ProgressBar p_bar;
RecyclerView rv_shop_sale,rv_products,rv_furniture;
List<ShopSaleModel> allShopSaleList=new ArrayList<>();
    List<AllProductPojo> allProductPojoList=new ArrayList<>();
    ShopSaleAdapter shopSaleAdapter;
    RecyclerView rv_prefwise_product;
    SectionedExpandableLayoutHelper sectionedExpandableLayoutHelper;
    public AllTypeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v= inflater.inflate(R.layout.fragment_all_type, container, false);
        initView(v);



       return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        getAllShopAndSale();
        getAllProducts();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);



      //  rv_furniture.setAdapter(productAdapter);
    }

    private void initView(View v) {
        rv_prefwise_product=v.findViewById(R.id.rv_prefwise_product);
        p_bar=v.findViewById(R.id.p_bar);
        rv_shop_sale = (RecyclerView) v.findViewById(R.id.rv_shop_sale);
        rv_furniture = (RecyclerView) v.findViewById(R.id.rv_furniture);
        rv_products = (RecyclerView) v.findViewById(R.id.rv_products);
        sectionedExpandableLayoutHelper = new SectionedExpandableLayoutHelper(getActivity(),
                rv_prefwise_product, this, 2);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 1);
        gridLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);

        LinearLayoutManager llm = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        rv_products.setLayoutManager(gridLayoutManager);
        rv_products.setHasFixedSize(false);

        GridLayoutManager gridLayoutManager1 = new GridLayoutManager(getActivity(), 1);
        gridLayoutManager1.setOrientation(LinearLayoutManager.HORIZONTAL);
        rv_shop_sale.setLayoutManager(gridLayoutManager1);

    }

    public void getAllShopAndSale() {

        try {
            p_bar.setVisibility(View.VISIBLE);
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
                            // String min_discount = j1.getString("min_discount");
                                //String max_discount = j1.getString("max_discount");
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

                                allShopSaleList.add(new ShopSaleModel(id,name,user_id,preference_id,address_line1,address_line2,city,state,country,pincode,lat,lon,low_price,high_price,discount,start_date,end_date,phone,hash_tags,description,web_url,image1,image2,"shop"));

                            }
                            ShopSaleAdapter shopSaleAdapter = new ShopSaleAdapter(allShopSaleList, getActivity());
                            rv_shop_sale.setAdapter(shopSaleAdapter);
                            shopSaleAdapter.notifyDataSetChanged();



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
                    Toast.makeText(getActivity(), getResources().getString(R.string.check_internet), Toast.LENGTH_LONG).show();
                    Log.i("fdfdfdfdfdf", "" + error.getMessage());

                }
            });
        } catch (Exception e) {
            e.printStackTrace();

        }


    }//etAllShopAndSale

    public void getAllProducts() {
        try {

            RetrofitClient retrofitClient = new RetrofitClient();
            RestInterface service = retrofitClient.getAPIClient(WebServiceURLs.DOMAIN_NAME);
            service.getAllProducts(new Callback<JsonElement>() {
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
                                String shop_id = j1.getString("shop_id");
                                String user_id = j1.getString("user_id");
                                String description = j1.getString("description");
                                String price = j1.getString("price");
                                String hash_tag = j1.getString("hash_tags");
                                String status1 = j1.getString("status");
                                String image1 = j1.getString("image1");
                                String image2 = j1.getString("image2");
                                String image= "";
                              /*  JSONArray product_images=j1.getJSONArray("product_images");
                                for (int k=0;k<product_images.length();k++)
                                {
                                    JSONObject j2=product_images.getJSONObject(k);
                                    image=j2.getString("image");
                                }*/
                                allProductPojoList.add(new AllProductPojo(id,name,shop_id,user_id,description,price,hash_tag,status1,image1,image2));
                            }

                            ProductsAdapter shopSaleAdapter = new ProductsAdapter(allProductPojoList, getActivity());
                            rv_products.setAdapter(shopSaleAdapter);


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
    public void getProductWisePosting()
    {
        try {

            RetrofitClient retrofitClient = new RetrofitClient();
            RestInterface service = retrofitClient.getAPIClient(WebServiceURLs.DOMAIN_NAME);
            service.getProductWisePosting(PrefManager.getInstance(getActivity()).getUserId(),new Callback<JsonElement>() {
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
                                String shop_id = j1.getString("shop_id");
                                String user_id = j1.getString("user_id");
                                String description = j1.getString("description");
                                String price = j1.getString("price");
                                String hash_tag = j1.getString("hash_tags");
                                String status1 = j1.getString("status");
                                String image1 = j1.getString("image1");
                                String image2 = j1.getString("image2");
                                String image= "";
                              /*  JSONArray product_images=j1.getJSONArray("product_images");
                                for (int k=0;k<product_images.length();k++)
                                {
                                    JSONObject j2=product_images.getJSONObject(k);
                                    image=j2.getString("image");
                                }*/
                                allProductPojoList.add(new AllProductPojo(id,name,shop_id,user_id,description,price,hash_tag,status1,image1,image2));
                            }
                          //  sectionedExpandableLayoutHelper.addSection(cat_name, cat_image, listsubcat);

                            ProductsAdapter shopSaleAdapter = new ProductsAdapter(allProductPojoList, getActivity());
                            rv_products.setAdapter(shopSaleAdapter);


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
    }//getProductWisePostingClose

    @Override
    public void itemClicked(CatProductsPojo item) {

    }

    @Override
    public void itemClicked(Section section) {

    }
}
