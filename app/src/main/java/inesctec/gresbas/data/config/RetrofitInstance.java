package inesctec.gresbas.data.config;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

// Connection mechanism to interact with the GRESBAS REST API
// FJCM

public class RetrofitInstance {

    private static Retrofit retrofit;
    private static final String BASE_URL = "https://api.gresbas.eu/index.php/";

    public static Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            retrofit = new retrofit2.Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

}