package inesctec.gresbas.data.model.model_files;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class EarnedPoints {

    @SerializedName("totalPoints")
    @Expose
    private String totalPoints;
    @SerializedName("name")
    @Expose
    private String name;

    public String getTotalPoints() {
        return totalPoints;
    }

    public void setTotalPoints(String totalPoints) {
        this.totalPoints = totalPoints;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}

