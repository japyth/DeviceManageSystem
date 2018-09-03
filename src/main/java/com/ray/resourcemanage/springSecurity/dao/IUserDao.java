package com.ray.resourcemanage.springSecurity.dao;

import com.ray.resourcemanage.springSecurity.entity.SysUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.io.Serializable;

public interface IUserDao extends JpaRepository<SysUser, Serializable>,JpaSpecificationExecutor<SysUser> {
    SysUser findByUsername(String username);
}
