package mimosale.com.profile;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
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
import android.graphics.Color;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import mimosale.com.BuildConfig;
import mimosale.com.R;
import mimosale.com.helperClass.PrefManager;
import mimosale.com.helperClass.ProgressIndicator;
import mimosale.com.home.HomeActivity;
import mimosale.com.login.LoginActivity;
import mimosale.com.network.RestInterface;
import mimosale.com.network.RetrofitClient;
import mimosale.com.network.WebServiceURLs;
import mimosale.com.preferences.MyPreferencePojo;
import mimosale.com.preferences.MyPreferencesAdapter;
import mimosale.com.signup.RegistrationActivity;
import com.google.gson.JsonElement;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
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

import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.media.MediaRecorder.VideoSource.CAMERA;
import static mimosale.com.helperClass.CustomUtils.showToast;
import static mimosale.com.signup.RegistrationActivity.RequestPermissionCode;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {
    Button btn_cahnge_pass, btn_submit;
    Dialog change_pass_dialog;
    EditText et_confirm_pass, et_new_pass, et_old_pass;
    TextView toolbar_title;
    EditText et_name, et_mobile, et_email, et_last_name;
    ImageView iv_back;
    ProgressBar p_bar;
    ImageView iv_edit;
    TextView tv_done, tv_cancel;
    AppBarLayout app_bar_layout_edit, app_bar_profile;
    EditText[] editTexts;
    Bitmap myBitmap;
    Uri picUri;
    TextView tv_change_profile;
    CircleImageView cv_profile;
    String convertedImage;

    File f;

    private ArrayList permissionsToRequest;
    private ArrayList permissionsRejected = new ArrayList();
    private ArrayList permissions = new ArrayList();

    private final static int ALL_PERMISSIONS_RESULT = 107;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        initView();
        setData();
        getUserData();
        iv_edit.setVisibility(View.VISIBLE);
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.drawable.user);
        requestOptions.fitCenter();
        if (!PrefManager.getInstance(ProfileActivity.this).getProfilePic().equals(""))
          //   Glide.with(ProfileActivity.this).setDefaultRequestOptions(requestOptions).load(WebServiceURLs.IMAGE_URL + PrefManager.getInstance(ProfileActivity.this).getProfilePic()).into(cv_profile);

            Picasso.with(ProfileActivity.this).load(WebServiceURLs.IMAGE_URL + PrefManager.getInstance(ProfileActivity.this).getProfilePic()).into(cv_profile);
       // Picasso.with(mctx).load(WebServiceURLs.SHOP_IMAGE + items.getImage2()).into(holder.iv_product_image2);
cv_profile.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        startActivity(new Intent(ProfileActivity.this,ProfilePictureActivity.class));
    }
});
    }//onCreateClose

    public void setData() {

        toolbar_title.setText(getResources().getString(R.string.profile));
        et_email.setText(PrefManager.getInstance(ProfileActivity.this).getEmail());
        et_mobile.setText(PrefManager.getInstance(ProfileActivity.this).getMobile());
        et_name.setText(PrefManager.getInstance(ProfileActivity.this).getFirstName());
        et_last_name.setText(PrefManager.getInstance(ProfileActivity.this).getLastName());

    }//setDataClose

    public void initView() {
        btn_cahnge_pass = findViewById(R.id.btn_cahnge_pass);
        p_bar = findViewById(R.id.p_bar);
        cv_profile = findViewById(R.id.cv_profile);
        tv_change_profile = findViewById(R.id.tv_change_profile);
        tv_done = findViewById(R.id.tv_done);
        tv_cancel = findViewById(R.id.tv_cancel);
        app_bar_layout_edit = findViewById(R.id.app_bar_layout_edit);
        app_bar_profile = findViewById(R.id.app_bar_profile);
        iv_edit = findViewById(R.id.iv_edit);
        et_name = findViewById(R.id.et_name);
        et_mobile = findViewById(R.id.et_mobile);
        et_email = findViewById(R.id.et_email);
        et_last_name = findViewById(R.id.et_last_name);
        toolbar_title = findViewById(R.id.toolbar_title);
        iv_back = findViewById(R.id.iv_back);
        change_pass_dialog = new Dialog(ProfileActivity.this);
        change_pass_dialog.setContentView(R.layout.dialog_change_password);
        btn_submit = change_pass_dialog.findViewById(R.id.btn_submit);
        et_confirm_pass = change_pass_dialog.findViewById(R.id.et_confirm_pass);
        et_new_pass = change_pass_dialog.findViewById(R.id.et_new_pass);
        et_old_pass = change_pass_dialog.findViewById(R.id.et_old_pass);
        btn_cahnge_pass.setOnClickListener(this);
        btn_submit.setOnClickListener(this);
        iv_back.setOnClickListener(this);
        iv_edit.setOnClickListener(this);
        tv_cancel.setOnClickListener(this);
        tv_done.setOnClickListener(this);
        tv_change_profile.setOnClickListener(this);
        editTexts = new EditText[]{et_email, et_name, et_last_name, et_mobile};
        et_old_pass.setOnKeyListener(new View.OnKeyListener() {

            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    // Perform action on Enter key press
                    et_old_pass.clearFocus();
                    et_new_pass.requestFocus();
                    return true;
                }
                return false;
            }
        });

        et_new_pass.setOnKeyListener(new View.OnKeyListener() {

            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    // Perform action on Enter key press
                    // check for username - password correctness here
                    et_new_pass.clearFocus();
                    et_confirm_pass.requestFocus();
                    return true;
                }
                return false;
            }
        });

        et_confirm_pass.setOnKeyListener(new View.OnKeyListener() {

            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    // Perform action on Enter key press
                    // check for username - password correctness here
                    return true;
                }
                return false;
            }
        });
    }


    public void getUserData() {
        try {

            p_bar.setVisibility(View.VISIBLE);
            RetrofitClient retrofitClient = new RetrofitClient();
            RestInterface service = retrofitClient.getAPIClient(WebServiceURLs.DOMAIN_NAME);
            service.getUserData(PrefManager.getInstance(ProfileActivity.this).getUserId(), "Bearer " + PrefManager.getInstance(ProfileActivity.this).getApiToken(), new Callback<JsonElement>() {
                @Override
                public void success(JsonElement jsonElement, Response response) {

                    try {
                        JSONObject jsonObject = new JSONObject(jsonElement.toString());
                        String status = jsonObject.getString("status");

                        if (status.equals("1")) {
                            JSONObject data = jsonObject.getJSONObject("data");
                            String id = data.getString("id");
                            String first_name = data.getString("first_name");
                            String last_name = data.getString("last_name");
                            String email = data.getString("email");
                            String mobile = data.getString("mobile");
                            String email_verified_at = data.getString("email_verified_at");
                            String profile_image = data.getString("profile_image");
                            String premium_id = data.getString("premium_id");
                            String is_premium = data.getString("is_premium");
                            String is_seller = data.getString("is_seller");
                            String status1 = data.getString("status");
                            String role_id = data.getString("role_id");
                            String email_status = data.getString("email_status");
                            String mobile_status = data.getString("mobile_status");
                            String term_accept = data.getString("term_accept");
                            String firebase_token = data.getString("firebase_token");

                        }
                        p_bar.setVisibility(View.GONE);
                    } catch (JSONException | NullPointerException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    p_bar.setVisibility(View.GONE);
                    Toast.makeText(ProfileActivity.this, getResources().getString(R.string.check_internet), Toast.LENGTH_LONG).show();
                    Log.i("fdfdfdfdfdf", "" + error.getMessage());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();

        }


    }//getUserDataClose


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_cahnge_pass:
                change_pass_dialog.show();
                break;
            case R.id.iv_back:
                finish();
                break;
            case R.id.btn_submit:
                if (et_old_pass.getText().toString().trim().length()==0)
                {
                    et_old_pass.requestFocus();
                    et_old_pass.setError(getResources().getString(R.string.enter_old_pass));
                    return;

                }
                if (et_new_pass.getText().toString().trim().length()!=0)
                {
                    if (!et_new_pass.getText().toString().trim().equals(et_confirm_pass.getText().toString().trim()))
                    {
                        et_confirm_pass.setError(getResources().getString(R.string.pass_not_matched));
                        return;
                    }

                }
                else
                {
                    et_new_pass.requestFocus();
                    et_new_pass.setError(getResources().getString(R.string.enter_new_pass));
                    return;
                }
                changePass(change_pass_dialog);
                break;
            case R.id.iv_edit:
                for (int i = 0; i < editTexts.length; i++) {
                    editTexts[i].setEnabled(true);
                }
                app_bar_layout_edit.setVisibility(View.VISIBLE);
                app_bar_profile.setVisibility(View.GONE);
                btn_cahnge_pass.setVisibility(View.GONE);

                break;
            case R.id.tv_cancel:
                for (int i = 0; i < editTexts.length; i++) {
                    editTexts[i].setEnabled(false);
                }
                app_bar_layout_edit.setVisibility(View.GONE);
                app_bar_profile.setVisibility(View.VISIBLE);
                btn_cahnge_pass.setVisibility(View.VISIBLE);

                break;
            case R.id.tv_done:
                submitForm();

                break;
            case R.id.tv_change_profile:

                if (checkPermission()) {
                    final CharSequence[] options = {getResources().getString(R.string.take_photo), getResources().getString(R.string.choose_from_gallery), getResources().getString(R.string.cancel)};
                    AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
                    builder.setTitle(getResources().getString(R.string.add_profile_pic_title));
                    builder.setItems(options, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int item) {
                            if (options[item].equals(getResources().getString(R.string.take_photo))) {
                                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                File f = new File(android.os.Environment.getExternalStorageDirectory(), "temp.jpg");
                                intent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(ProfileActivity.this, BuildConfig.APPLICATION_ID + ".provider", f));
                                startActivityForResult(intent, 1);
                            } else if (options[item].equals(getResources().getString(R.string.choose_from_gallery))) {
                                Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                startActivityForResult(intent, 2);
                            } else if (options[item].equals("Cancel")) {
                                dialog.dismiss();
                            }
                        }
                    });
                    builder.show();
                } else {
                    requestPermission();
                }

                break;
        }

    }

    public void changePass(final Dialog dialog) {
        try {

            p_bar.setVisibility(View.VISIBLE);
            RetrofitClient retrofitClient = new RetrofitClient();
            RestInterface service = retrofitClient.getAPIClient(WebServiceURLs.DOMAIN_NAME);
            service.changePass(PrefManager.getInstance(ProfileActivity.this).getUserId(), et_old_pass.getText().toString().trim(), et_new_pass.getText().toString().trim(), "Bearer " + PrefManager.getInstance(ProfileActivity.this).getApiToken(), new Callback<JsonElement>() {
                @Override
                public void success(JsonElement jsonElement, Response response) {

                    try {
                        JSONObject jsonObject = new JSONObject(jsonElement.toString());
                        String status = jsonObject.getString("status");
                        String message = jsonObject.getString("message");
                        if (status.equals("1")) {

                            Toast.makeText(ProfileActivity.this, ""+message, Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                        else
                        {
                            Toast.makeText(ProfileActivity.this, ""+message, Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                        p_bar.setVisibility(View.GONE);
                    } catch (JSONException | NullPointerException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    p_bar.setVisibility(View.GONE);
                    Toast.makeText(ProfileActivity.this, getResources().getString(R.string.check_internet), Toast.LENGTH_LONG).show();
                    Log.i("fdfdfdfdfdf", "" + error.getMessage());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();

        }

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
                    updateUserProfile();
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
                String[] filePath = {MediaStore.Images.Media.DATA};
                Cursor c = getContentResolver().query(selectedImage, filePath, null, null, null);
                assert c != null;
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePath[0]);
                String picturePath = c.getString(columnIndex);
                f = new File(picturePath);
                c.close();
                Bitmap thumbnail = (BitmapFactory.decodeFile(picturePath));
                convertedImage = convertImgPathToBase64(picturePath);
                SharedPreferences sharedPreferences = getSharedPreferences("mydata", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("convertedImage", convertedImage);
                editor.apply();
                cv_profile.setImageBitmap(thumbnail);
                updateUserProfile();
            }
        }
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(ProfileActivity.this, new
                String[]{WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, RequestPermissionCode);
//                String[]{READ_PHONE_STATE}, RequestPermissionCode);
    }

    public boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(ProfileActivity.this,
                WRITE_EXTERNAL_STORAGE);
        int result1 = ContextCompat.checkSelfPermission(ProfileActivity.this,
                Manifest.permission.CAMERA);
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
                    Toast.makeText(ProfileActivity.this, "Permission Canceled, Now your application cannot access CAMERA.", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    public String convertImgPathToBase64(String filePath) {
        Bitmap bmp = null;
        ByteArrayOutputStream bos = null;
        byte[] bt = null;
        String encodeString = null;
        try {
            bmp = BitmapFactory.decodeFile(filePath);
            bos = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.JPEG, 50, bos);
            bt = bos.toByteArray();
            encodeString = Base64.encodeToString(bt, Base64.DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return encodeString;
    }

    private String convertBitmapToBase64(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 50, baos);
        byte[] b = baos.toByteArray();
        String encImage = Base64.encodeToString(b, Base64.DEFAULT);

        return encImage;
    }

    public void submitForm() {
        if (et_name.getText().toString().trim().length() == 0) {
            et_name.requestFocus();
            et_name.setError(getResources().getString(R.string.enter_first_name));
            return;
        }
        if (et_last_name.getText().toString().trim().length() == 0) {
            et_last_name.requestFocus();
            et_last_name.setError(getResources().getString(R.string.enter_last_name));
            return;
        }
        if (!isValidMail(et_email.getText().toString().trim(), et_email)) {

            if (et_email.getText().toString().trim().length() == 0) {
                et_email.requestFocus();
                et_email.setError(getResources().getString(R.string.enter_email_id));

            } else {
                et_email.requestFocus();
                et_email.setError(getResources().getString(R.string.email_error));

            }

            return;
        }
        if (!isValidMobile(et_mobile.getText().toString().trim())) {
            et_mobile.requestFocus();
            if (et_mobile.getText().toString().trim().length() == 0) {
                et_mobile.setError(getResources().getString(R.string.enter_mobile_no));
            } else {
                et_mobile.setError(getResources().getString(R.string.mobile_error));
            }

            return;
        } else {
            et_mobile.setError(null);
        }
        updateUserProfile();
    }//subMitFormClose

    private boolean isValidMobile(String phone) {
        boolean check = false;
        if (!Pattern.matches("[a-zA-Z]+", phone)) {
            if (phone.length() < 8 || phone.length() > 15) {
                // if(phone.length() != 10) {
                check = false;
                et_mobile.setError("Not Valid Number");
            } else {
                check = true;
            }
        } else {
            check = false;
        }
        return check;
    }

    private boolean isValidMail(String email, EditText et_email) {
        boolean check;
        Pattern p;
        Matcher m;

        String EMAIL_STRING = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        p = Pattern.compile(EMAIL_STRING);

        m = p.matcher(email);
        check = m.matches();

        if (!check) {
            et_email.requestFocus();
            et_email.setError(getResources().getString(R.string.email_error));
        }
        return check;
    }

    public void updateUserProfile() {


        p_bar.setVisibility(View.VISIBLE);
        MultipartTypedOutput multipartTypedOutput = new MultipartTypedOutput();
        multipartTypedOutput.addPart("email", new TypedString(et_email.getText().toString().trim()));
        multipartTypedOutput.addPart("mobile", new TypedString(et_mobile.getText().toString().trim()));
        multipartTypedOutput.addPart("first_name", new TypedString(et_name.getText().toString().trim()));
        multipartTypedOutput.addPart("last_name", new TypedString(et_last_name.getText().toString().trim()));
        multipartTypedOutput.addPart("user_id", new TypedString(PrefManager.getInstance(ProfileActivity.this).getUserId()));


        if (f != null) {
            multipartTypedOutput.addPart("profile_image", new TypedFile("application/octet-stream", f));
        } else {
            multipartTypedOutput.addPart("profile_image", new TypedString(""));
        }

        //loop through object to get the path of the videos that has picked by user


        RetrofitClient retrofitClient = new RetrofitClient();
        final RestInterface service = retrofitClient.getAPIClient(WebServiceURLs.DOMAIN_NAME);
        service.update_profile(multipartTypedOutput, "Bearer  " + PrefManager.getInstance(ProfileActivity.this).getApiToken(), new Callback<JsonElement>() {
            @Override
            public void success(JsonElement jsonElement, Response response) {

                try {
                    p_bar.setVisibility(View.GONE);

                    JSONObject jsonObject = new JSONObject(jsonElement.toString());
                    String status = jsonObject.getString("status");
                    String message = jsonObject.getString("message");

                    if (status.equals("1")) {

                        JSONObject data = jsonObject.getJSONObject("data");
                        String profile_image = data.getString("profile_image");

                        Toast.makeText(ProfileActivity.this, "" + message, Toast.LENGTH_SHORT).show();
                        for (int i = 0; i < editTexts.length; i++) {
                            editTexts[i].setEnabled(false);
                        }
                        app_bar_layout_edit.setVisibility(View.GONE);
                        app_bar_profile.setVisibility(View.VISIBLE);
                        btn_cahnge_pass.setVisibility(View.VISIBLE);

                        PrefManager.getInstance(ProfileActivity.this).setFirstName(et_name.getText().toString().trim());
                        PrefManager.getInstance(ProfileActivity.this).setLastName(et_last_name.getText().toString().trim());
                        PrefManager.getInstance(ProfileActivity.this).setEmail(et_email.getText().toString().trim());
                        PrefManager.getInstance(ProfileActivity.this).setMobile(et_mobile.getText().toString().trim());
                        PrefManager.getInstance(ProfileActivity.this).setProfilePic(profile_image);


                    } else if (status.equals("0")) {

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

    }//updateUserProfileClose

    public void changePassword() {
        p_bar.setVisibility(View.VISIBLE);


        //loop through object to get the path of the videos that has picked by user


    }


}
