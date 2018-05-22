package inesctec.gresbas.data.model.model_files;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Level {

    @SerializedName("idlevel")
    @Expose
    private String idlevel;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("description")
    @Expose
    private Object description;
    @SerializedName("image")
    @Expose
    private Object image;
    @SerializedName("minPoints")
    @Expose
    private String minPoints;
    @SerializedName("maxPoints")
    @Expose
    private String maxPoints;
    @SerializedName("active")
    @Expose
    private String active;

    public String getIdlevel() {
        return idlevel;
    }

    public void setIdlevel(String idlevel) {
        this.idlevel = idlevel;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object getDescription() {
        return description;
    }

    public void setDescription(Object description) {
        this.description = description;
    }

    public Object getImage() {
        return image;
    }

    public void setImage(Object image) {
        this.image = image;
    }

    public String getMinPoints() {
        return minPoints;
    }

    public void setMinPoints(String minPoints) {
        this.minPoints = minPoints;
    }

    public String getMaxPoints() {
        return maxPoints;
    }

    public void setMaxPoints(String maxPoints) {
        this.maxPoints = maxPoints;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

}