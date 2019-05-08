package mimosale.com.preferences;

import android.content.Context;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import mimosale.com.R;
import mimosale.com.home.HomeFragment;

public class MyPreferencesActivity extends AppCompatActivity {
    TabLayout tabLayout;
    ViewPager viewPager;
    TextView toolbar_title;
    ImageView iv_back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_preferences);
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        toolbar_title=findViewById(R.id. toolbar_title);
        iv_back=findViewById(R.id.iv_back);
        tabLayout.addTab(tabLayout.newTab().setText(getResources().getString(R.string.my_preferences)));
        tabLayout.addTab(tabLayout.newTab().setText(getResources().getString(R.string.add_new)));

        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final MyPreferenceAdapter adapter = new MyPreferenceAdapter(this, getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        toolbar_title.setText(getResources().getString(R.string.my_preferences));
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    public class MyPreferenceAdapter extends FragmentPagerAdapter {

        private Context myContext;
        int totalTabs;

        public MyPreferenceAdapter(Context context, FragmentManager fm, int totalTabs) {
            super(fm);
            myContext = context;
            this.totalTabs = totalTabs;
        }

        // this is for fragment tabs
        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    MyPreferenceFragment myPreferenceFragment = new MyPreferenceFragment();
                    return myPreferenceFragment;
                case 1:
                    AddNewPreferenceFragment addNewPreferenceFragment = new AddNewPreferenceFragment();
                    return addNewPreferenceFragment;

                default:
                    return null;
            }
        }

        // this counts total number of tabs
        @Override
        public int getCount() {
            return totalTabs;
        }
    }




}
