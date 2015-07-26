package com.linroid.pushapp.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Parcel;
import android.os.Parcelable;

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
import java.util.Timer;

import timber.log.Timber;

/**
 * Created by linroid on 7/23/15.
 */
public class User implements Parcelable {

    @Expose
    private Integer id;
    @Expose
    private String nickname;
    @Expose
    private String avatar;
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
     * @return The nickname
     */
    public String getNickname() {
        return nickname;
    }

    /**
     * @param nickname The nickname
     */
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    /**
     * @return The avatar
     */
    public String getAvatar() {
        return avatar;
    }

    /**
     * @param avatar The avatar
     */
    public void setAvatar(String avatar) {
        this.avatar = avatar;
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

//
//    public void saveToPreference(SharedPreferences.Editor editor) {
//        editor.putInt("user_id", id);
//        editor.putString("nickname", nickname);
//        editor.putString("avatar", avatar);
//        editor.putString("updatedAt", updatedAt);
//        editor.putString("createdAt", createdAt);
//    }
//
//    private static User fromPreference(SharedPreferences sp) {
//        User user = new User();
//        user.setId(sp.getInt("user_id", 0));
//        user.setAvatar(sp.getString("avatar", null));
//        user.setAvatar(sp.getString("avatar", null));
//        user.setAvatar(sp.getString("avatar", null));
//        user.setAvatar(sp.getString("avatar", null));
//        return user;
//    }


    public boolean saveToFile(Context context) {
        Gson gson = new Gson();
        String json = gson.toJson(this);
        File saveFile = new File(context.getFilesDir(), "user.json");
        Writer writer = null;
        try {
            writer = new FileWriter(saveFile);
            writer.write(json);
            writer.close();
        } catch (IOException e) {
            Timber.e("write user to file fail", e);
            return false;
        }
        return true;
    }
    public static User readFromFile(Context context) {
        File saveFile = new File(context.getFilesDir(), "user.json");
        User user = null;
        try {
            Reader reader = new FileReader(saveFile);
            Gson gson = new Gson();
            user = gson.fromJson(reader, User.class);
        } catch (FileNotFoundException e) {
            Timber.e("read user from file fail", e);
        }
        return user;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", nickname='" + nickname + '\'' +
                ", avatar='" + avatar + '\'' +
                ", createdAt='" + createdAt + '\'' +
                ", updatedAt='" + updatedAt + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.nickname);
        dest.writeString(this.avatar);
        dest.writeString(this.createdAt);
        dest.writeString(this.updatedAt);
    }

    public User() {
    }

    protected User(Parcel in) {
        this.id = in.readInt();
        this.nickname = in.readString();
        this.avatar = in.readString();
        this.createdAt = in.readString();
        this.updatedAt = in.readString();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        public User createFromParcel(Parcel source) {
            return new User(source);
        }

        public User[] newArray(int size) {
            return new User[size];
        }
    };
}
