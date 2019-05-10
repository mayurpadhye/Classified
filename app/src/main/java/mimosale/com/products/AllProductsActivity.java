package mimosale.com.products;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import mimosale.com.R;

public class AllProductsActivity extends AppCompatActivity implements View.OnClickListener {
TextView toolbar_title;
ImageView iv_back;
FloatingActionButton fb_add;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_products);
        initView();

    }
    public void initView()
    {
        toolbar_title=findViewById(R.id.toolbar_title);
        fb_add=findViewById(R.id.fb_add);
        iv_back=findViewById(R.id.iv_back);
        toolbar_title.setText(getResources().getString(R.string.products));
        fb_add.setOnClickListener(this);
        iv_back.setOnClickListener(this);
    }//initViewClose

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.fb_add:
                startActivity(new Intent(AllProductsActivity.this,AddProductsActivity.class).putExtra("isUpdate","false"));
                break;
            case R.id.iv_back:
                finish();
                break;

        }
    }
}
