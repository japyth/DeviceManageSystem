package com.ray.resourcemanage.dao;

import com.ray.resourcemanage.entity.Device;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.io.Serializable;
import java.util.List;

/**
 * @ProjectName: resourcemanage
 * @address: http://www.hikvision.com
 * @Auther: jiangsong7
 * @Date: 2018/8/2 16:55
 * @Description: 记录增删改查
 */
public interface IDeviceDao extends JpaRepository<Device, Serializable>,JpaSpecificationExecutor<Device> {

    //@Modifying
    void deleteByDeviceId(Integer deviceId);

    Device findByDeviceId(Integer deviceId);
}
