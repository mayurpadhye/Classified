package mimosale.com.shop;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
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

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class EventImagesAdapter extends RecyclerView.Adapter<EventImagesAdapter.MyViewHolder> {

    Context mContext;
    ArrayList<ImageVideoData> result_list;
    Activity activity;
    String event_from;

    public EventImagesAdapter(Context context, Activity activity, ArrayList<ImageVideoData> asm_list,String event_from) {

        this.mContext = context;
        this.result_list = asm_list;
        this.activity=activity;
        this.event_from=event_from;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.shop_file_creation, parent, false);
        return new EventImagesAdapter.MyViewHolder(v);
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        ImageVideoData imageVideoData=result_list.get(position);
        if(result_list.get(position).getBitmap()!=null){
            //  holder.iv_activity.setImageBitmap(result_list.get(position).getBitmap());
            RequestOptions requestOptions = new RequestOptions();
            requestOptions.diskCacheStrategy(DiskCacheStrategy.ALL);
            requestOptions.priority(Priority.IMMEDIATE);
            requestOptions.skipMemoryCache(false);
            requestOptions.dontAnimate();


            Glide.with(mContext)

                    .load(result_list.get(position).getPath()) // it can be a remote URL or a local absolute file path.
                    .apply(requestOptions)
                    .into(holder.iv_activity);

            Picasso.with(mContext).load(result_list.get(position).getPath()).into(holder.iv_activity);
           // Picasso.with(mctx).load(WebServiceURLs.SHOP_IMAGE + items.getImage2()).into(holder.iv_product_image2);



            Log.i("imagePathhhhhBitmap",result_list.get(position).getPath());
        }else{
            holder.iv_activity.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_default_image));
            if(result_list.get(position).getPath()!=null)
                holder.iv_activity.setImageURI(Uri.parse(imageVideoData.getPath()));
               /* Glide.with(mContext)

                        .load(result_list.get(position).getPath()) // it can be a remote URL or a local absolute file path.
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .priority(Priority.IMMEDIATE)
                        .skipMemoryCache(false)
                        .dontAnimate()
                        .into(holder.iv_activity);*/
            Log.i("imagePathhhhh",result_list.get(position).getPath());

        }
        holder.iv_remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (result_list.size()>0)
                {
                    if (event_from.equals("create"))
                    {
                        if(((ShopPostingActivity)activity).imageFiles.size()>0) {


                            new SweetAlertDialog(mContext, SweetAlertDialog.WARNING_TYPE)
                                    .setTitleText(mContext.getResources().getString(R.string.are_you_sure))
                                    .setContentText(mContext.getResources().getString(R.string.want_to_delete))
                                    .setConfirmText(mContext.getResources().getString(R.string.delete))
                                    .setCancelText(mContext.getResources().getString(R.string.no))

                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sDialog) {
                                            result_list.remove(position);// removes from list
                                            ((ShopPostingActivity) activity).imageFiles.remove(position);
                                            notifyItemRemoved(position); // updates position
                                            notifyItemRangeChanged(position, result_list.size());
                                            sDialog.dismissWithAnimation();
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

                    }
                    else
                    {


                    }
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return result_list.size();
    }


    @Override
    public long getItemId(int i) {
        return i;
    }



    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        ImageView iv_activity,iv_icon,iv_remove;

        MyViewHolder(View view) {
            super(view);
            iv_activity = view.findViewById(R.id.img_event_file);
            iv_activity.setOnClickListener(this);
            iv_remove=view.findViewById(R.id.img_remove);
            // iv_remove.setOnClickListener(this);
            iv_icon = view.findViewById(R.id.img_icon);
            iv_icon.setVisibility(View.GONE);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition=this.getAdapterPosition();
            switch (v.getId())
            {
                case R.id.img_event_file:
                    ArrayList<String> arrayList=new ArrayList<>();
                    for(int i=0;i<result_list.size();i++){
                        arrayList.add(result_list.get(i).getPath());
                    }
                    break;




            }
        }
    }
}
