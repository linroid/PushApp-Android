package com.linroid.pushapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;

/**
 * Created by linroid on 7/25/15.
 */
public class Authorization implements Parcelable {
    @Expose
    private Device device;
    @Expose
    private User user;

    public Device getDevice() {
        return device;
    }

    public void setDevice(Device device) {
        this.device = device;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "Authoization{" +
                "device=" + device +
                ", user=" + user +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.device, 0);
        dest.writeParcelable(this.user, 0);
    }

    public Authorization() {
    }

    protected Authorization(Parcel in) {
        this.device = in.readParcelable(Device.class.getClassLoader());
        this.user = in.readParcelable(User.class.getClassLoader());
    }

    public static final Parcelable.Creator<Authorization> CREATOR = new Parcelable.Creator<Authorization>() {
        public Authorization createFromParcel(Parcel source) {
            return new Authorization(source);
        }

        public Authorization[] newArray(int size) {
            return new Authorization[size];
        }
    };
}
