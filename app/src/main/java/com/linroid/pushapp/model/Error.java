package com.linroid.pushapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by linroid on 7/24/15.
 */
public class Error {

    @Expose
    private String message;
    @SerializedName("status_code")
    @Expose
    private Integer statusCode;

    /**
     * @return The message
     */
    public String getMessage() {
        return message;
    }

    /**
     * @param message The message
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * @return The statusCode
     */
    public Integer getStatusCode() {
        return statusCode;
    }

    /**
     * @param statusCode The status_code
     */
    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    @Override
    public String toString() {
        return "Error{" +
                "message='" + message + '\'' +
                ", statusCode=" + statusCode +
                '}';
    }
}