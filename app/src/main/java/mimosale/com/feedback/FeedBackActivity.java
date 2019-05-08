package mimosale.com.feedback;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import mimosale.com.R;
import mimosale.com.helperClass.PrefManager;
import mimosale.com.home.HomeActivity;
import mimosale.com.login.LoginActivity;
import mimosale.com.network.RestInterface;
import mimosale.com.network.RetrofitClient;
import mimosale.com.network.WebServiceURLs;
import mimosale.com.profile.ProfileActivity;
import com.google.gson.JsonElement;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class FeedBackActivity extends AppCompatActivity {
TextView toolbar_title;
ImageView iv_back;
RadioGroup rg_que_one;
RadioGroup rg_que_two,rg_que_three,rg_que_four,rg_que_five,rg_que_six,rg_que_seven,rg_que_eight,rg_que_nine,rg_que_ten;
EditText et_que_eleven;
Button btn_submit;
LinearLayout ll_feed_submitted,ll_feedback;
ProgressBar p_bar;
String ans_one="",ans_two="",ans_three="",ans_four="",ans_five="",ans_six="",ans_seven="",ans_eight="",ans_nine="",ans_ten="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_back);
        toolbar_title = findViewById(R.id.toolbar_title);
        iv_back = findViewById(R.id.iv_back);
        p_bar = findViewById(R.id.p_bar);
        toolbar_title.setText(getResources().getString(R.string.feedback));
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        initView();


        rg_que_one.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            public void onCheckedChanged(RadioGroup group, int checkedId)
            {
             RadioButton checkedRadioButton = (RadioButton)group.findViewById(checkedId);

                boolean isChecked = checkedRadioButton.isChecked();

                if (isChecked)
                {  ans_one=""+checkedRadioButton.getText();
                }
            }
        });

        rg_que_two.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            public void onCheckedChanged(RadioGroup group, int checkedId)
            {
                RadioButton checkedRadioButton = (RadioButton)group.findViewById(checkedId);

                boolean isChecked = checkedRadioButton.isChecked();

                if (isChecked)
                {  ans_two=""+checkedRadioButton.getText();
                }
            }
        });
        rg_que_three.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            public void onCheckedChanged(RadioGroup group, int checkedId)
            {
                RadioButton checkedRadioButton = (RadioButton)group.findViewById(checkedId);

                boolean isChecked = checkedRadioButton.isChecked();

                if (isChecked)
                {
                    ans_three=""+checkedRadioButton.getText();

                }
            }
        });
        rg_que_four.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            public void onCheckedChanged(RadioGroup group, int checkedId)
            {
                RadioButton checkedRadioButton = (RadioButton)group.findViewById(checkedId);

                boolean isChecked = checkedRadioButton.isChecked();

                if (isChecked)
                {
                    ans_four=""+checkedRadioButton.getText();

                }
            }
        });
        rg_que_five.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            public void onCheckedChanged(RadioGroup group, int checkedId)
            {
                RadioButton checkedRadioButton = (RadioButton)group.findViewById(checkedId);

                boolean isChecked = checkedRadioButton.isChecked();

                if (isChecked)
                {
                    ans_five=""+checkedRadioButton.getText();

                }
            }
        });
        rg_que_six.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            public void onCheckedChanged(RadioGroup group, int checkedId)
            {
                RadioButton checkedRadioButton = (RadioButton)group.findViewById(checkedId);

                boolean isChecked = checkedRadioButton.isChecked();

                if (isChecked)
                {
                    ans_six=""+checkedRadioButton.getText();

                }
            }
        });
        rg_que_seven.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            public void onCheckedChanged(RadioGroup group, int checkedId)
            {
                RadioButton checkedRadioButton = (RadioButton)group.findViewById(checkedId);

                boolean isChecked = checkedRadioButton.isChecked();

                if (isChecked)
                {
                    ans_seven=""+checkedRadioButton.getText();

                }
            }
        });

        rg_que_eight.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            public void onCheckedChanged(RadioGroup group, int checkedId)
            {
                RadioButton checkedRadioButton = (RadioButton)group.findViewById(checkedId);

                boolean isChecked = checkedRadioButton.isChecked();

                if (isChecked)
                {
                    ans_eight=""+checkedRadioButton.getText();

                }
            }
        });

        rg_que_nine.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            public void onCheckedChanged(RadioGroup group, int checkedId)
            {
                RadioButton checkedRadioButton = (RadioButton)group.findViewById(checkedId);

                boolean isChecked = checkedRadioButton.isChecked();

                if (isChecked)
                {
                    ans_nine=""+checkedRadioButton.getText();

                }
            }
        });

        rg_que_ten.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            public void onCheckedChanged(RadioGroup group, int checkedId)
            {
                RadioButton checkedRadioButton = (RadioButton)group.findViewById(checkedId);

                boolean isChecked = checkedRadioButton.isChecked();

                if (isChecked)
                {
                    ans_ten=""+checkedRadioButton.getText();

                }
            }
        });

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ans_one.equals("")||ans_two.equals("")||ans_three.equals("")||ans_four.equals("")||ans_five.equals("")||ans_six.equals("")||ans_seven.equals("")
                        ||ans_eight.equals("")||ans_nine.equals("")|| ans_ten.equals("")||et_que_eleven.getText().toString().trim().length()==0)
                {
                    Toast.makeText(FeedBackActivity.this, ""+getResources().getString(R.string.attempt_all), Toast.LENGTH_SHORT).show();
                    return;
                }
                else
                {
                    subMitFeedbackForm();
                    //Toast.makeText(FeedBackActivity.this, "success", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }


    public void subMitFeedbackForm()
    {
        JSONArray data=new JSONArray();
String user_id=PrefManager.getInstance(FeedBackActivity.this).getUserId();
        JSONObject j1=new JSONObject();
        try {
            j1.put("user_id",user_id);
            j1.put("question_id","1");
            j1.put("answer",ans_one);
            data.put(j1);
            JSONObject j2=new JSONObject();
            j2.put("user_id",user_id);
            j2.put("question_id","2");
            j2.put("answer",ans_two);
            data.put(j2);

            JSONObject j3=new JSONObject();
            j3.put("user_id",user_id);
            j3.put("question_id","3");
            j3.put("answer",ans_three);
            data.put(j3);
            JSONObject j4=new JSONObject();
            j4.put("user_id",user_id);
            j4.put("question_id","4");
            j4.put("answer",ans_four);
            data.put(j4);

            JSONObject j5=new JSONObject();
            j5.put("user_id",user_id);
            j5.put("question_id","5");
            j5.put("answer",ans_five);
            data.put(j5);

            JSONObject j6=new JSONObject();
            j6.put("user_id",user_id);
            j6.put("question_id","6");
            j6.put("answer",ans_six);
            data.put(j6);

            JSONObject j7=new JSONObject();
            j7.put("user_id",user_id);
            j7.put("question_id","7");
            j7.put("answer",ans_seven);
            data.put(j7);

            JSONObject j8=new JSONObject();
            j8.put("user_id",user_id);
            j8.put("question_id","8");
            j8.put("answer",ans_eight);
            data.put(j8);

            JSONObject j9=new JSONObject();
            j9.put("user_id",user_id);
            j9.put("question_id","9");
            j9.put("answer",ans_nine);
            data.put(j9);

            JSONObject j10=new JSONObject();
            j10.put("user_id",user_id);
            j10.put("question_id","10");
            j10.put("answer",ans_ten);
            data.put(j10);

            JSONObject j11=new JSONObject();
            j11.put("user_id",user_id);
            j11.put("question_id","10");
            j11.put("answer",et_que_eleven.getText().toString().trim());
            data.put(j11);
            Log.i("frrddd",""+data.toString());
            submitFeedBack(user_id,data);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }//subMitFormClose


    public void submitFeedBack(String user_id,JSONArray array)
    {
        p_bar.setVisibility(View.VISIBLE);
        RetrofitClient retrofitClient = new RetrofitClient();
        RestInterface service = retrofitClient.getAPIClient(WebServiceURLs.DOMAIN_NAME);
        service.feedback(user_id, array.toString(),"Bearer " + PrefManager.getInstance(FeedBackActivity.this).getApiToken(), new Callback<JsonElement>() {
            @Override
            public void success(JsonElement jsonElement, Response response) {

                try {
                    JSONObject jsonObject = new JSONObject(jsonElement.toString());
                    String status=jsonObject.getString("status");

                    if (status.equals("1"))
                    {
                        p_bar.setVisibility(View.GONE);
                        String message=jsonObject.getString("data");

                        PrefManager.getInstance(FeedBackActivity.this).setFeedbackStatus("1");
                        new AlertDialog.Builder(FeedBackActivity.this)
                                .setTitle(getResources().getString(R.string.feedback))
                                .setMessage(message)
                                .setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                     finish();
                                    }
                                })
                                .setCancelable(false)
                                .show();
                    }
                    else
                    {
                        p_bar.setVisibility(View.GONE);
                        Toast.makeText(FeedBackActivity.this, ""+getResources().getString(R.string.unable_to_submit), Toast.LENGTH_SHORT).show();
                    }






                } catch (JSONException | NullPointerException e) {
                    p_bar.setVisibility(View.GONE);
                    e.printStackTrace();

                }
            }

            @Override
            public void failure(RetrofitError error) {
                p_bar.setVisibility(View.GONE);
                Toast.makeText(FeedBackActivity.this ,getResources().getString(R.string.check_internet), Toast.LENGTH_LONG ).show();

            }
        });
    }






    private void initView()
    {

        ll_feed_submitted=findViewById(R.id.ll_feed_submitted);
        ll_feedback=findViewById(R.id.ll_feedback);
        rg_que_one=findViewById(R.id.rg_que_one);
        rg_que_two=findViewById(R.id.rg_que_two);
        rg_que_three=findViewById(R.id.rg_que_three);
        rg_que_four=findViewById(R.id.rg_que_four);
        rg_que_five=findViewById(R.id.rg_que_five);
        rg_que_six=findViewById(R.id.rg_que_six);
        rg_que_seven=findViewById(R.id.rg_que_seven);
        rg_que_eight=findViewById(R.id.rg_que_eight);
        rg_que_nine=findViewById(R.id.rg_que_nine);
        rg_que_ten=findViewById(R.id.rg_que_ten);
        et_que_eleven=findViewById(R.id.et_que_eleven);
        btn_submit=findViewById(R.id.btn_submit);
        if (PrefManager.getInstance(FeedBackActivity.this).getFeedbackStatus().equals("1"))
        {
            ll_feed_submitted.setVisibility(View.VISIBLE);
            ll_feedback.setVisibility(View.GONE);
        }
        else
        {
            ll_feed_submitted.setVisibility(View.GONE);
            ll_feedback.setVisibility(View.VISIBLE);
        }

    }//initViewClose
}
