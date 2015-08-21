package com.linroid.pushapp.model;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

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
public class Account implements Parcelable {
    @Expose
    private Device device;
    @Expose
    private User user;
    @Expose
    private String token;

    private File file;


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

    public boolean saveToFile() {
        Gson gson = new Gson();
        String json = gson.toJson(this);
        Writer writer = null;
        try {
            writer = new FileWriter(this.file);
            writer.write(json);
            writer.close();
        } catch (IOException e) {
            Timber.e("write auth info to file fail", e);
            return false;
        }
        return true;
    }

    public static Account readFromFile(File file) {
        Account account = null;
        try {
            Reader reader = new FileReader(file);
            Gson gson = new Gson();
            account = gson.fromJson(reader, Account.class);
        } catch (FileNotFoundException e) {
            Timber.e("read auth info from file fail", e);
        }
        return account;
    }

    @Override
    public String toString() {
        return "Account{" +
                "device=" + device +
                ", user=" + user +
                ", token='" + token + '\'' +
                ", file=" + file +
                '}';
    }

    public Account() {
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

    protected Account(Parcel in) {
        this.device = in.readParcelable(Device.class.getClassLoader());
        this.user = in.readParcelable(User.class.getClassLoader());
        this.token = in.readString();
    }

    public static final Creator<Account> CREATOR = new Creator<Account>() {
        public Account createFromParcel(Parcel source) {
            return new Account(source);
        }

        public Account[] newArray(int size) {
            return new Account[size];
        }
    };

    public void setFile(File file) {
        this.file = file;
    }

    public void invalidate() {
        this.token = null;
        this.device = null;
        this.user = null;
        saveToFile();
    }
}
