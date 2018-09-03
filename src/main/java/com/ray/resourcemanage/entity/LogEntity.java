package com.ray.resourcemanage.entity;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "log")
public class LogEntity {

    @Id
    @Column(name = "log_id")
    @SequenceGenerator(name = "pk_sequence", sequenceName = "s_log_id", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pk_sequence")
    private Integer logId;                                                                //日志id

    @Column(name = "remark", columnDefinition = "TEXT")
    private String remark;                                                                  //日志具体内容

    @Column(name = "update_time")
    private Date updateTime;                                                                //更新时间

    @Column(name = "ip")
    private String ip;                      //操作ip

    @Column(name = "type")                  //操作类型
    private String type;

    @Column(name = "content")
    private String content;                     //操作内容


    public Integer getLogId() {
        return logId;
    }

    public void setLogId(Integer logId) {
        this.logId = logId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
