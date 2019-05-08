package mimosale.com.post;

import android.app.Dialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import mimosale.com.R;

import java.util.ArrayList;
import java.util.List;

public class MyPostingDetailsAcrivity extends AppCompatActivity implements View.OnClickListener {
    List<SalePreviewPojo> salePreviewPojoList = new ArrayList<>();
    ProductPreviewAdapter adapter;
    RecyclerView rv_products;
    Button btn_back, btn_submit;
    ImageView iv_back;
    TextView toolbar_title;
    Dialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_posting_details_acrivity);
        rv_products = findViewById(R.id.rv_products);

        iv_back = findViewById(R.id.iv_back);
        toolbar_title = findViewById(R.id.toolbar_title);

        iv_back.setOnClickListener(this);

        toolbar_title.setText(getResources().getString(R.string.posting_details));
        LinearLayoutManager llm = new LinearLayoutManager(MyPostingDetailsAcrivity.this, LinearLayoutManager.HORIZONTAL, false);
        rv_products.setLayoutManager(llm);
        for (int i = 0; i < 5; i++) {
            salePreviewPojoList.add(new SalePreviewPojo("", "bed", ""));
        }

        adapter = new ProductPreviewAdapter(salePreviewPojoList, MyPostingDetailsAcrivity.this);
        rv_products.setAdapter(adapter);


    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id. iv_back:
                finish();
                break;

        }
    }

    public void showPremimumDialog()
    {
        dialog=new Dialog(MyPostingDetailsAcrivity.this);
        dialog.setContentView(R.layout.dialog_go_premium);
        Button skip=dialog.findViewById(R.id.skip);
        Button btn_submit_dialog=dialog.findViewById(R.id.btn_submit_dialog);
        skip.setOnClickListener(this);
        btn_submit_dialog.setOnClickListener(this);
        dialog.show();
    }
}
