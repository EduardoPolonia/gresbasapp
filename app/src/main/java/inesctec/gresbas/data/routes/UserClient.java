package inesctec.gresbas.data.routes;

import inesctec.gresbas.data.model.Auth;
import inesctec.gresbas.data.model.model_data.EarnedPointsData;
import inesctec.gresbas.data.model.model_data.LevelData;
import inesctec.gresbas.data.model.model_files.Users;
import inesctec.gresbas.data.model.model_data.UsersData;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;

// INTERFACE with REST API
// Defines all the routing implemented on the REST API
// FJCM

public interface UserClient {

    // Ueer Authentication
    @GET("auth")
    Call<Auth> loginUser(@Header("Authorization") String credentials);

    // Get All Users
    @GET("users")
    Call<Users> getUsers(@Header("Authorization") String credentials);

    // Get User Info
    @GET("user")
    Call<UsersData> getUserInfo(@Header("Authorization") String credentials);

    // Get User Level
    @GET("userlevel")
    Call<LevelData> getUserLevel(@Header("Authorization") String credentials);


    // Get Earned Points for a User
    @GET("pointstype/{user}")
    Call<EarnedPointsData> getEarnedPoints(@Path("user") String user, @Header("Authorization") String credentials);

}
