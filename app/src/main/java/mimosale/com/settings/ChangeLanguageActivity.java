package mimosale.com.settings;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatSpinner;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import mimosale.com.R;
import mimosale.com.helperClass.CustomUtils;
import mimosale.com.helperClass.PrefManager;
import mimosale.com.home.HomeActivity;

import java.util.Locale;

public class ChangeLanguageActivity extends AppCompatActivity {
ImageView iv_back;
    TextView toolbar_title;
    AppCompatSpinner spinner;
   PrefManager manager;
    Locale LOCALE;
    Configuration CONFIG;
    boolean start = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_language);
        toolbar_title=findViewById(R.id.toolbar_title);
        iv_back=findViewById(R.id.iv_back);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        toolbar_title.setText(getResources().getString(R.string.change_language));

        start = true;
       manager=  PrefManager.getInstance(ChangeLanguageActivity.this);
        String language = PrefManager.getInstance(ChangeLanguageActivity.this).getAppLanguageCode();
        if(language.isEmpty())
            language= CustomUtils.JAPNEESE_CODE;

        String languageToLoad  =language; // your language
        Locale locale = new Locale(languageToLoad);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());



        spinner = findViewById(R.id.spinner_language);


        if (PrefManager.getInstance(ChangeLanguageActivity.this).getLanguage().equals(CustomUtils.JAPAN)) {
            spinner.setSelected(true);
            spinner.setSelection(1);
        } else {
            spinner.setSelected(true);
            spinner.setSelection(0);
        }
        Log.i("strtValue",""+start);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {

                if (!start) {
                    switch (position) {
                        case 0:

                            manager.setLanguage(CustomUtils.ENGLISH);
                            manager.setAppLanguageCode(CustomUtils.ENGLISH_CODE);
                            LOCALE = new Locale(CustomUtils.ENGLISH);
                            Locale.setDefault(LOCALE);
                            CONFIG = new Configuration();
                            CONFIG.locale = LOCALE;
                            CONFIG.setLocale(LOCALE);
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                                getApplicationContext().createConfigurationContext(CONFIG);
                            }
                            getResources().updateConfiguration(CONFIG, getResources().getDisplayMetrics());
                            onConfigurationChanged(CONFIG);
                            PrefManager.getInstance(ChangeLanguageActivity.this).setLanguage("1");
                           /* start=true;
                            recreate();*/

                           /* Intent intent = getIntent();
                            intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                            finish();
                            startActivity(intent);*/

                           startActivity(new Intent(ChangeLanguageActivity.this, HomeActivity.class));
                           finishAffinity();
                            break;

                        case 1:
                            manager.setLanguage(CustomUtils.JAPAN);
                            manager.setAppLanguageCode(CustomUtils.JAPNEESE_CODE);
                            LOCALE = new Locale(CustomUtils.JAPAN);
                            Locale.setDefault(LOCALE);
                            CONFIG = new Configuration();
                            CONFIG.locale = LOCALE;
                            CONFIG.setLocale(LOCALE);
                            getApplicationContext().createConfigurationContext(CONFIG);
                            getResources().updateConfiguration(CONFIG, getResources().getDisplayMetrics());
                            onConfigurationChanged(CONFIG);
                           /* start=true;
                           recreate();*/
                            PrefManager.getInstance(ChangeLanguageActivity.this).setLanguage("2");
                            startActivity(new Intent(ChangeLanguageActivity.this, HomeActivity.class));
                            finishAffinity();

                           /* Intent intent1 = getIntent();
                            intent1.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                            finish();
                            startActivity(intent1);*/
                            break;
                    }
                } else {
                    start = false;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


    }
}
