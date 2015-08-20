package com.linroid.pushapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by linroid on 8/20/15.
 */
public class Token implements Parcelable {

    @Expose
    private String value;
    @SerializedName("expire_in")
    @Expose
    private String expireIn;
    @Expose
    private Integer owner;
    @Expose
    private String type;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @Expose
    private Integer id;

    /**
     *
     * @return
     * The value
     */
    public String getValue() {
        return value;
    }

    /**
     *
     * @param value
     * The value
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     *
     * @return
     * The expireIn
     */
    public String getExpireIn() {
        return expireIn;
    }

    /**
     *
     * @param expireIn
     * The expire_in
     */
    public void setExpireIn(String expireIn) {
        this.expireIn = expireIn;
    }

    /**
     *
     * @return
     * The owner
     */
    public Integer getOwner() {
        return owner;
    }

    /**
     *
     * @param owner
     * The owner
     */
    public void setOwner(Integer owner) {
        this.owner = owner;
    }

    /**
     *
     * @return
     * The type
     */
    public String getType() {
        return type;
    }

    /**
     *
     * @param type
     * The type
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     *
     * @return
     * The updatedAt
     */
    public String getUpdatedAt() {
        return updatedAt;
    }

    /**
     *
     * @param updatedAt
     * The updated_at
     */
    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    /**
     *
     * @return
     * The createdAt
     */
    public String getCreatedAt() {
        return createdAt;
    }

    /**
     *
     * @param createdAt
     * The created_at
     */
    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    /**
     *
     * @return
     * The id
     */
    public Integer getId() {
        return id;
    }

    /**
     *
     * @param id
     * The id
     */
    public void setId(Integer id) {
        this.id = id;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.value);
        dest.writeString(this.expireIn);
        dest.writeValue(this.owner);
        dest.writeString(this.type);
        dest.writeString(this.updatedAt);
        dest.writeString(this.createdAt);
        dest.writeValue(this.id);
    }

    public Token() {
    }

    protected Token(Parcel in) {
        this.value = in.readString();
        this.expireIn = in.readString();
        this.owner = (Integer) in.readValue(Integer.class.getClassLoader());
        this.type = in.readString();
        this.updatedAt = in.readString();
        this.createdAt = in.readString();
        this.id = (Integer) in.readValue(Integer.class.getClassLoader());
    }

    public static final Parcelable.Creator<Token> CREATOR = new Parcelable.Creator<Token>() {
        public Token createFromParcel(Parcel source) {
            return new Token(source);
        }

        public Token[] newArray(int size) {
            return new Token[size];
        }
    };
}