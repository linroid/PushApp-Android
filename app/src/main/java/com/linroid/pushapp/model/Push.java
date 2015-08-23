/*
 * Copyright (c) linroid 2015.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.linroid.pushapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by linroid on 7/26/15.
 */
public class Push implements Parcelable {

    @SerializedName("package_id")
    @Expose
    private Integer packageId;
    @SerializedName("user_id")
    @Expose
    private Integer userId;
    @Expose
    private String sendno;
    @SerializedName("msg_id")
    @Expose
    private String msgId;
    @SerializedName("is_ok")
    @Expose
    private Boolean isOk;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @Expose
    private Integer id;
    @Expose
    private User user;
    @SerializedName("package")
    @Expose
    private Pack _package;

    /**
     * @return The packageId
     */
    public Integer getPackageId() {
        return packageId;
    }

    /**
     * @param packageId The package_id
     */
    public void setPackageId(Integer packageId) {
        this.packageId = packageId;
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
     * @return The sendno
     */
    public String getSendno() {
        return sendno;
    }

    /**
     * @param sendno The sendno
     */
    public void setSendno(String sendno) {
        this.sendno = sendno;
    }

    /**
     * @return The msgId
     */
    public String getMsgId() {
        return msgId;
    }

    /**
     * @param msgId The msg_id
     */
    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    /**
     * @return The isOk
     */
    public Boolean getIsOk() {
        return isOk;
    }

    /**
     * @param isOk The is_ok
     */
    public void setIsOk(Boolean isOk) {
        this.isOk = isOk;
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
     * @return The user
     */
    public User getUser() {
        return user;
    }

    /**
     * @param user The user
     */
    public void setUser(User user) {
        this.user = user;
    }

    /**
     * @return The _package
     */
    public Pack getPackage() {
        return _package;
    }

    /**
     * @param _package The package
     */
    public void setPackage(Pack _package) {
        this._package = _package;
    }



    public static class PushBuilder {
        private Integer packageId;
        private Integer userId;
        private String sendno;
        private String msgId;
        private Boolean isOk;
        private String updatedAt;
        private String createdAt;
        private Integer id;
        private User user;
        private Pack pack;

        private PushBuilder() {
        }

        public static PushBuilder aPush() {
            return new PushBuilder();
        }

        public PushBuilder withPackageId(Integer packageId) {
            this.packageId = packageId;
            return this;
        }

        public PushBuilder withUserId(Integer userId) {
            this.userId = userId;
            return this;
        }

        public PushBuilder withSendno(String sendno) {
            this.sendno = sendno;
            return this;
        }

        public PushBuilder withMsgId(String msgId) {
            this.msgId = msgId;
            return this;
        }

        public PushBuilder withIsOk(Boolean isOk) {
            this.isOk = isOk;
            return this;
        }

        public PushBuilder withUpdatedAt(String updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        public PushBuilder withCreatedAt(String createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public PushBuilder withId(Integer id) {
            this.id = id;
            return this;
        }

        public PushBuilder withUser(User user) {
            this.user = user;
            return this;
        }

        public PushBuilder but() {
            return aPush().withPackageId(packageId).withUserId(userId).withSendno(sendno).withMsgId(msgId).withIsOk(isOk).withUpdatedAt(updatedAt).withCreatedAt(createdAt).withId(id).withUser(user);
        }

        public Push build() {
            Push push = new Push();
            push.setPackageId(packageId);
            push.setUserId(userId);
            push.setSendno(sendno);
            push.setMsgId(msgId);
            push.setIsOk(isOk);
            push.setUpdatedAt(updatedAt);
            push.setCreatedAt(createdAt);
            push.setId(id);
            push.setUser(user);
            push.setPackage(pack);
            return push;
        }

        public PushBuilder withPackage(Pack pack) {
            this.pack = pack;
            return this;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.packageId);
        dest.writeValue(this.userId);
        dest.writeString(this.sendno);
        dest.writeString(this.msgId);
        dest.writeValue(this.isOk);
        dest.writeString(this.updatedAt);
        dest.writeString(this.createdAt);
        dest.writeValue(this.id);
        dest.writeParcelable(this.user, 0);
        dest.writeParcelable(this._package, flags);
    }

    public Push() {
    }

    protected Push(Parcel in) {
        this.packageId = in.readInt();
        this.userId = (Integer) in.readValue(Integer.class.getClassLoader());
        this.sendno = in.readString();
        this.msgId = in.readString();
        this.isOk = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.updatedAt = in.readString();
        this.createdAt = in.readString();
        this.id = (Integer) in.readValue(Integer.class.getClassLoader());
        this.user = in.readParcelable(User.class.getClassLoader());
        this._package = in.readParcelable(Pack.class.getClassLoader());
    }

    public static final Parcelable.Creator<Push> CREATOR = new Parcelable.Creator<Push>() {
        public Push createFromParcel(Parcel source) {
            return new Push(source);
        }

        public Push[] newArray(int size) {
            return new Push[size];
        }
    };

    @Override
    public String toString() {
        return "Push{" +
                "packageId='" + packageId + '\'' +
                ", userId=" + userId +
                ", sendno='" + sendno + '\'' +
                ", msgId='" + msgId + '\'' +
                ", isOk=" + isOk +
                ", updatedAt='" + updatedAt + '\'' +
                ", createdAt='" + createdAt + '\'' +
                ", id=" + id +
                ", user=" + user +
                ", _package=" + _package +
                '}';
    }
}