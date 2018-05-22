package inesctec.gresbas.data.model.model_files;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PointsWeek {

    @SerializedName("totalPoints")
    @Expose
    private String totalPoints;
    @SerializedName("name")
    @Expose
    private String name;

    public String getTotalPointsWeek() {
        return totalPoints;
    }

    public void setTotalPointsWeek(String totalPoints) {
        this.totalPoints = totalPoints;
    }

    public String getNameWeek() {
        return name;
    }

    public void setNameWeek(String name) {
        this.name = name;
    }
}
