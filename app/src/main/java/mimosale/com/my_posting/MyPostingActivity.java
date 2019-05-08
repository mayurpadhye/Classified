package mimosale.com.my_posting;

import android.app.Dialog;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import mimosale.com.R;
import mimosale.com.home.fragments.AllTypeFragment;
import mimosale.com.home.fragments.EnhancedWrapContentViewPager;
import mimosale.com.home.fragments.ProductsFragment;
import mimosale.com.home.fragments.ShopSaleFragment;
import mimosale.com.post.MyPostingAdapter;
import mimosale.com.post.SalePostingActivity;
import mimosale.com.preferences.MyPreferencePojo;
import mimosale.com.products.AddProductsActivity;
import mimosale.com.shop.ShopPostingActivity;

import java.util.ArrayList;
import java.util.List;

public class MyPostingActivity extends AppCompatActivity implements View.OnClickListener {
RecyclerView rv_posting_list;
int numberOfColumns=2;
ImageView iv_back;
TextView toolbar_title;
FloatingActionButton fb_add;
  Dialog dialog;
List<MyPreferencePojo>myPreferencePojoList=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_posting);
        initView();

    }
    public void initView()
    {
        rv_posting_list=findViewById(R.id.rv_posting_list);
        toolbar_title=findViewById(R.id.toolbar_title);
        iv_back=findViewById(R.id.iv_back);
        fb_add=findViewById(R.id.fb_add);
        toolbar_title.setText(getResources().getString(R.string.my_posting));
        rv_posting_list.setLayoutManager(new GridLayoutManager(this, numberOfColumns));
       // fabOptions.setOnClickListener(this);
        iv_back.setOnClickListener(this);
        fb_add.setOnClickListener(this);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.addTab(tabLayout.newTab().setText("A"));
        tabLayout.addTab(tabLayout.newTab().setText("B"));
        tabLayout.addTab(tabLayout.newTab().setText("C"));
        final EnhancedWrapContentViewPager viewPager = (EnhancedWrapContentViewPager) findViewById(R.id.viewpager);
        // mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(MyPostingActivity.this);
        mLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);

        //mRecyclerView.setLayoutManager(mLayoutManager);
        viewPager.setAdapter(new PagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount()));
        viewPager.measure(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        viewPager.setOffscreenPageLimit(0);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);

        for (int i=0;i<10;i++)
        {
            myPreferencePojoList.add(new MyPreferencePojo("Furniture","","1"));
        }
        MyPostingAdapter adapter=new MyPostingAdapter(myPreferencePojoList,MyPostingActivity.this);
        rv_posting_list.setAdapter(adapter);

    }//initViewClose

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.iv_back:
                finish();
                break;
            case R.id.fb_add:
                showSelectPostingDailog();
                break;
            case R.id.rb_shop_posting:
                startActivity(new Intent(MyPostingActivity.this,ShopPostingActivity.class));
                dialog.dismiss();
                break;
            case R.id.rb_sale_posting:
                startActivity(new Intent(MyPostingActivity.this, SalePostingActivity.class));
                dialog.dismiss();
                break;
            case R.id.rb_product_posting:
                startActivity(new Intent(MyPostingActivity.this,AddProductsActivity.class));
                dialog.dismiss();
                break;
        }
    }

    public void showSelectPostingDailog()
    {
        dialog=new Dialog(this);
        dialog.setContentView(R.layout.dialog_select_posting);
        RadioButton rb_shop_posting=dialog.findViewById(R.id.rb_shop_posting);
        RadioButton rb_sale_posting=dialog.findViewById(R.id.rb_sale_posting);
        RadioButton rb_product_posting=dialog.findViewById(R.id.rb_product_posting);
        rb_shop_posting.setOnClickListener(this);
        rb_sale_posting.setOnClickListener(this);
        rb_product_posting.setOnClickListener(this);
        dialog.show();



    }

    public class PagerAdapter extends FragmentStatePagerAdapter {
        int mNumOfTabs;

        public PagerAdapter(FragmentManager fm, int NumOfTabs) {
            super(fm);
            this.mNumOfTabs = NumOfTabs;
        }



        @Override
        public Fragment getItem(int position) {

            switch (position) {
                case 0:
                    return new MyShopPostingFragment();
                case 1:
                    return new MySalePostingFragment();
                case 2:
                    return new MyProductPostingFragment();

                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return mNumOfTabs;
        }
        @Override
        public CharSequence getPageTitle(int position) {
            String title = null;
            if (position == 0)
            {
                title = "Shop";
            }
            else if (position == 1)
            {
                title = "Sale";
            }
            else if (position == 2)
            {
                title = "Products";
            }
            return title;
        }
    }

}
