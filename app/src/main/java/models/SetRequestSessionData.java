package models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SetRequestSessionData {

    @SerializedName("request_token")
    @Expose
    private String requestToken;

    public String getRequestToken() {
        return requestToken;
    }

    public void setRequestToken(String requestToken) {
        this.requestToken = requestToken;
    }

}
