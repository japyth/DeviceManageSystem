package com.ray.resourcemanage.userManage.dao;

import com.ray.resourcemanage.springSecurity.entity.SysUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.io.Serializable;

public interface IUserManageDao extends JpaRepository<SysUser, Serializable>,JpaSpecificationExecutor<SysUser> {
}
