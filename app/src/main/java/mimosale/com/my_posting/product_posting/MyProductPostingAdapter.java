package mimosale.com.my_posting.product_posting;

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
import com.github.siyamed.shapeimageview.RoundedImageView;
import com.google.gson.JsonElement;
import com.squareup.picasso.Picasso;
import com.varunest.sparkbutton.SparkButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import mimosale.com.R;
import mimosale.com.helperClass.PrefManager;
import mimosale.com.home.fragments.AllProductPojo;
import mimosale.com.home.shop_sale.ProductsAdapter;
import mimosale.com.network.RestInterface;
import mimosale.com.network.RetrofitClient;
import mimosale.com.network.WebServiceURLs;
import mimosale.com.products.AddProductsActivity;
import mimosale.com.products.ProductDetailsActivity;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MyProductPostingAdapter  extends RecyclerView.Adapter<MyProductPostingAdapter.MyViewHolder> {
    List<AllProductPojo> allProductPojoList;
    Context mctx;
    ProgressBar p_bar;

    public MyProductPostingAdapter(List<AllProductPojo> allProductPojoList, Context mctx,ProgressBar p_bar) {
        this.allProductPojoList = allProductPojoList;
        this.mctx = mctx;
        this.p_bar=p_bar;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_my_product_posting, parent, false);
        ViewGroup.LayoutParams layoutParams = itemView.getLayoutParams();

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        final AllProductPojo items = allProductPojoList.get(position);

        holder.tv_product_name.setText(items.getName());
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.drawable.placeholder_logo);
        requestOptions.fitCenter();
        holder.cv_shop_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mctx.startActivity(new Intent(mctx, ProductDetailsActivity.class).putExtra("product_id", items.getId()));
            }
        });


        Picasso.with(mctx).load(WebServiceURLs.SHOP_IMAGE + items.getProduct_images()).into(holder.iv_product_image1);
        Picasso.with(mctx).load(WebServiceURLs.SHOP_IMAGE + items.getImage2()).into(holder.iv_product_image2);

        // Glide.with(mctx).setDefaultRequestOptions(requestOptions).load(WebServiceURLs.SHOP_IMAGE + items.getProduct_images()).into(holder.iv_product_image1);
        // Glide.with(mctx).setDefaultRequestOptions(requestOptions).load(WebServiceURLs.SHOP_IMAGE + items.getImage2()).into(holder.iv_product_image2);
        holder.tv_price_range.setText(items.getPrice());
        holder.tv_desc.setText(items.getDescription());
        holder.iv_pop_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              updateAction(items.getId(),position,holder.iv_pop_up);

            }
        });

        if (position == 0) {
            holder.ratingBar.setRating(3.5f);
        }

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
                           .setContentText(mctx.getResources().getString(R.string.want_to_delete_product))
                           .setConfirmText(mctx.getResources().getString(R.string.delete))
                           .setCancelText(mctx.getResources().getString(R.string.no))

                           .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                               @Override
                               public void onClick(SweetAlertDialog sDialog) {
                                   sDialog.dismissWithAnimation();
                                   deleteProductPosting(product_id,position);
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
                   Intent i=new Intent(mctx,AddProductsActivity.class);
                           i.putExtra("shop_name",allProductPojoList.get(position).getShop_id());
                           i.putExtra("product_name",allProductPojoList.get(position).getName());
                           i.putExtra("desc",allProductPojoList.get(position).getDescription());
                           i.putExtra("price",allProductPojoList.get(position).getPrice());
                           i.putExtra("product_id",allProductPojoList.get(position).getId());
                           i.putExtra("discount","");
                           i.putExtra("discount",allProductPojoList.get(position).getHash_tag());
                           i.putExtra("isUpdate","true");
                           mctx.startActivity(i);
               }
                return true;
            }
        });

        popup.show();//showing popup menu
    }



    public void deleteProductPosting(String product_id, final int postition)
    {
        try {
            p_bar.setVisibility(View.VISIBLE);
            RetrofitClient retrofitClient = new RetrofitClient();
            RestInterface service = retrofitClient.getAPIClient(WebServiceURLs.DOMAIN_NAME);
            service.delete_posting(PrefManager.getInstance(mctx).getUserId(),product_id,"Bearer "+PrefManager.getInstance(mctx).getApiToken(),new Callback<JsonElement>() {
                @Override
                public void success(JsonElement jsonElement, Response response) {
                    //this method call if webservice success
                    try {

                        JSONObject jsonObject = new JSONObject(jsonElement.toString());
                        String status = jsonObject.getString("status");
                        p_bar.setVisibility(View.GONE);
                        if (status.equals("1")) {
                            allProductPojoList.remove(postition);
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
        TextView tv_desc, tv_price_range, tv_product_name,tv_discount,tv_dis;
        RatingBar ratingBar;
        SparkButton spark_button;
        CardView cv_shop_main;
        ImageView iv_pop_up;

        public MyViewHolder(View view) {
            super(view);
            iv_product_image2 = (RoundedImageView) view.findViewById(R.id.iv_product_image2);
            iv_product_image1 = (RoundedImageView) view.findViewById(R.id.iv_product_image1);
            iv_like = (ImageView) view.findViewById(R.id.iv_like);
            tv_desc = (TextView) view.findViewById(R.id.tv_desc);
            tv_price_range = (TextView) view.findViewById(R.id.tv_price_range);
            tv_product_name = (TextView) view.findViewById(R.id.tv_product_name);
            tv_discount = (TextView) view.findViewById(R.id.tv_discount);
            tv_dis = (TextView) view.findViewById(R.id.tv_dis);
            spark_button = (SparkButton) view.findViewById(R.id.spark_button);
            ratingBar = view.findViewById(R.id.ratingBar);
            cv_shop_main = view.findViewById(R.id.cv_shop_main);
            iv_pop_up = view.findViewById(R.id.iv_pop_up);

        }
    }


}