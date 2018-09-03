package com.ray.resourcemanage.entity;

import java.util.List;

/**
 * @ProjectName: resourcemanage
 * @address: http://www.hikvision.com
 * @Auther: jiangsong7
 * @Date: 2018/8/21 14:45
 * @Description:
 */
public class SearchResult<Object> {
    private List<Object> rows; //查询到当前页面的信息
    private Integer totalPage; //所有页面
    private Long totalCount;  //所有数据

    public List<Object> getRows() {
        return rows;
    }

    public void setRows(List<Object> rows) {
        this.rows = rows;
    }

    public Integer getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(Integer totalPage) {
        this.totalPage = totalPage;
    }

    public Long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Long totalCount) {
        this.totalCount = totalCount;
    }
}