package com.linroid.pushapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by linroid on 7/24/15.
 */
public class Device implements Parcelable {

    @Expose
    private String id;
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
    private int userId;
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
    public String getId() {
        return id;
    }

    /**
     * @param id The id
     */
    public void setId(String id) {
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
    public int getUserId() {
        return userId;
    }

    /**
     * @param userId The user_id
     */
    public void setUserId(int userId) {
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
                ", sdkLevel='" + sdkLevel + '\'' +
                ", osName='" + osName + '\'' +
                ", height=" + height +
                ", width=" + width +
                ", dpi=" + dpi +
                ", deviceId='" + deviceId + '\'' +
                ", memorySize='" + memorySize + '\'' +
                ", cpuType='" + cpuType + '\'' +
                ", token='" + token + '\'' +
                ", networkType='" + networkType + '\'' +
                ", userId='" + userId + '\'' +
                ", createdAt='" + createdAt + '\'' +
                ", updatedAt='" + updatedAt + '\'' +
                '}';
    }

    public Device() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.model);
        dest.writeString(this.alias);
        dest.writeInt(this.sdkLevel);
        dest.writeString(this.osName);
        dest.writeInt(this.height);
        dest.writeInt(this.width);
        dest.writeInt(this.dpi);
        dest.writeString(this.deviceId);
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
        this.id = in.readString();
        this.model = in.readString();
        this.alias = in.readString();
        this.sdkLevel = in.readInt();
        this.osName = in.readString();
        this.height = in.readInt();
        this.width = in.readInt();
        this.dpi = in.readInt();
        this.deviceId = in.readString();
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

    public static class DeviceBuilder {
        private String id;
        private String model;
        private String alias;
        private int sdkLevel;
        private String osName;
        private int height;
        private int width;
        private int dpi;
        private String deviceId;
        private int memorySize;
        private String cpuType;
        private String token;
        private String networkType;
        private int userId;
        private String createdAt;
        private String updatedAt;

        private DeviceBuilder() {
        }

        public static DeviceBuilder aDevice() {
            return new DeviceBuilder();
        }

        public DeviceBuilder withId(String id) {
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
}