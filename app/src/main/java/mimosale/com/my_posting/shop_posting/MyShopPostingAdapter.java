package mimosale.com.my_posting.shop_posting;

import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.request.RequestOptions;

import cn.pedant.SweetAlert.SweetAlertDialog;
import mimosale.com.R;
import mimosale.com.helperClass.PrefManager;
import mimosale.com.my_posting.MyShopPojo;
import mimosale.com.network.RestInterface;
import mimosale.com.network.RetrofitClient;
import mimosale.com.network.WebServiceURLs;
import mimosale.com.shop.ImageVideoData;
import mimosale.com.shop.ShopDetailActivity;
import mimosale.com.shop.ShopDetailsActivityNew;
import mimosale.com.shop.ShopPostingActivity;
import mimosale.com.shop.ShopPreviewActivity;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.github.siyamed.shapeimageview.RoundedImageView;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.romainpiel.shimmer.Shimmer;
import com.romainpiel.shimmer.ShimmerTextView;
import com.squareup.picasso.Picasso;
import com.varunest.sparkbutton.SparkButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MyShopPostingAdapter  extends RecyclerView.Adapter<MyShopPostingAdapter.MyViewHolder> {
    List<MyShopPojo> allProductPojoList;
ProgressBar p_bar;
    Context mctx;
    Shimmer shimmer;

    public MyShopPostingAdapter(List<MyShopPojo> allProductPojoList, Context mctx,ProgressBar p_bar) {
        this.allProductPojoList = allProductPojoList;
        this.mctx = mctx;
        this.p_bar = p_bar;
        shimmer = new Shimmer();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_my_shop_posting, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        final MyShopPojo items = allProductPojoList.get(position);

        holder.tv_fragment.animate().alpha(0f).setDuration(2000);
        holder.tv_fragment.animate().alpha(1f).setDuration(2000);
        holder.tv_fragment.bringToFront();
        holder.tv_product_name.setText(items.getName());
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.drawable.placeholder_logo);
        requestOptions.fitCenter();

        holder.shimmer_view_container1.startShimmerAnimation();
        shimmer.start(holder.shimmer_premium);
        Picasso.with(mctx).load(WebServiceURLs.SHOP_IMAGE + items.getImage()).into(holder.iv_product_image1);
        Picasso.with(mctx).load(WebServiceURLs.SHOP_IMAGE + items.getImage2()).into(holder.iv_product_image2);
        holder.tv_price_range.setText("" + items.getLow_price() + "-" + items.getHigh_price());
        holder.tv_desc.setText(items.getDescription());
        holder.tv_discount.setText(items.getMin_discount() + "% - " + items.getMax_discount() + "%");
        if (position == 0) {
            holder.ratingBar.setRating(2.5f);
        }

        holder.cv_shop_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mctx.startActivity(new Intent(mctx, ShopDetailsActivityNew.class).putExtra("shop_id",items.getId()));
            }
        });
        holder.iv_pop_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateAction(items.getId(),position,holder.iv_pop_up);

            }
        });
    }

    public void updateAction(final String product_id, final int position, ImageView iv_pop_up)
    {

        PopupMenu popup = new PopupMenu(mctx, iv_pop_up);
        //Inflating the Popup using xml file
        popup.getMenuInflater().inflate(R.menu.poupup_menu_posting, popup.getMenu());

        //registering popup with OnMenuItemClickListener
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId()==R.id.delete)
                {
                    new SweetAlertDialog(mctx, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText(mctx.getResources().getString(R.string.are_you_sure))
                            .setContentText(mctx.getResources().getString(R.string.want_to_delete_shop))
                            .setConfirmText(mctx.getResources().getString(R.string.delete))
                            .setCancelText(mctx.getResources().getString(R.string.no))

                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sDialog.dismissWithAnimation();
                                    deleteShopPosting(product_id,position);
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
                if (item.getItemId()==R.id.edit)
                {

                    Intent i=new Intent(mctx,ShopPostingActivity.class);
                    i.putExtra("shop_name",allProductPojoList.get(position).getName());

                    i.putExtra("shop_desc",allProductPojoList.get(position).getDescription());
                    i.putExtra("shop_category","");
                    i.putExtra("min_discount",allProductPojoList.get(position).getMin_discount());
                    i.putExtra("max_discount",allProductPojoList.get(position).getMax_discount());
                    i.putExtra("start_date",allProductPojoList.get(position).getStart_date());
                    i.putExtra("end_date",allProductPojoList.get(position).getEnd_date());
                    i.putExtra("min_price",allProductPojoList.get(position).getLow_price());
                    i.putExtra("max_price",allProductPojoList.get(position).getHigh_price());
                    i.putExtra("pincode",allProductPojoList.get(position).getPincode());
                    i.putExtra("city",allProductPojoList.get(position).getCity());
                    i.putExtra("address_line_1",allProductPojoList.get(position).getAddress_line1());
                    i.putExtra("address_line_2",allProductPojoList.get(position).getAddress_line2());
                    i.putExtra("phone_number",allProductPojoList.get(position).getPhone());
                    i.putExtra("hash_tag",allProductPojoList.get(position).getHash_tags());
                    i.putExtra("web_url",allProductPojoList.get(position).getWeb_url());
                    i.putExtra("pref_id",allProductPojoList.get(position).getPreference_id());
                    i.putExtra("state",allProductPojoList.get(position).getState());
                    i.putExtra("country",allProductPojoList.get(position).getCountry());
                    i.putExtra("lat",allProductPojoList.get(position).getLat());
                    i.putExtra("lan",allProductPojoList.get(position).getLon());
                    i.putExtra("isUpdate","update_shop");
                    i.putExtra("shop_id",allProductPojoList.get(position).getId());



                    mctx.startActivity(i);
                }
                return true;
            }
        });

        popup.show();//showing popup menu
    }

    public void deleteShopPosting(String product_id, final int position)
    {

        try {
            p_bar.setVisibility(View.VISIBLE);
            RetrofitClient retrofitClient = new RetrofitClient();
            RestInterface service = retrofitClient.getAPIClient(WebServiceURLs.DOMAIN_NAME);
            service.delete_shop_posting(PrefManager.getInstance(mctx).getUserId(),product_id,"Bearer "+PrefManager.getInstance(mctx).getApiToken(),new Callback<JsonElement>() {
                @Override
                public void success(JsonElement jsonElement, Response response) {
                    //this method call if webservice success
                    try {

                        JSONObject jsonObject = new JSONObject(jsonElement.toString());
                        String status = jsonObject.getString("status");
                        p_bar.setVisibility(View.GONE);
                        if (status.equals("1")) {
                            allProductPojoList.remove(position);
                            notifyDataSetChanged();
                            Toast.makeText(mctx, ""+jsonObject.getString("message"), Toast.LENGTH_SHORT).show();

                        }
                        else
                        {
                            Toast.makeText(mctx, ""+jsonObject.getString("data"), Toast.LENGTH_SHORT).show();
                        }


                    } catch (JSONException | NullPointerException e) {
                        p_bar.setVisibility(View.GONE);
                        e.printStackTrace();

                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    p_bar.setVisibility(View.GONE);
                    Toast.makeText(mctx, mctx.getResources().getString(R.string.check_internet), Toast.LENGTH_LONG).show();
                    Log.i("fdfdfdfdfdf", "" + error.getMessage());

                }
            });
        } catch (Exception e) {
            e.printStackTrace();

        }

    }

    @Override
    public int getItemCount() {
        return allProductPojoList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        RoundedImageView iv_product_image2, iv_product_image1;
        ImageView iv_like;
        TextView tv_desc, tv_price_range, tv_product_name, tv_discount, tv_fragment;
        RatingBar ratingBar;
        SparkButton spark_button;
        ShimmerTextView shimmer_premium;
        CardView cv_shop_main;
        ShimmerFrameLayout shimmer_view_container1;
        ImageView iv_pop_up;

        public MyViewHolder(View view) {
            super(view);
            tv_fragment = view.findViewById(R.id.tv_premium);
            iv_pop_up = view.findViewById(R.id.iv_pop_up);
            iv_product_image2 = (RoundedImageView) view.findViewById(R.id.iv_product_image2);
            iv_product_image1 = (RoundedImageView) view.findViewById(R.id.iv_product_image1);
            iv_like = (ImageView) view.findViewById(R.id.iv_like);
            tv_desc = (TextView) view.findViewById(R.id.tv_desc);
            tv_discount = (TextView) view.findViewById(R.id.tv_discount);
            tv_price_range = (TextView) view.findViewById(R.id.tv_price_range);
            tv_product_name = (TextView) view.findViewById(R.id.tv_product_name);
            spark_button = (SparkButton) view.findViewById(R.id.spark_button);
            ratingBar = view.findViewById(R.id.ratingBar);
            shimmer_premium = view.findViewById(R.id.shimmer_premium);
            shimmer_view_container1 = view.findViewById(R.id.shimmer_view_container1);
            cv_shop_main = view.findViewById(R.id.cv_shop_main);

            //   shimmer.start(shimmer_premium);
        }
    }

    public int getScreenWidth() {

        WindowManager wm = (WindowManager) mctx.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        return size.x;
    }
}
