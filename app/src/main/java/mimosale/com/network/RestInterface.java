package mimosale.com.network;


import mimosale.com.helperClass.PrefManager;
import com.google.gson.JsonElement;

import java.util.Map;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.Headers;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.PartMap;
import retrofit.mime.MultipartTypedOutput;

/**
 * Rest Interface -> to access web_services using Retrofit
 */

public interface RestInterface {





    @FormUrlEncoded
    @POST(WebServiceURLs.USER_LOGIN)
    void LoginToApp(@Field("email") String email,@Field("password") String password, Callback<JsonElement> callback);

    @FormUrlEncoded
    @POST(WebServiceURLs.SEARCH_RESULT)
    void search(@Field("keyword") String keyword, Callback<JsonElement> callback);



    @POST(WebServiceURLs.REGISTER_USER)
    void register(@Body MultipartTypedOutput attachments, Callback<JsonElement> callback);


    @FormUrlEncoded
    @POST(WebServiceURLs.USER_PREFERENCES)
    void getUserPreferences(@Field("user_id") String user_id, @Header("Authorization") String header, Callback<JsonElement> callback);
    @FormUrlEncoded
    @POST(WebServiceURLs.DEVICE_WISE_PREF)
    void getDeviceWisePref(@Field("device_id") String device_id, @Header("Authorization") String header, Callback<JsonElement> callback);


    @GET(WebServiceURLs.GET_BANNER_IMAGES)
    void GetBannerImages( Callback<JsonElement> callback);

    @FormUrlEncoded
    @POST(WebServiceURLs.DELETE_USER_PREF)
    void deleteUserPref( @Field("user_id") String user_id, @Header("Authorization") String header, @Field("preference_id") String pref_id,Callback<JsonElement> callback);


    @FormUrlEncoded
    @POST(WebServiceURLs.USER_DATA)
    void getUserData(@Field("user_id") String user_id, @Header("Authorization") String header, Callback<JsonElement> callback);


    @FormUrlEncoded
    @POST(WebServiceURLs.MY_SALE_POSTING)
    void my_sale_posting(@Field("user_id") String user_id, @Header("Authorization") String header, Callback<JsonElement> callback);



    @FormUrlEncoded
    @POST(WebServiceURLs.GET_ALL_PREF)
    void getAllPreferences( @Field("user_id") String user_id, @Header("Authorization") String header, Callback<JsonElement> callback);

    @FormUrlEncoded
    @POST(WebServiceURLs.ADD_USER_PREF)
    void addUserPrefData( @Field("user_id") String user_id, @Header("Authorization") String header,@Field("preference_id") String preference_id, Callback<JsonElement> callback);

    @FormUrlEncoded
    @POST(WebServiceURLs.ADD_DEVICEID_PREF)
    void addPrefAgainstDeviceId( @Field("device_id") String device_id, @Header("Authorization") String header,@Field("preference_id") String preference_id, Callback<JsonElement> callback);




    @FormUrlEncoded
    @POST(WebServiceURLs.ADD_SALE_POSTING)
    void addSalePosting(@Field("title") String title, @Field("shop_id") String shop_id, @Field("product_id") String product_id,
            @Field("min_discount") String min_discount,
            @Field("max_discount") String max_discount,
            @Field("hash_tags") String hash_tags,
            @Field("description") String description,
            @Field("web_url") String web_url,
            @Field("user_id") String user_id,
                         @Header("Authorization") String header, Callback<JsonElement> callback);




    @FormUrlEncoded
    @POST(WebServiceURLs.GET_SHOP_DETAILS)
    void getShopDetails( @Field("shop_id") String shop_id,@Field("user_id") String user_id, Callback<JsonElement> callback);
    @FormUrlEncoded
    @POST(WebServiceURLs.GET_PRODUCT_DETAILS)
    void getProductDetails( @Field("product_id") String product_id, Callback<JsonElement> callback);

    @FormUrlEncoded
    @POST(WebServiceURLs.GET_USER_SHOP)
    void getUserShop( @Field("user_id") String user_id,@Header("Authorization") String header, Callback<JsonElement> callback);

    @FormUrlEncoded
    @POST(WebServiceURLs.GET_SHOP_WISE_PRODUCTS)
    void getShopWiseProdct( @Field("shop_id") String shop_id, @Field("user_id") String user_id,@Header("Authorization") String header, Callback<JsonElement> callback);

    @FormUrlEncoded
    @POST(WebServiceURLs.GET_MY_PRODUCT_POSTING)
    void my_product_posting(  @Field("user_id") String user_id,@Header("Authorization") String header, Callback<JsonElement> callback);

    @FormUrlEncoded
    @POST(WebServiceURLs.GET_SHOP_WISE_PRODUCTS)
    void my_shop_postings(  @Field("user_id") String user_id,@Header("Authorization") String header, Callback<JsonElement> callback);

    @FormUrlEncoded
    @POST(WebServiceURLs.GET_DEVICE_SELECTED_PREF)
    void getDivicePrefList(  @Field("device_id") String device_id,@Header("Authorization") String header, Callback<JsonElement> callback);





    @POST(WebServiceURLs.ADD_SHOP_POSTING)
    void addShopPosting(
                         @Header("Authorization") String header,@Body MultipartTypedOutput attachments,  Callback<JsonElement> callback);


    @POST(WebServiceURLs.UPDATE_SHOP_POSTING)
    void update_shop(
            @Header("Authorization") String header,@Body MultipartTypedOutput attachments,  Callback<JsonElement> callback);






    @POST(WebServiceURLs.ADD_PRODUCT)
    void addProduct(
            @Header("Authorization") String header,@Body MultipartTypedOutput attachments,  Callback<JsonElement> callback);

    @POST(WebServiceURLs.UPDATE_PRODUCTS)
    void update_product(
            @Header("Authorization") String header,@Body MultipartTypedOutput attachments,  Callback<JsonElement> callback);

    @FormUrlEncoded
    @POST(WebServiceURLs.ALL_SHOP_AND_SALE)
    void getAllShopAndSale(  @Field("user_id") String user_id, Callback<JsonElement> callback);

    @GET(WebServiceURLs.GET_ALL_PRODUCTS)
    void getAllProducts(  Callback<JsonElement> callback);

    @GET(WebServiceURLs.GET_ALL_PREFRENCES)
    void getAllPref(  Callback<JsonElement> callback);

    @FormUrlEncoded
    @POST(WebServiceURLs.GET_DEVICE_WISE_PREF)
    void getDevicePref(@Field("device_id") String device_id,  Callback<JsonElement> callback);

    @FormUrlEncoded
    @POST(WebServiceURLs.GET_DEVICE_WISE_PREF)
    void getProductWisePosting(@Field("user_id") String device_id,  Callback<JsonElement> callback);

    @POST(WebServiceURLs.UPDATE_USER)
    void update_profile(@Body MultipartTypedOutput attachments,@Header("Authorization") String header, Callback<JsonElement> callback);



    @FormUrlEncoded
    @POST(WebServiceURLs.SUBMIT_FEEDBACK)
    void feedback(@Field("user_id") String user_id,@Field("feedback") String feedback,@Header("Authorization") String header,  Callback<JsonElement> callback);

    @FormUrlEncoded
    @POST(WebServiceURLs.CHANGE_PASSWORD)
    void changePass(@Field("user_id") String user_id,@Field("current_password") String current_password,@Field("new_password") String new_password,@Header("Authorization") String header,  Callback<JsonElement> callback);
    @FormUrlEncoded
    @POST(WebServiceURLs.FOLLOWING_SHOPS)
    void getShopFollowingList(@Field("user_id") String user_id,@Header("Authorization") String header,  Callback<JsonElement> callback);

    @FormUrlEncoded
    @POST(WebServiceURLs.FOLLOW_SHOP)
    void followShop(@Field("status") String status,@Field("user_id") String user_id,@Field("shop_id") String shop_id,@Header("Authorization") String header,  Callback<JsonElement> callback);

    @FormUrlEncoded
    @POST(WebServiceURLs.DELETE_USER_PRODUCTS)
    void delete_posting(@Field("user_id") String user_id,@Field("product_id") String product_id,@Header("Authorization") String header,  Callback<JsonElement> callback);

    @FormUrlEncoded
    @POST(WebServiceURLs.DELETE_USER_SHOP)
    void delete_shop_posting(@Field("user_id") String user_id,@Field("shop_id") String product_id,@Header("Authorization") String header,  Callback<JsonElement> callback);

    @FormUrlEncoded
    @POST(WebServiceURLs.DELETE_SALE)
    void delete_sale(@Field("user_id") String user_id,@Field("sale_id") String sale_id,@Header("Authorization") String header,  Callback<JsonElement> callback);


    @FormUrlEncoded
    @POST(WebServiceURLs.UPDATE_SALE)
    void updateSalePosting(@Field("title") String title,
                           @Field("shop_id") String shop_id, @Field("product_id") String product_id,
                           @Field("min_discount") String min_discount,
                           @Field("max_discount") String max_discount,
                           @Field("hash_tags") String hash_tags,
                           @Field("description") String description,
                           @Field("web_url") String web_url,
                           @Field("user_id") String user_id,@Header("Authorization") String header,  Callback<JsonElement> callback);

    @FormUrlEncoded
    @POST(WebServiceURLs.UPDATE_SALE)
    void like_shop(@Field("title") String title,
                           @Field("shop_id") String shop_id, @Field("product_id") String product_id,
                           @Field("min_discount") String min_discount,
                           @Field("max_discount") String max_discount,
                           @Field("hash_tags") String hash_tags,
                           @Field("description") String description,
                           @Field("web_url") String web_url,
                           @Field("user_id") String user_id,@Header("Authorization") String header,  Callback<JsonElement> callback);


}
