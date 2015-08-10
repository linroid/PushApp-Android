package com.linroid.pushapp.model;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.linroid.pushapp.database.Db;
import com.squareup.sqlbrite.SqlBrite;

import java.util.ArrayList;
import java.util.List;

import rx.functions.Func1;

/**
 * Created by linroid on 7/24/15.
 */
public class Device implements Parcelable {

    @Expose
    private Integer id;
    @Expose
    private String model;
    @Expose
    private String alias;
    @SerializedName("sdk_level")
    @Expose
    private int sdkLevel;
    @SerializedName("os_name")
    @Expose
    private String osName;
    @Expose
    private int height;
    @Expose
    private int width;
    @Expose
    private int dpi;
    @SerializedName("device_id")
    @Expose
    private String deviceId;
    @SerializedName("push_id")
    @Expose
    private String pushId;
    @SerializedName("memory_size")
    @Expose
    private int memorySize;
    @SerializedName("cpu_type")
    @Expose
    private String cpuType;
    @Expose
    private String token;
    @SerializedName("network_type")
    @Expose
    private String networkType;
    @SerializedName("user_id")
    @Expose
    private Integer userId;
    @Expose
    private User user;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;

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
     * @return The model
     */
    public String getModel() {
        return model;
    }

    /**
     * @param model The model
     */
    public void setModel(String model) {
        this.model = model;
    }

    /**
     * @return The alias
     */
    public String getAlias() {
        return alias;
    }

    /**
     * @param alias The alias
     */
    public void setAlias(String alias) {
        this.alias = alias;
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
     * @return The osName
     */
    public String getOsName() {
        return osName;
    }

    /**
     * @param osName The os_name
     */
    public void setOsName(String osName) {
        this.osName = osName;
    }

    /**
     * @return The height
     */
    public int getHeight() {
        return height;
    }

    /**
     * @param height The height
     */
    public void setHeight(int height) {
        this.height = height;
    }

    /**
     * @return The width
     */
    public int getWidth() {
        return width;
    }

    /**
     * @param width The width
     */
    public void setWidth(int width) {
        this.width = width;
    }

    /**
     * @return The dpi
     */
    public int getDpi() {
        return dpi;
    }

    /**
     * @param dpi The dpi
     */
    public void setDpi(int dpi) {
        this.dpi = dpi;
    }

    /**
     * @return The deviceId
     */
    public String getDeviceId() {
        return deviceId;
    }

    /**
     * @param deviceId The device_id
     */
    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    /**
     * @param pushId The push_id
     */
    public void setPushId(String pushId) {
        this.pushId = pushId;
    }

    /**
     * @return The pushId
     */
    public String getPushId() {
        return pushId;
    }

    /**
     * @return The memorySize
     */
    public int getMemorySize() {
        return memorySize;
    }

    /**
     * @param memorySize The memory_size
     */
    public void setMemorySize(int memorySize) {
        this.memorySize = memorySize;
    }

    /**
     * @return The cpuType
     */
    public String getCpuType() {
        return cpuType;
    }

    /**
     * @param cpuType The cpu_type
     */
    public void setCpuType(String cpuType) {
        this.cpuType = cpuType;
    }

    /**
     * @return The token
     */
    public String getToken() {
        return token;
    }

    /**
     * @param token The token
     */
    public void setToken(String token) {
        this.token = token;
    }

    /**
     * @return The networkType
     */
    public String getNetworkType() {
        return networkType;
    }

    /**
     * @param networkType The network_type
     */
    public void setNetworkType(String networkType) {
        this.networkType = networkType;
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
     * @return The user
     */
    public User getUser() {
        return user;
    }

    /**
     * @param user The user
     */
    public void setUserId(User user) {
        this.user = user;
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

    @Override
    public String toString() {
        return "Device{" +
                "id='" + id + '\'' +
                ", model='" + model + '\'' +
                ", alias='" + alias + '\'' +
                ", sdkLevel=" + sdkLevel +
                ", osName='" + osName + '\'' +
                ", height=" + height +
                ", width=" + width +
                ", dpi=" + dpi +
                ", deviceId='" + deviceId + '\'' +
                ", pushId='" + pushId + '\'' +
                ", memorySize=" + memorySize +
                ", cpuType='" + cpuType + '\'' +
                ", token='" + token + '\'' +
                ", networkType='" + networkType + '\'' +
                ", userId=" + userId +
                ", user=" + user +
                ", createdAt='" + createdAt + '\'' +
                ", updatedAt='" + updatedAt + '\'' +
                '}';
    }

    public Device() {
    }
    private static Device fromCursor(Cursor cursor) {
        Device device = new Device();
        device.id = Db.getInt(cursor, DB.COLUMN_ID);
        device.alias = Db.getString(cursor, DB.COLUMN_ALIAS);
        device.sdkLevel = Db.getInt(cursor, DB.COLUMN_SDK_LEVEL);
        device.height = Db.getInt(cursor, DB.COLUMN_HEIGHT);
        device.width = Db.getInt(cursor, DB.COLUMN_WIDTH);
        device.dpi = Db.getInt(cursor, DB.COLUMN_DPI);
        device.deviceId = Db.getString(cursor, DB.COLUMN_DEVICE_ID);
        device.pushId = Db.getString(cursor, DB.COLUMN_PUSH_ID);
        device.memorySize = Db.getInt(cursor, DB.COLUMN_MEMORY_SIZE);
        device.cpuType = Db.getString(cursor, DB.COLUMN_CPU_TYPE);
        device.token = Db.getString(cursor, DB.COLUMN_TOKEN);
        device.networkType = Db.getString(cursor, DB.COLUMN_NETWORK_TYPE);
        device.userId = Db.getInt(cursor, DB.COLUMN_USER_ID);
        device.createdAt = Db.getString(cursor, DB.COLUMN_CREATED_AT);
        device.updatedAt = Db.getString(cursor, DB.COLUMN_UPDATED_AT);
        return device;
    }
    public ContentValues toContentValues() {
        ContentValues values = new ContentValues();
        values.put(DB.COLUMN_ID, id);
        values.put(DB.COLUMN_ALIAS, alias);
        values.put(DB.COLUMN_SDK_LEVEL, sdkLevel);
        values.put(DB.COLUMN_HEIGHT, height);
        values.put(DB.COLUMN_WIDTH, width);
        values.put(DB.COLUMN_DPI, dpi);
        values.put(DB.COLUMN_DEVICE_ID, deviceId);
        values.put(DB.COLUMN_PUSH_ID, pushId);
        values.put(DB.COLUMN_MEMORY_SIZE, memorySize);
        values.put(DB.COLUMN_CPU_TYPE, cpuType);
        values.put(DB.COLUMN_TOKEN, token);
        values.put(DB.COLUMN_NETWORK_TYPE, networkType);
        values.put(DB.COLUMN_USER_ID, userId);
        values.put(DB.COLUMN_CREATED_AT, createdAt);
        values.put(DB.COLUMN_UPDATED_AT, updatedAt);
        return values;
    }

    public static class DeviceBuilder {
        private Integer id;
        private String model;
        private String alias;
        private int sdkLevel;
        private String osName;
        private int height;
        private int width;
        private int dpi;
        private String deviceId;
        private String pushId;
        private int memorySize;
        private String cpuType;
        private String token;
        private String networkType;
        private Integer userId;
        private String createdAt;
        private String updatedAt;

        private DeviceBuilder() {
        }

        public static DeviceBuilder aDevice() {
            return new DeviceBuilder();
        }

        public DeviceBuilder withId(Integer id) {
            this.id = id;
            return this;
        }

        public DeviceBuilder withModel(String model) {
            this.model = model;
            return this;
        }

        public DeviceBuilder withAlias(String alias) {
            this.alias = alias;
            return this;
        }

        public DeviceBuilder withSdkLevel(int sdkLevel) {
            this.sdkLevel = sdkLevel;
            return this;
        }

        public DeviceBuilder withOsName(String osName) {
            this.osName = osName;
            return this;
        }

        public DeviceBuilder withHeight(int height) {
            this.height = height;
            return this;
        }

        public DeviceBuilder withWidth(int width) {
            this.width = width;
            return this;
        }

        public DeviceBuilder withDpi(int dpi) {
            this.dpi = dpi;
            return this;
        }

        public DeviceBuilder withDeviceId(String deviceId) {
            this.deviceId = deviceId;
            return this;
        }

        public DeviceBuilder withPushId(String pushId) {
            this.pushId = pushId;
            return this;
        }

        public DeviceBuilder withMemorySize(int memorySize) {
            this.memorySize = memorySize;
            return this;
        }

        public DeviceBuilder withCpuType(String cpuType) {
            this.cpuType = cpuType;
            return this;
        }

        public DeviceBuilder withToken(String token) {
            this.token = token;
            return this;
        }

        public DeviceBuilder withNetworkType(String networkType) {
            this.networkType = networkType;
            return this;
        }

        public DeviceBuilder withUserId(int userId) {
            this.userId = userId;
            return this;
        }

        public DeviceBuilder withCreatedAt(String createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public DeviceBuilder withUpdatedAt(String updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        public DeviceBuilder but() {
            return aDevice().withId(id).withModel(model).withAlias(alias).withSdkLevel(sdkLevel).withOsName(osName).withHeight(height).withWidth(width).withDpi(dpi).withDeviceId(deviceId).withMemorySize(memorySize).withCpuType(cpuType).withToken(token).withNetworkType(networkType).withUserId(userId).withCreatedAt(createdAt).withUpdatedAt(updatedAt);
        }

        public Device build() {
            Device device = new Device();
            device.setId(id);
            device.setModel(model);
            device.setAlias(alias);
            device.setSdkLevel(sdkLevel);
            device.setOsName(osName);
            device.setHeight(height);
            device.setWidth(width);
            device.setDpi(dpi);
            device.setDeviceId(deviceId);
            device.setPushId(pushId);
            device.setMemorySize(memorySize);
            device.setCpuType(cpuType);
            device.setToken(token);
            device.setNetworkType(networkType);
            device.setUserId(userId);
            device.setCreatedAt(createdAt);
            device.setUpdatedAt(updatedAt);
            return device;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.model);
        dest.writeString(this.alias);
        dest.writeInt(this.sdkLevel);
        dest.writeString(this.osName);
        dest.writeInt(this.height);
        dest.writeInt(this.width);
        dest.writeInt(this.dpi);
        dest.writeString(this.deviceId);
        dest.writeString(this.pushId);
        dest.writeInt(this.memorySize);
        dest.writeString(this.cpuType);
        dest.writeString(this.token);
        dest.writeString(this.networkType);
        dest.writeInt(this.userId);
        dest.writeParcelable(this.user, 0);
        dest.writeString(this.createdAt);
        dest.writeString(this.updatedAt);
    }

    protected Device(Parcel in) {
        this.id = in.readInt();
        this.model = in.readString();
        this.alias = in.readString();
        this.sdkLevel = in.readInt();
        this.osName = in.readString();
        this.height = in.readInt();
        this.width = in.readInt();
        this.dpi = in.readInt();
        this.deviceId = in.readString();
        this.pushId = in.readString();
        this.memorySize = in.readInt();
        this.cpuType = in.readString();
        this.token = in.readString();
        this.networkType = in.readString();
        this.userId = in.readInt();
        this.user = in.readParcelable(User.class.getClassLoader());
        this.createdAt = in.readString();
        this.updatedAt = in.readString();
    }

    public static final Creator<Device> CREATOR = new Creator<Device>() {
        public Device createFromParcel(Parcel source) {
            return new Device(source);
        }

        public Device[] newArray(int size) {
            return new Device[size];
        }
    };


    public static class DB {
        public static final String TABLE_NAME = "devices";

        public static final String COLUMN_ID = "model";
        public static final String COLUMN_ALIAS = "alias";
        public static final String COLUMN_SDK_LEVEL = "sdk_level";
        public static final String COLUMN_OS_NAME = "os_name";
        public static final String COLUMN_HEIGHT = "height";
        public static final String COLUMN_WIDTH = "width";
        public static final String COLUMN_DPI = "dpi";
        //TODO rename for conflict
        public static final String COLUMN_DEVICE_ID = "device_id";
        //TODO rename for conflict
        public static final String COLUMN_PUSH_ID = "push_id";
        public static final String COLUMN_MEMORY_SIZE = "memory_size";
        public static final String COLUMN_CPU_TYPE = "cpu_type";
        public static final String COLUMN_TOKEN = "token";
        public static final String COLUMN_NETWORK_TYPE = "network_type";
        public static final String COLUMN_USER_ID = "user_id";
        public static final String COLUMN_CREATED_AT = "created_at";
        public static final String COLUMN_UPDATED_AT = "updated_at";

        public static final String SQL_CREATE = ""
                + "CREATE TABLE " + TABLE_NAME + "("
                + COLUMN_ID + " INTEGER NOT NULL,"
                + COLUMN_ALIAS + " TEXT NOT NULL,"
                + COLUMN_SDK_LEVEL + " INTEGER NOT NULL,"
                + COLUMN_HEIGHT + " TEXT NOT NULL,"
                + COLUMN_WIDTH + " INTEGER NOT NULL,"
                + COLUMN_DPI + " INTEGER NOT NULL,"
                + COLUMN_DEVICE_ID + " TEXT NOT NULL,"
                + COLUMN_PUSH_ID + " TEXT NOT NULL,"
                + COLUMN_MEMORY_SIZE + " INTEGER NOT NULL,"
                + COLUMN_CPU_TYPE + " TEXT NOT NULL,"
                + COLUMN_TOKEN + " TEXT NOT NULL,"
                + COLUMN_NETWORK_TYPE + " TEXT NOT NULL,"
                + COLUMN_USER_ID + " TEXT NOT NULL,"
                + COLUMN_CREATED_AT + " TEXT NOT NULL,"
                + COLUMN_UPDATED_AT + " TEXT NOT NULL"
                + ")";

        public static final String SQL_DROP = "DROP TABLE " + TABLE_NAME;
        public static final String SQL_LIST_QUERY = "SELECT * FROM " + TABLE_NAME + " ORDER BY " + COLUMN_CREATED_AT + " DESC";
        public static final String WHERE_ID = COLUMN_ID + "= ?";
        public static final String SQL_ITEM_QUERY = "SELECT * FROM " + TABLE_NAME + " WHERE " + WHERE_ID;

        public static final Func1<SqlBrite.Query, List<Device>> MAP = new Func1<SqlBrite.Query, List<Device>>() {
            @Override
            public List<Device> call(SqlBrite.Query query) {
                Cursor cursor = query.run();
                try {
                    List<Device> values = new ArrayList<>(cursor.getCount());
                    while (cursor.moveToNext()) {
                        values.add(Device.fromCursor(cursor));
                    }
                    return values;
                } finally {
                    cursor.close();
                }
            }
        };
    }


}