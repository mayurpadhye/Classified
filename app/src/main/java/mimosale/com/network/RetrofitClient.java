package mimosale.com.network;


import com.squareup.okhttp.OkHttpClient;

import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.client.OkClient;

/**
 * RetrofitClient.
 */

public class RetrofitClient {

    public RestInterface getAPIClient(String url)
    {
        RequestInterceptor requestInterceptor = new RequestInterceptor() {
            @Override
            public void intercept(RequestFacade request) {

                //  request.addHeader("Content-Type", "application/json");
            }
        };


        RestAdapter adapter = new RestAdapter.Builder()
                .setEndpoint(url)
                .setRequestInterceptor(requestInterceptor)
                .setClient(new OkClient(new OkHttpClient()))
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();
        return adapter.create(RestInterface.class);
    }

}
