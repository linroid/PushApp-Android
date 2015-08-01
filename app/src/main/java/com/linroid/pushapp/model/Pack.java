package com.linroid.pushapp.model;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.linroid.pushapp.database.Db;
import com.linroid.pushapp.util.AndroidUtil;
import com.squareup.sqlbrite.SqlBrite;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import rx.functions.Func1;

/**
 * Package Model
 * <p/>
 * Created by linroid on 7/26/15.
 */
public class Pack implements Parcelable {
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
    @SerializedName("icon_url")
    @Expose
    private String iconUrl;
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
     * @return The iconUrl
     */
    public String getIconUrl() {
        return iconUrl;
    }

    /**
     * @param iconUrl The iconUrl
     */
    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
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

    /**
     * 获得友好的时间显示(xx分钟前)
     *
     * @return
     */
    public CharSequence getFriendlyTime() {
        return AndroidUtil.friendlyTime(createdAt);
    }

    /**
     * 判断是否存在本地文件
     *
     * @return
     */
    public boolean fileExists() {
        if (TextUtils.isEmpty(this.path)) {
            return false;
        }
        File file = new File(this.path);
        return file.exists();
    }

    @Override
    public String toString() {
        return "Pack{" +
                "id=" + id +
                ", packageName='" + packageName + '\'' +
                ", appName='" + appName + '\'' +
                ", versionName='" + versionName + '\'' +
                ", versionCode=" + versionCode +
                ", sdkLevel=" + sdkLevel +
                ", userId=" + userId +
                ", iconUrl='" + iconUrl + '\'' +
                ", createdAt='" + createdAt + '\'' +
                ", updatedAt='" + updatedAt + '\'' +
                ", downloadUrl='" + downloadUrl + '\'' +
                '}';
    }


    public Pack() {
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
        dest.writeString(this.iconUrl);
        dest.writeString(this.createdAt);
        dest.writeString(this.updatedAt);
        dest.writeString(this.downloadUrl);
        dest.writeString(this.path);
    }

    protected Pack(Parcel in) {
        this.id = (Integer) in.readValue(Integer.class.getClassLoader());
        this.packageName = in.readString();
        this.appName = in.readString();
        this.versionName = in.readString();
        this.versionCode = (Integer) in.readValue(Integer.class.getClassLoader());
        this.sdkLevel = (Integer) in.readValue(Integer.class.getClassLoader());
        this.userId = (Integer) in.readValue(Integer.class.getClassLoader());
        this.iconUrl = in.readString();
        this.createdAt = in.readString();
        this.updatedAt = in.readString();
        this.downloadUrl = in.readString();
        this.path = in.readString();
    }

    public ContentValues toContentValues() {
        ContentValues values = new ContentValues();
        values.put(DB.COLUMN_ID, id);
        values.put(DB.COLUMN_PACKAGE_NAME, packageName);
        values.put(DB.COLUMN_APP_NAME, appName);
        values.put(DB.COLUMN_VERSION_NAME, versionName);
        values.put(DB.COLUMN_VERSION_CODE, versionCode);
        values.put(DB.COLUMN_SDK_LEVEL, sdkLevel);
        values.put(DB.COLUMN_USER_ID, userId);
        values.put(DB.COLUMN_ICON_URL, iconUrl);
        values.put(DB.COLUMN_DOWNLOAD_URL, downloadUrl);
        values.put(DB.COLUMN_PATH, path);
        values.put(DB.COLUMN_CREATED_AT, createdAt);
        values.put(DB.COLUMN_UPDATED_AT, updatedAt);
        return values;
    }

    public static Pack fromCursor(Cursor cursor) {
        Pack pack = new Pack();
        pack.id = Db.getInt(cursor, DB.COLUMN_ID);
        pack.packageName = Db.getString(cursor, DB.COLUMN_PACKAGE_NAME);
        pack.appName = Db.getString(cursor, DB.COLUMN_APP_NAME);
        pack.versionName = Db.getString(cursor, DB.COLUMN_VERSION_NAME);
        pack.versionCode = Db.getInt(cursor, DB.COLUMN_VERSION_CODE);
        pack.sdkLevel = Db.getInt(cursor, DB.COLUMN_SDK_LEVEL);
        pack.userId = Db.getInt(cursor, DB.COLUMN_USER_ID);
        pack.iconUrl = Db.getString(cursor, DB.COLUMN_ICON_URL);
        pack.downloadUrl = Db.getString(cursor, DB.COLUMN_DOWNLOAD_URL);
        pack.path = Db.getString(cursor, DB.COLUMN_PATH);
        pack.createdAt = Db.getString(cursor, DB.COLUMN_CREATED_AT);
        pack.updatedAt = Db.getString(cursor, DB.COLUMN_UPDATED_AT);
        return pack;
    }


    public static final Creator<Pack> CREATOR = new Creator<Pack>() {
        public Pack createFromParcel(Parcel source) {
            return new Pack(source);
        }

        public Pack[] newArray(int size) {
            return new Pack[size];
        }
    };

    public static class DB {
        public static final String TABLE_NAME = "packages";

        public static final String COLUMN_ID = "id";
        public static final String COLUMN_PACKAGE_NAME = "package_name";
        public static final String COLUMN_APP_NAME = "app_name";
        public static final String COLUMN_VERSION_NAME = "version_name";
        public static final String COLUMN_VERSION_CODE = "version_code";
        public static final String COLUMN_SDK_LEVEL = "sdk_level";
        public static final String COLUMN_USER_ID = "user_id";
        public static final String COLUMN_ICON_URL = "icon_url";
        public static final String COLUMN_DOWNLOAD_URL = "download_url";
        public static final String COLUMN_PATH = "path";
        public static final String COLUMN_CREATED_AT = "created_at";
        public static final String COLUMN_UPDATED_AT = "updated_at";

        public static final String SQL_CREATE = ""
                + "CREATE TABLE " + TABLE_NAME + "("
                + COLUMN_ID + " INTEGER NOT NULL,"
                + COLUMN_APP_NAME + " TEXT NOT NULL,"
                + COLUMN_PACKAGE_NAME + " TEXT NOT NULL,"
                + COLUMN_VERSION_NAME + " TEXT NOT NULL,"
                + COLUMN_VERSION_CODE + " TEXT NOT NULL,"
                + COLUMN_SDK_LEVEL + " INTEGER NOT NULL,"
                + COLUMN_USER_ID + " INTEGER NOT NULL,"
                + COLUMN_ICON_URL + " TEXT,"
                + COLUMN_DOWNLOAD_URL + " TEXT NOT NULL,"
                + COLUMN_PATH + " TEXT,"
                + COLUMN_CREATED_AT + " TEXT NOT NULL,"
                + COLUMN_UPDATED_AT + " TEXT NOT NULL"
                + ")";

        public static final String SQL_DROP = "DROP TABLE " + TABLE_NAME;

        public static final String SQL_LIST_QUERY = "SELECT * FROM " + TABLE_NAME + " ORDER BY " + COLUMN_CREATED_AT + " DESC";

        public static final String WHERE_ID = COLUMN_ID + "= ?";
        public static final String SQL_ITEM_QUERY = "SELECT * FROM " + TABLE_NAME + " WHERE " + WHERE_ID;


        public static final Func1<SqlBrite.Query, List<Pack>> MAP = new Func1<SqlBrite.Query, List<Pack>>() {
            @Override
            public List<Pack> call(SqlBrite.Query query) {
                Cursor cursor = query.run();
                try {
                    List<Pack> values = new ArrayList<>(cursor.getCount());
                    while (cursor.moveToNext()) {
                        values.add(Pack.fromCursor(cursor));
                    }
                    return values;
                } finally {
                    cursor.close();
                }
            }
        };
    }
}