package mimosale.com.login;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import mimosale.com.R;
import mimosale.com.feedback.FeedBackActivity;
import mimosale.com.helperClass.PrefManager;
import mimosale.com.home.HomeActivity;
import mimosale.com.network.RestInterface;
import mimosale.com.network.RetrofitClient;
import mimosale.com.network.WebServiceURLs;
import mimosale.com.signup.RegistrationActivity;
import com.google.gson.JsonElement;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class LoginActivity extends AppCompatActivity {
EditText et_email,et_password;
TextView tv_forgot_pass,tv_sign_up_msg;
Button btn_login;
Toolbar toolbar;
TextView toolbar_title;
ImageView iv_back;
ProgressBar p_bar;
TextInputLayout tl_pass,tl_email;
RelativeLayout rl_main;
String intent_from="";
    View.OnClickListener mOnClickListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
        Intent i=getIntent();
        try{
            intent_from=i.getStringExtra("intent_from");
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        tv_sign_up_msg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // startActivity(new Intent(LoginActivity.this,RegistrationActivity.class));

                Intent i = new Intent(LoginActivity.this, RegistrationActivity.class);
                i.putExtra("intent_from","login");
                startActivityForResult(i, 1);
            }
        });
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkValidation();

            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        iv_back.setVisibility(View.GONE);
    toolbar_title.setText(getResources().getString(R.string.login));
        toolbar_title.setAllCaps(true);
    }




    public void Login()
    {

        try{
            p_bar.setVisibility(View.VISIBLE);
            RetrofitClient  retrofitClient = new RetrofitClient();
            RestInterface service = retrofitClient.getAPIClient(WebServiceURLs.DOMAIN_NAME);
        service.LoginToApp(et_email.getText().toString(), et_password.getText().toString(), new Callback<JsonElement>() {
            @Override
            public void success(JsonElement jsonElement, Response response) {

                try {
                    JSONObject jsonObject = new JSONObject(jsonElement.toString());
                    String status=jsonObject.getString("status");
                    String message=jsonObject.getString("message");
                    Toast.makeText(LoginActivity.this, ""+message, Toast.LENGTH_SHORT).show();
                    if (status.equals("1"))
                    {
                        String token= jsonObject.getString("token");
                        JSONObject data=jsonObject.getJSONObject("data");
                        String id=data.getString("id");
                        String first_name=data.getString("first_name");
                        String email=data.getString("email");
                        String last_name=data.getString("last_name");
                        String mobile=data.getString("mobile");
                        String is_submit_feedback=data.getString("is_submit_feedback");
                        String profile_image=data.getString("profile_image");
                        PrefManager.getInstance(LoginActivity.this);
                        PrefManager.setEmail(email);
                        PrefManager.setApiToken(token);
                        PrefManager.setFirstName(first_name);
                        PrefManager.setLastName(last_name);
                        PrefManager.setUserId(id);
                        PrefManager.setMobile(mobile);
                        PrefManager.setProfilePic(profile_image);
                        PrefManager.setIsLogin(true);
                        PrefManager.getInstance(LoginActivity.this).setFeedbackStatus(is_submit_feedback);

                        if (intent_from.equals("login"))
                        {
                            startActivity(new Intent(LoginActivity.this,HomeActivity.class));
                            finish();
                        }
                        else
                        {
                            Intent intent = new Intent();
                            intent.putExtra("intent_from",  intent_from);
                            setResult(RESULT_OK, intent);
                            finish();
                        }

                    }




                } catch (JSONException | NullPointerException e) {
                    p_bar.setVisibility(View.GONE);
                    e.printStackTrace();

              }
            }

            @Override
            public void failure(RetrofitError error) {
                p_bar.setVisibility(View.GONE);
                Toast.makeText(LoginActivity.this ,getResources().getString(R.string.check_internet), Toast.LENGTH_LONG ).show();

            }
        });
    }
        catch ( Exception e){
        e.printStackTrace();

    }

}



    public void initView()
    {
        toolbar=findViewById(R.id.toolbar);
        rl_main=findViewById(R.id.rl_main);
        tl_email=findViewById(R.id.tl_email);
        tl_pass=findViewById(R.id.tl_pass);
        toolbar_title=findViewById(R.id.toolbar_title);
        iv_back=findViewById(R.id.iv_back);
        tv_sign_up_msg=findViewById(R.id.tv_sign_up_msg);
        tv_forgot_pass=findViewById(R.id.tv_forgot_pass);
        btn_login=findViewById(R.id.btn_login);
        et_password=findViewById(R.id.et_password);
        et_email=findViewById(R.id.et_email);
        p_bar=findViewById(R.id.p_bar);
        Spannable word = new SpannableString(getResources().getString(R.string.dont_have_acc));

        word.setSpan(new ForegroundColorSpan(Color.GRAY), 0, word.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        tv_sign_up_msg.setText(word+"  ");
        Spannable wordTwo = new SpannableString(getResources().getString(R.string.register_here));
        wordTwo.setSpan(new UnderlineSpan(), 0, wordTwo.length(), 0);
        wordTwo.setSpan(new StyleSpan(Typeface.BOLD), 0, wordTwo.length(), 0);
        wordTwo.setSpan(new ForegroundColorSpan(Color.BLACK), 0, wordTwo.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        int startIndexOfPath = wordTwo.toString().indexOf(getResources().getString(R.string.register_here));
        wordTwo.setSpan(new AbsoluteSizeSpan(50), startIndexOfPath,
                startIndexOfPath + getResources().getString(R.string.register_here).length(), 0);
        tv_sign_up_msg.append(wordTwo);

    }//initViewClose

    public void checkValidation()
    {
        if (!isValidMail(et_email.getText().toString().trim(),et_email))
        {

            if (et_email.getText().toString().trim().length()==0)
            {
                et_email.requestFocus();
                tl_email.setError(getResources().getString(R.string.enter_email_id));
                Snackbar.make(rl_main, ""+getResources().getString(R.string.enter_email_id), Snackbar.LENGTH_LONG)
                        .setAction(getResources().getString(R.string.retry), mOnClickListener)
                        .setActionTextColor(Color.RED)
                        .show();
            }
            else
            {
                et_email.requestFocus();
                tl_email.setError(getResources().getString(R.string.email_error));
                Snackbar.make(rl_main, ""+getResources().getString(R.string.email_error), Snackbar.LENGTH_LONG)
                        .setAction(getResources().getString(R.string.retry), mOnClickListener)
                        .setActionTextColor(Color.RED)
                        .show();
            }



            mOnClickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   checkValidation();
                }
            };

            return;
        }
        if (!validatePassword(et_password.getText().toString().trim()))
        {
            et_password.requestFocus();
            tl_pass.setError(getResources().getString(R.string.password_error));
            Snackbar.make(rl_main, ""+getResources().getString(R.string.password_error), Snackbar.LENGTH_LONG)
                    .setAction(getResources().getString(R.string.retry), mOnClickListener)
                    .setActionTextColor(Color.RED)
                    .show();

            mOnClickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    checkValidation();
                }
            };
            return;
        }
        Login();
    }//checkValidationClose

    public boolean validatePassword( String password)
    {
        if (password.length()>0)
        {
            return true;
        }
        else
            return false;
    }

    private boolean isValidMail(String email,EditText et_email) {
        boolean check;
        Pattern p;
        Matcher m;

        String EMAIL_STRING = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        p = Pattern.compile(EMAIL_STRING);

        m = p.matcher(email);
        check = m.matches();

        if(!check) {
            et_email.requestFocus();
            tl_email.setError(getResources().getString(R.string.email_error));
        }
        return check;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

         if (requestCode==1)
         {
             if (data!=null)
             {
                 intent_from=data.getStringExtra("intent_from");
             }
         }

    }
}
