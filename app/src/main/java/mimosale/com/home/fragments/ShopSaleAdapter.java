package mimosale.com.home.fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import mimosale.com.R;

import mimosale.com.home.shop_sale.ShopSaleModel;
import mimosale.com.network.WebServiceURLs;
import mimosale.com.products.ProductDetailsActivity;
import mimosale.com.shop.ShopDetailActivity;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.github.siyamed.shapeimageview.RoundedImageView;
import com.romainpiel.shimmer.Shimmer;
import com.romainpiel.shimmer.ShimmerTextView;
import com.squareup.picasso.Picasso;
import com.varunest.sparkbutton.SparkButton;

import java.util.List;

public class ShopSaleAdapter extends RecyclerView.Adapter<ShopSaleAdapter.MyViewHolder> {
    List<ShopSaleModel> allProductPojoList;

    Context mctx;
    Shimmer shimmer;

    public ShopSaleAdapter(List<ShopSaleModel> allProductPojoList, Context mctx) {
        this.allProductPojoList = allProductPojoList;
        this.mctx = mctx;
        shimmer = new Shimmer();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_shop_sale, parent, false);
        itemView.getLayoutParams().width = (int) (getScreenWidth() / 1.5);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final ShopSaleModel items = allProductPojoList.get(position);

        holder.tv_fragment.animate().alpha(0f).setDuration(2000);
        holder.tv_fragment.animate().alpha(1f).setDuration(2000);
        holder.tv_fragment.bringToFront();
        holder.tv_product_name.setText(items.getName());
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.drawable.placeholder_logo);
        requestOptions.fitCenter();

        holder.shimmer_view_container1.startShimmerAnimation();
        shimmer.start(holder.shimmer_premium);

        Picasso.with(mctx).load(WebServiceURLs.SHOP_IMAGE + items.getImage()).resize(120, 120).into(holder.iv_product_image1);
        Picasso.with(mctx).load(WebServiceURLs.SHOP_IMAGE + items.getImage2()).resize(120, 120).into(holder.iv_product_image2);
       // Glide.with(mctx).load("http://4.bp.blogspot.com/-zlCqi4iWQb8/Tk_iLyLnoVI/AAAAAAAAED4/egErzO8ARQ0/s1600/s%25C3%25BCti+4548.jpg").into(holder.iv_product_image1);
     //   Glide.with(mctx).setDefaultRequestOptions(requestOptions).load(WebServiceURLs.SHOP_IMAGE + items.getImage2()).into(holder.iv_product_image2);
        holder.tv_price_range.setText("" + items.getLow_price() + "-" + items.getHigh_price());
        holder.tv_desc.setText(items.getDescription());
        if (!items.getMin_discount().equals("null") || !items.getMax_discount().equals("null"))
        holder.tv_discount.setText(items.getMin_discount() + "% - " + items.getMax_discount() + "%");
        else
        {
            holder.tv_discount.setVisibility(View.GONE);
        }

        if (position == 0) {
            holder.ratingBar.setRating(2.5f);
        }

        holder.cv_shop_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (items.getType().equals("shop"))
                mctx.startActivity(new Intent(mctx,ShopDetailActivity.class).putExtra("shop_id",items.getId()));

                else
                    mctx.startActivity(new Intent(mctx, ProductDetailsActivity.class).putExtra("product_id", items.getId()));
            }
        });

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

        public MyViewHolder(View view) {
            super(view);
            tv_fragment = view.findViewById(R.id.tv_premium);
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
