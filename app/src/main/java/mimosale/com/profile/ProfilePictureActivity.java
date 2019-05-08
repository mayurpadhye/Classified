package mimosale.com.profile;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import mimosale.com.R;
import mimosale.com.helperClass.PrefManager;
import mimosale.com.network.WebServiceURLs;
import com.squareup.picasso.Picasso;

public class ProfilePictureActivity extends AppCompatActivity {
ImageView iv_close,iv_profile_pic;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_picture);
        initView();
        Picasso.with(ProfilePictureActivity.this).load(WebServiceURLs.IMAGE_URL + PrefManager.getInstance(ProfilePictureActivity.this).getProfilePic()).into(iv_profile_pic);
        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void initView()
    {
        iv_close=findViewById(R.id.iv_close);
        iv_profile_pic=findViewById(R.id.iv_profile_pic);
    }
}
