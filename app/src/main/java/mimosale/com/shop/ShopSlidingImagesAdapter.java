package mimosale.com.shop;

import android.content.Context;
import android.net.Uri;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import mimosale.com.R;
import mimosale.com.network.WebServiceURLs;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class ShopSlidingImagesAdapter  extends PagerAdapter {
    private ArrayList<ImageVideoData> IMAGES;
    private LayoutInflater inflater;
    private Context context;

    public ShopSlidingImagesAdapter(ArrayList<ImageVideoData> IMAGES, LayoutInflater inflater, Context context) {
        this.IMAGES = IMAGES;
        this.inflater = inflater;
        this.context = context;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return IMAGES.size();
    }

    @Override
    public Object instantiateItem(ViewGroup view, int position) {
        View imageLayout = inflater.inflate(R.layout.sliding_shop_images, view, false);

        assert imageLayout != null;
        final ImageView imageView = (ImageView) imageLayout
                .findViewById(R.id.image);




        ImageVideoData imageVideoData=IMAGES.get(position);
        if(IMAGES.get(position).getBitmap()!=null){
            //  holder.iv_activity.setImageBitmap(result_list.get(position).getBitmap());
            RequestOptions requestOptions = new RequestOptions();
            requestOptions.diskCacheStrategy(DiskCacheStrategy.ALL);
            requestOptions.priority(Priority.IMMEDIATE);
            requestOptions.skipMemoryCache(false);
            requestOptions.dontAnimate();


            Picasso.with(context).load(IMAGES.get(position).getPath()).into(imageView);



            Log.i("imagePathhhhhBitmap",IMAGES.get(position).getPath());
        }


      //  imageView.setImageResource(IMAGES.get(position));

        view.addView(imageLayout, 0);

        return imageLayout;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

    @Override
    public void restoreState(Parcelable state, ClassLoader loader) {
    }

    @Override
    public Parcelable saveState() {
        return null;
    }
}
