package mimosale.com.post;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import mimosale.com.R;
import mimosale.com.shop.ShopPreviewActivity;

import java.util.ArrayList;
import java.util.List;

public class SalePostingPreviewActivity extends AppCompatActivity implements View.OnClickListener {
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
        setContentView(R.layout.activity_sale_posting_preview);
        rv_products = findViewById(R.id.rv_products);
        btn_back = findViewById(R.id.btn_back);
        btn_submit = findViewById(R.id.btn_submit);
        iv_back = findViewById(R.id.iv_back);
        toolbar_title = findViewById(R.id.toolbar_title);

        btn_back.setOnClickListener(this);
        btn_submit.setOnClickListener(this);
        iv_back.setOnClickListener(this);


        toolbar_title.setText("");
        LinearLayoutManager llm = new LinearLayoutManager(SalePostingPreviewActivity.this, LinearLayoutManager.HORIZONTAL, false);
        rv_products.setLayoutManager(llm);
        for (int i = 0; i < 5; i++) {
            salePreviewPojoList.add(new SalePreviewPojo("", "bed", ""));
        }

        adapter = new ProductPreviewAdapter(salePreviewPojoList, SalePostingPreviewActivity.this);
        rv_products.setAdapter(adapter);
    }//onCreateClose

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.btn_submit:
                showPremimumDialog();
                break;
            case R.id.skip:
               dialog.dismiss();
                break;
            case R.id.btn_submit_dialog:
                dialog.dismiss();
                break;
            case R.id.iv_back:
                finish();
                break;
        }
    }

    public void showPremimumDialog()
    {
        dialog=new Dialog(SalePostingPreviewActivity.this);
        dialog.setContentView(R.layout.dialog_go_premium);
        Button skip=dialog.findViewById(R.id.skip);
        Button btn_submit_dialog=dialog.findViewById(R.id.btn_submit_dialog);
        skip.setOnClickListener(this);
        btn_submit_dialog.setOnClickListener(this);
        dialog.show();
    }
}
