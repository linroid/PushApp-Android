package com.linroid.pushapp.model;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

import timber.log.Timber;

/**
 * Created by linroid on 7/25/15.
 */
public class Binding implements Parcelable {
    @Expose
    private Device device;
    @Expose
    private User user;
    @Expose
    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

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


    /**
     * 判断授权是否有效
     * @return
     */
    public boolean isValid() {
        return !TextUtils.isEmpty(token);
    }

    public boolean saveToFile(Context context) {
        Gson gson = new Gson();
        String json = gson.toJson(this);
        File saveFile = new File(context.getFilesDir(), "auth.json");
        Writer writer = null;
        try {
            writer = new FileWriter(saveFile);
            writer.write(json);
            writer.close();
        } catch (IOException e) {
            Timber.e("write auth info to file fail", e);
            return false;
        }
        return true;
    }

    public static Binding readFromFile(Context context) {
        File saveFile = new File(context.getFilesDir(), "auth.json");
        Binding auth = null;
        try {
            Reader reader = new FileReader(saveFile);
            Gson gson = new Gson();
            auth = gson.fromJson(reader, Binding.class);
        } catch (FileNotFoundException e) {
            Timber.e("read auth info from file fail", e);
        }
        return auth;
    }

    @Override
    public String toString() {
        return "Binding{" +
                "device=" + device +
                ", user=" + user +
                ", token='" + token + '\'' +
                '}';
    }

    public Binding() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.device, 0);
        dest.writeParcelable(this.user, 0);
        dest.writeString(this.token);
    }

    protected Binding(Parcel in) {
        this.device = in.readParcelable(Device.class.getClassLoader());
        this.user = in.readParcelable(User.class.getClassLoader());
        this.token = in.readString();
    }

    public static final Creator<Binding> CREATOR = new Creator<Binding>() {
        public Binding createFromParcel(Parcel source) {
            return new Binding(source);
        }

        public Binding[] newArray(int size) {
            return new Binding[size];
        }
    };
}
