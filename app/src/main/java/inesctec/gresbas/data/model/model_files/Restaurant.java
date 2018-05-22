package inesctec.gresbas.data.model.model_files;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Restaurant {

    @SerializedName("description")
    @Expose
    private String mindescription;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("Description")
    @Expose
    private String maxdescription;

    public String getMindescription() {
        return mindescription;
    }

    public void setMindescription(String value) {
        this.mindescription = value;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String value) {
        this.date = value;
    }

    public String getMaxdescription() {
        return maxdescription;
    }

    public void setMaxdescription(String value) {
        this.maxdescription = value;
    }
}
