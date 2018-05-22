package inesctec.gresbas.data.routes;

import inesctec.gresbas.data.model.model_data.ImpactData;
import inesctec.gresbas.data.model.model_data.MessagesData;
import inesctec.gresbas.data.model.model_data.MyTeamData;
import inesctec.gresbas.data.model.model_data.PointsWeekData;
import inesctec.gresbas.data.model.model_data.RestaurantData;
import inesctec.gresbas.data.model.model_data.ScoreBoardData;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;

public interface GameClient {
    // Get ScoreBoard
    @GET("scoreboard")
    Call<ScoreBoardData> getScoreboard(@Header("Authorization") String credentials);

    // Get Impact
    @GET("impact/{id}/{d1}/{d2}")
    Call<ImpactData> getImpact(@Path("id") String id, @Path("d1") String d1, @Path("d2") String d2, @Header("Authorization") String credentials);

    // Get Menu
    @GET("menu/{id}/{d1}")
    Call<RestaurantData> getMenu(@Path("id") String id, @Path("d1") String d1, @Header("Authorization") String credentials);

    // Get Team
    @GET("myteam/{id}")
    Call<MyTeamData> getTeam(@Path("id") String id, @Header("Authorization") String credentials);

    // Get Message
    @GET("messages/{id}")
    Call<MessagesData> getMessage(@Path("id") String id, @Header("Authorization") String credentials);

    // Get Points Week
    @GET("pointstypebyweek/{id}")
    Call<PointsWeekData> getPointsWeek(@Path("id") String id, @Header("Authorization") String credentials);
}
