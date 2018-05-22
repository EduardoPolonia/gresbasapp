package inesctec.gresbas.data.model.model_files;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MyTeam {
    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("points")
    @Expose
    private String points;

    @SerializedName("photo")
    @Expose
    private byte[] photo;

    public String getUserTeam() {
        return username;
    }

    public void setUserTeam(String username) {
        this.username = username;
    }

    public String getPointsTeam() {
        return points;
    }

    public void setPointsTeam(String points) {
        this.points = points;
    }

 /*   public byte[] getPhoto() { return photo; }

    public void setPhoto(byte[] photo) {
        this.photo = photo;
    }*/
}
