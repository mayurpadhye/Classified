package mimosale.com.account;

import android.annotation.TargetApi;
import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import mimosale.com.R;

public class TermsAndConditionActivity extends AppCompatActivity {
WebView webview;
ImageView iv_back;
TextView toolbar_title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms_and_condition);
        webview=findViewById(R.id.webview);
        toolbar_title=findViewById(R.id.toolbar_title);
        iv_back=findViewById(R.id.iv_back);
        toolbar_title.setText(getResources().getString(R.string.toolbar_title));



        webview.getSettings().setJavaScriptEnabled(true); // enable javascript

        final Activity activity = this;

        webview.setWebViewClient(new WebViewClient() {
            @SuppressWarnings("deprecation")
            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                Toast.makeText(activity, description, Toast.LENGTH_SHORT).show();
            }
            @TargetApi(android.os.Build.VERSION_CODES.M)
            @Override
            public void onReceivedError(WebView view, WebResourceRequest req, WebResourceError rerr) {
                // Redirect to deprecated method, so you can use it in all SDK versions
                onReceivedError(view, rerr.getErrorCode(), rerr.getDescription().toString(), req.getUrl().toString());
            }
        });

        webview .loadUrl("http://classified.ccube9.com/public/termAndCondition");




        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }
}
