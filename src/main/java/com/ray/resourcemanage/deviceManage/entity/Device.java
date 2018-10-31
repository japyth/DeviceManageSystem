package com.ray.resourcemanage.deviceManage.entity;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "device")
public class Device {
    @Id
    @Column(name = "device_id")
    @SequenceGenerator(name = "pk_sequence", sequenceName = "s_device_id", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pk_sequence")
    private Integer deviceId;                                                                //设备编号

    @Column(name = "device_name", length = 254)
    private String deviceName;                                                            //设备名称


    @Column(name = "device_type", length = 254)
    private String deviceType;                                                            //设备类型

    @Column(name = "owner", length = 254)
    private String owner;                                                                     //拥有者

    @Column(name = "borrower", length = 254)
    private String borrower;                                                                  //归还者

    @Column(name = "is_back", columnDefinition = "BOOLEAN")
    private Boolean isBack;                                                                 //是否归还

    @Column(name = "back_time")                                                           //预计归还时间
    private Date backTime;

    @Column(name = "serial_number")
    private String serialNumber;                                                            //设备序列号

    @Column(name = "update_time")
    private Date updateTime;                                                                //更新时间

    @Column(name = "remark", columnDefinition = "TEXT")
    private String remark;                                                                  //备注

    @Column(name = "device_username", columnDefinition = "TEXT")
    private String deviceUsername;                                                      //用户名

    @Column(name = "device_pwd", columnDefinition = "TEXT")
    private String devicePwd;                                                         //密码

    @Column(name = "device_ip", columnDefinition = "TEXT")
    private String deviceIp;                                                  //设备ip

    public Date getBackTime() {
        return backTime;
    }

    public void setBackTime(Date backTime) {
        this.backTime = backTime;
    }

    public Integer getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(Integer deviceId) {
        this.deviceId = deviceId;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getBorrower() {
        return borrower;
    }

    public void setBorrower(String borrower) {
        this.borrower = borrower;
    }

    public Boolean getBack() {
        return isBack;
    }

    public void setBack(Boolean back) {
        isBack = back;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getDeviceUsername() {
        return deviceUsername;
    }

    public void setDeviceUsername(String deviceUsername) {
        this.deviceUsername = deviceUsername;
    }

    public String getDevicePwd() {
        return devicePwd;
    }

    public void setDevicePwd(String devicePwd) {
        this.devicePwd = devicePwd;
    }

    public String getDeviceIp() {
        return deviceIp;
    }

    public void setDeviceIp(String deviceIp) {
        this.deviceIp = deviceIp;
    }
}
