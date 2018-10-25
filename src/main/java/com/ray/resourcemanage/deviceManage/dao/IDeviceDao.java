package com.ray.resourcemanage.deviceManage.dao;

import com.ray.resourcemanage.deviceManage.entity.Device;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.io.Serializable;


/**
 * @Description: 记录增删改查
 */
public interface IDeviceDao extends JpaRepository<Device, Serializable>,JpaSpecificationExecutor<Device> {

    //@Modifying
    void deleteByDeviceId(Integer deviceId);

    Device findByDeviceId(Integer deviceId);
}
