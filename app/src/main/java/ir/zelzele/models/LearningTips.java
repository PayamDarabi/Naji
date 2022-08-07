package ir.zelzele.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Payam on 12/30/2017.
 */

public class LearningTips {

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTittle() {
        return tittle;
    }

    public void setTittle(String tittle) {
        this.tittle = tittle;
    }

    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("title")
    @Expose
    private String tittle;

}
