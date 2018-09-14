package com.ray.resourcemanage.userManage.bean;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@Entity
@Table(name = "sys_role")
public class SysRole implements Serializable {
    private static final long serialVersionUID = 8732087703528287081L;
    @Id
    @Column(name = "role_id")
    @SequenceGenerator(name = "pk_sequence", sequenceName = "s_role_id", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pk_sequence")
    private Integer roleId;
    @Column(name = "role_name")
    private String roleName;

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

}
