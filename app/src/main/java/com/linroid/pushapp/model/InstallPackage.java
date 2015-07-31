package com.linroid.pushapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.linroid.pushapp.util.AndroidUtil;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

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
    private Integer versionCode;
    @SerializedName("sdk_level")
    @Expose
    private Integer sdkLevel;
    @SerializedName("user_id")
    @Expose
    private Integer userId;
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
     * 本地保存的路径
     */
    private String path;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }


    /**
     * @return The id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id The id
     */
    public void setId(Integer id) {
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
    public Integer getVersionCode() {
        return versionCode;
    }

    /**
     * @param versionCode The version_code
     */
    public void setVersionCode(Integer versionCode) {
        this.versionCode = versionCode;
    }

    /**
     * @return The sdkLevel
     */
    public Integer getSdkLevel() {
        return sdkLevel;
    }

    /**
     * @param sdkLevel The sdk_level
     */
    public void setSdkLevel(Integer sdkLevel) {
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

    public CharSequence getFriendlyTime() {
        return AndroidUtil.friendlyTime(createdAt);
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
        dest.writeValue(this.versionCode);
        dest.writeValue(this.sdkLevel);
        dest.writeValue(this.userId);
        dest.writeString(this.icon);
        dest.writeString(this.createdAt);
        dest.writeString(this.updatedAt);
        dest.writeString(this.downloadUrl);
        dest.writeString(this.path);
    }

    protected InstallPackage(Parcel in) {
        this.id = (Integer) in.readValue(Integer.class.getClassLoader());
        this.packageName = in.readString();
        this.appName = in.readString();
        this.versionName = in.readString();
        this.versionCode = (Integer) in.readValue(Integer.class.getClassLoader());
        this.sdkLevel = (Integer) in.readValue(Integer.class.getClassLoader());
        this.userId = (Integer) in.readValue(Integer.class.getClassLoader());
        this.icon = in.readString();
        this.createdAt = in.readString();
        this.updatedAt = in.readString();
        this.downloadUrl = in.readString();
        this.path = in.readString();
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