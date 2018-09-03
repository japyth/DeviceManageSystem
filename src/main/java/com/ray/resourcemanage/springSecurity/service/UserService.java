package com.ray.resourcemanage.springSecurity.service;

import com.ray.resourcemanage.springSecurity.dao.IUserDao;
import com.ray.resourcemanage.springSecurity.entity.SysUser;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {
    @Autowired
    private IUserDao userDao;
    private Log log = LogFactory.getLog(this.getClass());
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        SysUser sysUser = userDao.findByUsername(username);
        if(sysUser == null) {
            log.error("未查询到用户"+username+"的信息");
        }
        return sysUser;
    }
}
