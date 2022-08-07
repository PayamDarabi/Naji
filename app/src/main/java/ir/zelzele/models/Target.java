
package ir.zelzele.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Target {

    @SerializedName("state")
    @Expose
    private String state;
    @SerializedName("longitude")
    @Expose
    private String longitude;
    @SerializedName("latitude")
    @Expose
    private String latitude;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("depth")
    @Expose
    private String depth;
    @SerializedName("size")
    @Expose
    private String size;

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Target withState(String state) {
        this.state = state;
        return this;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public Target withLongitude(String longitude) {
        this.longitude = longitude;
        return this;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public Target withLatitude(String latitude) {
        this.latitude = latitude;
        return this;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Target withDate(String date) {
        this.date = date;
        return this;
    }

    public String getDepth() {
        return depth;
    }

    public void setDepth(String depth) {
        this.depth = depth;
    }

    public Target withDepth(String depth) {
        this.depth = depth;
        return this;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public Target withSize(String size) {
        this.size = size;
        return this;
    }


}
