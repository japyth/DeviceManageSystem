package com.ray.resourcemanage.logManage.dao;

import com.ray.resourcemanage.logManage.entity.LogEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.io.Serializable;

public interface ILogDao extends JpaRepository<LogEntity, Serializable>,JpaSpecificationExecutor<LogEntity> {


}
