package mimosale.com.account;


import android.app.Dialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import mimosale.com.R;
import mimosale.com.feedback.FeedBackActivity;
import mimosale.com.following.FollowingShopActivity;
import mimosale.com.helperClass.PrefManager;
import mimosale.com.home.HomeActivity;
import mimosale.com.login.LoginActivity;
import mimosale.com.network.WebServiceURLs;
import mimosale.com.my_posting.MyPostingActivity;
import mimosale.com.post.SalePostingActivity;
import mimosale.com.preferences.MyPreferencesActivity;
import mimosale.com.products.AllProductsActivity;
import mimosale.com.profile.ProfileActivity;
import mimosale.com.profile.ProfilePictureActivity;
import mimosale.com.settings.ChangeLanguageActivity;
import mimosale.com.shop.ShopPostingActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import cn.pedant.SweetAlert.SweetAlertDialog;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class AccountFragment extends Fragment implements View.OnClickListener {
    View v;
    Dialog dialog;
    TextView tv_profile, tv_post, tv_products, tv_premium, tv_messages, tv_transaction, tv_followers, tv_preferences, tv_help_support, tv_log_out;
    Spinner sp_language;
    TextView tv_email_id;
    CircleImageView cv_profile;
    TextView tv_setting,tv_feed_back;
    CoordinatorLayout cl_main;
    TextView tv_terms;
    FrameLayout fragment_container;
    Snackbar snackbar;

    View.OnClickListener mOnClickListener;
    public AccountFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_account, container, false);
        initView(v);
        tv_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), ChangeLanguageActivity.class));
            }
        });

        tv_terms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), TermsAndConditionActivity.class));
            }
        });

        tv_feed_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (PrefManager.getInstance(getActivity()).IS_LOGIN()) {
                    startActivity(new Intent(getActivity(), FeedBackActivity.class));
                } else {
                    dialogLoginWarning("account");

                }

            }
        });
        return v;
    }//initViewClose

    @Override
    public void onResume() {
        super.onResume();
        if (!PrefManager.getInstance(getActivity()).getProfilePic().equals(""))
      //  Glide.with(getActivity()).load(WebServiceURLs.IMAGE_URL+PrefManager.getInstance(getActivity()).getProfilePic()).into(cv_profile);
        Picasso.with(getActivity()).load(WebServiceURLs.IMAGE_URL+PrefManager.getInstance(getActivity()).getProfilePic()).into(cv_profile);
       // Picasso.with(mctx).load(WebServiceURLs.SHOP_IMAGE + items.getImage2()).into(holder.iv_product_image2);


        if (PrefManager.getInstance(getActivity()).IS_LOGIN())
        {
            tv_log_out.setVisibility(View.VISIBLE);
            snackbar.dismiss();
           tv_email_id.setText(PrefManager.getInstance(getActivity()).getEmail());
            fragment_container.setPadding(0,0,0,0);

        }
        else
        {
            fragment_container.setPadding(0,0,0,160);
            tv_log_out.setVisibility(View.GONE);


            // Display an action button in Snackbar
            snackbar.setAction("Login", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(getActivity(), LoginActivity.class);
                    i.putExtra("intent_from", "account");
                    startActivityForResult(i, 1);
                }
            });

            // Set the Snackbar action button default text color
            snackbar.setActionTextColor(Color.parseColor("#FF378F44"));

            // Change the Snackbar default text color
            View snackbarView = snackbar.getView();
            TextView tv = (TextView) snackbarView.findViewById(android.support.design.R.id.snackbar_text);
            tv.setTextColor(Color.RED);

            // Change the Snackbar default background color
            snackbarView.setBackgroundColor(Color.parseColor("#FFB0D9B9"));

            // Display the Snackbar
            snackbar.show();
        }
    }

    private void initView(View v) {

        List<String> spinnerArray = new ArrayList<>();
        spinnerArray.add("English");
        spinnerArray.add("Japanese");
        cv_profile=v.findViewById(R.id.cv_profile);
        tv_terms=v.findViewById(R.id.tv_terms);
        cl_main=v.findViewById(R.id.cl_main);
        fragment_container=v.findViewById(R.id.fragment_container);
        tv_feed_back=v.findViewById(R.id.tv_feed_back);
        //if (!PrefManager.getInstance(getActivity()).getProfilePic().isEmpty())
         tv_email_id=v.findViewById(R.id.tv_email_id);
        tv_setting=v.findViewById(R.id.tv_setting);
        sp_language = v.findViewById(R.id.sp_language);
        tv_profile = v.findViewById(R.id.tv_profile);
        tv_post = v.findViewById(R.id.tv_post);
        tv_products = v.findViewById(R.id.tv_products);
        tv_premium = v.findViewById(R.id.tv_premium);
        tv_messages = v.findViewById(R.id.tv_messages);
        tv_transaction = v.findViewById(R.id.tv_transaction);
        tv_followers = v.findViewById(R.id.tv_followers);
        tv_preferences = v.findViewById(R.id.tv_preferences);
        tv_help_support = v.findViewById(R.id.tv_help_support);
        tv_log_out = v.findViewById(R.id.tv_log_out);
      //  tv_email_id.setText(PrefManager.getInstance(getActivity()).getEmail());
        tv_profile.setOnClickListener(this);
        tv_post.setOnClickListener(this);
        tv_products.setOnClickListener(this);
        tv_premium.setOnClickListener(this);
        tv_messages.setOnClickListener(this);
        tv_transaction.setOnClickListener(this);
        tv_followers.setOnClickListener(this);
        tv_preferences.setOnClickListener(this);
        tv_help_support.setOnClickListener(this);
        tv_log_out.setOnClickListener(this);
        snackbar = Snackbar.make(cl_main,getResources().getString(R.string.please_login),Snackbar.LENGTH_INDEFINITE);
        View view = snackbar.getView();
        CoordinatorLayout.LayoutParams params =(CoordinatorLayout.LayoutParams)view.getLayoutParams();
        params.gravity = Gravity.BOTTOM;
        view.setLayoutParams(params);
        cv_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(),ProfilePictureActivity.class));
            }
        });
if (PrefManager.getInstance(getActivity()).IS_LOGIN())
{
    tv_log_out.setVisibility(View.VISIBLE);
    snackbar.dismiss();


}
else
{
    tv_log_out.setVisibility(View.GONE);


    // Display an action button in Snackbar
    snackbar.setAction("Login", new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent i = new Intent(getActivity(), LoginActivity.class);
            i.putExtra("intent_from", "account");
            startActivityForResult(i, 1);
        }
    });

    // Set the Snackbar action button default text color
    snackbar.setActionTextColor(Color.parseColor("#FF378F44"));

    // Change the Snackbar default text color
    View snackbarView = snackbar.getView();
    TextView tv = (TextView) snackbarView.findViewById(android.support.design.R.id.snackbar_text);
    tv.setTextColor(Color.RED);

    // Change the Snackbar default background color
    snackbarView.setBackgroundColor(Color.parseColor("#FFB0D9B9"));

    // Display the Snackbar
    snackbar.show();
}


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                getActivity(),
                R.layout.row_language,
                spinnerArray
        );

        sp_language.setAdapter(adapter);



    }//initViewClose


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_profile:
                if (PrefManager.getInstance(getActivity()).IS_LOGIN()) {
                    startActivity(new Intent(getActivity(), ProfileActivity.class));
                } else {
                    dialogLoginWarning("account");

                }


                break;
            case R.id.tv_products:
                if (PrefManager.getInstance(getActivity()).IS_LOGIN()) {
                    startActivity(new Intent(getActivity(), AllProductsActivity.class));
                } else {
                    dialogLoginWarning("account");

                }

                break;
            case R.id.tv_preferences:
                if (PrefManager.getInstance(getActivity()).IS_LOGIN()) {
                    startActivity(new Intent(getActivity(), MyPreferencesActivity.class));
                } else {
                    dialogLoginWarning("account");

                }

                break;
            case R.id.tv_post:
                if (PrefManager.getInstance(getActivity()).IS_LOGIN()) {
                    startActivity(new Intent(getActivity(), MyPostingActivity.class));
                } else {
                    dialogLoginWarning("account");

                }

                break;
            case R.id.tv_followers:
                if (PrefManager.getInstance(getActivity()).IS_LOGIN()) {
                    startActivity(new Intent(getActivity(), FollowingShopActivity.class));
                } else {
                    dialogLoginWarning("account");

                }

                break;

            case R.id.rb_shop_posting:
                if (PrefManager.getInstance(getActivity()).IS_LOGIN()) {
                    startActivity(new Intent(getActivity(), ShopPostingActivity.class).putExtra("isUpdate","false"));
                    dialog.dismiss();
                } else {
                    dialogLoginWarning("account");

                }


                break;
            case R.id.rb_sale_posting:
                if (PrefManager.getInstance(getActivity()).IS_LOGIN()) {
                    startActivity(new Intent(getActivity(), SalePostingActivity.class).putExtra("isUpdate","false"));
                    dialog.dismiss();
                } else {
                    dialogLoginWarning("account");

                }

                break;


            case R.id.tv_log_out:

                new SweetAlertDialog(getActivity(), SweetAlertDialog.WARNING_TYPE)
                        .setTitleText(getActivity().getResources().getString(R.string.log_out))
                        .setContentText(getResources().getString(R.string.want_logout))
                        .setConfirmText(getResources().getString(R.string.yes))
                        .setCancelText(getResources().getString(R.string.cancel))

                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {

                                PrefManager.getInstance(getActivity()).Logout();
                                startActivity(new Intent(getActivity(),LoginActivity.class).putExtra("intent_from","login"));
                                getActivity().finish();


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
                break;


            case R.id.tv_help_support:

                startActivity(new Intent(getActivity(),HelpSupportActivity.class));

                break;

        }

    }

    public void showSelectPostingDailog() {
        dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.dialog_select_posting);
        RadioButton rb_shop_posting = dialog.findViewById(R.id.rb_shop_posting);
        RadioButton rb_sale_posting = dialog.findViewById(R.id.rb_sale_posting);
        rb_shop_posting.setOnClickListener(this);
        rb_sale_posting.setOnClickListener(this);
        dialog.show();


    }

    public void dialogLoginWarning(final String intent_from) {

        new SweetAlertDialog(getActivity(), SweetAlertDialog.WARNING_TYPE)
                .setTitleText(getActivity().getResources().getString(R.string.login_waning))
                .setContentText(getResources().getString(R.string.please_login))
                .setConfirmText(getResources().getString(R.string.login))
                .setCancelText(getResources().getString(R.string.cancel))

                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {

                        if (intent_from.equals("shop_posting")) {
                            Intent i = new Intent(getActivity(), LoginActivity.class);
                            i.putExtra("intent_from", "shop_posting");
                            startActivityForResult(i, 1);
                        }

                        if (intent_from.equals("sale_posting")) {
                            Intent i = new Intent(getActivity(), LoginActivity.class);
                            i.putExtra("intent_from", "sale_posting");
                            startActivityForResult(i, 1);
                        }
                        if (intent_from.equals("product_posting")) {
                            Intent i = new Intent(getActivity(), LoginActivity.class);
                            i.putExtra("intent_from", "product_posting");
                            startActivityForResult(i, 1);
                        }

                        if (intent_from.equals("account")) {
                            Intent i = new Intent(getActivity(), LoginActivity.class);
                            i.putExtra("intent_from", "account");
                            startActivityForResult(i, 1);
                        }
                        if (intent_from.equals("favorite")) {
                            Intent i = new Intent(getActivity(), LoginActivity.class);
                            i.putExtra("intent_from", "favorite");
                            startActivityForResult(i, 1);
                        }
                        if (intent_from.equals("noti")) {
                            Intent i = new Intent(getActivity(), LoginActivity.class);
                            i.putExtra("intent_from", "noti");
                            startActivityForResult(i, 1);
                        }

                        if (intent_from.equals("add_pref")) {
                            Intent i = new Intent(getActivity(), LoginActivity.class);
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
}
