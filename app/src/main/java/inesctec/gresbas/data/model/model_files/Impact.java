package inesctec.gresbas.data.model.model_files;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Impact {

    @SerializedName("Date")
    @Expose
    private String date;
    @SerializedName("Value")
    @Expose
    private String value;
    @SerializedName("ValueInt")
    @Expose
    private Double valueInt;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Double getValueInt() {
        return valueInt;
    }

    public void setValueInt(Double valueInt) {
        this.valueInt = valueInt;
    }

}