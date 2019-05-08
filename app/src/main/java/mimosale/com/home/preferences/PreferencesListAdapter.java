package mimosale.com.home.preferences;

import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
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
import mimosale.com.helperClass.RoundRectCornerImageView;
import mimosale.com.home.HomeFragment;
import mimosale.com.network.WebServiceURLs;
import mimosale.com.preferences.MyPreferencePojo;
import mimosale.com.preferences.MyPreferencesActivity;
import com.github.siyamed.shapeimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class PreferencesListAdapter extends RecyclerView.Adapter<PreferencesListAdapter.MyViewHolder> {
    List<PreferenceListModel> allPrefList;
    Context mctx;
    HomeFragment homeFragment;

    public PreferencesListAdapter(List<PreferenceListModel> allPrefList, Context mctx, HomeFragment homeFragment) {
        this.allPrefList = allPrefList;
        this.mctx = mctx;
        this.homeFragment = homeFragment;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView;

        if(viewType == R.layout.row_preferences){
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_preferences, parent, false);
        }

        else {
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.add_pref_layout, parent, false);
        }


        itemView.getLayoutParams().width = (int) (getScreenWidth() / 4.5);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {

if (position==allPrefList.size())
{
    holder.cv_add_pref.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //homeFragment.addPost();
           mctx.startActivity(new Intent(mctx, MyPreferencesActivity.class));

        }
    });

}
else {
    final PreferenceListModel items = allPrefList.get(position);
    holder.tv_pref_name.setText(items.getPref_name());
    RequestOptions requestOptions = new RequestOptions();
    requestOptions.placeholder(R.drawable.placeholder_logo);
    requestOptions.fitCenter();
  //  Glide.with(mctx).setDefaultRequestOptions(requestOptions).load(WebServiceURLs.IMAGE_URL + items.getPref_image()).into(holder.ri_pref_image);


    Picasso.with(mctx).load(WebServiceURLs.IMAGE_URL + items.getPref_image()).into(holder.ri_pref_image);




}

    }

    @Override
    public int getItemViewType(int position) {
        return (position == allPrefList.size()) ? R.layout.add_pref_layout : R.layout.row_preferences;
    }

    @Override
    public int getItemCount() {
        return allPrefList.size()+1;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        RoundedImageView ri_pref_image;
        TextView tv_pref_name;

        CardView cv_add_pref;

        public MyViewHolder(View view) {
            super(view);
            ri_pref_image = (RoundedImageView) view.findViewById(R.id.ri_pref_image);
            tv_pref_name = (TextView) view.findViewById(R.id.tv_pref_name);

            cv_add_pref = (CardView) view.findViewById(R.id.cv_add_pref);

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