package ir.FiveMFive.FiveMFive.Java;

import com.google.gson.annotations.SerializedName;

public class Group {
    @SerializedName("id")
    private String id;
    @SerializedName("title")
    private String name;
    @SerializedName("number")
    private String mobileCount;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobileCount() {
        return mobileCount;
    }

    public void setMobileCount(String mobileCount) {
        this.mobileCount = mobileCount;
    }
}
