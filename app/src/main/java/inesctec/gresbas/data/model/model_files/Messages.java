package inesctec.gresbas.data.model.model_files;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Messages {

    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("messagetypedesc")
    @Expose
    private String typemessage;

    public String getTitleMessage() {
        return title;
    }

    public void setTitleMessage(String value) {
        this.title = value;
    }

    public String getDescriptionMessage() {
        return description;
    }

    public void setDescriptionMessage(String value) {
        this.description = value;
    }

    public String getDateMessage() {
        return date;
    }

    public void setDateMessage(String value) {
        this.date = value;
    }

    public String getMessageType() {
        return typemessage;
    }

    public void setMessageType(String value) {
        this.typemessage = value;
    }
}
