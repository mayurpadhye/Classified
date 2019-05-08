package mimosale.com.preferences;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import mimosale.com.R;
import mimosale.com.helperClass.PrefManager;
import mimosale.com.network.RestInterface;
import mimosale.com.network.RetrofitClient;
import mimosale.com.network.WebServiceURLs;
import com.google.gson.JsonElement;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MyPreferencesAdapter extends RecyclerView.Adapter<MyPreferencesAdapter.MyViewHolder> {
    List<MyPreferencePojo> myPreferencePojoList;
    Context mctx;
    ProgressBar p_bar;
    public MyPreferencesAdapter(List<MyPreferencePojo> myPreferencePojoList, Context mctx, ProgressBar p_bar) {
        this.myPreferencePojoList = myPreferencePojoList;
        this.mctx = mctx;
        this.p_bar=p_bar;
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
        final MyPreferencePojo items = myPreferencePojoList.get(position);
        holder.iv_delete.setVisibility(View.VISIBLE);
        holder.tv_pref_name.setText(items.getPref_name());

        Picasso.with(mctx).load(WebServiceURLs.IMAGE_URL+items.getPref_image()).into(holder.iv_pref_image);


        holder.iv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteConfirmationDialog(items.getPref_id(),position);

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

        public MyViewHolder(View view) {
            super(view);
            iv_pref_image = (ImageView) view.findViewById(R.id.iv_pref_image);
            iv_delete = (ImageView) view.findViewById(R.id.iv_delete);
            tv_pref_name = (TextView) view.findViewById(R.id.tv_pref_name);

        }
    }

    public void deleteUserPref(String preference_id, final int position)
    {
        try {
            p_bar.setVisibility(View.VISIBLE);
            RetrofitClient retrofitClient = new RetrofitClient();
            RestInterface service = retrofitClient.getAPIClient(WebServiceURLs.DOMAIN_NAME);
            service.deleteUserPref(PrefManager.getInstance(mctx).getUserId(), "Bearer " + PrefManager.getInstance(mctx).getApiToken(),preference_id, new Callback<JsonElement>() {
                @Override
                public void success(JsonElement jsonElement, Response response) {
                    //this method call if webservice success
                    try {
                        JSONObject jsonObject = new JSONObject(jsonElement.toString());
                        String status = jsonObject.getString("status");

                        if (status.equals("1")) {
                            myPreferencePojoList.remove(position);
                            notifyDataSetChanged();
                            Toast.makeText(mctx, ""+jsonObject.getString("message"), Toast.LENGTH_SHORT).show();

                        }

                        p_bar.setVisibility(View.GONE);
                    } catch (JSONException | NullPointerException e) {
                        e.printStackTrace();

                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    p_bar.setVisibility(View.GONE);
                    Toast.makeText(mctx, mctx.getString(R.string.check_internet), Toast.LENGTH_LONG).show();
                    Log.i("fdfdfdfdfdf", "" + error.getMessage());

                }
            });
        } catch (Exception e) {
            e.printStackTrace();

        }

    }//deleteUserPref

    public void deleteConfirmationDialog(final String pref_id, final int position)
    {
       /* AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mctx);
        final AlertDialog alertDialog = alertDialogBuilder.create();

        alertDialogBuilder.setMessage(mctx.getResources().getString(R.string.delete_msg));
                alertDialogBuilder.setPositiveButton("yes",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                               deleteUserPref(pref_id,position);
                            }
                        });

        alertDialogBuilder.setNegativeButton("No",new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
*/

        final AlertDialog.Builder builder = new AlertDialog.Builder(
                mctx);
        builder.setMessage(mctx.getResources().getString(R.string.delete_msg))
                .setCancelable(false)
                .setPositiveButton(mctx.getResources().getString(R.string.yes),
                        new DialogInterface.OnClickListener() {
                            //do something
                            public void onClick(DialogInterface dialog,
                                                int id) {
                                deleteUserPref(pref_id,position);
                            }
                        })
                .setNegativeButton(mctx.getResources().getString(R.string.no),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int id) {
                                dialog.cancel();
                            }
                        });
        final AlertDialog alert = builder.create();
        alert.show();
    }

}
