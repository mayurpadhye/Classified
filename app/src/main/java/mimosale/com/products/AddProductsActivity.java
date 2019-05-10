package mimosale.com.products;

import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import mimosale.com.R;
import mimosale.com.helperClass.CustomFileUtils;
import mimosale.com.helperClass.CustomPermissions;
import mimosale.com.helperClass.CustomUtils;
import mimosale.com.helperClass.PrefManager;
import mimosale.com.my_posting.product_posting.PostingImagesAdapter;
import mimosale.com.network.RestInterface;
import mimosale.com.network.RetrofitClient;
import mimosale.com.network.WebServiceURLs;
import mimosale.com.shop.ImageVideoData;

import com.google.gson.JsonElement;
import com.iceteck.silicompressorr.SiliCompressor;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.MultipartTypedOutput;
import retrofit.mime.TypedFile;
import retrofit.mime.TypedString;

import static mimosale.com.helperClass.CustomUtils.IMAGE_LIMIT;

public class AddProductsActivity extends AppCompatActivity implements View.OnClickListener {
    TextView toolbar_title;
    ImageView iv_back;
    List<String> shopnameList = new ArrayList<>();
    List<String> shopId = new ArrayList<>();
    Spinner sp_select_shop;
    ArrayList<ImageVideoData> image_thumbnails;
    Button btn_upload;
    ProgressDialog progressDialog;
    PostingImagesAdapter adapter;
    public ArrayList<File> imageFiles;
    RecyclerView rv_images;
    LinearLayoutManager llm_images;
    ProgressBar p_bar;
    String shop_id = "";

    Button btn_submit;
    Intent i;
    String isUpdate="";
    TextInputLayout tl_hash_tag, tl_product_name, tl_desc, tl_price, tl_discount;
    EditText et_product_name, et_desc, et_price, et_discount, et_hash_tag;
    String product_id="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_products);
        i=getIntent();
        initView();


        getUserShop();

        btn_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomUtils.hideKeyboard(v, getApplicationContext());
                if (imageFiles.size() >= IMAGE_LIMIT) {
                    CustomUtils.showAlertDialog(AddProductsActivity.this, getString(R.string.can_not_share_more_than_five_images));
                } else {
                    selectImage();
                }

            }
        });

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProductValidte();
            }
        });

        sp_select_shop.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (!sp_select_shop.getSelectedItem().equals(getResources().getString(R.string.select_shop))) {
                    shop_id = shopId.get(position);
                } else {
                    shop_id = "";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

                // sometimes you need nothing here
            }
        });
    }

    private void initView() {
        btn_submit = findViewById(R.id.btn_submit);
        tl_hash_tag = findViewById(R.id.tl_hash_tag);
        tl_product_name = findViewById(R.id.tl_product_name);
        tl_desc = findViewById(R.id.tl_desc);
        tl_price = findViewById(R.id.tl_price);
        tl_discount = findViewById(R.id.tl_discount);
        p_bar = findViewById(R.id.p_bar);
        et_product_name = findViewById(R.id.et_product_name);
        et_desc = findViewById(R.id.et_desc);
        et_discount = findViewById(R.id.et_discount);
        et_price = findViewById(R.id.et_price);
        et_hash_tag = findViewById(R.id.et_hash_tag);

        imageFiles = new ArrayList<>();
        rv_images = findViewById(R.id.rv_images);
        llm_images = new LinearLayoutManager(getApplicationContext());
        llm_images.setOrientation(LinearLayoutManager.HORIZONTAL);
        image_thumbnails = new ArrayList<ImageVideoData>();
        adapter = new PostingImagesAdapter(AddProductsActivity.this, AddProductsActivity.this, image_thumbnails, "create");
        toolbar_title = findViewById(R.id.toolbar_title);
        toolbar_title.setText(getResources().getString(R.string.add_products));
        iv_back = findViewById(R.id.iv_back);
        iv_back.setOnClickListener(this);
        sp_select_shop = findViewById(R.id.sp_select_shop);
        btn_upload = findViewById(R.id.btn_upload);
        toolbar_title.setText(""+getResources().getString(R.string.product_posting));
        toolbar_title.setTextColor(getResources().getColor(R.color.black));

         isUpdate=i.getStringExtra("isUpdate");

        if (isUpdate.equals("true"))
        {
            String shop_id=i.getStringExtra("shop_name");
            String product_name=i.getStringExtra("product_name");
            String desc=i.getStringExtra("desc");
            String price=i.getStringExtra("price");
            String discount=i.getStringExtra("discount");
            String hash_tag=i.getStringExtra("hash_tag");
             product_id=i.getStringExtra("product_id");
             et_product_name.setText(product_name);
            et_desc.setText(desc);
            et_price.setText(price);
            et_discount.setText(discount);
            et_hash_tag.setText(hash_tag);
        }






    }

    private void selectImage() {
        final String[] items = new String[]{
                getString(R.string.camera),
                getString(R.string.gallery)
        };

        AlertDialog.Builder ad = new AlertDialog.Builder(this);
        ad.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                switch (i) {
                    case 0:
                        boolean result_camera = CustomPermissions.checkCameraPermission(AddProductsActivity.this);
                        if (result_camera) {
                            cameraImageIntent();
                        }
                        break;

                    case 1:
                        boolean result_gallery = CustomPermissions.checkPermissionForFileAccess(AddProductsActivity.this);
                        if (result_gallery) {
                            galleryImageIntent();
                        }
                        break;
                }
            }
        });
        ad.show();
    }

    private void galleryImageIntent() {
        Intent intent = new Intent(
                Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, getString(R.string.select)), CustomPermissions.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
    }

    private void cameraImageIntent() {

        Intent i = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            PackageManager pm = getPackageManager();

            final ResolveInfo mInfo = pm.resolveActivity(i, 0);

            Intent intent = new Intent();
            intent.setComponent(new ComponentName(mInfo.activityInfo.packageName, mInfo.activityInfo.name));
            intent.setAction(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            startActivityForResult(i, CustomPermissions.MY_PERMISSIONS_REQUEST_CAMERA);
        } catch (Exception e) {
            Log.i("launch_camera", "Unable to launch camera: " + e);
        }


    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onActivityResult(int requestCode, int resultCode, final Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case CustomPermissions.MY_PERMISSIONS_REQUEST_CAMERA:
                    Bitmap bitmap_thumbnail = (Bitmap) data.getExtras().get("data");
                    bitmap_thumbnail.getByteCount();
                    Uri tempUri = CustomUtils.getImageUri(getApplicationContext(), bitmap_thumbnail);
                    File finalFile = new File(CustomUtils.getRealPathFromURI(getApplicationContext(), tempUri));
                    CustomUtils.showLog("Camera File path ", finalFile.getAbsolutePath() + "");
                    if (imageFiles.size() <= CustomUtils.IMAGE_LIMIT) {
                        ArrayList<String> selected_image = new ArrayList<>();
                        selected_image.add(tempUri.toString());
                        new ImageCompressAsyncTask(this).execute(selected_image);
                    } else {
                        CustomUtils.showAlertDialog(AddProductsActivity.this, getString(R.string.can_not_share_more_than_five_images));
                    }
                    break;

                case CustomPermissions.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                    if (data.getClipData() != null) {
                        int count = data.getClipData().getItemCount();
                        int imageCount = imageFiles.size();
                        int selectedImages = imageCount + count;
                        if (selectedImages > IMAGE_LIMIT) {
                            CustomUtils.showAlertDialog(AddProductsActivity.this, getString(R.string.can_not_share_more_than_five_images));
                            return;
                        } else {
                            ArrayList<String> images = new ArrayList<>();

                            for (int i = 0; i < count; i++) {
                                images.add(data.getClipData().getItemAt(i).getUri().toString());
                                //  CustomUtil.showLog("Image selected ", data.getClipData().getItemAt(i).getUri().toString());

                                String picturePath = CustomFileUtils.getPath(getApplicationContext(), data.getClipData().getItemAt(i).getUri());
                                File file = new File(picturePath);
                                imageFiles.add(file);
                                long lengthInMB = lengthInMB = (file.length() / 1024) / 1024;
                                ArrayList<String> image = new ArrayList<>();
                                image.add(data.getClipData().getItemAt(i).getUri().toString());

                                if (lengthInMB >= 20) {
                                    new ImageCompressAsyncTask(this).execute(image);
                                } else {

                                    Bitmap bitmap = BitmapFactory.decodeFile(picturePath);
                                    ImageVideoData image_v = new ImageVideoData();
                                  /* image_v.setBitmap(bitmap);
                                    image_v.setPath(picturePath);
                                    image_thumbnails.add(image_v);
*/

                                    ExifInterface ei = null;
                                    try {
                                        ei = new ExifInterface(picturePath);
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                                            ExifInterface.ORIENTATION_UNDEFINED);

                                    Bitmap rotatedBitmap = null;
                                    switch (orientation) {

                                        case ExifInterface.ORIENTATION_ROTATE_90:
                                            rotatedBitmap = rotateImage(bitmap, 90);
                                            break;

                                        case ExifInterface.ORIENTATION_ROTATE_180:
                                            rotatedBitmap = rotateImage(bitmap, 180);
                                            break;

                                        case ExifInterface.ORIENTATION_ROTATE_270:
                                            rotatedBitmap = rotateImage(bitmap, 270);
                                            break;

                                        case ExifInterface.ORIENTATION_NORMAL:
                                        default:
                                            rotatedBitmap = bitmap;
                                    }
                                    //File file1 = new File(String.valueOf(data.getData()));

                                    image_v.setBitmap(rotatedBitmap);
                                    image_v.setPath(picturePath);
                                    image_thumbnails.add(image_v);

                                       /* adapter = new EventImagesAdapter(getApplicationContext(), CreateEventActivity.this, image_thumbnails);
                                        rv_event_image.setLayoutManager(llm_images);
                                        rv_event_image.setAdapter(adapter);*/
                                }


                            }

                            //    new ImageCompressAsyncTask(CreateEventActivity.this).execute(images);
                            if (imageFiles.size() <= IMAGE_LIMIT) {
                                adapter = new PostingImagesAdapter(AddProductsActivity.this, AddProductsActivity.this, image_thumbnails, "create");
                                rv_images.setLayoutManager(llm_images);
                                rv_images.setAdapter(adapter);
                            } else {
                                //showToast(getString(R.string.can_not_share_more_than_five_images), CreateEventActivity.this);
                                if (imageCount <= imageFiles.size()) {
                                    for (int i = imageFiles.size(); i > imageCount; i--) {
                                        image_thumbnails.remove(i - 1);
                                        imageFiles.remove(i - 1);
                                    }

                                }
                                CustomUtils.showAlertDialog(AddProductsActivity.this, getString(R.string.can_not_share_more_than_five_images));
                            }
                        }
                    } else if (data.getData() != null) {
                        Uri selectedImage = data.getData();
                        String picturePath = CustomFileUtils.getPath(getApplicationContext(), selectedImage);
                        File file = new File(picturePath);
                        imageFiles.add(file);
                        if (imageFiles.size() <= IMAGE_LIMIT) {
                            long lengthInMB = (file.length() / 1024) / 1024;
                            ArrayList<String> image = new ArrayList<>();
                            image.add(selectedImage.toString());
                            if (lengthInMB >= 20) {
                                new ImageCompressAsyncTask(this).execute(image);
                            } else {

                                Bitmap bitmap = BitmapFactory.decodeFile(picturePath);
                                ImageVideoData image_v = new ImageVideoData();
                             /* image_v.setBitmap(bitmap);
                                image_v.setPath(picturePath);
                                image_thumbnails.add(image_v);*/

                                ExifInterface ei = null;
                                try {
                                    ei = new ExifInterface(picturePath);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                                        ExifInterface.ORIENTATION_UNDEFINED);

                                Bitmap rotatedBitmap = null;
                                switch (orientation) {

                                    case ExifInterface.ORIENTATION_ROTATE_90:
                                        rotatedBitmap = rotateImage(bitmap, 90);
                                        break;

                                    case ExifInterface.ORIENTATION_ROTATE_180:
                                        rotatedBitmap = rotateImage(bitmap, 180);
                                        break;

                                    case ExifInterface.ORIENTATION_ROTATE_270:
                                        rotatedBitmap = rotateImage(bitmap, 270);
                                        break;

                                    case ExifInterface.ORIENTATION_NORMAL:
                                    default:
                                        rotatedBitmap = bitmap;
                                }
                                //File file1 = new File(String.valueOf(data.getData()));

                                image_v.setBitmap(rotatedBitmap);
                                image_v.setPath(picturePath);
                                image_thumbnails.add(image_v);
                                adapter = new PostingImagesAdapter(AddProductsActivity.this, AddProductsActivity.this, image_thumbnails, "create");
                                rv_images.setLayoutManager(llm_images);
                                rv_images.setAdapter(adapter);
                            }
                        } else {
                            CustomUtils.showAlertDialog(AddProductsActivity.this, getString(R.string.can_not_share_more_than_five_images));
                        }
                    }
                    break;


            }
        }

    }

    public class ImageCompressAsyncTask extends AsyncTask<List<String>, String, String> {
        Context mContext;
        Uri selectedImage;

        public ImageCompressAsyncTask(Context context) {
            mContext = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(AddProductsActivity.this);
            progressDialog.setTitle(getString(R.string.app_name));
            progressDialog.setMessage(getString(R.string.please_wait));
            progressDialog.setCanceledOnTouchOutside(false);
            //  progressDialog.show();
        }

        @Override
        protected String doInBackground(List<String>... paths) {
            //final ArrayList<String> filePaths=
            //getImages(paths[0]);

            for (String filePaths : paths[0]) {
                //   final int finalI = i;
                String filePath = null;
                try {
                    filePath = SiliCompressor.with(AddProductsActivity.this).compress(filePaths, new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + getPackageName() + "/media/images"));
                    File imageFile = new File(filePath);
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    byte[] bt = bos.toByteArray();
                    String encodedImageString1 = Base64.encodeToString(bt, Base64.DEFAULT);
                    byte[] decodedString1 = Base64.decode(encodedImageString1.getBytes(), Base64.NO_WRAP);
                    Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString1, 0, decodedString1.length);
                    ImageVideoData image_v = new ImageVideoData();
                    image_v.setBitmap(decodedByte);
                    image_v.setPath(filePath);
                    image_thumbnails.add(image_v);
                    imageFiles.add(imageFile);
                    Thread.sleep(500);
                    progressDialog.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return "";
        }


        @Override
        protected void onPostExecute(String compressedFilePath) {
            super.onPostExecute(compressedFilePath);
            adapter.notifyDataSetChanged();
            progressDialog.dismiss();
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
            progressDialog.dismiss();
            Log.i("Silicompressor", "Path: " + compressedFilePath);
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
        }
    }

    public static Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix,
                true);
    }

    public void getUserShop() {
        try {
            Log.i("show_details", "Bearer " + PrefManager.getInstance(AddProductsActivity.this).getApiToken());

            RetrofitClient retrofitClient = new RetrofitClient();
            RestInterface service = retrofitClient.getAPIClient(WebServiceURLs.DOMAIN_NAME);
            service.getUserShop(PrefManager.getInstance(AddProductsActivity.this).getUserId(), "Bearer " + PrefManager.getInstance(AddProductsActivity.this).getApiToken(), new Callback<JsonElement>() {
                @Override
                public void success(JsonElement jsonElement, Response response) {
                    //this method call if webservice success
                    try {

                        JSONObject jsonObject = new JSONObject(jsonElement.toString());
                        String status = jsonObject.getString("status");

                        if (status.equals("1")) {
                            JSONArray data = jsonObject.getJSONArray("data");
                            for (int i = 0; i < data.length(); i++) {
                                JSONObject j1 = data.getJSONObject(i);
                                String id = j1.getString("id");
                                String name = j1.getString("name");
                                shopnameList.add(name);
                                shopId.add(id);


                            }
                            shopnameList.add(0, "" + getResources().getString(R.string.select_shop));
                            shopId.add(0, "selct id");
                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                                    AddProductsActivity.this,
                                    android.R.layout.simple_spinner_item,
                                    shopnameList
                            );
                            sp_select_shop.setAdapter(adapter);
                        }


                    } catch (JSONException | NullPointerException e) {
                        e.printStackTrace();

                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    Toast.makeText(AddProductsActivity.this, getResources().getString(R.string.check_internet), Toast.LENGTH_LONG).show();
                    Log.i("fdfdfdfdfdf", "" + error.getMessage());

                }
            });
        } catch (Exception e) {
            e.printStackTrace();

        }

    }//getUserShopClose

    public void ProductValidte() {


        if (et_product_name.getText().toString().trim().length() == 0) {

            et_product_name.requestFocus();
            tl_product_name.setError(getResources().getString(R.string.enter_product_name));
            return;
        } else {
            tl_product_name.setError(null);
        }

        if (et_desc.getText().toString().trim().length() ==0) {

            et_desc.requestFocus();
            tl_desc.setError(getResources().getString(R.string.product_desc));

            return;
        } else {
            tl_desc.setError(null);
        }

        if (et_price.getText().toString().trim().length() == 0) {

            et_price.requestFocus();
            tl_price.setError(getResources().getString(R.string.enter_price));

            return;
        } else {
            tl_price.setError(null);
        }
        if (et_hash_tag.getText().toString().trim().length() == 0) {

            et_hash_tag.requestFocus();
            tl_hash_tag.setError(getResources().getString(R.string.enter_hash_tag));

            return;
        } else {
            tl_hash_tag.setError(null);
        }
        if (shop_id.equals("")) {
            Toast.makeText(this, "" + getResources().getString(R.string.select_shop), Toast.LENGTH_SHORT).show();
            return;

        }
        if (imageFiles.size() <= 1) {

            Toast.makeText(AddProductsActivity.this, ""+getResources().getString(R.string.please_select_atleast_two_images), Toast.LENGTH_SHORT).show();
            return;
        }

        if ( !isUpdate.equals("true"))
        SaveProductDetails();
        else
            UpdateProductDetails();

    }

    public void UpdateProductDetails()
    {
        try {
            String user_id = PrefManager.getInstance(AddProductsActivity.this).getUserId();
            p_bar.setVisibility(View.VISIBLE);

            MultipartTypedOutput multipartTypedOutput = new MultipartTypedOutput();
            multipartTypedOutput.addPart("name", new TypedString(et_product_name.getText().toString().trim()));
            multipartTypedOutput.addPart("shop_id", new TypedString(shop_id));
            multipartTypedOutput.addPart("description", new TypedString(et_desc.getText().toString().trim()));
            multipartTypedOutput.addPart("price", new TypedString(et_price.getText().toString().trim()));

            multipartTypedOutput.addPart("user_id", new TypedString(user_id));
            multipartTypedOutput.addPart("discount", new TypedString(et_discount.getText().toString()));
            multipartTypedOutput.addPart("product_id", new TypedString(product_id));


            if (imageFiles.size() > 0) {
                for (int i = 0; i < imageFiles.size(); i++) {
                    multipartTypedOutput.addPart("product_photos[]", new TypedFile("application/octet-stream", new File(imageFiles.get(i).getAbsolutePath())));
                }
            } else {
                multipartTypedOutput.addPart("product_photos", new TypedString(""));
            }


            RetrofitClient retrofitClient = new RetrofitClient();
            RestInterface service = retrofitClient.getAPIClient(WebServiceURLs.DOMAIN_NAME);
            service.update_product("Bearer " + PrefManager.getInstance(AddProductsActivity.this).getApiToken(),
                    multipartTypedOutput, new Callback<JsonElement>() {
                        @Override
                        public void success(JsonElement jsonElement, Response response) {
                            //this method call if webservice success
                            try {
                                JSONObject jsonObject = new JSONObject(jsonElement.toString());
                                String status = jsonObject.getString("status");
                                p_bar.setVisibility(View.GONE);
                                if (status.equals("1")) {


                                    new SweetAlertDialog(AddProductsActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                                            .setTitleText(getResources().getString(R.string.success))
                                            .setContentText(""+getResources().getString(R.string.product_updated))
                                            .setConfirmText(getResources().getString(R.string.ok))
                                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                @Override
                                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                    finish();
                                                }
                                            })
                                            .show();

                                } else {
                                    Toast.makeText(AddProductsActivity.this, "" + jsonObject.getString("message"), Toast.LENGTH_SHORT).show();

                                }


                            } catch (JSONException | NullPointerException e) {
                                e.printStackTrace();

                            }
                        }

                        @Override
                        public void failure(RetrofitError error) {
                            p_bar.setVisibility(View.GONE);
                            Toast.makeText(AddProductsActivity.this, getString(R.string.check_internet), Toast.LENGTH_LONG).show();
                            Log.i("fdfdfdfdfdf", "" + error.getMessage());

                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    public void SaveProductDetails() {
        try {
            String user_id = PrefManager.getInstance(AddProductsActivity.this).getUserId();
            p_bar.setVisibility(View.VISIBLE);

            MultipartTypedOutput multipartTypedOutput = new MultipartTypedOutput();
            multipartTypedOutput.addPart("name", new TypedString(et_product_name.getText().toString().trim()));
            multipartTypedOutput.addPart("shop_id", new TypedString(shop_id));
            multipartTypedOutput.addPart("description", new TypedString(et_desc.getText().toString().trim()));
            multipartTypedOutput.addPart("price", new TypedString(et_price.getText().toString().trim()));

            multipartTypedOutput.addPart("user_id", new TypedString(user_id));
            multipartTypedOutput.addPart("discount", new TypedString(et_discount.getText().toString()));


            if (imageFiles.size() > 0) {
                for (int i = 0; i < imageFiles.size(); i++) {
                    multipartTypedOutput.addPart("product_photos[]", new TypedFile("application/octet-stream", new File(imageFiles.get(i).getAbsolutePath())));
                }
            } else {
                multipartTypedOutput.addPart("product_photos", new TypedString(""));
            }


            RetrofitClient retrofitClient = new RetrofitClient();
            RestInterface service = retrofitClient.getAPIClient(WebServiceURLs.DOMAIN_NAME);
            service.addProduct("Bearer " + PrefManager.getInstance(AddProductsActivity.this).getApiToken(),
                    multipartTypedOutput, new Callback<JsonElement>() {
                        @Override
                        public void success(JsonElement jsonElement, Response response) {
                            //this method call if webservice success
                            try {
                                JSONObject jsonObject = new JSONObject(jsonElement.toString());
                                String status = jsonObject.getString("status");

                                if (status.equals("1")) {

                                    Toast.makeText(AddProductsActivity.this, "" + jsonObject.getString("message"), Toast.LENGTH_SHORT).show();

                                    new SweetAlertDialog(AddProductsActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                                            .setTitleText(getResources().getString(R.string.success))
                                            .setContentText(""+jsonObject.getString("message"))
                                            .setConfirmText(getResources().getString(R.string.ok))
                                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                @Override
                                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                    finish();
                                                }
                                            })
                                            .show();

                                } else {
                                    Toast.makeText(AddProductsActivity.this, "" + jsonObject.getString("message"), Toast.LENGTH_SHORT).show();

                                }

                                p_bar.setVisibility(View.GONE);
                            } catch (JSONException | NullPointerException e) {
                                e.printStackTrace();

                            }
                        }

                        @Override
                        public void failure(RetrofitError error) {
                            p_bar.setVisibility(View.GONE);
                            Toast.makeText(AddProductsActivity.this, getString(R.string.check_internet), Toast.LENGTH_LONG).show();
                            Log.i("fdfdfdfdfdf", "" + error.getMessage());

                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();

        }


    }//seaveProductDetailsClose


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
        }

    }
}
