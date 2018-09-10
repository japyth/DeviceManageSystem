package com.ray.resourcemanage.springSecurity.entity;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "sys_user")
public class SysUser implements Serializable, UserDetails {
    private static final long serialVersionUID = 7670147386796230394L;
    @Id
    @Column(name = "user_id")
    @SequenceGenerator(name = "pk_sequence", sequenceName = "s_user_id", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pk_sequence")
    private Integer userId;

    @Column(name = "user_name")
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "isHaveDevice")
    private Boolean isHaveDevice;

    @ManyToMany(cascade = {CascadeType.REFRESH},fetch = FetchType.EAGER)
    @JoinTable(
            joinColumns = {@JoinColumn(name = "user_id") },
            inverseJoinColumns = {@JoinColumn(name = "role_id") }
            )
    private Set<SysRole> sysRoleSet;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        Set<SysRole> roleSet = this.getSysRoleSet();
        for(SysRole sysRole : roleSet){
            authorities.add(new SimpleGrantedAuthority(sysRole.getRoleName()));
        }
        return authorities;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    @Override
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<SysRole> getSysRoleSet() {
        return sysRoleSet;
    }

    public void setSysRoleSet(Set<SysRole> sysRoleSet) {
        this.sysRoleSet = sysRoleSet;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public Boolean getHaveDevice() {
        return isHaveDevice;
    }

    public void setHaveDevice(Boolean haveDevice) {
        isHaveDevice = haveDevice;
    }
}
