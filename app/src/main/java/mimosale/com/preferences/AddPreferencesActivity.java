package mimosale.com.preferences;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import mimosale.com.R;
import com.joaquimley.faboptions.FabOptions;

import java.util.ArrayList;
import java.util.List;

public class AddPreferencesActivity extends AppCompatActivity implements View.OnClickListener {
    FabOptions fabOptions;
    TextView toolbar_title;
    ImageView iv_back;
    List<MyPreferencePojo> myPreferencePojoList;
    RecyclerView rv_my_preferences;
    int numberOfColumns=2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_preferences);
        initView();
        toolbar_title.setText(getResources().getString(R.string.my_preferences));
    }

    public void initView()
    {
        myPreferencePojoList=new ArrayList<MyPreferencePojo>();
         fabOptions = (FabOptions) findViewById(R.id.fab_options);
         toolbar_title=findViewById(R.id.toolbar_title);
        rv_my_preferences=findViewById(R.id.rv_my_preferences);
        iv_back=findViewById(R.id.iv_back);
        rv_my_preferences.setLayoutManager(new GridLayoutManager(this, numberOfColumns));
        fabOptions.setOnClickListener(this);
        iv_back.setOnClickListener(this);






    }//initViewClose

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.iv_back:
                finish();
                break;
            case R.id.fab_add_pref:
                Toast.makeText(AddPreferencesActivity.this, "Favorite", Toast.LENGTH_SHORT).show();
                break;

            case R.id.fab_edit:
                Toast.makeText(AddPreferencesActivity.this, "Message", Toast.LENGTH_SHORT).show();
                break;

        }
    }
}
