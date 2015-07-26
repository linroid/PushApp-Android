package com.linroid.pushapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by linroid on 7/26/15.
 */
public class InstallPackage implements Parcelable {

    @Expose
    private Integer id;
    @SerializedName("package_name")
    @Expose
    private String packageName;
    @SerializedName("app_name")
    @Expose
    private String appName;
    @SerializedName("version_name")
    @Expose
    private String versionName;
    @SerializedName("version_code")
    @Expose
    private int versionCode;
    @SerializedName("sdk_level")
    @Expose
    private int sdkLevel;
    @SerializedName("user_id")
    @Expose
    private int userId;
    @Expose
    private String icon;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("download_url")
    @Expose
    private String downloadUrl;

    /**
     * @return The id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id The id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return The packageName
     */
    public String getPackageName() {
        return packageName;
    }

    /**
     * @param packageName The package_name
     */
    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    /**
     * @return The appName
     */
    public String getAppName() {
        return appName;
    }

    /**
     * @param appName The app_name
     */
    public void setAppName(String appName) {
        this.appName = appName;
    }

    /**
     * @return The versionName
     */
    public String getVersionName() {
        return versionName;
    }

    /**
     * @param versionName The version_name
     */
    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    /**
     * @return The versionCode
     */
    public int getVersionCode() {
        return versionCode;
    }

    /**
     * @param versionCode The version_code
     */
    public void setVersionCode(int versionCode) {
        this.versionCode = versionCode;
    }

    /**
     * @return The sdkLevel
     */
    public int getSdkLevel() {
        return sdkLevel;
    }

    /**
     * @param sdkLevel The sdk_level
     */
    public void setSdkLevel(int sdkLevel) {
        this.sdkLevel = sdkLevel;
    }

    /**
     * @return The userId
     */
    public Integer getUserId() {
        return userId;
    }

    /**
     * @param userId The user_id
     */
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    /**
     * @return The icon
     */
    public String getIcon() {
        return icon;
    }

    /**
     * @param icon The icon
     */
    public void setIcon(String icon) {
        this.icon = icon;
    }

    /**
     * @return The createdAt
     */
    public String getCreatedAt() {
        return createdAt;
    }

    /**
     * @param createdAt The created_at
     */
    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * @return The updatedAt
     */
    public String getUpdatedAt() {
        return updatedAt;
    }

    /**
     * @param updatedAt The updated_at
     */
    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    /**
     * @return The downloadUrl
     */
    public String getDownloadUrl() {
        return downloadUrl;
    }

    /**
     * @param downloadUrl The downloadUrl
     */
    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    @Override
    public String toString() {
        return "InstallPackage{" +
                "id=" + id +
                ", packageName='" + packageName + '\'' +
                ", appName='" + appName + '\'' +
                ", versionName='" + versionName + '\'' +
                ", versionCode=" + versionCode +
                ", sdkLevel=" + sdkLevel +
                ", userId=" + userId +
                ", icon='" + icon + '\'' +
                ", createdAt='" + createdAt + '\'' +
                ", updatedAt='" + updatedAt + '\'' +
                ", downloadUrl='" + downloadUrl + '\'' +
                '}';
    }

    public InstallPackage() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.id);
        dest.writeString(this.packageName);
        dest.writeString(this.appName);
        dest.writeString(this.versionName);
        dest.writeInt(this.versionCode);
        dest.writeInt(this.sdkLevel);
        dest.writeInt(this.userId);
        dest.writeString(this.icon);
        dest.writeString(this.createdAt);
        dest.writeString(this.updatedAt);
        dest.writeString(this.downloadUrl);
    }

    protected InstallPackage(Parcel in) {
        this.id = (Integer) in.readValue(Integer.class.getClassLoader());
        this.packageName = in.readString();
        this.appName = in.readString();
        this.versionName = in.readString();
        this.versionCode = in.readInt();
        this.sdkLevel = in.readInt();
        this.userId = in.readInt();
        this.icon = in.readString();
        this.createdAt = in.readString();
        this.updatedAt = in.readString();
        this.downloadUrl = in.readString();
    }

    public static final Creator<InstallPackage> CREATOR = new Creator<InstallPackage>() {
        public InstallPackage createFromParcel(Parcel source) {
            return new InstallPackage(source);
        }

        public InstallPackage[] newArray(int size) {
            return new InstallPackage[size];
        }
    };
}