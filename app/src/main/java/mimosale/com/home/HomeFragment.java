package mimosale.com.home;


import android.Manifest;
import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import mimosale.com.R;
import mimosale.com.helperClass.PrefManager;
import mimosale.com.home.fragments.AllTypeFragment;
import mimosale.com.home.fragments.EnhancedWrapContentViewPager;
import mimosale.com.home.fragments.ProductsFragment;
import mimosale.com.home.fragments.ShopSaleFragment;
import mimosale.com.home.preferences.PreferenceListModel;
import mimosale.com.home.preferences.PreferencesListAdapter;
import mimosale.com.home.shop_sale.ShopSaleModel;
import mimosale.com.login.LoginActivity;
import mimosale.com.network.RestInterface;
import mimosale.com.network.RetrofitClient;
import mimosale.com.network.WebServiceURLs;
import mimosale.com.preferences.MyPreferencesActivity;
import mimosale.com.spalsh.SplashActivity;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.gson.JsonElement;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment implements View.OnClickListener {

    View v;
    List<ShopSaleModel> allShopSaleList = new ArrayList<>();
    RecyclerView rv_preferences, rv_shop_sale, rv_products, rv_furniture;
    ImageView iv_add_post;
    List<PreferenceListModel> allPrefList = new ArrayList<>();
    PrefManager pref;
    ImageView iv_banner_image;
    // JSONArray bannerData = null;
    int currentPage = 0;
    ViewPager main_banner;
    Timer timer;
    View view_main;

    List<String> bannerImages = new ArrayList<>();

    String device_id="";
    private ShimmerFrameLayout mShimmerViewContainer;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_home, container, false);
device_id=Settings.Secure.getString(getActivity().getContentResolver(), Settings.Secure.ANDROID_ID);
        initView(v);


        //Toast.makeText(getActivity(), ""+), Toast.LENGTH_SHORT).show();
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        mShimmerViewContainer.startShimmerAnimation();
        if (PrefManager.getInstance(getActivity()).IS_LOGIN())
        {getUserPreferences();}
        else
        {
            //getUserPreferences();
            getDeviceIdWisePref();
        }

        getBannerImage();
    }

    @Override
    public void onPause() {
        mShimmerViewContainer.stopShimmerAnimation();
        super.onPause();
    }

    public void initView(View v) {
        pref = PrefManager.getInstance(getActivity());
        rv_preferences = v.findViewById(R.id.rv_preferences);
        main_banner = v.findViewById(R.id.main_banner);
        iv_add_post = v.findViewById(R.id.iv_add_post);
        view_main = v.findViewById(R.id.view_main);
        iv_banner_image = v.findViewById(R.id.iv_banner_image);
        mShimmerViewContainer = v.findViewById(R.id.shimmer_view_container);

        GridLayoutManager gridLayoutManager1 = new GridLayoutManager(getActivity(), 1);
        gridLayoutManager1.setOrientation(LinearLayoutManager.HORIZONTAL);

        rv_preferences.setLayoutManager(gridLayoutManager1);
        rv_preferences.setHasFixedSize(true);

        TabLayout tabLayout = (TabLayout) v.findViewById(R.id.tabs);
        tabLayout.addTab(tabLayout.newTab().setText("A"));
        tabLayout.addTab(tabLayout.newTab().setText("B"));
        tabLayout.addTab(tabLayout.newTab().setText("C"));
        final EnhancedWrapContentViewPager viewPager = (EnhancedWrapContentViewPager) v.findViewById(R.id.viewpager);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        viewPager.setAdapter(new PagerAdapter(getChildFragmentManager(), tabLayout.getTabCount()));
        viewPager.measure(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        viewPager.setOffscreenPageLimit(0);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        viewPager.setOffscreenPageLimit(4);
        iv_add_post.setOnClickListener(this);

        ViewGroup slidingTabStrip = (ViewGroup) tabLayout.getChildAt(0);
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            View tab = slidingTabStrip.getChildAt(i);
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) tab.getLayoutParams();
            layoutParams.weight = 1;
            tab.setLayoutParams(layoutParams);
        }

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }


        });
    }//initClose


    public void getUserPreferences() {

        try {

            RetrofitClient retrofitClient = new RetrofitClient();
            RestInterface service = retrofitClient.getAPIClient(WebServiceURLs.DOMAIN_NAME);
            service.getUserPreferences(pref.getUserId(), "Bearer " + pref.getApiToken(), new Callback<JsonElement>() {
                @Override
                public void success(JsonElement jsonElement, Response response) {
                    //this method call if webservice success
                    try {
                        JSONObject jsonObject = new JSONObject(jsonElement.toString());
                        String status = jsonObject.getString("status");

                        if (status.equals("1")) {
                            allPrefList.clear();
                            JSONArray data = jsonObject.getJSONArray("data");
                            for (int i = 0; i < data.length(); i++) {
                                JSONObject j1 = data.getJSONObject(i);
                                String id = j1.getString("id");
                                String name = j1.getString("name");
                                String description = j1.getString("description");
                                String image = j1.getString("image");
                                String status1 = j1.getString("status");
                                String created_at = j1.getString("created_at");
                                String updated_at = j1.getString("updated_at");
                                allPrefList.add(new PreferenceListModel(id, name, image, description, status1));
                            }

                            PreferencesListAdapter adapter = new PreferencesListAdapter(allPrefList, getActivity(), HomeFragment.this);
                            rv_preferences.setAdapter(adapter);
                            mShimmerViewContainer.stopShimmerAnimation();
                            mShimmerViewContainer.setVisibility(View.GONE);
                            view_main.setVisibility(View.VISIBLE);
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


    }//userPreferencesClose


    public void getDeviceIdWisePref()
    {


        try {

            RetrofitClient retrofitClient = new RetrofitClient();
            RestInterface service = retrofitClient.getAPIClient(WebServiceURLs.DOMAIN_NAME);
            service.getDevicePref(device_id,new Callback<JsonElement>() {
                @Override
                public void success(JsonElement jsonElement, Response response) {
                    //this method call if webservice success
                    try {
                        JSONObject jsonObject = new JSONObject(jsonElement.toString());
                        String status = jsonObject.getString("status");

                        if (status.equals("1")) {
                            allPrefList.clear();
                            JSONArray data = jsonObject.getJSONArray("data");
                            for (int i = 0; i < data.length(); i++) {
                                JSONObject j1 = data.getJSONObject(i);
                                String id = j1.getString("id");
                                String name = j1.getString("name");
                                String description = j1.getString("description");
                                String image = j1.getString("image");
                                String status1 = j1.getString("status");
                                String created_at = j1.getString("created_at");
                                String updated_at = j1.getString("updated_at");
                                allPrefList.add(new PreferenceListModel(id, name, image, description, status1));
                            }

                            PreferencesListAdapter adapter = new PreferencesListAdapter(allPrefList, getActivity(), HomeFragment.this);
                            rv_preferences.setAdapter(adapter);
                            mShimmerViewContainer.stopShimmerAnimation();
                            mShimmerViewContainer.setVisibility(View.GONE);
                            view_main.setVisibility(View.VISIBLE);
                        }
                        else
                        {
                            mShimmerViewContainer.setVisibility(View.GONE);
                            view_main.setVisibility(View.VISIBLE);
                        }


                    } catch (JSONException | NullPointerException e) {
                        e.printStackTrace();
                        Log.i("fdfdfdfdfdf", "" + e.getMessage());


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
            Log.i("fdfdfdfdfdf", "" + e.getMessage());

        }


    }//getDeviceIdPref

    public void getBannerImage() {

        try {

            RetrofitClient retrofitClient = new RetrofitClient();
            RestInterface service = retrofitClient.getAPIClient(WebServiceURLs.DOMAIN_NAME);
            service.GetBannerImages(new Callback<JsonElement>() {
                @Override
                public void success(JsonElement jsonElement, Response response) {
                    //this method call if webservice success
                    try {
                        JSONObject jsonObject = new JSONObject(jsonElement.toString());
                        String status = jsonObject.getString("status");

                        if (status.equals("1")) {
                            JSONArray data = jsonObject.getJSONArray("data");


                            if (data != null) {
                                bannerImages.clear();
                                for (int i = 0; i < data.length(); i++) {
                                    JSONObject j1 = data.getJSONObject(i);
                                    String image = j1.getString("image");
                                    bannerImages.add(image);
                                    Picasso.with(getActivity()).load(WebServiceURLs.IMAGE_URL+bannerImages.get(0)).into(iv_banner_image);
                                     //  Glide.with(getActivity()).load(WebServiceURLs.IMAGE_URL+bannerImages.get(0)).into(iv_banner_image);
                                }
                                BannerAdapter bannerAdapter = new BannerAdapter(getActivity(), bannerImages);
                                main_banner.setAdapter(bannerAdapter);
                                final Handler handler = new Handler();
                                final Runnable Update = new Runnable() {
                                    public void run() {
                                        if (currentPage <= bannerImages.size()) {

                                            main_banner.setCurrentItem(currentPage, true);
                                            currentPage++;
                                        }
                                        if (currentPage == bannerImages.size()) {
                                            currentPage = -1;
                                        } else {
                                            main_banner.setCurrentItem(currentPage);
                                        }
                                    }
                                };

                                timer = new Timer(); // This will create a new Thread
                                timer.schedule(new TimerTask() { // task to be scheduled

                                    @Override
                                    public void run() {
                                        handler.post(Update);


                                    }
                                }, 1000, 7000);

                            }


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


    }//userPreferencesClose


    public class PagerAdapter extends FragmentStatePagerAdapter {
        int mNumOfTabs;

        public PagerAdapter(FragmentManager fm, int NumOfTabs) {
            super(fm);
            this.mNumOfTabs = NumOfTabs;
        }


        @Override
        public Fragment getItem(int position) {

            switch (position) {
                case 0:
                    return new AllTypeFragment();
                case 1:
                    return new ShopSaleFragment();
                case 2:
                    return new ProductsFragment();

                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return mNumOfTabs;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            String title = null;
            if (position == 0) {
                title = getResources().getString(R.string.all);
            } else if (position == 1) {
                title =getResources().getString(R.string.shop_and_sale);
            } else if (position == 2) {
                title = getResources().getString(R.string.products);
            }
            return title;
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_add_post:
                if (pref.IS_LOGIN()) {
                    startActivity(new Intent(getActivity(), MyPreferencesActivity.class));
                } else {
                    Intent i = new Intent(getActivity(), LoginActivity.class);
                    i.putExtra("intent_from", "add_pref");
                    startActivityForResult(i, 1);
                }
                break;

        }
    }

    public void addPost() {
        if (pref.IS_LOGIN()) {
            startActivity(new Intent(getActivity(), MyPreferencesActivity.class));


        } else {

            ((HomeActivity) getActivity()).dialogLoginWarning("add_pref");

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {

            if (data != null) {

                if (data.getStringExtra("intent_from").equals("add_pref")) {
                    getActivity().startActivity(new Intent(getActivity(), MyPreferencesActivity.class));
                }
            }


        }
    }

}
