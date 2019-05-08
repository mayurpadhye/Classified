package mimosale.com.shop;

import android.content.Context;
import android.graphics.Point;
import android.support.annotation.NonNull;
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
import mimosale.com.home.fragments.AllProductPojo;
import mimosale.com.network.WebServiceURLs;
import com.github.siyamed.shapeimageview.RoundedImageView;
import com.squareup.picasso.Picasso;
import com.varunest.sparkbutton.SparkButton;

import java.util.List;

public class ShopProductAdapter  extends RecyclerView.Adapter<ShopProductAdapter.MyViewHolder> {
    List<AllProductPojo> allProductPojoList;
    Context mctx;

    public ShopProductAdapter(List<AllProductPojo> allProductPojoList, Context mctx) {
        this.allProductPojoList = allProductPojoList;
        this.mctx = mctx;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_shop_sale, parent, false);
        ViewGroup.LayoutParams layoutParams = itemView.getLayoutParams();

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final AllProductPojo items = allProductPojoList.get(position);

        holder.tv_product_name.setText(items.getName());
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.drawable.placeholder_logo);
        requestOptions.fitCenter();

        if (!items.getProduct_images().equals("") &&!items.getImage2().equals(""))
        {

            Picasso.with(mctx).load(WebServiceURLs.SHOP_IMAGE + items.getProduct_images()).into(holder.iv_product_image1);
            Picasso.with(mctx).load(WebServiceURLs.SHOP_IMAGE + items.getImage2()).into(holder.iv_product_image2);

        }
         holder.tv_price_range.setText(items.getPrice());
        holder.tv_desc.setText(items.getDescription());

        if (position == 0) {
            holder.ratingBar.setRating(3.5f);
        }

    }


    @Override
    public int getItemCount() {
        return allProductPojoList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        RoundedImageView iv_product_image2, iv_product_image1;
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
            ratingBar = view.findViewById(R.id.ratingBar);

        }
    }
}