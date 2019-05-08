package mimosale.com.signup;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import mimosale.com.BuildConfig;
import mimosale.com.R;
import mimosale.com.feedback.FeedBackActivity;
import mimosale.com.helperClass.PrefManager;
import mimosale.com.home.HomeActivity;
import mimosale.com.login.LoginActivity;
import mimosale.com.network.RestInterface;
import mimosale.com.network.RetrofitClient;
import mimosale.com.network.WebServiceURLs;
import com.google.gson.JsonElement;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.MultipartTypedOutput;
import retrofit.mime.TypedFile;
import retrofit.mime.TypedString;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static mimosale.com.helperClass.CustomUtils.showToast;


public class RegistrationActivity extends AppCompatActivity {
    Toolbar toolbar;
    TextView toolbar_title;
    ImageView iv_back;
    ProgressBar p_bar;
    EditText et_first_name,et_last_name,et_mobile,et_password,et_cpassword,et_email;
    CircleImageView cv_profile;
    Button btn_register;
    ImageView iv_pick_image;
    public static final int RequestPermissionCode = 1;
    TextInputLayout tl_f_name,tl_c_pass,tl_pass,tl_email,tl_mobile_no,tl_last_name;
    String convertedImage;
    Bitmap myBitmap;
    Uri picUri;
    File f;
    String intent_from="";
    private ArrayList<String> permissionsToRequest;
    private ArrayList<String> permissionsRejected = new ArrayList<>();
    private ArrayList<String> permissions = new ArrayList<>();

    private final static int ALL_PERMISSIONS_RESULT = 107;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        toolbar=findViewById(R.id.toolbar);
        iv_pick_image=findViewById(R.id.iv_pick_image);
        toolbar_title=findViewById(R.id.toolbar_title);
        iv_back=findViewById(R.id.iv_back);
        p_bar=findViewById(R.id.p_bar);
        toolbar_title.setText(getResources().getString(R.string.register));
        toolbar_title.setAllCaps(true);

        initView();
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitForm();
            }
        });
        iv_pick_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkPermission()){
                    final CharSequence[] options = { getResources().getString(R.string.take_photo), getResources().getString(R.string.choose_from_gallery),getResources().getString(R.string.cancel) };
                    AlertDialog.Builder builder = new AlertDialog.Builder(RegistrationActivity.this);
                    builder.setTitle(getResources().getString(R.string.add_profile_pic_title));
                    builder.setItems(options, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int item) {
                            if (options[item].equals(getResources().getString(R.string.take_photo)))
                            {
                                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                File f = new File(android.os.Environment.getExternalStorageDirectory(), "temp.jpg");
                                intent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(RegistrationActivity.this, BuildConfig.APPLICATION_ID + ".provider",f));
                                startActivityForResult(intent, 1);
                            }
                            else if (options[item].equals(getResources().getString(R.string.choose_from_gallery)))
                            {
                                Intent intent = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                startActivityForResult(intent, 2);
                            }
                            else if (options[item].equals("Cancel")) {
                                dialog.dismiss();
                            }
                        }
                    });
                    builder.show();
                }
                else {
                    requestPermission();
                }
            }
        });



    }


    public void submitForm()
    {
        //  SignUp();

            if (et_first_name.getText().toString().trim().length()==0)
            {
                et_first_name.requestFocus();
                tl_f_name.setError(getResources().getString(R.string.enter_first_name));
            }


            et_last_name.requestFocus();
            if (et_last_name.getText().toString().trim().length()==0)
            {

                tl_last_name.setError(getResources().getString(R.string.enter_last_name));
            }



        if (!isValidMobile(et_mobile.getText().toString().trim()))
        {
            et_mobile.requestFocus();
            if (et_mobile.getText().toString().trim().length()==0)
            {
                tl_mobile_no.setError(getResources().getString(R.string.enter_mobile_no));
            }
            else
            {
                tl_mobile_no.setError(getResources().getString(R.string.mobile_error));
            }

            return;
        }
        else
        {
            tl_mobile_no.setError(null);
        }
        if (!isValidMail(et_email.getText().toString().trim()))
        {
            et_email.requestFocus();
            if (et_email.getText().toString().trim().length()==0)
            {
                tl_email.setError(getResources().getString(R.string.enter_email_id));
            }
            else
            {
                tl_email.setError(getResources().getString(R.string.email_error));
            }

            return;
        }
        else
        {
            tl_email.setError(null);
        }


        if (!validatePassword(et_password.getText().toString().trim()))
        {
            et_password.requestFocus();
            tl_pass.setError(getResources().getString(R.string.password_error));
            return;
        }
        else
        {
            tl_pass.setError(null);
        }
        if (!validateConfirmPassword(et_cpassword.getText().toString().trim()))
        {
            et_cpassword.requestFocus();
            if (et_cpassword.getText().toString().trim().length()==0)
            {
                tl_c_pass.setError(getResources().getString(R.string.enter_confirm_pass));
            }
            else
                tl_c_pass.setError(getResources().getString(R.string.c_password_error));
            return;
        }
        else
        {
            tl_c_pass.setError(null);
        }
        RegisterUser();
    }//submitFormClose


    public void RegisterUser()
    {
        p_bar.setVisibility(View.VISIBLE);
        MultipartTypedOutput multipartTypedOutput = new MultipartTypedOutput();
        multipartTypedOutput.addPart("email", new TypedString(et_email.getText().toString().trim()));
        multipartTypedOutput.addPart("password", new TypedString(et_password.getText().toString().trim()));
        multipartTypedOutput.addPart("first_name", new TypedString(et_first_name.getText().toString().trim()));
        multipartTypedOutput.addPart("last_name", new TypedString(et_last_name.getText().toString().trim()));
        multipartTypedOutput.addPart("mobile", new TypedString(et_mobile.getText().toString().trim()));



        if (f!=null)
        {
            multipartTypedOutput.addPart("profile_image", new TypedFile("application/octet-stream", f));
        }
        else
        {
            multipartTypedOutput.addPart("profile_image", new TypedString(""));
        }

        //loop through object to get the path of the videos that has picked by user



        RetrofitClient retrofitClient = new RetrofitClient();
        final RestInterface service = retrofitClient.getAPIClient(WebServiceURLs.DOMAIN_NAME);
        service.register(multipartTypedOutput, new Callback<JsonElement>() {
            @Override
            public void success(JsonElement jsonElement, Response response) {

                try {
                    p_bar.setVisibility(View.GONE);

                    JSONObject jsonObject=new JSONObject(jsonElement.toString());
                    String status=jsonObject.getString("status");
                    String message=jsonObject.getString("message");

                    if (status.equals("1"))
                    {
                        Toast.makeText(RegistrationActivity.this, message, Toast.LENGTH_LONG).show();

                        JSONObject data=jsonObject.getJSONObject("data");
                        String email=data.getString("email");
                        String first_name=data.getString("first_name");
                        String last_name=data.getString("email");
                        String mobile=data.getString("mobile");
                        String is_premium=data.getString("is_premium");
                        String is_seller=data.getString("is_seller");
                        String status1=data.getString("status");
                        String role_id=data.getString("role_id");
                        String email_status=data.getString("email_status");
                        String mobile_status=data.getString("mobile_status");
                        String term_accept=data.getString("term_accept");
                        String username=data.getString("username");
                        String id=data.getString("id");
                        String profile_image=data.getString("profile_image");
                        String token= jsonObject.getString("token");
                        PrefManager.getInstance(RegistrationActivity.this);
                        PrefManager.setEmail(email);
                        PrefManager.setApiToken(token);
                        PrefManager.setFirstName(first_name);
                        PrefManager.setLastName(last_name);
                        PrefManager.setUserId(id);
                        PrefManager.setMobile(mobile);
                        PrefManager.setProfilePic(profile_image);
                        PrefManager.setIsLogin(true);
                        PrefManager.getInstance(RegistrationActivity.this).setFeedbackStatus("0");
                        startActivity(new Intent(RegistrationActivity.this,HomeActivity.class));
                        finish();

                       /* Intent intent = new Intent();
                        intent.putExtra("intent_from",  intent_from);
                        setResult(RESULT_OK, intent);
                        finish();*/
                    }
                    else if (status.equals("0"))
                    {
                        Toast.makeText(RegistrationActivity.this, ""+getResources().getString(R.string.user_already_registered), Toast.LENGTH_SHORT).show();
                    }






                } catch (Exception je) {
                    p_bar.setVisibility(View.GONE);
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                    showToast(getString(R.string.failed_create_event), getApplicationContext());
                    je.printStackTrace();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                p_bar.setVisibility(View.GONE);
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                error.printStackTrace();
            }
        });


    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                 f = new File(Environment.getExternalStorageDirectory().toString());
                for (File temp : f.listFiles()) {
                    if (temp.getName().equals("temp.jpg")) {
                        f = temp;

                        break;
                    }
                }
                try {
                    Bitmap bitmap;
                    BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();

                    bitmap = BitmapFactory.decodeFile(f.getAbsolutePath(), bitmapOptions);
                    cv_profile.setImageBitmap(bitmap);

                    convertedImage = convertBitmapToBase64(bitmap);

                    SharedPreferences sharedPreferences = getSharedPreferences("mydata", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("convertedImage", convertedImage);
                    editor.apply();

                    String path = android.os.Environment.getExternalStorageDirectory() + File.separator + "Phoenix" + File.separator + "default";
                    f.delete();
                    OutputStream outFile = null;
                    File file = new File(path, String.valueOf(System.currentTimeMillis()) + ".jpg");
                    try {
                        outFile = new FileOutputStream(file);
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 85, outFile);
                        outFile.flush();
                        outFile.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (requestCode == 2) {

                Uri selectedImage = data.getData();
                String[] filePath = { MediaStore.Images.Media.DATA};
                Cursor c = getContentResolver().query(selectedImage,filePath, null, null, null);
                assert c != null;
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePath[0]);
                String picturePath = c.getString(columnIndex);
                f=new File(picturePath);
                c.close();
                Bitmap thumbnail = (BitmapFactory.decodeFile(picturePath));
                convertedImage = convertImgPathToBase64(picturePath);
                SharedPreferences sharedPreferences = getSharedPreferences("mydata", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("convertedImage", convertedImage);
                editor.apply();
                cv_profile.setImageBitmap(thumbnail);
            }
        }
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(RegistrationActivity.this, new
                String[]{WRITE_EXTERNAL_STORAGE, CAMERA}, RequestPermissionCode);
//                String[]{READ_PHONE_STATE}, RequestPermissionCode);
    }

    public boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(RegistrationActivity.this,
                WRITE_EXTERNAL_STORAGE);
        int result1 = ContextCompat.checkSelfPermission(RegistrationActivity.this,
                CAMERA);
        return result == PackageManager.PERMISSION_GRANTED &&
                result1 == PackageManager.PERMISSION_GRANTED;
//        return result == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onRequestPermissionsResult(int RC, @NonNull String per[], @NonNull int[] PResult) {
        switch (RC) {
            case RequestPermissionCode:
                if (PResult.length > 0 && PResult[0] == PackageManager.PERMISSION_GRANTED) {
//                    Toast.makeText(DetailPage.this,"Permission Granted, Now your application can access CAMERA.", Toast.LENGTH_LONG).show();
                    Log.e("Permission Granted", "Permission Granted, Now your application can access CAMERA.");
                } else {
                    Toast.makeText(RegistrationActivity.this, "Permission Canceled, Now your application cannot access CAMERA.", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    public String convertImgPathToBase64(String filePath){
        Bitmap bmp = null;
        ByteArrayOutputStream bos = null;
        byte[] bt = null;
        String encodeString = null;
        try{
            bmp = BitmapFactory.decodeFile(filePath);
            bos = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.JPEG, 50, bos);
            bt = bos.toByteArray();
            encodeString = Base64.encodeToString(bt, Base64.DEFAULT);
        }catch (Exception e){
            e.printStackTrace();
        }
        return encodeString;
    }

    private String convertBitmapToBase64(Bitmap bm)
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG,50,baos);
        byte[] b = baos.toByteArray();
        String encImage = Base64.encodeToString(b, Base64.DEFAULT);

        return encImage;
    }

    public void initView()
    {
        try{
            intent_from=getIntent().getStringExtra("intent_from");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        cv_profile=findViewById(R.id.cv_profile);
        et_first_name=findViewById(R.id.et_first_name);
        tl_f_name=findViewById(R.id.tl_f_name);
        tl_c_pass=findViewById(R.id.tl_c_pass);
        tl_pass=findViewById(R.id.tl_pass);
        tl_last_name=findViewById(R.id.tl_last_name);
        tl_mobile_no=findViewById(R.id.tl_mobile_no);
        tl_email=findViewById(R.id.tl_email);



        et_last_name=findViewById(R.id.et_last_name);
        et_mobile=findViewById(R.id.et_mobile);
        et_password=findViewById(R.id.et_password);
        et_cpassword=findViewById(R.id.et_cpassword);
        et_email=findViewById(R.id.et_email);
        btn_register=findViewById(R.id.btn_register);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);







    }//initViewClose


    private boolean isValidMail(String email) {
        boolean check;
        Pattern p;
        Matcher m;

        String EMAIL_STRING = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        p = Pattern.compile(EMAIL_STRING);

        m = p.matcher(email);
        check = m.matches();

        if(!check) {
            et_mobile.setError("Not Valid Email");
        }
        return check;
    }
    private boolean isValidMobile(String phone) {
        boolean check=false;
        if(!Pattern.matches("[a-zA-Z]+", phone)) {
            if(phone.length() < 8 || phone.length() > 15) {
                // if(phone.length() != 10) {
                check = false;
                et_mobile.setError("Not Valid Number");
            } else {
                check = true;
            }
        } else {
            check=false;
        }
        return check;
    }
    public  boolean isValidateFirstName( String firstName )
    {
        if (firstName.trim().equals(""))
            return  false;
        else
            return firstName.matches("[a-zA-Z]*");

    } // end method validateFirstName

    // validate last name
    public  boolean isValidateLastName( String lastName )
    {
        if (lastName.trim().equals(""))
            return  false;
        else
            return lastName.matches("[a-zA-Z]*");
    }

    public boolean validatePassword( String password)
    {
       if (password.length()>0)
       return true;
       else
           return false;
    }

    public boolean validateConfirmPassword( String Cpassword)
    {
        if (Cpassword.length()>0 && Cpassword.equals(et_password.getText().toString()))
        {
            return true;
        }
        else
            return false;
    }

}
