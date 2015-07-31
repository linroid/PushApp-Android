package com.linroid.pushapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by linroid on 7/25/15.
 */
public class Pagination<T> {

    @Expose
    private Integer total;
    @SerializedName("per_page")
    @Expose
    private Integer perPage;
    @SerializedName("current_page")
    @Expose
    private Integer currentPage;
    @SerializedName("last_page")
    @Expose
    private Integer lastPage;
    @SerializedName("next_page_url")
    @Expose
    private String nextPageUrl;
    @SerializedName("prev_page_url")
    @Expose
    private String prevPageUrl;
    @Expose
    private Integer from;
    @Expose
    private Integer to;
    @Expose
    private List<T> data = new ArrayList<>();

    /**
     * @return The total
     */
    public Integer getTotal() {
        return total;
    }

    /**
     * @param total The total
     */
    public void setTotal(Integer total) {
        this.total = total;
    }

    /**
     * @return The perPage
     */
    public Integer getPerPage() {
        return perPage;
    }

    /**
     * @param perPage The per_page
     */
    public void setPerPage(Integer perPage) {
        this.perPage = perPage;
    }

    /**
     * @return The currentPage
     */
    public Integer getCurrentPage() {
        return currentPage;
    }

    /**
     * @param currentPage The current_page
     */
    public void setCurrentPage(Integer currentPage) {
        this.currentPage = currentPage;
    }

    /**
     * @return The lastPage
     */
    public Integer getLastPage() {
        return lastPage;
    }

    /**
     * @param lastPage The last_page
     */
    public void setLastPage(Integer lastPage) {
        this.lastPage = lastPage;
    }

    /**
     * @return The nextPageUrl
     */
    public Object getNextPageUrl() {
        return nextPageUrl;
    }

    /**
     * @param nextPageUrl The next_page_url
     */
    public void setNextPageUrl(String nextPageUrl) {
        this.nextPageUrl = nextPageUrl;
    }

    /**
     * @return The prevPageUrl
     */
    public Object getPrevPageUrl() {
        return prevPageUrl;
    }

    /**
     * @param prevPageUrl The prev_page_url
     */
    public void setPrevPageUrl(String prevPageUrl) {
        this.prevPageUrl = prevPageUrl;
    }

    /**
     * @return The from
     */
    public Integer getFrom() {
        return from;
    }

    /**
     * @param from The from
     */
    public void setFrom(Integer from) {
        this.from = from;
    }

    /**
     * @return The to
     */
    public Integer getTo() {
        return to;
    }

    /**
     * @param to The to
     */
    public void setTo(Integer to) {
        this.to = to;
    }

    /**
     * @return The data
     */
    public List<T> getData() {
        return data;
    }

    /**
     * @param data The data
     */
    public void setData(List<T> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "Pagination{" +
                "total=" + total +
                ", perPage=" + perPage +
                ", currentPage=" + currentPage +
                ", lastPage=" + lastPage +
                ", nextPageUrl='" + nextPageUrl + '\'' +
                ", prevPageUrl='" + prevPageUrl + '\'' +
                ", from=" + from +
                ", to=" + to +
                ", data=" + data +
                '}';
    }
}