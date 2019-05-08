package mimosale.com.home;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import mimosale.com.R;
import mimosale.com.network.WebServiceURLs;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.github.siyamed.shapeimageview.RoundedImageView;
import com.romainpiel.shimmer.ShimmerTextView;

import com.squareup.picasso.Picasso;
import com.varunest.sparkbutton.SparkButton;

import java.util.ArrayList;

public class SectionedExpandableGridAdapter extends RecyclerView.Adapter<SectionedExpandableGridAdapter.ViewHolder> {

    //data array
    private ArrayList<Object> mDataArrayList;

    //context
    private final Context mContext;

    //listeners
    private final ItemClickListener mItemClickListener;
    private final SectionStateChangeListener mSectionStateChangeListener;

    //view type
    private static final int VIEW_TYPE_SECTION = R.layout.row_section;
    private static final int VIEW_TYPE_ITEM = R.layout.row_shop_sale; //TODO : change this

    public SectionedExpandableGridAdapter(Context context, ArrayList<Object> dataArrayList,
                                          final GridLayoutManager gridLayoutManager, ItemClickListener itemClickListener,
                                          SectionStateChangeListener sectionStateChangeListener) {
        mContext = context;
        mItemClickListener = itemClickListener;
        mSectionStateChangeListener = sectionStateChangeListener;
        mDataArrayList = dataArrayList;

        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return isSection(position)?gridLayoutManager.getSpanCount():1;
            }
        });
    }

    private boolean isSection(int position) {
        return mDataArrayList.get(position) instanceof Section;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mContext).inflate(viewType, parent, false), viewType);
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        switch (holder.viewType) {
            case VIEW_TYPE_ITEM :
                final CatProductsPojo items = (CatProductsPojo) mDataArrayList.get(position);
                holder.tv_product_name.setText(items.getName());
                // holder.tv_original_price.setText(items.getProduct_original_price());
                holder.tv_discount.setText(items.getDiscount());
                holder.tv_price_range.setText(items.getPrice());




                //set drawable to imageview
        /*Glide.with(context)
                .load(items.getProduct_image())
                .apply(new RequestOptions())
                .into(holder.iv_product_image);*/

       /* Glide.with(context).load(items.getProduct_image())
                .thumbnail(0.5f)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.iv_product_image);*/

             //   Picasso.with(mContext).load(items.getProduct_image()).networkPolicy(NetworkPolicy.NO_CACHE).into(holder.iv_product_image);
                RequestOptions requestOptions = new RequestOptions();
                requestOptions.placeholder(R.drawable.placeholder_logo);
                requestOptions.fitCenter();

                holder.shimmer_view_container1.startShimmerAnimation();
                Picasso.with(mContext).load(WebServiceURLs.IMAGE_URL + items.getImage1()).into(holder.iv_product_image1);
                Picasso.with(mContext).load(WebServiceURLs.IMAGE_URL + items.getImage2()).into(holder.iv_product_image2);


//                Glide.with(mContext).setDefaultRequestOptions(requestOptions).load(WebServiceURLs.IMAGE_URL + items.getImage1()).into(holder.iv_product_image1);
  //              Glide.with(mContext).setDefaultRequestOptions(requestOptions).load(WebServiceURLs.IMAGE_URL + items.getImage2()).into(holder.iv_product_image2);
                holder.tv_price_range.setText("" + items.getPrice());
                holder.tv_desc.setText(items.getDescription());



               /* holder.view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i=new Intent(mContext, ProductDetailsNewActivity.class);
                        i.putExtra("product_id",items.getProduct_id());
                        i.putExtra("product_name",items.getProduct_name());
                        i.putExtra("category_id",items.getCategory_id());
                        mContext.startActivity(i);
                    }
                });*/
                break;
            case VIEW_TYPE_SECTION :
                final Section section = (Section) mDataArrayList.get(position);
                holder.sectionTextView.setText(section.getName());

                holder.sectionToggleButton.setChecked(section.isExpanded);
                holder.sectionToggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        mSectionStateChangeListener.onSectionStateChanged(section, isChecked);
                    }
                });
                break;
        }
    }

    @Override
    public int getItemCount() {
        return mDataArrayList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (isSection(position))
            return VIEW_TYPE_SECTION;
        else return VIEW_TYPE_ITEM;
    }



    protected static class ViewHolder extends RecyclerView.ViewHolder {

        //common
        View view;
        int viewType;

        //for section
        TextView sectionTextView;
        ImageView iv_cat_image;
        ToggleButton sectionToggleButton;

        RoundedImageView iv_product_image2, iv_product_image1;
        ImageView iv_like;
        TextView tv_desc, tv_price_range, tv_product_name, tv_discount, tv_fragment;
        RatingBar ratingBar;
        SparkButton spark_button;
        ShimmerTextView shimmer_premium;
        CardView cv_shop_main;
        ShimmerFrameLayout shimmer_view_container1;

        public ViewHolder(View view, int viewType) {
            super(view);
            this.viewType = viewType;
            this.view = view;
            if (viewType == VIEW_TYPE_ITEM) {
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
            } else {
                sectionTextView = (TextView) view.findViewById(R.id.text_section);

            }
        }
    }
}
