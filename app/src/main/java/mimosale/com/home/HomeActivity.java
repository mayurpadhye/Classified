package mimosale.com.home;

import android.Manifest;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import mimosale.com.R;
import mimosale.com.account.AccountFragment;
import mimosale.com.favourite.FavouriteFragment;
import mimosale.com.helperClass.CustomUtils;
import mimosale.com.helperClass.PrefManager;
import mimosale.com.login.LoginActivity;
import mimosale.com.notification.NotificationFragment;
import mimosale.com.post.SalePostingActivity;
import mimosale.com.products.AddProductsActivity;
import mimosale.com.search.SearchResultActivity;
import mimosale.com.settings.ChangeLanguageActivity;
import mimosale.com.shop.ShopPostingActivity;

import java.util.Locale;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static mimosale.com.helperClass.CustomPermissions.MY_PERMISSIONS_REQUEST_CALL_PHONE;
import static mimosale.com.helperClass.CustomPermissions.MY_PERMISSIONS_REQUEST_LOCATION;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {
    BottomNavigationView navigationView;
    BottomSheetDialog dialog;
    String languageToLoad = "",language_code="";
    FragmentManager fragmentManager;
    private static final String TAG_HOME = "home";
    private static final String TAG_ACCOUNT = "account";
    private static final String TAG_NOTI = "noti";
    private static final String TAG_FAV = "favorite";
    public static int navItemIndex = 0;
    public static String CURRENT_TAG = TAG_HOME;
    private boolean shouldLoadHomeFragOnBackPress = true;
TextView et_search;

RelativeLayout rl_posting;
    @Override
    protected void onResume() {
        super.onResume();


      /*  String lang=PrefManager.getInstance(HomeActivity.this).getAppLanguageCode();


        if (lang.length()==0)
        {
            String languageToLoad  = CustomUtils.JAPNEESE_CODE; // your language
            PrefManager.getInstance(HomeActivity.this).setLanguage(CustomUtils.JAPAN);
            PrefManager.getInstance(HomeActivity.this).setAppLanguageCode(CustomUtils.JAPNEESE_CODE);
            Locale locale = new Locale(languageToLoad);
            Locale.setDefault(locale);
            Configuration config = new Configuration();
            config.locale = locale;
            getBaseContext().getResources().updateConfiguration(config,
                    getBaseContext().getResources().getDisplayMetrics());
            finish();
            startActivity(getIntent());

        }


*/
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        if (PrefManager.getInstance(HomeActivity.this).getLanguage().length()==0)
        {
            String languageToLoad  = CustomUtils.ENGLISH_CODE; // your language
            PrefManager.getInstance(HomeActivity.this).setLanguage(CustomUtils.ENGLISH);
            PrefManager.getInstance(HomeActivity.this).setAppLanguageCode(CustomUtils.ENGLISH_CODE);
            Locale locale = new Locale(languageToLoad);
            Locale.setDefault(locale);
            Configuration config = new Configuration();
            config.locale = locale;
            getBaseContext().getResources().updateConfiguration(config,
                    getBaseContext().getResources().getDisplayMetrics());
            finish();
            startActivity(getIntent());
            setContentView(R.layout.activity_home);

        }
        else if (PrefManager.getInstance(HomeActivity.this).getLanguage().equals("2"))
        {
            String languageToLoad  = CustomUtils.JAPNEESE_CODE; // your language
            PrefManager.getInstance(HomeActivity.this).setLanguage(CustomUtils.JAPAN);
            PrefManager.getInstance(HomeActivity.this).setAppLanguageCode(CustomUtils.JAPNEESE_CODE);
            Locale locale = new Locale(languageToLoad);
            Locale.setDefault(locale);
            Configuration config = new Configuration();
            config.locale = locale;
            getBaseContext().getResources().updateConfiguration(config,
                    getBaseContext().getResources().getDisplayMetrics());
            setContentView(R.layout.activity_home);

        }
        setContentView(R.layout.activity_home);
        navigationView = findViewById(R.id.bottom_navigation);
        rl_posting = findViewById(R.id.rl_posting);

        BottomNavigationMenuView bottomNavigationMenuView =
                (BottomNavigationMenuView) navigationView.getChildAt(0);
        View v = bottomNavigationMenuView.getChildAt(3);
        BottomNavigationItemView itemView = (BottomNavigationItemView) v;

        View badge = LayoutInflater.from(HomeActivity.this)
                .inflate(R.layout.notification_badge, itemView, true);

        TextView tv_noti_itm = badge.findViewById(R.id.notifications);
        et_search=findViewById(R.id.et_search);

        et_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, SearchResultActivity.class));
            }
        });


        BottomNavigationMenuView menuView = (BottomNavigationMenuView) navigationView.getChildAt(0);
        for (int i = 0; i < menuView.getChildCount(); i++) {
            BottomNavigationItemView item = (BottomNavigationItemView) menuView.getChildAt(i);
            View activeLabel = item.findViewById(R.id.largeLabel);
            if (activeLabel instanceof TextView) {
                activeLabel.setPadding(0, 0, 0, 0);
            }
        }
        navigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        if (savedInstanceState == null) {
            navItemIndex = 0;
            CURRENT_TAG = TAG_HOME;
            loadHomeFragment();
        }

        checkLocationPermission();

        //  getSupportFragmentManager().beginTransaction().add(R.id.rv_main,new HomeFragment()).commit();
    }

    public void loadHomeFragment() {
       /* Fragment fragment = getHomeFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                android.R.anim.fade_out);
        fragmentTransaction.replace(R.id.rv_main, fragment, CURRENT_TAG);
        fragmentTransaction.commitAllowingStateLoss();
*/


        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

        Fragment curFrag = getSupportFragmentManager().getPrimaryNavigationFragment();
        if (curFrag != null) {
            fragmentTransaction.detach(curFrag);
        }

        Fragment fragment = getSupportFragmentManager().findFragmentByTag(CURRENT_TAG);
        if (fragment == null) {
            fragment = getHomeFragment();
            fragmentTransaction.add(R.id.rv_main, fragment, CURRENT_TAG);
        } else {
            fragmentTransaction.attach(fragment);
        }

        fragmentTransaction.setPrimaryNavigationFragment(fragment);
        fragmentTransaction.setReorderingAllowed(true);
        fragmentTransaction.commitNowAllowingStateLoss();

    }

    private Fragment getHomeFragment() {
        switch (navItemIndex) {
            case 0:
                return new HomeFragment();
            case 1:
                return new FavouriteFragment();
            case 2:
                return new NotificationFragment();
            case 3:
                return new AccountFragment();

            default:
                return new HomeFragment();
        }
    }

    @Override
    public void onBackPressed() {

        if (shouldLoadHomeFragOnBackPress) {
            if (navItemIndex != 0) {
                navItemIndex = 0;
                CURRENT_TAG = TAG_HOME;
                loadHomeFragment();
                return;
            }
        }

        super.onBackPressed();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment;


            fragmentManager = getSupportFragmentManager();

            switch (item.getItemId()) {
                case R.id.nav_home:


                    navItemIndex = 0;
                    CURRENT_TAG = TAG_HOME;
                    break;
                case R.id.nav_account:
                    //  toolbar.setTitle("My Gifts");
                    navItemIndex = 3;
                    CURRENT_TAG = TAG_ACCOUNT;
                    /*if (PrefManager.getInstance(HomeActivity.this).IS_LOGIN()) {
                        // getSupportFragmentManager().beginTransaction().replace(R.id.rv_main, new AccountFragment()).commit();



                    } else {
                        dialogLoginWarning("account");

                    }*/
                    break;


                case R.id.nav_add_post:



                   showSelectPostingDailog();

                    break;
                case R.id.nav_fav:
                    if (PrefManager.getInstance(HomeActivity.this).IS_LOGIN()) {
                        navItemIndex = 1;
                        CURRENT_TAG = TAG_FAV;
                    } else {
                        dialogLoginWarning("favorite");

                    }

                    break;
                case R.id.nav_noti:
                    if (PrefManager.getInstance(HomeActivity.this).IS_LOGIN()) {
                        //   getSupportFragmentManager().beginTransaction().replace(R.id.rv_main, new NotificationFragment()).commit();
                        navItemIndex = 2;
                        CURRENT_TAG = TAG_NOTI;
                    } else {
                        dialogLoginWarning("noti");

                    }
                    break;


            }
            loadHomeFragment();
            return true;
        }
    };


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rb_shop_posting:

                if (PrefManager.getInstance(HomeActivity.this).IS_LOGIN()) {
                    startActivity(new Intent(HomeActivity.this, ShopPostingActivity.class).putExtra("isUpdate","create_shop"));
                } else {
                    dialogLoginWarning("shop_posting");

                }

                dialog.dismiss();
                break;
            case R.id.rb_sale_posting:
                if (PrefManager.getInstance(HomeActivity.this).IS_LOGIN()) {
                    startActivity(new Intent(HomeActivity.this, SalePostingActivity.class).putExtra("isUpdate","false"));
                } else {
                    dialogLoginWarning("sale_posting");

                }

                dialog.dismiss();
                break;
            case R.id.rb_product_posting:
                if (PrefManager.getInstance(HomeActivity.this).IS_LOGIN()) {
                    startActivity(new Intent(HomeActivity.this, AddProductsActivity.class).putExtra("isUpdate","false"));
                } else {
                    dialogLoginWarning("product_posting");

                }

                dialog.dismiss();
                break;
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {

            if (data != null) {

                if (data.getStringExtra("intent_from").equals("account")) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.rv_main, new AccountFragment()).commit();
                } else if (data.getStringExtra("intent_from").equals("favorite")) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.rv_main, new FavouriteFragment()).commit();
                } else if (data.getStringExtra("intent_from").equals("noti")) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.rv_main, new NotificationFragment()).commit();
                } else if (data.getStringExtra("intent_from").equals("shop_posting")) {
                    startActivity(new Intent(HomeActivity.this, ShopPostingActivity.class));
                } else if (data.getStringExtra("intent_from").equals("sale_posting")) {
                    startActivity(new Intent(HomeActivity.this, SalePostingActivity.class).putExtra("isUpdate","false"));
                } else if (data.getStringExtra("intent_from").equals("product_posting")) {
                    startActivity(new Intent(HomeActivity.this, AddProductsActivity.class).putExtra("isUpdate","false"));
                } else if (data.getStringExtra("intent_from").equals("add_pref")) {

                }

            }


        }
    }

    public void showSelectPostingDailog() {
        dialog = new BottomSheetDialog(this);
        dialog.setContentView(R.layout.dialog_select_posting);
        RadioButton rb_shop_posting = dialog.findViewById(R.id.rb_shop_posting);
        RadioButton rb_sale_posting = dialog.findViewById(R.id.rb_sale_posting);
        RadioButton rb_product_posting = dialog.findViewById(R.id.rb_product_posting);
        rb_shop_posting.setOnClickListener(this);
        rb_sale_posting.setOnClickListener(this);
        rb_product_posting.setOnClickListener(this);
        dialog.show();

    }


    public void dialogLoginWarning(final String intent_from) {

        new SweetAlertDialog(HomeActivity.this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText(HomeActivity.this.getResources().getString(R.string.login_waning))
                .setContentText(getResources().getString(R.string.please_login))
                .setConfirmText(getResources().getString(R.string.login))
                .setCancelText(getResources().getString(R.string.cancel))

                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {

                        if (intent_from.equals("shop_posting")) {
                            Intent i = new Intent(HomeActivity.this, LoginActivity.class);
                            i.putExtra("intent_from", "shop_posting");
                            startActivityForResult(i, 1);
                        }

                        if (intent_from.equals("sale_posting")) {
                            Intent i = new Intent(HomeActivity.this, LoginActivity.class);
                            i.putExtra("intent_from", "sale_posting");
                            startActivityForResult(i, 1);
                        }
                        if (intent_from.equals("product_posting")) {
                            Intent i = new Intent(HomeActivity.this, LoginActivity.class);
                            i.putExtra("intent_from", "product_posting");
                            startActivityForResult(i, 1);
                        }

                        if (intent_from.equals("account")) {
                            Intent i = new Intent(HomeActivity.this, LoginActivity.class);
                            i.putExtra("intent_from", "account");
                            startActivityForResult(i, 1);
                        }
                        if (intent_from.equals("favorite")) {
                            Intent i = new Intent(HomeActivity.this, LoginActivity.class);
                            i.putExtra("intent_from", "favorite");
                            startActivityForResult(i, 1);
                        }
                        if (intent_from.equals("noti")) {
                            Intent i = new Intent(HomeActivity.this, LoginActivity.class);
                            i.putExtra("intent_from", "noti");
                            startActivityForResult(i, 1);
                        }

                        if (intent_from.equals("add_pref")) {
                            Intent i = new Intent(HomeActivity.this, LoginActivity.class);
                            i.putExtra("intent_from", "add_pref");
                            startActivityForResult(i, 1);
                        }


                        sDialog.dismissWithAnimation();
                    }
                })
                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismissWithAnimation();
                    }
                })
                .show();

    }

    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                ActivityCompat.requestPermissions(HomeActivity.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION );


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }


            return false;

        } else {
            return true;
        }
    }




    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        //Request location updates:

                    }

                } else {
                      // checkLocationPermission();
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.

                }
                return;
            }


        }
    }



}
