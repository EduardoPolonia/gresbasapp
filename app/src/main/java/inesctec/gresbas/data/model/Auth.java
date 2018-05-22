package inesctec.gresbas.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


// Authentication Data Model
// FJCM
public class Auth {

    @SerializedName("auth-jwt")
    @Expose
    private String authJwt;

    public String getAuthJwt() {
        return authJwt;
    }

    public void setAuthJwt(String authJwt) {
        this.authJwt = authJwt;
    }

}
