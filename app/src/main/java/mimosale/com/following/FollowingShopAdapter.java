package mimosale.com.following;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.request.RequestOptions;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.github.siyamed.shapeimageview.RoundedImageView;
import com.google.gson.JsonElement;
import mimosale.com.R;
import mimosale.com.helperClass.PrefManager;
import mimosale.com.home.fragments.ShopSaleAdapter;
import mimosale.com.home.shop_sale.ShopSaleModel;
import mimosale.com.network.RestInterface;
import mimosale.com.network.RetrofitClient;
import mimosale.com.network.WebServiceURLs;
import mimosale.com.products.ProductDetailsActivity;
import mimosale.com.shop.ShopDetailActivity;
import com.romainpiel.shimmer.Shimmer;
import com.romainpiel.shimmer.ShimmerTextView;
import com.squareup.picasso.Picasso;
import com.varunest.sparkbutton.SparkButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class FollowingShopAdapter extends RecyclerView.Adapter<FollowingShopAdapter.MyViewHolder> {
    List<FollowingShopPojo> allProductPojoList;
    ProgressDialog pDialog;
    Context mctx;
    Shimmer shimmer;

    public FollowingShopAdapter(List<FollowingShopPojo> allProductPojoList, Context mctx) {
        this.allProductPojoList = allProductPojoList;
        this.mctx = mctx;
        shimmer = new Shimmer();
        pDialog = new ProgressDialog(mctx);
        pDialog.setMessage("Loading...");
    }

    @NonNull
    @Override
    public FollowingShopAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_shop_list, parent, false);

        return new FollowingShopAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull FollowingShopAdapter.MyViewHolder holder, final int position) {
        final FollowingShopPojo items = allProductPojoList.get(position);






        Picasso.with(mctx).load(WebServiceURLs.SHOP_IMAGE + items.getShop_image()).into(holder.cv_shop_image);

        holder.tv_shop_name.setText(items.getShop_name());

         holder.btn_unfollow.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 followShop(items.getShop_id(),position);
             }
         });
         holder.cv_main.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 mctx.startActivity(new Intent(mctx,ShopDetailActivity.class).putExtra("shop_id",items.getShop_id()));
             }
         });


    }

    @Override
    public int getItemCount() {
        return allProductPojoList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tv_shop_name;
      CircleImageView cv_shop_image;
      Button btn_unfollow;
      CardView cv_main;

        public MyViewHolder(View view) {
            super(view);

            tv_shop_name = (TextView) view.findViewById(R.id.tv_shop_name);
            cv_shop_image = (CircleImageView) view.findViewById(R.id.cv_shop_image);
            btn_unfollow = (Button) view.findViewById(R.id.btn_unfollow);
            cv_main = (CardView) view.findViewById(R.id.cv_main);


            //   shimmer.start(shimmer_premium);
        }
    }
    public void followShop(String shop_id, final int position)
    {
        try {
            pDialog.show();
            RetrofitClient retrofitClient = new RetrofitClient();
            RestInterface service = retrofitClient.getAPIClient(WebServiceURLs.DOMAIN_NAME);
            service.followShop("unfollow",PrefManager.getInstance(mctx).getUserId(),shop_id,"Bearer "+PrefManager.getInstance(mctx).getApiToken(), new Callback<JsonElement>() {
                @Override
                public void success(JsonElement jsonElement, Response response) {

                    try {
                        JSONObject jsonObject=new JSONObject(jsonElement.toString());
                        pDialog.dismiss();
                        String status=jsonObject.getString("status");
                        String message=jsonObject.getString("message");
                        if (message.equals("Shop followed"))
                        {

                        }
                        else
                        {
                            allProductPojoList.remove(position);
                            notifyDataSetChanged();

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    pDialog.dismiss();
                    Toast.makeText(mctx, mctx.getResources().getString(R.string.check_internet), Toast.LENGTH_LONG).show();
                    Log.i("fdfdfdfdfdf", "" + error.getMessage());

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            pDialog.dismiss();
            Log.i("detailsException", "" + e.toString());


        }
    }

}
