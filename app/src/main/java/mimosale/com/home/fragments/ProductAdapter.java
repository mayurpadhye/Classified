package mimosale.com.home.fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import mimosale.com.R;
import mimosale.com.home.shop_sale.ShopSaleModel;
import mimosale.com.products.ProductDetailsActivity;
import com.github.siyamed.shapeimageview.RoundedImageView;
import com.varunest.sparkbutton.SparkButton;

import java.util.List;

public class ProductAdapter  extends RecyclerView.Adapter<ProductAdapter.MyViewHolder> {
    List<ShopSaleModel> allShopList;
    Context mctx;

    public ProductAdapter(List<ShopSaleModel> allShopList, Context mctx) {
        this.allShopList = allShopList;
        this.mctx = mctx;
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
        final ShopSaleModel items = allShopList.get(position);

        holder.tv_product_name.setText(items.getProduct_name());
       /* if (position==0)
        {
            holder.ratingBar.setRating(3.5f);
            holder.iv_product_image.setImageDrawable(mctx.getResources().getDrawable(R.drawable.sofa));
        }
        else
        {
            holder.iv_product_image.setImageDrawable(mctx.getResources().getDrawable(R.drawable.prefences1));
        }
        Animation animation = new AlphaAnimation(1, 2); //to change visibility from visible to invisible
        animation.setDuration(1000); //1 second duration for each animation cycle
        animation.setInterpolator(new LinearInterpolator());
        animation.setRepeatCount(Animation.INFINITE); //repeating indefinitely
        animation.setRepeatMode(Animation.REVERSE); //animation will start from end point once ended.
        holder.tv_offer.startAnimation(animation);*/ //to start animation


    }

    @Override
    public int getItemCount() {
        return allShopList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        RoundedImageView iv_product_image2,iv_product_image1;
        ImageView iv_like;
        TextView tv_desc, tv_price_range, tv_product_name;
        RatingBar ratingBar;
        SparkButton spark_button;

        public MyViewHolder(View view) {
            super(view);
            iv_product_image2 = (RoundedImageView) view.findViewById(R.id.iv_product_image2);
            iv_product_image1 = (RoundedImageView) view.findViewById(R.id.iv_product_image1);
            iv_like = (ImageView) view.findViewById(R.id.iv_like);
            tv_desc = (TextView) view.findViewById(R.id.tv_desc);
            tv_price_range = (TextView) view.findViewById(R.id.tv_price_range);
            tv_product_name = (TextView) view.findViewById(R.id.tv_product_name);
            spark_button = (SparkButton) view.findViewById(R.id.spark_button);
            ratingBar=view.findViewById(R.id.ratingBar);

        }

        @Override
        public void onClick(View v) {
            switch (v.getId())
            {
                case R.id.iv_product_image2:
                //    mctx.startActivity(new Intent(mctx,ProductDetailsActivity.class).putExtra("title","Product Details"));
            }
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
