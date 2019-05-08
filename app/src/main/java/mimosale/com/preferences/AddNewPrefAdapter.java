package mimosale.com.preferences;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import mimosale.com.R;
import mimosale.com.network.WebServiceURLs;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AddNewPrefAdapter  extends RecyclerView.Adapter<AddNewPrefAdapter.MyViewHolder> {
    List<AllPrefPojo> myPreferencePojoList;
    Context mctx;

    public AddNewPrefAdapter(List<AllPrefPojo> myPreferencePojoList, Context mctx) {
        this.myPreferencePojoList = myPreferencePojoList;
        this.mctx = mctx;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_my_preferences, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        final AllPrefPojo items = myPreferencePojoList.get(position);
        holder.iv_delete.setVisibility(View.GONE);
        holder.cb_add_new.setVisibility(View.VISIBLE);
        if (items.getSelected().equals("true"))
        {
            holder.cb_add_new.setChecked(true);
        }
        else
        {
            holder.cb_add_new.setChecked(false);
        }
        holder.iv_delete.setVisibility(View.GONE);
       // Glide.with(mctx).load(WebServiceURLs.IMAGE_URL+items.getPref_image()).into(holder.iv_pref_image);

        Picasso.with(mctx).load(WebServiceURLs.IMAGE_URL+items.getPref_image()).into(holder.iv_pref_image);
      //  Picasso.with(mctx).load(WebServiceURLs.SHOP_IMAGE + items.getImage2()).into(holder.iv_product_image2);

        holder.tv_pref_name.setText(items.getPref_name());


        holder.cb_add_new.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if ( isChecked )
                {
                   items.setSelected("true");

                }
                else
                {
                    items.setSelected("false");

                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return myPreferencePojoList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView iv_pref_image,iv_delete;
        TextView tv_pref_name;
        CheckBox cb_add_new;

        public MyViewHolder(View view) {
            super(view);
            iv_pref_image = (ImageView) view.findViewById(R.id.iv_pref_image);
            cb_add_new = (CheckBox) view.findViewById(R.id.cb_add_new);
            iv_delete = (ImageView) view.findViewById(R.id.iv_delete);
            tv_pref_name = (TextView) view.findViewById(R.id.tv_pref_name);

        }
    }



}
