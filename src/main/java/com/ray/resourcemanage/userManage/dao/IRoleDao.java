package com.ray.resourcemanage.userManage.dao;

import com.ray.resourcemanage.userManage.bean.SysRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.io.Serializable;

public interface IRoleDao  extends JpaRepository<SysRole, Serializable>, JpaSpecificationExecutor<SysRole> {
    SysRole findByRoleName(String roleName);
}
