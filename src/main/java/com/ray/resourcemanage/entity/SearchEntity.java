package com.ray.resourcemanage.entity;


import java.util.Date;
import java.util.Map;

/**
 * @Description:
 */
public class SearchEntity {
    private Integer pageIndex;
    private Integer pageSize;
    private Date startTime;
    private Date endTime;
    private String searchValue;
    private Map<String, Object> searchOther; //其他的一些查询参数

    public Integer getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(Integer pageIndex) {
        this.pageIndex = pageIndex;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getSearchValue() {
        return searchValue;
    }

    public void setSearchValue(String searchValue) {
        this.searchValue = searchValue;
    }

    public Map<String, Object> getSearchOther() {
        return searchOther;
    }

    public void setSearchOther(Map<String, Object> searchOther) {
        this.searchOther = searchOther;
    }
}