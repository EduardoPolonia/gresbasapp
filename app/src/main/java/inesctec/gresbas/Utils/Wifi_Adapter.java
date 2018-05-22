package inesctec.gresbas.Utils;

public class Wifi_Adapter {

    boolean selected = false;

    String SSID = null;
    String BSSID = null;
    Integer frequency = null;
    Integer level = null;

    public Wifi_Adapter(String SSID, String BSSID, Integer frequency, Integer level, Boolean selected) {
        super();

        this.SSID = SSID;
        this.BSSID = BSSID;
        this.frequency = frequency;
        this.level = level;
        this.selected = selected;
    }

    public Integer getFrequency() {
        return frequency;
    }

    public void setFrequency(Integer frequency) {
        this.frequency = frequency;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public String getSSID() {
        return SSID;
    }

    public void setSSID(String SSID) {
        this.SSID = SSID;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getBSSID() {
        return BSSID;
    }

    public void setBSSID(String BSSID) {
        this.BSSID = BSSID;
    }


}
