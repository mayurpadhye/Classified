package mimosale.com.post;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import mimosale.com.R;
import mimosale.com.preferences.MyPreferencePojo;
import mimosale.com.preferences.MyPreferencesAdapter;

import java.util.List;

public class MyPostingAdapter  extends RecyclerView.Adapter<MyPostingAdapter.MyViewHolder> {
    List<MyPreferencePojo> myPreferencePojoList;
    Context mctx;

    public MyPostingAdapter(List<MyPreferencePojo> myPreferencePojoList, Context mctx) {
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
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final MyPreferencePojo items = myPreferencePojoList.get(position);

        holder.tv_pref_name.setText(items.getPref_name());
        holder.iv_pref_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mctx.startActivity(new Intent(mctx,MyPostingDetailsAcrivity.class));
            }
        });


    }

    @Override
    public int getItemCount() {
        return myPreferencePojoList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView iv_pref_image;
        TextView tv_pref_name;

        public MyViewHolder(View view) {
            super(view);
            iv_pref_image = (ImageView) view.findViewById(R.id.iv_pref_image);
            tv_pref_name = (TextView) view.findViewById(R.id.tv_pref_name);

        }
    }
}